package com.doordash.android.utils.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Ashish Thirunavalli on 01 July 2019.
 */
public interface IRecycleViewItemClickListener {
    void onItemClicked(RecyclerView recyclerView, View v, int position, int viewType);
}
