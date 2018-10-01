package lsh.framgia.com.isoundcloud.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.List;

import lsh.framgia.com.isoundcloud.R;
import lsh.framgia.com.isoundcloud.constant.Constant;
import lsh.framgia.com.isoundcloud.constant.LoopMode;
import lsh.framgia.com.isoundcloud.constant.ShuffleMode;
import lsh.framgia.com.isoundcloud.constant.TrackState;
import lsh.framgia.com.isoundcloud.data.model.Track;
import lsh.framgia.com.isoundcloud.screen.main.MainActivity;
import lsh.framgia.com.isoundcloud.screen.player.PlayerActivity;

import static lsh.framgia.com.isoundcloud.service.MediaPlayerManager.ACTION_NEXT;
import static lsh.framgia.com.isoundcloud.service.MediaPlayerManager.ACTION_PREVIOUS;
import static lsh.framgia.com.isoundcloud.service.MediaPlayerManager.ACTION_STATE_CHANGE;

public class MusicService extends Service {

    private static final int NOTIFICATION_ID = 1001;
    private static final int DEFAULT_REQUEST_CODE = 0;
    private static final int DEFAULT_FLAG = 0;

    private final IBinder mBinder = new MusicBinder();
    private NotificationManagerCompat mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    private MediaPlayerManager mManager;
    private OnMediaPlayerStatusListener mOnMediaPlayerStatusListener;

    @Override
    public void onCreate() {
        super.onCreate();
        mManager = MediaPlayerManager.getInstance(this);
        mNotificationManager = NotificationManagerCompat.from(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction() != null) {
            mManager.handleAction(intent.getAction());
        }
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mManager.releasePlayer();
    }

    public void playTrack(Track track) {
        mManager.playTrack(track);
    }

    public void onTrackPrepared(Track track) {
        if (mOnMediaPlayerStatusListener == null) return;
        mOnMediaPlayerStatusListener.onTrackPrepared(track);
    }

    public void onTrackPaused() {
        if (mOnMediaPlayerStatusListener == null) return;
        mOnMediaPlayerStatusListener.onTrackPaused();
    }

    public void onTrackResumed() {
        if (mOnMediaPlayerStatusListener == null) return;
        mOnMediaPlayerStatusListener.onTrackResumed();
    }

    public void showNotification(Track track) {
        mBuilder = new NotificationCompat.Builder(this, getPackageName())
                .setSmallIcon(R.drawable.ic_app)
                .setContentTitle(track.getTitle())
                .setContentText(track.getArtist())
                .setContentIntent(createPlayerPendingIntent());
        displayTrackArtwork();
        addActionForNotification();
        Notification notification = mBuilder.build();
        mNotificationManager.notify(NOTIFICATION_ID, notification);
        startForeground(NOTIFICATION_ID, notification);
    }

    public void playPreviousTrack() {
        mManager.playPreviousTrack();
    }

    public void playNextTrack() {
        mManager.playNextTrack();
    }

    public void stopPlayingMusic() {
        mManager.stopPlayingMusic();
    }

    public void resumePlayingMusic() {
        mManager.resumePlayingMusic();
    }

    public void changeLoopMode() {
        mManager.changeLoopMode();
        if (mOnMediaPlayerStatusListener == null) return;
        mOnMediaPlayerStatusListener.onLoopModeChanged(mManager.getLoopMode());
    }

    public void changeShuffleMode() {
        mManager.changeShuffleMode();
        if (mOnMediaPlayerStatusListener == null) return;
        mOnMediaPlayerStatusListener.onShuffleModeChanged(mManager.getShuffleMode());
    }

    public boolean isPlaying() {
        return mManager.isPlaying();
    }

    public int getDuration() {
        return mManager.getDuration();
    }

    public int getCurrentPosition() {
        return mManager.getCurrentPosition();
    }

    public void setOnMediaPlayerStatusListener(OnMediaPlayerStatusListener listener) {
        mOnMediaPlayerStatusListener = listener;
    }

    public void setPlaylist(List<Track> tracks) {
        mManager.setPlaylist(tracks);
    }

    public List<Track> getPlaylist() {
        return mManager.getPlaylist();
    }

    public Track getCurrentTrack() {
        return mManager.getCurrentTrack();
    }

    public void seekTo(int progress) {
        mManager.seekTo(progress);
    }

    public @LoopMode
    int getLoopMode() {
        return mManager.getLoopMode();
    }

    public @ShuffleMode
    int getShuffleMode() {
        return mManager.getShuffleMode();
    }

    private PendingIntent createPlayerPendingIntent() {
        Intent mainIntent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(mainIntent);
        Intent playerIntent = new Intent(this, PlayerActivity.class);
        stackBuilder.addNextIntent(playerIntent);
        return stackBuilder.getPendingIntent(
                DEFAULT_FLAG, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private PendingIntent createNewPendingIntent(String action) {
        Intent intent = new Intent(this, MusicService.class);
        intent.setAction(action);
        return PendingIntent.getService(this, DEFAULT_REQUEST_CODE, intent, DEFAULT_FLAG);
    }

    public void onNewTrackRequested(Track track) {
        if (mOnMediaPlayerStatusListener == null) return;
        mOnMediaPlayerStatusListener.onNewTrackRequested(track);
    }

    public void onTrackCompleted() {
        if (mOnMediaPlayerStatusListener == null) return;
        mOnMediaPlayerStatusListener.onTrackCompleted();
    }

    public void onTrackError() {
        if (mOnMediaPlayerStatusListener == null) return;
        mOnMediaPlayerStatusListener.onTrackError();
    }

    public int getTrackState() {
        return mManager != null ? mManager.getTrackState() : TrackState.INVALID;
    }

    private void addActionForNotification() {
        mBuilder.addAction(R.drawable.ic_skip_previous, getString(R.string.action_previous),
                createNewPendingIntent(ACTION_PREVIOUS));
        if (mManager.getTrackState() != TrackState.PAUSED) {
            mBuilder.addAction(R.drawable.ic_pause_white, getString(R.string.action_state_change),
                    createNewPendingIntent(ACTION_STATE_CHANGE));
        } else {
            mBuilder.addAction(R.drawable.ic_play_white, getString(R.string.action_state_change),
                    createNewPendingIntent(ACTION_STATE_CHANGE));
        }
        mBuilder.addAction(R.drawable.ic_skip_next, getString(R.string.action_next),
                createNewPendingIntent(ACTION_NEXT));
        mBuilder.setStyle(new android.support.v4.media.app.NotificationCompat.MediaStyle()
                .setShowActionsInCompactView(1));
    }

    private void displayTrackArtwork() {
        if (getCurrentTrack() == null) return;
        if (getCurrentTrack().getArtworkUrl().equalsIgnoreCase(Constant.TEXT_NULL)) {
            mBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(),
                    R.drawable.bg_all_audio));
            mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        } else {
            Glide.with(this)
                    .asBitmap()
                    .load(getCurrentTrack().getArtworkUrl())
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.bg_all_audio)
                            .error(R.drawable.bg_all_audio))
                    .into(new SimpleTarget<Bitmap>(100, 100) {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource,
                                                    @Nullable Transition<? super Bitmap> transition) {
                            mBuilder.setLargeIcon(resource);
                            mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
                        }
                    });
        }
    }

    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }
}
