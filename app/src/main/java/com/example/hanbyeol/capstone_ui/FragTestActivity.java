package com.example.hanbyeol.capstone_ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

public class FragTestActivity extends AppCompatActivity {

    WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frag_test);

        mWebView = (WebView) findViewById(R.id.frag_test_webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(new AndroidBridge(), "androidBridge_frag_test");
        mWebView.loadUrl("file:///android_asset/new_frag_test.html");
        //mWebView.loadUrl("file:///android_asset/webView2.html");
    }

    private class AndroidBridge {
        @JavascriptInterface
        public void testaaaa() {
            //set new fragment.
            Log.d("in activity", "testaaaa() is working");

            Fragment3 testFrag = new Fragment3();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.addToBackStack(null);
            ft.replace(R.id.fragment_replace, testFrag);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();

            mWebView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        if(mWebView.canGoBack())
            mWebView.goBack();
        else {
            super.onBackPressed();
            overridePendingTransition(R.anim.anim_hold, R.anim.anim_slide_out_to_right);
        }
    }

}
