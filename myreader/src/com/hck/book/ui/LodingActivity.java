package com.hck.book.ui;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.hck.book.uti.JMPManager;
import com.hck.book.util.Reserver;
import com.hck.date.FinalDate;
import com.hck.test.R;

public class LodingActivity extends Activity {

	private ImageView imageView;
	private Animation animation;
	private boolean isLogin;
	private SharedPreferences sp;
	private Reserver reserver;
	private Editor editor;
	private List<PackageInfo> packageInfos;
	private boolean isOK;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FinalDate.context=this;
		sp = getSharedPreferences("book", Context.MODE_PRIVATE);
		if (sp.getLong("time", 0) == 0) {
			editor = sp.edit();
			editor.putLong("time", System.currentTimeMillis());
			editor.commit();
			FinalDate.tme=System.currentTimeMillis();
		}
		else {
			FinalDate.tme=sp.getLong("time", 0);
		}
		if (sp.getBoolean("isFirst", true)) {
			FinalDate.isFirst=true;
			editor = sp.edit();
			editor.putBoolean("isFirst", false);
			editor.commit();
		}
		else {
			FinalDate.isFirst=false;
		}
		JMPManager manager = new JMPManager(); 
		manager.startService(this,1);
	//	getInfo();
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.loding);
		imageView = (ImageView) findViewById(R.id.loding_im);
		animation = AnimationUtils.loadAnimation(this, R.anim.loding);
		imageView.setAnimation(animation);
		if (!isLogin()) {
			new AsyncSetApprove().execute();
			isLogin = false;
		} else {
			isLogin = true;
		}
		animation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation arg0) {
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {

			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				isOK = true;
				handler.sendEmptyMessage(2);
			}
		});
	}

	private boolean isLogin() {
		sp = getSharedPreferences("mark", MODE_PRIVATE);
		isLogin = sp.getBoolean("isInit", false);
		if (isLogin) {
			return true;
		}
		return false;
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			if (isLogin && isOK ) {
				startActivity(new Intent(LodingActivity.this,
						BookListActivity2.class));
				LodingActivity.this.finish();
			}
		};
	};
	class AsyncSetApprove extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... params) {
			File path = getFilesDir();
		
			String[] strings = getResources().getStringArray(R.array.bookid);// 获取assets目录下的文件列表
			for (int i = 0; i < strings.length; i++) {
				try {
					FileOutputStream out = new FileOutputStream(path + "/"
							+ strings[i]);
					BufferedInputStream bufferedIn = new BufferedInputStream(
							getResources().openRawResource(R.raw.book0 + i));
					BufferedOutputStream bufferedOut = new BufferedOutputStream(
							out);
					byte[] data = new byte[2048];
					int length = 0;
					while ((length = bufferedIn.read(data)) != -1) {
						bufferedOut.write(data, 0, length);
					}
					// 将缓冲区中的数据全部写出
					bufferedOut.flush();
					// 关闭流
					bufferedIn.close();
					bufferedOut.close();
					sp.edit().putBoolean("isInit", true).commit();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			ArrayList<HashMap<String, String>> insertList = new ArrayList<HashMap<String, String>>();
			File[] f1 = path.listFiles();
			int len = f1.length;
			for (int i = 0; i < len; i++) {
				if (f1[i].isFile()) {
					if (f1[i].toString().contains(".txt")) {
						HashMap<String, String> insertMap = new HashMap<String, String>();
						insertMap.put("parent", f1[i].getParent());
						insertMap.put("path", f1[i].toString());
						insertList.add(insertMap);
					}
				}
			}
			SQLiteDatabase db = MyApplication.bookDB.getWritableDatabase();
			db.delete(FinalDate.DATABASE_TABKE, "type='" + 2 + "'", null);

			for (int i = 0; i < insertList.size(); i++) {
				try {
					if (insertList.get(i) != null) {
						String s = insertList.get(i).get("parent");
						String s1 = insertList.get(i).get("path");
						String sql1 = "insert into " + FinalDate.DATABASE_TABKE
								+ " (parent,path" + ", type"
								+ ",now,ready) values('" + s + "','" + s1
								+ "',2,0,null" + ");";
						db.execSQL(sql1);
					}
				} catch (SQLException e) {
					Log.e("hck", "setApprove SQLException", e);
				} catch (Exception e) {
					Log.e("hck", "setApprove Exception", e);
				}
			}
			db.close();
			sp.edit().putBoolean("isInit", true).commit();
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			isLogin = true;
			handler.sendEmptyMessage(1);
			super.onPostExecute(result);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		try {
			this.unregisterReceiver(reserver);
		} catch (Exception e) {
		}
	}

	private void getInfo() {

		// 获取系统内的所有程序信息
		Intent mainintent = new Intent(Intent.ACTION_MAIN, null);
		mainintent.addCategory(Intent.CATEGORY_LAUNCHER);
		packageInfos = getApplicationContext().getPackageManager()
				.getInstalledPackages(0);

		int count = packageInfos.size();
		for (int i = 0; i < count; i++) {

			PackageInfo pinfo = packageInfos.get(i);
			ApplicationInfo appInfo = pinfo.applicationInfo;
			if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0) {
				// 系统程序 忽略
			} else {
				Log.i("hck", "page:" + pinfo.applicationInfo.packageName);
				if (pinfo.applicationInfo.packageName
						.equals("com.snda.tts.service")) {
					FinalDate.isTrue = true;
					return;
				} else {
					FinalDate.isTrue = false;

				}
			}
		}

	}

	
}
