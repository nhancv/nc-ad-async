package com.nhancv.async;

import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @ViewById(R.id.tvMsg)
    TextView tvMsg;

    private Handler handler;

    @AfterViews
    void init() {
        handler = new Handler();

        new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                int finalI = i;
                handler.post(() -> {
                    tvMsg.setText(String.format("Tick  %s", finalI));
                });
                SystemClock.sleep(1000);
            }
        }).start();

    }


}
