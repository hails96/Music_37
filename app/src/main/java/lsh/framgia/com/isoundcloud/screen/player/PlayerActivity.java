package lsh.framgia.com.isoundcloud.screen.player;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import lsh.framgia.com.isoundcloud.R;
import lsh.framgia.com.isoundcloud.base.mvp.BaseActivity;
import lsh.framgia.com.isoundcloud.constant.TrackState;
import lsh.framgia.com.isoundcloud.data.model.Track;
import lsh.framgia.com.isoundcloud.service.OnMediaPlayerStatusListener;
import lsh.framgia.com.isoundcloud.util.StringUtils;

public class PlayerActivity extends BaseActivity<PlayerContract.Presenter>
        implements PlayerContract.View, OnMediaPlayerStatusListener, View.OnClickListener,
        SeekBar.OnSeekBarChangeListener {

    public static final String EXTRA_TRACK = "lsh.framgia.com.isoundcloud.EXTRA_TRACK";
    private static final int DELAY_TIME = 1000;

    private ImageView mImageBackground;
    private ImageView mImageArrowDown;
    private ImageView mImageAlarmClock;
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

    private RequestOptions mBackGroundOptions;
    private RequestOptions mArtworkOptions;
    private Track mTrack;

    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (mIsBound && mMusicService.isPlaying()) {
                int position = mMusicService.getCurrentPosition();
                mSeekBarDuration.setProgress(position);
                mTextCurrentDuration.setText(StringUtils.convertMillisToDuration(position));
            }
            mHandler.postDelayed(this, DELAY_TIME);
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_player;
    }

    @Override
    protected void initLayout() {
        mTrack = getIntent().getParcelableExtra(EXTRA_TRACK);
        setPresenter(new PlayerPresenter());
        setupPreferences();
        setupListener();
        setupOptions();
        setupView(mTrack);
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
            updatePlayPauseView(mMusicService.getTrackState());
            mHandler.postDelayed(mRunnable, DELAY_TIME);
        }
    }

    @Override
    public void onTrackPrepared(Track track) {
        updatePlayPauseView(mMusicService.getTrackState());
        runOnUiThread(mRunnable);
    }

    @Override
    public void onTrackPaused() {
        mImagePlayPause.setImageResource(R.drawable.ic_play);
    }

    @Override
    public void onTrackResumed() {
        mImagePlayPause.setImageResource(R.drawable.ic_pause);
    }

    @Override
    public void onNewTrackRequested(Track track) {
        mImagePlayPause.setImageResource(R.drawable.ic_pause);
        setupView(track);
    }

    @Override
    public void onTrackError() {
        Toast.makeText(this, getString(R.string.error_player), Toast.LENGTH_SHORT).show();
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
            default:
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mMusicService.seekTo(seekBar.getProgress());
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

    private void setupListener() {
        mImagePlayPause.setOnClickListener(this);
        mImagePrevious.setOnClickListener(this);
        mImageNext.setOnClickListener(this);
        mSeekBarDuration.setOnSeekBarChangeListener(this);
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
        if (mMusicService != null) {
            updatePlayPauseView(mMusicService.getTrackState());
        }
        displayArtwork(track);
        displayTrackInfo(track);
        updateDownloadableView(track.isDownloadable());
        updateFavoriteView(track.isFavorite());
        resetSeekBar(track);
    }

    private void updatePlayPauseView(@TrackState int state) {
        if (state != TrackState.PREPARED) {
            mProgressBarLoading.setVisibility(View.VISIBLE);
            mImagePlayPause.setVisibility(View.INVISIBLE);
        } else {
            mProgressBarLoading.setVisibility(View.GONE);
            mImagePlayPause.setVisibility(View.VISIBLE);
        }
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
        } else {
            mImageDownload.setImageResource(R.drawable.ic_not_downloadable);
        }
    }

    private void updateFavoriteView(boolean isFavorite) {
        if (isFavorite) {
            mImageFavorite.setImageResource(R.drawable.ic_favorite);
        } else {
            mImageFavorite.setImageResource(R.drawable.ic_not_favorite);
        }
    }

    private void loadImageWithGlide(RequestOptions options, ImageView imageView, String url) {
        Glide.with(this)
                .load(url)
                .apply(options)
                .into(imageView);
    }

    private void setupPreferences() {
        mImageBackground = findViewById(R.id.image_transparent);
        mImageArrowDown = findViewById(R.id.image_arrow_down);
        mImageArrowDown = findViewById(R.id.image_alarm_clock);
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
