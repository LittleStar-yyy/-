package com.example.lenovo.thedictionaryofsanguo;

/**
 * Created by lenovo on 2017/11/9.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

public abstract class CommonAdapter<T>extends RecyclerView.Adapter<MyViewHolder>{

    private Context mContext;
    private List<T> mDatas;
    private int mLayoutId;
    private OnItemClickListener mOnItemClickListener=null;

    public CommonAdapter(Context context,int layoutId,List<T>datas){
        mContext = context;
        mLayoutId=layoutId;
        mDatas = datas;
    }

    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType){
        MyViewHolder viewHolder = MyViewHolder.get(mContext,parent,mLayoutId);
        return viewHolder;
    }

    public void onBindViewHolder(final MyViewHolder holder, int position){
        //设置holder内view的内容
        convert(holder,mDatas.get(position));
        if(mOnItemClickListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    mOnItemClickListener.onClick(holder.getAdapterPosition());
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v){
                    mOnItemClickListener.onLongClick(holder.getAdapterPosition());
                    return false;
                }
            });
        }
    }


    public abstract void convert(MyViewHolder holder, T t);

    @Override
    public int getItemCount(){
        Log.d("aaa","hjghgsd: "+mDatas.size());
        return mDatas.size();
    }

    public void removeItem(int position){
        mDatas.remove(position);
        notifyItemRemoved(position);
    }

    public interface OnItemClickListener{
        void onClick(int position);
        void onLongClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mOnItemClickListener=onItemClickListener;
    }
}
