package com.ariadnext.metricsproxy.adapter.prometheus

import com.fasterxml.jackson.annotation.JsonAlias

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
            val health: String,
            val scrapeInterval: String,
            val scrapeTimeout: String
        ) {
            data class DiscoveredLabels(
                @JsonAlias("__address__")
                val address: String,
                @JsonAlias("__metrics_path__")
                val path: String,
                @JsonAlias("__scheme__")
                val scheme: String,
                @JsonAlias("__scrape_interval__")
                val scrapeInterval: String,
                @JsonAlias("__scrape_timeout__")
                val scrapeTimeout: String,
                val job: String
            )

            data class Labels(
                val instance: String,
                val job: String
            )
        }
    }
}