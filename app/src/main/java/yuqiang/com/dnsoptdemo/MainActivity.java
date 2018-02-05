package yuqiang.com.dnsoptdemo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import com.yuqiang.dnsoptlib.util.NetAddressUtil;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final Executor EXECUTOR = Executors.newSingleThreadExecutor();

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .show();
            }
        });

        dnsTest("www.baidu.com");
        dnsTest("www.test.com");

    }

    private void dnsTest(final String hostName) {
        EXECUTOR.execute(new Runnable() {
            @Override public void run() {
                try {
                    long start = System.currentTimeMillis();
                    InetAddress[] inetAddresses = InetAddress.getAllByName(hostName);
                    for (InetAddress inetAddress : inetAddresses) {
                        Log.e(TAG, "-----inetAddress------" + String.valueOf(inetAddress));
                    }
                    long end = System.currentTimeMillis();
                    Log.e(TAG, hostName + "----DNS--Cost--Time-----------" + (end - start));
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
