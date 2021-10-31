package com.ariadnext.metricsproxy.adapter.prometheus

import com.ariadnext.metricsproxy.adapter.IMetricsAdapter
import com.ariadnext.metricsproxy.bean.QueryResponse
import com.ariadnext.metricsproxy.bean.TargetsResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate


@Service
class PrometheusAdapter(
    val prometheusRestTemplate: RestTemplate
) : IMetricsAdapter {

    companion object {
        const val QUERY_BASE_PATH = "api/v1/query?query={query}"
        const val TARGETS_BASE_PATH = "api/v1/targets"
    }

    override fun getMetrics(metricName: String): QueryResponse? {
        val prometheusResponse = executeQuery(metricName)
        if (HttpStatus.OK == prometheusResponse.statusCode) {
            val response = QueryResponse()
            prometheusResponse.body?.data?.result?.forEach {
                val mappedResponse = QueryResponse.HostUpstateResponse(
                    area = it.metric.area,
                    id = it.metric.id,
                    instance = it.metric.instance,
                    state = it.value[1].toString(),
                )

                response.hosts.add(mappedResponse)
            }

            return response
        }

        return null
    }

    override fun getTargets(): TargetsResponse? {
        val prometheusResponse = executeTargets()

        if (HttpStatus.OK == prometheusResponse.statusCode) {
            val response = TargetsResponse()

            prometheusResponse.body?.data?.activeTargets?.forEach {
                response.targets.add(it.labels.instance)
            }

            return response
        }

        return null
    }

    fun executeQuery(metricName: String): ResponseEntity<PrometheusQueryResponse> {
        val params: MutableMap<String, String> = HashMap()
        params["query"] = metricName

        return prometheusRestTemplate.getForEntity(QUERY_BASE_PATH, PrometheusQueryResponse::class.java, params)
    }

    fun executeTargets(): ResponseEntity<PrometheusTargetsResponse> {
        return prometheusRestTemplate.getForEntity(TARGETS_BASE_PATH, PrometheusTargetsResponse::class.java)
    }
}