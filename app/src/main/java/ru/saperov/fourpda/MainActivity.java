package ru.saperov.fourpda;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "myLogs";
    private static DatabaseHelper mDatabaseHelper;
    public static ArrayList<String> lstTitle = new ArrayList<String>();
    public static String dscNews="";
    public static String srcImgNews;
    private ProgressBar spinner;
    private String httpATask="http://4pda.ru/";
    private String menuSelect = "news";
    private int cntScrollTitle=1;
    public static String articleHref=null;
   // TextView tvTitle;
    //public static int posTitleClick;
    public List<DbWrapper> lstWrapperNews;

    ArrayList<ModelTitle> arrayOfTitles;
    CustomTitlesAdapter adapter;
    ListView lvTitles;

    ArrayList<ModelNews> arrayOfNews;
    CustomNewsAdapter newsAdapter;
    ListView lvNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("4PDA Новости");

        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        spinner.setVisibility(View.GONE);
        lvTitles = (ListView) findViewById(R.id.lvTitles);
        lvTitles.setOnItemClickListener(itemTitleClickListener);
        lvNews = (ListView) findViewById(R.id.lvNews);
        lvNews.setOnItemClickListener(itemNewsClickListener);

        //проверим достигнут ли конец списка
        lvTitles.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                Log.d(TAG, "scrollState = " + scrollState);
                if(lvTitles.getLastVisiblePosition()==arrayOfTitles.size() - 1){
                    cntScrollTitle++;
                    Log.d(TAG, httpATask + menuSelect + "/page/" + cntScrollTitle);
                    new GetAsincTask().execute(httpATask + menuSelect + "/page/" + cntScrollTitle);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        mDatabaseHelper = new DatabaseHelper(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateNewsList();

    }

    private void populateNewsList() {
        Log.d(TAG, "------------------//////////////////--------------------------");
        if(isOnline()) {
            mDatabaseHelper.delAllNews();
            new GetAsincTask().execute(httpATask + menuSelect);
        } else {
            showNewsFromDBase();
        }

    }

    public class GetAsincTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            spinner.setVisibility(View.VISIBLE);
            //listView.setVisibility(View.GONE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            Document document = null;
            String title = null;
            String href = null;
            String desc = null;
            String srcImg = null;
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
                      //  Log.d(TAG, "href=" + eLinks.attr("href").toString());
                       // Log.d(TAG, "title=" + eLinks.attr("title").toString());
                       // Log.d(TAG, "desc=" + eDescription.select("p").text());
                       // Log.d(TAG, "srcImg=" + eVisual.select("img").attr("src"));
                        title = eLinks.attr("title").toString();
                        href = eLinks.attr("href").toString();
                        desc = eDescription.select("p").text();
                        srcImg = eVisual.select("img").attr("src");
                        mDatabaseHelper.addNews(new DbWrapper(title, href, desc, srcImg));
                    }
                    //title = myElement.text();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return title;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            spinner.setVisibility(View.GONE);
            showNewsFromDBase();

        }
    }

    public void showNewsFromDBase(){
        if(mDatabaseHelper.getNewsCount()>0){
            lstTitle.clear();
            lstWrapperNews = mDatabaseHelper.getAllNews();
            for (int i=0; i<lstWrapperNews.size(); i++){
                lstTitle.add(lstWrapperNews.get(i).getTitle());
                Log.d(TAG, i+" lstTitle=" + lstWrapperNews.get(i).getTitle());
            }
            dscNews = lstWrapperNews.get(1).getDesc();
            srcImgNews = lstWrapperNews.get(1).getSrcImg();
            articleHref = lstWrapperNews.get(1).getHref();
           // populating data into a lvTitles
            arrayOfTitles = ModelTitle.getTitles();
            adapter = new CustomTitlesAdapter(this, arrayOfTitles);
         //   lvTitles = (ListView) findViewById(R.id.lvTitles);
            lvTitles.setAdapter(adapter);

            //populating data into lvNews
            arrayOfNews = ModelNews.getNoNews();
            newsAdapter = new CustomNewsAdapter(this, arrayOfNews);
            //lvNews = (ListView) findViewById(R.id.lvNews);
            lvNews.setAdapter(newsAdapter);
        } else {
            Toast.makeText(getApplicationContext(), "News are missing!", Toast.LENGTH_SHORT).show();
        }

    }

    //обрабатываем нажатие на списке заголовков
    protected AdapterView.OnItemClickListener itemTitleClickListener = new AdapterView.OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            view.setSelected(true);
            dscNews = lstWrapperNews.get(position).getDesc();
            srcImgNews = lstWrapperNews.get(position).getSrcImg();
            articleHref = lstWrapperNews.get(position).getHref();
            //populating data into lvNews
            arrayOfNews = ModelNews.getNoNews();
            newsAdapter = new CustomNewsAdapter(getApplicationContext(), arrayOfNews);
            lvNews = (ListView) findViewById(R.id.lvNews);
            lvNews.setAdapter(newsAdapter);
        }
    };
//обрабатываем нажатие на списке новостей
    protected AdapterView.OnItemClickListener itemNewsClickListener =new AdapterView.OnItemClickListener(){

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(new Intent(getApplicationContext(), fullArticle.class));
    }
};


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

        switch (id){
            case R.id.action_news: {
                setTitle("4PDA Новости");
                menuSelect = "news";
                populateNewsList();
            }
                break;
            case R.id.action_articles: {
                setTitle("4PDA Статьи");
                menuSelect = "articles";
                populateNewsList();
            }
                break;
            case R.id.action_software: {
                setTitle("4PDA Программы");
                menuSelect = "software";
                populateNewsList();
            }
                break;
            case R.id.action_games: {
                setTitle("4PDA Игры");
                menuSelect = "games";
                populateNewsList();
            }
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    //проверяем есть ли Internet соединение
    boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cm.getActiveNetworkInfo();
        if (nInfo != null && nInfo.isConnected()) {
            return true;
        }
        else {
            return false;
        }
    }
}
