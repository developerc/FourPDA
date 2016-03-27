package ru.saperov.fourpda;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by saperov on 24.03.16.
 */
public class CustomTitlesAdapter extends ArrayAdapter<ModelTitle> {
    /*  public CustomTitlesAdapter(Context context, int resource) {
          super(context, resource);
      }*/
    public CustomTitlesAdapter(Context context, ArrayList<ModelTitle> modelTitles){
        super(context, 0, modelTitles);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ModelTitle modelTitle = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_titles, parent, false);
        }
        TextView tvTitles = (TextView) convertView.findViewById(R.id.tvTitles);
        tvTitles.setText(modelTitle.myTitle);

        return convertView;
    }
}