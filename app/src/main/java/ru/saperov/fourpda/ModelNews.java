package ru.saperov.fourpda;

import java.util.ArrayList;

/**
 * Created by saperov on 26.03.16.
 */
public class ModelNews {
    public String myNews;
    static ArrayList<ModelNews> newsArrayList;

    public ModelNews(String myNews) {
        this.myNews=myNews;
    }

    public static ArrayList<ModelNews> getNoNews(){
        newsArrayList = new ArrayList<ModelNews>();
        newsArrayList.add(new ModelNews("No news1"));
        newsArrayList.add(new ModelNews("No news2"));
        return newsArrayList;
    }
}
