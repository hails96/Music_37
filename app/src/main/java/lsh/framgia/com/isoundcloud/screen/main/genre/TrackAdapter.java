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

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.ViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Track> mTracks;
    private OnTrackItemClickListener mOnTrackItemClickListener;

    public TrackAdapter(Context context, List<Track> tracks, OnTrackItemClickListener listener) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mTracks = tracks;
        mOnTrackItemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_track, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(mContext, mTracks.get(position), mOnTrackItemClickListener);
    }

    @Override
    public int getItemCount() {
        return mTracks == null ? 0 : mTracks.size();
    }

    public void addAll(List<Track> tracks) {
        if (tracks != null || !tracks.isEmpty()) {
            mTracks.addAll(tracks);
            notifyDataSetChanged();
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageArtwork;
        private TextView mTextTitle;
        private TextView mTextArtist;
        private TextView mTextDuration;
        private ImageView mImageMenu;

        ViewHolder(View itemView) {
            super(itemView);
            mImageArtwork = itemView.findViewById(R.id.image_artwork);
            mTextTitle = itemView.findViewById(R.id.text_title);
            mTextArtist = itemView.findViewById(R.id.text_artist);
            mTextDuration = itemView.findViewById(R.id.text_duration);
            mImageMenu = itemView.findViewById(R.id.image_menu);
        }

        void bindData(Context context, final Track track,
                      final OnTrackItemClickListener listener) {
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .circleCrop()
                    .placeholder(R.drawable.ic_circle_place_holder)
                    .error(R.drawable.ic_circle_place_holder);
            Glide.with(context)
                    .load(track.getArtworkUrl())
                    .apply(options)
                    .into(mImageArtwork);
            mTextTitle.setText(track.getTitle());
            mTextArtist.setText(track.getArtist());
            mTextDuration.setText(String.valueOf(track.getDuration()));

            mImageMenu.setOnClickListener(new View.OnClickListener() {
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
