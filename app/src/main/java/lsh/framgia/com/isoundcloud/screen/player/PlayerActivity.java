package lsh.framgia.com.isoundcloud.screen.player;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import lsh.framgia.com.isoundcloud.R;
import lsh.framgia.com.isoundcloud.base.mvp.BaseActivity;
import lsh.framgia.com.isoundcloud.constant.LoopMode;
import lsh.framgia.com.isoundcloud.constant.ShuffleMode;
import lsh.framgia.com.isoundcloud.constant.TrackState;
import lsh.framgia.com.isoundcloud.data.model.Track;
import lsh.framgia.com.isoundcloud.data.repository.TrackRepository;
import lsh.framgia.com.isoundcloud.data.source.local.TrackLocalDataSource;
import lsh.framgia.com.isoundcloud.data.source.remote.TrackDownloadManager;
import lsh.framgia.com.isoundcloud.data.source.remote.TrackRemoteDataSource;
import lsh.framgia.com.isoundcloud.screen.player.nowplaying.NowPlayingFragment;
import lsh.framgia.com.isoundcloud.screen.player.nowplaying.NowPlayingPresenter;
import lsh.framgia.com.isoundcloud.service.OnMediaPlayerStatusListener;
import lsh.framgia.com.isoundcloud.util.StringUtils;

