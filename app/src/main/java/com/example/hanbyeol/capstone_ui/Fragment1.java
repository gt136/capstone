package com.example.hanbyeol.capstone_ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Vector;

public class Fragment1 extends Fragment {

    Adapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("T.T", "0");

        View view = inflater.inflate(R.layout.content_fragment1, container, false);

        ViewPager viewPager = (ViewPager) (view.findViewById(R.id.main_viewpager));
        if (viewPager != null) {
            setupViewPager(viewPager);
        }

        Log.d("T.T", "1");

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.main_tabs);
       viewPager.setOffscreenPageLimit(6);
        Log.d("T.T", "2");
        tabLayout.setupWithViewPager(viewPager);

        Log.d("T.T", "3");

        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        //get information about tabs from server
        //make Fragments as number of categories
        Vector<Fragment1_> vector = new Vector<Fragment1_>(5);
        // Adapter adapter = new Adapter(getChildFragmentManager());
        adapter = new Adapter(getFragmentManager());
        Bundle extra = getArguments();
        String[] url = extra.getStringArray("url");
        String[] title = extra.getStringArray("title");
        for(int i=0; i<url.length;i++){
            Bundle bundle = new Bundle();
            bundle.putString("url", url[i]);
            vector.addElement(new Fragment1_());
            vector.get(i).setArguments(bundle);
            adapter.addFragment(vector.get(i), title[i]);
        }
       /* Bundle bundle = new Bundle();
        bundle.putString("url", "http://devilbbong.tistory.com/9");

        Fragment1_ a =new Fragment1_();
        a.setArguments(bundle);
        adapter.addFragment( a, url[1]);
        for(int i=0; i<url.length-1;i++)
        adapter.addFragment(new Fragment3(), url[i]);*/



        viewPager.setAdapter(adapter);
    }

}
