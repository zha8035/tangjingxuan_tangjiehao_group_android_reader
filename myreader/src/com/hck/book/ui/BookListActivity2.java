package com.hck.book.ui;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.hck.book.helper.BookDB;
import com.hck.book.helper.MarkHelper;
import com.hck.book.util.AlertDialogs;
import com.hck.book.util.MangerActivitys;
import com.hck.book.util.PinyinListComparator;
import com.hck.date.FinalDate;
import com.hck.test.R;

public class BookListActivity2 extends Activity implements BaseActivity{
	private static final String TAG = "MainActivity";
	private Intent it;
	private ArrayList<HashMap<String, Object>> listItem = null;
	private BookDB localbook;
	private HashMap<String, Object> map = null;
	private Map<String, Integer[]> map2;// 存放本地推荐目录的小封面图片引用
	protected ProgressDialog mDialog = null;
	View menuView;
	ShlefAdapter adapter;
	private SharedPreferences sp;
	private GridView toolbarGrid;
	private MarkHelper markhelper;
	private SharedPreferences.Editor editor;
	private int post;
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		
		MangerActivitys.activitys.add(this);
		
		
		toolbarGrid=(GridView) findViewById(R.id.bookShelf);
		localbook = new BookDB(this,FinalDate.DATABASE_TABKE);
			getDate();
			sp=getSharedPreferences("book", Context.MODE_PRIVATE);
		toolbarGrid.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				Log.i("hck", "longclick ");
				post=arg2;
	   new AlertDialogs(BookListActivity2.this,BookListActivity2.this)
	   .alertDialog("确定删除吗？", "", "删除", "取消", "delete");
				return true;
			}
		});
		toolbarGrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				try {
					
					// 修改数据库中图书的最近阅读状态为1
					String s = (String) listItem.get(arg2).get("path");
					SQLiteDatabase db = localbook.getWritableDatabase();

					File f = new File(s);
					if (f.length() == 0) {
						Toast.makeText(BookListActivity2.this, "该文件为空文件", Toast.LENGTH_SHORT).show();
						
					} else {
						ContentValues values = new ContentValues();
						values.put("now", 1);// key为字段名，value为值
						db.update(FinalDate.DATABASE_TABKE, values, "path=?", new String[] { s });// 修改状态为图书被已被导入
						db.close();
						String path = (String) listItem.get(arg2).get("path");
						it = new Intent();
						it.setClass(BookListActivity2.this, Read.class);
						it.putExtra("path", path);
						startActivity(it);
					}
				} catch (SQLException e) {
					Log.e("hck", "list.setOnItemClickListener-> SQLException error", e);
				} catch (Exception e) {
					Log.e("hck", "list.setOnItemClickListener Exception", e);
				}
			}				
		});
	}
	public void online(View view){
		Intent intent = new Intent();
		intent.setClass(BookListActivity2.this,Online.class);
		startActivity(intent);
	}
	public void addText(View view)
	{
		Intent intent = new Intent();
		intent.setClass(BookListActivity2.this, InActivity.class);
		startActivity(intent);
		this.finish();
	}
	
	
	/**
	 * 获取SD卡根目录
	 * 
	 * @return
	 */
	public String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
		}
		return sdDir.toString();
	}

	/**
	 * 本地书库载入
	 */
	public void getDate() {
		SQLiteDatabase db = localbook.getReadableDatabase();
		String col[] = { "path" };
		Cursor cur = db.query(FinalDate.DATABASE_TABKE, col, "type=1", null, null, null, null);
		Cursor cur1 = db.query(FinalDate.DATABASE_TABKE, col, "type=2", null, null, null, null);
		Integer num = cur.getCount();
		Integer num1 = cur1.getCount();
		Log.i("hck", "booklistActivity2 :"+num);
		Log.i("hck", "booklistActivity2 :"+num1);
		ArrayList<String> arraylist = new ArrayList<String>();
		while (cur1.moveToNext()) {
			String s = cur1.getString(cur1.getColumnIndex("path"));
			arraylist.add(s);
		}
		while (cur.moveToNext()) {
			String s = cur.getString(cur.getColumnIndex("path"));
			arraylist.add(s);
		}
		db.close();
		cur.close();
		cur1.close();
		if (listItem == null)
			listItem = new ArrayList<HashMap<String, Object>>();
		listItem.clear();
		String[] bookids = getResources().getStringArray(R.array.bookid);
		String[] booknames = getResources().getStringArray(R.array.bookname);
		String[] bookauthors = getResources().getStringArray(R.array.bookauthor);
		Map<String, String[]> maps = new HashMap<String, String[]>();
		for (int i = 0; i < bookids.length; i++) {
			String[] value = new String[2];
			value[0] = booknames[i];
			value[1] = bookauthors[i];
			maps.put(bookids[i], value);
		}
		for (int i = 0; i < num + num1; i++) {
			if (i < num1) {
				File file1 = new File(arraylist.get(i));
				String m = file1.getName().substring(0, file1.getName().length() - 4);
				if (m.length() > 8) {
					m = m.substring(0, 8) + "...";
				}
				String id = arraylist.get(i).substring(arraylist.get(i).lastIndexOf("/") + 1);
				String[] array = maps.get(id);
				String auther = array != null && array[1] == null ? "未知" : array[1];
				String name = array[0] == null ? m : array[0];
				map = new HashMap<String, Object>();

				if (i == 0) {
					map.put("itemback", R.drawable.itemback);
				} else if ((i % 2) == 0) {
					map.put("itemback", R.drawable.itemback);
				}
				map.put("ItemImage", map2 != null ? map2.get(file1.getName())[0] : R.drawable.cover);
				map.put("BookName", name == null ? m : name);
				map.put("ItemTitle1", "作者：" + auther);
				map.put("LastImage", "推荐书目");
				map.put("path", file1.getPath());
				map.put("com", 0 + file1.getName());// 单独用于排序
				listItem.add(map);
				Log.i("hck","本地"+ listItem.size()+"size");
			} else {
				map = new HashMap<String, Object>();

				File file1 = new File(arraylist.get(i));
				String m = file1.getName().substring(0, file1.getName().length() - 4);
				if (m.length() > 8) {
					m = m.substring(0, 8) + "...";
				}
				if (i == 0) {
					map.put("itemback", R.drawable.itemback);
				} else if ((i % 2) == 0) {
					map.put("itemback", R.drawable.itemback);
				}
				map.put("ItemImage", R.drawable.cover);
				map.put("BookName", m);
				map.put("ItemTitle", m);
				map.put("ItemTitle1", "作者：未知");
				map.put("LastImage", "本地导入");
				map.put("path", file1.getPath());
				map.put("com", "1");
				listItem.add(map);
				
			}
		}
		Collections.sort(listItem, new PinyinListComparator());
	//	if (adapter == null) {
			adapter = new ShlefAdapter();
			toolbarGrid.setAdapter(adapter);
	//	}
	//	adapter.notifyDataSetChanged();
	}



	
	 class ShlefAdapter extends BaseAdapter{

			@Override
			public int getCount() {
				return listItem.size();
			}

			@Override
			public Object getItem(int arg0) {
				return arg0;
			}

			@Override
			public long getItemId(int arg0) {
				return arg0;
			}

			@Override
			public View getView(int position, View contentView, ViewGroup arg2) {
				
				contentView=LayoutInflater.from(getApplicationContext()).inflate(R.layout.item1, null);
				
				TextView view=(TextView) contentView.findViewById(R.id.imageView1);
			
                view.setText(listItem.get(position).get("BookName").toString());
                
				return contentView;
			}
	    	
	    }
	

	@Override
	protected void onDestroy() {

		super.onDestroy();
		
		for (int i = 0; i < MangerActivitys.activitys.size(); i++) {
			if (MangerActivitys.activitys.get(i)!=null) {
				((Activity)MangerActivitys.activitys.get(i)).finish();
			}
		}
		FinalDate.isTrue=false;
		FinalDate.mConnection=null;
	
		MyApplication.bookDB=null;
		finish();
		System.gc();
	}

	
	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	@Override
	public void init() {
		
	}
	@Override
	public void refresh(Object... params) {
		
	}
	@Override
	public void server() {
		deleteFile(post);
	}
	private void deleteFile(int post)
	{
		HashMap<String, Object> imap = listItem.get(post);
		String path0 = (String) imap.get("path");
		SQLiteDatabase db = localbook.getWritableDatabase();
		try {
			ContentValues values = new ContentValues();
			values.put("now", 0);// key为字段名，value为值
			values.put("type", 0);
			values.putNull("ready");
			// db.update("localbook", values, "path=?", new String[] { s
			// });// 修改状态为图书被已被导入
			db.delete(FinalDate.DATABASE_TABKE, "path='" + path0 + "'", null);
			db.update(FinalDate.DATABASE_TABKE, values, "path=? and type=1", new String[] { path0 });// 修改状态为图书被已被导入
			// 清空对本书的记录
			editor = sp.edit();
			editor.remove(path0 + "jumpPage");
			editor.remove(path0 + "count");
			editor.remove(path0 + "begin");
			editor.commit();
			markhelper = new MarkHelper(this);
			// 删除数据库书签记录
			SQLiteDatabase db2 = markhelper.getWritableDatabase();
			db2.delete("markhelper", "path='" + path0 + "'", null);
			db2.close();
		} catch (SQLException e) {
			Log.e(TAG, "onContextItemSelected-> SQLException error", e);
		} catch (Exception e) {
			Log.e(TAG, "onContextItemSelected-> Exception error", e);
		}
		db.close();
		// 重新载入页面
		getDate();
	}
	

}