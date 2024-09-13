package com.amdose.base.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
public class RestTemplateConfig {

    private @Value("${restTemplate.timeout.connection}")
    int connectionTimeout;
    private @Value("${restTemplate.timeout.socket}")
    int socketTimeout;
    private @Value("${restTemplate.timeout.read}")
    int readTimeout;
    private @Value("${restTemplate.http-client-max.perroute}")
    int httpClientMaxPerRoute;
    private @Value("${restTemplate.http-client-max.total}")
    int httpClientMaxTotal;

    @Bean
    public RestTemplate restTemplate() {
        log.info("initializing RestTemplate...");

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(connectionTimeout);
        requestFactory.setHttpClient(this.poolingHttpClientConnectionManager());

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(requestFactory);
        return restTemplate;
    }

    private CloseableHttpClient poolingHttpClientConnectionManager() {
        PoolingHttpClientConnectionManager result = new PoolingHttpClientConnectionManager();
        result.setDefaultMaxPerRoute(httpClientMaxPerRoute);
        result.setMaxTotal(httpClientMaxTotal);

        ConnectionConfig connConfig = ConnectionConfig.custom()
                .setConnectTimeout(connectionTimeout, TimeUnit.MILLISECONDS)
                .setSocketTimeout(socketTimeout, TimeUnit.MILLISECONDS)
                .build();

        result.setDefaultConnectionConfig(connConfig);
        return HttpClients.custom().setConnectionManager(result).build();
    }
}
