package lsh.framgia.com.isoundcloud.base;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class EndlessScrollListener extends RecyclerView.OnScrollListener {

    private static final int VISIBLE_THRESHOLD = 0;

    private int mPreviousTotal = 0;
    private boolean mIsLoading = true;

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
                <= (firstVisibleItem + VISIBLE_THRESHOLD)) {
            onLoadMore();
            mIsLoading = true;
        }
    }

    public void resetListener() {
        mPreviousTotal = 0;
        mIsLoading = true;
    }

    public abstract void onLoadMore();
}
