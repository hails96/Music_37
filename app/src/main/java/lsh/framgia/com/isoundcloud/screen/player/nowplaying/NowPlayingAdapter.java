package lsh.framgia.com.isoundcloud.screen.player.nowplaying;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import lsh.framgia.com.isoundcloud.R;
import lsh.framgia.com.isoundcloud.data.model.Track;
import lsh.framgia.com.isoundcloud.screen.main.genre.TrackAdapter;

public class NowPlayingAdapter extends TrackAdapter {

    public NowPlayingAdapter(Context context, List<Track> tracks) {
        super(context, tracks);
    }

    @NonNull
    @Override
    public TrackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NowPlayingViewHolder(mInflater.inflate(R.layout.item_track, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TrackViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (holder instanceof NowPlayingViewHolder) {
            ((NowPlayingViewHolder) holder).highlightCurrentTrack(
                    mContext, mTracks.get(position), mCurrentTrack);
        }
    }

    static class NowPlayingViewHolder extends TrackViewHolder {

        private NowPlayingViewHolder(View itemView) {
            super(itemView);
        }

        void highlightCurrentTrack(Context context, Track track, Track currentTrack) {
            if (track.equals(currentTrack)) {
                mImageAction.setVisibility(View.VISIBLE);
                mImageAction.setImageResource(R.drawable.ic_headset);
                mTextTitle.setTextColor(context.getResources().getColor(R.color.color_primary_dark));
                mTextArtist.setTextColor(context.getResources().getColor(R.color.color_primary));
                mTextDuration.setTextColor(context.getResources().getColor(R.color.color_primary));
            } else {
                mImageAction.setVisibility(View.INVISIBLE);
                mTextTitle.setTextColor(Color.BLACK);
                mTextArtist.setTextColor(context.getResources().getColor(R.color.color_gray));
                mTextDuration.setTextColor(context.getResources().getColor(R.color.color_gray));
            }
        }
    }
}
