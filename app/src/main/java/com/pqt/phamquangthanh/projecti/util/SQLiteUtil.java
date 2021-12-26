package com.pqt.phamquangthanh.projecti.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.pqt.phamquangthanh.projecti.model.Transaction;
import com.pqt.phamquangthanh.projecti.model.TransactionGroup;
import com.pqt.phamquangthanh.projecti.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class SQLiteUtil extends SQLiteOpenHelper {
    // Database
    public static String DATABASE_NAME = "QUAN_LY_CHI_TIEU_SINH_VIEN";
    public static int DATABASE_VERSION = 1;

    // Table
    public static final String TABLE_TRANSACTION = "TBL_TRANSACTION";
    public static final String TABLE_GROUP       = "TBL_GROUP";

    //Table transaction
    public static final String TRANSACTION_ID       = "ID";
    public static final String TRANSACTION_AMOUNT   = "AMOUNT";
    public static final String TRANSACTION_GROUP_ID = "GROUP_ID";
    public static final String TRANSACTION_DATE     = "DATE";
    public static final String TRANSACTION_CONTENT  = "CONTENT";
    //Table group

    public static final String GROUP_ID     = "ID";
    public static final String GROUP_NAME   = "NAME";
    public static final String GROUP_TYPE   = "TYPE";


    public static final String CREATE_TABLE_GROUP = "CREATE TABLE " + TABLE_GROUP +" ("
                                                    + GROUP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                                                    + GROUP_NAME + " TEXT, "
                                                    + GROUP_TYPE + " INTEGER)";
    public static final String CREATE_TABLE_TRANSACTION = "CREATE TABLE " + TABLE_TRANSACTION + " ("
                                                    + TRANSACTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                                                    + TRANSACTION_AMOUNT + " BIGINT, "
                                                    + TRANSACTION_GROUP_ID + " INTEGER, "
                                                    + TRANSACTION_DATE + " BIGINT, "
                                                    + TRANSACTION_CONTENT + " TEXT)";

    public SQLiteUtil(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //logger =LogUtil.getLogger("Data Manipulation Object");
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE_GROUP);
            db.execSQL(CREATE_TABLE_TRANSACTION);
            ArrayList<String> doInGoing = new ArrayList<>(Arrays.asList("Người thân cho","Làm thêm","Học bổng"));
            ArrayList<String> doOutGoing = new ArrayList<>(Arrays.asList("Thuê nhà","Ăn uống","Xăng xe","Bạn bè","Thời trang","Làm đẹp"));

            for(String groupIn : doInGoing){
                db.execSQL(insertIntoGroupQuery(groupIn,1));
            }
            for (String groupOut : doOutGoing){
                db.execSQL(insertIntoGroupQuery(groupOut,0));
            }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {
    }

    public String insertIntoGroupQuery( String group, int type){
        String insert_Group = "INSERT INTO " + TABLE_GROUP + " ("
                        + GROUP_NAME + ", "
                        + GROUP_TYPE + ") VALUES ("
                        + "'"+ group +"',"
                        + type +")";
        return insert_Group;
    }

    public void insertIntoTransaction(Transaction transaction){
        SQLiteDatabase database = this.getWritableDatabase();
        String note  = transaction.getNote();
        long date     = (long) transaction.getDate().getTime();
        int amount   = (int) transaction.getAmountOfMoney();
        int group_id = transaction.getTransactionGroup().getId();

        int type = transaction.getTransactionGroup().getTypeGroup();
        if(type ==0){
            amount = 0-amount;
        }

        String  insert1 = "INSERT INTO " + TABLE_TRANSACTION + " ("
                    + TRANSACTION_AMOUNT + ", "
                    + TRANSACTION_GROUP_ID + ", "
                    + TRANSACTION_DATE + ", "
                    + TRANSACTION_CONTENT + ") VALUES ("
                    + amount + ","
                    + group_id + ","
                    + date + ","
                    + "'" + note + "')";

        database.execSQL(insert1);
    }
    public void insertIntogroup(String group, int type){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(insertIntoGroupQuery(group,type));
    }

    @SuppressLint("Range")
    public ArrayList<Transaction> getTransactionInRange(Date FirstDate, Date LastDate) {
        SQLiteDatabase db = this.getReadableDatabase();
        long FirstDateTime = DateUtil.getDayTime(FirstDate);
        long LastDateTime = DateUtil.getDayTime(LastDate);
        String query_transaction = "SELECT * FROM " + TABLE_TRANSACTION + " WHERE " + TRANSACTION_DATE + " >= " + FirstDateTime + " AND " + TRANSACTION_DATE + " <=" + LastDateTime + " ORDER BY " + TRANSACTION_DATE + " DESC";
        //System.out.println(query);

        ArrayList<Transaction> lst = new ArrayList<>();

        Cursor c = db.rawQuery(query_transaction, null);

        if (c == null) {
            return lst;
        } else {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                int id = c.getInt(c.getColumnIndex(TRANSACTION_ID));
                long amount = c.getInt(c.getColumnIndex(TRANSACTION_AMOUNT));
                String note = c.getString(c.getColumnIndex(TRANSACTION_CONTENT));
                int groupId = c.getInt(c.getColumnIndex(TRANSACTION_GROUP_ID));
                Date date = ConversionUtil.timestampToDate(c.getLong(c.getColumnIndex(TRANSACTION_DATE)));
                lst.add(new Transaction(id, getGroupById(groupId), amount, note, date,0));
                c.moveToNext();
            }
            c.close();
            return lst;
        }
    }
    public long getTotalAmountInMonthFollowType(int type,int month,int year){
        String query_transaction;
        SQLiteDatabase db = this.getReadableDatabase();
        long startDayOfMonth = DateUtil.getStartDayOfMonth(month,year);
        long endDayOfMonth   = DateUtil.getEndDayOfMonth(month,year);
        long totalAmount =0;
        if(type == 0){
            query_transaction = "SELECT * FROM " + TABLE_TRANSACTION + " WHERE " + TRANSACTION_DATE + " >= " + startDayOfMonth + " AND " + TRANSACTION_DATE + " <=" + endDayOfMonth + " AND " + TRANSACTION_AMOUNT + "<=0";
        }else{
            query_transaction = "SELECT * FROM " + TABLE_TRANSACTION + " WHERE " + TRANSACTION_DATE + " >= " + startDayOfMonth + " AND " + TRANSACTION_DATE + " <=" + endDayOfMonth + " AND " + TRANSACTION_AMOUNT + ">=0";
        }


        Cursor c = db.rawQuery(query_transaction, null);
        if (c == null) {
            return 0;
        } else {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                long amount = c.getInt(c.getColumnIndex(TRANSACTION_AMOUNT));
                totalAmount += amount;
                c.moveToNext();
            }
            c.close();
        }

        return totalAmount;
    }

    public HashMap<Integer, Long> getTransactionInRangeFollowGroupId(Date FirstDate, Date LastDate,int type) {
        SQLiteDatabase db = this.getReadableDatabase();
        HashMap<Integer, Long> hashmap = new HashMap<Integer, Long> ();
        long FirstDateTime = DateUtil.getStartDayTime(FirstDate);
        long LastDateTime = DateUtil.getEndDayTime(LastDate);
        String query_transaction = "SELECT "+ TRANSACTION_GROUP_ID +" ,SUM("+TRANSACTION_AMOUNT+")" + "AS 'TotalAmount'FROM " + TABLE_TRANSACTION + " WHERE " + TRANSACTION_DATE + " >= " + FirstDateTime + " AND " + TRANSACTION_DATE + " <=" + LastDateTime + " GROUP BY " + TRANSACTION_GROUP_ID;

        Cursor c = db.rawQuery(query_transaction, null);

        if (c == null) {
            return hashmap;
        } else {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                int id = c.getInt(c.getColumnIndex(TRANSACTION_GROUP_ID));
                long totalAmount = c.getInt(c.getColumnIndex("TotalAmount"));
                if(type == 1) {
                    if (totalAmount >= 0) {
                        hashmap.put(id,totalAmount);
                    }
                }else{
                    if(totalAmount <0 ){
                        hashmap.put(id,totalAmount);
                    }
                }

                c.moveToNext();
            }
            c.close();
            return hashmap;
        }
    }


    @SuppressLint("Range")
    public Transaction getTransactionById(int id){
        Transaction transaction = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String select_Transaction = "SELECT * FROM " + TABLE_TRANSACTION +" WHERE " + TRANSACTION_ID + " = " + id;
        Cursor c =  db.rawQuery(select_Transaction,null);
        while(c.moveToNext()){
            long amount = c.getInt(c.getColumnIndex(TRANSACTION_AMOUNT));
            String note = c.getString(c.getColumnIndex(TRANSACTION_CONTENT));
            int groupId = c.getInt(c.getColumnIndex(TRANSACTION_GROUP_ID));
            Date date = ConversionUtil.timestampToDate(c.getLong(c.getColumnIndex(TRANSACTION_DATE)));
            transaction = new Transaction(id,getGroupById(groupId),amount,note,date,0);
            c.moveToNext();

        }
        return transaction;
    }
   public String getGroupNameById(int id){
        String name = "";
       SQLiteDatabase db = this.getReadableDatabase();
       String select_Transaction = "SELECT * FROM " + TABLE_GROUP +" WHERE " + GROUP_ID + " = " + id;
       Cursor c =  db.rawQuery(select_Transaction,null);
       while(c.moveToNext()){
           name = c.getString(c.getColumnIndex(GROUP_NAME));
       }
       return name;
   }

    @SuppressLint("Range")
    public ArrayList<TransactionGroup> getAllGroup(int type){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<TransactionGroup> data = new ArrayList<>();
        String select = "SELECT * FROM " + TABLE_GROUP +" WHERE " + GROUP_TYPE + " = " + type;
        Cursor c =  db.rawQuery(select,null);
        while(c.moveToNext()){
            String groupName = c.getString(c.getColumnIndex(GROUP_NAME));
            int id = c.getInt(c.getColumnIndex(GROUP_ID));
            int typeGroup = c.getInt(c.getColumnIndex(GROUP_TYPE));
            int icon = GroupIconUtil.getIcon(groupName);
            TransactionGroup transactionGroup = new TransactionGroup(id,groupName,icon,typeGroup);
            data.add(transactionGroup);
        }
        return data;
    }
    @SuppressLint("Range")
    public long getMoneyAmountInSpecificDay(Date date) {
        long dateInMilliseconds = DateUtil.getStartDayTime(date);
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_TRANSACTION + " WHERE " + TRANSACTION_DATE + " <= " + dateInMilliseconds;
        long result = 0;

        Cursor c = db.rawQuery(query, null);

        if (c == null) {
            return result;
        } else {
            c.moveToFirst();
            Transaction transaction = null;
            while (!c.isAfterLast()) {
                long amount = c.getInt(c.getColumnIndex(TRANSACTION_AMOUNT));
                result += amount;
                c.moveToNext();
            }
            c.close();

            return result;
        }
    }
    public long getMoneyInADay(Date date) {
        long start = DateUtil.getStartDayTime(date);
        long end = DateUtil.getEndDayTime(date);

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_TRANSACTION + " WHERE " + TRANSACTION_DATE + " >= " + start + " AND " + TRANSACTION_DATE + " < " + end + " ORDER BY " + TRANSACTION_DATE + " ASC";

        Cursor c = db.rawQuery(query, null);

        long result = 0;

        if (c == null) {
            return 0;
        } else {
            c.moveToFirst();
            while  (!c.isAfterLast()) {
                @SuppressLint("Range") float amount = c.getFloat(c.getColumnIndex(TRANSACTION_AMOUNT));
                result += ((long) amount);
                c.moveToNext();
            }
            c.close();
            return result;
        }
    }
    @SuppressLint("Range")
    public TransactionGroup getGroupById(int Id){
        TransactionGroup transactionGroup;
        SQLiteDatabase db = this.getReadableDatabase();
        String select = "SELECT * FROM " + TABLE_GROUP +" WHERE " + GROUP_ID + " = " + Id;
        Cursor c =  db.rawQuery(select,null);
        while(c.moveToNext()){
            String groupName = c.getString(c.getColumnIndex(GROUP_NAME));
             int id = c.getInt(c.getColumnIndex(GROUP_ID));
            int typeGroup = c.getInt(c.getColumnIndex(GROUP_TYPE));
            int icon = GroupIconUtil.getIcon(groupName);
            transactionGroup = new TransactionGroup(id,groupName,icon,typeGroup);
            return transactionGroup;

        }
        return new TransactionGroup(0,"",0,0);
    }


}
