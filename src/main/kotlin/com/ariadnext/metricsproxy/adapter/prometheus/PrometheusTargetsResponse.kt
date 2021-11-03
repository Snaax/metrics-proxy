package com.ariadnext.metricsproxy.adapter.prometheus

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonInclude

data class PrometheusTargetsResponse(
    val status: String,
    val data: Data
) {
    data class Data(
        val activeTargets: List<ActiveTargets> = listOf(),
        val droppedTargets: List<ActiveTargets> = listOf()
    ) {
        data class ActiveTargets(
            val discoveredLabels: DiscoveredLabels,
            val labels: Labels,
            val scrapePool: String,
            val scrapeUrl: String,
            val globalUrl: String,
            val lastError: String,
            val lastScrape: String,
            val lastScrapeDuration: String,
            val health: String
        ) {
            data class DiscoveredLabels(
                @JsonAlias("__address__")
                val address: String,
                @JsonAlias("__metrics_path__")
                val path: String,
                @JsonAlias("__scheme__")
                val scheme: String,
                val application: String? = null,
                val job: String
            )

            data class Labels(
                val application: String? = null,
                val instance: String,
                val job: String
            )
        }
    }
}