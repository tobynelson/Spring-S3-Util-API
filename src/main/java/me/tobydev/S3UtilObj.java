package me.tobydev;

import com.amazonaws.services.s3.model.S3ObjectSummary;

import java.net.URL;

public class S3UtilObj {
    private String title;
    private URL dataUrl;
    private S3ObjectSummary objectSummary;

    public S3UtilObj() {  }

    public S3UtilObj(String title) {
        this.setTitle(title);
    }

    public URL getDataUrl() { return this.dataUrl; }

    public void setDataUrl(URL dataUrl) { this.dataUrl = dataUrl; }

    public String getTitle() { return title; }

    public void setTitle(String title) {
        this.title = title;
    }

    public S3ObjectSummary getObjectSummary() { return this.objectSummary; }

    public void setSummary(S3ObjectSummary summary) { this.objectSummary = summary; }


    @Override
    public String toString() {
        return "S3UtilObj{" +
                ", title='" + title + '\'' +
                '}';
    }
}
