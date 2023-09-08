package com.redhat.interceptor;

import io.quarkus.vertx.web.Route;
import io.quarkus.vertx.web.RouteFilter;
import io.quarkus.vertx.web.RoutingExchange;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.RequestOptions;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.WebClient;
import io.vertx.mutiny.core.Vertx;

import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import java.util.logging.Logger;

public class TrafficInterceptor {
    private final static Logger LOG = Logger.getLogger(TrafficInterceptor.class.getName());

    @ConfigProperty(name = "target.server.port")
    int targetPort;

    @Route(regex = ".*", type = Route.HandlerType.BLOCKING, methods = {Route.HttpMethod.GET, Route.HttpMethod.POST, Route.HttpMethod.PUT})
    public void handle(RoutingExchange re) throws InterruptedException {
        LOG.info("wip");
        var client = WebClient.create(re.context().vertx());
        var req = client.request(re.request().method(), this.targetPort, "localhost", re.request().uri());
        req.send().onSuccess(resp -> {
            re.response().end(resp.bodyAsBuffer());
        });
    }

    @RouteFilter(0)
    void myResponseFilter(RoutingContext rc) {
        rc.response().putHeader("my_custom_header", "filtered_in");
        rc.next();
    }
}
