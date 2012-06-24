package com.dl.ygo;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

import com.dl.ocg.CardDataC;
import com.dl.ocg.CardString;
import com.dl.ocg.Zone;
import com.dl.ocg.Common.*;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.MotionEvent;

public class Singleton {
	//单态
	private static Singleton instance = null;
	private Singleton(){
		init();
	}
	private void init() {
		pen = new Paint();
		readData();
		setZone();
		bgImg = textures.get("bg");
		bgCardImg = textures.get("bg_card");
		coverImg = textures.get("cover");
		cardOnShow = new Card(cardDatas.get(1), cardStrings.get(1),cardPics.get(1),zone_on_show);
		initialCardGroup();
		test();
	}
	public static synchronized Singleton getInstance(){
		if(instance == null){
			instance = new Singleton();
		}
		return instance;
	}
	//
	private CardGroup cardsHandPlayer00;
	private CardGroup cardsSTPlayer00;
	private CardGroup cardsMZONEPlayer00;
	private CardGroup cardsDeckPlayer00;
	private CardGroup cardsGravePlayer00;
	private CardGroup cardsExtraPlayer00;
	private CardGroup cardsBanishPlayer00;
	private CardGroup cardsHandPlayer01;	
	private CardGroup cardsSTPlayer01;
	private CardGroup cardsMZONEPlayer01;
	private CardGroup cardsDeckPlayer01;
	private CardGroup cardsGravePlayer01;
	private CardGroup cardsExtraPlayer01;
	private CardGroup cardsBanishPlayer01;
	
	private Player player00;
	private Player player01;
	private Card cardOnShow;
	

	private static Bitmap bgImg;
	private static Bitmap bgCardImg;
	private Bitmap coverImg;
	
	private Paint pen;
	private Context context;
	//card data
	private HashMap<String,Bitmap> textures = new HashMap<String, Bitmap>();
	private HashMap<Integer,Bitmap> cardPics = new HashMap<Integer, Bitmap>();
	private HashMap<Integer,CardDataC> cardDatas = new HashMap<Integer,CardDataC>();
	private HashMap<Integer,CardString> cardStrings = new HashMap<Integer, CardString>();
	private HashMap<Integer,String> counterStrings = new HashMap<Integer, String>();
	private HashMap<Integer,String> victoryStrings = new HashMap<Integer, String>();
	private HashMap<Integer,String> sysStrings = new HashMap<Integer, String>();
	
	//数据库。。常量
	private static final String TAG = "YGO";
	private static final int CARD_DECK_MAX_SIZE = 60;
	private static final int CARD_EXTRA_MAX_SIZE = 15;
	private static final int CARD_BANISH_MAX_SIZE = 75;
	private static final int CARD_MZONE_MAXSIZE = 5;
	private static final int CARD_SZONE_MAXSIZE = 6;
	private static final int CARD_GRAVE_MAX_SIZE = 75;
	private static final int CARD_HAND_MAX_SIZE = 75;
	private static final int smallCardWidth = 38;
	private static final int smallCardHeight = 54;
	//坐标，区域
	/* 1_hand ...
	 * 1_deck    1_szone_4 1_szone_3 1_szone_2 1_szone_1 1_szone_0 1_extra
	 * 1_grave   1_mzone_4 1_mzone_3 1_mzone_2 1_mzone_1 1_mzone_0 1_szone_5
	 * 1_remove													   0_remove
	 * 0_szone_5 0_mzone_0 0_mzone_1 0_mzone_2 0_mzone_3 0_mzone_4 0_grave
	 * 0_extra   0_szone_0 0_szone_1 0_szone_2 0_szone_3 0_szone_4 0_deck
	 * 0_hand ...
	 */
	private Zone zone_on_show = new Zone(CardLocation.LOCATION_OFFFIELD);
	
