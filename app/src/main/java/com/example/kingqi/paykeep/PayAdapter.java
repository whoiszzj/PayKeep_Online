package com.example.kingqi.paykeep;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import static android.content.ContentValues.TAG;

public class PayAdapter extends RecyclerView.Adapter<PayAdapter.ViewHolder>{
    private List<Pay> payList;
    public PayAdapter(List<Pay> payList){
        this.payList = payList;
    }

    public interface OnItemOnClickListener{
        void onItemOnClick(View view,int pos);
        void onItemLongOnClick(View view ,int pos);
    }
    private OnItemOnClickListener mOnItemOnClickListener;
    public void setOnItemClickListener(OnItemOnClickListener listener){
        this.mOnItemOnClickListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView itemSpend,money,date;
        public ViewHolder(View itemView) {
            super(itemView);
            itemSpend = (TextView)itemView.findViewById(R.id.item_spend);
            money = (TextView)itemView.findViewById(R.id.money);
            date = (TextView)itemView.findViewById(R.id.date);
        }
    }

    @NonNull
    @Override
    public PayAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_pay,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder,final int position) {
        Pay pay = payList.get(position);
        TextView itemSpend = holder.itemSpend, money = holder.money, date = holder.date;
        itemSpend.setText(pay.getName());
        money.setText(String.valueOf(pay.getMoney()));
        String t = pay.getYear() + "/" + pay.getMonth() + "/" + pay.getDay();
        date.setText(t);
        if (mOnItemOnClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemOnClickListener.onItemOnClick(holder.itemView,holder.getLayoutPosition());
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mOnItemOnClickListener.onItemLongOnClick(holder.itemView,holder.getLayoutPosition());
                    return true;
                }
            });
        }
    }
    @Override
    public int getItemCount() {
        return payList.size();
    }
    public void removeItem(int pos){
        payList.remove(pos);
        notifyItemRemoved(pos);
    }
}
