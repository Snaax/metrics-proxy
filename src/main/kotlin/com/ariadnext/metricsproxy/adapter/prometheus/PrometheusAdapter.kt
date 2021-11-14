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
                response.hosts
                    .find { host ->
                        when (host.job) {
                            "k8s" -> (host as QueryResponse.K8sHostResponse).k8sInstance == it.metric.k8sInstance
                            else -> (host as QueryResponse.CommonHostResponse).instance == it.metric.instance
                        }
                    }
                    ?.let { host ->
                        when (host.job) {
                            "k8s" -> it.metric.podName?.let { _ ->
                                (host as QueryResponse.K8sHostResponse).pods.add(QueryResponse.K8sHostResponse.PodResponse(it.metric.podName, it.metric.exportedInstance!!.split(":")[1], it.value[1].toString()))
                            }
                            else -> (host as QueryResponse.CommonHostResponse)
                        }
                    }
                    ?: run {
                        val mappedResponse = when (it.metric.job) {
                            "k8s" -> if (it.metric.k8sInstance != null) createK8sHostResponse(it) else null
                            else -> createCommonHostResponse(it)
                        }

                        if (mappedResponse != null) {
                            response.hosts.add(mappedResponse)
                        }
                    }
            }

            return response
        }

        return null
    }

    private fun createCommonHostResponse(it: PrometheusQueryResponse.Data.Result): QueryResponse.IHostResponse {
        return QueryResponse.CommonHostResponse(
            instance = it.metric.instance,
            job = it.metric.job,
            metricValue = it.value[1].toString()
        )
    }

    private fun createK8sHostResponse(it: PrometheusQueryResponse.Data.Result): QueryResponse.IHostResponse {
        val mappedResponse = QueryResponse.K8sHostResponse(
            instance = it.metric.instance,
            job = it.metric.job,
            k8sComponent = it.metric.k8sComponent,
            k8sInstance = it.metric.k8sInstance,
            k8sManagedBy = it.metric.k8sManagedBy,
            k8sName = it.metric.k8sName,
            k8sPartOf = it.metric.k8sPartOf,
            k8sVersion = it.metric.k8sVersion,
            version = it.metric.version
        )

        it.metric.podName?.let { _ -> mappedResponse.pods.add(QueryResponse.K8sHostResponse.PodResponse(it.metric.podName, it.metric.exportedInstance!!.split(":")[1], it.value[1].toString())) }

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