	private ArrayList<Zone> zones = new ArrayList<Zone>();
	private Zone zone_1_deck = new Zone(CardLocation.LOCATION_DECK);
	private Zone zone_1_grave = new Zone(CardLocation.LOCATION_GRAVE);
	private Zone zone_1_extra = new Zone(CardLocation.LOCATION_EXTRA);
	private Zone zone_1_remove = new Zone(CardLocation.LOCATION_REMOVED);
	private Zone zone_1_mzone_0 = new Zone(CardLocation.LOCATION_MZONE);
	private Zone zone_1_mzone_1 = new Zone(CardLocation.LOCATION_MZONE);
	private Zone zone_1_mzone_2 = new Zone(CardLocation.LOCATION_MZONE);
	private Zone zone_1_mzone_3 = new Zone(CardLocation.LOCATION_MZONE);
	private Zone zone_1_mzone_4 = new Zone(CardLocation.LOCATION_MZONE);
	private Zone zone_1_szone_0 = new Zone(CardLocation.LOCATION_SZONE);
	private Zone zone_1_szone_1 = new Zone(CardLocation.LOCATION_SZONE);
	private Zone zone_1_szone_2 = new Zone(CardLocation.LOCATION_SZONE);
	private Zone zone_1_szone_3 = new Zone(CardLocation.LOCATION_SZONE);
	private Zone zone_1_szone_4 = new Zone(CardLocation.LOCATION_SZONE);
	private Zone zone_1_szone_5 = new Zone(CardLocation.LOCATION_SZONE);
	private Zone zone_1_hand = new Zone(CardLocation.LOCATION_HAND);
	
	private Zone zone_0_deck = new Zone(CardLocation.LOCATION_DECK);
	private Zone zone_0_grave = new Zone(CardLocation.LOCATION_GRAVE);
	private Zone zone_0_extra = new Zone(CardLocation.LOCATION_EXTRA);
	private Zone zone_0_remove = new Zone(CardLocation.LOCATION_REMOVED);
	private Zone zone_0_mzone_0 = new Zone(CardLocation.LOCATION_MZONE);
	private Zone zone_0_mzone_1 = new Zone(CardLocation.LOCATION_MZONE);
	private Zone zone_0_mzone_2 = new Zone(CardLocation.LOCATION_MZONE);
	private Zone zone_0_mzone_3 = new Zone(CardLocation.LOCATION_MZONE);
	private Zone zone_0_mzone_4 = new Zone(CardLocation.LOCATION_MZONE);
	private Zone zone_0_szone_0 = new Zone(CardLocation.LOCATION_SZONE);
	private Zone zone_0_szone_1 = new Zone(CardLocation.LOCATION_SZONE);
	private Zone zone_0_szone_2 = new Zone(CardLocation.LOCATION_SZONE);
	private Zone zone_0_szone_3 = new Zone(CardLocation.LOCATION_SZONE);
	private Zone zone_0_szone_4 = new Zone(CardLocation.LOCATION_SZONE);
	private Zone zone_0_szone_5 = new Zone(CardLocation.LOCATION_SZONE);//环境卡
	private Zone zone_0_hand = new Zone(CardLocation.LOCATION_HAND);
	
	private Rect rect_all = new Rect();
	private Rect rect_desc = new Rect();

