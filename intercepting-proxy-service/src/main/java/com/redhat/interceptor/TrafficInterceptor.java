package com.redhat.interceptor;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.interceptor.gator.*;
import io.quarkus.vertx.web.Route;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.RequestOptions;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.WebClient;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class TrafficInterceptor {
    private final static Logger LOG = Logger.getLogger(TrafficInterceptor.class.getName());

    private final WebClient client;
    private final ObjectMapper mapper;
    private final TargetConfig target;
    private final ApiConfig config;

    @Inject
    public TrafficInterceptor(WebClient client, ObjectMapper mapper, TargetConfig target, ApiConfig config) {
        this.client = client;
        this.mapper = mapper;
        this.target = target;
        this.config = config;
    }

    @PostConstruct
    void initialize() {
        this.mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    }

    @Route(regex = ".*")
    public void handle(RoutingContext rc) {
        LOG.fine(() -> String.format("got new request, %s", rc.request().absoluteURI()));
        // create target request options using origin request and target host and port
        var targetRequestOpts = new RequestOptions()
            .setHost(this.target.host())
            .setPort(this.target.port())
            .setHeaders(rc.request().headers())
            .setURI(rc.request().uri());
        // wrap target request
        var targetRequest = this.client.request(rc.request().method(), targetRequestOpts);
        LOG.info("proxying request to target");
        targetRequest.sendBuffer(rc.body().buffer()).onSuccess(targetResponse -> {
            LOG.info("proxying request to target successful");
            // create token request options
            var tokenReqOpts = new RequestOptions()
                .setHost(config.host())
                .setPort(config.port())
                .setURI(config.tokenUri())
                .putHeader(RequestHeaders.X_Api_Key.toString(), config.apiKey())
                .putHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
            // build token request payload
            var tokenReqPayload = new TokenPayload(config.clientId(), config.clientSecret(), config.grantType());
            // create token request
            var tokenRequest = client.request(HttpMethod.POST, tokenReqOpts);
            LOG.info("sending token request to gator");
            tokenRequest
                .ssl(true)
                .sendBuffer(Buffer.buffer(tokenReqPayload.toString()))
                .onSuccess(tokenResponse -> {
                    LOG.info("sending token request to gator successful");
                    TokenResponse parsedTokenResp = null;
                    try {
                        parsedTokenResp = mapper.readValue(tokenResponse.bodyAsString(), TokenResponse.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    // create dataset request options
                    var datasetReqOpts = new RequestOptions()
                        .setHost(config.host())
                        .setPort(config.port())
                        .setURI(config.datasetUri())
                        .putHeader(RequestHeaders.X_Api_Key.toString(), config.apiKey())
                        .putHeader(
                            RequestHeaders.X_Resource_Token.toString(),
                            String.format("%s %s", parsedTokenResp.token_type(), parsedTokenResp.access_token()))
                        .putHeader(RequestHeaders.X_Data_Set_Type.toString(), DatasetPayload.DatasetType.JSON.toString())
                        .putHeader(HttpHeaders.CONTENT_TYPE, "application/json");
                    // build dataset request payload
                    var datasetReqPayload = new DatasetPayload(
                        config.manifestName(),
                        config.jobType(),
                        config.thirdPartyName().isPresent() || config.thirdPartyId().isPresent()
                            ? new DatasetPayload.ThirdPartyIdentiferPayload(
                                config.thirdPartyName().orElse(null), config.thirdPartyId().orElse(null))
                            : null,
                        ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT),
                        config.dataOwningCountryCode().orElse(null),
                        config.countryCode(),
                        config.dataUsageId().orElse(null),
                        config.protectNullValues(),
                        config.restrictedText().orElse(null),
                        targetResponse.bodyAsString(),
                        config.preserveStringLength(),
                        config.sqlType().orElse(null),
                        config.classificationModel().orElse(null));
                    // create dataset request
                    var datasetRequest = this.client.request(HttpMethod.POST, datasetReqOpts);
                    LOG.info("sending dataset request to gator");
                    try {
                        datasetRequest
                            .ssl(true)
                            .sendBuffer(Buffer.buffer(mapper.writeValueAsString(datasetReqPayload)))
                            .onSuccess(datasetResponse -> {
                                LOG.info("sending dataset request to gator successful");
                                DatasetResponse parsedDatasetResp = null;
                                try {
                                    parsedDatasetResp = mapper.readValue(datasetResponse.bodyAsString(), DatasetResponse.class);
                                } catch (JsonProcessingException e) {
                                    throw new RuntimeException(e);
                                }
                                LOG.info("returning gator dataset as response");
                                rc.response().end(parsedDatasetResp.dataSet());
                                LOG.info("returning gator dataset as response successful");
                            }
                        ).onFailure(t -> {
                            LOG.severe("failed intercepting response with gator");
                            LOG.log(Level.FINE, t, t::getMessage);
                        });
                    } catch (JsonProcessingException e) {
                        LOG.severe("failed parsing gator request payload");
                        LOG.log(Level.FINE, e, e::getMessage);
                    }
                }
            ).onFailure(t -> {
                LOG.severe("failed fetching token from gator");
                LOG.log(Level.FINE, t, t::getMessage);
            });

        }).onFailure(t -> {
            LOG.severe("failed proxying request to target");
            LOG.log(Level.FINE, t, t::getMessage);
        });
    }
}
