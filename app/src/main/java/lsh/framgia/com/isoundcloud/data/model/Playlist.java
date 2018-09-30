package lsh.framgia.com.isoundcloud.data.model;

import java.util.List;

public class Playlist {

    private int mId;
    private String mName;
    private long mCreatedDate;
    private int mNumberOfPlays;
    private List<Track> mTracks;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public long getCreatedDate() {
        return mCreatedDate;
    }

    public void setCreatedDate(long createdDate) {
        mCreatedDate = createdDate;
    }

    public int getNumberOfPlays() {
        return mNumberOfPlays;
    }

    public void setNumberOfPlays(int numberOfPlays) {
        mNumberOfPlays = numberOfPlays;
    }

    public List<Track> getTracks() {
        return mTracks;
    }

    public void setTracks(List<Track> tracks) {
        mTracks = tracks;
    }
}
