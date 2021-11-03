package com.ariadnext.metricsproxy.bean

data class QueryResponse(
    var hosts: MutableList<InstanceResponse> = mutableListOf()
) {
    data class InstanceResponse(
        val instance: String,
        val job: String,
        val metricStatus: Map<String, String>,
        val k8sComponent: String? = null,
        val k8sInstance: String? = null,
        val k8sManagedBy: String? = null,
        val k8sName: String? = null,
        val k8sPartOf: String? = null,
        val k8sVersion: String? = null,
        val podName: String? = null,
        val version: String? = null
    )
}