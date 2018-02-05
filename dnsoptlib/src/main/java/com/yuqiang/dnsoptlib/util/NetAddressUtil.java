package com.yuqiang.dnsoptlib.util;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import com.yuqiang.dnsoptlib.fastdns.DnsCacheManager;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedHashMap;

/**
 * Created by yuqiang on 2018/2/5.
 */

public final class NetAddressUtil {

    private static final String TAG = "NetAddressUtil";

    /**
     * HOOK系统DNS解析的API
     */
    public static void dnsCacheInject() {
        Class clazz = createInetAddress();
        if (clazz != null) {
            Object addressCacheInstance = ReflectUtils.getStaticFieldInstance(clazz, "addressCache");
            Object cacheInstance = ReflectUtils.getInstanceFieldInstance(addressCacheInstance, "cache");
            ReflectUtils.injectFieldInstance(cacheInstance, "map", new NDSIpLruCache());
        }
    }
    /**
     * 创建InetAddress
     * 需要适配各种版本 目前适配的版本24以及以下的
     * 大于24的版本需要读源代码 目前看到的7.1的源代码跟7.0的一样
     * @return
     */
    private static Class createInetAddress() {
        Class clazz = null;
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {
            return clazz;
        }
        try {
            clazz = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ? Class.forName("java.net.Inet6AddressImpl") :
                    InetAddress.class;
        } catch (ClassNotFoundException e) {

        }
        return clazz;
    }

    /**
     * DNSIpLruCache
     * AddressCache --->
     * BasicLruCache<AddressCacheKey, AddressCacheEntry> cache
     * AddressCacheKey      -----> {@link DNSCacheKey}      对应AddressCacheKey(hostname,netId)
     * AddressCacheEntry    -----> {@link DNSCacheEntry}    构造AddressCacheEntry的实例 默认2s过期 用的时候构造绕过2s过期的检验
     * BasicLruCache {LinkedHashMap<K, V> map} 内部是Map实现LruCache
     * 源码路径
     * https://github.com/google/j2objc/blob/master/jre_emul/android/libcore/luni/src/main/java/libcore/util/BasicLruCache.java
     */
    static class NDSIpLruCache extends LinkedHashMap {

        public Object get(Object key) {
            Object result = null;
            DNSCacheKey dnsCacheKey = DNSCacheKey.getInstance(key);
            String ip = DnsCacheManager.getInstance().lookupIpByHostName(dnsCacheKey.hostname);
            Log.e(TAG, dnsCacheKey.hostname + " hostname ip " + ip);
            if (!TextUtils.isEmpty(ip)) { // parse Ip ---> InetAddress
                //dns host->ip www.baidu.com/220.181.112.244
                //dns host->ip www.baidu.com/220.181.111.188
                //存在解析出俩个IP地址的情况 所有这里要用数组
                result = DNSCacheEntry.getInstance(new String[]{ip});
            }
            return result;
        }
    }

    /**
     * AddressCacheKey
     * {@link java.net.AddressCache$AddressCacheKey} //hide api
     */
    private static class DNSCacheKey {
        private static final String HOST_NAME = "mHostname";
        private static final String NET_ID = "mNetId";

        public String hostname;
        public int netId;

        public static DNSCacheKey getInstance(Object key) {
            DNSCacheKey dnsCacheKey = new DNSCacheKey();
            if (key instanceof String) {
                dnsCacheKey.hostname = (String) key;
            } else {
                dnsCacheKey.init(key);
            }
            return dnsCacheKey;
        }

        private void init(Object key) {
            hostname = (String) ReflectUtils.getInstanceFieldInstance(key, HOST_NAME);
            netId = (int) ReflectUtils.getInstanceFieldInstance(key, NET_ID);
        }
    }

    /**
     * AddressCacheEntry
     * {@link java.net.AddressCache$AddressCacheEntry} //hide api
     * 系统NDS的过期时间是2s
     * 从缓存中获取的时候 通过反射创建AddressCacheEntry 使得缓存永不过期
     */
    private static class DNSCacheEntry {
        private static final String REFLECT_NAME = "java.net.AddressCache$AddressCacheEntry";
        /**
         * AddressCacheEntry
         * @return
         */
        public static Object getInstance(String[] ips) {
            if (ips == null || ips.length == 0) {
                Log.e(TAG, "ip cache is null go system dns");
                return null;
            }
            int len = ips.length;
            InetAddress[] addresses = new InetAddress[len];
            try {
                for (int i = 0; i < len; i++) {
                    addresses[i] = InetAddress.getByName(ips[i]);
                }
            } catch (UnknownHostException e) {
                Log.e(TAG, Log.getStackTraceString(e));
            }
            Log.e(TAG, "DNSCache hit hostname ip = " + ips[0] + ",,,ips Size " + ips.length);
            return ReflectUtils.invokeConstructor(REFLECT_NAME, (Object) addresses);
        }
    }
}
