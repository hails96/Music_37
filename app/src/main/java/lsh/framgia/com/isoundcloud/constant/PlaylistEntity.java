package lsh.framgia.com.isoundcloud.constant;

import android.support.annotation.StringDef;

@StringDef({
        PlaylistEntity.TABLE_NAME,
        PlaylistEntity.ID,
        PlaylistEntity.NAME,
        PlaylistEntity.CREATED_DATE,
        PlaylistEntity.NUMBER_OF_PLAYS
})

public @interface PlaylistEntity {
    String TABLE_NAME = "Playlist";
    String ID = "id";
    String NAME = "name";
    String CREATED_DATE = "created_date";
    String NUMBER_OF_PLAYS = "number_of_plays";
}

