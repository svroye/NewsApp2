package com.example.android.newsapp2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Steven on 14/02/2017.
 */

public class NewsFeaturesAdapter extends ArrayAdapter<NewsFeatures> {

    public NewsFeaturesAdapter(Context context, List<NewsFeatures> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        //get current newsFeature
        NewsFeatures currentNews = getItem(position);

        //find the textviews for the title, section, subsection and date
        TextView titleView = (TextView) convertView.findViewById(R.id.title_textview);
        TextView sectionView = (TextView) convertView.findViewById(R.id.section_textview);
        TextView dateView = (TextView) convertView.findViewById(R.id.date_textview);

        //set the title, section, subsection and date from the currentNews
        titleView.setText(currentNews.getTitle());

        sectionView.setText(currentNews.getSection());

        //separate the date from the time. Separation symbol was found to be T, e.g. [date]T[time]
        //therefore, only the part of the date string until T is used for the date TextView
        String oldDate = currentNews.getDate();
        dateView.setText(oldDate.substring(0, oldDate.indexOf("T")));

        return convertView;
    }
}