	private void readData(){
		readDatabase();
		readStrings();
		readBitmap();
	}
	//读取图片信息
	private void readBitmap(){
		Bitmap bmp ;
		String dirPath = "/data/data/com.dl.ygo/pics/";
		File dir = new File(dirPath);
		File[] fileList = dir.listFiles();
		if(fileList != null){
			for(File f : fileList){
				//Log.d(TAG, f.getAbsolutePath());
				bmp = ((BitmapDrawable)(BitmapDrawable.createFromPath(f.getAbsolutePath()))).getBitmap();  				
				Log.d(TAG,f.getName());
				try{
				cardPics.put(Integer.parseInt(f.getName().replaceAll(".jpg","")),bmp);				
				}
				catch(NumberFormatException ex){
					continue;
				}
			}
		}
		dirPath = "/data/data/com.dl.ygo/textures/";
		dir = new File(dirPath);
		fileList = dir.listFiles();
		if(fileList != null){
			for(File f : fileList){
				//Log.d(TAG, f.getAbsolutePath());
				bmp = ((BitmapDrawable)(BitmapDrawable.createFromPath(f.getAbsolutePath()))).getBitmap();  				
				Log.d(TAG,f.getName());
				textures.put(f.getName().replaceAll(".png",""),bmp);				
			}
		}
	}
	//读取字符串信息
	private void readStrings(){
		Log.d(TAG,"read strings.conf");
    	String fileName = "/data/data/com.dl.ygo/strings.conf";
		File file = new File(fileName);
		BufferedReader bf = null;
		try{
			bf = new BufferedReader(new FileReader(file));
			String tempString = null;
			while((tempString = bf.readLine())!= null){
				if(!tempString.startsWith("!")){
					continue;
				}
				if(tempString.startsWith("!system")){
					String tmpStrings[] = tempString.split(" ");
					sysStrings.put(Integer.parseInt(tmpStrings[1]), tmpStrings[2]);
				}else if(tempString.startsWith("!victory")){
					String tmpStrings[] = tempString.split(" ");
					victoryStrings.put(Integer.parseInt(tmpStrings[1].replace("0x",""), 16), tmpStrings[2]);
				}else if(tempString.startsWith("!counter")){
					String tmpStrings[] = tempString.split(" ");
					counterStrings.put(Integer.parseInt(tmpStrings[1].replace("0x",""), 16), tmpStrings[2]);
				}
			}
		}
		catch(Exception ex){			
			ex.printStackTrace();
		}
		finally{
			if(bf!=null){
				try{
					bf.close();
				}catch(Exception ex)
				{}
			}
			
		}
		Log.d(TAG,"end strings.conf");
	}
	//读取数据库
	private void readDatabase(){
		CardDataC cd = new CardDataC();
    	CardString cs = new CardString();
    	Log.d(TAG,"begin read");
    	//SQLiteDatabase db = ygoDB.getWritableDatabase();
    	SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase("/data/data/com.dl.ygo/databases/cards.cdb", null);
    	Log.d(TAG, "get db");
    	String sql = "select * from datas,texts where datas.id=texts.id";
    	Cursor cursor = db.rawQuery(sql, null);
    	Log.d(TAG, "begin sql");
    	while(cursor.moveToNext()){    		
    		cd.code = cursor.getInt(0);
			cd.ot = cursor.getInt(1);
			cd.alias = cursor.getInt(2);
			cd.setcode = cursor.getInt(3);
			cd.type = CardType.toCardType(cursor.getInt(4));
			cd.attack = cursor.getInt(5);
			cd.defence = cursor.getInt(6);
			cd.level = cursor.getInt(7);
			cd.race = CardRace.toCardRace(cursor.getInt(8));
			cd.attribute =CardAttribute.toCardAttribute(cursor.getInt(9));
			cd.category = cursor.getInt(10);
			cardDatas.put(cd.code, cd);
			cs.name = cursor.getString(12);
			cs.text = cursor.getString(13);
			for(int i = 14;i < 30;++i){
				cs.desc[i-14] = cursor.getString(i);
			}
			cardStrings.put(cd.code, cs);
			
			cd = new CardDataC();
			cs = new CardString();
    	}
    	Log.d(TAG,"end sql");
    	db.close();
	}
	
