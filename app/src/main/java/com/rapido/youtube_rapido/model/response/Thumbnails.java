package com.rapido.youtube_rapido.model.response;

import java.io.Serializable;

public class Thumbnails  implements Serializable {
    ThumbnailData medium;
    ThumbnailData high;
    ThumbnailData standard;
    ThumbnailData maxres;

    public ThumbnailData getMedium() {
        return medium;
    }

    public void setMedium(ThumbnailData medium) {
        this.medium = medium;
    }

    public ThumbnailData getHigh() {
        return high;
    }

    public void setHigh(ThumbnailData high) {
        this.high = high;
    }

    public ThumbnailData getStandard() {
        return standard;
    }

    public void setStandard(ThumbnailData standard) {
        this.standard = standard;
    }

    public ThumbnailData getMaxres() {
        return maxres;
    }

    public void setMaxres(ThumbnailData maxres) {
        this.maxres = maxres;
    }
}