package com.example.personalworldtour.sql_lite

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler(context : Context) : SQLiteOpenHelper(context, DATABASE_NAME,null,DATABASE_VERSION){

    companion object {
        private const val DATABASE_VERSION = 3
        private const val DATABASE_NAME = "PWTDatabase"
        private const val TABLE_PROFILE_INFO = "UserInfo"
        private const val TABLE_POSTS = "PlacesToVisit"

        private const val KEY_ID = "_id"
        private const val KEY_FNAME = "fName"
        private const val KEY_LNAME = "lName"
        private const val KEY_USERNAME = "username"
        private const val KEY_PASS = "password"
        private const val KEY_EMAIL = "email"
        private const val KEY_USER_PICTURE = "profilePicture"

        private const val KEY_TITLE = "_title"
        private const val KEY_TEXT = "description"
        private const val KEY_LOCATION = "_location"
        private const val KEY_PICTURE = "picture"
        private const val KEY_NUMOFLIKES = "likesNum"
        private const val KEY_NUMOFDISLIKES = "dislikeNum"
        private const val KEY_DATE = "postDate"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_PROFILE_INFO_TABLE = ("CREATE TABLE " + TABLE_PROFILE_INFO + " ("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_FNAME + " TEXT,"
                + KEY_LNAME + " TEXT," + KEY_USERNAME + " TEXT," + KEY_PASS + " TEXT,"
                + KEY_EMAIL + " TEXT," + KEY_USER_PICTURE + " BLOB" + ")"
                )
        db?.execSQL(CREATE_PROFILE_INFO_TABLE)
        val CREATE_POSTS_TABLE = ("CREATE TABLE " + TABLE_POSTS + " ("
                + KEY_ID + " INTEGER," + KEY_TITLE + " TEXT,"
                + KEY_PICTURE + " BLOB," + KEY_LOCATION + " TEXT," + KEY_TEXT + " TEXT,"
                + KEY_NUMOFLIKES + " INTEGER," + KEY_NUMOFDISLIKES + " INTEGER," + KEY_DATE + " TEXT,"
                + "PRIMARY KEY ("+KEY_ID+", "+ KEY_TITLE +", "+ KEY_LOCATION +")" + ")"
                )
        db?.execSQL(CREATE_POSTS_TABLE)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILE_INFO)
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_POSTS)
        onCreate(db)
    }

    fun addUser(user : UserData) : Long{
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_FNAME, user.name)
        contentValues.put(KEY_LNAME, user.lname)
        contentValues.put(KEY_EMAIL, user.email)
        contentValues.put(KEY_USERNAME, user.uname)
        contentValues.put(KEY_PASS, user.pass)
        contentValues.put(KEY_USER_PICTURE, user.pic)

        val success = db.insert(TABLE_PROFILE_INFO, null, contentValues)
        db.close()
        return success

    }
    fun viewUser() : ArrayList<UserData>{
        val userList : ArrayList<UserData> = ArrayList<UserData>()
        val selectQuery = "SELECT * FROM " + TABLE_PROFILE_INFO

        val db = this.readableDatabase
        var cursor : Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery,null)
        }catch (e : SQLiteException){
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id : Int
        var fName : String
        var lName : String
        var uName : String
        var pass : String
        var email : String
        var upicture : Int

        if (cursor.moveToFirst()){
            do{
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                fName = cursor.getString(cursor.getColumnIndex(KEY_FNAME))
                lName = cursor.getString(cursor.getColumnIndex(KEY_LNAME))
                uName = cursor.getString(cursor.getColumnIndex(KEY_USERNAME))
                pass = cursor.getString(cursor.getColumnIndex(KEY_PASS))
                email = cursor.getString(cursor.getColumnIndex(KEY_EMAIL))
                upicture = cursor.getInt(cursor.getColumnIndex(KEY_USER_PICTURE))

                val user = UserData(id = id, name = fName, lname = lName, email = email, uname = uName, pass = pass, pic = upicture)
                userList.add(user)
            }while(cursor.moveToNext())
        }
        return userList
    }
    fun updateUser(user : UserData) : Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_FNAME, user.name)
        contentValues.put(KEY_LNAME, user.lname)
        contentValues.put(KEY_EMAIL, user.email)
        contentValues.put(KEY_USERNAME, user.uname)
        contentValues.put(KEY_PASS, user.pass)
        contentValues.put(KEY_USER_PICTURE, user.pic)

        val success = db.update(TABLE_PROFILE_INFO, contentValues, KEY_ID + "=" + user.id, null)
        db.close()
        return success
    }
    fun deleteUser(user : UserData) : Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, user.id)
        val success = db.delete(TABLE_PROFILE_INFO,KEY_ID + "=" + user.id, null)
        db.close()
        return success
    }
    fun findUser(u : UserData) : Boolean{
        val user : UserData = u
        val selectQuery =
            "SELECT $KEY_ID FROM $TABLE_PROFILE_INFO WHERE $KEY_USERNAME ='${user.uname}' AND $KEY_PASS ='${user.pass}'"
        val db = this.readableDatabase
        var cursor : Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery,null)
        }catch (e : SQLiteException){
            return true
        }
        return !cursor.moveToFirst()
    }
    fun findUser(uid : Int) : UserData{
        val userId  = uid
        var user : UserData = UserData(0,"","","","","")
        val selectQuery =
            "SELECT * FROM $TABLE_PROFILE_INFO WHERE $KEY_ID = $userId"
        val db = this.readableDatabase
        var cursor : Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery,null)
        }catch (e : SQLiteException){
            return user
        }
        var id : Int
        var fName : String
        var lName : String
        var uName : String
        var pass : String
        var email : String
        var upicture : Int
        if(cursor.moveToFirst()){
            id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
            fName = cursor.getString(cursor.getColumnIndex(KEY_FNAME))
            lName = cursor.getString(cursor.getColumnIndex(KEY_LNAME))
            uName = cursor.getString(cursor.getColumnIndex(KEY_USERNAME))
            pass = cursor.getString(cursor.getColumnIndex(KEY_PASS))
            email = cursor.getString(cursor.getColumnIndex(KEY_EMAIL))
            upicture = cursor.getInt(cursor.getColumnIndex(KEY_USER_PICTURE))
            user = UserData(id = id, name = fName, lname = lName, email = email, uname = uName, pass = pass, pic = upicture)
        }
        return user


    }
    fun findUserID(u : UserData) : Int{
        val user : UserData = u
        val selectQuery = "SELECT $KEY_ID FROM $TABLE_PROFILE_INFO WHERE $KEY_USERNAME = '${user.uname}' " +
                "AND $KEY_PASS = '${user.pass}' "
        val db = this.readableDatabase
        var cursor : Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery,null)
        }catch (e : SQLiteException){
            return -1
        }
        var id : Int = -2
        if(cursor.moveToFirst()){
            id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
        }
        return id
    }