	//set rect
	private void setZone(){
		zone_on_show.set(3, 5, 163, 230);
		zone_0_extra.set(170,323,210,377);
		zone_0_szone_0.set(235, 323, 275, 377);
		zone_0_szone_1.set(300, 323, 340, 377);
		zone_0_szone_2.set(370, 323, 408, 377);
		zone_0_szone_3.set(435, 323, 475, 377);
		zone_0_szone_4.set(502, 323, 540, 377);
		zone_0_deck.set(570,323,610,377);
		zone_0_szone_5.set(170,264,210,318);
		zone_0_mzone_0.set(235,264,275,318);
		zone_0_mzone_1.set(300,264,340,318);
		zone_0_mzone_2.set(370,264,408,318);
		zone_0_mzone_3.set(435,264,475,318);
		zone_0_mzone_4.set(502,264,540,318);
		zone_0_grave.set(570,264,610,318);
		zone_0_remove.set(570,207,610,257);
		//从左到右
		zone_0_hand.set(169, 386, 610, 436);
		
		zone_1_deck.set(170,63,210,119);
		zone_1_szone_4.set(235, 63, 275, 119);
		zone_1_szone_3.set(300, 63, 340, 119);
		zone_1_szone_2.set(370, 63, 408, 119);
		zone_1_szone_1.set(435, 63, 475, 119);
		zone_1_szone_0.set(502, 63, 540, 119);
		zone_1_extra.set(570,63,610,119);
		zone_1_grave.set(170,124,210,180);
		zone_1_mzone_4.set(235,124,275,180);
		zone_1_mzone_3.set(300,124,340,180);
		zone_1_mzone_2.set(370,124,408,180);
		zone_1_mzone_1.set(435,124,475,180);
		zone_1_mzone_0.set(502,124,540,180);
		zone_1_szone_5.set(570,124,610,180);
		zone_1_remove.set(170,184,210,234);
		//从右到左
		zone_1_hand.set(169,5,609,55);
		
		zones.add(zone_0_deck);
		zones.add(zone_0_grave);
		zones.add(zone_0_extra);
		zones.add(zone_0_remove);
		zones.add(zone_0_mzone_0);
		zones.add(zone_0_mzone_1);
		zones.add(zone_0_mzone_2);
		zones.add(zone_0_mzone_3);
		zones.add(zone_0_mzone_4);
		zones.add(zone_0_szone_0);
		zones.add(zone_0_szone_1);
		zones.add(zone_0_szone_2);
		zones.add(zone_0_szone_3);
		zones.add(zone_0_szone_4);
		zones.add(zone_0_szone_5);
		zones.add(zone_0_hand);
		
		zones.add(zone_1_deck);
		zones.add(zone_1_grave);
		zones.add(zone_1_extra);
		zones.add(zone_1_remove);
		zones.add(zone_1_mzone_0);
		zones.add(zone_1_mzone_1);
		zones.add(zone_1_mzone_2);
		zones.add(zone_1_mzone_3);
		zones.add(zone_1_mzone_4);
		zones.add(zone_1_szone_0);
		zones.add(zone_1_szone_1);
		zones.add(zone_1_szone_2);
		zones.add(zone_1_szone_3);
		zones.add(zone_1_szone_4);
		zones.add(zone_1_szone_5);	
		zones.add(zone_1_hand);
		
		rect_all.set(0, 0, 800, 442);
		rect_desc.set(3,230, 163, 380);
	}	
	//get card return null if there is not a card
	private Card getCard(float x,float y){
		Card c = null;
		//TODO test
		c = new Card(cardDatas.get(135598), cardStrings.get(135598), cardPics.get(135598));		
		return c;
	}
	//get loc
	private CardLocation getLocation(float x,float y){
		CardLocation loc = CardLocation.LOCATION_OFFFIELD;
		for(Zone z : zones){
			if(z.getRect().contains((int)x,(int)y)){
				loc = z.getLoc();
				break;
			}
		}
		return loc;
	}
	//get rect return null if no matches
	private Rect getRect(float x,float y){
		Rect tmp = null;
		for(Zone z : zones){
			if(z.getRect().contains((int)x,(int)y)){
				tmp = z.getRect();
				break;
			}
		}
		return tmp;
	}
	//rotate
	private Rect rotate(Rect rect){
		int l = rect.left;
		int t = rect.top;
		int r = rect.right;
		int b = rect.bottom;
		int w = r-l;
		int h = b-t;
		return new Rect(l-w/5,t+h/6,l-w/5+b-t,t+h/6+r-l);
	}
	
