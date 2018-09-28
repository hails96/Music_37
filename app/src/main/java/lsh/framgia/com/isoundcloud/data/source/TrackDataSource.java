package lsh.framgia.com.isoundcloud.data.source;

import java.util.List;

import lsh.framgia.com.isoundcloud.base.asynctask.BaseFetchingAsyncTask.OnResponseListener;
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
