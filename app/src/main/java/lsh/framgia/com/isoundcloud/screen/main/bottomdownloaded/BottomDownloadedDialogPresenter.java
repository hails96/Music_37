package lsh.framgia.com.isoundcloud.screen.main.bottomdownloaded;

import lsh.framgia.com.isoundcloud.data.model.Track;

public class BottomDownloadedDialogPresenter implements BottomDownloadedDialogContract.Presenter {

    private BottomDownloadedDialogContract.View mView;
    private Track mTrack;

    @Override
    public void setView(BottomDownloadedDialogContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        if (mTrack != null) mView.showTrack(mTrack);
    }

    public BottomDownloadedDialogPresenter setTrack(Track track) {
        mTrack = track;
        return this;
    }
}
