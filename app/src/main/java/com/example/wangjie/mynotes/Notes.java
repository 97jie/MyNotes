package com.example.wangjie.mynotes;

import java.io.Serializable;

public class Notes implements Serializable{
    private int id;
    private String time;
    private String content;
    private String img_path;
    private String video_path;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getVideo_path() {
        return video_path;
    }

    public void setVideo_path(String video_path) {
        this.video_path = video_path;
    }

    public String getImg_path() {

        return img_path;
    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    @Override
    public String toString() {
        return "Notes{" +
                "id=" + id +
                ", time='" + time + '\'' +
                ", content='" + content + '\'' +
                ", img_path='" + img_path + '\'' +
                ", video_path='" + video_path + '\'' +
                '}';
    }
}
