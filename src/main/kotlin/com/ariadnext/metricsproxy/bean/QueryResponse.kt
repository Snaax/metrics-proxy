package com.ariadnext.metricsproxy.bean

data class QueryResponse(
    var hosts: MutableList<HostResponse> = mutableListOf()
) {
    data class HostResponse(
        val id: String,
        val job: String,
        val component: String? = null,
        val instance: String? = null,
        val managedBy: String? = null,
        val name: String? = null,
        val partOf: String? = null,
        val pods: MutableList<PodResponse> = mutableListOf(),
        val version: String? = null,
        val status: String? = null,
        val type: HostTypeEnum
    )

    data class PodResponse(
        val name: String,
        val port: String,
        val status: String
    )
}