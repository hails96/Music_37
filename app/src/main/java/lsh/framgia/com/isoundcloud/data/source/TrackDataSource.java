package lsh.framgia.com.isoundcloud.data.source;

import java.util.List;

import lsh.framgia.com.isoundcloud.base.asynctask.BaseFetchingAsyncTask.OnResponseListener;
import lsh.framgia.com.isoundcloud.data.model.Track;

public interface TrackDataSource {
    interface LocalDataSource {
        void saveTrackToDatabase(Track track, OnLocalResponseListener listener);

        void updateDownloadedTrack(long requestId, OnLocalResponseListener listener);

        boolean isTrackDownloaded(Track track);
    }

    interface RemoteDataSource {
        void getRemoteTracks(String genre, int offset, int limit,
                             OnResponseListener<List<Track>> listener);

        void getSearchTracks(String query, int offset, int limit,
                             OnResponseListener<List<Track>> listener);
    }

    interface OnLocalResponseListener {
        void onSuccess();

        void onFailure(String msg);
    }
}
