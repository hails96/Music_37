package lsh.framgia.com.isoundcloud.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import lsh.framgia.com.isoundcloud.constant.TrackEntity;

public class Track implements Parcelable {

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
    private int mGenreArtworkResId;
    private boolean mIsFavorite;

    public Track(JSONObject trackObject) {
        JSONObject publisherObject = trackObject.optJSONObject(TrackEntity.PUBLISHER_METADATA);
        mArtworkUrl = trackObject.optString(TrackEntity.ARTWORK_URL);
        mDescription = trackObject.optString(TrackEntity.DESCRIPTION);
        mIsDownloadable = trackObject.optBoolean(TrackEntity.IS_DOWNLOADABLE);
        mDownloadUrl = trackObject.optString(TrackEntity.DOWNLOAD_URL);
        mDuration = trackObject.optInt(TrackEntity.DURATION);
        mTitle = trackObject.optString(TrackEntity.TITLE);
        mUri = trackObject.optString(TrackEntity.URI);
        if (publisherObject != null) {
            mArtist = publisherObject.optString(TrackEntity.ARTIST);
        }
    }

    protected Track(Parcel in) {
        mId = in.readString();
        mTitle = in.readString();
        mArtist = in.readString();
        mDuration = in.readInt();
        mUri = in.readString();
        mStreamUrl = in.readString();
        mArtworkUrl = in.readString();
        mIsDownloadable = in.readByte() != 0;
        mDownloadUrl = in.readString();
        mDescription = in.readString();
        mGenreArtworkResId = in.readInt();
        mIsFavorite = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mTitle);
        dest.writeString(mArtist);
        dest.writeInt(mDuration);
        dest.writeString(mUri);
        dest.writeString(mStreamUrl);
        dest.writeString(mArtworkUrl);
        dest.writeByte((byte) (mIsDownloadable ? 1 : 0));
        dest.writeString(mDownloadUrl);
        dest.writeString(mDescription);
        dest.writeInt(mGenreArtworkResId);
        dest.writeByte((byte) (mIsFavorite ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Track> CREATOR = new Creator<Track>() {
        @Override
        public Track createFromParcel(Parcel in) {
            return new Track(in);
        }

        @Override
        public Track[] newArray(int size) {
            return new Track[size];
        }
    };

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

    public void setIsDownloadable(boolean downloadable) {
        this.mIsDownloadable = downloadable;
    }

    public String getDownloadUrl() {
        return mDownloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        mDownloadUrl = downloadUrl;
    }

    public int getGenreArtworkResId() {
        return mGenreArtworkResId;
    }

    public void setGenreArtworkResId(int genreResId) {
        mGenreArtworkResId = genreResId;
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

    public boolean isFavorite() {
        return mIsFavorite;
    }

    public void setIsFavorite(boolean isFavorite) {
        mIsFavorite = isFavorite;
    }
}
