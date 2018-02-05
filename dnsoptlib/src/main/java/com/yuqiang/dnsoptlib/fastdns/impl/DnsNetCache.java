package com.yuqiang.dnsoptlib.fastdns.impl;

import com.yuqiang.dnsoptlib.fastdns.IDnsCache;

/**
 * Created by yuqiang on 2018/2/5.
 */

public class DnsNetCache implements IDnsCache {

    @Override public String lookupIpByHostName(String hostName) {
        // TODO: 2018/2/5 采用第三方DNS解析服务
        // TODO: 更新MemoryCache & DiskCache
        return null;
    }
}
