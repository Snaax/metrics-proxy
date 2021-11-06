package com.ariadnext.metricsproxy.configuration

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile


@Configuration
@Profile("dev-mock")
class MockConfiguration(
    val metricsProxyProperties: MetricsProxyProperties
) {

    @Bean
    fun wireMockServer() {
        val wireMockServer = WireMockServer(WireMockConfiguration.options().port(metricsProxyProperties.server.port))
        wireMockServer.start()
        configureFor("localhost", metricsProxyProperties.server.port);
        stubFor(get("/api/v1/query?query=up").willReturn(okJson({}.javaClass.getResource("/mock/mock-query-up.json").readText())))
        stubFor(get("/api/v1/targets").willReturn(okJson({}.javaClass.getResource("/mock/mock-targets.json").readText())))
    }
}