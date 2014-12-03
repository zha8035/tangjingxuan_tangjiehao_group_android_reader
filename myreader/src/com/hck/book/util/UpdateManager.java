package com.hck.book.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hck.book.ui.MyApplication;
import com.hck.date.FinalDate;

import com.hck.test.R;
public class UpdateManager
{
	
	private static final int DOWNLOAD = 1;
	/* 下载结束 */
	private static final int DOWNLOAD_FINISH = 2;
	/* 保存解析的XML信息 */
	HashMap<String, String> mHashMap;
	/* 下载保存路径 */
	private File mSavePath;
	/* 记录进度条数�?*/
	private int progress;
	/* 是否取消更新 */
	private boolean cancelUpdate = false;

	private Context mContext;
	/* 更新进度�?*/
	private ProgressBar mProgress;
	private Dialog mDownloadDialog;
  //  private String path="http://tingting.sdo.com/center/download.php";
    private String path="http://cdn.market.hiapk.com/data/upload/2013/03_11/15/com.snda.tts.service_152131.apk";
	private Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
			case DOWNLOAD:
				
				mProgress.setProgress(progress);
				break;
			case DOWNLOAD_FINISH:
				Log.i("hck", "完成");
				installApk();
				break;
			case 0:
				Toast.makeText(mContext, "网络异常 下载失败", Toast.LENGTH_LONG).show();
				break;
			default:
				break;
			}
		};
	};

	public UpdateManager(Context context)
	{
		this.mContext = context;
	}

	

	/**
	 * 显示软件下载对话�?
	 */
	public void showDownloadDialog()
	{
		// 构�?软件下载对话�?
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("软件下载");
		// 给下载对话框增加进度�?
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.softupdate_progress, null);
		mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
		builder.setView(v);
		// 取消更新
		builder.setNegativeButton("取消", new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
				// 设置取消状�?
				cancelUpdate = true;
			}
		});
		mDownloadDialog = builder.create();
		mDownloadDialog.show();
		// 现在文件
		downloadApk();
	}

	/**
	 * 下载apk文件
	 */
	private void downloadApk()
	{
		new downloadApkThread().start();
	}

	/**
	 * 下载文件线程
	 * 
	 * @author coolszy
	 *@date 2012-4-26
	 *@blog http://blog.92coding.com
	 */
	private class downloadApkThread extends Thread
	{
		@Override
		public void run()
		{
			try
			{
				// 判断SD卡是否存在，并且是否具有读写权限
				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
				{
					Log.i("hck", "sdsdsdsd");
					// 获得存储卡的路径
					File file = Environment.getExternalStorageDirectory();
					
					URL url = new URL(path);
					// 创建连接
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.connect();
					int length = conn.getContentLength();
					InputStream is = conn.getInputStream();
					mSavePath = new File(file+"/kedou/");
					if(!mSavePath.exists()){
						mSavePath.mkdir();
					}
					File apkFile = new File(mSavePath+"/kedou.apk");
					FileOutputStream fos = new FileOutputStream(apkFile);
					int count = 0;
					// 缓存
					byte buf[] = new byte[1024];
					// 写入到文件中
					do
					{
						int numread = is.read(buf);
						
						count += numread;
						// 计算进度条位�?
						progress = (int) (((float) count / length) * 100);
						// 更新进度
						mHandler.sendEmptyMessage(DOWNLOAD);
						if (numread <= 0)
						{
							// 下载完成
							mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
							break;
						}
						// 写入文件
						fos.write(buf, 0, numread);
						fos.flush();
					} while (!cancelUpdate);// 点击取消就停止下�?
					fos.close();
					is.close();
				}
				else {
					
				}
			} catch (MalformedURLException e)
			{
				e.printStackTrace();
				Log.e("hck", "eee:"+e);
			} catch (IOException e)
			{
				e.printStackTrace();
				Log.e("hck", "eee:"+e);
			}
			
			// 取消下载对话框显�?
			mDownloadDialog.dismiss();
		}
		
	};

	/**
	 * 安装APK文件
	 */
	public boolean isExit()
	{
		File apkfile = new File(Environment.getExternalStorageDirectory()+"/kedou/","kedou.apk");
		if (!apkfile.exists())
		{
			return false;
		}
		return true;
	}
	public void installApk()
	{
		File apkfile = new File(mSavePath,"kedou.apk");
		if (!apkfile.exists())
		{
			return ;
		}
		
		// 通过Intent安装APK文件
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
		mContext.startActivity(i);
		FinalDate.isTrue=true;
		
	}
}
