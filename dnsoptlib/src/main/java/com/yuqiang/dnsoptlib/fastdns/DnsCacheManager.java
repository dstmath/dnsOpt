package com.yuqiang.dnsoptlib.fastdns;

import android.text.TextUtils;
import com.yuqiang.dnsoptlib.fastdns.impl.DnsDiskCache;
import com.yuqiang.dnsoptlib.fastdns.impl.DnsMemoryCache;
import com.yuqiang.dnsoptlib.fastdns.impl.DnsNetCache;
import java.util.LinkedList;

/**
 * Created by yuqiang on 2018/2/5.
 */

public final class DnsCacheManager implements IDnsCache {

    private static final String TAG = "DnsCacheManager";
    private final LinkedList<IDnsCache> caches;

    private DnsCacheManager(){
        caches = new LinkedList<>();
        caches.add(new DnsMemoryCache());
        caches.add(new DnsDiskCache());
        caches.add(new DnsNetCache());
    }

    @Override public String lookupIpByHostName(String hostName) {
        String ip = null;
        for (IDnsCache dnsCache : caches) {
            ip = dnsCache.lookupIpByHostName(hostName);
            if (!TextUtils.isEmpty(ip)) {
                return ip;
            }
        }
        return ip;
    }

    /**
     * Holder for Singleton
     */
    private static final class DnsCacheManagerHolder {
        private static final DnsCacheManager INSTANCE = new DnsCacheManager();
    }

    public static DnsCacheManager getInstance() {
        return DnsCacheManagerHolder.INSTANCE;
    }

}
