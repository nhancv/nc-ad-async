package com.nhancv.async;

import android.os.Handler;
import android.os.Message;
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

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String tickMsg = msg.obj.toString();
                tvMsg.setText(tickMsg);
            }
        };

        new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                //get message from MainThread
                Message msg = handler.obtainMessage();
                //update message
                msg.obj = "Tick  " + i;
                //return message back to MainThread
                handler.sendMessage(msg);
                SystemClock.sleep(1000);
            }
        }).start();

    }


}
