package lsh.framgia.com.isoundcloud.screen.main.home;

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

public class RecentlyDownloadedAdapter extends RecyclerView.Adapter
        <RecentlyDownloadedAdapter.ViewHolder> {

    private static final int MAXIMUM_ITEMS = 3;

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Track> mTracks;
    private OnTrackClickListener mOnTrackClickListener;

    public RecentlyDownloadedAdapter(Context context, List<Track> tracks, OnTrackClickListener listener) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mTracks = tracks;
        mOnTrackClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_recently_downloaded, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(mContext, mTracks.get(position), mOnTrackClickListener);
    }

    @Override
    public int getItemCount() {
        if (mTracks == null) return 0;
        if (mTracks.size() > MAXIMUM_ITEMS) return MAXIMUM_ITEMS;
        return mTracks.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageArtwork;
        private TextView mTextTitle;
        private TextView mTextArtist;

        ViewHolder(View itemView) {
            super(itemView);
            mImageArtwork = itemView.findViewById(R.id.image_artwork);
            mTextTitle = itemView.findViewById(R.id.text_title);
            mTextArtist = itemView.findViewById(R.id.text_artist);
        }

        private void bindData(Context context, final Track track, final OnTrackClickListener onTrackClickListener) {
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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onTrackClickListener != null) onTrackClickListener.onTrackClick(track);
                }
            });
        }
    }

    public interface OnTrackClickListener {
        void onTrackClick(Track track);
    }
}
