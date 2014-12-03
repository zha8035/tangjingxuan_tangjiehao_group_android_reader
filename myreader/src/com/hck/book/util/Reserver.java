package com.hck.book.util;

import com.hck.book.ui.MyApplication;
import com.hck.date.FinalDate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class Reserver extends BroadcastReceiver{
	    @Override  
	    public void onReceive(Context context, Intent intent){
	        //接收安装广播 
	        if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {   
	            String packageName = intent.getDataString();   
	            Log.i("hck","安装了:" +packageName + "包名的程序");  
	            if (packageName.equals("com.snda.tts.service")) {
					FinalDate.isTrue=true;
				}
	        }   
	       
	    }
	
}
