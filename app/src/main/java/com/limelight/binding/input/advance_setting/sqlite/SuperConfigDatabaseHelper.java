package com.limelight.binding.input.advance_setting.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class SuperConfigDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "super_config.db";
    private static final int DATABASE_VERSION = 1;

    public SuperConfigDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建表格的SQL语句
        String createElementTable = "CREATE TABLE IF NOT EXISTS element (" +
                "_id INTEGER PRIMARY KEY, " +
                "element_id INTEGER," +
                "config_id INTEGER," +
                "element_type TEXT," +
                "element_value TEXT," +
                "element_middle_value TEXT," +
                "element_up_value TEXT," +
                "element_down_value TEXT," +
                "element_left_value TEXT," +
                "element_right_value TEXT," +
                "element_text TEXT," +
                "element_mode TEXT," +
                "element_width INTEGER," +
                "element_height INTEGER," +
                "element_area_width INTEGER," +
                "element_area_height INTEGER," +
                "element_sense INTEGER," +
                "element_x INTEGER," +
                "element_y INTEGER," +
                "element_radius INTEGER," +
                "element_shape TEXT," +
                "element_opacity INTEGER," +
                "element_layer INTEGER," +
                "element_background_color INTEGER," +
                "element_click_background_color INTEGER," +
                "element_border_color INTEGER," +
                "element_click_border_color INTEGER," +
                "element_text_color INTEGER," +
                "element_click_text_color INTEGER," +
                "element_create_time INTEGER," +
                "element_update_time INTEGER" +
                ")";

        // 执行SQL语句
        db.execSQL(createElementTable);

        String createConfigTable = "CREATE TABLE IF NOT EXISTS config (" +
                "_id INTEGER PRIMARY KEY, " +
                "config_id INTEGER," +
                "config_name TEXT," +
                "touch_enable TEXT," +
                "touch_mode TEXT," +
                "touch_sense INTEGER" +
                ")";

        db.execSQL(createConfigTable);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 升级数据库时执行的操作
    }

    public void insertElement(ContentValues values){
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert("element",null,values);
    }

    public void deleteElement(long elementId){
        SQLiteDatabase db = this.getWritableDatabase();

        // 定义 WHERE 子句
        String selection = "element_id = ?";
        // 定义 WHERE 子句中的参数
        String[] selectionArgs = { String.valueOf(elementId) };

        // 执行删除操作
        int deletedRows = db.delete("element", selection, selectionArgs);

    }

    public void updateElement(long elementId,ContentValues values){
        SQLiteDatabase db = this.getWritableDatabase();

        // SQL WHERE 子句
        String selection = "element_id = ?";
        // selectionArgs 数组提供了 WHERE 子句中占位符 ? 的实际值
        String[] selectionArgs = { String.valueOf(elementId) };

        int count = db.update(
                "config",   // 要更新的表
                values,    // 新值
                selection, // WHERE 子句
                selectionArgs // WHERE 子句中的占位符值
        );

    }

    public List<Long> queryAllElementIds(long configId){
        SQLiteDatabase db = this.getReadableDatabase();

        // 定义要查询的列
        String[] projection = { "element_id" };

        // 定义 WHERE 子句
        String selection = "config_id = ?";
        // 定义 WHERE 子句中的参数
        String[] selectionArgs = { String.valueOf(configId) };
        // 排序方式，增序
        String orderBy = "config_id ASC";

        // 执行查询
        Cursor cursor = db.query(
                "element",   // 表名
                projection, // 要查询的列
                selection,  // WHERE 子句
                selectionArgs, // WHERE 子句中的参数
                null, // 不分组
                null, // 不过滤
                orderBy  // 增序排序
        );

        List<Long> elementIds = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int elementIdIndex = cursor.getColumnIndexOrThrow("element_id");
                long elementId = cursor.getLong(elementIdIndex);
                elementIds.add(elementId);
            }
            cursor.close();
        }

        return elementIds;
    }
    public Object queryElementAttribute(long elementId,String elementAttribute){
        SQLiteDatabase db = this.getReadableDatabase();

        // 定义要查询的列
        String[] projection = { elementAttribute };

        // 定义 WHERE 子句
        String selection = "element_id = ?";
        // 定义 WHERE 子句中的参数
        String[] selectionArgs = { String.valueOf(elementId) };

        // 执行查询
        Cursor cursor = db.query(
                "element",   // 表名
                projection, // 要查询的列
                selection,  // WHERE 子句
                selectionArgs, // WHERE 子句中的参数
                null, // 不分组
                null, // 不过滤
                null  // 不排序
        );

        Object o = null;
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int columnIndex = cursor.getColumnIndexOrThrow(elementAttribute);
                switch (cursor.getType(columnIndex)) {
                    case Cursor.FIELD_TYPE_INTEGER:
                        o = cursor.getInt(columnIndex);
                        break;
                    case Cursor.FIELD_TYPE_FLOAT:
                        o = cursor.getFloat(columnIndex);
                        break;
                    case Cursor.FIELD_TYPE_STRING:
                        o = cursor.getString(columnIndex);
                        break;
                    case Cursor.FIELD_TYPE_BLOB:
                        o = cursor.getBlob(columnIndex);
                        break;
                    case Cursor.FIELD_TYPE_NULL:
                        o = null;
                        break;
                }

            }
            cursor.close();
        }
        db.close();
        return o;
    }

    public void insertConfig(ContentValues values){
        System.out.println("insertConfig values = " + values);
        SQLiteDatabase db = this.getWritableDatabase();
        long errorCode = db.insert("config",null,values);
        System.out.println("errorCode = " + errorCode);
        db.close();
    }

    public void deleteConfig(long configId){
        System.out.println("deleteConfig configId = " + configId);
        SQLiteDatabase db = this.getWritableDatabase();

        // 定义 WHERE 子句
        String selection = "config_id = ?";
        // 定义 WHERE 子句中的参数
        String[] selectionArgs = { String.valueOf(configId) };

        // 执行删除操作
        db.delete("config", selection, selectionArgs);

        //删除element表中所有的config_id的element
        db.delete("element", selection, selectionArgs);
        db.close();
    }

    public void updateConfig(long configId,ContentValues values){
        System.out.println("updateConfig configId = " + configId);
        System.out.println("updateConfig values = " + values);
        SQLiteDatabase db = this.getWritableDatabase();

        // SQL WHERE 子句
        String selection = "config_id = ?";
        // selectionArgs 数组提供了 WHERE 子句中占位符 ? 的实际值
        String[] selectionArgs = { String.valueOf(configId) };

        db.update(
                "config",   // 要更新的表
                values,    // 新值
                selection, // WHERE 子句
                selectionArgs // WHERE 子句中的占位符值
        );
        db.close();

    }

    public List<Long> queryAllConfigIds(){
        SQLiteDatabase db = this.getReadableDatabase();

        // 定义要查询的列
        String[] projection = { "config_id" };
        // 排序方式，增序
        String orderBy = "config_id ASC";
        // 执行查询
        Cursor cursor = db.query(
                "config",   // 表名
                projection, // 要查询的列
                null,  // WHERE 子句
                null, // WHERE 子句中的参数
                null, // 不分组
                null, // 不过滤
                orderBy  // 增序
        );

        List<Long> configIds = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int configIdIndex = cursor.getColumnIndexOrThrow("config_id");
                long configId = cursor.getLong(configIdIndex);
                configIds.add(configId);
            }
            cursor.close();
        }
        db.close();
        System.out.println("queryAllConfigIds configIds = " + configIds);
        return configIds;
    }

    public Object queryConfigAttribute(long configId,String configAttribute){
        SQLiteDatabase db = this.getReadableDatabase();

        // 定义要查询的列
        String[] projection = { configAttribute };

        // 定义 WHERE 子句
        String selection = "config_id = ?";
        // 定义 WHERE 子句中的参数
        String[] selectionArgs = { String.valueOf(configId) };

        // 执行查询
        Cursor cursor = db.query(
                "config",   // 表名
                projection, // 要查询的列
                selection,  // WHERE 子句
                selectionArgs, // WHERE 子句中的参数
                null, // 不分组
                null, // 不过滤
                null  // 不排序
        );

        Object o = null;
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int columnIndex = cursor.getColumnIndexOrThrow(configAttribute);
                switch (cursor.getType(columnIndex)) {
                    case Cursor.FIELD_TYPE_INTEGER:
                        o = cursor.getLong(columnIndex);
                        break;
                    case Cursor.FIELD_TYPE_FLOAT:
                        o = cursor.getFloat(columnIndex);
                        break;
                    case Cursor.FIELD_TYPE_STRING:
                        o = cursor.getString(columnIndex);
                        break;
                    case Cursor.FIELD_TYPE_BLOB:
                        o = cursor.getBlob(columnIndex);
                        break;
                    case Cursor.FIELD_TYPE_NULL:
                        o = null;
                        break;
                }

            }
            cursor.close();
        }
        db.close();
        System.out.println("queryConfigAttribute o = " + o);
        return o;
    }
}

