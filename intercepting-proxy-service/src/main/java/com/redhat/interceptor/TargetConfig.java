package com.redhat.interceptor;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "target.server")
public interface TargetConfig {
    int port();

    String host();
}