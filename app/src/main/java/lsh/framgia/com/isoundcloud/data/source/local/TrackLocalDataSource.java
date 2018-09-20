package lsh.framgia.com.isoundcloud.data.source.local;

import lsh.framgia.com.isoundcloud.data.source.TrackDataSource;

public class TrackLocalDataSource implements TrackDataSource.LocalDataSource {

    private static TrackLocalDataSource sInstance;

    private TrackLocalDataSource() {
    }

    public static synchronized TrackLocalDataSource getInstance() {
        if (sInstance == null) {
            sInstance = new TrackLocalDataSource();
        }
        return sInstance;
    }
}
