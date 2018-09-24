package lsh.framgia.com.isoundcloud.service;

import lsh.framgia.com.isoundcloud.data.model.Track;

public interface OnMediaPlayerStatusListener {
    void onTrackPrepared(Track track);

    void onTrackPaused();

    void onTrackResumed();

    void onNewTrackRequested(Track track);

    void onTrackError();
}
