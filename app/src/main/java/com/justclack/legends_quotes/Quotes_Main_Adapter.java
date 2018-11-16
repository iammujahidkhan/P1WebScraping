package com.justclack.legends_quotes;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;

import java.util.ArrayList;

import static android.content.Context.CLIPBOARD_SERVICE;
import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class Quotes_Main_Adapter extends RecyclerView.Adapter<Quotes_Main_Adapter.MyView> {

    ArrayList<QuotesModelClass> list;
    Activity context;
    Typeface typeface, fontawesome;
    public static SQLiteHelper sqLiteHelper;
    int RC_IMG_CHOOSE = 1212;

    public Quotes_Main_Adapter(ArrayList<QuotesModelClass> list, Activity context) {
        this.list = list;
        this.context = context;
        sqLiteHelper = new SQLiteHelper(context);
        sqLiteHelper.createTable(Const.fav_table_query);
    }

    @Override
    public MyView onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.main_card_view, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rootView.setLayoutParams(lp);
        return new MyView(rootView);
    }

    @Override
    public void onBindViewHolder(final MyView holder, int position) {
        final String readMore = " Read more Quotes at: https://play.google.com/store/apps/details?id=" + context.getPackageName();
        typeface = Typeface.createFromAsset(context.getAssets(), "NotoSerif-Regular.ttf");
        fontawesome = Typeface.createFromAsset(context.getAssets(), "fontawesome.ttf");
        final QuotesModelClass data = list.get(position);
        final String txt_title = data.getTitle().replace("'","").replace("'", "").replace("we're", "we are").replace("We're", "We are").replace("don't", "do not").replace("it's", "it is").replace("It's", "It is").replace("doesn't", "does not").replace("didn't", "did not").replace("isn't", "is not");
        holder.title.setTypeface(typeface);
        holder.title.setText(data.getTitle());
        //
        Cursor child = sqLiteHelper.getData("SELECT * FROM " + Const.FAV_TABLE_NAME + " where " + Const.title + " = '" + txt_title + "'");
        if (child.getCount() > 0) {
            holder.fav.setTextColor(context.getResources().getColor(R.color.colorAccent));
        } else {
            holder.fav.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }
        //
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
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View mView = context.getLayoutInflater().inflate(R.layout.view_popup, null);
                TextView text, close, copy, photo;
                photo = mView.findViewById(R.id.photo);
                text = mView.findViewById(R.id.text);
                close = mView.findViewById(R.id.close);
                close.setTypeface(fontawesome);
                text.setTypeface(typeface);
                photo.setTypeface(fontawesome);
                photo.setText("\uf03e");
                close.setText("\uf2d3");
                copy = mView.findViewById(R.id.copy);
                copy.setTypeface(fontawesome);
                copy.setText("\uf0c5");
                copy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("label", data.getTitle() + readMore);
                        Toast.makeText(context, "Copied To ClipBoard", Toast.LENGTH_SHORT).show();
                        clipboard.setPrimaryClip(clip);
                    }
                });
                text.setText(data.getTitle());
                builder.setView(mView);
                final AlertDialog dialog = builder.create();
                dialog.show();
                photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent go = new Intent(context, PhotoActivity.class);
                        go.putExtra("title", data.getTitle());
                        context.startActivity(go);
                    }
                });
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });

        holder.fav.setTypeface(fontawesome);
        holder.share.setTypeface(fontawesome);
        holder.fav.setText("\uf004");
        //fav ->f004
        //copy ->f0c5
        holder.share.setText("\uf1e0");
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, data.getTitle() + readMore);
                sendIntent.setType("text/plain");
                context.startActivity(sendIntent);
            }
        });
        holder.fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                Cursor cursor = null;
                try {
                    cursor = sqLiteHelper.getData("SELECT * FROM " + Const.FAV_TABLE_NAME + " where " + Const.title + " = '" + txt_title + "'");
                    if (cursor.getCount() > 0) {
                        try {
                            sqLiteHelper.deleteData(String.valueOf(data.title));
                            holder.fav.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                            //Toast.makeText(context, "Removed From Favourite", Toast.LENGTH_SHORT).show();
                            Log.d("deleted", "deleted");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            sqLiteHelper.AddToFav(data.getTitle());
                            holder.fav.setTextColor(context.getResources().getColor(R.color.colorAccent));
                            //Toast.makeText(context, "Added to Favourite", Toast.LENGTH_SHORT).show();
                            Log.d("added", "added");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                } finally {
                    // this gets called even if there is an exception somewhere above
                    if (cursor != null) {
                        cursor.close();
                    }

                }
                //

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MyView extends RecyclerView.ViewHolder {

        TextView title, share, fav;
        View view;

        public MyView(View itemView) {
            super(itemView);
            view = itemView;
            title = itemView.findViewById(R.id.title);
            share = itemView.findViewById(R.id.share);
            fav = itemView.findViewById(R.id.fav);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                title.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
            }
        }
    }

}