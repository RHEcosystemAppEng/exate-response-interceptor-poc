package com.redhat.interceptor;

import io.quarkus.vertx.web.RouteFilter;
import io.vertx.ext.web.RoutingContext;

import java.util.logging.Logger;

public class RouteFilters {
    private final static Logger LOG = Logger.getLogger(RouteFilters.class.getName());

    @RouteFilter(0)
    void chainApiGator(RoutingContext rc) {
        LOG.info("wip");
        rc.response().putHeader("my_custom_header", "filtered_in");
        rc.next();
    }
}
