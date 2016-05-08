package com.example.hanbyeol.capstone_ui;

import android.app.Activity;
import android.app.LauncherActivity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Vector;

public class UrlActivity extends Activity {


    ImageView imView;
    TextView txtView;

    Bitmap bmImg;
    //back task;
    phpDown task;
    ArrayList<LauncherActivity.ListItem> listItem= new ArrayList<LauncherActivity.ListItem>();
    LauncherActivity.ListItem Item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_url);
        task = new phpDown();
        txtView = (TextView)findViewById(R.id.url_textview);

        //imView = (ImageView) findViewById(R.id.imageView1);

        task.execute("http://52.74.198.10/d.php");





    }


    private class back extends AsyncTask<String, Integer,Bitmap>{



        @Override
        protected Bitmap doInBackground(String... urls) {
            // TODO Auto-generated method stub
            try{
                URL myFileUrl = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection)myFileUrl.openConnection();
                conn.setDoInput(true);
                conn.connect();
                //String json = DownloadHtml("http://117.16.243.116/appdata.php");
                InputStream is = conn.getInputStream();

                bmImg = BitmapFactory.decodeStream(is);


            }catch(IOException e){
                e.printStackTrace();
            }
            return bmImg;
        }

        protected void onPostExecute(Bitmap img){
            imView.setImageBitmap(bmImg);
        }

    }
    private class phpDown extends AsyncTask<String, Integer,String>{



        @Override
        protected String doInBackground(String... urls) {
            StringBuilder jsonHtml = new StringBuilder();
            try{
                // 연결 url 설정
                URL url = new URL(urls[0]);
                // 커넥션 객체 생성
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                // 연결되었으면.
                if(conn != null){
                    conn.setConnectTimeout(10000);
                    conn.setUseCaches(false);
                    // 연결되었음 코드가 리턴되면.
                    if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                        for(;;){
                            // 웹상에 보여지는 텍스트를 라인단위로 읽어 저장.
                            String line = br.readLine();
                            if(line == null) break;
                            // 저장된 텍스트 라인을 jsonHtml에 붙여넣음
                            jsonHtml.append(line + "\n");
                        }
                        br.close();
                    }
                    conn.disconnect();
                }
            } catch(Exception ex){
                ex.printStackTrace();
            }
            return jsonHtml.toString();

        }

        protected void onPostExecute(String str){

            String imgurl="";
            String txt1="";
            String txt2="";
            Vector<String> vector = new Vector<String>(3);
            Vector<String> vector2 = new Vector<String>(3);
            try{

                JSONObject root = new JSONObject(str);
                JSONArray ja = root.getJSONArray("result");
                for(int i=0; i<ja.length(); i++){
                    JSONObject jo = ja.getJSONObject(i);
                    imgurl = jo.getString("name");
                    txt1 = jo.getString("address");
                    vector.addElement(jo.getString("name"));
                    vector2.addElement(jo.getString("address"));

                }
            }catch(JSONException e){
                e.printStackTrace();
            }
            for(int i =0; i<vector.size();i++)
                txt2=txt2+"name: "+vector.elementAt(i)+" address: "+vector2.elementAt(i)+"\n";


            txtView.setText(txt2);
        }


    }



}