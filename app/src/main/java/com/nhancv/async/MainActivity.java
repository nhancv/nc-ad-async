package com.nhancv.async;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledThreadPoolExecutor;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @ViewById(R.id.tvMsg)
    TextView tvMsg;

    @AfterViews
    void init() {
        new TickWithAsyncTask().execute();
//        new TickWithAsyncTask().execute();
//        new TickWithAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    /**
     * Update text
     *
     * @param tick
     */
    private void updateText(String tick) {
        tvMsg.setText(tick);
    }

    private class TickWithAsyncTask extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            updateText("Start");
        }

        @Override
        protected String doInBackground(Void... voids) {
            //Delay random for test execute tasks in parallel
            SystemClock.sleep(Math.abs(new Random().nextInt(3000)));
            for (int i = 0; i < 5; i++) {
                publishProgress(i);
                SystemClock.sleep(1000);
            }
            return "Done";
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            updateText(String.valueOf(progress[0]));
        }

        @Override
        protected void onPostExecute(String result) {
            updateText(result);
        }
    }

}
