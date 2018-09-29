package lsh.framgia.com.isoundcloud.screen.main.bottommenu;

import lsh.framgia.com.isoundcloud.constant.MenuType;
import lsh.framgia.com.isoundcloud.data.model.Track;
import lsh.framgia.com.isoundcloud.data.repository.TrackRepository;
import lsh.framgia.com.isoundcloud.data.source.TrackDataSource;

public class BottomMenuDialogPresenter implements BottomMenuDialogContract.Presenter {

    private BottomMenuDialogContract.View mView;
    private TrackRepository mTrackRepository;
    private Track mTrack;
    private int mMenuType;
    private OnDeleteListener mOnDeleteListener;

    public BottomMenuDialogPresenter(TrackRepository trackRepository) {
        mTrackRepository = trackRepository;
    }

    @Override
    public void setView(BottomMenuDialogContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        if (mTrack != null) {
            mTrack.setIsFavorite(checkFavoriteTrack(mTrack));
            mView.showTrack(mTrack);
        }
        if (mMenuType == MenuType.DOWNLOAD) {
            mView.enableDownloadAction();
        } else {
            mView.enableDeleteAction();
        }
    }

    @Override
    public void updateFavorite(Track track) {
        mTrackRepository.updateFavoriteTrack(track, new TrackDataSource.OnLocalResponseListener<Boolean>() {
            @Override
            public void onSuccess(Boolean isFavorite) {
                mView.updateFavoriteSuccess(isFavorite);
            }

            @Override
            public void onFailure(String msg) {

            }
        });
    }

    @Override
    public boolean checkDownloadedTrack(Track track) {
        return mTrackRepository.isDownloadedTrack(track);
    }

    @Override
    public void saveTrack(Track track) {
        mTrackRepository.saveTrackToDatabase(track);
    }

    @Override
    public void deleteTrackFromDatabase(final Track track) {
        mTrackRepository.deleteTrackFromDatabase(track, new TrackDataSource.OnLocalResponseListener<Track>() {
            @Override
            public void onSuccess(Track result) {
                mView.deleteTrackSuccess(result);
                mTrackRepository.deleteTrackFromStorage(track);
                if (mOnDeleteListener != null) {
                    mOnDeleteListener.onTrackDeleted();
                }
            }

            @Override
            public void onFailure(String msg) {
                mView.deleteTrackFailed(msg);
            }
        });
    }

    public BottomMenuDialogPresenter setTrack(Track track) {
        mTrack = track;
        return this;
    }

    public BottomMenuDialogPresenter setMenuType(@MenuType int type) {
        mMenuType = type;
        return this;
    }

    public BottomMenuDialogPresenter setOnDeleteListener(OnDeleteListener listener) {
        mOnDeleteListener = listener;
        return this;
    }

    private boolean checkFavoriteTrack(Track track) {
        return mTrackRepository.isFavoriteTrack(track);
    }
}
