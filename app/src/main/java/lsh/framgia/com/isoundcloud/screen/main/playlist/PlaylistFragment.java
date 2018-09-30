package lsh.framgia.com.isoundcloud.screen.main.playlist;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import lsh.framgia.com.isoundcloud.R;
import lsh.framgia.com.isoundcloud.base.mvp.BaseFragment;
import lsh.framgia.com.isoundcloud.data.model.Playlist;
import lsh.framgia.com.isoundcloud.data.repository.TrackRepository;
import lsh.framgia.com.isoundcloud.data.source.local.TrackLocalDataSource;
import lsh.framgia.com.isoundcloud.data.source.remote.TrackRemoteDataSource;
import lsh.framgia.com.isoundcloud.screen.main.OnToolbarChangeListener;
import lsh.framgia.com.isoundcloud.screen.main.playlisttrack.PlaylistTrackFragment;
import lsh.framgia.com.isoundcloud.screen.main.playlisttrack.PlaylistTrackPresenter;
import lsh.framgia.com.isoundcloud.util.StringUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlaylistFragment extends BaseFragment<PlaylistContract.Presenter>
        implements PlaylistContract.View, View.OnClickListener, OnPlaylistClickListener {

    private RecyclerView mRecyclerPlaylist;
    private FloatingActionButton mFabAddPlaylist;

    private OnToolbarChangeListener mOnToolbarChangeListener;
    private PlaylistAdapter mPlaylistAdapter;
    private List<Playlist> mPlaylists;

    public static PlaylistFragment newInstance() {
        return new PlaylistFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnToolbarChangeListener = (OnToolbarChangeListener) context;
            mOnToolbarChangeListener.onTitleChange(getString(R.string.text_playlist));
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnToolbarChangeListener/OnPlaylistChangeListener");
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_playlist;
    }

    @Override
    protected void initLayout() {
        setupPreferences();
        setupRecyclerPlaylist();
        setupListeners();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.getPlaylists();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_add_playlist:
                handleAddNewPlaylist();
                break;
            default:
                break;
        }
    }

    @Override
    public void createPlaylistSuccess() {
        mPresenter.getPlaylists();
        Toast.makeText(getContext(), getString(R.string.msg_created_playlist_successfully),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void failedToCreatePlaylist(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showPlaylists(List<Playlist> playlists) {
        mPlaylists.clear();
        mPlaylists.addAll(playlists);
        mPlaylistAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPlaylistClick(Playlist playlist) {
        PlaylistTrackFragment fragment = PlaylistTrackFragment.newInstance();
        fragment.setPlaylist(playlist);
        PlaylistTrackPresenter presenter = new PlaylistTrackPresenter(
                TrackRepository.getInstance(TrackRemoteDataSource.getInstance(),
                        TrackLocalDataSource.getInstance(getContext())));
        presenter.setView(fragment);
        replaceFragment(R.id.frame_container, fragment, true, null);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mOnToolbarChangeListener == null) return;
        mOnToolbarChangeListener.onTitleChange(getString(R.string.label_library));
    }

    private void setupRecyclerPlaylist() {
        mRecyclerPlaylist.setHasFixedSize(true);
        mRecyclerPlaylist.setLayoutManager(new LinearLayoutManager(getContext()));
        mPlaylists = new ArrayList<>();
        mPlaylistAdapter = new PlaylistAdapter(getContext(), mPlaylists, this);
        mRecyclerPlaylist.setAdapter(mPlaylistAdapter);
    }

    private void handleAddNewPlaylist() {
        if (getContext() == null) return;
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final EditText editTextPlaylist = new EditText(getContext());
        builder
                .setTitle(getString(R.string.text_create_new_playlist))
                .setIcon(R.drawable.ic_playlist_add)
                .setView(editTextPlaylist)
                .setPositiveButton(R.string.text_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String playlistName = editTextPlaylist.getText().toString();
                        if (StringUtils.isEmpty(playlistName.trim())) {
                            Toast.makeText(getContext(), getString(R.string.error_empty_playlist_name),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            mPresenter.createNewPlaylist(playlistName);
                        }
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.text_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    private void setupListeners() {
        mFabAddPlaylist.setOnClickListener(this);
    }

    private void setupPreferences() {
        mRecyclerPlaylist = mRootView.findViewById(R.id.recycler_playlist);
        mFabAddPlaylist = mRootView.findViewById(R.id.fab_add_playlist);
    }
}
