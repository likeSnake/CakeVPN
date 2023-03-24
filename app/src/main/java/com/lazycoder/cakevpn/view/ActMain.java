package com.lazycoder.cakevpn.view;

import static com.lazycoder.cakevpn.data.FirebaseData.MY_FIRE_BASE_TEST_VPN_SERVERS;
import static com.lazycoder.cakevpn.data.FirebaseData.VPN_CITY;
import static com.lazycoder.cakevpn.data.FirebaseData.VPN_SERVERS;
import static com.lazycoder.cakevpn.util.Utils.MyLog;
import static com.lazycoder.cakevpn.util.Utils.getCountryFlag;
import static com.lazycoder.cakevpn.util.Utils.getServerList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.VpnService;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.RemoteException;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.lazycoder.cakevpn.R;
import com.lazycoder.cakevpn.bean.ServerBean;
import com.lazycoder.cakevpn.util.AesTools;
import com.lazycoder.cakevpn.util.DownTask;
import com.lazycoder.cakevpn.util.Utils;
import com.tencent.mmkv.MMKV;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import de.blinkt.openvpn.OpenVpnApi;
import de.blinkt.openvpn.core.OpenVPNThread;
import soup.neumorphism.NeumorphButton;
import soup.neumorphism.NeumorphTextView;
import soup.neumorphism.ShapeType;

public class ActMain extends AppCompatActivity implements View.OnClickListener {
    private static Boolean isSwitch = false;
    private static int VPN_CODE = 20;
    private int ids;
    private AlertDialog dialogs;
    private static boolean vpnStart = false;
    private NeumorphButton tvServerCountry;
    private NeumorphButton tv_server_desc;
    private NeumorphTextView tvVpnConnectStatus;
    private NeumorphTextView speed;
    private FrameLayout fltSelectLocation;
    private NeumorphButton ivToggle;
    private FrameLayout adsTopContainer;
    private FrameLayout adsBottomContainer;
    private ImageView  cn_icon;
    ArrayList<ServerBean> list ;
    private AVLoadingIndicatorView av;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        initUI();

