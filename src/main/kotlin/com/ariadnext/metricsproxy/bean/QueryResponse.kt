package com.ariadnext.metricsproxy.bean

data class QueryResponse(
    var hosts: MutableList<IHostResponse> = mutableListOf()
) {
    interface IHostResponse {
        val job: String
    }

    data class CommonHostResponse(
        override val job: String,
        val instance: String,
        val metricValue: String,
    ): IHostResponse

    data class K8sHostResponse(
        override val job: String,
        val instance: String,
        val k8sComponent: String? = null,
        val k8sInstance: String? = null,
        val k8sManagedBy: String? = null,
        val k8sName: String? = null,
        val k8sPartOf: String? = null,
        val k8sVersion: String? = null,
        val pods: MutableList<PodResponse> = mutableListOf(),
        val version: String? = null
    ): IHostResponse {
        data class PodResponse(
            val name: String,
            val port: String,
            val status: String
        )
    }
}