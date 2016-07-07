package com.example.hanbyeol.capstone_ui;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    TabLayout tabLayout_bottom;
    Adapter adapter;
    CustomViewPager viewPager;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;


        viewPager = (CustomViewPager) findViewById(R.id.fragment_part_test);
        //Tool bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        //toolbar.setDisplayHomeAsUpEnabled(true);

        //Navigation Drawer
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //tabLayout_bottom = (TabLayout) findViewById(R.id.main_tabs_bottom);
        //TabLayoutBottomEvent();

        new ReadJSONFeed().execute("http://itaxi.handong.edu/init.php");

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent;
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (id == R.id.nav_home) {
            //intent = new Intent(this, MainActivity.class);
            // startActivity(intent);
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_url) {
            intent = new Intent(this, UrlActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_slide_in_from_right, R.anim.anim_hold);
        } else if (id == R.id.nav_fragment_test) {
            intent = new Intent(this, FragTestActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_slide_in_from_right, R.anim.anim_hold);
        } else if (id == R.id.nav_login_ex) {
            intent = new Intent(this, LoginExActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_slide_in_from_right, R.anim.anim_hold);
        } else if (id == R.id.nav_webview) {
            intent = new Intent(this, WebViewActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_slide_in_from_right, R.anim.anim_hold);
        }else if (id == R.id.app_login) {
            intent = new Intent(this, Login.class);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_slide_in_from_right, R.anim.anim_hold);
        }

        return true;
    }

    private class ReadJSONFeed extends AsyncTask<String, String, String> {
        protected void onPreExecute() {}
        Fragment1 frag1;
        Vector<String> vector = new Vector<String>(3);
        Vector<String> vector2 = new Vector<String>(3);
        @Override
        protected String doInBackground(String... urls) {
            HttpClient httpclient = new DefaultHttpClient();
            StringBuilder builder = new StringBuilder();
            HttpPost httppost = new HttpPost(urls[0]);
            try {
                HttpResponse response = httpclient.execute(httppost);
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                if (statusCode == 200) {
                    HttpEntity entity = response.getEntity();
                    InputStream content = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return builder.toString();
        }

        protected void onPostExecute(String result) {
            String stateInfo="";

            try{
                JSONObject object = new JSONObject(result);
                JSONArray countriesArray = new JSONArray(object.getString("band_menu"));
                tabLayout_bottom = (TabLayout) findViewById(R.id.main_tabs_bottom);

                for (int i =0 ; i<countriesArray.length();i++) {
                    JSONObject jObject = countriesArray.getJSONObject(i);
                    stateInfo+="Title: "+jObject.getString("title")+"\n";
                    stateInfo+="Url: "+jObject.getString("url")+"\n";
                    vector.addElement(jObject.getString("title"));
                    vector2.addElement(jObject.getString("url"));
                }
                setupViewPager(viewPager);
                viewPager.setOffscreenPageLimit(6);
                tabLayout_bottom.setupWithViewPager(viewPager);

            }
            catch (JSONException e) {
                e.printStackTrace();
            }

        }
        private void TabLayoutBottomEvent() {
            tabLayout_bottom.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    setCurrentTabFragment(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) { }

                @Override
                public void onTabReselected(TabLayout.Tab tab) { }
            });
        }

        private void setCurrentTabFragment(int tabPosition) {

            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            frag1 = new Fragment1();
            Bundle bundle = new Bundle();

            String[] title = new String[vector.size()];
            title = (String[])vector.toArray(title);
            String[] url = new String[vector2.size()];
            url = (String[])vector2.toArray(url);

            bundle.putStringArray("title",title);
            bundle.putStringArray("url",url);
            frag1.setArguments(bundle);
            Fragment2 frag2 = new Fragment2();
            Fragment3 frag3 = new Fragment3();
            Fragment4 frag4 = new Fragment4();

            switch (tabPosition) {
                case 0 :
                    Log.d("test", "1");
                    ft.replace(R.id.fragment_part_test, frag1);
                    Log.d("test", "2");
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    Log.d("test", "3");
                    ft.commit();
                    Log.d("test", "4");
                    break;
                case 1 :
                    ft.replace(R.id.fragment_part_test, frag2);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.commit();
                    break;
                case 2 :
                    ft.replace(R.id.fragment_part_test, frag3);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.commit();
                    break;
                case 3 :
                    ft.replace(R.id.fragment_part_test, frag4);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.commit();
                    break;
                default:
                    break;
            }
        }
        private void setupViewPager(ViewPager viewPager) {
            //get information about tabs from server
            //make Fragments as number of categories


            adapter = new Adapter(getSupportFragmentManager());
            frag1 = new Fragment1();
            Bundle bundle = new Bundle();

            String[] title = new String[vector.size()];
            title = (String[])vector.toArray(title);
            String[] url = new String[vector2.size()];
            url = (String[])vector2.toArray(url);

            bundle.putStringArray("title",title);
            bundle.putStringArray("url",url);
            frag1.setArguments(bundle);
            Fragment2 frag2 = new Fragment2();
            Fragment3 frag3 = new Fragment3();
            Fragment4 frag4 = new Fragment4();
            adapter.addFragment(frag1, "1");
            adapter.addFragment(frag2, "2");
            adapter.addFragment(frag3, "3");
            adapter.addFragment(frag4, "4");

            viewPager.setAdapter(adapter);
            Log.d("test", "in fragment1, 3");
        }
    }

}

