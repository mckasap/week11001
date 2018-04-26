package org.kasapbasi.week11001;

import android.app.DownloadManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    TextView tv;
    TextView tv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.textView);
        tv2 = (TextView) findViewById(R.id.textView2);

        MyAsyncTask asc = new MyAsyncTask();
        asc.execute("https://reqres.in/api/users/2");

    }

    public void btnClick(View v) {

        OkHttpClient client = new OkHttpClient();
        Request.Builder builder = new Request.Builder();

        builder.url("https://reqres.in/api/users/2");

        Request req = builder.build();

        try {
            client.newCall(req).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    call.cancel();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    final String str = response.body().string();
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv2.setText(str);

                        }
                    });
                }
            });


        } catch (Exception e) {

            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            Log.d("Exception>>", e.getMessage());

        }

    }

    public class MyAsyncTask extends AsyncTask<String, String, String> {

        OkHttpClient client = new OkHttpClient();

        @Override
        protected String doInBackground(String... strings) {

            Request.Builder myBuilder = new Request.Builder();
            myBuilder.url(strings[0]);
            Request req = myBuilder.build();


            try {

                Response res = client.newCall(req).execute();
                return res.body().string();
            } catch (Exception e) {

                return e.getMessage().toString();

            }

        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }


        // Final anahtarı kullanılması Thread Safe erişim için gerekli
        @Override
        protected void onPostExecute(final String s) {
            super.onPostExecute(s);

            //runOnUIThread kodu main activity içinde erişilen TextView da değişiklik
            //yapabilmek için eklenmiştir.
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tv.setText(s);
                }
            });

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }
}
