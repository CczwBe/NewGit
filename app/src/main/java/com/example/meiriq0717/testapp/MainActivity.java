package com.example.meiriq0717.testapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.meiriq0717.testapp.sdcard.SdCardHelper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_download, btn_show,btn_nextpage;
    private ImageView pic_iv;
    private static final String img_url = "http://meiriq-file.b0.upaiyun.com/play-meiriq/avatar/1442828465c95c9fa67444a026318f6f563544b4b5.png ";
    private SdCardHelper helper;
    private static final String TAG = "MainActivity";
    private String fileName = null;
    private String filename = "14428284653c87bcd5af032330742921effadccff8";
    private ProgressBar progressBar;
    private String str="wawawaw     d   sdsdssdsdsdsdsd ssdasfafaffss";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        helper = new SdCardHelper(MainActivity.this);
        initView();
    }

    private void initView() {
        btn_download = (Button) findViewById(R.id.download);
        btn_show = (Button) findViewById(R.id.showpic);
        pic_iv = (ImageView) findViewById(R.id.pic);
        btn_show.setOnClickListener(this);
        btn_download.setOnClickListener(this);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btn_nextpage= (Button) findViewById(R.id.btn_nextPager);
        btn_nextpage.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.download:
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHH:mm:ss");
                Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                fileName = formatter.format(curDate);
                Log.i(TAG, fileName + " ==============================  ");
                new MyDownLoadPicTask().execute(img_url);
                break;
            case R.id.showpic:
                byte[] data = helper.loadDataFromSdCard(filename + ".png");
                if (data != null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    pic_iv.setImageBitmap(bitmap);
                } else {
                    Toast.makeText(MainActivity.this, "头像读取失败  （自SdCard缓存中）", Toast.LENGTH_SHORT).show();
                }
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this, R.style.PopMenu).create();
                View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_laayout, null);
                Window window = dialog.getWindow();
                WindowManager windowManager = window.getWindowManager();
                WindowManager.LayoutParams lp=window.getAttributes();
                lp.width=400;
                lp.height=200;
                window.setAttributes(lp);
                dialog.show();
                dialog.setContentView(view);
                break;
            case R.id.btn_nextPager:
                Intent intent=new Intent(MainActivity.this,SecondActivity.class);
                startActivity(intent);
                break;
        }
    }

    //下载图片转化成byte[]类型数据
    private byte[] downLoadPic(String img_url) {
        HttpURLConnection connection = null;
        BufferedInputStream bis = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            URL url = new URL(img_url);
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10 * 1000);
            connection.setDoInput(true);
            connection.connect();
            if (connection.getResponseCode() == 200) {
                bis = new BufferedInputStream(connection.getInputStream());
                byte[] bytes = new byte[1024 * 4];
                int len = 0;
                while ((len = bis.read(bytes)) != -1) {
                    baos.write(bytes, 0, len);
                    baos.flush();
                }
                return baos.toByteArray();
            } else {
                return null;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }

    //异步下载图片类
    public class MyDownLoadPicTask extends AsyncTask<String, Void, byte[]> {
        @Override
        protected byte[] doInBackground(String... params) {
            return downLoadPic(params[0]);
        }

        @Override
        protected void onPostExecute(byte[] bytes) {
            super.onPostExecute(bytes);

            helper.saveData2SdCard(bytes, filename + ".png");
            Log.i(TAG, "=========save   Finish==========");
        }
    }
}
