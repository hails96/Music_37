package lsh.framgia.com.isoundcloud.data.source.remote;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import lsh.framgia.com.isoundcloud.R;
import lsh.framgia.com.isoundcloud.data.model.Track;
import lsh.framgia.com.isoundcloud.util.StringUtils;

public class TrackDownloadManager {

    private static TrackDownloadManager sInstance;

    private Context mContext;
    private DownloadManager mDownloadManager;
    private OnDownloadListener mOnDownloadListener;

    private TrackDownloadManager() {
    }

    private TrackDownloadManager(Context context, OnDownloadListener onDownloadListener) {
        mContext = context;
        mDownloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        mOnDownloadListener = onDownloadListener;
    }

    public static TrackDownloadManager getInstance(Context context, OnDownloadListener listener) {
        if (sInstance == null) {
            sInstance = new TrackDownloadManager(context, listener);
        }
        return sInstance;
    }

    public void downloadTrack(Track track) {
        if (track == null) {
            Toast.makeText(mContext, mContext.getString(R.string.error_download_track),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
                .mkdirs();

        DownloadManager.Request request = new DownloadManager.Request(
                Uri.parse(StringUtils.formatStreamUrl(track.getUri())));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE |
                DownloadManager.Request.NETWORK_WIFI);
        request.setTitle(track.getTitle());
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_MUSIC,
                StringUtils.formatTrackFile(track.getTitle()));
        long requestId = mDownloadManager.enqueue(request);
        track.setRequestId(requestId);
        if (mOnDownloadListener != null) {
            mOnDownloadListener.onDownload(track);
        }
        Toast.makeText(mContext, mContext.getString(R.string.msg_downloading_track, track.getTitle()),
                Toast.LENGTH_SHORT).show();
    }

    public interface OnDownloadListener {
        void onDownload(Track track);
    }
}
