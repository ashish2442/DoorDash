package com.doordash.android.utils.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Ashish Thirunavalli on 01 July 2019.
 *
 * This call will take in recycler view and will provide click actions for that it.
 * This listens to child attach event to attach click actions for it.
 */
public class RecyclerViewClickEventHandler {
    private RecyclerView recyclerView;
    private Boolean isItemClickableSet = Boolean.FALSE;
    private IRecycleViewItemClickListener recycleViewItemClickListener;
    private IRecycleViewItemLongClickListener recycleViewItemLongClickListener;

    public RecyclerViewClickEventHandler(RecyclerView rv) {
        recyclerView = rv;
        recyclerView.addOnChildAttachStateChangeListener(getChildAttachStateChangeListener());
    }

    public RecyclerViewClickEventHandler(RecyclerView rv, Boolean isItemClickableSet) {
        recyclerView = rv;
        this.isItemClickableSet = isItemClickableSet;
        recyclerView.addOnChildAttachStateChangeListener(getChildAttachStateChangeListener());
    }

    /**
     * This method is used to set touch/click action on any Recycler view Items i.e rows
     *
     * @param listener Interface for RecycleView Item Click Listener
     */
    public void setOnItemClickListener(IRecycleViewItemClickListener listener) {
        recycleViewItemClickListener = listener;
    }

    /**
     * This method is used to set long press/click action on any Recycler view Items i.e rows
     *
     * @param listener Interface for RecycleView Item Click Listener
     */
    public void setOnItemLongClickListener(IRecycleViewItemLongClickListener listener) {
        recycleViewItemLongClickListener = listener;
    }

    private RecyclerView.OnChildAttachStateChangeListener getChildAttachStateChangeListener() {
        return new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {
                if (isItemClickableSet) {
                    if (recycleViewItemClickListener != null && view.isClickable()) {
                        view.setOnClickListener(getViewClickListener());
                    }
                    if (recycleViewItemLongClickListener != null && view.isClickable()) {
                        view.setOnLongClickListener(getViewLongClickListener());
                    }
                } else {
                    if (recycleViewItemClickListener != null) {
                        view.setOnClickListener(getViewClickListener());
                    }
                    if (recycleViewItemLongClickListener != null) {
                        view.setOnLongClickListener(getViewLongClickListener());
                    }
                }
            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {

            }
        };
    }

    private View.OnClickListener getViewClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recycleViewItemClickListener != null && getRVItemPosition(view) != -1) {
                    recycleViewItemClickListener.onItemClicked(recyclerView, view, getRVItemPosition(view), getRVItemViewType(view));
                }
            }
        };
    }

    private View.OnLongClickListener getViewLongClickListener() {
        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return recycleViewItemLongClickListener != null &&
                        getRVItemPosition(view) != -1 &&
                        recycleViewItemLongClickListener.onItemLongClicked(recyclerView, view, getRVItemPosition(view), getRVItemViewType(view));
            }
        };
    }

    private int getRVItemPosition(View view) {
        return recyclerView.getChildViewHolder(view).getAdapterPosition();
    }

    private int getRVItemViewType(View view) {
        return getRVItemPosition(view) == -1 ? 0 : recyclerView.getAdapter().getItemViewType(getRVItemPosition(view));
    }

}
