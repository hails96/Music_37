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
import lsh.framgia.com.isoundcloud.constant.PlaylistEntity;
import lsh.framgia.com.isoundcloud.constant.TrackEntity;
import lsh.framgia.com.isoundcloud.constant.TrackPlaylistEntity;
import lsh.framgia.com.isoundcloud.data.model.Playlist;
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

    private static final String SQL_CREATE_PLAYLIST_TABLE =
            "CREATE TABLE " + PlaylistEntity.TABLE_NAME + "(" +
                    PlaylistEntity.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    PlaylistEntity.NAME + " TEXT, " +
                    PlaylistEntity.CREATED_DATE + " INTEGER, " +
                    PlaylistEntity.NUMBER_OF_PLAYS + " INTEGER);";

    private static final String SQL_CREATE_TRACK_PLAYLIST_TABLE =
            "CREATE TABLE " + TrackPlaylistEntity.TABLE_NAME + "(" +
                    TrackPlaylistEntity.TRACK_ID + " TEXT, " +
                    TrackPlaylistEntity.PLAYLIST_ID + " INTEGER, " +
                    "FOREIGN KEY (" + TrackPlaylistEntity.TRACK_ID + ") REFERENCES " +
                    TrackEntity.TABLE_NAME + "(" + TrackEntity.ID + "), " +
                    "FOREIGN KEY (" + TrackPlaylistEntity.PLAYLIST_ID + ") REFERENCES " +
                    PlaylistEntity.TABLE_NAME + "(" + PlaylistEntity.ID + "));";

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
        db.execSQL(SQL_CREATE_PLAYLIST_TABLE);
        db.execSQL(SQL_CREATE_TRACK_PLAYLIST_TABLE);
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

    public long createNewPlaylist(Playlist playlist, OnLocalResponseListener<Boolean> listener) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PlaylistEntity.NAME, playlist.getName());
        values.put(PlaylistEntity.CREATED_DATE, playlist.getCreatedDate());
        values.put(PlaylistEntity.NUMBER_OF_PLAYS, playlist.getNumberOfPlays());
        long rowId = database.insert(PlaylistEntity.TABLE_NAME, null, values);
        if (listener == null) return rowId;
        if (rowId == -1) {
            listener.onFailure(mContext.getString(R.string.error_create_new_playlist));
        } else {
            listener.onSuccess(true);
        }
        return rowId;
    }

    public void getPlaylists(OnLocalResponseListener<List<Playlist>> listener) {
        SQLiteDatabase database = getReadableDatabase();
        List<Playlist> playlists = new ArrayList<>();
        Cursor cursor = database.query(
                PlaylistEntity.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
        while (cursor.moveToNext()) {
            Playlist playlist = new Playlist();
            playlist.setId(cursor.getInt(cursor.getColumnIndex(PlaylistEntity.ID)));
            playlist.setName(cursor.getString(cursor.getColumnIndex(PlaylistEntity.NAME)));
            playlist.setCreatedDate(cursor.getLong(cursor.getColumnIndex(PlaylistEntity.CREATED_DATE)));
            playlist.setNumberOfPlays(cursor.getInt(cursor.getColumnIndex(PlaylistEntity.NUMBER_OF_PLAYS)));
            playlists.add(playlist);
        }
        cursor.close();
        listener.onSuccess(playlists);
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

    public void addTrackToNewPlaylist(Track track, Playlist playlist, OnLocalResponseListener<Boolean> listener) {
        long playlistId = createNewPlaylist(playlist, null);
        if (playlistId == -1) {
            listener.onFailure(mContext.getString(R.string.error_create_new_playlist));
            return;
        }
        playlist.setId((int) playlistId);
        if (!isTrackInPlaylist(track, playlist)) {
            addTrackToPlaylist(track, playlist);
            listener.onSuccess(true);
        } else {
            listener.onFailure(mContext.getString(R.string.error_track_is_in_playlist));
        }
    }

    public void addTrackToPlaylist(Track track, Playlist playlist) {
        if (!isTrackExisted(track)) {
            saveTrack(track);
        }
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TrackPlaylistEntity.TRACK_ID, track.getId());
        values.put(TrackPlaylistEntity.PLAYLIST_ID, playlist.getId());
        database.insert(TrackPlaylistEntity.TABLE_NAME, null, values);
    }

    private boolean isTrackExisted(Track track) {
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(
                TrackEntity.TABLE_NAME,
                null,
                StringUtils.formatSingleWhereClause(TrackEntity.ID),
                new String[]{track.getId()},
                null,
                null,
                null
        );
        boolean isExisted = cursor.moveToNext();
        cursor.close();
        return isExisted;
    }

    private boolean isTrackInPlaylist(Track track, Playlist playlist) {
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(
                TrackPlaylistEntity.TABLE_NAME,
                null,
                StringUtils.formatDoubleWhereClause(TrackPlaylistEntity.TRACK_ID, TrackPlaylistEntity.PLAYLIST_ID),
                new String[]{track.getId(), String.valueOf(playlist.getId())},
                null,
                null,
                null
        );
        boolean isExisted = cursor.moveToNext();
        cursor.close();
        return isExisted;
    }
}
