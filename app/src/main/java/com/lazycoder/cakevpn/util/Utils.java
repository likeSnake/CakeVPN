package com.lazycoder.cakevpn.util;

import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lazycoder.cakevpn.R;
import com.lazycoder.cakevpn.bean.ServerBean;

import java.util.ArrayList;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

public class Utils {

    /**
     * Convert drawable image resource to string
     *
     * @param resourceId drawable image resource
     * @return image path
     */
    public static String getImgURL(int resourceId) {

        // Use BuildConfig.APPLICATION_ID instead of R.class.getPackage().getName() if both are not same
        return Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + resourceId).toString();
    }

    public static ArrayList<ServerBean> getServerList(String configServer){
        String decrypt = AesTools.decrypt(configServer);
        JSONObject jsonObject = JSONUtil.parseObj(decrypt);
        String wa_servers = jsonObject.getJSONArray("wa_servers").toString();

        return (ArrayList<ServerBean>)new Gson().fromJson(wa_servers,new TypeToken<ArrayList<ServerBean>>(){}.getType());
    }

    public static void MyLog(Object s){
        System.out.println("------->"+s);
    }

    public static int getCountryFlag(String country){
        switch (country){
            case "US":{
                return R.drawable.us;

            }
            case "GB":
            case "UK": {
                return R.drawable.sh;

            }

            case "DE":{
                return R.drawable.de;

            }

            case "FR":{
                return R.drawable.fr;

            }

            case "JP":{
                return R.drawable.jp;

            }

            case "AU":{
                return R.drawable.au;

            }

            case "SG":{
                return R.drawable.sg;

            }
            default:
                return R.drawable.us;
        }
    }
}