//    working with articles
    fun addArticle(a : ArticleData) : Long{
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_ID,a.id)
        contentValues.put(KEY_TITLE, a.title)
        contentValues.put(KEY_PICTURE, a.image)
        contentValues.put(KEY_TEXT, a.text)
        contentValues.put(KEY_LOCATION, a.location)
        contentValues.put(KEY_NUMOFLIKES, 0)
        contentValues.put(KEY_NUMOFDISLIKES, 0)

        val success = db.insert(TABLE_POSTS, null, contentValues)
        db.close()
        return success
    }
    fun viewPersonalArticles(uid : Int) : ArrayList<ArticleData>{
        val articleList : ArrayList<ArticleData> = ArrayList<ArticleData>()
        val selectQuery = "SELECT * FROM $TABLE_POSTS WHERE $KEY_ID = $uid"
        val db = this.readableDatabase
        var cursor : Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery,null)
        }catch (e : SQLiteException){
            return ArrayList()
        }

        var id : Int
        var title : String
        var text : String
        var image : Int
        var location : String
        var likes : Int
        var dislikes : Int

        if (cursor.moveToFirst()){
            do{
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                title = cursor.getString(cursor.getColumnIndex(KEY_TITLE))
                text = cursor.getString(cursor.getColumnIndex(KEY_TEXT))
                image = cursor.getInt(cursor.getColumnIndex(KEY_PICTURE))
                location = cursor.getString(cursor.getColumnIndex(KEY_LOCATION))
                likes = cursor.getInt(cursor.getColumnIndex(KEY_NUMOFLIKES))
                dislikes = cursor.getInt(cursor.getColumnIndex(KEY_NUMOFDISLIKES))

                val article = ArticleData(id = id, title = title, text = text, image = image,location = location,likes = likes, dislikes = dislikes )
                articleList.add(article)
            }while(cursor.moveToNext())
        }
        return articleList
    }
    fun viewBestArticles() : ArrayList<ArticleData>{
        val articleList : ArrayList<ArticleData> = ArrayList<ArticleData>()
        val selectQuery = "SELECT * FROM $TABLE_POSTS ORDER BY $KEY_NUMOFLIKES DESC , $KEY_NUMOFDISLIKES ASC"
        val db = this.readableDatabase
        var cursor : Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery,null)
        }catch (e : SQLiteException){
            return ArrayList()
        }

        var id : Int
        var title : String
        var text : String
        var image : Int
        var location : String
        var likes : Int
        var dislikes : Int

        if (cursor.moveToFirst()){
            do{
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                title = cursor.getString(cursor.getColumnIndex(KEY_TITLE))
                text = cursor.getString(cursor.getColumnIndex(KEY_TEXT))
                image = cursor.getInt(cursor.getColumnIndex(KEY_PICTURE))
                location = cursor.getString(cursor.getColumnIndex(KEY_LOCATION))
                likes = cursor.getInt(cursor.getColumnIndex(KEY_NUMOFLIKES))
                dislikes = cursor.getInt(cursor.getColumnIndex(KEY_NUMOFDISLIKES))

                val article = ArticleData(id = id, title = title, text = text, image = image,location = location,likes = likes, dislikes = dislikes )
                articleList.add(article)
            }while(cursor.moveToNext())
        }
        return articleList
    }
    fun updateArticle(id : Int, likes : Int, dislikes : Int) : Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_NUMOFLIKES, likes)
        contentValues.put(KEY_NUMOFDISLIKES, dislikes)

        val success = db.update(TABLE_POSTS, contentValues, "$KEY_ID == $id", null)
        db.close()
        return success
    }
    fun deleteArticle(a : ArticleData) : Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, a.id)
        val success = db.delete(TABLE_PROFILE_INFO,KEY_ID + "=" +  a.id, null)
        db.close()
        return success
    }



}