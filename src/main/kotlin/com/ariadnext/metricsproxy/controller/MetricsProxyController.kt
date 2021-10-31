package com.ariadnext.metricsproxy.controller

import com.ariadnext.metricsproxy.bean.QueryResponse
import com.ariadnext.metricsproxy.bean.TargetsResponse
import com.ariadnext.metricsproxy.service.MetricsProxyService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("")
class MetricsProxyController(
    val service: MetricsProxyService
) {

    @GetMapping("/metrics", produces = ["application/json"])
    fun getMetrics(@RequestParam(required = true) metricName: String): QueryResponse? {
        return service.getMetrics(metricName)
    }

    @GetMapping("/targets", produces = ["application/json"])
    fun getTargets(): TargetsResponse? {
        return service.getTargets()
    }
}