        initData();

    }

    public void initData(){
        if (list!=null){
            list.clear();
        }
        list = (ArrayList<ServerBean>) getIntent().getSerializableExtra(VPN_SERVERS);
        if (list!=null){
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getSelected()){
                    tvServerCountry.setText(list.get(i).getWa_ser_city());
                    String wa_ser_country = list.get(i).getWa_ser_country();
                    cn_icon.setImageResource(getCountryFlag(wa_ser_country));
                    ids = i;

                    if (vpnStart){
                        isSwitch = true;
                        switchServers();
                    }else {
                        isSwitch = false;
                    }
                }
            }
        }else {
            String s = MMKV.defaultMMKV().decodeString(MY_FIRE_BASE_TEST_VPN_SERVERS);
            Utils.MyLog(s);
            initVpnServers(s);
        }
    }
    private void initUI() {
        fltSelectLocation = findViewById(R.id.flt_select_server);
        ivToggle = findViewById(R.id.iv_vpn_connection);
        adsTopContainer = findViewById(R.id.ads_main_native_top);
        tvVpnConnectStatus = findViewById(R.id.tv_vpn_connect_status);
        adsBottomContainer = findViewById(R.id.ads_main_native_bottom);
        tvServerCountry = findViewById(R.id.tv_server_desc);
        cn_icon = findViewById(R.id.Top_flag);
        fltSelectLocation.setOnClickListener(this);
        ivToggle.setOnClickListener(this);


    }

    public void initVpnServers(String serverConfig){

        if (serverConfig!=null){
            list = getServerList(serverConfig);
            MyLog(list.size());
            ids = new Random().nextInt(list.size());
            list.get(ids).setSelected(true);
            String wa_ser_country = list.get(ids).getWa_ser_country();
            cn_icon.setImageResource(getCountryFlag(wa_ser_country));
            for (ServerBean serverBean : list) {
                if(serverBean.getSelected()==null){
                    serverBean.setSelected(false);
                }
            }
            tvServerCountry.setText("FastFast Smart");

        }
    }
    public void switchServers(){
        OpenVPNThread.stop();

    }
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.flt_select_server:
                Intent intent = new Intent(ActMain.this,ActNode.class);
                intent.putExtra(VPN_SERVERS,list);
                startActivity(intent);
                break;
            case R.id.iv_vpn_connection:
                isSwitch = false;
                Utils.MyLog(vpnStart);
                LoadingDialog();
                Intent intents = VpnService.prepare(this);
                if (intents != null) {
                    startActivityForResult(intents, VPN_CODE);
                } else startVpn();
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    private void startVpn() {
        if (!vpnStart) {
            MMKV.defaultMMKV().encode(VPN_CITY,list.get(ids).getWa_ser_city());
            ivToggle.setShapeType(ShapeType.PRESSED);
            ivToggle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F);
            ivToggle.setText("Disconnection");
            tvVpnConnectStatus.setText("Status: Connected");
            String wa_ser_cert = list.get(ids).getWa_ser_cert();
            String wa_ser_city = list.get(ids).getWa_ser_city();
            MyLog(wa_ser_cert);
            DownTask downTask = new DownTask(new DownTask.OnDownloadCompleteListener() {
                @Override
                public void onDownloadComplete(String s) {
                    try {
                        OpenVpnApi.startVpn(ActMain.this, s, wa_ser_city);
                        vpnStart = true;
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            downTask.execute(wa_ser_cert);
        }else {
            OpenVPNThread.stop();
            vpnStart = false;
            ivToggle.setShapeType(ShapeType.FLAT);
            ivToggle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F);
            ivToggle.setText("Connect");
            tvVpnConnectStatus.setText("Status: Not Connected");
        }
    }
    public void rebootVpn(){
        ivToggle.setShapeType(ShapeType.PRESSED);
        ivToggle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F);
        ivToggle.setText("Disconnection");
        tvVpnConnectStatus.setText("Status: Connected");
        MMKV.defaultMMKV().encode(VPN_CITY,list.get(ids).getWa_ser_city());
        LoadingDialog();
        DownTask downTask = new DownTask(new DownTask.OnDownloadCompleteListener() {
            @Override
            public void onDownloadComplete(String s) {
                try {
                    OpenVpnApi.startVpn(ActMain.this, s, list.get(ids).getWa_ser_city());
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        downTask.execute(list.get(ids).getWa_ser_cert());
    }
    //VPN服务监听
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            try {
               if(intent.getStringExtra("state")!=null) {
                   MyLog(intent.getStringExtra("state"));
                   if (intent.getStringExtra("state").equals("CONNECTED")) {
                       vpnStart = true;
                       dialogs.dismiss();
                       av.hide();
                       startActivity(new Intent(ActMain.this, ActConn.class));
                   }else if (intent.getStringExtra("state").equals("DISCONNECTED")){
                       vpnStart = false;
                       if (!isSwitch) {
                           dialogs.dismiss();
                           av.hide();
                           startActivity(new Intent(ActMain.this, ActConn.class).putExtra("openai", "DISCONNECTED"));
                       }else {
                           rebootVpn();
                       }
                   }
               }
                String duration = intent.getStringExtra("duration");
                String lastPacketReceive = intent.getStringExtra("lastPacketReceive");
                String byteIn = intent.getStringExtra("byteIn");
                String byteOut = intent.getStringExtra("byteOut");

                if (duration == null) duration = "00:00:00";
                if (lastPacketReceive == null) lastPacketReceive = "0";
                if (byteIn == null) byteIn = " ";
                if (byteOut == null) byteOut = " ";
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

    public void LoadingDialog(){
        AlertDialog.Builder builder = new
                AlertDialog.Builder(this);
        dialogs = builder.create();
        View dialogView = null;
        //设置对话框布局
        dialogView = View.inflate(this,
                R.layout.dialog_loading, null);
        dialogs.setView(dialogView);
        dialogs.setCancelable(false);
        dialogs.setCanceledOnTouchOutside(false);
        dialogs.getWindow().setBackgroundDrawableResource(com.google.android.material.R.color.mtrl_btn_transparent_bg_color);
        dialogs.show();
        av = dialogView.findViewById(R.id.avi);

        av.show();

    }

   
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==VPN_CODE){
            if (resultCode==RESULT_OK) {
                startVpn();
            }else {
                dialogs.dismiss();
            }
        }
    }
}