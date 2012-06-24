package com.dl.ygo;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class MyButton {
	public Rect getRect() {
		return rect;
	}
	public Bitmap getBmp() {
		return bmp;
	}
	private Rect rect;
	private Bitmap bmp;
	public MyButton(Rect rect,Bitmap bmp){
		this.rect = rect;
		this.bmp = bmp;
	}
}
