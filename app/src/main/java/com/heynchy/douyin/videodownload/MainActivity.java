package com.heynchy.douyin.videodownload;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.heynchy.douyin.videodownload.event.DecodeVideo;
import com.heynchy.douyin.videodownload.utils.DouyinDecodeUtil;
import com.heynchy.douyin.videodownload.utils.DownloadUtil;
import com.heynchy.douyin.videodownload.utils.PermissionUtil;
import com.heynchy.douyin.videodownload.utils.ToastUtil;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private long mWaitTime = 2000;
    private long mTouchTime = 0;
    private EditText mUrlEt;
    private Button mDownloadBtn;
    private static TextView mProgressTv;
   private static Handler handler = new Handler(){
       @Override
       public void handleMessage( Message msg) {
           switch (msg.what){
               case 0:
                   ToastUtil.getInstance().showToast("视频url异常");
                   break;
               case 1:
                   mProgressTv.setText(" 0% ");
                   ToastUtil.getInstance().showToast("视频下载成功");
                   break;
               case 2:
                   ToastUtil.getInstance().showToast("视频下载失败");
                   break;
               case 3:
                   ToastUtil.getInstance().showToast("视频地址为空");
                   break;
               case 4:
                   Log.i("","");
                   mProgressTv.setText(msg.obj.toString()+" % ");
                   break;
           }
       }
   };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setListener();
    }
    private void initView(){
        mUrlEt = findViewById(R.id.et_url);
        mDownloadBtn = findViewById(R.id.btn_download);
        mProgressTv = findViewById(R.id.tv_progress);
        PermissionUtil.getPermissions(this);
    }
    private void setListener(){
        mDownloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url =mUrlEt.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        download(url);
                    }
                }).start();
            }
        });
    }

    private void download(String url){
//        url="#在抖音，记录美好生活#蜈蚣精真是太厉害了，变幻成各路神仙大咖对抗悟空。" +
//                "真是太精彩了！！安排！ https://v.douyin.com/x6bSkQ/ 复制此链接，打开" +
//                "【抖音短视频】，直接观看视频！";
        DecodeVideo video = null;
        Log.i("chy1234","url==="+ url);
        if (TextUtils.isEmpty(url)){
            Message msg = new Message();
            msg.what = 3;
            handler.sendMessage(msg);
            return;
        }
        try {
            video = DouyinDecodeUtil.decode(url).orElseThrow(() -> new Exception("Parsing failed."));
        } catch (Throwable throwable) {
            Message msg = new Message();
            msg.what = 0;
            handler.sendMessage(msg);
            return;
        }
        if (video != null){
            String savePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/DouYinDownload/";
            DownloadUtil.get().download(video.getPlayAddr(), savePath,
                        video.getTitle()+".mp4", new DownloadUtil.OnDownloadListener() {
                            @Override
                            public void onDownloadSuccess(File file) {
                                Message msg = new Message();
                                msg.what = 1;
                                handler.sendMessage(msg);
                            }

                            @Override
                            public void onDownloading(int progress) {
                                Log.i("chy1234","progress==="+progress);
                                Message msg = new Message();
                                msg.what = 4;
                                msg.obj = progress;
                                handler.sendMessage(msg);
                            }

                            @Override
                            public void onDownloadFailed(Exception e) {
                                Log.i("chy1234","onDownloading==="+e.getMessage());
                                Message msg = new Message();
                                msg.what = 2;
                                handler.sendMessage(msg);
                            }
                        });
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long currentTime = System.currentTimeMillis();
            if ((currentTime - mTouchTime) >= mWaitTime) {
                ToastUtil.getInstance().showToast("再按一次返回桌面");
                mTouchTime = currentTime;
            } else {
                Intent home = new Intent(Intent.ACTION_MAIN);
                home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                home.addCategory(Intent.CATEGORY_HOME);
                startActivity(home);
            }
        }
        return true;
    }
}
