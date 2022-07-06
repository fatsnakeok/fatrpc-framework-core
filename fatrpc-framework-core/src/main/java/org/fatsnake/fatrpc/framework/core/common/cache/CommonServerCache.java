package org.fatsnake.fatrpc.framework.core.common.cache;

import org.fatsnake.fatrpc.framework.core.registy.URL;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Auther: fatsnake
 * @Description":
 * @Date:2022/7/3 4:00 下午
 * Copyright (c) 2022, zaodao All Rights Reserved.
 */
public class CommonServerCache {
    public  static final Map<String, Object> PROVIDER_CLASS_MAP = new HashMap<>();
    public static final Set<URL> PROVIDER_URL_SET = new HashMap<>();
}
