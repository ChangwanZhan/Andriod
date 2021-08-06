package com.example.uscfilms.modal;

public class SliderData {

    // image url is used to
    // store the url of image
    private String posterPath;
    private String mediaId;
    private String mediaType;
    private String mediaName;
    private String backdropPath;

    // Constructor method.
    public SliderData(String posterPath, String mediaId, String mediaType, String mediaName, String backdropPath) {
        this.mediaId = mediaId;
        this.mediaType = mediaType;
        this.mediaName = mediaName;
        this.backdropPath = backdropPath;
        this.posterPath = posterPath;
    }

    // Getter method
    public String getMediaId() {return mediaId;}

    public String getMediaType() {return mediaType;}

    public String getMediaName() {return mediaName;}

    public String getBackdropPath() {return backdropPath;}

    public String getPosterPath() {return posterPath;}

}