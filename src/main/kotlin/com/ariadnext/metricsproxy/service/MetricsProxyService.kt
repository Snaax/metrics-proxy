package com.ariadnext.metricsproxy.service

import com.ariadnext.metricsproxy.adapter.IMetricsAdapter
import com.ariadnext.metricsproxy.bean.QueryResponse
import com.ariadnext.metricsproxy.bean.TargetsResponse
import org.springframework.stereotype.Service

@Service
class MetricsProxyService(
    val metricsAdapter: IMetricsAdapter
) {

    fun getMetrics(metricName: String, id: String?): QueryResponse? {
        val metrics = metricsAdapter.getMetrics(metricName)

        if(metrics != null && !id.isNullOrBlank()) {
            metrics.hosts.removeIf { it.id != id }
        }

        return metrics
    }

    fun getTargets(): TargetsResponse? {
        return metricsAdapter.getTargets()
    }
}
