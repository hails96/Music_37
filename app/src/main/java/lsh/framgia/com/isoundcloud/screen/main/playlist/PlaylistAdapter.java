package lsh.framgia.com.isoundcloud.screen.main.playlist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import lsh.framgia.com.isoundcloud.R;
import lsh.framgia.com.isoundcloud.constant.Constant;
import lsh.framgia.com.isoundcloud.data.model.Playlist;
import lsh.framgia.com.isoundcloud.util.StringUtils;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Playlist> mPlaylists;
    private OnPlaylistClickListener mOnPlaylistClickListener;

    public PlaylistAdapter(Context context, List<Playlist> playlists, OnPlaylistClickListener listener) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mPlaylists = playlists;
        mOnPlaylistClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_playlist, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(mPlaylists.get(position), mOnPlaylistClickListener);
    }

    @Override
    public int getItemCount() {
        return mPlaylists == null ? 0 : mPlaylists.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTextName;
        private TextView mCreatedDate;
        private TextView mNumberOfPlays;

        ViewHolder(View itemView) {
            super(itemView);
            mTextName = itemView.findViewById(R.id.text_playlist_name);
            mCreatedDate = itemView.findViewById(R.id.text_playlist_created_date);
            mNumberOfPlays = itemView.findViewById(R.id.text_playlist_number_of_plays);
        }

        void bindData(final Playlist playlist, final OnPlaylistClickListener listener) {
            mTextName.setText(playlist.getName());
            mCreatedDate.setText(StringUtils.formatMillisToDate(
                    playlist.getCreatedDate(), Constant.FORMAT_DATE));
            mNumberOfPlays.setText(String.valueOf(playlist.getNumberOfPlays()));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onPlaylistClick(playlist);
                    }
                }
            });
        }
    }
}
