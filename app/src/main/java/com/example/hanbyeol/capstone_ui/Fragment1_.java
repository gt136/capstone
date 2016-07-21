package com.example.hanbyeol.capstone_ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

public class Fragment1_ extends Fragment {

    private String curURL;
    private WebView mWebView;
    private WebSettings mWebSettings;
    private ProgressBar progress;
    public Fragment1_() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.content_fragment1_, container, false);
        String userAgent = System.getProperty("http.agent");
        progress = (ProgressBar) view.findViewById(R.id.web_progress);
        Log.d("userAgent",userAgent);
        mWebView = (WebView) view.findViewById(R.id.frag1_webview);
        mWebSettings = mWebView.getSettings();


        String userAgent2 = mWebSettings.getUserAgentString();
        Log.d("userAgent2",userAgent2);
        mWebSettings.setBuiltInZoomControls(true);
        mWebSettings.setSupportZoom(true);
        mWebSettings.setUseWideViewPort(true);
        mWebSettings.setLoadWithOverviewMode(true);
        mWebSettings.setSaveFormData(false);
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        // mWebSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

       
        mWebView.setWebViewClient(new MyWebClient());
        final Context myApp = this.getContext();
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

        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d("URL_CATCH", url);
                Intent intent1 = new Intent(getActivity(), PopUpWebview.class);
                intent1.putExtra("url",url);
                startActivity(intent1);
                getActivity().overridePendingTransition(R.anim.anim_slide_in_from_right, R.anim.anim_hold);
                Log.d("되나","되라");
                return true;
                /*FragmentTransaction fragTran = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment1_ test = new Fragment1_();
                Bundle bundle = new Bundle();
                bundle.putString("url", url);
                test.setArguments(bundle);
                fragTran.setCustomAnimations(R.anim.anim_slide_in_from_right, R.anim.anim_hold);
                Log.d("이쪽",url);
                fragTran.replace(R.id.app_bar_main, test);
                fragTran.addToBackStack(null);
                fragTran.commit();
                return true;*/
            }
          public void onPageStarted(WebView view, String url,
                                      android.graphics.Bitmap favicon) {
                super.onPageStarted(view, url, favicon); //페이지 로딩 시작
              progress.setVisibility(View.VISIBLE);
            };
            public void onPageFinished(WebView view, String url) { //페이지 로딩 완료
                super.onPageFinished(view, url);
                progress.setVisibility(View.GONE);
            };
            public void onReceivedError(WebView view, int errorCode,String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                switch (errorCode) {

                    case ERROR_AUTHENTICATION:               // 서버에서 사용자 인증 실패
                        Toast.makeText(myApp, "사용자인증실패", Toast.LENGTH_LONG).show();
                        break;
                    case ERROR_BAD_URL:                            // 잘못된 URL
                        Toast.makeText(myApp,"잘못된URL", Toast.LENGTH_LONG).show();
                        break;
                    case ERROR_CONNECT:                           // 서버로 연결 실패
                        Toast.makeText(myApp,"서버로 연결 실패", Toast.LENGTH_LONG).show();
                        break;
                    case ERROR_FAILED_SSL_HANDSHAKE:     // SSL handshake 수행 실패
                        Toast.makeText(myApp," SSL handshake 수행 실패", Toast.LENGTH_LONG).show();
                        break;
                    case ERROR_FILE:                                   // 일반 파일 오류
                        Toast.makeText(myApp,"일반 파일 오류", Toast.LENGTH_LONG).show();
                        break;
                    case ERROR_FILE_NOT_FOUND:                // 파일을 찾을 수 없습니다
                        Toast.makeText(myApp,"파일을 찾을 수 없습니다", Toast.LENGTH_LONG).show();
                        break;
                    case ERROR_HOST_LOOKUP:            // 서버 또는 프록시 호스트 이름 조회 실패
                        Toast.makeText(myApp,"서버 또는 프록시 호스트 이름 조회 실패", Toast.LENGTH_LONG).show();
                        break;
                    case ERROR_IO:                               // 서버에서 읽거나 서버로 쓰기 실패
                        Toast.makeText(myApp,"서버에서 읽거나 서버로 쓰기 실패", Toast.LENGTH_LONG).show();
                        break;
                    case ERROR_PROXY_AUTHENTICATION:    // 프록시에서 사용자 인증 실패
                        Toast.makeText(myApp,"프록시에서 사용자 인증 실패", Toast.LENGTH_LONG).show();
                        break;
                    case ERROR_REDIRECT_LOOP:                // 너무 많은 리디렉션
                        Toast.makeText(myApp," 너무 많은 리디렉션", Toast.LENGTH_LONG).show();
                        break;
                    case ERROR_TIMEOUT:                          // 연결 시간 초과
                        Toast.makeText(myApp,"연결 시간 초과", Toast.LENGTH_LONG).show();
                        break;
                    case ERROR_TOO_MANY_REQUESTS:            // 페이지 로드중 너무 많은 요청 발생
                        Toast.makeText(myApp,"페이지 로드중 너무 많은 요청 발생", Toast.LENGTH_LONG).show();
                        break;
                    case ERROR_UNKNOWN:                         // 일반 오류
                        Toast.makeText(myApp,"일반 오류", Toast.LENGTH_LONG).show();
                        break;
                    case ERROR_UNSUPPORTED_AUTH_SCHEME:  // 지원되지 않는 인증 체계
                        Toast.makeText(myApp,"지원되지 않는 인증 체계", Toast.LENGTH_LONG).show();
                        break;
                    case ERROR_UNSUPPORTED_SCHEME:
                        Toast.makeText(myApp,"ERROR_UNSUPPORTED_SCHEME", Toast.LENGTH_LONG).show();
                        break;


                }
                mWebView.stopLoading();
            }
        });
            ;

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
