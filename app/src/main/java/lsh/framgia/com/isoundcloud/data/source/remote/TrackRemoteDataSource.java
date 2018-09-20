package lsh.framgia.com.isoundcloud.data.source.remote;

import java.util.List;

import lsh.framgia.com.isoundcloud.base.asynctask.BaseFetchingAsyncTask.OnResponseListener;
import lsh.framgia.com.isoundcloud.data.model.Track;
import lsh.framgia.com.isoundcloud.data.source.TrackDataSource;
import lsh.framgia.com.isoundcloud.util.StringUtils;

public class TrackRemoteDataSource implements TrackDataSource.RemoteDataSource {

    private static TrackRemoteDataSource sInstance;

    private TrackRemoteDataSource() {
    }

    public static synchronized TrackRemoteDataSource getInstance() {
        if (sInstance == null) {
            sInstance = new TrackRemoteDataSource();
        }
        return sInstance;
    }

    @Override
    public void getTracks(String genre, int offset, int limit, OnResponseListener<List<Track>> listener) {
        new FetchingTrackAsyncTask()
                .setOnResponseListener(listener)
                .execute(StringUtils.formatFetchingTrackUrl(genre, offset, limit));
    }
}
