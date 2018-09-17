package lsh.framgia.com.isoundcloud.screen.main.home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import lsh.framgia.com.isoundcloud.R;
import lsh.framgia.com.isoundcloud.data.Genre;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.GenreViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Genre> mGenres;

    public GenreAdapter(Context context, List<Genre> genres) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mGenres = genres;
    }

    @NonNull
    @Override
    public GenreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GenreViewHolder(mInflater.inflate(R.layout.item_genre, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GenreViewHolder holder, int position) {
        holder.bindData(mContext, mGenres.get(position));
    }

    @Override
    public int getItemCount() {
        return mGenres == null ? 0 : mGenres.size();
    }

    static class GenreViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageArtwork;
        private TextView mTextTitle;

        GenreViewHolder(View itemView) {
            super(itemView);
            mImageArtwork = itemView.findViewById(R.id.image_artwork);
            mTextTitle = itemView.findViewById(R.id.text_title);
        }

        void bindData(Context context, Genre genre) {
            mImageArtwork.setImageResource(genre.getArtworkResId());
            mTextTitle.setText(context.getString(genre.getNameResId()));
        }
    }
}