public class PlayerActivity extends BaseActivity<PlayerContract.Presenter>
        implements PlayerContract.View, OnMediaPlayerStatusListener, View.OnClickListener,
        SeekBar.OnSeekBarChangeListener, TrackDownloadManager.OnDownloadListener,
        OnCurrentTrackChangeListener {

    public static final String EXTRA_TRACK = "lsh.framgia.com.isoundcloud.EXTRA_TRACK";
    private static final int DELAY_TIME = 1000;
    private static final int WRITE_EXTERNAL_STORAGE_CODE = 100;

    private ImageView mImageBackground;
    private ImageView mImageArrowDown;
    private ImageView mImageVolume;
    private ImageView mImageArtwork;
    private TextView mTextTrackTitle;
    private TextView mTextTrackArtist;
    private SeekBar mSeekBarDuration;
    private TextView mTextCurrentDuration;
    private TextView mTextFullDuration;
    private ImageView mImageLoop;
    private ImageView mImagePrevious;
    private ImageView mImagePlayPause;
    private ImageView mImageNext;
    private ImageView mImageShuffle;
    private ImageView mImageDownload;
    private ImageView mImageFavorite;
    private ImageView mImageNowPlaying;
    private ProgressBar mProgressBarLoading;

    private AudioManager mAudioManager;
    private RequestOptions mBackGroundOptions;
    private RequestOptions mArtworkOptions;
    private ObjectAnimator mObjectAnimator;
    private Track mTrack;
    private OnCurrentTrackChangeListener mOnCurrentTrackChangeListener;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_player;
    }

    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (mIsBound) {
                int position = mMusicService.getCurrentPosition();
                mSeekBarDuration.setProgress(position);
                mTextCurrentDuration.setText(StringUtils.convertMillisToDuration(position));
            }
            mHandler.postDelayed(this, DELAY_TIME);
        }
    };

    @Override
    protected void initLayout() {
        mTrack = getIntent().getParcelableExtra(EXTRA_TRACK);
        PlayerPresenter playerPresenter = new PlayerPresenter(TrackRepository.getInstance(
                TrackRemoteDataSource.getInstance(), TrackLocalDataSource.getInstance(this)));
        setPresenter(playerPresenter);
        playerPresenter.setView(this);
        setupPreferences();
        setupListener();
        setupObjectAnimator(mImageArtwork);
        setupOptions();
        setupView(mTrack);
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
    }

    @Override
    protected OnMediaPlayerStatusListener getMediaPlayerStatusListener() {
        return this;
    }

    @Override
    protected void onMusicServiceConnected() {
        if (mTrack != null) {
            mMusicService.playTrack(mTrack);
        } else {
            setupView(mMusicService.getCurrentTrack());
            resetSeekBar(mMusicService.getCurrentTrack());
            updatePlayPauseView(mMusicService.getTrackState());
            mHandler.post(mRunnable);
        }
        if (!mObjectAnimator.isStarted() && mMusicService.getTrackState() == TrackState.PREPARED) {
            mObjectAnimator.start();
        }
    }

    @Override
    public void onTrackPrepared(Track track) {
        mObjectAnimator.start();
        updatePlayPauseView(mMusicService.getTrackState());
        runOnUiThread(mRunnable);
    }

    @Override
    public void onTrackPaused() {
        mObjectAnimator.pause();
        mImagePlayPause.setImageResource(R.drawable.ic_play);
    }

    @Override
    public void onTrackResumed() {
        mObjectAnimator.resume();
        mImagePlayPause.setImageResource(R.drawable.ic_pause);
    }

    @Override
    public void onTrackCompleted() {
        mObjectAnimator.pause();
    }

    @Override
    public void onNewTrackRequested(Track track) {
        mImagePlayPause.setImageResource(R.drawable.ic_pause);
        setupView(track);
        resetSeekBar(track);
        if (mOnCurrentTrackChangeListener != null) {
            mOnCurrentTrackChangeListener.onChanged(track);
        }
    }

    @Override
    public void onTrackError() {
        Toast.makeText(this, getString(R.string.error_player), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoopModeChanged(int loopMode) {
        switch (loopMode) {
            case LoopMode.OFF:
                mImageLoop.setImageResource(R.drawable.ic_loop_off);
                break;
            case LoopMode.ONE:
                mImageLoop.setImageResource(R.drawable.ic_loop_one);
                break;
            case LoopMode.ALL:
                mImageLoop.setImageResource(R.drawable.ic_loop_all);
                break;
            default:
                break;
        }
    }

    @Override
    public void onShuffleModeChanged(int shuffleMode) {
        switch (shuffleMode) {
            case ShuffleMode.OFF:
                mImageShuffle.setImageResource(R.drawable.ic_shuffle_off);
                break;
            case ShuffleMode.ON:
                mImageShuffle.setImageResource(R.drawable.ic_shuffle_on);
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_play:
                handlePlayerActionChanged();
                break;
            case R.id.image_previous:
                mMusicService.playPreviousTrack();
                break;
            case R.id.image_next:
                mMusicService.playNextTrack();
                break;
            case R.id.image_loop:
                mMusicService.changeLoopMode();
                break;
            case R.id.image_shuffle:
                mMusicService.changeShuffleMode();
                break;
            case R.id.image_arrow_down:
                onBackPressed();
                break;
            case R.id.image_download:
                handleDownloadTrack();
                break;
            case R.id.image_favorite:
                handleFavoriteChange();
                break;
            case R.id.image_now_playing:
                openNowPlaying();
                break;
            case R.id.image_volume:
                handleVolumeChange();
                break;
            default:
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mRunnable);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mMusicService.seekTo(seekBar.getProgress());
        mTextCurrentDuration.setText(StringUtils.convertMillisToDuration(seekBar.getProgress()));
        mHandler.post(mRunnable);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case WRITE_EXTERNAL_STORAGE_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    downloadTrack();
                } else {
                    Toast.makeText(this, getString(R.string.error_download_track),
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onDownload(Track track) {
        mTrack.setRequestId(track.getRequestId());
    }

    @Override
    protected void updateDownloadedTrack(long requestId) {
        super.updateDownloadedTrack(requestId);
        if (requestId == -1) return;
        mPresenter.updateDownloadedTrack(requestId);
    }

    @Override
    public void onUpdateDownloadedTrackFailure(String msg) {
        if (!StringUtils.isEmpty(msg)) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void updateFavoriteSuccess(boolean isFavorite) {
        if (isFavorite) {
            Toast.makeText(this, getString(R.string.msg_added_to_favorite),
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.msg_removed_to_favorite),
                    Toast.LENGTH_SHORT).show();
        }
        mTrack.setIsFavorite(isFavorite);
        setupView(mTrack);
    }

    @Override
    public void onChanged(Track currentTrack) {
        mMusicService.playTrack(currentTrack);
    }

    public static Intent getPlayerIntent(Context context, Track track) {
        Intent intent = new Intent(context, PlayerActivity.class);
        intent.putExtra(EXTRA_TRACK, track);
        return intent;
    }

    private void handlePlayerActionChanged() {
        if (mMusicService.isPlaying()) {
            mMusicService.stopPlayingMusic();
        } else {
            mMusicService.resumePlayingMusic();
        }
    }

    private void setupOptions() {
        mBackGroundOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.bg_genre_place_holder)
                .error(R.drawable.bg_genre_place_holder);

        mArtworkOptions = new RequestOptions()
                .centerCrop()
                .circleCrop()
                .placeholder(R.drawable.bg_track_place_holder)
                .placeholder(R.drawable.bg_track_place_holder);
    }

    private void setupView(Track track) {
        if (track == null) return;
        mTrack = track;
        if (mMusicService != null) {
            updatePlayPauseView(mMusicService.getTrackState());
            onLoopModeChanged(mMusicService.getLoopMode());
            onShuffleModeChanged(mMusicService.getShuffleMode());
        }
        displayArtwork(track);
        displayTrackInfo(track);
        updateDownloadableView(track.isDownloadable());
        updateFavoriteView(mPresenter.isFavoriteTrack(mTrack));
    }

    private void updatePlayPauseView(@TrackState int state) {
        if (state != TrackState.PREPARED && state != TrackState.PAUSED) {
            mProgressBarLoading.setVisibility(View.VISIBLE);
            mImagePlayPause.setVisibility(View.INVISIBLE);
        } else {
            mProgressBarLoading.setVisibility(View.GONE);
            mImagePlayPause.setVisibility(View.VISIBLE);
        }
        if (state == TrackState.PAUSED) onTrackPaused();
        else onTrackResumed();
    }

    private void resetSeekBar(Track track) {
        mSeekBarDuration.setMax(track.getDuration());
        mSeekBarDuration.setProgress(0);
    }

    private void displayArtwork(Track track) {
        loadImageWithGlide(mBackGroundOptions, mImageBackground,
                StringUtils.getOriginalUrl(track.getArtworkUrl()));
        loadImageWithGlide(mArtworkOptions, mImageArtwork,
                StringUtils.getOriginalUrl(track.getArtworkUrl()));
        mObjectAnimator.pause();
        mImageArtwork.setRotation(0);
    }

    private void displayTrackInfo(Track track) {
        mTextTrackTitle.setText(track.getTitle());
        mTextTrackArtist.setText(track.getArtist());
        mTextCurrentDuration.setText(getString(R.string.default_duration));
        mTextFullDuration.setText(StringUtils.convertMillisToDuration(track.getDuration()));
    }

    private void updateDownloadableView(boolean isDownloadable) {
        if (isDownloadable) {
            mImageDownload.setImageResource(R.drawable.ic_downloadable);
            mImageDownload.setClickable(true);
        } else {
            mImageDownload.setImageResource(R.drawable.ic_not_downloadable);
            mImageDownload.setClickable(false);
        }
    }

    private void updateFavoriteView(boolean isFavorite) {
        if (isFavorite) {
            mImageFavorite.setImageResource(R.drawable.ic_favorite);
        } else {
            mImageFavorite.setImageResource(R.drawable.ic_not_favorite);
        }
    }

    private void openNowPlaying() {
        NowPlayingFragment fragment = NowPlayingFragment
                .newInstance()
                .setOnCurrentTrackChangeListener(this);
        mOnCurrentTrackChangeListener = fragment;
        NowPlayingPresenter presenter = new NowPlayingPresenter();
        presenter
                .setPlaylist(mMusicService.getPlaylist())
                .setCurrentTrack(mMusicService.getCurrentTrack())
                .setView(fragment);
        fragment.show(getSupportFragmentManager(), NowPlayingFragment.class.getSimpleName());
    }

    private void handleVolumeChange() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        SeekBar seekBar = new SeekBar(this);
        seekBar.setPadding(50, 20, 50, 20);
        seekBar.setMax(mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) * 10);
        seekBar.setProgress(mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC) * 10);
        builder
                .setTitle(getString(R.string.text_adjust_volume))
                .setIcon(R.drawable.ic_volume_level)
                .setView(seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress / 10, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        builder.create().show();
    }

    private void handleDownloadTrack() {
        if (!mPresenter.checkDownloadedTrack(mTrack)) {
            checkWriteStoragePermission();
        } else {
            Toast.makeText(this, getString(R.string.msg_already_downloaded), Toast.LENGTH_SHORT).show();
        }
    }

    private void downloadTrack() {
        TrackDownloadManager.getInstance(this, this).downloadTrack(mTrack);
        mTrack.setDownloadPath(StringUtils.formatFilePath(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getPath(),
                mTrack.getTitle()));
        mPresenter.saveTrack(mTrack);
    }

    private void handleFavoriteChange() {
        if (mTrack == null) return;
        mTrack.setIsFavorite(!mTrack.isFavorite());
        mPresenter.updateFavorite(mTrack);
    }

    private void setupObjectAnimator(ImageView imageView) {
        if (mObjectAnimator == null) {
            mObjectAnimator = ObjectAnimator.ofFloat(imageView, "rotation", 0, 360);
            mObjectAnimator.setDuration(30000);
            mObjectAnimator.setRepeatCount(ObjectAnimator.INFINITE);
            mObjectAnimator.setInterpolator(new LinearInterpolator());
        }
    }

    private void checkWriteStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_EXTERNAL_STORAGE_CODE);
        } else {
            downloadTrack();
        }
    }

    private void loadImageWithGlide(RequestOptions options, ImageView imageView, String url) {
        Glide.with(getApplicationContext())
                .load(url)
                .apply(options)
                .into(imageView);
    }

    private void setupListener() {
        mImagePlayPause.setOnClickListener(this);
        mImagePrevious.setOnClickListener(this);
        mImageNext.setOnClickListener(this);
        mImageLoop.setOnClickListener(this);
        mImageShuffle.setOnClickListener(this);
        mSeekBarDuration.setOnSeekBarChangeListener(this);
        mImageArrowDown.setOnClickListener(this);
        mImageDownload.setOnClickListener(this);
        mImageFavorite.setOnClickListener(this);
        mImageNowPlaying.setOnClickListener(this);
        mImageVolume.setOnClickListener(this);
    }

    private void setupPreferences() {
        mImageBackground = findViewById(R.id.image_transparent);
        mImageArrowDown = findViewById(R.id.image_arrow_down);
        mImageVolume = findViewById(R.id.image_volume);
        mImageArtwork = findViewById(R.id.image_artwork);
        mTextTrackTitle = findViewById(R.id.text_track_title);
        mTextTrackArtist = findViewById(R.id.text_track_artist);
        mSeekBarDuration = findViewById(R.id.seek_bar_duration);
        mTextCurrentDuration = findViewById(R.id.text_current_duration);
        mTextFullDuration = findViewById(R.id.text_full_duration);
        mImageLoop = findViewById(R.id.image_loop);
        mImagePrevious = findViewById(R.id.image_previous);
        mImagePlayPause = findViewById(R.id.image_play);
        mImageNext = findViewById(R.id.image_next);
        mImageShuffle = findViewById(R.id.image_shuffle);
        mImageDownload = findViewById(R.id.image_download);
        mImageFavorite = findViewById(R.id.image_favorite);
        mImageNowPlaying = findViewById(R.id.image_now_playing);
        mProgressBarLoading = findViewById(R.id.progress_bar_loading);
    }
}
