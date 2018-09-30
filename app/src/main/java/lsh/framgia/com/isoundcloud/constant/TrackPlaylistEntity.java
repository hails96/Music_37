package lsh.framgia.com.isoundcloud.constant;

import android.support.annotation.StringDef;

@StringDef({
        TrackPlaylistEntity.TABLE_NAME,
        TrackPlaylistEntity.PLAYLIST_ID,
        TrackPlaylistEntity.TRACK_ID
})

public @interface TrackPlaylistEntity {
    String TABLE_NAME = "Track_Playlist";
    String PLAYLIST_ID = "playlist_id";
    String TRACK_ID = "track_id";
}