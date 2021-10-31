package com.ariadnext.metricsproxy

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MetricsProxyApplication

fun main(args: Array<String>) {
	runApplication<MetricsProxyApplication>(*args)
}
