package com.nhancv.async;

import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.gson.annotations.SerializedName;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import bolts.Continuation;
import bolts.Task;
import bolts.TaskCompletionSource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @ViewById(R.id.tvMsg)
    TextView tvMsg;

    @AfterViews
    void init() {
        fetchGithub();
    }

    /**
     * Update text
     *
     * @param txt
     */
    private void updateText(String txt) {
        tvMsg.setText(txt);
    }

    /**
     * Fetch repo list on github
     */
    private void fetchGithub() {

        Task<Void> task = taskGitHub()
                .continueWith(new Continuation<List<Repo>, Void>() {
                    @Override
                    public Void then(Task<List<Repo>> task) {
                        if (!task.isFaulted() && !task.isCancelled()) {
                            List<Repo> repoList = task.getResult();
                            updateText("Done task1: " + repoList.size());
                        }
                        return null;
                    }
                }, Task.UI_THREAD_EXECUTOR);

        Task<Void> task2 = Task.callInBackground(() -> {
            for (int i = 0; i < 5; i++) {
                int finalI = i;
                runOnUiThread(() -> {
                    updateText(String.valueOf(finalI));
                });
            }
            return "Done task2";
        }).continueWith(new Continuation<String, Void>() {
            @Override
            public Void then(Task<String> task) {
                if (!task.isFaulted() && !task.isCancelled()) {
                    updateText(task.getResult());
                }
                return null;
            }
        }, Task.UI_THREAD_EXECUTOR);

        ArrayList<Task<Void>> tasks = new ArrayList<>();
        tasks.add(task);
        tasks.add(task2);

        Task.whenAll(tasks).continueWith(task1 -> {
            MainActivity.this.updateText("DoneAll");
            return null;
        }, Task.UI_THREAD_EXECUTOR);
    }

    /**
     * Create task which fetch repo list on github with bolts
     *
     * @return
     */
    private Task<List<Repo>> taskGitHub() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GitHubService service = retrofit.create(GitHubService.class);
        Call<List<Repo>> body = service.listRepos("nhancv");

        final TaskCompletionSource<List<Repo>> tcs = new TaskCompletionSource<>();
        body.enqueue(new Callback<List<Repo>>() {
            @Override
            public void onResponse(Call<List<Repo>> call, Response<List<Repo>> response) {
                tcs.setResult(response.body());
            }

            @Override
            public void onFailure(Call<List<Repo>> call, Throwable t) {
                tcs.setError((Exception) t);
            }
        });
        return tcs.getTask();
    }

    public interface GitHubService {
        @GET("users/{user}/repos")
        Call<List<Repo>> listRepos(@Path("user") String user);
    }

    class Repo {
        @SerializedName("id")
        String id;
    }


}
