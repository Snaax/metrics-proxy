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
                val instance: String,
                val job: String,
                @JsonAlias("app_kubernetes_io_component")
                val k8sComponent: String?,
                @JsonAlias("app_kubernetes_io_instance")
                val k8sInstance: String?,
                @JsonAlias("app_kubernetes_io_managed_by")
                val k8sManagedBy: String?,
                @JsonAlias("app_kubernetes_io_name")
                val k8sName: String?,
                @JsonAlias("app_kubernetes_io_part_of")
                val k8sPartOf: String?,
                @JsonAlias("app_kubernetes_io_version")
                val k8sVersion: String?,
                @JsonAlias("exported_instance")
                val exportedInstance: String?,
                @JsonAlias("exported_job")
                val exportedJob: String?,
                val namespace: String?,
                val app: String?,
                @JsonAlias("pod_name")
                val podName: String?,
                @JsonAlias("pod_template_hash")
                val podTemplateHash: String?,
                @JsonAlias("security_istio_io_tlsMode")
                val istioTlsMode: String?,
                @JsonAlias("service_istio_io_canonical_name")
                val istioCanonicalName: String?,
                @JsonAlias("service_istio_io_canonical_revision")
                val istioCanonicalRevision: String?,
                val version: String?
            )
        }
    }
}