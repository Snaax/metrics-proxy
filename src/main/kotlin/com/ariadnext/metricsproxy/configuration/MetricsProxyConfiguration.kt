package com.ariadnext.metricsproxy.configuration

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.DefaultUriBuilderFactory


@Configuration
@EnableConfigurationProperties(MetricsProxyProperties::class)
class MetricsProxyConfiguration(
    val properties: MetricsProxyProperties
) {

    @Bean
    fun metricsRestTemplate(restTemplateBuilder: RestTemplateBuilder): RestTemplate {
        val uriBuilderFactory = DefaultUriBuilderFactory("${ if (properties.server.tls.enabled) "https" else "http" }://${properties.server.host}:${properties.server.port}${properties.server.basePath}")

        return restTemplateBuilder
            .uriTemplateHandler(uriBuilderFactory)
            .build()
    }
}