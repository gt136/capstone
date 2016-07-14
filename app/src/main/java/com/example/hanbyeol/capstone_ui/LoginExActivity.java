package com.example.hanbyeol.capstone_ui;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieSyncManager;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginExActivity extends AppCompatActivity {

    private String UserID, UserPW; // 아이디 와 패스워드를 저장
    EditText EtID, EtPW;         // 패스워드를 입력
    Button BtnLogin, BtnLogout;       // Login버튼 처리
    private WebView Webv;
    String sfName = "LoginFile";

    public CookieManager cookieManager;
    public CookieSyncManager cookieSyncManager;
    public DefaultHttpClient httpclient = new DefaultHttpClient();
    public String domain = "http://www.cconmausa.com";

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        super.onCreate(savedInstanceState);

        try {
            if (cookieManager == null) {
                cookieSyncManager.createInstance(this);
                cookieManager = CookieManager.getInstance();
            } else
                cookieManager.removeAllCookie();
            cookieSyncManager.getInstance().startSync();
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (isOnline() == true) {

            setContentView(R.layout.activity_login_ex);

            EtID = (EditText) findViewById(R.id.editID); // ID입력 공간
            EtPW = (EditText) findViewById(R.id.editPW); // 패스워드 입력 공간

            BtnLogin = (Button) findViewById(R.id.btnLogin);
            BtnLogout = (Button) findViewById(R.id.btnLogout);

            Webv = (WebView) findViewById(R.id.imageView1);
            Webv.getSettings().setJavaScriptEnabled(true);
            Webv.getSettings().setDomStorageEnabled(true);
            Webv.getSettings().setUseWideViewPort(true);

            final Context myApp = this;

            Webv.setWebChromeClient(new WebChromeClient() {
                @Override
                public boolean onJsAlert(WebView view, String url, String message, final android.webkit.JsResult result) {
                    new AlertDialog.Builder(myApp).setTitle("AlertDialog").setMessage(message).setPositiveButton(android.R.string.ok,
                            new AlertDialog.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    result.confirm();
                                }
                            }).setCancelable(false).create().show();
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


//        final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
//        final String tmDevice = "" + tm.getDeviceId();
//        StringBuffer userAgent = new StringBuffer(Webv.getSettings().getUserAgentString());
//        userAgent.append(";" + tmDevice);
//        Webv.getSettings().setUserAgentString(userAgent.toString());


            //String androID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID); //안드로이드 device id 추출

            SharedPreferences sf = getSharedPreferences(sfName, 0);
            EtID.setText(sf.getString("name", ""));
            //EtPW.setText(sf.getString("passwd", ""));



            Webv.loadUrl(domain);
            Webv.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            });

            BtnLogin.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    UserID = EtID.getText().toString(); // EtPw에 입력한 텍스트를 ID에 넣음
                    UserPW = EtPW.getText().toString(); // EtPW에 입력한 텍스트를 PW에 넣음
                    new Login().execute(); //로그인 함수 호출
                }
            });

            BtnLogout.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Webv.clearHistory();
                    Webv.clearCache(true);
                    Webv.clearView();

                    cookieSyncManager = CookieSyncManager.getInstance();
                    cookieManager = CookieManager.getInstance();
                    cookieManager.setAcceptCookie(true);
                    cookieManager.removeSessionCookie();
                    cookieSyncManager.sync();

                    Webv.reload();
                }
            });
        }

        else{ //네트워크 연결 실패

            // 비행기모드 체크 - 이부분 다시 정리
//            if (Settings.System.getInt(getApplication().getContentResolver(),
//                    Settings.System.AIRPLANE_MODE_ON, 0) == 1) {
//                Toast.makeText(getApplicationContext(), "비행기모드 ON", Toast.LENGTH_LONG).show();
//            } else {
//                Toast.makeText(getApplicationContext(), "비행기모드 OFF", Toast.LENGTH_LONG).show();
//            }

            Log.d("NETWORK", "FAIL TO CONNECT");
            alertDialogBuilder
                    .setMessage("NO NETWORK")
                    .setCancelable(false)
                    .setPositiveButton("FINISH",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog, int id) {
                                    Log.d("NETWORK", "FINISH");
                                    dialog.cancel();
                                    finish();
                                }
                            })
                    .setNegativeButton("TRY AGAIN",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog, int id) {
                                    Log.d("NETWORK", "TRY AGAIN");
                                    dialog.cancel();
                                    onRestart();
                                }
                            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        }

    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onPause() {
        super.onPause();
        if (CookieSyncManager.getInstance() != null) {
            CookieSyncManager.getInstance().stopSync();
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onResume() {
        super.onResume();
        CookieSyncManager.getInstance().startSync();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences sf = getSharedPreferences(sfName, 0);
        SharedPreferences.Editor editor = sf.edit();
        editor.putString("name", UserID);
        editor.putString("passwd", UserPW);
        editor.commit();
    }

//    @SuppressWarnings("deprecation")
//    protected void onDestroy() {
//        super.onDestroy();
//        if (cookieManager != null) {
//            cookieManager.removeAllCookie();
//        }
//    }

    class Login extends AsyncTask<String, String, String> {

        HttpEntity entity;
        BasicResponseHandler myHandler = new BasicResponseHandler();

        @Override
        protected void onPreExecute(){
        }

        @SuppressWarnings("deprecation")
        @Override
        protected String doInBackground(String... str){

            try {
                List<NameValuePair> qparams = new ArrayList<NameValuePair>();
                qparams.add(new BasicNameValuePair("id", UserID));
                qparams.add(new BasicNameValuePair("passwd", UserPW));

                JSONObject json = new JSONObject();
                json.put("id", UserID);
                json.put("passwd", UserPW);

                // 네트워크 연결 시간이 지연될경우
                HttpParams params = httpclient.getParams();
                HttpConnectionParams.setConnectionTimeout(params, 10000);
                HttpConnectionParams.setSoTimeout(params, 10000);

                //Post 로그인 사이트
                UrlEncodedFormEntity Urlentity = new UrlEncodedFormEntity(qparams, "UTF-8");
                //StringEntity se = new StringEntity(json.toString());
                HttpPost httppost = new HttpPost(domain + "/member/login_handle.php");
                httppost.setEntity(Urlentity);
                //httppost.setEntity(se);

                HttpResponse response = httpclient.execute(httppost);
                entity = response.getEntity();
                final String content = EntityUtils.toString(entity);

//                Header[] headers = response.getHeaders("Set-Cookie");
//                //Header[] headers = response.getAllHeaders();
//                cookieManager = CookieManager.getInstance();
//                if(headers.length>0) {
//                    //HttpUtil.PHPSESSID = headers[0].getValue();
//                        for(Header h : headers){
//                            String cookieString = h.getName() + "=" + h.getValue();
//                            cookieManager.setCookie(domain, cookieString);
//                            CookieSyncManager.getInstance().sync();
//                            Log.d("test", ": " + cookieString);
//                        }
//                }


                List<Cookie> cookies = ((DefaultHttpClient)httpclient).getCookieStore().getCookies();
                cookieManager = CookieManager.getInstance();
                if(!cookies.isEmpty()){
                    for(int i= 0; i < cookies.size(); i++){
                        String cookieString = cookies.get(i).getName() + "=" + cookies.get(i).getValue();
                        cookieManager.setCookie(domain, cookieString);
                        CookieSyncManager.getInstance().sync();
                        Log.d("cookies", " " + cookieString);
                    }
                }

                Thread.sleep(500);

                if (entity != null) {
                    Log.d("LOG", "entity != null");
                    return content;
                }
                else {
                    Log.d("LOG", "entity == null");
                }

            }catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e){
                e.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            }

            return "";
        }

        @Override
        protected void onPostExecute(final String str) {
            // final Context myApp = this.getContext();
            if(entity != null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("LOG", "before webview load ");
                        Log.d("LOG", "content= " + str);
                        //Webv.loadData(content, "text/html; charset=utf-8", "UTF-8");
                        Webv.loadUrl(domain);
                        Webv.setWebViewClient(new WebViewClient() {
                            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                view.loadUrl(url);
                                return true;
                            }
                        });
                        //Webv.reload();
                    }
                });
            }
            httpclient.getConnectionManager().shutdown();
        }
    }

    private class DraptWebViewClient extends WebViewClient {
        @Override
        public void onFormResubmission(WebView view, Message dontResend, Message resend) {
            resend.sendToTarget();
        }
    }

    private boolean isOnline() { // network 연결 상태 확인
        try {
            ConnectivityManager manager =
                    (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mobile.isConnected() || wifi.isConnected()){
                Log.d("NETWORK", "Network connect success");
                return true;
            }else{
                Log.d("NETWORK", "Network connect fail");
                return false;
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }
}
