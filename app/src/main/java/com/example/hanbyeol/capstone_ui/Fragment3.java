package com.example.hanbyeol.capstone_ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

import java.net.CookieManager;

public class Fragment3 extends Fragment {

    CookieManager CookieManager = new CookieManager();
    public EditText id;
    public EditText pw;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_fragment3, container, false);
        /*CookieManager.setAcceptFileSchemeCookies(true);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.acceptCookie();
        String cookie = CookieManager.getInstance().getCookie("mylink");*/
        return view;
    }

    public class MyWebClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url){
            view.loadUrl(url);
            Log.d("URL_CATCH", url);
            return super.shouldOverrideUrlLoading(view, url);
            //return true;
        }

//        public void onPageFinished(WebView view, String url){
//            CookieSyncManager.getInstance().sync();
//        }
    }

//    @Override
//    protected void onResume(){
//        super.onResume();
//        CookieSyncManager.getInstance().startSync();
//    }
//
//    @Override
//    protected void onPause(){
//        super.onPause();
//        CookieSyncManager.getInstance().stopSync();
//    }

}
