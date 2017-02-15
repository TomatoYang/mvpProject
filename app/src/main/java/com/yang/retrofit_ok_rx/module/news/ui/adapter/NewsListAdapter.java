package com.yang.retrofit_ok_rx.module.news.ui.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yang.retrofit_ok_rx.R;
import com.yang.retrofit_ok_rx.base.BaseRecyclerViewAdapter;
import com.yang.retrofit_ok_rx.base.BaseRecyclerViewHolder;
import com.yang.retrofit_ok_rx.bean.NeteastNewsSummary;

import java.util.List;

/**
 * Created by Administrator on 2017/2/8.
 */
public class NewsListAdapter extends BaseRecyclerViewAdapter<NeteastNewsSummary> {

    public NewsListAdapter(Context context, List<NeteastNewsSummary> list) {
        super(context, list);
    }

    @Override
    protected int getItemLayout(int viewType) {
        return R.layout.item_news_list;
    }

    @Override
    protected void bindData(BaseRecyclerViewHolder holder, int position) {
        NeteastNewsSummary summary = list.get(position);

        ImageView iv = holder.getViewById(R.id.iv_news_photo);
        TextView title = holder.getViewById(R.id.tv_news_title);
        TextView digest = holder.getViewById(R.id.tv_news_digest);
        TextView time = holder.getViewById(R.id.tv_news_ptime);

        Glide.with(context)
                .load(summary.imgsrc)
                .placeholder(R.drawable.ic_loading)
                .error(R.drawable.ic_fail)
                .into(iv);
        title.setText(summary.title);
        digest.setText(summary.digest);
        time.setText(summary.ptime);
    }
}
