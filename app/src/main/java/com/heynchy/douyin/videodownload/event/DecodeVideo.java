package com.heynchy.douyin.videodownload.event;


public class DecodeVideo {
    /**
     * 作者
     */
    private String author;
    /**
     * 标题
     */
    private String title;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 封面图
     */
    private String coverPicture;
    /**
     * 播放地址
     */
    private String playAddr;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCoverPicture() {
        return coverPicture;
    }

    public void setCoverPicture(String coverPicture) {
        this.coverPicture = coverPicture;
    }

    public String getPlayAddr() {
        return playAddr;
    }

    public void setPlayAddr(String playAddr) {
        this.playAddr = playAddr;
    }
}