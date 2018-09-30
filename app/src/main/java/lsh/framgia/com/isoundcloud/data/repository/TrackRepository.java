package lsh.framgia.com.isoundcloud.data.repository;

import java.util.List;

import lsh.framgia.com.isoundcloud.base.asynctask.BaseFetchingAsyncTask;
import lsh.framgia.com.isoundcloud.data.model.Playlist;
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
    public void saveTrackToDatabase(Track track) {
        mTrackLocalDataSource.saveTrackToDatabase(track);
    }

    @Override
    public void updateDownloadedTrack(long requestId, TrackDataSource.OnLocalResponseListener listener) {
        mTrackLocalDataSource.updateDownloadedTrack(requestId, listener);
    }

    @Override
    public boolean isDownloadedTrack(Track track) {
        return mTrackLocalDataSource.isDownloadedTrack(track);
    }

    @Override
    public boolean isFavoriteTrack(Track track) {
        return mTrackLocalDataSource.isFavoriteTrack(track);
    }

    @Override
    public void updateFavoriteTrack(Track track, TrackDataSource.OnLocalResponseListener<Boolean> listener) {
        mTrackLocalDataSource.updateFavoriteTrack(track, listener);
    }

    @Override
    public void getDownloadedTracks(TrackDataSource.OnLocalResponseListener<List<Track>> listener) {
        mTrackLocalDataSource.getDownloadedTracks(listener);
    }

    @Override
    public void getFavoriteTracks(TrackDataSource.OnLocalResponseListener<List<Track>> listener) {
        mTrackLocalDataSource.getFavoriteTracks(listener);
    }

    @Override
    public void deleteTrackFromDatabase(Track track, TrackDataSource.OnLocalResponseListener<Track> listener) {
        mTrackLocalDataSource.deleteTrackFromDatabase(track, listener);
    }

    @Override
    public void deleteTrackFromStorage(Track track) {
        mTrackLocalDataSource.deleteTrackFromStorage(track);
    }

    @Override
    public void createNewPlaylist(Playlist playlist, TrackDataSource.OnLocalResponseListener<Boolean> listener) {
        mTrackLocalDataSource.createNewPlaylist(playlist, listener);
    }

    @Override
    public void getPlaylists(TrackDataSource.OnLocalResponseListener<List<Playlist>> listener) {
        mTrackLocalDataSource.getPlaylists(listener);
    }
}