	private void test(){
		Card c1 = new Card(cardDatas.get(1), cardStrings.get(1), cardPics.get(1));
		Card c131182 = new Card(cardDatas.get(131182), cardStrings.get(131182), cardPics.get(131182));
		Card c135598 = new Card(cardDatas.get(135598), cardStrings.get(135598), cardPics.get(135598));
		Card c168917 = new Card(cardDatas.get(168917), cardStrings.get(168917), cardPics.get(168917));
		Card c176392 = new Card(cardDatas.get(176392), cardStrings.get(176392), cardPics.get(176392));
		Card c191749 = new Card(cardDatas.get(191749), cardStrings.get(191749), cardPics.get(191749));
		Card c218704 = new Card(cardDatas.get(218704), cardStrings.get(218704), cardPics.get(218704));
		Card c27551 = new Card(cardDatas.get(27551), cardStrings.get(27551), cardPics.get(27551));
		Card c50755 = new Card(cardDatas.get(50755), cardStrings.get(50755), cardPics.get(50755));
		
		c1.setCardZone(zone_0_remove);
		cardsBanishPlayer00.addCard(c1);
		
		c131182.setCardZone(zone_0_deck);
		c176392.setCardZone(zone_0_deck);
		c218704.setCardZone(zone_0_deck);
		c27551.setCardZone(zone_0_deck);
		cardsDeckPlayer00.addCard(c131182);
		cardsDeckPlayer00.addCard(c176392);
		cardsDeckPlayer00.addCard(c218704);
		cardsDeckPlayer00.addCard(c27551);
		
		c176392.setCardZone(zone_0_hand);
		c218704.setCardZone(zone_0_hand);
		c27551.setCardZone(zone_0_hand);
		cardsHandPlayer00.addCard(c176392);
		cardsHandPlayer00.addCard(c218704);
		cardsHandPlayer00.addCard(c27551);
		
		c135598.setCardZone(zone_0_mzone_2);
		cardsMZONEPlayer00.addCard(c135598);
		
		c50755.setCardZone(zone_0_szone_1);
		cardsMZONEPlayer00.addCard(c50755);
		
		c27551.setCardZone(zone_0_extra);
		cardsExtraPlayer00.addCard(c27551);
		
		c27551.setCardZone(zone_0_szone_5);
		cardsSTPlayer00.addCard(c27551);
		
		c191749.setCardZone(zone_1_extra);
		cardsExtraPlayer01.addCard(c191749);
		
		c168917.setCardZone(zone_1_grave);
		cardsGravePlayer01.addCard(c168917);
		
		c168917.setCardZone(zone_1_hand);
		cardsHandPlayer01.addCard(c168917);
		cardsHandPlayer01.addCard(c168917);
		cardsHandPlayer01.addCard(c168917);
		cardsHandPlayer01.addCard(c168917);
		cardsHandPlayer01.addCard(c168917);
		cardsHandPlayer01.addCard(c168917);
		cardsHandPlayer01.addCard(c168917);
		cardsHandPlayer01.addCard(c168917);
		cardsHandPlayer01.addCard(c168917);
		cardsHandPlayer01.addCard(c168917);
	}
	//inital
	public void initialCardGroup(){
		
		cardsBanishPlayer00 = new CardGroup(CARD_BANISH_MAX_SIZE );
		cardsDeckPlayer00 = new CardGroup(CARD_DECK_MAX_SIZE );
		cardsExtraPlayer00 = new CardGroup(CARD_EXTRA_MAX_SIZE );
		cardsGravePlayer00 = new CardGroup(CARD_GRAVE_MAX_SIZE );
		cardsHandPlayer00 = new CardGroup(CARD_HAND_MAX_SIZE );
		cardsMZONEPlayer00 = new CardGroup(CARD_MZONE_MAXSIZE );
		cardsSTPlayer00 = new CardGroup(CARD_SZONE_MAXSIZE );;
		
		cardsBanishPlayer01 = new CardGroup(CARD_BANISH_MAX_SIZE );
		cardsDeckPlayer01 = new CardGroup(CARD_DECK_MAX_SIZE );
		cardsExtraPlayer01 = new CardGroup(CARD_EXTRA_MAX_SIZE );
		cardsGravePlayer01 = new CardGroup(CARD_GRAVE_MAX_SIZE );
		cardsHandPlayer01 = new CardGroup(CARD_HAND_MAX_SIZE );
		cardsMZONEPlayer01 = new CardGroup(CARD_MZONE_MAXSIZE );
		cardsSTPlayer01 = new CardGroup(CARD_SZONE_MAXSIZE );
	}
	
