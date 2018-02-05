package com.yuqiang.dnsoptlib.fastdns.impl;

import com.yuqiang.dnsoptlib.fastdns.IDnsCache;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yuqiang on 2018/2/5.
 */

public class DnsMemoryCache implements IDnsCache {

    private static final Map<String, String> CACHE_MAP;

    static {
        CACHE_MAP = new HashMap<>();
        CACHE_MAP.put("www.test.com", "1.1.1.1");
    }

    @Override public String lookupIpByHostName(String hostName) {
        return CACHE_MAP.get(hostName);
    }
}
