package com.example.Capstone.db

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject


object SharedPreferenceController{
    private val NICKNAME = "nickname"
    private val USERID = "userid"
    private val NOTIFICATION = "notification"
    private val FOLDER = "folder"

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
        val defValue = Gson().toJson(HashMap<String, Int>())
        val typeToken = object: TypeToken<HashMap<String, Any>>(){}
        var obj: HashMap<String, Int> = HashMap()
        if (!TextUtils.isEmpty(json)) {
            obj = gson.fromJson<Any>(json, typeToken.type) as HashMap<String, Int>
        }
        return obj
    }
    //유저 아이디 저장, 받아오기
    fun setUserId(context:Context, userId: Int){
        val pref = context.getSharedPreferences(NICKNAME, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putInt(USERID, userId)
        editor.commit()
    }

    fun getUserId(context:Context): Int? {
        val pref = context.getSharedPreferences(NICKNAME, Context.MODE_PRIVATE)
        return pref.getInt(USERID, 1)
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

//    //이메일 저장, 받아오기
//    fun setUserEmail(context: Context, email: String){
//        val pref = context.getSharedPreferences(USER_EMAIL, Context.MODE_PRIVATE)
//        val editor = pref.edit()
//        editor.putString(USER_EMAIL, email)
//        editor.commit()
//    }
//
//    fun getUserEmail(context: Context): String? {
//        val pref = context.getSharedPreferences(USER_EMAIL, Context.MODE_PRIVATE)
//        return pref.getString(MYAUTH, "")
//    }


    //erase all when logout
    fun clearUserSharedPreferences(ctx: Context){
        val preference: SharedPreferences = ctx.getSharedPreferences(NICKNAME, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = preference.edit()
        editor.clear()
        editor.commit()
    }
}