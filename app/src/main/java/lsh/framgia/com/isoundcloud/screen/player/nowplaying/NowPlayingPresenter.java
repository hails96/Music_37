package lsh.framgia.com.isoundcloud.screen.player.nowplaying;

import java.util.List;

import lsh.framgia.com.isoundcloud.data.model.Track;

public class NowPlayingPresenter implements NowPlayingContract.Presenter {

    private NowPlayingContract.View mView;
    private List<Track> mPlaylist;
    private Track mCurrentTrack;

    @Override
    public void setView(NowPlayingContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        mView.showNowPlayingPlaylist(mPlaylist, mCurrentTrack);
    }

    public NowPlayingPresenter setPlaylist(List<Track> playlist) {
        mPlaylist = playlist;
        return this;
    }

    public NowPlayingPresenter setCurrentTrack(Track track) {
        mCurrentTrack = track;
        return this;
    }
}
