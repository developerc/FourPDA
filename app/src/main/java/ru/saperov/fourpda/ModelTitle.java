package ru.saperov.fourpda;

import java.util.ArrayList;

/**
 * Created by saperov on 24.03.16.
 */
public class ModelTitle {
    public String myTitle;
    static ArrayList<ModelTitle> titles;

    public ModelTitle(String myTitle){
        this.myTitle=myTitle;
    }

    public static ArrayList<ModelTitle> getNoTitles(){
        titles = new ArrayList<ModelTitle>();
        titles.add(new ModelTitle("Нет новостей"));
        return titles;
    }

    public static ArrayList<ModelTitle> getTitles(){
        titles = new ArrayList<ModelTitle>();
        for (int i=0; i<MainActivity.lstTitle.size(); i++){
            titles.add(new ModelTitle(MainActivity.lstTitle.get(i)));
        }
        return titles;
    }
}
