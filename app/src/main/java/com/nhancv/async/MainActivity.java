package com.nhancv.async;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @ViewById(R.id.tvMsg)
    TextView tvMsg;

    @AfterViews
    void init() {
        rxSample();
    }

    /**
     * Update text
     *
     * @param tick
     */
    private void updateText(String tick) {
        tvMsg.setText(tick);
    }

    /**
     * Some samples with rxandroid
     */
    private void rxSample() {

        Observable.defer(() -> Observable.just(1, 2, 3, 4, 5, 6))
                .flatMap(integer -> {
                    Log.e(TAG, "rxSample: emit error 0");
                    return Observable.error(new Exception("error 0"));
                })
                .doOnSubscribe(() -> {
                    Log.e(TAG, "rxSample: begin");
                })
                .doOnError(throwable -> {
                    Log.e(TAG, "rxSample: do on error 1 " + throwable);
                })
                .doOnNext(integer -> {
                    Log.e(TAG, "rxSample: next " + integer);
                })
                .doOnCompleted(() -> {
                    Log.e(TAG, "rxSample: completed");
                })
                .doOnTerminate(() -> {
                    Log.e(TAG, "rxSample: terminate");
                })
                .onErrorResumeNext(throwable -> {
                    Log.e(TAG, "rxSample: emit error 1");
                    return Observable.error(new Exception("error 1"));
                })
                .doOnError(throwable -> {
                    Log.e(TAG, "rxSample: do on error 2 " + throwable);
                })
                .onErrorResumeNext(throwable -> {
                    Log.e(TAG, "rxSample: emit error 2");
                    return Observable.error(new Exception("error 2"));
                })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(integer -> {
                    Log.e(TAG, "rxSample: " + integer);
                }, throwable -> {
                    Log.e(TAG, "rxSample: error final " + throwable);
                });

    }

}