	//onTouch
	public Rect[] onTouchEvent(MotionEvent event){
		float selX = event.getX();
		float selY = event.getY();
		cardOnShow = getCard(selX,selY);
		cardOnShow.setCardZone(zone_on_show);
		return new Rect[]{zone_on_show.getRect(),rect_desc};
	}
	
	//draw
	public void onDraw(Canvas canvas){
		//background
		canvas.drawBitmap(bgImg, null,rect_all, pen);
		//card lines
		canvas.drawBitmap(bgCardImg, null,rect_all, pen);
		//draw cardOnShow		
		if(cardOnShow!=null){
			canvas.drawBitmap(cardOnShow.getCardPic(), null, cardOnShow.getRect(), pen);
		}
		else
		{						
			//bmp = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.cover)).getBitmap();
			canvas.drawBitmap(coverImg,null,zone_on_show.getRect(), pen);
		}
		//draw desc
	    String name = cardOnShow.getName();
	    String attr = cardOnShow.getAttr();
	    int level =cardOnShow.getLevel();
	    int atk = cardOnShow.getAttack();
	    int def = cardOnShow.getDefence();
	    String race = cardOnShow.getRace();
	    String desc = cardOnShow.getDesc();
	    String text = null;
	    TextUtil txtUtil = null;
	    //name
	    Log.d(TAG,"name:"+name);
	    text = name;
	    txtUtil = new TextUtil(text, 5, 250,	150, 20, Color.WHITE, Color.BLACK, 0, 15);
	    txtUtil.InitText();
	    txtUtil.DrawText(canvas);
	    //attr
	    text = attr + "  LV:" + level + "  ATK:" + atk + " /DEF:" + def;
	    Log.d(TAG,"attr:"+text);
	    txtUtil = new TextUtil(text, 5, 270, 150, 15, Color.WHITE, Color.BLACK, 0, 10);
	    txtUtil.InitText();
	    txtUtil.DrawText(canvas);
	    //race
	    text = "[" + race + "]";
	    Log.d(TAG,"race:"+race);
	    txtUtil = new TextUtil(text, 5, 285, 150, 15, Color.WHITE, Color.BLACK, 0, 10);
	    txtUtil.InitText();
	    txtUtil.DrawText(canvas);
	    //counter
	    //TODO
	    //desc
	    text = "描述";
	    txtUtil = new TextUtil(text, 5, 325, 150, 15, Color.WHITE, Color.BLACK, 0, 10);
	    txtUtil.InitText();
	    txtUtil.DrawText(canvas);
	    text = desc;
	    Log.d(TAG,"desc:"+desc);
	    txtUtil = new TextUtil(text, 5, 340, 150, 100, Color.WHITE, Color.BLACK, 0, 10);
	    txtUtil.InitText();
	    txtUtil.DrawText(canvas);
	    //EXTRA
	    if(cardsExtraPlayer00!=null &&cardsExtraPlayer01!=null){
	    	if(cardsExtraPlayer00.getCount() > 0){
	    		canvas.drawBitmap(coverImg, null, zone_0_extra.getRect(), pen);
	    	}
	    	if(cardsExtraPlayer01.getCount() > 0){
	    		canvas.drawBitmap(coverImg, null, zone_1_extra.getRect(), pen);
	    	}
	    }
	    //GRAVE
	    if(cardsGravePlayer00!=null &&cardsGravePlayer01!=null){
	    	if(cardsGravePlayer00.getCount() > 0){
	    		canvas.drawBitmap(cardsGravePlayer00.getLastCard().getCardPic(), null, zone_0_grave.getRect(), pen);
	    	}
	    	if(cardsGravePlayer01.getCount() > 0){
	    		canvas.drawBitmap(cardsGravePlayer01.getLastCard().getCardPic(), null, zone_1_grave.getRect(), pen);
	    	}
	    }
	    //DECK
	    if(cardsDeckPlayer00!=null &&cardsDeckPlayer01!=null){
	    	if(cardsDeckPlayer00.getCount() > 0){
	    		canvas.drawBitmap(coverImg, null, zone_0_deck.getRect(), pen);
	    	}
	    	if(cardsDeckPlayer01.getCount() > 0){
	    		canvas.drawBitmap(coverImg, null, zone_1_deck.getRect(), pen);
	    	}
	    }
	    //REMOVE BANNISH
	    if(cardsBanishPlayer00!=null &&cardsBanishPlayer01!=null){
	    	if(cardsBanishPlayer00.getCount() > 0){
	    		canvas.drawBitmap(coverImg, null, zone_0_remove.getRect(), pen);
	    	}
	    	if(cardsBanishPlayer01.getCount() > 0){
	    		canvas.drawBitmap(coverImg, null, zone_1_remove.getRect(), pen);
	    	}
	    }
	    //MZONE
	    if(cardsMZONEPlayer00!=null &&cardsMZONEPlayer01!=null){
	    	if(cardsMZONEPlayer00.getCount() > 0){
	    		for(Card c : cardsMZONEPlayer00.getCards()){
	    			canvas.drawBitmap(c.getCardPic(), null, c.getRect(), pen);
	    		}
	    	}
	    	if(cardsMZONEPlayer01.getCount() > 0){
	    		for(Card c : cardsMZONEPlayer01.getCards()){
	    			canvas.drawBitmap(c.getCardPic(), null, c.getRect(), pen);
	    		}
	    	}
	    }
	    //SZONE
	    if(cardsSTPlayer00!=null &&cardsSTPlayer01!=null){
	    	if(cardsSTPlayer00.getCount() > 0){
	    		for(Card c : cardsSTPlayer00.getCards()){
	    			canvas.drawBitmap(c.getCardPic(), null, c.getRect(), pen);
	    		}
	    	}
	    	if(cardsSTPlayer01.getCount() > 0){
	    		for(Card c : cardsSTPlayer00.getCards()){
	    			canvas.drawBitmap(c.getCardPic(), null, c.getRect(), pen);
	    		}
	    	}
	    }
	    //Hand
	    if(cardsHandPlayer00!=null &&cardsHandPlayer01!=null){
	    	int n00 = cardsHandPlayer00.getCount() ;
	    	int n01= cardsHandPlayer01.getCount();
	    	if(n00> 0){	    		
	    		int left = zone_0_hand.getRect().left;
	    		int top = zone_0_hand.getRect().top;
	    		int right = zone_0_hand.getRect().right;
	    		int bottom = zone_0_hand.getRect().bottom;
	    		int seam = (right-left-smallCardWidth)/(n00-1);
	    		if(seam > smallCardWidth){
	    			seam = smallCardWidth +1;
	    		}
	    		int i = left;
	    		for(Card c : cardsHandPlayer00.getCards()){
	    			canvas.drawBitmap(c.getCardPic(), null,new Rect(i, top, i + smallCardWidth, bottom), pen);
	    			 i += seam;
	    		}
	    	}
	    	if(n01> 0){
	    		int left = zone_1_hand.getRect().left;
	    		int top = zone_1_hand.getRect().top;
	    		int right = zone_1_hand.getRect().right;
	    		int bottom = zone_1_hand.getRect().bottom;
	    		int seam = (right-left-smallCardWidth)/(n01-1);
	    		if(seam > smallCardWidth){
	    			seam = smallCardWidth +1;
	    		}
	    		int i = right;
	    		for(Card c : cardsHandPlayer01.getCards()){
	    			canvas.drawBitmap(c.getCardPic(), null,new Rect(i - smallCardWidth, top, i  , bottom), pen);
	    			 i -= seam;
	    		}
	    	}
	    }    
	}
}
