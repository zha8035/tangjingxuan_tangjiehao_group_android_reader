package com.hck.book.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.hck.book.ui.Read;
import com.iflytek.speech.SpeechError;
import com.iflytek.speech.SynthesizerPlayer;
import com.iflytek.ui.SynthesizerDialog;
import com.iflytek.ui.SynthesizerDialogListener;

public class SpeekUtil {

	private int flag;
	private int post;
	private int type;
	private Context context;
	private String appId = "519328ab";

	private SynthesizerPlayer mSynthesizerPlayer;

	private SynthesizerDialog ttsDialog;

	public SpeekUtil(Context context) {
		this.context = context;
		if (ttsDialog == null) {
			ttsDialog = new SynthesizerDialog(context, "appid=" + appId);
		}
		// if (beans == null) {
		// beans = new ArrayList<JokSpeekBean>();
		// }

	}

	public void start(String content) {
		Log.i("hck", "start content: "+content);
		if (ttsDialog == null) {
			ttsDialog = new SynthesizerDialog(context, "appid=" + appId);
		}
		
		showSynDialog(content);
		ttsDialog.setListener(new SynthesizerDialogListener() {

			@Override
			public void onEnd(SpeechError arg0) {
				
				((Read) context).nextPage();

			}
		});
	}

	public void showSynDialog(String content) {

		ttsDialog.setText(content, null);

		String role = "vixx";
		ttsDialog.setVoiceName(role);

		int speed = 65;
		ttsDialog.setSpeed(speed);
		int volume = 90;
		ttsDialog.setVolume(volume);

		ttsDialog.show();

	}

	public void onStop() {
		if (null != mSynthesizerPlayer) {
			mSynthesizerPlayer.cancel();
		}
	}

}
