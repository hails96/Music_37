package lsh.framgia.com.isoundcloud.data.source;

import java.util.List;

import lsh.framgia.com.isoundcloud.base.asynctask.BaseFetchingAsyncTask.OnResponseListener;
import lsh.framgia.com.isoundcloud.data.model.Playlist;
import lsh.framgia.com.isoundcloud.data.model.Track;

public interface TrackDataSource {
    interface LocalDataSource {
        void saveTrackToDatabase(Track track);

        void updateDownloadedTrack(long requestId, OnLocalResponseListener listener);

        boolean isDownloadedTrack(Track track);

        boolean isFavoriteTrack(Track track);

        void updateFavoriteTrack(Track track, OnLocalResponseListener<Boolean> listener);

        void getDownloadedTracks(OnLocalResponseListener<List<Track>> listener);

        void getFavoriteTracks(OnLocalResponseListener<List<Track>> listener);

        void deleteTrackFromDatabase(Track track, OnLocalResponseListener<Track> listener);

        void deleteTrackFromStorage(Track track);

        void createNewPlaylist(Playlist playlist, OnLocalResponseListener<Boolean> listener);

        void getPlaylists(OnLocalResponseListener<List<Playlist>> listener);

        void addTrackToNewPlaylist(Track track, Playlist playlist, OnLocalResponseListener<Boolean> listener);

        void addTrackToExistingPlaylist(Track track, Playlist playlist, OnLocalResponseListener<Boolean> listener);

        void getTracksFromPlaylist(Playlist playlist, OnLocalResponseListener<List<Track>> listener);

        void addNumberOfPlays(Playlist playlist);
    }

    interface RemoteDataSource {
        void getRemoteTracks(String genre, int offset, int limit,
                             OnResponseListener<List<Track>> listener);

        void getSearchTracks(String query, int offset, int limit,
                             OnResponseListener<List<Track>> listener);
    }

    interface OnLocalResponseListener<R> {
        void onSuccess(R result);

        void onFailure(String msg);
    }
}
