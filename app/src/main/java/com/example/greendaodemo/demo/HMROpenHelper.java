package com.example.greendaodemo.demo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.greendaodemo.MigrationHelper;
import com.example.greendaodemo.greendao.gen.DaoMaster;
import com.example.greendaodemo.greendao.gen.UserDao;

import org.greenrobot.greendao.database.Database;

public class HMROpenHelper extends DaoMaster.OpenHelper {
    public HMROpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    // 在HMROpenHelper类中重写onUpgrade()方法
    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        //DaoMaster.dropAllTables(db, true);
        //onCreate(db);
        // 目前用USER表来测试
        MigrationHelper.migrate(db, UserDao.class);
    }
}