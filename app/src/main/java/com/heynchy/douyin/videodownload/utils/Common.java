package com.heynchy.douyin.videodownload.utils;


import android.text.TextUtils;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.HashMap;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;


public class Common {
 
    public static final String RE_URLS = "(https://|http://)?([\\w-]+\\.)+[\\w-]+(:\\d+)*(/[\\w- ./?%&=]*)?";
 
    public static Document jsonpConnect(String url, Boolean isMobile) {
        Document doc = null;
        if (!TextUtils.isEmpty(url)) {
            try {
                Connection connect = Jsoup.connect(ReUtil.get(Common.RE_URLS, url, 0));
                if (isMobile) {
                    connect.headers(Common.getMobileHeaders());
                }
                doc = connect.timeout(5000).get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return doc;
    }
 
    public static String getLocationUrl(String url) {
        HashMap headers = getMobileHeaders();
        HttpResponse execute = HttpUtil.createGet(url).addHeaders(headers).execute();
        String redirectUrl = execute.header("Location");
        return redirectUrl;
    }
 
    public static HashMap<String, String> getMobileHeaders() {
        HashMap headers = MapUtil.newHashMap();
        headers.put("User-Agent", "Mozilla/5.0 (Linux; Android 8.0; Pixel 2 Build/OPD3.170816.012) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.70 Mobile Safari/537.36");
        return headers;
    }
}