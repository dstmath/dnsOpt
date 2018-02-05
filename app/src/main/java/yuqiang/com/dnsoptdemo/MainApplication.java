package yuqiang.com.dnsoptdemo;

import android.app.Application;
import android.content.Context;
import com.yuqiang.dnsoptlib.util.NetAddressUtil;

/**
 * Created by yuqiang on 2018/2/5.
 */

public class MainApplication extends Application {

    @Override protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        NetAddressUtil.dnsCacheInject();
    }
}
