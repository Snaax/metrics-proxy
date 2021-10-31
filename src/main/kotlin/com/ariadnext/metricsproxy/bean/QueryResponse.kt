package com.ariadnext.metricsproxy.bean

data class QueryResponse(
    var hosts: MutableList<HostUpstateResponse> = mutableListOf()
) {
    data class HostUpstateResponse(
        val area: String?,
        val id: String?,
        val instance: String,
        val state: String
    )
}