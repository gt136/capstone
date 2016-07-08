package com.example.hanbyeol.capstone_ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by ymg on 2016-07-06.
 */
public class LoginWebView extends AppCompatActivity {
    private WebView mWebView;
    private WebSettings mWebSettings;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_webview);
        CookieSyncManager.createInstance(this);

        mWebView = (WebView) findViewById(R.id.login_web);
        mWebSettings = mWebView.getSettings();
        mWebSettings.setBuiltInZoomControls(true);
        mWebSettings.setSupportZoom(true);
        mWebSettings.setUseWideViewPort(true);
        mWebSettings.setLoadWithOverviewMode(true);
        mWebSettings.setSaveFormData(false);
        mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebSettings.setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new MyWebClient());

        mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        mWebView.loadUrl("http://www.cconmausa.com/");
    }
    protected void onResume(){
super.onResume();
        CookieSyncManager.getInstance().startSync();
    }
    protected void onPause(){
        super.onPause();
        CookieSyncManager.getInstance().stopSync();
    }
    public class MyWebClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url){
            view.loadUrl(url);
            Log.d("URL_CATCH", url);

            //return super.shouldOverrideUrlLoading(view, url);
            return true;
        }
    }
}
