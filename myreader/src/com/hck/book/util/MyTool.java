package com.hck.book.util;

import android.app.Activity;
import android.content.Context;
import android.view.Display;

public class MyTool {
	public static Context context;
	private static Display display;

	public static int getWidth() {
		if (context==null) {
			return 480;
		}
		try {
			display = ((Activity) context).getWindowManager().getDefaultDisplay();
			return display.getWidth();
		} catch (Exception e) {
			return 480;
		}
		
	}

	public static int getHight() {
		if (context==null) {
			return 850;
		}
		try {
			display = ((Activity) context).getWindowManager().getDefaultDisplay();
			return display.getHeight();
		} catch (Exception e) {
			return 850;
		}
		
	}

}
