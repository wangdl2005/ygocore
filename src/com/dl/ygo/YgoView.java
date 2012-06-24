package com.dl.ygo;

import java.util.ArrayList;

import android.content.Context;
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
import android.widget.TextView;

import com.dl.ygo.*;

public class YgoView extends View {
	int width = 0;
	int height = 0;
	
	public YgoView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
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
		Singleton.getInstance().onDraw(canvas);;
		super.onDraw(canvas);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub		
		ArrayList<Rect> rects  = Singleton.getInstance().onTouchEvent(event);
		for(Rect r : rects){
			invalidate(r);
		}
		return super.onTouchEvent(event);
	}
}
