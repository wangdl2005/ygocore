package com.dl.ocg;

import android.graphics.Rect;

import com.dl.ocg.Common.CardLocation;

public class Zone{
	private CardLocation loc;
	private Rect rect;
	public CardLocation getLoc() {
		return loc;
	}
	public void setLoc(CardLocation loc) {
		this.loc = loc;
	}
	public Rect getRect() {
		return rect;
	}
	public void setRect(Rect rect) {
		this.rect = rect;
	}
	public void set(int left,int top,int right,int bottom)
	{
		this.rect = new Rect(left,top,right,bottom);
	}
	public Zone(CardLocation loc){
		this.loc = loc;
	}
	
	public boolean isTheSame(Zone another){
		if(another.getLoc() == loc && 
				another.getRect().equals(rect))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
