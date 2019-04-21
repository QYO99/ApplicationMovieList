package com.example.listmovieapps.handler

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

class DataBaseHandler{
    private val dbName = "MyDb"
    private val dbTable = "Movie"
    private val colId = "id_movie"
    private val colTitle = "original_title"
    private val colVote = "vote_average"
    private val colPopularity = "popularity"
    private val colLanguage = "original_language"
    private val colOverview = "overview"
    private val colPoster = "poster_path"
    private val colBackdrop = "backdrop_path"
    private val colDate = "release_date"

    private val dbVersion = 1

    private val CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS " + dbTable + " (" + colId + " INTEGER PRIMARY KEY," +
            colTitle + " VARCHAR(256), "+ colVote + " VARCHAR(256), "+ colPopularity + " VARCHAR(256), "+ colLanguage + " VARCHAR(256), "+
            colOverview + " VARCHAR(256), "+colPoster + " VARCHAR(256), "+ colBackdrop + " VARCHAR(256), "+colDate + " VARCHAR(256)" +
            ");"

    private var sqlDb: SQLiteDatabase? = null

    constructor(context: Context){
        var db = DatabaseHelper(context)
        sqlDb = db.writableDatabase
    }

    fun insert(values: ContentValues): Long {
        val ID = sqlDb!!.insert(dbTable, "", values)
        return ID
    }

    fun selectData(): Cursor {
        return sqlDb!!.rawQuery("select * from " + dbTable+"" , null)
    }

    fun getId(idMovie:String): Cursor {
        return sqlDb!!.rawQuery("Select COUNT(*) AS much from " + dbTable+" where "+colId+" = '"+idMovie+"'" , null)
    }

    fun searchData(name:String): Cursor {
        return sqlDb!!.rawQuery("select * from " + dbTable+" Where "+colTitle+" LIKE '%"+name+"%' ", null)
    }

    fun delete(selection: String, selectionArgs: Array<String>): Int {
        val count = sqlDb!!.delete(dbTable, selection, selectionArgs)
        return count
    }

    fun update(values: ContentValues, selection: String, selectionargs: Array<String>): Int {

        val count = sqlDb!!.update(dbTable, values, selection, selectionargs)
        return count
    }

    inner class DatabaseHelper : SQLiteOpenHelper {

        var context: Context? = null

        constructor(context: Context) : super(context, dbName, null, dbVersion) {
            this.context = context
        }

        override fun onCreate(db: SQLiteDatabase?) {
            db!!.execSQL(CREATE_TABLE_SQL)
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db!!.execSQL("Drop table IF EXISTS " + dbTable)
        }
    }

}