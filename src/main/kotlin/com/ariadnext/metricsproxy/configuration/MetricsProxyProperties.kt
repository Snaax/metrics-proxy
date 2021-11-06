package com.ariadnext.metricsproxy.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("metrics")
data class MetricsProxyProperties(
    val server:Server
) {
    data class Server(
        val host: String,
        val port: Int,
        val username: String,
        val password: String,
        val basePath: String,
        val tls: Tls
    ) {
        data class Tls(
            val enabled: Boolean
        )
    }
}
