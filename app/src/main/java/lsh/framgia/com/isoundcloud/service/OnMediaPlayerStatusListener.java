package lsh.framgia.com.isoundcloud.service;

import lsh.framgia.com.isoundcloud.constant.LoopMode;
import lsh.framgia.com.isoundcloud.constant.ShuffleMode;
import lsh.framgia.com.isoundcloud.data.model.Track;

public interface OnMediaPlayerStatusListener {
    void onTrackPrepared(Track track);

    void onTrackPaused();

    void onTrackResumed();

    void onNewTrackRequested(Track track);

    void onTrackError();

    void onLoopModeChanged(@LoopMode int loopMode);

    void onShuffleModeChanged(@ShuffleMode int shuffleMode);
}
