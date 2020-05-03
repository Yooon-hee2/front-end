package com.example.Capstone.db

import android.content.Context
import android.content.SharedPreferences

object SharedPreferenceController{
    private val NICKNAME = "nickname"
    private val USERID = "userid"
    private val NOTIFICATION = "notification"

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