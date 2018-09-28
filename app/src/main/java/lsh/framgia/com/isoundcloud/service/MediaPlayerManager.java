package lsh.framgia.com.isoundcloud.service;

import android.Manifest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v4.app.ActivityCompat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lsh.framgia.com.isoundcloud.constant.LoopMode;
import lsh.framgia.com.isoundcloud.constant.ShuffleMode;
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
    private List<Track> mOriginalPlaylist;
    private List<Track> mShufflePlaylist;
    private int mCurrentTrackPosition;
    private int mTrackState;
    private int mShuffleMode = ShuffleMode.OFF;
    private int mLoopMode = LoopMode.OFF;

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
        setTrackState(TrackState.INVALID);
        if (mLoopMode == LoopMode.OFF && mCurrentTrackPosition == getPlaylist().size() - 1) return;

        mMediaPlayer.reset();
        if (mLoopMode == LoopMode.ONE) {
            playTrack(getPlaylist().get(mCurrentTrackPosition));
        } else {
            playNextTrack();
        }
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
            String uri = !StringUtils.isEmpty(track.getDownloadPath()) ? track.getDownloadPath() :
                    StringUtils.formatStreamUrl(track.getUri());
            mMediaPlayer.setDataSource(uri);
            mMediaPlayer.prepareAsync();
            mCurrentTrackPosition = findCurrentTrackPosition(track);
            mMusicService.onNewTrackRequested(getCurrentTrack());
            mMusicService.showNotification(getCurrentTrack());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void playNextTrack() {
        int nextTrackPosition = findNextTrackPosition(getPlaylist());
        playTrack(getPlaylist().get(nextTrackPosition));
    }

    public void playPreviousTrack() {
        int previousTrackPosition = findPreviousTrackPosition(getPlaylist());
        playTrack(getPlaylist().get(previousTrackPosition));
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
                if (mTrackState == TrackState.INVALID || mTrackState == TrackState.PREPARING)
                    return;
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

    public void changeLoopMode() {
        switch (mLoopMode) {
            case LoopMode.OFF:
                mLoopMode = LoopMode.ONE;
                break;
            case LoopMode.ONE:
                mLoopMode = LoopMode.ALL;
                break;
            case LoopMode.ALL:
                mLoopMode = LoopMode.OFF;
                break;
            default:
                break;
        }
    }

    public void changeShuffleMode() {
        switch (mShuffleMode) {
            case ShuffleMode.OFF:
                handleShuffleOn();
                break;
            case ShuffleMode.ON:
                mShuffleMode = ShuffleMode.OFF;
                break;
            default:
                break;
        }
    }

    public void releasePlayer() {
        if (mMediaPlayer != null) mMediaPlayer.release();
    }

    public Track getCurrentTrack() {
        return getPlaylist() != null && mCurrentTrackPosition != -1 ?
                getPlaylist().get(mCurrentTrackPosition) : null;
    }

    public void setPlaylist(List<Track> tracks) {
        mCurrentTrackPosition = 0;
        mTrackState = TrackState.INVALID;
        mOriginalPlaylist = tracks;
        mShufflePlaylist = new ArrayList<>(mOriginalPlaylist);
        Collections.shuffle(mShufflePlaylist);
    }

    public List<Track> getPlaylist() {
        return mShuffleMode == ShuffleMode.OFF ? mOriginalPlaylist : mShufflePlaylist;
    }

    public void seekTo(int progress) {
        if (mTrackState != TrackState.PREPARED) return;
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

    public int getLoopMode() {
        return mLoopMode;
    }

    public int getShuffleMode() {
        return mShuffleMode;
    }

    private void handleShuffleOn() {
        Track currentTrack = getPlaylist().get(mCurrentTrackPosition);
        mShuffleMode = ShuffleMode.ON;
        mShufflePlaylist = new ArrayList<>(mOriginalPlaylist);
        Collections.shuffle(mShufflePlaylist);
        mCurrentTrackPosition = mShufflePlaylist.indexOf(currentTrack);
    }

    private int findPreviousTrackPosition(List<Track> tracks) {
        return mCurrentTrackPosition == 0 ? tracks.size() - 1 : --mCurrentTrackPosition;
    }

    private int findCurrentTrackPosition(Track track) {
        return mShuffleMode ==
                ShuffleMode.OFF ? mOriginalPlaylist.indexOf(track) : mShufflePlaylist.indexOf(track);
    }

    private int findNextTrackPosition(List<Track> tracks) {
        return mCurrentTrackPosition == tracks.size() - 1 ? 0 : ++mCurrentTrackPosition;
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
