package com.lazycoder.cakevpn.view;

import static com.lazycoder.cakevpn.data.FirebaseData.DEFAULT_SERVERS;
import static com.lazycoder.cakevpn.data.FirebaseData.MY_FIRE_BASE_TEST_VPN_SERVERS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.lazycoder.cakevpn.R;
import com.lazycoder.cakevpn.util.AesTools;
import com.lazycoder.cakevpn.util.Utils;
import com.tencent.mmkv.MMKV;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.HashMap;
import java.util.Map;

public class ActStart extends AppCompatActivity {

    private AVLoadingIndicatorView av;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_start);
        MMKV.initialize(this);
        initUI();
        initFireBase();
    }

    public void initUI(){
        av = findViewById(R.id.avi);

        startAnim();
    }

    void startAnim(){
        av.show();
    }
    void stopAnim(){
        av.hide();
    }
    public void initFireBase(){
        FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();

        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(0)
                .build();
        remoteConfig.setConfigSettingsAsync(configSettings);
        Map<String,Object> map = new HashMap<>();
        map.put("my_test","123456");


        remoteConfig.fetchAndActivate()
                .addOnCompleteListener(this, new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        if (task.isSuccessful()) {
                            Utils.MyLog("远程配置成功");
                            remoteConfig.activate();
                            String server = remoteConfig.getString("my_test");
                            MMKV.defaultMMKV().encode(MY_FIRE_BASE_TEST_VPN_SERVERS, server);
                            startActivity(new Intent(ActStart.this,ActMain.class));
                            stopAnim();
                            finish();
                        }else {
                            Utils.MyLog("远程配置失败");
                            MMKV.defaultMMKV().encode(MY_FIRE_BASE_TEST_VPN_SERVERS, DEFAULT_SERVERS);
                            startActivity(new Intent(ActStart.this,ActMain.class));
                            stopAnim();
                            finish();
                        }
                    }
                });



    }
}