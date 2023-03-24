package com.lazycoder.cakevpn.view;

import static com.lazycoder.cakevpn.data.FirebaseData.VPN_CITY;
import static com.lazycoder.cakevpn.util.Utils.MyLog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lazycoder.cakevpn.R;
import com.lazycoder.cakevpn.util.Utils;
import com.tencent.mmkv.MMKV;

import soup.neumorphism.NeumorphImageView;
import soup.neumorphism.NeumorphTextView;

public class ActConn extends AppCompatActivity implements View.OnClickListener{

    private NeumorphImageView iv_back;
    private NeumorphTextView speed;
    private NeumorphTextView tv_desc;
    private NeumorphTextView tv_status_title;
    private ImageView isConnect;
    private RelativeLayout layout1;
    private RelativeLayout layout2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_conn);
        initUi();
    }


    public void initUi(){
        speed = findViewById(R.id.speed);
        tv_desc = findViewById(R.id.tv_desc);
        isConnect = findViewById(R.id.isConnect);
        tv_status_title = findViewById(R.id.tv_status_title);
        iv_back = findViewById(R.id.iv_back);
        layout1 = findViewById(R.id.layout1);
        layout2 = findViewById(R.id.layout2);
        iv_back.setOnClickListener(this);

        if(getIntent().getStringExtra("openai")!=null){
            MyLog(getIntent().getStringExtra("openai"));
            layout1.setVisibility(View.GONE);
            layout2.setVisibility(View.GONE);
            isConnect.setVisibility(View.VISIBLE);
            tv_status_title.setText("Disconnected!");

        }else {
            layout1.setVisibility(View.VISIBLE);
            layout2.setVisibility(View.VISIBLE);
            isConnect.setVisibility(View.GONE);
            tv_status_title.setText("Success!");
            String s = MMKV.defaultMMKV().decodeString(VPN_CITY);
            if (s!=null){
                tv_desc.setText(s);
            }else {
                tv_desc.setText("US");
            }
        }
    }
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("Conn连接成功");

            try {
                String duration = intent.getStringExtra("duration");
                String lastPacketReceive = intent.getStringExtra("lastPacketReceive");
                String byteIn = intent.getStringExtra("byteIn");
                String byteOut = intent.getStringExtra("byteOut");

                if (duration == null) duration = "00:00:00";
                if (lastPacketReceive == null) lastPacketReceive = "0";
                if (byteIn == null) byteIn = " ";
                if (byteOut == null) byteOut = " ";
                String[] split = byteIn.split("-");

                speed.setText(split[0]);
                MyLog(duration + "--" + lastPacketReceive + "--" + byteIn + "--" + byteOut);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    };


    @Override
    protected void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("connectionState"));
        super.onResume();
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
        }
    }
}