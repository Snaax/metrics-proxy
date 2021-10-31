package com.ariadnext.metricsproxy.adapter.prometheus

import com.fasterxml.jackson.annotation.JsonAlias

data class PrometheusQueryResponse(
    val status: String,
    val data: Data
) {
    data class Data(
        val resultType: String,
        val result: List<Result>
    ) {
        data class Result(
            val metric: Metric,
            val value: List<Any>
        ) {
            data class Metric(
                @JsonAlias("__name__")
                val name: String,
                val area: String?,
                val id: String?,
                val instance: String,
                val job: String
            )
        }
    }
}