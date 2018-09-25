package lsh.framgia.com.isoundcloud.data.source;

import java.util.List;

import lsh.framgia.com.isoundcloud.base.asynctask.BaseFetchingAsyncTask.OnResponseListener;
import lsh.framgia.com.isoundcloud.data.model.Track;

public interface TrackDataSource {
    interface LocalDataSource {

    }

    interface RemoteDataSource {
        void getRemoteTracks(String genre, int offset, int limit,
                             OnResponseListener<List<Track>> listener);

        void getSearchTracks(String query, int offset, int limit,
                             OnResponseListener<List<Track>> listener);
    }
}
