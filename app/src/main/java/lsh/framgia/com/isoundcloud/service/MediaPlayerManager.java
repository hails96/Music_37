package lsh.framgia.com.isoundcloud.service;

import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;
import java.util.List;

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
    private boolean mIsPlaying;

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
        mMusicService.onTrackPrepared(getCurrentTrack());
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mMediaPlayer.reset();
        mIsPlaying = false;
        return true;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mMediaPlayer.reset();
        mIsPlaying = false;
        playNextTrack();
    }

    public void playTrack(Track track) {
        if (mMediaPlayer == null) initMediaPlayer();

        if (mMediaPlayer.isPlaying()
                || (!mMediaPlayer.isPlaying() && mMediaPlayer.getCurrentPosition() > 0)) {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
        }

        try {
            mMediaPlayer.setDataSource(StringUtils.formatStreamUrl(track.getUri()));
            mMediaPlayer.prepareAsync();
            mCurrentTrackPosition = mPlaylist.indexOf(track);
            mIsPlaying = true;
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
                if (mIsPlaying) stopPlayingMusic();
                else resumePlayingMusic();
                break;
        }
    }

    public void stopPlayingMusic() {
        mIsPlaying = false;
        mMediaPlayer.pause();
        mMusicService.showNotification(getCurrentTrack());
        mMusicService.stopForeground(false);
        mMusicService.onTrackPaused();
    }

    public void resumePlayingMusic() {
        mIsPlaying = true;
        mMediaPlayer.start();
        mMusicService.showNotification(getCurrentTrack());
        mMusicService.onTrackResumed();
    }

    public void releasePlayer() {
        if (mMediaPlayer != null) mMediaPlayer.release();
    }

    public Track getCurrentTrack() {
        return mPlaylist.get(mCurrentTrackPosition);
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
        return mMediaPlayer != null && mIsPlaying;
    }

    public int getDuration() {
        return mMediaPlayer.getDuration();
    }

    public int getCurrentPosition() {
        return mMediaPlayer.getCurrentPosition();
    }

    private void initMediaPlayer() {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnErrorListener(this);
        mMediaPlayer.setOnCompletionListener(this);
    }
}
