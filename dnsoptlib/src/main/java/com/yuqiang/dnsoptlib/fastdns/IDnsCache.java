package com.yuqiang.dnsoptlib.fastdns;

/**
 * Created by yuqiang on 2018/2/5.
 */

public interface IDnsCache {
    /**
     * 检索host对应的ip
     * @param hostName      域名
     * @return              ip地址
     */
    String lookupIpByHostName(String hostName);
}
