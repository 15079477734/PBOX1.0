package com.library.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 14-4-29.
 */
public class DatabaseManage {

    DatabaseHelper helper = null;
    String TABLE_NAME = "user";

    public DatabaseManage(Context context) {
        helper = new DatabaseHelper(context);
        try {
            helper.createDataBase();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public DatabaseManage(Context context,String table_name) {
        helper = new DatabaseHelper(context);
        TABLE_NAME=table_name;
        try {
            helper.createDataBase();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public boolean addData(ContentValues values) {
        boolean flag = false;
        SQLiteDatabase database = null;
        try {
            long id = -1;
            database = helper.getWritableDatabase();
            //加入判断
            id = database.insert(TABLE_NAME, null, values);
            flag = (id != -1 ? true : false);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database != null)
                database.close();
        }

        return flag;
    }

    public boolean deleteData(String whereClause, String[] whereArgs) {
        boolean flag = false;
        SQLiteDatabase database = null;
        try {
            long id = 0;
            database = helper.getWritableDatabase();
            id = database.delete(TABLE_NAME, whereClause, whereArgs);
            flag = (id > 0 ? true : false);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database != null)
                database.close();

            return flag;
        }
    }

    public boolean updateData(ContentValues values, String whereClause, String[] whereArgs) {
        boolean flag = false;
        SQLiteDatabase database = null;
        try {
            long id = 0;
            database = helper.getWritableDatabase();
            id = database.update(TABLE_NAME, values, whereClause, whereArgs);
            flag = (id > 0);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database != null)
                database.close();
            Log.e("更新成功","good");
            return flag;
        }
    }

    public Map<String, String> findData(String selection, String[] selectionArgs) {    // select 返回的列的名称(投影查询) from
        SQLiteDatabase database = null;
        Cursor cursor = null;

        Map<String, String> map = new HashMap<String, String>();
        try {
            database = helper.getReadableDatabase();
            cursor = database.query(true, TABLE_NAME, null, selection,
                    selectionArgs, null, null, null, null);
            int cols_len = cursor.getColumnCount();
            while (cursor.moveToNext()) {
                for (int i = 0; i < cols_len; i++) {
                    String cols_name = cursor.getColumnName(i);
                    String cols_value = cursor.getString(cursor
                            .getColumnIndex(cols_name));
                    if (cols_value == null) {
                        cols_value = "";
                    }
                    map.put(cols_name, cols_value);
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }
        }
        return map;
    }

    public List<Map<String, String>> listData(String selection, String[] selectionArgs) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        SQLiteDatabase database = null;
        Cursor cursor = null;
        try {
            database = helper.getReadableDatabase();
            cursor = database.query(false, TABLE_NAME, null, selection,
                    selectionArgs, null, null, null, null);
            int cols_len = cursor.getColumnCount();
            while (cursor.moveToNext()) {
                HashMap<String, String> map = new HashMap<String, String>();
                for (int i = 0; i < cols_len; i++) {
                    String cols_name = cursor.getColumnName(i);
                    String cols_value = cursor.getString(cursor
                            .getColumnIndex(cols_name));
                    if (cols_value == null) {
                        cols_value = "";
                    }
                    map.put(cols_name, cols_value);
                }
                list.add(map);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }
        }
        Collections.reverse(list);
        return list;
    }
    public List<Map<String, String>> listData() {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        SQLiteDatabase database = null;
        Cursor cursor = null;
        try {
            database = helper.getReadableDatabase();
            cursor = database.query(false, TABLE_NAME, null, null,
                   null, null, null, null, null);
            int cols_len = cursor.getColumnCount();
            while (cursor.moveToNext()) {
                HashMap<String, String> map = new HashMap<String, String>();
                for (int i = 0; i < cols_len; i++) {
                    String cols_name = cursor.getColumnName(i);
                    String cols_value = cursor.getString(cursor
                            .getColumnIndex(cols_name));
                    if (cols_value == null) {
                        cols_value = "";
                    }
                    map.put(cols_name, cols_value);
                }
                list.add(map);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }
        }
        Collections.reverse(list);
        return list;
    }

    public Cursor getCursor() {
        SQLiteDatabase database = null;
        Cursor cursor = null;
        database = helper.getReadableDatabase();
        cursor = database.query(false, TABLE_NAME, null, null,
                null, null, null, null, null);
        return cursor;
    }

    public String getLastID() {
        int m = 0;
        SQLiteDatabase database = null;
        Cursor cursor = null;
        database = helper.getReadableDatabase();
        cursor = database.query(TABLE_NAME, null, null, null, null, null, null);
        while (cursor.moveToNext())
            m++;
        cursor.moveToLast();
        String id_value = cursor.getString(cursor
                .getColumnIndex("_id"));
        return id_value;

    }

    public String getLastData(String str, String selection, String[] selectionArgs) {

        SQLiteDatabase database = null;
        Map<String, String> map = new HashMap<String, String>();
        Cursor cursor = null;
        try {
            database = helper.getReadableDatabase();
            cursor = database.query(false, TABLE_NAME, null, selection,
                    selectionArgs, null, null, null, null);
            int cols_len = cursor.getColumnCount();
            int row_len = cursor.getCount();
            cursor.move(row_len);
            {
                for (int i = 0; i < cols_len; i++) {
                    String cols_name = cursor.getColumnName(i);
                    String cols_value = cursor.getString(cursor
                            .getColumnIndex(cols_name));
                    if (cols_value == null) {
                        cols_value = "";
                    }
                    map.put(cols_name, cols_value);
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }
        }
        return map.get(str);
    }

}



