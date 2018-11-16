package com.justclack.legends_quotes;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class QuotesDisplayActivity extends AppCompatActivity {
    ProgressBar mProgressDialog;
    RecyclerView recyclerview;
    Quotes_Main_Adapter quotesMain_adapter;
    ArrayList<QuotesModelClass> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotes_display);
        mProgressDialog = findViewById(R.id.progress);
        recyclerview = findViewById(R.id.recyclerview);
        recyclerview.hasFixedSize();
        // recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerview.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        quotesMain_adapter = new Quotes_Main_Adapter(list, QuotesDisplayActivity.this);
        getSupportActionBar().setTitle(getIntent().getStringExtra("title"));
        new Title().execute();
    }

    private class Title extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                // Connect to the web site
                Document document = Jsoup.connect(getIntent().getStringExtra("url")).get();
                //Elements els = document.getElementById("b-qt").children();
                Elements els = document.select(".b-qt");
                //title = els.text();
                for (Element element : els) {
                    System.out.println(element.ownText());
                    list.add(new QuotesModelClass(element.ownText()));
                    JSONObject json = new JSONObject();
                    JSONObject manJson = new JSONObject();
                    try {
                        manJson.put("name", "emil");
                        json.put("man", manJson);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
               /* File file = getFileStreamPath( "myjson.html");
                if (!file.exists()) {
                    try {
                        OutputStreamWriter out = new OutputStreamWriter(openFileOutput(titleText + ".html", 0));
                        out.write(html);
                        out.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else {
                    Log.d("FILE", "Il file esiste");
                }*/
                quotesMain_adapter.notifyDataSetChanged();


            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Set title into TextView
            mProgressDialog.setVisibility(View.GONE);
            recyclerview.setAdapter(quotesMain_adapter);
            //quotesMain_adapter.notifyDataSetChanged();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.contact) {
            Intent contact = new Intent(Intent.ACTION_SEND);
            contact.setPackage("com.google.android.gm");
            contact.setData(Uri.parse("email"));
            String[] s = {"imjalilahmad@gmail.com"};
            contact.putExtra(Intent.EXTRA_EMAIL, s);
            contact.putExtra(Intent.EXTRA_SUBJECT, "Recommend subject Here");
            contact.putExtra(Intent.EXTRA_TEXT, "Recommend Message Here");
            contact.setType("message/rfc822");
            Intent chooser = Intent.createChooser(contact, "Choose Gmail for contact us");
            startActivity(chooser);
        }
        if (id == R.id.rate) {
            Uri uri = Uri.parse(getResources().getString(R.string.link11) + getApplicationContext().getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse(getResources().getString(R.string.link10) + getApplicationContext().getPackageName())));
            }
        }

        return super.onOptionsItemSelected(item);
    }
}