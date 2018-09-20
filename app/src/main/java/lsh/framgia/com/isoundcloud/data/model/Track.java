package lsh.framgia.com.isoundcloud.data.model;

import org.json.JSONObject;

import lsh.framgia.com.isoundcloud.constant.TrackEntity;

public class Track {

    private String mId;
    private String mTitle;
    private String mArtist;
    private int mDuration;
    private String mUri;
    private String mStreamUrl;
    private String mArtworkUrl;
    private boolean mIsDownloadable;
    private String mDownloadUrl;
    private String mDescription;
    private boolean mIsFavorite;

    public Track(JSONObject trackObject) {
        JSONObject publisherObject = trackObject.optJSONObject(TrackEntity.PUBLISHER_METADATA);
        mArtworkUrl = trackObject.optString(TrackEntity.ARTWORK_URL);
        mDescription = trackObject.optString(TrackEntity.DESCRIPTION);
        mIsDownloadable = trackObject.optBoolean(TrackEntity.IS_DOWNLOADABLE);
        mDownloadUrl = trackObject.optString(TrackEntity.DOWNLOAD_URL);
        mTitle = trackObject.optString(TrackEntity.TITLE);
        mUri = trackObject.optString(TrackEntity.URI);
        if (publisherObject != null) {
            mArtist = publisherObject.optString(TrackEntity.ARTIST);
        }
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getArtist() {
        return mArtist;
    }

    public void setArtist(String artist) {
        mArtist = artist;
    }

    public int getDuration() {
        return mDuration;
    }

    public void setDuration(int duration) {
        mDuration = duration;
    }

    public String getStreamUrl() {
        return mStreamUrl;
    }

    public void setStreamUrl(String streamUrl) {
        mStreamUrl = streamUrl;
    }

    public String getArtworkUrl() {
        return mArtworkUrl;
    }

    public void setArtworkUrl(String artworkUrl) {
        mArtworkUrl = artworkUrl;
    }

    public boolean isDownloadable() {
        return mIsDownloadable;
    }

    public void setDownloadable(boolean downloadable) {
        this.mIsDownloadable = downloadable;
    }

    public String getDownloadUrl() {
        return mDownloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        mDownloadUrl = downloadUrl;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getUri() {
        return mUri;
    }

    public void setUri(String uri) {
        mUri = uri;
    }

    public boolean isIsFavorite() {
        return mIsFavorite;
    }

    public void setIsFavorite(boolean isFavorite) {
        mIsFavorite = isFavorite;
    }
}
