package lsh.framgia.com.isoundcloud.screen.main.bottommenu;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import lsh.framgia.com.isoundcloud.R;
import lsh.framgia.com.isoundcloud.data.model.Track;
import lsh.framgia.com.isoundcloud.data.source.remote.TrackDownloadManager;
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
    private ImageView mImageFavorite;
    private ImageView mImageDownload;
    private ImageView mImagePlaylist;

    private Track mTrack;

    public static BottomMenuDialogFragment newInstance() {
        return new BottomMenuDialogFragment();
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
                // TODO: handle add to playlist event
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
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_EXTERNAL_STORAGE_CODE);
        } else {
            downloadTrack();
        }
    }

    private void downloadTrack() {
        TrackDownloadManager.getInstance(getContext(), this).downloadTrack(mTrack);
        mTrack.setDownloadPath(StringUtils.formatFilePath(Environment.DIRECTORY_MUSIC, mTrack.getTitle()));
        mPresenter.saveTrack(mTrack);
    }

    private void handleFavoriteChange() {
        if (mTrack == null) return;
        mTrack.setIsFavorite(!mTrack.isFavorite());
        mPresenter.updateFavorite(mTrack);
    }

    private void updateDownloadView(Track track) {
        if (track.isDownloadable()) {
            mImageDownload.setImageResource(R.drawable.ic_downloadable);
            mImageDownload.setClickable(true);
        } else {
            mImageDownload.setImageResource(R.drawable.ic_not_downloadable);
            mImageDownload.setClickable(false);
        }
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

    private void setupListeners() {
        mImageFavorite.setOnClickListener(this);
        mImageDownload.setOnClickListener(this);
        mImagePlaylist.setOnClickListener(this);
        mTextFavorite.setOnClickListener(this);
        mTextDownload.setOnClickListener(this);
        mTextPlaylist.setOnClickListener(this);
    }

    private void setupPreferences(View view) {
        mTextTrackInfo = view.findViewById(R.id.text_track_info);
        mImageFavorite = view.findViewById(R.id.image_favorite);
        mImageDownload = view.findViewById(R.id.image_download);
        mImagePlaylist = view.findViewById(R.id.image_playlist);
        mTextFavorite = view.findViewById(R.id.text_favorite);
        mTextDownload = view.findViewById(R.id.text_download);
        mTextPlaylist = view.findViewById(R.id.text_playlist);
    }
}
