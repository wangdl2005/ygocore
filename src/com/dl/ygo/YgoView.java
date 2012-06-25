package com.dl.ygo;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dl.ygo.*;

public class YgoView extends ViewGroup {
	int width = 0;
	int height = 0;
	public YgoView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		setWillNotDraw(false);
	}


	public YgoView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		width = w;
		height = h;
		Log.d("width:",String.valueOf(w));
		Log.d("height:",String.valueOf(h));
		super.onSizeChanged(w, h, oldw, oldh);
	}
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub		
		Singleton.getInstance(this).onDraw(canvas);;
		super.onDraw(canvas);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub		
		ArrayList<Rect> rects  = Singleton.getInstance(this).onTouchEvent(event);
		for(Rect r : rects){
			invalidate(r);
		}
		//addMyView();
		return super.onTouchEvent(event);
	}


	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		Singleton.getInstance(this).onLayout();
	}
	
	private void addMyView()
	{
		AlertDialog.Builder dlg = new AlertDialog.Builder(this.getContext());
		final EditText editText =  new EditText(this.getContext());
		editText.setId(100);
		dlg.setTitle("LP:").setIcon(
			     android.R.drawable.ic_dialog_info).setView(editText).setPositiveButton("确定", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						String text = editText.getText().toString();
						Log.d("YGO",text);
					}
				} )
		.setNegativeButton("取消", null).show();	
	}
}
