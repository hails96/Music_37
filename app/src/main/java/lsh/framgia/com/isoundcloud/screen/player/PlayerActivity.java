package lsh.framgia.com.isoundcloud.screen.player;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import lsh.framgia.com.isoundcloud.R;
import lsh.framgia.com.isoundcloud.base.mvp.BaseActivity;
import lsh.framgia.com.isoundcloud.data.model.Track;
import lsh.framgia.com.isoundcloud.service.OnMediaPlayerStatusListener;
import lsh.framgia.com.isoundcloud.util.StringUtils;

public class PlayerActivity extends BaseActivity<PlayerContract.Presenter>
        implements PlayerContract.View, OnMediaPlayerStatusListener {

    public static final String EXTRA_TRACK = "lsh.framgia.com.isoundcloud.EXTRA_TRACK";

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
    private ImageView mImagePlay;
    private ImageView mImageNext;
    private ImageView mImageShuffle;
    private ImageView mImageDownload;
    private ImageView mImageFavorite;
    private ImageView mImageNowPlaying;

    private RequestOptions mBackGroundOptions;
    private RequestOptions mArtworkOptions;
    private Track mTrack;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_player;
    }

    @Override
    protected void initLayout() {
        mTrack = getIntent().getParcelableExtra(EXTRA_TRACK);
        if (mTrack == null) return;
        setPresenter(new PlayerPresenter());
        showProgress();
        setupPreferences();
        setupOptions();
        setupView(mTrack);
        hideProgress();
    }

    @Override
    protected OnMediaPlayerStatusListener getMediaPlayerStatusListener() {
        return this;
    }

    @Override
    protected void onMusicServiceConnected() {
        if (mMusicService.isPlaying()) {
            // TODO: bind current track info
        } else {
            mMusicService.playTrack(mTrack);
        }
    }

    @Override
    public void onTrackPrepared(Track track) {

    }

    @Override
    public void onTrackPaused() {

    }

    @Override
    public void onTrackResumed() {

    }

    public static Intent getPlayerIntent(Context context, Track track) {
        Intent intent = new Intent(context, PlayerActivity.class);
        intent.putExtra(EXTRA_TRACK, track);
        return intent;
    }

    private void setupOptions() {
        mBackGroundOptions = new RequestOptions()
                .centerCrop()
                .placeholder(mTrack.getGenreArtworkResId())
                .error(mTrack.getGenreArtworkResId());

        mArtworkOptions = new RequestOptions()
                .centerCrop()
                .circleCrop()
                .placeholder(R.drawable.bg_track_place_holder)
                .placeholder(R.drawable.bg_track_place_holder);
    }

    private void setupView(Track track) {
        if (track == null) return;
        displayArtwork(track);
        displayTrackInfo(track);
        updateDownloadableView(track.isDownloadable());
        updateFavoriteView(track.isFavorite());
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
        mImagePlay = findViewById(R.id.image_play);
        mImageNext = findViewById(R.id.image_next);
        mImageShuffle = findViewById(R.id.image_shuffle);
        mImageDownload = findViewById(R.id.image_download);
        mImageFavorite = findViewById(R.id.image_favorite);
        mImageNowPlaying = findViewById(R.id.image_now_playing);
    }
}
