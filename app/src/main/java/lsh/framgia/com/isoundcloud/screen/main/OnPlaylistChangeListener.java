package lsh.framgia.com.isoundcloud.screen.main;

import java.util.List;

import lsh.framgia.com.isoundcloud.data.model.Track;

public interface OnPlaylistChangeListener {
    void onPlaylistChange(List<Track> playlist);
}
