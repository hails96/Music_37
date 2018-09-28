package lsh.framgia.com.isoundcloud.screen.main.genre;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import lsh.framgia.com.isoundcloud.R;
import lsh.framgia.com.isoundcloud.data.model.Track;
import lsh.framgia.com.isoundcloud.util.StringUtils;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.TrackViewHolder> {

    protected Context mContext;
    protected LayoutInflater mInflater;
    protected List<Track> mTracks;
    protected OnTrackItemClickListener mOnTrackItemClickListener;
    protected Track mCurrentTrack;

    public TrackAdapter(Context context, List<Track> tracks) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mTracks = tracks;
    }

    @NonNull
    @Override
    public TrackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TrackViewHolder(mInflater.inflate(R.layout.item_track, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TrackViewHolder holder, int position) {
        holder.bindData(mContext, mTracks.get(position), mCurrentTrack, mOnTrackItemClickListener);
    }

    @Override
    public int getItemCount() {
        return mTracks == null ? 0 : mTracks.size();
    }

    public void setOnTrackItemClickListener(OnTrackItemClickListener listener) {
        mOnTrackItemClickListener = listener;
    }

    public void addAll(List<Track> tracks) {
        if (tracks == null || tracks.isEmpty()) return;
        mTracks.addAll(tracks);
        notifyDataSetChanged();
    }

    public List<Track> getTracks() {
        return mTracks;
    }

    public void setCurrentTrack(Track track) {
        mCurrentTrack = track;
        notifyDataSetChanged();
    }

    protected static class TrackViewHolder extends RecyclerView.ViewHolder {

        protected ImageView mImageArtwork;
        protected TextView mTextTitle;
        protected TextView mTextArtist;
        protected TextView mTextDuration;
        protected ImageView mImageAction;

        protected TrackViewHolder(View itemView) {
            super(itemView);
            mImageArtwork = itemView.findViewById(R.id.image_artwork);
            mTextTitle = itemView.findViewById(R.id.text_title);
            mTextArtist = itemView.findViewById(R.id.text_artist);
            mTextDuration = itemView.findViewById(R.id.text_duration);
            mImageAction = itemView.findViewById(R.id.image_menu);
        }

        private void bindData(Context context, final Track track, Track currentTrack,
                              final OnTrackItemClickListener listener) {
            displayArtwork(context, track);
            displayTrackInfo(track);
            setupListeners(track, listener);
        }

        private void displayArtwork(Context context, Track track) {
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .circleCrop()
                    .placeholder(R.drawable.bg_track_place_holder)
                    .error(R.drawable.bg_track_place_holder);
            Glide.with(context)
                    .load(track.getArtworkUrl())
                    .apply(options)
                    .into(mImageArtwork);
        }

        private void displayTrackInfo(Track track) {
            mTextTitle.setText(track.getTitle());
            mTextArtist.setText(track.getArtist());
            mTextDuration.setText(StringUtils.convertMillisToDuration(track.getDuration()));
        }

        private void setupListeners(final Track track, final OnTrackItemClickListener listener) {
            mImageAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener == null) return;
                    listener.onMenuClick(track);
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener == null) return;
                    listener.onTrackClick(track);
                }
            });
        }
    }

    public interface OnTrackItemClickListener {
        void onTrackClick(Track track);

        void onMenuClick(Track track);
    }
}
