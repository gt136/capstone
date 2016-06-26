package com.example.hanbyeol.capstone_ui;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewFragment extends Fragment {

    private String curURL;
    private WebView mWebView;
    private WebSettings mWebSettings;

    private WebViewFragment(){ }
    public WebViewFragment(String url){
        curURL = url;
    }

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

    public class MyWebClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url){
            view.loadUrl(url);
            Log.d("test", "before catch");
            Log.d("URL_CATCH", url);

            return super.shouldOverrideUrlLoading(view, url);
            //return true;
        }
    }
}
