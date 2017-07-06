package com.example.admin.lockscreen;


import android.util.Log;

public class LogUtil {
	private static boolean isShow=true;
	private static String Analytic="LockScreen";
	
	public static void i(String i){
		if(isShow){
			Log.i(Analytic, i);
			}
		}
	public static void v(String v){
		if(isShow){
			Log.v(Analytic, v);
			//Util.writeSuperposition(Contant.log_txt, Util.getTime(System.currentTimeMillis())+":"+v+"\n");
			}
		}
	public static void w(String w){
		if(isShow){
			Log.w(Analytic, w);
			}
		}
	public static void d(String d){
		if(isShow){
			Log.d(Analytic, d);
			}
		}
	public static void e(String e){
		if(isShow){
			Log.e(Analytic, e);
			}
		}
}
