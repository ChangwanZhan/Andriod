package com.example.uscfilms.modal;

public class WatchlistData {
    private String posterPath;
    private String mediaId;
    private String mediaType;
    private String mediaName;
    private String backdropPath;

    // Constructor method.
    public WatchlistData(String mediaId, String mediaType, String mediaName, String backdropPath, String posterPath) {
        this.posterPath = posterPath;
        this.mediaId = mediaId;
        this.mediaType = mediaType;
        this.mediaName = mediaName;
        this.backdropPath = backdropPath;
    }

    // Getter method
    public String getPosterPath() {
        return posterPath;
    }

    public String getMediaId() {return mediaId;}

    public String getMediaType() {return mediaType;}

    public String getMediaName() {return mediaName;}

    public String getBackdropPath() {return backdropPath;}
}
