package com.pqt.phamquangthanh.projecti.adapter;

import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pqt.phamquangthanh.projecti.R;
import com.pqt.phamquangthanh.projecti.model.TransactionGroup;
import com.pqt.phamquangthanh.projecti.util.GroupIconUtil;

import java.util.ArrayList;
import java.util.List;

public class TransactionGroupAdapter extends RecyclerView.Adapter<TransactionGroupAdapter.TransactionGroupViewHolder>{

    private Context mContext;
    private ArrayList<TransactionGroup> data;

    public TransactionGroupAdapter(Context mContext, ArrayList<TransactionGroup> data) {
        this.mContext = mContext;
        this.data = data;
    }

    public class TransactionGroupViewHolder extends RecyclerView.ViewHolder {
        ImageView iconGroup;
        TextView tvNameGroup;
        LinearLayout transactionGroupWrapper;
        public TransactionGroupViewHolder(@NonNull View itemView) {
            super(itemView);
            iconGroup = (ImageView) itemView.findViewById(R.id.iconGroup);
            tvNameGroup = (TextView) itemView.findViewById(R.id.nameGroup);
            transactionGroupWrapper = (LinearLayout) itemView.findViewById(R.id.transactionGroupWrapper);

        }
        void setTransactionGroup(TransactionGroup transactionGroup){
            tvNameGroup.setText(transactionGroup.getGroupName());
            iconGroup.setImageResource(transactionGroup.getIcon());
        }
    }
    @NonNull
    @Override
    public TransactionGroupAdapter.TransactionGroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(mContext).inflate(R.layout.row_transaction_group,parent,false);
        return new TransactionGroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionGroupAdapter.TransactionGroupViewHolder holder, int position) {
        final TransactionGroup transactionGroup = data.get(position);
        holder.setTransactionGroup(transactionGroup);
        holder.transactionGroupWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemCLickListener.onClick(transactionGroup);
//                    Toast.makeText(mContext,"OK",Toast.LENGTH_LONG).show();
            }
        });
    }
    public interface OnItemCLickListener{
        void onClick(TransactionGroup transactionGroup);
    }
    public OnItemCLickListener onItemCLickListener;

    public void setOnItemClickListener(OnItemCLickListener onItemCLickListener){
        this.onItemCLickListener = onItemCLickListener;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
