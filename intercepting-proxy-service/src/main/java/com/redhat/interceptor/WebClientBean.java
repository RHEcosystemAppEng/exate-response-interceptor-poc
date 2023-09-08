package com.redhat.interceptor;

import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import jakarta.enterprise.inject.Produces;

public class WebClientBean {
    @Produces
    public WebClient createClient(Vertx vertx) {
        return WebClient.create(vertx);
    }
}
