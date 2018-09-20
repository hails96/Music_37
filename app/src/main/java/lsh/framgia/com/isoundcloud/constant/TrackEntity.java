package lsh.framgia.com.isoundcloud.constant;

import android.support.annotation.StringDef;

@StringDef({
        TrackEntity.ID,
        TrackEntity.COLLECTION,
        TrackEntity.TRACK,
        TrackEntity.ARTWORK_URL,
        TrackEntity.DESCRIPTION,
        TrackEntity.IS_DOWNLOADABLE,
        TrackEntity.DOWNLOAD_URL,
        TrackEntity.DURATION,
        TrackEntity.PUBLISHER_METADATA,
        TrackEntity.ARTIST,
        TrackEntity.TITLE,
        TrackEntity.URI,
        TrackEntity.IS_DOWNLOADABLE
})

public @interface TrackEntity {
    String ID = "id";
    String COLLECTION = "collection";
    String TRACK = "track";
    String ARTWORK_URL = "artwork_url";
    String DESCRIPTION = "description";
    String IS_DOWNLOADABLE = "downloadable";
    String DOWNLOAD_URL = "download_url";
    String DURATION = "duration";
    String PUBLISHER_METADATA = "publisher_metadata";
    String ARTIST = "artist";
    String TITLE = "title";
    String URI = "uri";
    String IS_FAVORITE = "is_favorite";
}
