package com.xilu.wybz.bean;

/**
 * Created by June on 16/4/26.
 */
public class ShareBean {
    public String title;
    public String author;
    public String content;
    public String link;
    public String pic;
    public String playurl;
    public int type;
    public ShareBean(String title,String author,String content,String link,String pic,String playurl,int type){
        this.title = title;
        this.author = author;
        this.content = content;
        this.link = link;
        this.pic = pic;
        this.playurl = playurl;
        this.type = type;
    }
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getPlayurl() {
        return playurl;
    }

    public void setPlayurl(String playurl) {
        this.playurl = playurl;
    }
}
