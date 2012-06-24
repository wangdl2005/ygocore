package com.dl.ygo;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class CardGroup {
	private ArrayList<Card> cards;
	//cout cards当前的数目
	private int count;
	//size cards的容量
	private int size;
	public CardGroup(int size,ArrayList<Card> cards){
		this.size = size;
		this.cards = cards;
		this.count = cards.size();
	}
	public CardGroup(int size){
		this.size = size;
		this.cards = new ArrayList<Card>();
		this.count = 0;
	}
	
	public int getCount(){
		return count;
	}
	public int getSize(){
		return size;
	}
	//only work when count > 0
	public Card getFirstCard(){
		Card c = null;
		if(count > 0){
			c = cards.get(0);
		}
		return c;
	}
	//only work when count > 0
		public Card getLastCard(){
			Card c = null;
			if(count > 0){
				c = cards.get(count-1);
			}
			return c;
		}
	//getCards
	public 	ArrayList<Card> getCards(){
		return cards;
	}
	//addCard
	public void addCard(Card c){
		++count;
		cards.add(c);		
	}
}
