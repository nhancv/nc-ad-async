package com.nhancv.async;

import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.gson.annotations.SerializedName;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
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
        fetchGithub();
    }

    private void updateText(String tick) {
        tvMsg.setText(tick);
    }

    private void fetchGithub() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GitHubService service = retrofit.create(GitHubService.class);

        Observable.defer(() -> service.listRepos("nhancv"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(repoList -> {
                    updateText("Done: " + repoList.size());
                });
    }

    public interface GitHubService {
        @GET("users/{user}/repos")
        Observable<List<Repo>> listRepos(@Path("user") String user);
    }

    class Repo {
        @SerializedName("id")
        String id;
    }



}
