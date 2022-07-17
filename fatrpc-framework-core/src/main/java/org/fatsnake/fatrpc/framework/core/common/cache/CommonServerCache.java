package org.fatsnake.fatrpc.framework.core.common.cache;

import org.fatsnake.fatrpc.framework.core.common.config.ServerConfig;
import org.fatsnake.fatrpc.framework.core.filter.server.ServerFilterChain;
import org.fatsnake.fatrpc.framework.core.registy.RegistryService;
import org.fatsnake.fatrpc.framework.core.registy.URL;
import org.fatsnake.fatrpc.framework.core.serialize.SerializeFactory;
import org.fatsnake.fatrpc.framework.core.server.ServiceWrapper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Auther: fatsnake
 * @Description":
 * @Date:2022/7/3 4:00 下午
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public class CommonServerCache {
    public  static final Map<String, Object> PROVIDER_CLASS_MAP = new HashMap<>();
    public static final Set<URL> PROVIDER_URL_SET = new HashSet<>();
    public static RegistryService REGISTRY_SERVICE;
    public static SerializeFactory SERVER_SERIALIZE_FACTORY;
    public static ServerConfig SERVER_CONFIG;
    // 服务端过滤器链
    public static ServerFilterChain SERVER_FILTER_CHAIN;

    public  static final Map<String, ServiceWrapper> PROVIDER_SERVICE_WRAPPER_MAP = new ConcurrentHashMap();
}
