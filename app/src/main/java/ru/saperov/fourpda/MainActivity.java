package ru.saperov.fourpda;

import android.os.AsyncTask;
import android.os.Bundle;
import android.renderscript.Element;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "myLogs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Log.d(TAG, "------------------//////////////////--------------------------");
        new GetAsincTask().execute("http://4pda.ru/");
    }

    public class GetAsincTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            Document document = null;
            String title = null;
            try {
                // Соединяемся с адресом и получаем документ
                document = Jsoup.connect(params[0]).userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.154 Safari/537.36").get();
                // org.jsoup.nodes.Element myDiv = document.select("article.post").first();
                Elements myArticles = document.select("article");
                for(org.jsoup.nodes.Element myElement : myArticles){
                    Elements eVisual = myElement.select("div.visual");
                    Elements eDescription = myElement.select("div.description");

                    if(eDescription.text().toString().length()>0) {
                        Elements eLinks = eDescription.select("a");

                      //  Log.d(TAG, "myElement=" + myElement.html());
                      //  Log.d(TAG, "eVisual=" + eVisual.html());
                      //  Log.d(TAG, "eDescription=" + eDescription.html());
                        Log.d(TAG, "href=" + eLinks.attr("href"));
                        Log.d(TAG, "title=" + eLinks.attr("title"));
                        Log.d(TAG, "p=" + eDescription.select("p").text());
                        Log.d(TAG, "srcImg=" + eVisual.select("img").attr("src"));
                    }
                    //title = myElement.text();
                }
                //title = myDiv.text();
            } catch (IOException e) {
                e.printStackTrace();
            }
           // title = document.title(); // получаем заголовок страницы
            return title;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
          //  mTextView.setText(result);
            Log.d(TAG, "result=" + result);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
