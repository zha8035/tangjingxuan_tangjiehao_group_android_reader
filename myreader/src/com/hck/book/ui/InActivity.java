package com.hck.book.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hck.book.adapter.FileDapter;
import com.hck.book.helper.BookDB;
import com.hck.book.util.MangerActivitys;
import com.hck.book.vo.BookVo;
import com.hck.date.FinalDate;
import com.hck.test.R;
/**
 * �ļ��ĵ���
 * 
 * @author
 * 
 */
public class InActivity extends Activity implements OnClickListener {
	protected static final String TAG = "InActivity";
	protected ListView lv;
	protected TextView tt, tv1;
	protected ImageView im, imChoose;
	protected String name[];
	protected String path[];
	protected int num[];
	protected ArrayList<String> list;
	protected Set<String> set;
	protected Map<String, Integer> parentmap;
	protected Map<String, Integer> mapIn;
	protected ArrayList<Map<String, Object>> aList;
	protected int a;
	protected int i;
	protected Boolean b = true;
	protected ArrayList<String> names = null;
	protected ArrayList<String> paths = null;
	private TextView all;
	private int Image[] = { R.drawable.ok1, R.drawable.no1 };
	private Button aaaa;
	private String PATH = "path";
	private String TYPE = "type";
	private PopupWindow mPopupWindow;
	private View popunwindwow;
	private BookDB localbook;
	private HashMap<String, ArrayList<BookVo>> map1;
	protected Boolean ok = false;
	protected ProgressDialog mpDialog = null;
	private ArrayList<HashMap<String, String>> insertList;
	private HashMap<String, String> insertMap;
	protected ArrayList<Integer> intList;
	protected Context context;
	protected AlertDialog ab;
	private Thread InThread = new Thread() {
		@Override
		public void run() {
			Looper.prepare();
			File sdpath = Environment.getExternalStorageDirectory();
			try {
				printAllFile(sdpath);
			} catch (Exception e) {
				Log.e(TAG, "InThread error", e);
			}
			// �����̷߳�����Ϣ
			mHandler.sendEmptyMessage(1);
		}
	};
	private Thread updateThread = new Thread() {
		File sdpath = Environment.getExternalStorageDirectory();
		@Override
		public void run() {
			Log.i("hck", "thrunrun");
			try {
				printAllFile(sdpath);
			} catch (Exception e) {
				Log.e(TAG, "updateThread error", e);
			}
			// �����̷߳�����Ϣ
			mHandler.sendEmptyMessage(2);
			mHandler.removeCallbacks(updateThread);
		}
	};

