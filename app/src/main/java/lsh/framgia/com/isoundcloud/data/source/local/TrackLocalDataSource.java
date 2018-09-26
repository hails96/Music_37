package lsh.framgia.com.isoundcloud.data.source.local;

import android.content.Context;

import lsh.framgia.com.isoundcloud.data.model.Track;
import lsh.framgia.com.isoundcloud.data.source.TrackDataSource;
import lsh.framgia.com.isoundcloud.data.source.TrackDataSource.OnLocalResponseListener;
import lsh.framgia.com.isoundcloud.data.source.local.sqlite.TrackDatabaseHelper;

public class TrackLocalDataSource implements TrackDataSource.LocalDataSource {

    private static TrackLocalDataSource sInstance;
    private TrackDatabaseHelper mTrackDatabaseHelper;

    private TrackLocalDataSource(Context context) {
        mTrackDatabaseHelper = TrackDatabaseHelper.getInstance(context);
    }

    public static synchronized TrackLocalDataSource getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new TrackLocalDataSource(context);
        }
        return sInstance;
    }

    @Override
    public void saveTrackToDatabase(Track track) {
        mTrackDatabaseHelper.saveTrack(track);
    }

    @Override
    public void updateDownloadedTrack(long requestId, OnLocalResponseListener listener) {
        mTrackDatabaseHelper.updateDownloadedTrack(requestId, listener);
    }

    @Override
    public boolean isDownloadedTrack(Track track) {
        return mTrackDatabaseHelper.isDownloadedTrack(track);
    }

    @Override
    public boolean isFavoriteTrack(Track track) {
        return mTrackDatabaseHelper.isFavoriteTrack(track);
    }

    @Override
    public void updateFavoriteTrack(Track track, OnLocalResponseListener<Boolean> listener) {
        mTrackDatabaseHelper.updateFavoriteTrack(track, listener);
    }
}
