package com.example.lenovo.thedictionaryofsanguo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//它的作用就在于减少不必要的调用findViewById，然后把对底下的控件引用存在ViewHolder里面，
// 再在View.setTag(holder)把它放在view里，下次就可以直接取了。
public class MyViewHolder extends RecyclerView.ViewHolder {

    private SparseArray<View> mViews;//存储list_Item的子View
    private View mConvertView;//存储list_Item;

    public MyViewHolder(Context context, View itemView, ViewGroup parent) {
        super(itemView);
        mConvertView=itemView;
        mViews=new SparseArray<View>();
    }

    public static MyViewHolder get(Context context, ViewGroup parent, int layoutId){
        View itemView = LayoutInflater.from(context).inflate(layoutId,parent,false);
        MyViewHolder holder = new MyViewHolder(context,itemView,parent);
        return holder;
    }

    public <T extends View> T getView(int viewId){
        View view = mViews.get(viewId);
        if(view == null){
            //创建view；
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId,view);
        }
        return (T) view;
    }

}