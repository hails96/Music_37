package lsh.framgia.com.isoundcloud.screen.main.search;

import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import lsh.framgia.com.isoundcloud.R;
import lsh.framgia.com.isoundcloud.base.EndlessScrollListener;
import lsh.framgia.com.isoundcloud.base.mvp.BaseFragment;
import lsh.framgia.com.isoundcloud.data.model.Track;
import lsh.framgia.com.isoundcloud.data.repository.TrackRepository;
import lsh.framgia.com.isoundcloud.data.source.local.TrackLocalDataSource;
import lsh.framgia.com.isoundcloud.data.source.remote.TrackRemoteDataSource;
import lsh.framgia.com.isoundcloud.screen.main.MainActivity;
import lsh.framgia.com.isoundcloud.screen.main.bottommenu.BottomMenuDialogFragment;
import lsh.framgia.com.isoundcloud.screen.main.bottommenu.BottomMenuDialogPresenter;
import lsh.framgia.com.isoundcloud.screen.main.genre.TrackAdapter;
import lsh.framgia.com.isoundcloud.screen.player.PlayerActivity;

public class SearchFragment extends BaseFragment<SearchContract.Presenter>
        implements SearchContract.View, TrackAdapter.OnTrackItemClickListener,
        TextView.OnEditorActionListener {

    private static final int DELAY_SEARCH_TIME = 500;

    private EditText mEditTextSearch;
    private RecyclerView mRecyclerResult;
    private ProgressBar mProgressBarLoading;

    private TrackAdapter mTrackAdapter;
    private Handler mHandler;
    private Runnable mRunnable;

    private EndlessScrollListener mScrollListener;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search;
    }

    @Override
    protected void initLayout() {
        setupReferences();
        showKeyBoard(mEditTextSearch);
        setupEditTextSearch();
        setupRecyclerTrack();
    }

    @Override
    public void onStop() {
        super.onStop();
        hideKeyBoard(mEditTextSearch);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateTracks(List<Track> tracks) {
        mTrackAdapter.addAll(tracks);
        mProgressBarLoading.setVisibility(View.GONE);
    }

    @Override
    public void onTrackClick(Track track) {
        if (getActivity() == null) return;
        ((MainActivity) getActivity()).setPlaylist(mTrackAdapter.getTracks());
        getActivity().startActivity(PlayerActivity.getPlayerIntent(getContext(), track));
    }

    @Override
    public void onMenuClick(Track track) {
        BottomMenuDialogFragment fragment = BottomMenuDialogFragment.newInstance();
        BottomMenuDialogPresenter presenter = new BottomMenuDialogPresenter(
                TrackRepository.getInstance(TrackRemoteDataSource.getInstance(),
                        TrackLocalDataSource.getInstance(getContext())));
        presenter
                .setTrack(track)
                .setView(fragment);
        fragment.show(getChildFragmentManager(), BottomMenuDialogFragment.class.getSimpleName());
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            hideKeyBoard(mEditTextSearch);
            clearSearchQuery();
            mPresenter.getSearchResult(mEditTextSearch.getText().toString());
            return true;
        }
        return false;
    }

    private void setupRecyclerTrack() {
        mRecyclerResult.setHasFixedSize(true);
        mRecyclerResult.setLayoutManager(new LinearLayoutManager(getContext()));
        mTrackAdapter = new TrackAdapter(getContext(), new ArrayList<Track>());
        mTrackAdapter.setOnTrackItemClickListener(this);
        mRecyclerResult.setAdapter(mTrackAdapter);
        mScrollListener = new EndlessScrollListener() {
            @Override
            public void onLoadMore() {
                mProgressBarLoading.setVisibility(View.VISIBLE);
                mPresenter.getSearchResult(mEditTextSearch.getText().toString());
            }
        };
        mRecyclerResult.addOnScrollListener(mScrollListener);
    }

    private void setupEditTextSearch() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mRunnable == null) return;
                mHandler.removeCallbacks(mRunnable);
            }

            @Override
            public void afterTextChanged(Editable s) {
                mHandler.postDelayed(mRunnable, DELAY_SEARCH_TIME);
            }
        };
        mEditTextSearch.addTextChangedListener(textWatcher);
        mEditTextSearch.setOnEditorActionListener(this);
        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                clearSearchQuery();
                mPresenter.getSearchResult(mEditTextSearch.getText().toString());
            }
        };
    }

    private void clearSearchQuery() {
        mScrollListener.resetListener();
        mTrackAdapter.getTracks().clear();
        mTrackAdapter.notifyDataSetChanged();
        mPresenter.clearSearchQuery();
    }

    private void setupReferences() {
        mEditTextSearch = mRootView.findViewById(R.id.edit_text_search);
        mRecyclerResult = mRootView.findViewById(R.id.recycler_result);
        mProgressBarLoading = mRootView.findViewById(R.id.progress_bar_loading);
    }
}
