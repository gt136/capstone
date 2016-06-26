package com.example.hanbyeol.capstone_ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Fragment1_ extends Fragment {

    private String curURL;
    private WebView mWebView;
    private WebSettings mWebSettings;

    public Fragment1_() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.content_fragment1_, container, false);

        mWebView = (WebView) view.findViewById(R.id.frag1_webview);
        mWebSettings = mWebView.getSettings();
        mWebSettings.setBuiltInZoomControls(true);
        mWebSettings.setSupportZoom(true);
        mWebSettings.setUseWideViewPort(true);
        mWebSettings.setLoadWithOverviewMode(true);
        mWebSettings.setSaveFormData(false);
        mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        // mWebSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        mWebView.setWebViewClient(new MyWebClient());

        Log.d("test", "in fragment1_");

        Bundle extra = getArguments();
        String url = extra.getString("url");
        mWebView.loadUrl(url);

        /*if(savedInstanceState == null){
            mWebView.setWebViewClient(new MyWebClient());
            mWebSettings.setJavaScriptEnabled(true);
            mWebView.loadUrl(curURL);
            Log.d("temp:",curURL);
        } else {
            mWebView.restoreState(savedInstanceState);
            mWebView.loadUrl(curURL);
            Log.d("temp2:", curURL);
        }*/
        //mWebView.addJavascriptInterface(new AndroidBridge(), "androidBridge");

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        mWebView.saveState(outState);
    }

    public class MyWebClient extends WebViewClient{
        public boolean shouldOverrideUrlLoading(WebView view, String url){
            view.loadUrl(url);
            Log.d("URL_CATCH", url);

            //return super.shouldOverrideUrlLoading(view, url);
            return true;
        }
    }

}
