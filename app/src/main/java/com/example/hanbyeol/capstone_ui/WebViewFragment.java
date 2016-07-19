package com.example.hanbyeol.capstone_ui;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.util.logging.LogRecord;

public class WebViewFragment extends Fragment {

    private String curURL;
    private WebView mWebView;
    private WebSettings mWebSettings;

    //public WebViewFragment(){ }
    //public WebViewFragment(String url){
       // curURL = url;
    //}

    private static final int CLICK_ON_WEBVIEW = 1;
    private static final int CLICK_ON_URL = 2;

    private final Handler handler = new Handler((Handler.Callback) this);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_web_view_fragment, container, false);

        mWebView = (WebView) view.findViewById(R.id.webviewFrag_webview);
        mWebSettings = mWebView.getSettings();
        mWebSettings.setBuiltInZoomControls(true);
        mWebSettings.setSupportZoom(true);
        mWebSettings.setUseWideViewPort(true);
        mWebSettings.setLoadWithOverviewMode(true);
        mWebSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        mWebView.setWebViewClient(new MyWebClient());
        Log.d("URL_CATCHhhh", curURL);

        mWebSettings.setJavaScriptEnabled(true);

        mWebView.loadUrl(curURL);

        return view;
    }


    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.webviewFrag_webview && event.getAction() == MotionEvent.ACTION_DOWN){
            handler.sendEmptyMessageDelayed(CLICK_ON_WEBVIEW, 500);
        }
        return false;
    }

    public boolean handleMessage(Message msg) {
        if (msg.what == CLICK_ON_URL){
            handler.removeMessages(CLICK_ON_WEBVIEW);
            return true;
        }
        if (msg.what == CLICK_ON_WEBVIEW){
            Toast.makeText(WebViewFragment.this.getContext(), "Webview clicked", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    public class MyWebClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url){
            view.loadUrl(url);
            Log.d("URL_CATCH", url);

            return super.shouldOverrideUrlLoading(view, url);
            //return true;
        }
    }
}
