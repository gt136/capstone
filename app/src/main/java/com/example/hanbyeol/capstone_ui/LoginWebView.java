package com.example.hanbyeol.capstone_ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;

/**
 * Created by ymg on 2016-07-06.
 */
public class LoginWebView extends AppCompatActivity {
    private WebView mWebView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_webview);
        CookieSyncManager.createInstance(this);

        mWebView = (WebView) findViewById(R.id.login_web);
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
}
