package lsh.framgia.com.isoundcloud.constant;

import android.support.annotation.IntDef;

@IntDef({
        TrackState.INVALID,
        TrackState.PREPARING,
        TrackState.PREPARED,
        TrackState.PAUSED
})

public @interface TrackState {
    int INVALID = -1;
    int PREPARING = 0;
    int PREPARED = 1;
    int PAUSED = 2;
}
