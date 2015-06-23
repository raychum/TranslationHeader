package com.translationheader.sample;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by raychum on 14/5/15.
 */
public class ImageRecyclerAdapter extends RecyclerView.Adapter<ImageRecyclerAdapter.FeedListRowHolder> {

    public static final String VIEW_ALL = "View All";
    public static final String EDIT = "Edit";
    private Context mContext;
    private ArrayList<String> titleList;

    public ImageRecyclerAdapter(Context context, ArrayList<String> filteredList) {
        this.mContext = context;
        setTitleList(filteredList);
    }

    public void setTitleList(ArrayList<String> titleList) {
        this.titleList = titleList;
        notifyDataSetChanged();
    }

    @Override
    public FeedListRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyler_view_item, null);
        FeedListRowHolder mh = new FeedListRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(FeedListRowHolder feedListRowHolder, int i) {
        feedListRowHolder.title.setText(titleList.get(i));
        if (VIEW_ALL.equals(titleList.get(i)) || EDIT.equals(titleList.get(i))) {
            feedListRowHolder.thumbnail.setImageDrawable(null);
            feedListRowHolder.view.setOnClickListener((View.OnClickListener) mContext);
        } else {
            feedListRowHolder.thumbnail.setImageResource(ChildFragment.imageList.get(i));
            feedListRowHolder.view.setOnClickListener(null);
        }
    }

    @Override
    public int getItemCount() {
        return titleList == null ? 0 : titleList.size();
    }

    public class FeedListRowHolder extends RecyclerView.ViewHolder {
        protected View view;
        protected ImageButton thumbnail;
        protected TextView title;

        public FeedListRowHolder(View view) {
            super(view);
            this.view = view;
            this.thumbnail = (ImageButton) view.findViewById(R.id.thumbnail);
            this.title = (TextView) view.findViewById(R.id.title);
        }

    }
}
