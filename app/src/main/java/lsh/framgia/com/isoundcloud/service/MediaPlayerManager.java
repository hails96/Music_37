package lsh.framgia.com.isoundcloud.service;

import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;
import java.util.List;

import lsh.framgia.com.isoundcloud.constant.TrackState;
import lsh.framgia.com.isoundcloud.data.model.Track;
import lsh.framgia.com.isoundcloud.util.StringUtils;

public class MediaPlayerManager implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {

    static final String ACTION_PREVIOUS = "lsh.framgia.com.isoundcloud.PREVIOUS";
    static final String ACTION_STATE_CHANGE = "lsh.framgia.com.isoundcloud.STATE_CHANGE";
    static final String ACTION_NEXT = "llsh.framgia.com.isoundcloud.NEXT";

    private static MediaPlayerManager sInstance;
    private MusicService mMusicService;
    private MediaPlayer mMediaPlayer;
    private List<Track> mPlaylist;
    private int mCurrentTrackPosition;
    private int mTrackState;

    static synchronized MediaPlayerManager getInstance(MusicService musicService) {
        if (sInstance == null) {
            sInstance = new MediaPlayerManager(musicService);
        }
        return sInstance;
    }

    private MediaPlayerManager(MusicService musicService) {
        mMusicService = musicService;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mMediaPlayer.start();
        setTrackState(TrackState.PREPARED);
        mMusicService.onTrackPrepared(getCurrentTrack());
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mMediaPlayer.reset();
        setTrackState(TrackState.INVALID);
        mMusicService.onTrackError();
        return true;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mMediaPlayer.reset();
        setTrackState(TrackState.INVALID);
        playNextTrack();
    }

    public void playTrack(Track track) {
        if (mMediaPlayer == null) initMediaPlayer();

        if (mMediaPlayer.isPlaying()
                || (!mMediaPlayer.isPlaying() && mMediaPlayer.getCurrentPosition() > 0)) {
            mMediaPlayer.stop();
        }
        mMediaPlayer.reset();

        try {
            setTrackState(TrackState.PREPARING);
            mMediaPlayer.setDataSource(StringUtils.formatStreamUrl(track.getUri()));
            mMediaPlayer.prepareAsync();
            mCurrentTrackPosition = mPlaylist.indexOf(track);
            mMusicService.onNewTrackRequested(getCurrentTrack());
            mMusicService.showNotification(getCurrentTrack());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void playNextTrack() {
        int nextTrackPosition =
                mCurrentTrackPosition == (mPlaylist.size() - 1) ? 0 : ++mCurrentTrackPosition;
        playTrack(mPlaylist.get(nextTrackPosition));
    }

    public void playPreviousTrack() {
        int previousTrackPosition =
                mCurrentTrackPosition == 0 ? mPlaylist.size() - 1 : --mCurrentTrackPosition;
        playTrack(mPlaylist.get(previousTrackPosition));
    }

    public void handleAction(String action) {
        switch (action) {
            case ACTION_NEXT:
                playNextTrack();
                break;
            case ACTION_PREVIOUS:
                playPreviousTrack();
                break;
            case ACTION_STATE_CHANGE:
                if (mTrackState == TrackState.INVALID || mTrackState == TrackState.PREPARING) return;
                if (isPlaying()) stopPlayingMusic();
                else resumePlayingMusic();
                break;
        }
    }

    public void stopPlayingMusic() {
        setTrackState(TrackState.PAUSED);
        mMediaPlayer.pause();
        mMusicService.showNotification(getCurrentTrack());
        mMusicService.stopForeground(false);
        mMusicService.onTrackPaused();
    }

    public void resumePlayingMusic() {
        setTrackState(TrackState.PREPARED);
        mMediaPlayer.start();
        mMusicService.showNotification(getCurrentTrack());
        mMusicService.onTrackResumed();
    }

    public void releasePlayer() {
        if (mMediaPlayer != null) mMediaPlayer.release();
    }

    public Track getCurrentTrack() {
        return mPlaylist != null ? mPlaylist.get(mCurrentTrackPosition) : null;
    }

    public void setPlaylist(List<Track> tracks) {
        mPlaylist = tracks;
    }

    public List<Track> getPlaylist() {
        return mPlaylist;
    }

    public void seekTo(int progress) {
        mMediaPlayer.seekTo(progress);
    }

    public boolean isPlaying() {
        return mMediaPlayer != null && mMediaPlayer.isPlaying();
    }

    public int getDuration() {
        return mMediaPlayer.getDuration();
    }

    public int getCurrentPosition() {
        return mMediaPlayer.getCurrentPosition();
    }

    public int getTrackState() {
        return mTrackState;
    }

    private void initMediaPlayer() {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnErrorListener(this);
        mMediaPlayer.setOnCompletionListener(this);
    }

    private void setTrackState(@TrackState int trackState) {
        mTrackState = trackState;
    }
}
