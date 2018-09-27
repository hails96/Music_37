package lsh.framgia.com.isoundcloud.data.source.local.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.util.ArrayList;
import java.util.List;

import lsh.framgia.com.isoundcloud.R;
import lsh.framgia.com.isoundcloud.constant.TrackEntity;
import lsh.framgia.com.isoundcloud.data.model.Track;
import lsh.framgia.com.isoundcloud.data.source.TrackDataSource.OnLocalResponseListener;
import lsh.framgia.com.isoundcloud.util.StringUtils;

public class TrackDatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Track.db";
    private static TrackDatabaseHelper sInstance;
    private Context mContext;

    private static final String SQL_CREATE_TRACK_TABLE =
            "CREATE TABLE " + TrackEntity.TABLE_NAME + "(" +
                    TrackEntity.ID + " INTEGER NOT NULL PRIMARY KEY, " +
                    TrackEntity.TITLE + " TEXT, " +
                    TrackEntity.ARTIST + " TEXT, " +
                    TrackEntity.ARTWORK_URL + " TEXT, " +
                    TrackEntity.URI + " TEXT, " +
                    TrackEntity.DURATION + " INTEGER, " +
                    TrackEntity.IS_FAVORITE + " INTEGER, " +
                    TrackEntity.IS_DOWNLOADED + " INTERGER, " +
                    TrackEntity.IS_DOWNLOADABLE + " INTEGER, " +
                    TrackEntity.REQUEST_ID + " INTEGER, " +
                    TrackEntity.DESCRIPTION + " TEXT);";

    public static TrackDatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new TrackDatabaseHelper(context);
        }
        return sInstance;
    }

    public TrackDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TRACK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void saveTrack(Track track) {
        SQLiteDatabase database = getWritableDatabase();
        database.insert(TrackEntity.TABLE_NAME, null, createValuesForTrack(track));
    }

    public void updateDownloadedTrack(long requestId, OnLocalResponseListener listener) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TrackEntity.IS_DOWNLOADED, 1);
        int result = database.update(
                TrackEntity.TABLE_NAME,
                values,
                StringUtils.formatSingleWhereClause(TrackEntity.REQUEST_ID),
                new String[]{String.valueOf(requestId)}
        );
        if (result == 0) {
            listener.onFailure(mContext.getString(R.string.error_update_downloaded_track));
        }
    }

    public boolean isDownloadedTrack(Track track) {
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(
                TrackEntity.TABLE_NAME,
                null,
                StringUtils.formatDoubleWhereClause(TrackEntity.ID, TrackEntity.IS_DOWNLOADED),
                new String[]{String.valueOf(track.getId()), String.valueOf(1)},
                null,
                null,
                null
        );
        boolean isDownloaded = cursor.moveToNext();
        cursor.close();
        return isDownloaded;
    }

    public boolean isFavoriteTrack(Track track) {
        SQLiteDatabase database = getReadableDatabase();
        boolean isFavorite = false;
        Cursor cursor = database.query(
                TrackEntity.TABLE_NAME,
                new String[]{TrackEntity.IS_FAVORITE},
                StringUtils.formatSingleWhereClause(TrackEntity.ID),
                new String[]{String.valueOf(track.getId())},
                null,
                null,
                null
        );
        if (cursor.moveToNext()) {
            isFavorite = cursor.getInt(cursor.getColumnIndex(TrackEntity.IS_FAVORITE)) == 1;
        }
        cursor.close();
        return isFavorite;
    }

    public boolean updateFavoriteTrack(Track track, OnLocalResponseListener listener) {
        SQLiteDatabase database = getWritableDatabase();
        track.setUri(StringUtils.formatFilePath(Environment.DIRECTORY_MUSIC, track.getTitle()));
        long rowId = database.replace(TrackEntity.TABLE_NAME, null, createValuesForTrack(track));
        if (rowId != -1) {
            listener.onSuccess(track.isFavorite());
        }
        return track.isFavorite();
    }

    private ContentValues createValuesForTrack(Track track) {
        ContentValues values = new ContentValues();
        values.put(TrackEntity.ID, track.getId());
        values.put(TrackEntity.TITLE, track.getTitle());
        values.put(TrackEntity.ARTIST, track.getArtist());
        values.put(TrackEntity.ARTWORK_URL, track.getArtworkUrl());
        values.put(TrackEntity.URI, track.getUri());
        values.put(TrackEntity.DURATION, track.getDuration());
        int isFavorite = track.isFavorite() ? 1 : 0;
        values.put(TrackEntity.IS_FAVORITE, isFavorite);
        int isDownloaded = track.isDownloaded() ? 1 : 0;
        values.put(TrackEntity.IS_DOWNLOADED, isDownloaded);
        int isDownloadable = track.isDownloadable() ? 1 : 0;
        values.put(TrackEntity.IS_DOWNLOADABLE, isDownloadable);
        values.put(TrackEntity.REQUEST_ID, track.getRequestId());
        values.put(TrackEntity.DESCRIPTION, track.getDescription());
        return values;
    }

    public void getDownloadedTracks(OnLocalResponseListener<List<Track>> listener) {
        SQLiteDatabase database = getReadableDatabase();
        List<Track> tracks = new ArrayList<>();
        Cursor cursor = database.query(
                TrackEntity.TABLE_NAME,
                null,
                StringUtils.formatSingleWhereClause(TrackEntity.IS_DOWNLOADED),
                new String[]{String.valueOf(1)},
                null,
                null,
                null
        );
        while (cursor.moveToNext()) {
            Track track = new Track();
            track.setId(cursor.getString(cursor.getColumnIndex(TrackEntity.ID)));
            track.setTitle(cursor.getString(cursor.getColumnIndex(TrackEntity.TITLE)));
            track.setArtist(cursor.getString(cursor.getColumnIndex(TrackEntity.ARTIST)));
            track.setArtworkUrl(cursor.getString(cursor.getColumnIndex(TrackEntity.ARTWORK_URL)));
            track.setUri(cursor.getString(cursor.getColumnIndex(TrackEntity.URI)));
            track.setDuration(cursor.getInt(cursor.getColumnIndex(TrackEntity.DURATION)));
            int tmp = cursor.getInt(cursor.getInt(cursor.getColumnIndex(TrackEntity.IS_FAVORITE)));
            track.setIsFavorite(tmp == 1);
            tmp = cursor.getInt(cursor.getInt(cursor.getColumnIndex(TrackEntity.IS_DOWNLOADED)));
            track.setIsDownloaded(tmp == 1);
            tmp = cursor.getInt(cursor.getInt(cursor.getColumnIndex(TrackEntity.IS_DOWNLOADABLE)));
            track.setIsDownloadable(tmp == 1);
            track.setRequestId(cursor.getInt(cursor.getColumnIndex(TrackEntity.REQUEST_ID)));
            track.setDescription(cursor.getString(cursor.getColumnIndex(TrackEntity.DESCRIPTION)));
            tracks.add(track);
        }
        cursor.close();
        listener.onSuccess(tracks);
    }
}
