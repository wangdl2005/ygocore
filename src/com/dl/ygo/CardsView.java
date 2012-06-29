package com.dl.ygo;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.GridView;

public class CardsView extends GridView {
	private CardGroup cards = null;
	public void setCards(CardGroup cards){
		this.cards = cards;
		this.setAdapter(new CardAdapter(getContext(), cards));
	}
	
	public CardsView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT
				, ViewGroup.LayoutParams.FILL_PARENT));
		this.setNumColumns(8);
		this.setVerticalSpacing(3);
		this.setHorizontalSpacing(4);
		this.setColumnWidth(50);
		this.setStretchMode(GridView.NO_STRETCH);
		//this.setGravity(Gravity.CENTER);
		this.setAdapter(new CardAdapter(context, cards));
		this.setBackgroundColor(Color.LTGRAY);
	}
	
	public CardsView(Context context,CardGroup cards) {
		super(context);
		this.cards = cards;

		this.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT
				, ViewGroup.LayoutParams.FILL_PARENT));
		this.setNumColumns(8);
		this.setVerticalSpacing(3);
		this.setHorizontalSpacing(4);
		this.setColumnWidth(50);
		this.setStretchMode(GridView.NO_STRETCH);
		//this.setGravity(Gravity.CENTER);
		this.setAdapter(new CardAdapter(context, cards));
		this.setBackgroundColor(Color.LTGRAY);
	}	
	
	
}
