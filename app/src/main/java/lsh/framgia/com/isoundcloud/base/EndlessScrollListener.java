package lsh.framgia.com.isoundcloud.base;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class EndlessScrollListener extends RecyclerView.OnScrollListener {

    private int mPreviousTotal = 0;
    private boolean mIsLoading = true;
    private int mVisibleThreshold = 0;

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        int visibleItemCount = recyclerView.getChildCount();
        int totalItemCount = recyclerView.getLayoutManager().getItemCount();
        int firstVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager())
                .findFirstVisibleItemPosition();
        if (mIsLoading) {
            if (totalItemCount > mPreviousTotal) {
                mIsLoading = false;
                mPreviousTotal = totalItemCount;
            }
        }
        if (!mIsLoading && (totalItemCount - visibleItemCount)
                <= (firstVisibleItem + mVisibleThreshold)) {
            onLoadMore();
            mIsLoading = true;
        }
    }

    public abstract void onLoadMore();
}
