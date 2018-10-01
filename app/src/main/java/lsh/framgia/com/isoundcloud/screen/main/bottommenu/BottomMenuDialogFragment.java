package lsh.framgia.com.isoundcloud.screen.main.bottommenu;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import lsh.framgia.com.isoundcloud.MusicApplication;
import lsh.framgia.com.isoundcloud.R;
import lsh.framgia.com.isoundcloud.data.model.Playlist;
import lsh.framgia.com.isoundcloud.data.model.Track;
import lsh.framgia.com.isoundcloud.data.source.remote.TrackDownloadManager;
import lsh.framgia.com.isoundcloud.screen.main.MainActivity;
import lsh.framgia.com.isoundcloud.util.DialogUtils;
import lsh.framgia.com.isoundcloud.util.StringUtils;

public class BottomMenuDialogFragment extends BottomSheetDialogFragment
        implements BottomMenuDialogContract.View, View.OnClickListener,
        TrackDownloadManager.OnDownloadListener {

    private static final int WRITE_EXTERNAL_STORAGE_CODE = 100;

    private BottomMenuDialogContract.Presenter mPresenter;

    private TextView mTextTrackInfo;
    private TextView mTextFavorite;
    private TextView mTextDownload;
    private TextView mTextPlaylist;
    private TextView mTextDelete;
    private ImageView mImageFavorite;
    private ImageView mImageDownload;
    private ImageView mImagePlaylist;
    private ImageView mImageDelete;

    private MainActivity mActivity;
    private Track mTrack;
    private List<Playlist> mPlaylists;

    public static BottomMenuDialogFragment newInstance() {
        return new BottomMenuDialogFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mActivity = (MainActivity) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " is not MainActivity");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bottom_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupPreferences(view);
        setupListeners();
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.start();
    }

    @Override
    public void setPresenter(BottomMenuDialogContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public Context getViewContext() {
        return getContext();
    }

    @Override
    public void showProgress() {
        DialogUtils.showProgressDialog(getActivity());
    }

    @Override
    public void hideProgress() {
        DialogUtils.dismissProgressDialog();
    }

    @Override
    public void showTrack(Track track) {
        mTrack = track;
        mTextTrackInfo.setText(mTrack.getTitle());
        updateFavoriteView(mTrack);
        updateDownloadView(mTrack);
    }

    @Override
    public void updateFavoriteSuccess(Boolean isFavorite) {
        if (isFavorite) {
            Toast.makeText(getContext(), getString(R.string.msg_added_to_favorite),
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), getString(R.string.msg_removed_to_favorite),
                    Toast.LENGTH_SHORT).show();
        }
        mTrack.setIsFavorite(isFavorite);
        updateFavoriteView(mTrack);
    }

    @Override
    public void back() {

    }

    @Override
    public void onDownload(Track track) {
        mTrack.setRequestId(track.getRequestId());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.text_favorite:
            case R.id.image_favorite:
                handleFavoriteChange();
                break;
            case R.id.text_download:
            case R.id.image_download:
                handleDownloadTrack();
                break;
            case R.id.text_playlist:
            case R.id.image_playlist:
                handleAddToPlaylist();
                break;
            case R.id.text_delete:
            case R.id.image_delete:
                handleDeleteTrack();
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case WRITE_EXTERNAL_STORAGE_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    downloadTrack();
                } else {
                    Toast.makeText(getContext(), getString(R.string.error_download_track),
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void enableDownloadAction() {
        mTextDownload.setVisibility(View.VISIBLE);
        mImageDownload.setVisibility(View.VISIBLE);
    }

    @Override
    public void enableDeleteAction() {
        mTextDelete.setVisibility(View.VISIBLE);
        mImageDelete.setVisibility(View.VISIBLE);
    }

    @Override
    public void deleteTrackSuccess(Track track) {
        dismiss();
        Toast.makeText(getContext(), getString(R.string.msg_delete_successfully), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void deleteTrackFailed(String msg) {
        dismiss();
        Toast.makeText(getContext(), getString(R.string.msg_delete_failed, msg), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getPlaylistsSuccess(List<Playlist> playlists) {
        mPlaylists = playlists;
    }

    @Override
    public void addedTrackSuccessfully() {
        Toast.makeText(MusicApplication.getAppContext(), MusicApplication.getAppContext().getString(
                R.string.msg_add_track_to_new_playlist), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void failedToAddTrack(String msg) {
        Toast.makeText(MusicApplication.getAppContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void handleAddToPlaylist() {
        dismiss();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_to_playlist, null);
        final Spinner spinnerPlaylist = dialogView.findViewById(R.id.spinner_existed_playlist);
        final EditText editTextPlaylist = dialogView.findViewById(R.id.edit_playlist);
        setupAddToPlaylistView(spinnerPlaylist, editTextPlaylist);
        builder
                .setTitle(getString(R.string.label_add_to_playlist))
                .setIcon(R.drawable.ic_playlist_add)
                .setView(dialogView)
                .setPositiveButton(R.string.text_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (editTextPlaylist != null && !StringUtils.isEmpty(editTextPlaylist.getText().toString())) {
                            mPresenter.addTrackToNewPlaylist(mTrack, editTextPlaylist.getText().toString());
                        } else {
                            if (spinnerPlaylist == null) return;
                            mPresenter.addTrackToExistingPlaylist(mTrack,
                                    mPlaylists.get(spinnerPlaylist.getSelectedItemPosition()));
                        }
                    }
                })
                .setNegativeButton(R.string.text_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });
        builder.create().show();
    }

    private void setupAddToPlaylistView(final Spinner spinnerPlaylist, EditText editTextPlaylist) {
        if (spinnerPlaylist != null) {
            List<String> names = getPlaylistNames();
            spinnerPlaylist.setAdapter(new ArrayAdapter<>(MusicApplication.getAppContext(),
                    android.R.layout.simple_spinner_dropdown_item, names));
        }
        if (spinnerPlaylist == null) return;
        editTextPlaylist.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    spinnerPlaylist.setEnabled(false);
                    spinnerPlaylist.setAlpha(0.5f);
                } else {
                    spinnerPlaylist.setEnabled(true);
                    spinnerPlaylist.setAlpha(1.0f);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private List<String> getPlaylistNames() {
        List<String> result = new ArrayList<>();
        if (mPlaylists == null || mPlaylists.isEmpty()) return result;
        for (Playlist playlist : mPlaylists) {
            result.add(playlist.getName());
        }
        return result;
    }

    private void handleDownloadTrack() {
        if (!mPresenter.checkDownloadedTrack(mTrack)) {
            checkWriteStoragePermission();
        } else {
            Toast.makeText(getContext(), getString(R.string.msg_already_downloaded), Toast.LENGTH_SHORT).show();
        }
    }

    private void checkWriteStoragePermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_EXTERNAL_STORAGE_CODE);
        } else {
            downloadTrack();
        }
    }

    private void downloadTrack() {
        TrackDownloadManager.getInstance(getContext(), this).downloadTrack(mTrack);
        mTrack.setDownloadPath(StringUtils.formatFilePath(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getPath(),
                mTrack.getTitle()));
        mPresenter.saveTrack(mTrack);
        dismiss();
    }

    private void updateDownloadView(Track track) {
        if (track.isDownloadable()) {
            mImageDownload.setImageResource(R.drawable.ic_downloadable);
            mImageDownload.setClickable(true);
            mTextDownload.setClickable(true);
        } else {
            mImageDownload.setImageResource(R.drawable.ic_not_downloadable);
            mImageDownload.setClickable(false);
            mTextDownload.setClickable(false);
        }
    }

    private void handleFavoriteChange() {
        if (mTrack == null) return;
        mTrack.setIsFavorite(!mTrack.isFavorite());
        mPresenter.updateFavorite(mTrack);
    }

    private void updateFavoriteView(Track track) {
        if (track.isFavorite()) {
            mTextFavorite.setText(getContext().getString(R.string.label_remove_from_favorite));
            mImageFavorite.setImageResource(R.drawable.ic_favorite);
        } else {
            mTextFavorite.setText(getContext().getString(R.string.label_add_to_favorite));
            mImageFavorite.setImageResource(R.drawable.ic_not_favorite);
        }
    }

    private void handleDeleteTrack() {
        if (mActivity.getCurrentTrack().equals(mTrack)) {
            Toast.makeText(mActivity, getString(R.string.error_cannot_delete_playing_track),
                    Toast.LENGTH_SHORT).show();
            dismiss();
            return;
        }
        mPresenter.deleteTrackFromDatabase(mTrack);
    }

    private void setupListeners() {
        mImageFavorite.setOnClickListener(this);
        mImageDownload.setOnClickListener(this);
        mImagePlaylist.setOnClickListener(this);
        mImageDelete.setOnClickListener(this);
        mTextFavorite.setOnClickListener(this);
        mTextDownload.setOnClickListener(this);
        mTextPlaylist.setOnClickListener(this);
        mTextDelete.setOnClickListener(this);
    }

    private void setupPreferences(View view) {
        mTextTrackInfo = view.findViewById(R.id.text_track_info);
        mImageFavorite = view.findViewById(R.id.image_favorite);
        mImageDownload = view.findViewById(R.id.image_download);
        mImagePlaylist = view.findViewById(R.id.image_playlist);
        mImageDelete = view.findViewById(R.id.image_delete);
        mTextFavorite = view.findViewById(R.id.text_favorite);
        mTextDownload = view.findViewById(R.id.text_download);
        mTextPlaylist = view.findViewById(R.id.text_playlist);
        mTextDelete = view.findViewById(R.id.text_delete);
    }
}
