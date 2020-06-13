package com.example.Capstone.db

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


object SharedPreferenceController{
    private val NICKNAME = "nickname"
    private val USERCURRENTID = "usercurrentid"
    private val USERID = "userid"
    private val NOTIFICATION = "notification"
    private val FOLDER = "folder"
    private val STORAGE = "storage"
    private val LATITUDE = "latitude"
    private val LONGITUDE = "longitude"
    private val EMAIL = "email"
    private val TOKEN = "token"


    //닉네임 저장, 받아오기
    fun setUserNickname(context:Context, nickname: String){
        val pref = context.getSharedPreferences(NICKNAME, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString(NICKNAME, nickname)
        editor.commit()
    }

    fun getUserNickname(context:Context): String? {
        val pref = context.getSharedPreferences(NICKNAME, Context.MODE_PRIVATE)
        return pref.getString(NICKNAME, "")
    }

    //토큰 저장, 받아오기
    fun setUserToken(context:Context, token: String){
        val pref = context.getSharedPreferences(TOKEN, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString(TOKEN, token)
        editor.commit()
    }

    fun getUserToken(context:Context): String? {
        val pref = context.getSharedPreferences(TOKEN, Context.MODE_PRIVATE)
        return pref.getString(TOKEN, "")
    }

    //유저 현재 주인 아이디 저장, 받아오기
    fun setCurrentUserId(context:Context, userId: Int){
        val pref = context.getSharedPreferences(USERCURRENTID, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putInt(USERCURRENTID, userId)
        editor.commit()
    }

    fun getCurrentUserId(context:Context): Int? {
        val pref = context.getSharedPreferences(USERCURRENTID, Context.MODE_PRIVATE)
        return pref.getInt(USERCURRENTID, 1)
    }

    //유저 현재 주인 아이디 저장, 받아오기
    fun setUserId(context:Context, userId: Int){
        val pref = context.getSharedPreferences(USERID, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putInt(USERID, userId)
        editor.commit()
    }

    fun getUserId(context:Context): Int? {
        val pref = context.getSharedPreferences(USERID, Context.MODE_PRIVATE)
        return pref.getInt(USERID, 1)
    }

    //유저 아이디 저장, 받아오기
    fun setUserEmail(context:Context, userEmail: String){
        val pref = context.getSharedPreferences(EMAIL, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString(EMAIL, userEmail)
        editor.commit()
    }

    fun getUserEmail(context:Context): String? {
        val pref = context.getSharedPreferences(EMAIL, Context.MODE_PRIVATE)
        return pref.getString(EMAIL, "")
    }

    //폴더 정보 저장/ 받아오기
    fun setUserFolderInfo(context:Context, folder: HashMap<String, Int>){
        val gson = Gson()
        val pref = context.getSharedPreferences(FOLDER, Context.MODE_PRIVATE)
        val editor = pref.edit()
        val json = gson.toJson(folder)
        editor.putString(FOLDER, json)
        editor.commit()
    }

    fun getUserFolderInfo(context:Context): HashMap<String, Int> {
        val pref = context.getSharedPreferences(FOLDER, Context.MODE_PRIVATE)
        val gson = Gson()
        val json = pref.getString(FOLDER, "")
        val typeToken = object: TypeToken<HashMap<String, Int>>(){}
        var obj: HashMap<String, Int> = HashMap()
        if (!TextUtils.isEmpty(json)) {
            obj = gson.fromJson<Any>(json, typeToken.type) as HashMap<String, Int>
        }
        return obj
    }

    //저장소 정보 저장/ 받아오기
    fun setUserStorageInfo(context:Context, storage: HashMap<String, Int>){
        val gson = Gson()
        val pref = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE)
        val editor = pref.edit()
        val json = gson.toJson(storage)
        editor.putString(STORAGE, json)
        editor.commit()
    }

    fun getUserStorageInfo(context:Context): HashMap<String, Int> {
        val pref = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE)
        val gson = Gson()
        val json = pref.getString(STORAGE, "")
        val typeToken = object: TypeToken<HashMap<String, Int>>(){}
        var obj: HashMap<String, Int> = HashMap()
        if (!TextUtils.isEmpty(json)) {
            obj = gson.fromJson<Any>(json, typeToken.type) as HashMap<String, Int>
        }
        return obj
    }

    //유저 알람 on/off 받아오기
    fun setUserNoti(context:Context, noti: Boolean){
        val pref = context.getSharedPreferences(NICKNAME, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putBoolean(NOTIFICATION, noti)
        editor.commit()
    }

    fun getUserNoti(context:Context): Boolean? {
        val pref = context.getSharedPreferences(NICKNAME, Context.MODE_PRIVATE)
        return pref.getBoolean(NOTIFICATION, false)
    }

    //유저 위치 받아오기
    fun setUserLatitude(context:Context, latitude: Double){
        val pref = context.getSharedPreferences(LATITUDE, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putFloat(LATITUDE, latitude.toFloat())
        editor.commit()
    }

    fun getUserLatitude(context:Context): Double? {
        val pref = context.getSharedPreferences(LATITUDE, Context.MODE_PRIVATE)
        return pref.getFloat(LATITUDE, 1F).toDouble()
    }

    fun setUserLongitude(context:Context, longitude: Double){
        val pref = context.getSharedPreferences(LONGITUDE, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putFloat(LONGITUDE, longitude.toFloat())
        editor.commit()
    }

    fun getUserLongitude(context:Context): Double? {
        val pref = context.getSharedPreferences(LONGITUDE, Context.MODE_PRIVATE)
        return pref.getFloat(LONGITUDE, 1F).toDouble()
    }


    //erase all when logout
    fun clearUserSharedPreferences(ctx: Context){
        val preference: SharedPreferences = ctx.getSharedPreferences(NICKNAME, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = preference.edit()
        editor.clear()
        editor.commit()
    }
}