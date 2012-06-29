package com.dl.ygo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.GridView;

public class CardsView extends GridView {
	private CardGroup cards = null;
	public CardsView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	public CardsView(Context context,CardGroup cards) {
		super(context);
		this.cards = cards;

		this.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
				, ViewGroup.LayoutParams.WRAP_CONTENT));
		this.setNumColumns(5);
		this.setVerticalSpacing(10);
		this.setHorizontalSpacing(4);
		this.setColumnWidth(40 + 10);
		this.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
		this.setGravity(Gravity.CENTER);
		this.setAdapter(new CardAdapter(context, cards));
	}	
}
