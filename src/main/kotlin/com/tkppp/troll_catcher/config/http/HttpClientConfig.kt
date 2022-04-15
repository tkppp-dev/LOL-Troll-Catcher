package com.tkppp.troll_catcher.config.http

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.web.client.RestTemplate

@Configuration
class HttpClientConfig {

    @Value("\${riot.access-key}")
    private lateinit var accessKey: String

    @Bean
    fun getRestTemplate(): RestTemplate =
        RestTemplate()

    @Bean
    fun getHttpEntity(): HttpEntity<String> {
        val header = HttpHeaders()
        header["X-RIOT-TOKEN"] = accessKey
        return HttpEntity("", header)
    }
}