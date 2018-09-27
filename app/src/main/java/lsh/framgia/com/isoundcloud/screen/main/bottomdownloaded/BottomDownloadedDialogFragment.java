package lsh.framgia.com.isoundcloud.screen.main.bottomdownloaded;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import lsh.framgia.com.isoundcloud.R;
import lsh.framgia.com.isoundcloud.data.model.Track;
import lsh.framgia.com.isoundcloud.util.DialogUtils;

public class BottomDownloadedDialogFragment extends BottomSheetDialogFragment
        implements BottomDownloadedDialogContract.View {

    private BottomDownloadedDialogContract.Presenter mPresenter;

    private TextView mTextTrackInfo;

    public static BottomDownloadedDialogFragment newInstance() {
        return new BottomDownloadedDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bottom_downloaded, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupPreferences(view);
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.start();
    }

    @Override
    public void setPresenter(BottomDownloadedDialogContract.Presenter presenter) {
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
        mTextTrackInfo.setText(track.getTitle());
    }

    @Override
    public void back() {

    }

    private void setupPreferences(View view) {
        mTextTrackInfo = view.findViewById(R.id.text_track_info);
    }

}
