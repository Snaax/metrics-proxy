package com.ariadnext.metricsproxy.adapter

import com.ariadnext.metricsproxy.bean.QueryResponse
import com.ariadnext.metricsproxy.bean.TargetsResponse

interface IMetricsAdapter {
    fun getMetrics(metricName: String): QueryResponse?

    fun getTargets(): TargetsResponse?
}