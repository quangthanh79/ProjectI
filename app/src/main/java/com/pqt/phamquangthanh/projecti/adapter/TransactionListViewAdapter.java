package com.pqt.phamquangthanh.projecti.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.pqt.phamquangthanh.projecti.R;
import com.pqt.phamquangthanh.projecti.model.Transaction;
import com.pqt.phamquangthanh.projecti.model.TransactionDate;
import com.pqt.phamquangthanh.projecti.model.TransactionGroup;
import com.pqt.phamquangthanh.projecti.model.TransactionStatistic;
import com.pqt.phamquangthanh.projecti.util.ConversionUtil;
import com.pqt.phamquangthanh.projecti.util.DateUtil;
import com.pqt.phamquangthanh.projecti.util.SQLiteUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class TransactionListViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static int TYPE_STATISTIC = 1;
    private static int TYPE_HEADER = 2;
    private static int TYPE_ITEM = 3;
    private Context mContext;
    private ArrayList<Object> data;
    private OnTransactionItemClickListener onTransactionItemClickListener;
    private OnTransactionHeaderClickListener onTransactionHeaderClickListener;
    private OnTransactionStaticClickListener onTransactionStaticClickListener;
    public TransactionListViewAdapter(Context mContext, ArrayList<Object> data) {
        this.mContext = mContext;
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType == TYPE_STATISTIC){
            view = LayoutInflater.from(mContext).inflate(R.layout.row_statistic, parent, false);
            return new StatisticViewHolder(view);
        }
        else if(viewType == TYPE_HEADER){
            view = LayoutInflater.from(mContext).inflate(R.layout.row_trans_header,parent,false);
            return new HeaderViewHolder(view);
        }
        else{
            view = LayoutInflater.from(mContext).inflate(R.layout.row_trans_item1,parent,false);
            return new ItemViewHolder(view);
        }

    }

    public class StatisticViewHolder extends RecyclerView.ViewHolder{
        TextView tv_begin_money_value,tv_end_money_value,tv_remain_money,row_transaction_report;


        public StatisticViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_begin_money_value   = (TextView) itemView.findViewById(R.id.tv_begin_money_value);
            tv_end_money_value     = (TextView) itemView.findViewById(R.id.tv_end_money_value);
            tv_remain_money        = (TextView) itemView.findViewById(R.id.tv_remain_money);
            row_transaction_report = (TextView) itemView.findViewById(R.id.row_transaction_report);
        }
        void setStatisticView(TransactionStatistic transactionStatistic){
            tv_begin_money_value.setText(ConversionUtil.longToString(transactionStatistic.getBeginMoneyAmount()));
            tv_end_money_value.setText(ConversionUtil.longToString(transactionStatistic.getEndMoneyAmount()));
            long remain_money = transactionStatistic.getEndMoneyAmount() - transactionStatistic.getBeginMoneyAmount();
            tv_remain_money.setText(ConversionUtil.longToString(remain_money));
        }
    }
    public class HeaderViewHolder extends RecyclerView.ViewHolder{
        TextView row_trans_header_day_of_month,row_trans_header_day_of_week,row_trans_header_month,row_trans_header_amount;
        ImageView row_trans_header_drop_down;
        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            row_trans_header_day_of_month = (TextView) itemView.findViewById(R.id.row_trans_header_day_of_month);
            row_trans_header_day_of_week  = (TextView) itemView.findViewById(R.id.row_trans_header_day_of_week);
            row_trans_header_month        = (TextView) itemView.findViewById(R.id.row_trans_header_month);
            row_trans_header_amount       = (TextView) itemView.findViewById(R.id.row_trans_header_amount);
            row_trans_header_drop_down    = (ImageView) itemView.findViewById(R.id.row_trans_header_drop_down);
        }
        void setHeaderView(TransactionDate transactionDate){
            Calendar calendar_now = Calendar.getInstance();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(transactionDate.getDate());
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            int month      = calendar.get(Calendar.MONTH)+1;
            int year       = calendar.get(Calendar.YEAR);
            long amount    = transactionDate.getAmountOfMoney();
            DecimalFormat decimalFormat = new DecimalFormat("##,###,###");
            if(amount>=0) {
                row_trans_header_amount.setText("+"+decimalFormat.format(amount));
            }
            else{
                row_trans_header_amount.setText(decimalFormat.format(amount));
            }
            row_trans_header_day_of_month.setText(dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth + "");
            row_trans_header_day_of_week.setText(DateUtil.getDayOfWeek(transactionDate.getDate()));
            row_trans_header_month.setText("tháng "+ month +" "+year);

        }
    }
    public class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView row_trans_item_group_name,row_trans_item_note;
        TextView row_trans_item_amount;
        ImageView transaction_group_icon;
        RelativeLayout relativeLayout_Item;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            row_trans_item_group_name = (TextView) itemView.findViewById(R.id.row_trans_item_group_name);
            row_trans_item_note       = (TextView) itemView.findViewById(R.id.row_trans_item_note);
            row_trans_item_amount     = (TextView) itemView.findViewById(R.id.row_trans_item_amount);
            transaction_group_icon    = (ImageView) itemView.findViewById(R.id.transaction_group_icon);
            relativeLayout_Item       = (RelativeLayout) itemView.findViewById(R.id.relativeLayout_Item);

        }

        void setItemView(Transaction transaction){
            final Transaction transaction1 = transaction;
            TransactionGroup transactionGroup = transaction.getTransactionGroup();
            Long val = transaction.getAmountOfMoney();
            DecimalFormat decimalFormat = new DecimalFormat("##,###,###");


            if(val>=0) {
                row_trans_item_amount.setTextColor(ContextCompat.getColor(mContext, R.color.colorIn));
            }else
                row_trans_item_amount.setTextColor(ContextCompat.getColor(mContext, R.color.colorOut));
            row_trans_item_group_name.setText(transactionGroup.getGroupName());
            row_trans_item_note.setText(transaction.getNote());
            row_trans_item_amount.setText(decimalFormat.format(val));
            transaction_group_icon.setImageResource(transactionGroup.getIcon());
            relativeLayout_Item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onTransactionItemClickListener.onTransactionItemClick(transaction1.getId());
                }
            });
        }
        void setShow(){
            relativeLayout_Item.setVisibility(View.VISIBLE);
            relativeLayout_Item.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        }
        void setHide(){

            relativeLayout_Item.setVisibility(View.GONE);
            relativeLayout_Item.setLayoutParams(new RecyclerView.LayoutParams(0, 0));

        }
    }


    @Override
    public int getItemViewType(int position) {
        Object obj = data.get(position);
        if(obj instanceof TransactionStatistic){
            return TYPE_STATISTIC;
        }else if(obj instanceof Transaction){
            return TYPE_ITEM;
        }
        else
            return TYPE_HEADER;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final RecyclerView.ViewHolder holder1 = holder;
        Object obj = data.get(position);
        if(obj instanceof TransactionStatistic){
            ((StatisticViewHolder) holder).setStatisticView((TransactionStatistic) obj);
            ((StatisticViewHolder) holder).row_transaction_report.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onTransactionStaticClickListener.OnTransactionStaticClick();
                }
            });
        }else if(obj instanceof Transaction){
            if(((Transaction) data.get(holder.getAdapterPosition())).getIsShow() == 1){
                ((ItemViewHolder) holder).setShow();
            }
            else{
                ((ItemViewHolder) holder).setHide();
            }
            ((ItemViewHolder) holder).setItemView((Transaction) obj);

        }
        else {
            ((HeaderViewHolder) holder).setHeaderView((TransactionDate) obj);
            ((HeaderViewHolder) holder).row_trans_header_drop_down.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onTransactionHeaderClickListener.onTransactionHeaderClick(holder1.getAdapterPosition());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface OnTransactionItemClickListener {
        void onTransactionItemClick(int transactionId);
    }
    public interface OnTransactionStaticClickListener {
        void OnTransactionStaticClick();
    }
    public interface OnTransactionHeaderClickListener {
        void onTransactionHeaderClick(int pot);
    }

    public void setOnTransactionHeaderClickListener(OnTransactionHeaderClickListener onTransactionHeaderClickListener) {
        this.onTransactionHeaderClickListener = onTransactionHeaderClickListener;
    }

    public void setOnTransactionStaticClickListener(OnTransactionStaticClickListener onTransactionStaticClickListener) {
        this.onTransactionStaticClickListener = onTransactionStaticClickListener;
    }

    public void setOnTransactionItemClickListener(OnTransactionItemClickListener onTransactionItemClickListener) {
        this.onTransactionItemClickListener = onTransactionItemClickListener;
    }
}
