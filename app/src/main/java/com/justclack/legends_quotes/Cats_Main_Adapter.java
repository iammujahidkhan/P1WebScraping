package com.justclack.legends_quotes;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;

import java.util.ArrayList;

public class Cats_Main_Adapter extends RecyclerView.Adapter<Cats_Main_Adapter.MyView> {

    ArrayList<AllCatsModel> list;
    Context context;
    Typeface typeface;

    public Cats_Main_Adapter(ArrayList<AllCatsModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyView onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.card_card_view, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rootView.setLayoutParams(lp);
        return new MyView(rootView);

    }

    @Override
    public void onBindViewHolder(MyView holder, int position) {
        typeface = Typeface.createFromAsset(context.getAssets(), "NotoSerif-Regular.ttf");
        final AllCatsModel data = list.get(position);
        final String txt_title = data.getTitle().substring(0, 1).toUpperCase() + data.getTitle().substring(1).replace("_", " ");
        holder.title.setTypeface(typeface);
        holder.title.setText(txt_title);
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PublisherInterstitialAd mPublisherInterstitialAd = new PublisherInterstitialAd(context);
                mPublisherInterstitialAd.setAdUnitId("ca-app-pub-8267269636738230/9909930959");
                mPublisherInterstitialAd.loadAd(new PublisherAdRequest.Builder().build());
                mPublisherInterstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        // Code to be executed when an ad finishes loading.
                        if (mPublisherInterstitialAd.isLoaded()) {
                            mPublisherInterstitialAd.show();
                        } else {
                            Log.d("TAG", "The interstitial wasn't loaded yet.");
                        }
                    }

                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        // Code to be executed when an ad request fails.
                    }

                    @Override
                    public void onAdOpened() {
                        // Code to be executed when the ad is displayed.
                    }

                    @Override
                    public void onAdLeftApplication() {
                        // Code to be executed when the user has left the app.
                    }

                    @Override
                    public void onAdClosed() {
                        // Code to be executed when when the interstitial ad is closed.
                    }
                });
                //Toast.makeText(context, data.getUrl() + data.getTitle(), Toast.LENGTH_SHORT).show();
                Intent go = new Intent(context, QuotesDisplayActivity.class);
                go.putExtra("url", data.getUrl() + data.getTitle());
                go.putExtra("title", txt_title);
                context.startActivity(go);
            }
        });
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