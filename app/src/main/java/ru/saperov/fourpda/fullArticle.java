package ru.saperov.fourpda;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class fullArticle extends AppCompatActivity {
    private static final String TAG = "myLogs";
    private WebView mWebView;
    private String htmlText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_article);

        mWebView = (WebView) findViewById(R.id.webView);
        htmlText = "<html><body>Loading, please wait ... </body></html>";
        // включаем поддержку JavaScript
        mWebView.getSettings().setJavaScriptEnabled(true);
        // указываем страницу загрузки
        //mWebView.loadUrl("http://pchelka.teleknot.ru");
        mWebView.loadDataWithBaseURL(null, htmlText, "text/html", "en_US", null);
        loadArticle();
        mWebView.setWebViewClient(new MyWebViewClient());
    }

    private class MyWebViewClient extends WebViewClient
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            view.loadUrl(url);
            return true;
        }
    }

    public void loadArticle(){
        new GetAsincTask().execute(MainActivity.articleHref);
        //htmlText = "<!DOCTYPE html><html lang=\"ru-RU\"><body>"+htmlText +"</body></html>";
//        mWebView.loadDataWithBaseURL(null, htmlText, "text/html", "en_US", null);
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
                Elements myArticles = document.select("div.container");
                for(org.jsoup.nodes.Element myElement : myArticles){
                    htmlText = "<!DOCTYPE html><html lang=\"ru-RU\"><body>"+myElement.html()+"</body></html>";
                    Log.d(TAG, "htmlText=" + htmlText);
                }


            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
           // spinner.setVisibility(View.GONE);
            mWebView.loadDataWithBaseURL(null, htmlText, "text/html", "en_US", null);

        }
    }
}
