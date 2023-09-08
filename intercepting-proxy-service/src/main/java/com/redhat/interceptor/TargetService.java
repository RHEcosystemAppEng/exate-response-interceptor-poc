package com.redhat.interceptor;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

@ConfigMapping(prefix = "target.server")
public interface TargetService {
    int port();

    @WithDefault("localhost")
    String host();
}
