package com.justclack.legends_quotes;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


public class Colors_Adapter extends RecyclerView.Adapter<Colors_Adapter.MyView> {

    ArrayList<QuotesModelClass> list;
    Activity context;

    public Colors_Adapter(ArrayList<QuotesModelClass> list, Activity context) {
        this.list = list;
        this.context = context;

    }

    @Override
    public MyView onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.color_card_view, null, false);
       /* RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rootView.setLayoutParams(lp);*/
        return new MyView(rootView);
    }

    @Override
    public void onBindViewHolder(final MyView holder, int position) {
        final QuotesModelClass data = list.get(position);
        holder.title.setBackgroundColor(Color.parseColor(data.getTitle()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MyView extends RecyclerView.ViewHolder {

        TextView title;
        View view;

        public MyView(View itemView) {
            super(itemView);
            view = itemView;
            title = itemView.findViewById(R.id.title);
        }
    }

}