package ru.saperov.fourpda;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by saperov on 26.03.16.
 * адаптер с listview different  two item layout
 */
public class CustomNewsAdapter extends ArrayAdapter<ModelNews> {
    public CustomNewsAdapter(Context context, ArrayList<ModelNews> modelNewsArrayList){
        super(context, 0, modelNewsArrayList);
    }
    TextView tvNews;
    int type;

    @Override
    public int getViewTypeCount() {
        //количество типов лейаутов
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        // Возвращает целое число для определения типа лейаута.
        // Число должно находиться в диапазоне от 0 до getViewTypeCount() - 1
        return position==0 ? 0 : 1;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Получим тип для заданной позиции
        type = getItemViewType(position);

        switch (type){
            case 0:
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_news_top, parent, false);
                ImageView ivNews = (ImageView) convertView.findViewById(R.id.ivNews);
                Picasso.with(this.getContext()).load(MainActivity.srcImgNews).placeholder(R.drawable.emptytrash).into(ivNews);
                break;
            case 1:
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_news_bottom, parent, false);
                tvNews = (TextView) convertView.findViewById(R.id.tvNews);
                tvNews.setText(MainActivity.dscNews);
                break;
        }


        return convertView;
    }


}
