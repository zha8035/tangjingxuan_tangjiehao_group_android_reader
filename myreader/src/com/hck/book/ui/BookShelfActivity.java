package com.hck.book.ui;


import java.util.List;

import com.hck.book.util.MangerActivitys;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SlidingDrawer;
import android.widget.TextView;

import com.hck.test.R;


public class BookShelfActivity extends Activity {
    private GridView bookShelf;
    
    private String[] name={
    		"天龙八部","搜神记","水浒传","黑道悲情"
    };
    
    private GridView gv;  
    private SlidingDrawer sd;  
    private Button iv;  
    private List<ResolveInfo> apps;  
    

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        MangerActivitys.activitys.add(this);
        bookShelf = (GridView) findViewById(R.id.bookShelf);
        ShlefAdapter adapter=new ShlefAdapter();
        bookShelf.setAdapter(adapter);
        bookShelf.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
			}
		});
        loadApps();  
        gv.setAdapter(new GridAdapter());  
        sd.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener()// 开抽屉  
        {  
            @Override  
            public void onDrawerOpened() {  
            	iv.setText("返回");
                iv.setBackgroundResource(R.drawable.btn_local);// 响应开抽屉事件  
                                                                // ，把图片设为向下的  
            }  
        });  
        sd.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {  
            @Override  
            public void onDrawerClosed() {  
            	iv.setText("本地");
                iv.setBackgroundResource(R.drawable.btn_local);// 响应关抽屉事件  
            }  
        });  
    }

    class ShlefAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return 5;
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
			if(5>position){
				if(position<name.length){
				   view.setText(name[position]);
				}
				
			}else{
				view.setClickable(false);
				view.setVisibility(View.INVISIBLE);
			}
			return contentView;
		}
    	
    }
    
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("你确定退出吗？")
					.setCancelable(false)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									finish();
								}
							})
					.setNegativeButton("返回",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});
			AlertDialog alert = builder.create();
			alert.show();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}
    
    
    private void loadApps() {  
        Intent intent = new Intent(Intent.ACTION_MAIN, null);  
        intent.addCategory(Intent.CATEGORY_LAUNCHER);  
  
        apps = getPackageManager().queryIntentActivities(intent, 0);  
    }  
  
    public class GridAdapter extends BaseAdapter {  
        public GridAdapter() {  
  
        }  
  
        public int getCount() {  
            // TODO Auto-generated method stub  
            return apps.size();  
        }  
  
        public Object getItem(int position) {  
            // TODO Auto-generated method stub  
            return apps.get(position);  
        }  
  
        public long getItemId(int position) {  
            // TODO Auto-generated method stub  
            return position;  
        }  
  
        public View getView(int position, View convertView, ViewGroup parent) {  
            // TODO Auto-generated method stub  
            ImageView imageView = null;  
            if (convertView == null) {  
                imageView = new ImageView(BookShelfActivity.this);  
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);  
                imageView.setLayoutParams(new GridView.LayoutParams(50, 50));  
            } else {  
                imageView = (ImageView) convertView;  
            }  
  
            ResolveInfo ri = apps.get(position);  
            imageView.setImageDrawable(ri.activityInfo  
                    .loadIcon(getPackageManager()));  
  
            return imageView;  
        }  
  
    }  

}