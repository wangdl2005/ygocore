package com.dl.ygo;

import java.util.HashMap;

import com.dl.ocg.CardDataC;
import com.dl.ocg.CardString;
import com.dl.ocg.Direction;
import com.dl.ocg.Zone;
import static com.dl.ocg.Common.*;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Card {
	private int x;
	private int y;
	private int width;
	private int height;
	private Direction dir;
	private int code;
	private CardDataC cardDataC;
	private String cardName;
	private String cardDesc;
	private Bitmap cardPic;
	private Zone cardZone;
	private Rect rect;
	public Bitmap getCardPic(){
		return cardPic;
	}
	public void setCardPic(Bitmap cardPic){
		this.cardPic = cardPic;
	}
	
	public Rect getRect(){
		CardLocation loc =getLoc();
		if(loc == CardLocation.LOCATION_MZONE || loc == CardLocation.LOCATION_SZONE)
		{
			return cardZone.getRect();
		}
		return rect;
	}
	//对于Rect 与 Zone 不一样的Card都需要重新设置
	public void setRect(Rect r){
		this.rect = r;
	}
	
	public String getName(){
		return cardName;
	}
	public int getCode(){
		return code;
	}
	public String getAttr(){
		return FormatAttribute(cardDataC.attribute.getValue());
	}
	public int getLevel(){
		return cardDataC.level;
	}
	public int getAttack(){
		return cardDataC.attack;
	}
	public int getDefence(){
		return cardDataC.defence;
	}
	public String getRace(){
		return FormatRace(cardDataC.race.getValue());
	}
	public String getDesc(){
		return cardDesc;
	}
	
	public String getType(){
		return FormatType(cardDataC.type);
	}
	
	public CardLocation getLoc(){
		return this.cardZone.getLoc();
	}
	
	public void setCardZone(Zone z){
		this.cardZone = z;
	}
	public Card(CardDataC cdc,CardString cs,Bitmap bmp,Zone cardZone){
		this.cardDataC = cdc;
		this.cardName = cs.name;
		this.cardDesc = cs.text;
		this.cardPic = bmp;
		this.cardZone = cardZone;
		this.code = cdc.code;
		this.dir = Direction.UP;		
	}
	public Card(CardDataC cdc,CardString cs,Bitmap bmp){
		this.cardDataC = cdc;
		this.cardName = cs.name;
		this.cardDesc = cs.text;
		this.cardPic = bmp;
		this.code = cdc.code;
		this.dir = Direction.UP;		
		this.cardZone = new Zone(CardLocation.LOCATION_OFFFIELD);
	}
	public Card(HashMap<Integer,CardDataC> cardDatas,HashMap<Integer,CardString> cardStrings
			,HashMap<Integer,Bitmap> cardPics,int code)
	{
		this(cardDatas.get(code),cardStrings.get(code),cardPics.get(code));
	}
}
