package lsh.framgia.com.isoundcloud.constant;

import android.support.annotation.StringDef;

@StringDef({
        TrackEntity.TABLE_NAME,
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
        TrackEntity.DOWNLOAD_PATH,
        TrackEntity.IS_FAVORITE,
        TrackEntity.IS_DOWNLOADED,
        TrackEntity.REQUEST_ID
})

public @interface TrackEntity {
    String TABLE_NAME = "Track";
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
    String DOWNLOAD_PATH = "download_path";
    String IS_FAVORITE = "is_favorite";
    String IS_DOWNLOADED = "is_downloaded";
    String REQUEST_ID = "request_id";
}
