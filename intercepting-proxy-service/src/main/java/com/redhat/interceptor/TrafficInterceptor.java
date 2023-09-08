package com.redhat.interceptor;

import io.quarkus.vertx.web.Route;
import io.vertx.core.http.RequestOptions;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.WebClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.logging.Logger;

@ApplicationScoped
public class TrafficInterceptor {
    private final static Logger LOG = Logger.getLogger(TrafficInterceptor.class.getName());

    private final WebClient client;
    private final TargetService target;

    @Inject
    public TrafficInterceptor(WebClient client, TargetService target) {
        this.client = client;
        this.target = target;
    }

    @Route(regex = ".*")
    public void handle(RoutingContext rc) {
        LOG.fine(() -> String.format("got new request, %s", rc.request().absoluteURI()));
        // create request options using origin request and target host and port
        var opts = new RequestOptions()
            .setHost(this.target.host())
            .setPort(this.target.port())
            .setHeaders(rc.request().headers())
            .setURI(rc.request().uri());
        // build request
        var req = this.client.request(rc.request().method(), opts);
        LOG.info("proxying request to target");
        // send request to target service using original request body
        req.sendBuffer(rc.body().buffer()).onSuccess(resp -> {
            LOG.info("proxying request to target successful");
            rc.response().end(resp.bodyAsBuffer());
        }).onFailure(t -> {
            LOG.severe("failed proxying request to target");
            t.printStackTrace();
        });
    }
}
