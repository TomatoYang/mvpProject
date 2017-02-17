package com.yang.retrofit_ok_rx.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/8.
 */
public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseRecyclerViewHolder> {

    protected Context context;
    protected List<T> list;
    protected LayoutInflater inflater;

    public BaseRecyclerViewAdapter(Context context, List<T> list) {
        this.context = context;
        this.list = list != null ? list : new ArrayList<T>();
        inflater = LayoutInflater.from(context);
    }

    public List<T> getList() {
        return list;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseRecyclerViewHolder(
                inflater.inflate(getItemLayout(viewType), parent, false)
        );
    }

    protected abstract int getItemLayout(int viewType);

    @Override
    public void onBindViewHolder(final BaseRecyclerViewHolder holder, final int position) {
        bindData(holder, position);
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(holder,position);
                }
            });
        }
    }

    protected abstract void bindData(BaseRecyclerViewHolder holder, int position);

    public void refreshData(List<T> data) {
        list.clear();
        list.addAll(data);
        notifyItemRangeChanged(0, data.size());
    }

    public void loadMoreData(List<T> data) {
        int size = list.size();
        list.addAll(data);
        notifyItemRangeInserted(size - 1, data.size());
    }

    public interface OnItemClickListener{
        void onItemClick(BaseRecyclerViewHolder holder, int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
