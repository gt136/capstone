package com.example.hanbyeol.capstone_ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
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

        final Context myApp = this.getContext();
        View view = inflater.inflate(R.layout.content_fragment1_, container, false);

        mWebView = (WebView) view.findViewById(R.id.frag1_webview);
        mWebSettings = mWebView.getSettings();
        mWebSettings.setBuiltInZoomControls(true);
        mWebSettings.setSupportZoom(true);
        mWebSettings.setUseWideViewPort(true);
        mWebSettings.setLoadWithOverviewMode(true);
        mWebSettings.setSaveFormData(false);
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        // mWebSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        mWebView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //This is the filter
                if (event.getAction()!=KeyEvent.ACTION_DOWN)
                    return true;
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (mWebView.canGoBack()) {
                        mWebView.goBack();
                    } else {
                        ((MainActivity)getActivity()).onBackPressed();
                    }
                    return true;
                }
                return false;
            }
        });

        mWebView.setWebViewClient(new MyWebClient());
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final android.webkit.JsResult result) {
                new AlertDialog.Builder(myApp)
                        .setTitle("Alert title")
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok,
                                new AlertDialog.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        result.confirm();
                                    }
                                })
                        .setCancelable(false)
                        .create()
                        .show();
                return true;
            }

            public boolean onJsConfirm(WebView view, String url,
                                       String message, final android.webkit.JsResult result) {
                new AlertDialog.Builder(myApp)
                        .setTitle("Concierge")
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok,
                                new AlertDialog.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog,
                                            int which) {
                                        result.confirm();
                                    }
                                })
                        .setNegativeButton(android.R.string.cancel,
                                new AlertDialog.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog,
                                            int which) {
                                        result.cancel();
                                    }
                                }).setCancelable(false).create().show();
                return true;
            }
        });

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
