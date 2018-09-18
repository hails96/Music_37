package lsh.framgia.com.isoundcloud.screen.main.search;

import lsh.framgia.com.isoundcloud.R;
import lsh.framgia.com.isoundcloud.base.mvp.BaseFragment;

public class SearchFragment extends BaseFragment<SearchContract.Presenter>
        implements SearchContract.View {

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search;
    }

    @Override
    protected void initLayout() {

    }
}