	private Handler mHandler = new Handler() {
		// �������̷߳�������Ϣ��ͬʱ����UI
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				insert();
				map1 = select();
				show("a");
					mpDialog.dismiss();
			} else if (msg.what == 2) {
				// flshThread.stopThread();
				flu();
			}
		}
	};
	/**
	 * ����SD��,�����ݴ���insertList
	 */
	public void printAllFile(File f) {

		if (f.isFile()) {
			if (f.toString().contains(".txt")) {
				insertMap = new HashMap<String, String>();
				insertMap.put("parent", f.getParent());
				insertMap.put("path", f.toString());
				insertList.add(insertMap);
			}
		}
		if (f.isDirectory()) {
			if (f!=null) {
				File[] f1 = f.listFiles();
				if (f1==null) {
					return;
				}
				int len = f1.length;
				for (int i = 0; i < len; i++) {
					printAllFile(f1[i]);
				}
			}
		}

	}
	/**
	 * ������д�����ݿ�
	 */
	public void insert() {
		SQLiteDatabase db = localbook.getWritableDatabase();
		for (int i = 0; i < insertList.size(); i++) {
			try {
				if (insertList.get(i) != null) {
					String s = insertList.get(i).get("parent");
					String s1 = insertList.get(i).get("path");
					String sql1 = "insert into " + FinalDate.DATABASE_TABKE + " (parent,"
							+ PATH + ", " + TYPE + ",now,ready) values('" + s
							+ "','" + s1 + "',0,0,null" + ");";
					db.execSQL(sql1);
				}
			} catch (SQLException e) {
				Log.e(TAG, "insert sqlException error", e);
			} catch (Exception e) {
				Log.e(TAG, "insert Exception error", e);
			}
		}
		mPopupWindow.dismiss();
		db.close();
	}
	/**
	 * �жϲ���ʾ����
	 * 
	 * @param p
	 */
	public void show(String p) {
		Log.i("hck", "show");
		Set<String> set1 = map1.keySet();
		if (p.equals("a")) {
			aList = new ArrayList<Map<String, Object>>();
			name = new String[set1.size()];
			paths = new ArrayList<String>();
			i = 0;
			mapIn.clear();
			Iterator<String> it = set1.iterator();
			while (it.hasNext()) {
				a = 0;
				paths.add((String) it.next());
				Map<String, Object> map = new HashMap<String, Object>();
				File f1 = new File(paths.get(i));
				name[i] = f1.getName();
				map.put("icon", R.drawable.cartoon_folder);
				if (name[i].length() > 8) {
					map.put("name", name[i].substring(0, 8) + "...");
				} else {
					map.put("name", name[i]);
				}
				map.put("num", map1.get(paths.get(i)).size());
				aList.add(map);
				i = i + 1;
			}
			all.setText(null);
			if (mPopupWindow.isShowing()) {
				mPopupWindow.dismiss();
			}
			setDate(1);
		} else {
			aList = new ArrayList<Map<String, Object>>();
			ArrayList<BookVo> al = map1.get(paths.get(Integer.parseInt(p)));
			paths = new ArrayList<String>();
			Map<String, Object> map2 = new HashMap<String, Object>();
			map2.put("icon", R.drawable.back);
			map2.put("name", "������һ��");
			map2.put("num", null);
			paths.add("a");
			aList.add(map2);
			for (int i = 0; i < al.size(); i++) {
				paths.add(al.get(i).getOwen());
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("icon", R.drawable.my_fiction);
				File file = new File(al.get(i).getOwen());
				if (file.getName().substring(0, file.getName().length() - 4)
						.length() > 8) {
					map.put("name", file.getName().substring(0, 8) + "...");
				} else {
					map.put("name",
							file.getName().substring(0,
									file.getName().length() - 4));
				}
				map.put("num", "��ʽ��txt");
				// ��¼�������ݵ���ʽ
				if (al.get(i).getLocal() == 0) {
					map.put("imChoose", Image[1]);
				} else if (al.get(i).getLocal() == 1) {
					map.put("imChoosezz", "�ѵ���");
				}
				aList.add(map);
			}
			all.setText("ȫѡ");
			setDate(2);
		}
	}
	/**
	 * popupwindow�ĵ���
	 */
	public void pop() {
		mPopupWindow.showAtLocation(findViewById(R.id.main11), Gravity.BOTTOM,
				0, 0);
		aaaa = (Button) popunwindwow.findViewById(R.id.aaaa);// ȷ�ϵ��밴ť
		aaaa.setBackgroundResource(R.drawable.popin);
		aaaa.setText("ȷ�ϵ���(" + String.valueOf(mapIn.size()) + ")");
		aaaa.setOnClickListener(this);
	}

	/**
	 * ����adapter
	 * public void aaa() {
		
		SimpleAdapter listItemAdapter = new SimpleAdapter(this, aList,
				R.layout.item_in, new String[] { "icon", "name", "num",
						"imChoose", "imChoosezz" }, new int[] { R.id.im,
						R.id.tv1, R.id.tv2, R.id.imChoose, R.id.imChoosezz });
		lv.setAdapter(listItemAdapter);
	}

	 */
	public void setDate(int a) {
		Log.i("hck", "setDate");
	FileDapter	aDapter=new FileDapter(this, aList,a);
		lv.setAdapter(aDapter);
		Log.i("hck", "adpter");
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.in);
		tv1 = (TextView) findViewById(R.id.name2);
		lv = (ListView) findViewById(R.id.ListView02);
		context = this;
		imChoose = (ImageView) findViewById(R.id.imChoose);
		all = (TextView) findViewById(R.id.all);
		all.setVisibility(View.GONE);
		localbook = new BookDB(this, FinalDate.DATABASE_TABKE);
		insertList = new ArrayList<HashMap<String, String>>();
		popunwindwow = this.getLayoutInflater().inflate(R.layout.popwindow,
				null);
		mPopupWindow = new PopupWindow(popunwindwow, LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		mapIn = new HashMap<String, Integer>();// ��¼���׼��������ļ�
		parentmap = new HashMap<String, Integer>();
		set = new HashSet<String>();
		list = new ArrayList<String>();
		// �������ݿ�lackbook ������ݿ�Ϊ�տ����߳� ����SD�� ����ֱ�Ӵ����ݿ�����ȡ��ʾ
		if (select().isEmpty()) {
			Log.i("hck", "runrun");
			// �����ļ�
			showProgressDialog("���Ժ�......");
			InThread.start();
		} else {
			// ˢ���ļ�
			showProgressDialog("���Ժ�.....");
			updateThread.start();
			// ��ʾ�ļ�
			// map1 = select();
			// show("a");
		}
		// �������ļ�ʱlistview�ڵĵ���¼�
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String p = paths.get(arg2);
				all.setVisibility(View.GONE);
				// �ص���Ŀ¼
				if (p.equals("a")) {
					all.setVisibility(View.GONE);
					show("a");
				} else {
					all.setVisibility(View.VISIBLE);
					File file = new File(p);
					String s = file.getParent();
					if (file.isFile()) {
						if (map1.get(s).get(arg2 - 1).getLocal() == 0) {
							if (!mapIn.containsKey(p)) {
								Map<String, Object> map1 = aList.get(arg2);
								map1.put("imChoose", Image[0]);
								setDate(2);
								mapIn.put(p, arg2);
								if (!mPopupWindow.isShowing()) {
									pop();
									aaaa.setText("ȷ�ϵ���("
											+ String.valueOf(mapIn.size())
											+ ")");
								}
								aaaa.setText("ȷ�ϵ���("
										+ String.valueOf(mapIn.size()) + ")");
							} else {
								Map<String, Object> map1 = aList.get(arg2);
								map1.put("imChoose", Image[1]);
								setDate(2);
								mapIn.remove(p);
								if (mapIn.isEmpty()) {
									mPopupWindow.dismiss();
								}
								aaaa.setText("ȷ�ϵ���("
										+ String.valueOf(mapIn.size()) + ")");
							}
						}
					} else {
						show(String.valueOf(arg2));
					}
				}
			}
		});
		// ȫѡ����ѡ�Ĵ���
		all.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (b) {
					int length = paths.size();
					for (int i = 1; i < length; i++) {
						File file = new File(paths.get(i));
						String s = file.getParent();
						if (file.isFile()) {
							if (map1.get(s).get(i - 1).getLocal() == 0) {

								if (!mapIn.containsKey(paths.get(i))) {
									Map<String, Object> map1 = aList.get(i);
									map1.put("imChoose", Image[0]);
									setDate(2);
									mapIn.put(paths.get(i), i);
									if (!mPopupWindow.isShowing()) {
										pop();
										aaaa.setText("ȷ�ϵ���("
												+ String.valueOf(mapIn.size())
												+ ")");
									}
									aaaa.setText("ȷ�ϵ���("
											+ String.valueOf(mapIn.size())
											+ ")");
								}
							}
						}
					}
					all.setText("��ѡ");
					b = false;
				} else {
					int length = paths.size();
					for (int i = 1; i < length; i++) {
						File file = new File(paths.get(i));
						String s = file.getParent();
						if (file.isFile()) {
							if (map1.get(s).get(i - 1).getLocal() == 0) {
								if (mapIn.containsKey(paths.get(i))) {
									Map<String, Object> map1 = aList.get(i);
									map1.put("imChoose", Image[1]);
									mapIn.remove(paths.get(i));
								} else {
									Map<String, Object> map1 = aList.get(i);
									map1.put("imChoose", Image[0]);
									mapIn.put(paths.get(i), i);
								}
							}
						}
					}
					setDate(2);
					if (mapIn.isEmpty()) {
						mPopupWindow.dismiss();
					}
					if (aaaa!=null) {
						aaaa.setText("ȷ�ϵ���(" + String.valueOf(mapIn.size()) + ")");
					}
					all.setText("ȫѡ");
					b = true;
				}
			}
		});
	}
	/**
	 * ˢ�µķ���
	 */
	public void flu() {
		ArrayList<HashMap<String, String>> dbList = new ArrayList<HashMap<String, String>>();
		SQLiteDatabase db = localbook.getReadableDatabase();
		String col[] = { "parent", "path" };
		Cursor cur = db.query(FinalDate.DATABASE_TABKE, col, null, null, null, null, null);
		// �����ݿ��е�����������ӵ�dbList
		while (cur.moveToNext()) {
			HashMap<String, String> dbMap = new HashMap<String, String>();
			String s1 = cur.getString(cur.getColumnIndex("path"));
			String s = cur.getString(cur.getColumnIndex("parent"));
			dbMap.put("parent", s);
			dbMap.put("path", s1);
			dbList.add(dbMap);
		}
		SQLiteDatabase db5 = localbook.getReadableDatabase();
		Cursor cur2 = db5.query(FinalDate.DATABASE_TABKE, new String[]{"path"}, "type=2", null, null, null, null);
		Integer num = cur2.getCount();
		Log.i("hck", "InActivity444  :" +num+"");
		// ������SD���õ���insertList �� dbList���бȽ� �����д���
		for (int i = 0; i < dbList.size(); i++) {
			if (insertList.size() == 0) {
				SQLiteDatabase db1 = localbook.getWritableDatabase();
				db1.delete(FinalDate.DATABASE_TABKE, "path='" + dbList.get(i).get("path")
						+ "'", null);
				db1.close();
			} else {
				for (int j = 0; j < insertList.size(); j++) {
					if (insertList.get(j).get("parent")
							.equals(dbList.get(i).get("parent"))
							&& insertList.get(j).get("path")
									.equals(dbList.get(i).get("path"))) {
						insertList.remove(j);
						j = j - 1;
						break;
					} else {
//						Log.i("hck", "463"+"j: "+j+dbList.get(i).get("path"));
//						if (j == insertList.size() - 2) {
//							SQLiteDatabase db1 = localbook
//									.getWritableDatabase();
//							db1.delete(FinalDate.DATABASE_TABKE, "path='"
//									+ dbList.get(i).get("path") + "'", null);
//							db1.close();
//						}
					}
				}
			}
		}
		
		db.close();
		insert();
		map1 = select();
		show("a");
		try {
			mpDialog.dismiss();
		} catch (Exception e) {
		}
		
		
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// ���밴ť
		case R.id.aaaa:
			SQLiteDatabase db = localbook.getWritableDatabase();
			Set<String> setIn = mapIn.keySet();
			Iterator<?> it = setIn.iterator();
			while (it.hasNext()) {
				try {
					String s = (String) it.next();
					File f = new File(s);
					String s1 = f.getParent();
					map1.get(s1).get(mapIn.get(s) - 1).setLocal(1);// ���õ���״̬
					Map<String, Object> map = aList.get(mapIn.get(s));
					map.remove("imChoose");
					map.put("imChoosezz", "�ѵ���");
					setDate(2);
					ContentValues values = new ContentValues();
					values.put("type", 1);// keyΪ�ֶ�����valueΪֵ
					Cursor cur = db.query(FinalDate.DATABASE_TABKE, new String[]{"path"}, "type=2", null, null, null, null);
					Log.i("hck","InActivity11: "+cur.getCount());
					db.update(FinalDate.DATABASE_TABKE, values, "path=?", new String[] { s });// �޸�״̬Ϊͼ�鱻�ѱ�����
					
					//db.insert(FinalDate.DATABASE_TABKE, "path", values);// �޸�״̬Ϊͼ�鱻�ѱ�����
				} catch (SQLException e) {
					Log.e(TAG, "R.id.aaaa onclick-> SQLException error", e);
				} catch (Exception e) {
					Log.e(TAG, "R.id.aaaa onclick-> Exception error", e);
				}
			}
			db.close();
			mPopupWindow.dismiss();
			break;
		}
	}
	/**
	 * �������ݿ� �����ݴ���map1
	 * 
	 * @return map1
	 */
	public HashMap<String, ArrayList<BookVo>> select() {
		SQLiteDatabase db = localbook.getReadableDatabase();
		String col[] = { "parent" };
		Cursor cur = db.queryWithFactory(null, true, FinalDate.DATABASE_TABKE, col,
				"type<>2", null, null, null, null, null);
		ArrayList<String> arraylist1 = new ArrayList<String>();

		HashMap<String, ArrayList<BookVo>> map1 = new HashMap<String, ArrayList<BookVo>>();
		while (cur.moveToNext()) {
			String s1 = cur.getString(cur.getColumnIndex("parent"));
			arraylist1.add(s1);
		}
		String col1[] = { "path", "type" };
		for (int i = 0; i < arraylist1.size(); i++) {
			ArrayList<BookVo> arraylist2 = new ArrayList<BookVo>();
			Cursor cur1 = db.query(FinalDate.DATABASE_TABKE, col1,
					"parent = '" + arraylist1.get(i) + "'", null, null, null,
					null);
			while (cur1.moveToNext()) {
				String s2 = cur1.getString(cur1.getColumnIndex("path"));
				int s3 = cur1.getInt(cur1.getColumnIndex("type"));
				BookVo bookvo = new BookVo(s2, s3);
				arraylist2.add(bookvo);
				map1.put(arraylist1.get(i), arraylist2);
			}
		}
		db.close();
		cur.close();
		return map1;
	}

	/**
	 * ��д���˰�ť ��ҳ����˵��������
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {

			Intent it = new Intent();
			it.setClass(InActivity.this, BookListActivity2.class);
			it.putExtra("nol", "l");
			startActivity(it);
			this.finish();
		}
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i("hck", "");
	}
	@Override
	protected void onPause() {
		super.onPause();
		Log.i("hck", "onpase");
		if (mpDialog!=null) {
			mpDialog.dismiss();
		}
	}
	private void showProgressDialog(String msg) {
		mpDialog = new ProgressDialog(InActivity.this);
		mpDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// ���÷��ΪԲ�ν�����
		mpDialog.setMessage(msg);
		mpDialog.setIndeterminate(false);// ���ý������Ƿ�Ϊ����ȷ
		mpDialog.setCancelable(true);// ���ý������Ƿ���԰��˻ؼ�ȡ��
		mpDialog.show();
	}

	public void back(View view)
	{
		Intent intent = new Intent();
		intent.setClass(InActivity.this, BookListActivity2.class);
		intent.putExtra("nol", "l");
		startActivity(intent);
		this.finish();
	}
}
