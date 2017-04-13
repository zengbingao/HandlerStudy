package com.robin.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    //图片资源  
    final String url = "http://s16.sinaimg.cn/orignal/89429f6dhb99b4903ebcf&690";
    Bitmap bitmap = null;
    private final MyHandler handler  =new MyHandler(MainActivity.this);
    private static class MyHandler extends Handler{
        final WeakReference<MainActivity> weakReference;

        private MyHandler(MainActivity mainActivity) {
            this.weakReference = new WeakReference<>(mainActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bitmap bitmap = (Bitmap) msg.obj;
            if(weakReference.get()!=null){
                weakReference.get().imageView.setImageBitmap(bitmap);
            }
        }
    }
    //定义一个图片显示控件  
    private ImageView imageView;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        imageView = (ImageView)this.findViewById(R.id.imageViewId);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    bitmap = getBitmap(url);
                    Message msg = handler.obtainMessage();
                    msg.obj = bitmap;
                    handler.sendMessage(msg);
                } catch (IOException e) {
                    Log.i(TAG, "onCreate: ");
                    e.printStackTrace();
                }
            }
        }).start();
        imageView.setImageBitmap(bitmap);

    }
    public static Bitmap getBitmap(String path) throws IOException {

        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        conn.setUseCaches(true);
        if (conn.getResponseCode() == 200){
            InputStream inputStream = conn.getInputStream();
            return BitmapFactory.decodeStream(inputStream);
        }
        return null;
    }
}
