package com.nhancv.async;

import android.support.v7.app.AppCompatActivity;
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

    private void updateText(String tick) {
        tvMsg.setText(tick);
    }

    private void rxSample() {

        Observable.defer(() -> Observable.just(1, 2, 3, 4, 5, 6))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(integer -> integer > 4) //5, 6
                .map(integer -> integer * 2) //10, 12
                .flatMap(integer -> Observable.range(integer, 3)) // 10, 11, 12, 12, 13, 14
                .subscribe(integer -> {
                    updateText(tvMsg.getText() + " " + integer);
                });
    }

}
