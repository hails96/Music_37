package lsh.framgia.com.isoundcloud.screen.main.home;


import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import lsh.framgia.com.isoundcloud.R;
import lsh.framgia.com.isoundcloud.base.mvp.BaseFragment;
import lsh.framgia.com.isoundcloud.data.Genre;
import lsh.framgia.com.isoundcloud.screen.main.genre.GenreFragment;
import lsh.framgia.com.isoundcloud.screen.main.genre.GenrePresenter;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment<HomeContract.Presenter> implements HomeContract.View,
        GenreAdapter.OnGenreClickListener {

    private static final int COLUMN_NUMBERS = 2;
    private static final int SPLIT_NUMBER = 3;
    private static final int DOUBLE_COLUMN = 2;
    private static final int SINGLE_COLUMN = 1;

    private RecyclerView mRecyclerRecentlyDownloaded;
    private RecyclerView mRecyclerGenre;


    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initLayout() {
        setupPreferences();
        setupRecyclers();
    }

    @Override
    public void onGenreClick(Genre genre) {
        GenreFragment genreFragment = GenreFragment.newInstance();
        GenrePresenter genrePresenter = new GenrePresenter();
        genrePresenter
                .setGenre(genre)
                .setView(genreFragment);
        replaceFragment(R.id.frame_container, genreFragment, true, Genre.class.getSimpleName());
    }

    private void setupRecyclers() {
        mRecyclerGenre.setHasFixedSize(true);
        mRecyclerGenre.setNestedScrollingEnabled(false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), COLUMN_NUMBERS);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position % SPLIT_NUMBER == 0) {
                    return DOUBLE_COLUMN;
                } else {
                    return SINGLE_COLUMN;
                }
            }
        });
        mRecyclerGenre.setLayoutManager(gridLayoutManager);
        mRecyclerGenre.setAdapter(new GenreAdapter(getContext(), mPresenter.getGenres(), this));
    }

    private void setupPreferences() {
        mRecyclerRecentlyDownloaded = mRootView.findViewById(R.id.recycler_recently_downloaded);
        mRecyclerGenre = mRootView.findViewById(R.id.recycler_genre);
    }
}
