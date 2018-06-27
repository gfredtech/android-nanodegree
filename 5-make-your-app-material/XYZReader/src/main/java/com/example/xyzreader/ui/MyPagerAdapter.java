package com.example.xyzreader.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.example.xyzreader.data.ArticleLoader;

class MyPagerAdapter extends FragmentStatePagerAdapter {
    private ArticleDetailActivity articleDetailActivity;

    public MyPagerAdapter(ArticleDetailActivity articleDetailActivity, FragmentManager fm) {
        super(fm);
        this.articleDetailActivity = articleDetailActivity;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        ArticleDetailFragment fragment = (ArticleDetailFragment) object;
        if (fragment != null) {
            articleDetailActivity.mSelectedItemUpButtonFloor = fragment.getUpButtonFloor();
            articleDetailActivity.updateUpButtonPosition();
        }
    }

    @Override
    public Fragment getItem(int position) {
        articleDetailActivity.mCursor.moveToPosition(position);
        return ArticleDetailFragment.newInstance(articleDetailActivity.mCursor.getLong(ArticleLoader.Query._ID));
    }

    @Override
    public int getCount() {
        return (articleDetailActivity.mCursor != null) ? articleDetailActivity.mCursor.getCount() : 0;
    }
}
