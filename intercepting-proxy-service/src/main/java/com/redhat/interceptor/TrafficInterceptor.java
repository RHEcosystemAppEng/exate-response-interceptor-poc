package com.redhat.interceptor;

import io.quarkus.vertx.web.Route;
import io.quarkus.vertx.web.RoutingExchange;
import io.vertx.core.Vertx;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.logging.Logger;

public class TrafficInterceptor {
    private final static Logger LOG = Logger.getLogger(TrafficInterceptor.class.getName());

    private final Vertx vertx;

    @Inject
    public TrafficInterceptor(Vertx vertx) {
        this.vertx = vertx;
    }
    @ConfigProperty(name = "target.server.port")
    int targetPort;

    @Route(regex = ".*", methods = {Route.HttpMethod.GET, Route.HttpMethod.POST, Route.HttpMethod.PUT})
    public void handle(RoutingExchange re) {
        LOG.info("wip");
        re.context().response().end("this is a wip");
    }
}
