package com.limelight.binding.input.advance_setting.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SuperConfigDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "super_config.db";
    private static final int DATABASE_VERSION = 1;
    private SQLiteDatabase writableDataBase;
    private SQLiteDatabase readableDataBase;

    public SuperConfigDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        writableDataBase = getWritableDatabase();
        readableDataBase = getReadableDatabase();
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS element");
        // 创建表格的SQL语句
        String createElementTable = "CREATE TABLE IF NOT EXISTS element (" +
                "_id INTEGER PRIMARY KEY, " +
                "element_id INTEGER," +
                "config_id INTEGER," +
                "element_type INTEGER," +
                "element_value TEXT," +
                "element_middle_value TEXT," +
                "element_up_value TEXT," +
                "element_down_value TEXT," +
                "element_left_value TEXT," +
                "element_right_value TEXT," +
                "element_layer INTEGER," +
                "element_mode INTEGER," +
                "element_sense INTEGER," +
                "element_central_x INTEGER," +
                "element_central_y INTEGER," +
                "element_width INTEGER," +
                "element_height INTEGER," +
                "element_area_width INTEGER," +
                "element_area_height INTEGER," +
                "element_text TEXT," +
                "element_click_text TEXT," +
                "element_background_icon TEXT," +
                "element_click_background_icon TEXT," +
                "element_radius INTEGER," +
                "element_opacity INTEGER," +
                "element_thick INTEGER," +
                "element_background_color INTEGER," +
                "element_color INTEGER," +
                "element_pressed_color INTEGER," +
                "element_create_time INTEGER" +
                ")";

        // 执行SQL语句
        db.execSQL(createElementTable);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建表格的SQL语句
        String createElementTable = "CREATE TABLE IF NOT EXISTS element (" +
                "_id INTEGER PRIMARY KEY, " +
                "element_id INTEGER," +
                "config_id INTEGER," +
                "element_type INTEGER," +
                "element_value TEXT," +
                "element_middle_value TEXT," +
                "element_up_value TEXT," +
                "element_down_value TEXT," +
                "element_left_value TEXT," +
                "element_right_value TEXT," +
                "element_layer INTEGER," +
                "element_mode INTEGER," +
                "element_sense INTEGER," +
                "element_central_x INTEGER," +
                "element_central_y INTEGER," +
                "element_width INTEGER," +
                "element_height INTEGER," +
                "element_area_width INTEGER," +
                "element_area_height INTEGER," +
                "element_text TEXT," +
                "element_click_text TEXT," +
                "element_background_icon TEXT," +
                "element_click_background_icon TEXT," +
                "element_radius INTEGER," +
                "element_opacity INTEGER," +
                "element_thick INTEGER," +
                "element_background_color INTEGER," +
                "element_color INTEGER," +
                "element_pressed_color INTEGER," +
                "element_create_time INTEGER" +
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
    public void deleteTable(String tableName){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 升级数据库时执行的操作
    }

    public void insertElement(ContentValues values){
        writableDataBase.insert("element",null,values);
    }

    public void deleteElement(long elementId){

        // 定义 WHERE 子句
        String selection = "element_id = ?";
        // 定义 WHERE 子句中的参数
        String[] selectionArgs = { String.valueOf(elementId) };

        // 执行删除操作
        writableDataBase.delete("element", selection, selectionArgs);
    }

    public void updateElement(long elementId,ContentValues values){

        // SQL WHERE 子句
        String selection = "element_id = ?";
        // selectionArgs 数组提供了 WHERE 子句中占位符 ? 的实际值
        String[] selectionArgs = { String.valueOf(elementId) };

        writableDataBase.update(
                "element",   // 要更新的表
                values,    // 新值
                selection, // WHERE 子句
                selectionArgs // WHERE 子句中的占位符值
        );
    }

    public List<Long> queryAllElementIds(long configId){

        // 定义要查询的列
        String[] projection = { "element_id", "element_layer" };

        // 定义 WHERE 子句
        String selection = "config_id = ?";
        // 定义 WHERE 子句中的参数
        String[] selectionArgs = { String.valueOf(configId) };
        // 排序方式，增序
        String orderBy = "element_id + (element_layer * 281474976710656) ASC";

        // 执行查询
        Cursor cursor = readableDataBase.query(
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
        System.out.println("elementIds = " + elementIds);
        return elementIds;
    }
    public Object queryElementAttribute(long elementId,String elementAttribute){

        // 定义要查询的列
        String[] projection = { elementAttribute };

        // 定义 WHERE 子句
        String selection = "element_id = ?";
        // 定义 WHERE 子句中的参数
        String[] selectionArgs = { String.valueOf(elementId) };

        // 执行查询
        Cursor cursor = readableDataBase.query(
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
        
        return o;
    }

    public Map<String, Object> queryAllElementAttributes(long elementId){
        Map<String, Object> resultMap = new HashMap<>();
        // 定义 WHERE 子句
        String selection = "element_id = ?";
        // 定义 WHERE 子句中的参数
        String[] selectionArgs = { String.valueOf(elementId) };

        // 执行查询
        Cursor cursor = readableDataBase.query(
                "element",   // 表名
                null, // 要查询的列
                selection,  // WHERE 子句
                selectionArgs, // WHERE 子句中的参数
                null, // 不分组
                null, // 不过滤
                null  // 不排序
        );
        if (cursor.moveToFirst()) {
            int columnCount = cursor.getColumnCount();
            for (int i = 0; i < columnCount; i++) {
                String columnName = cursor.getColumnName(i);
                int columnType = cursor.getType(i);

                switch (columnType) {
                    case Cursor.FIELD_TYPE_INTEGER:
                        resultMap.put(columnName, cursor.getLong(i));
                        break;
                    case Cursor.FIELD_TYPE_STRING:
                        resultMap.put(columnName, cursor.getString(i));
                        break;
                    case Cursor.FIELD_TYPE_FLOAT:
                        resultMap.put(columnName, cursor.getFloat(i));
                        break;
                    case Cursor.FIELD_TYPE_BLOB:
                        resultMap.put(columnName, cursor.getBlob(i));
                        break;
                    case Cursor.FIELD_TYPE_NULL:
                        resultMap.put(columnName, null);
                        break;
                }
            }
        }
        cursor.close();
        return resultMap;
    }

    public void insertConfig(ContentValues values){

        writableDataBase.insert("config",null,values);
        
    }

    public void deleteConfig(long configId){

        // 定义 WHERE 子句
        String selection = "config_id = ?";
        // 定义 WHERE 子句中的参数
        String[] selectionArgs = { String.valueOf(configId) };

        // 执行删除操作
        writableDataBase.delete("config", selection, selectionArgs);

        //删除element表中所有的config_id的element
        writableDataBase.delete("element", selection, selectionArgs);
        
    }

    public void updateConfig(long configId,ContentValues values){

        // SQL WHERE 子句
        String selection = "config_id = ?";
        // selectionArgs 数组提供了 WHERE 子句中占位符 ? 的实际值
        String[] selectionArgs = { String.valueOf(configId) };

        writableDataBase.update(
                "config",   // 要更新的表
                values,    // 新值
                selection, // WHERE 子句
                selectionArgs // WHERE 子句中的占位符值
        );
        

    }

    public List<Long> queryAllConfigIds(){

        // 定义要查询的列
        String[] projection = { "config_id" };
        // 排序方式，增序
        String orderBy = "config_id ASC";
        // 执行查询
        Cursor cursor = readableDataBase.query(
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
        
        System.out.println("configIds = " + configIds);
        return configIds;
    }

    public Object queryConfigAttribute(long configId,String configAttribute){

        // 定义要查询的列
        String[] projection = { configAttribute };

        // 定义 WHERE 子句
        String selection = "config_id = ?";
        // 定义 WHERE 子句中的参数
        String[] selectionArgs = { String.valueOf(configId) };

        // 执行查询
        Cursor cursor = readableDataBase.query(
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
        
        return o;
    }
}

