package lsh.framgia.com.isoundcloud.data.repository;

import java.util.List;

import lsh.framgia.com.isoundcloud.base.asynctask.BaseFetchingAsyncTask;
import lsh.framgia.com.isoundcloud.data.model.Track;
import lsh.framgia.com.isoundcloud.data.source.TrackDataSource;

public class TrackRepository implements TrackDataSource.LocalDataSource, TrackDataSource.RemoteDataSource {

    private static TrackRepository sInstance;
    private TrackDataSource.RemoteDataSource mTrackRemoteDataSource;
    private TrackDataSource.LocalDataSource mTrackLocalDataSource;

    private TrackRepository(TrackDataSource.RemoteDataSource trackRemoteDataSource,
                            TrackDataSource.LocalDataSource trackLocalDataSource) {
        mTrackRemoteDataSource = trackRemoteDataSource;
        mTrackLocalDataSource = trackLocalDataSource;
    }

    public static synchronized TrackRepository getInstance(TrackDataSource.RemoteDataSource remote,
                                                           TrackDataSource.LocalDataSource local) {
        if (sInstance == null) {
            sInstance = new TrackRepository(remote, local);
        }
        return sInstance;
    }

    @Override
    public void getRemoteTracks(String genre, int offset, int limit,
                                BaseFetchingAsyncTask.OnResponseListener<List<Track>> listener) {
        mTrackRemoteDataSource.getRemoteTracks(genre, offset, limit, listener);
    }

    @Override
    public void getSearchTracks(String query, int offset, int limit,
                                BaseFetchingAsyncTask.OnResponseListener<List<Track>> listener) {
        mTrackRemoteDataSource.getSearchTracks(query, offset, limit, listener);
    }

    @Override
    public void saveTrackToDatabase(Track track, TrackDataSource.OnLocalResponseListener listener) {
        mTrackLocalDataSource.saveTrackToDatabase(track, listener);
    }

    @Override
    public void updateDownloadedTrack(long requestId, TrackDataSource.OnLocalResponseListener listener) {
        mTrackLocalDataSource.updateDownloadedTrack(requestId, listener);
    }

    @Override
    public boolean isTrackDownloaded(Track track) {
        return mTrackLocalDataSource.isTrackDownloaded(track);
    }
}
