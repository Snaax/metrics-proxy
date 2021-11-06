package com.ariadnext.metricsproxy.controller

import com.ariadnext.metricsproxy.bean.QueryResponse
import com.ariadnext.metricsproxy.bean.TargetsResponse
import com.ariadnext.metricsproxy.service.MetricsProxyService
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("")
@CrossOrigin("*")
class MetricsProxyController(
    val service: MetricsProxyService
) {

    @GetMapping("/metrics/{metricName}", produces = ["application/json"])
    fun getMetrics(@PathVariable metricName: String): QueryResponse? {
        return service.getMetrics(metricName)
    }

    @GetMapping("/targets", produces = ["application/json"])
    fun getTargets(): TargetsResponse? {
        return service.getTargets()
    }
}