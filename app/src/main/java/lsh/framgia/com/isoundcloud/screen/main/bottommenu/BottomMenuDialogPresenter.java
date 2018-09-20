package lsh.framgia.com.isoundcloud.screen.main.bottommenu;

import lsh.framgia.com.isoundcloud.data.model.Track;

public class BottomMenuDialogPresenter implements BottomMenuDialogContract.Presenter {

    private BottomMenuDialogContract.View mView;
    private Track mTrack;

    @Override
    public void setView(BottomMenuDialogContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        if (mTrack != null) mView.setupTrack(mTrack);
    }

    public BottomMenuDialogPresenter setTrack(Track track) {
        mTrack = track;
        return this;
    }
}
