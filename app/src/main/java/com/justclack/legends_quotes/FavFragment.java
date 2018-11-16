package com.justclack.legends_quotes;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import static com.justclack.legends_quotes.AllCatsModel.site;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavFragment extends Fragment {

    RecyclerView recyclerview;
    ArrayList<QuotesModelClass> list = new ArrayList<>();
    View mainView;
    ProgressBar progress;
    Quotes_Main_Adapter quotes_main_adapter;

    public FavFragment() {
        // Required empty public constructor
    }

    public static SQLiteHelper sqLiteHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        sqLiteHelper = new SQLiteHelper(getActivity());
        sqLiteHelper.createTable(Const.fav_table_query);
        mainView = inflater.inflate(R.layout.activity_quotes_display, container, false);
        recyclerview = mainView.findViewById(R.id.recyclerview);
        progress = mainView.findViewById(R.id.progress);
        recyclerview.hasFixedSize();
        quotes_main_adapter = new Quotes_Main_Adapter(list, getActivity());
        recyclerview.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        new Title().execute();
        recyclerview.setAdapter(quotes_main_adapter);
        return mainView;
    }

    private class Title extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            Cursor cursor = sqLiteHelper.getData("SELECT * FROM " + Const.FAV_TABLE_NAME + " order by id desc");
            list.clear();
            try {
                while (cursor.moveToNext()) {
                    String title = cursor.getString(1);
                    list.add(new QuotesModelClass(title));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                cursor.close();
                sqLiteHelper.close();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Set title into TextView
            progress.setVisibility(View.GONE);
            quotes_main_adapter.notifyDataSetChanged();

        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser == true) {
            new Title().execute();
        }
    }

}