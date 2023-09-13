package com.redhat.interceptor.factories;

import com.redhat.interceptor.TargetConfig;
import com.redhat.interceptor.gator.ApiConfig;
import com.redhat.interceptor.gator.DatasetPayload;
import com.redhat.interceptor.gator.RequestHeaders;
import com.redhat.interceptor.gator.TokenResponse;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.RequestOptions;

public class RequestOptionsFactory {
    private RequestOptionsFactory() {}

    public static RequestOptions createTargetOptions(TargetConfig target, HttpServerRequest originRequest) {
        return new RequestOptions()
            .setHost(target.host())
            .setPort(target.port())
            .setHeaders(originRequest.headers())
            .setURI(originRequest.uri());
    }

    public static RequestOptions createTokenOptions(ApiConfig config) {
        return new RequestOptions()
            .setHost(config.host())
            .setPort(config.port())
            .setURI(config.tokenUri())
            .putHeader(RequestHeaders.X_Api_Key.toString(), config.apiKey())
            .putHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
    }

    public static RequestOptions createDatasetOptions(ApiConfig config, TokenResponse tokenResponse) {
        return new RequestOptions()
            .setHost(config.host())
            .setPort(config.port())
            .setURI(config.datasetUri())
            .putHeader(RequestHeaders.X_Api_Key.toString(), config.apiKey())
            .putHeader(
                RequestHeaders.X_Resource_Token.toString(),
                String.format("%s %s", tokenResponse.token_type(), tokenResponse.access_token()))
            .putHeader(RequestHeaders.X_Data_Set_Type.toString(), DatasetPayload.DatasetType.JSON.toString())
            .putHeader(HttpHeaders.CONTENT_TYPE, "application/json");
    }
}
