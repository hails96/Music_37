package lsh.framgia.com.isoundcloud.data.source.local.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import lsh.framgia.com.isoundcloud.R;
import lsh.framgia.com.isoundcloud.constant.Constant;
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
                    TrackEntity.DOWNLOAD_PATH + " TEXT, " +
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
        long result = database.insertWithOnConflict(TrackEntity.TABLE_NAME, null,
                createValuesForTrack(track), SQLiteDatabase.CONFLICT_IGNORE);
        if (result == -1) {
            ContentValues values = new ContentValues();
            values.put(TrackEntity.REQUEST_ID, track.getRequestId());
            database.update(
                    TrackEntity.TABLE_NAME,
                    values,
                    StringUtils.formatSingleWhereClause(TrackEntity.ID),
                    new String[]{track.getId()}
            );
        }
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
        ContentValues values = new ContentValues();
        values.put(TrackEntity.IS_FAVORITE, track.isFavorite());
        int rowAffected = database.update(
                TrackEntity.TABLE_NAME,
                values,
                StringUtils.formatSingleWhereClause(TrackEntity.ID),
                new String[]{track.getId()});
        if (rowAffected == 0) {
            rowAffected = (int) database.insert(
                    TrackEntity.TABLE_NAME, null, createValuesForTrack(track));
        }
        if (rowAffected >= 0) {
            listener.onSuccess(track.isFavorite());
        }
        return track.isFavorite();
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
            Track track = new Track(cursor);
            tracks.add(track);
        }
        cursor.close();
        listener.onSuccess(tracks);
    }

    public void getFavoriteTracks(OnLocalResponseListener<List<Track>> listener) {
        SQLiteDatabase database = getReadableDatabase();
        List<Track> tracks = new ArrayList<>();
        Cursor cursor = database.query(
                TrackEntity.TABLE_NAME,
                null,
                StringUtils.formatSingleWhereClause(TrackEntity.IS_FAVORITE),
                new String[]{String.valueOf(1)},
                null,
                null,
                null
        );
        while (cursor.moveToNext()) {
            Track track = new Track(cursor);
            tracks.add(track);
        }
        cursor.close();
        listener.onSuccess(tracks);
    }

    public void deleteTrack(Track track, OnLocalResponseListener<Track> listener) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TrackEntity.IS_DOWNLOADED, 0);
        values.put(TrackEntity.DOWNLOAD_PATH, Constant.TEXT_EMPTY);
        int result = database.update(
                TrackEntity.TABLE_NAME,
                values,
                StringUtils.formatSingleWhereClause(TrackEntity.ID),
                new String[]{track.getId()}
        );
        if (result > 0) {
            listener.onSuccess(track);
        } else {
            listener.onFailure(track.getTitle());
        }
    }

    private ContentValues createValuesForTrack(Track track) {
        ContentValues values = new ContentValues();
        values.put(TrackEntity.ID, track.getId());
        values.put(TrackEntity.TITLE, track.getTitle());
        values.put(TrackEntity.ARTIST, track.getArtist());
        values.put(TrackEntity.ARTWORK_URL, track.getArtworkUrl());
        values.put(TrackEntity.URI, track.getUri());
        values.put(TrackEntity.DOWNLOAD_PATH, track.getDownloadPath());
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
}
