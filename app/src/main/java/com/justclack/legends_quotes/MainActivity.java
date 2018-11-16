package com.justclack.legends_quotes;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    Toolbar mToolbar;
    private ViewPager viewPager;
    SimpleFragmentPagerAdapter sectionViewPagerAdapter;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.main_tabs);
        mToolbar = findViewById(R.id.toolbar);
        viewPager = findViewById(R.id.viewPager);
        sectionViewPagerAdapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(sectionViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
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