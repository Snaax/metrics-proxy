package com.ariadnext.metricsproxy.adapter.prometheus

import com.ariadnext.metricsproxy.adapter.IMetricsAdapter
import com.ariadnext.metricsproxy.bean.HostTypeEnum
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
            var index = 1
            prometheusResponse.body?.data?.result?.forEach {
                response.hosts
                    .find { host ->
                        when (host.type) {
                            HostTypeEnum.COMMON -> host.instance == it.metric.instance
                            HostTypeEnum.K8S -> host.instance == it.metric.k8sInstance
                        }
                    } ?.let { host ->
                        when (host.type) {
                            HostTypeEnum.K8S -> it.metric.podName?.let { _ ->
                                host.pods.add(QueryResponse.PodResponse(it.metric.podName, it.metric.exportedInstance!!.split(":")[1], it.value[1].toString()))
                            }
                            HostTypeEnum.COMMON -> host
                        }
                    } ?: run {
                        val mappedResponse = when (it.metric.job) {
                            "k8s" -> if (it.metric.k8sInstance != null) createHostResponseFromK8s(index.toString(), it) else null
                            else -> createHostResponseFromCommon(index.toString(), it)
                        }

                        if (mappedResponse != null) {
                            response.hosts.add(mappedResponse)
                        }
                    }

                ++index
            }

            return response
        }

        return null
    }

    private fun createHostResponseFromCommon(index: String, it: PrometheusQueryResponse.Data.Result): QueryResponse.HostResponse {
        return QueryResponse.HostResponse(
            id = index,
            instance = it.metric.instance,
            job = it.metric.job,
            status = it.value[1].toString(),
            type = HostTypeEnum.COMMON
        )
    }

    private fun createHostResponseFromK8s(index: String, it: PrometheusQueryResponse.Data.Result): QueryResponse.HostResponse {
        val mappedResponse = QueryResponse.HostResponse(
            id = index,
            job = it.metric.job,
            component = it.metric.k8sComponent,
            instance = it.metric.k8sInstance,
            managedBy = it.metric.k8sManagedBy,
            name = it.metric.k8sName,
            partOf = it.metric.k8sPartOf,
            version = it.metric.k8sVersion,
            type = HostTypeEnum.K8S
        )

        it.metric.podName?.let { _ -> mappedResponse.pods.add(QueryResponse.PodResponse(it.metric.podName, it.metric.exportedInstance!!.split(":")[1], it.value[1].toString())) }

        return mappedResponse
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