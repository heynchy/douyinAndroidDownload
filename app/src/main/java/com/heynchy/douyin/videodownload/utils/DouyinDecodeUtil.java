package com.heynchy.douyin.videodownload.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.heynchy.douyin.videodownload.event.DecodeVideo;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.StrFormatter;
import cn.hutool.core.text.UnicodeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;

public class DouyinDecodeUtil {

//    public static void main(String[] args) throws Exception {
//        String url = "#在抖音，记录美好生活#蜈蚣精真是太厉害了，变幻成各路神仙大咖对抗悟空。真是太精彩了！！安排！ https://v.douyin.com/x6bSkQ/ 复制此链接，打开【抖音短视频】，直接观看视频！";
//        DecodeVideo video = decode(url).orElseThrow(() -> new Exception("Parsing failed."));
////        System.out.println(JSONUtil.toJsonPrettyStr(video));
//        Log.i("chy1234","")
//    }

    public static Optional<DecodeVideo> decode(String url) {
        Document doc = Common.jsonpConnect(url, true);
        // 获取作者信息
        Elements authorElements = doc.select("[class=\"user-info-name\"]");
        String author = StrUtil.removePrefix(authorElements.get(0).text(), "@");
        // 获取头像
        Elements avatarElements = doc.select("[class=\"img-avator\"]");
        String avatar = avatarElements.attr("src").trim();

        // 构建基本信息
        DecodeVideo video  = new DecodeVideo();
        video.setAuthor(author);
        video.setAvatar(avatar);
//        DecodeVideo video = DecodeVideo.builder().author(author).avatar(avatar).build();
        // 获取itemInfo详细信息
        Elements scriptElements = doc.getElementsByTag("script");
        for (Element elements : scriptElements) {
            if (StrUtil.containsAny(elements.toString(), "douyin_falcon:page/reflow_video/index")) {
                String text = StrUtil.removeAll(StrUtil.removeAll(StrUtil.subBetween(elements.toString(), "({", "})"), '"')).trim();
                List<String> keys = StrUtil.splitTrim(text, ",");
                if (CollUtil.isNotEmpty(keys)) {
                    HashMap<String, String> params = MapUtil.newHashMap();
                    for (String key : keys) {
                        List<String> list = StrUtil.splitTrim(key, ":");
                        params.put(list.get(0), list.get(1));
                    }
                    String itemId = params.get("itemId");
                    String token = params.get("dytk");
                    String authorName = UnicodeUtil.toString(params.get("authorName"));
                    String itemInfoUrl = StrFormatter.format("https://www.iesdouyin.com/web/api/v2/aweme/iteminfo/?item_ids={}&dytk={}", itemId, token);
                    String body = HttpUtil.get(itemInfoUrl);
                    if (StrUtil.isNotEmpty(body)) {
                        JSON json = JSONUtil.parse(body);
                        String statusCode = JSONUtil.getByPath(json, "status_code").toString();
                        if (StrUtil.equals(statusCode, "0")) {
                            String desc = JSONUtil.getByPath(json, "item_list[0].desc").toString();
                            String addr = JSONUtil.getByPath(json, "item_list[0].video.play_addr.url_list[0]").toString();
                            String cover = JSONUtil.getByPath(json, "item_list[0].video.origin_cover.url_list[0]").toString();
                            String playAddr = Common.getLocationUrl(addr);
                            video.setAuthor(author);
                            video.setAvatar(avatar);
                            video.setTitle(desc);
                            video.setPlayAddr(playAddr);
                            video.setCoverPicture(cover);
//                            video = video.toBuilder().author(author).avatar(avatar).title(desc).playAddr(playAddr).coverPicture(cover).build();
                            break;
                        }
                    }
                }
            }
        }
        Log.i("chy1234","Optional==="+new Gson().toJson(video));
        return Optional.ofNullable(video);
    }
}
