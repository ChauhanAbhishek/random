package com.rapido.youtube_rapido.model.response;

import java.io.Serializable;

public class ThumbnailData  implements Serializable {
    private String url;
    private float width;
    private float height;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }
}