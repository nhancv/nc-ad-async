package com.nhancv.async;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import bolts.Continuation;
import bolts.Task;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @ViewById(R.id.tvMsg)
    TextView tvMsg;

    @AfterViews
    void init() {
        tickWithBolts();
    }

    private void updateText(String tick) {
        tvMsg.setText(tick);
    }

    private void tickWithBolts() {
        Task.callInBackground(() -> {
            for (int i = 0; i < 5; i++) {
                int finalI = i;
                runOnUiThread(() -> {
                    updateText(String.valueOf(finalI));
                });
                SystemClock.sleep(1000);
            }
            return "Done";
        }).continueWith(new Continuation<String, Void>() {
            @Override
            public Void then(Task<String> task) {
                if (!task.isFaulted() && !task.isCancelled()) {
                    updateText(task.getResult());
                }
                return null;
            }
        }, Task.UI_THREAD_EXECUTOR);
    }


}
