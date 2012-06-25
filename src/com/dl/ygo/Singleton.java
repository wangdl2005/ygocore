package com.dl.ygo;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import com.dl.ocg.CardDataC;
import com.dl.ocg.CardString;
import com.dl.ocg.Zone;
import com.dl.ocg.Common.*;
import com.dl.ygo.R.drawable;

import static com.dl.ocg.Common.*;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class Singleton {
	//单态
	private static Singleton instance = null;

	private Timer timer = new Timer(); 
	private Handler handler = new Handler(){ 
		public void handleMessage(Message msg) { 
				switch (msg.what) { 
					case 1: 
					{
						if(cardOnShow != null)
						{
							String name = cardOnShow.getName();
						    String attr = cardOnShow.getAttr();
						    int level =cardOnShow.getLevel();
						    int atk = cardOnShow.getAttack();
						    int def = cardOnShow.getDefence();
						    String race = cardOnShow.getRace();
						    String desc = cardOnShow.getDesc();
						    String loc = FormatLocation(cardOnShow.getLoc().getValue());
							StringBuilder text = new StringBuilder();
							text.append(name + "\n");
							text.append(attr + "  LV:" + level + "  ATK:" + atk + " /DEF:" + def + "\n");
							text.append("[" + race + "]" + "\n");
							text.append("位置: " + loc + "    原拥有者:"+"Player00" + "\n");
							//TODO
							text.append("\n");
							text.append("描述:\n");
							text.append(desc);
							if(view != null){
								TextView txtDesc = (TextView) view.findViewById(R.id.txtDesc);
								txtDesc.setText(text);
							}
						}
						
						TextView txtMessage =(TextView) view.findViewById(R.id.txtMessage);
						txtMessage.setText(message);
						break; 
					}
				} 
			super.handleMessage(msg); 
			} 
	};
	private TimerTask task = new TimerTask() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Message message = new Message(); 
			message.what = 1; 
			handler.sendMessage(message); 	
		}
	};
	private Singleton(View  view){
		this.context = view.getContext();
		this.view = view;
		init();
	}
	public static synchronized Singleton getInstance(View view){
		if(instance == null){
			instance = new Singleton(view);
		}	
		return instance;
	}
	private void init() {
		pen = new Paint();
		readData();
		setZone();
		bgImg = textures.get("bg");
		bgCardImg = textures.get("bg_card");
		coverImg = textures.get("cover");
		//button
		btn_dp = new MyButton(rect_dp,textures.get("dp"));
		btn_sp = new MyButton(rect_sp,textures.get("sp"));
		btn_m1 = new MyButton(rect_m1,textures.get("m1"));
		btn_bp = new MyButton(rect_bp,textures.get("bp"));
		btn_m2 = new MyButton(rect_m2,textures.get("m2"));
		btn_ep = new MyButton(rect_ep,textures.get("ep"));
		btn_coin = new MyButton(rect_coin,textures.get("coin"));
		btn_dice = new MyButton(rect_dice,textures.get("dice"));
		btn_token = new MyButton(rect_token,textures.get("token"));
		btn_lp = new MyButton(rect_lp,textures.get("lp"));
		btn_endTurn = new MyButton(rect_endTurn,textures.get("endTurn"));
		buttons.add(btn_dp);
		buttons.add(btn_sp);
		buttons.add(btn_m1);
		buttons.add(btn_bp);
		buttons.add(btn_m2);
		buttons.add(btn_ep);
		buttons.add(btn_coin);
		buttons.add(btn_dice);
		buttons.add(btn_token);
		buttons.add(btn_lp);
		buttons.add(btn_endTurn);
		//player 		
		CardGroup emptyCardGroup = new CardGroup(0);
		player00 = new Player(LP_MAX, false, emptyCardGroup, emptyCardGroup
				, emptyCardGroup, emptyCardGroup, emptyCardGroup
				, emptyCardGroup, emptyCardGroup,rect_player00_lp);
		player01 = new Player(LP_MAX, false, emptyCardGroup, emptyCardGroup
				, emptyCardGroup, emptyCardGroup, emptyCardGroup
				, emptyCardGroup, emptyCardGroup,rect_player01_lp);
		cardOnShow = new Card(cardDatas, cardStrings, cardPics, 1);
		initialCardGroup();
		test();
		//timer.schedule(task,0,100);
	}
	//View
	private View view;
	
	//
	private Player player00;
	private Player player01;
	private Card cardOnShow;
	

	private static Bitmap bgImg;
	private static Bitmap bgCardImg;
	private Bitmap coverImg;
	
	private Paint pen;
	private Context context;
	private String message = "";
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
	private static final int LP_MAX = 8000;
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
	private Rect rect_message = new Rect();
	//按钮区域
	private Rect rect_dp=new Rect();
	private Rect rect_sp =new Rect();
	private Rect rect_m1 =new Rect();
	private Rect rect_bp =new Rect();
	private Rect rect_m2 =new Rect();
	private Rect rect_ep =new Rect();
	
	private Rect rect_dice =new Rect();
	private Rect rect_coin =new Rect();
	private Rect rect_token =new Rect();
	private Rect rect_lp =new Rect();
	private Rect rect_endTurn =new Rect();
	private Rect rect_player00_lp = new Rect();
	private Rect rect_player01_lp = new Rect();
	
	
	private MyButton btn_dp;
	private MyButton btn_sp ;
	private MyButton btn_m1 ;
	private MyButton btn_bp ;
	private MyButton btn_m2 ;
	private MyButton btn_ep ;
	
	private MyButton btn_dice ;
	private MyButton btn_coin ;
	private MyButton btn_token ;
	private MyButton btn_lp ;
	private MyButton btn_endTurn ;

	private ArrayList<MyButton> buttons = new ArrayList<MyButton>();
	
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
		rect_desc.set(3,245, 163, 442);
		rect_message.set(618,93,793,315);
		
		rect_dp.set(212,182,270,220);
		rect_sp.set(270,182,328,220);
		rect_m1.set(328,182,386,220);
		rect_bp.set(386,182,444,220);
		rect_m2.set(444,182,502,220);
		rect_ep.set(502,182,560,220);
		rect_coin.set(230,220,267,257);
		rect_dice.set(280,220,317,257);
		rect_token.set(338,220,375,257);
		rect_lp.set(396,220,456,257);
		rect_endTurn.set(480,220,560,257);
		
		//
		rect_player01_lp.set(618,5,793,55);
		rect_player00_lp.set(618,385,793,435);
	}	
	//get card return null if there is not a card
	private Card getCard(float x,float y){
		Card c = null;
		//TODO test
		CardLocation cL = getLocation(x,y);
		if(cL == CardLocation.LOCATION_MZONE){
			for(Card card : player00.getCardsMZONE().getCards()){
				if(card.getRect().contains((int)x,(int) y))
				{
					c = card;
					break;
				}
			}
			for(Card card : player01.getCardsMZONE().getCards()){
				if(card.getRect().contains((int)x,(int) y))
				{
					c = card;
					break;
				}
			}
		}
		if(cL == CardLocation.LOCATION_SZONE){
			for(Card card : player00.getCardsST().getCards()){
				if(card.getRect().contains((int)x,(int) y))
				{
					c = card;
					break;
				}
			}
			for(Card card : player01.getCardsST().getCards()){
				if(card.getRect().contains((int)x,(int) y))
				{
					c = card;
					break;
				}
			}
		}
		if(cL == CardLocation.LOCATION_HAND){
			for(Card card : player00.getCardsHand().getCards()){
				if(card.getRect().contains((int)x,(int) y))
				{
					c = card;
					break;
				}
			}
			for(Card card : player01.getCardsHand().getCards()){
				if(card.getRect().contains((int)x,(int) y))
				{
					c = card;
					break;
				}
			}
		}
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
//	private Rect getRect(float x,float y){
//		Rect tmp = null;
//		for(Zone z : zones){
//			if(z.getRect().contains((int)x,(int)y)){
//				tmp = z.getRect();
//				break;
//			}
//		}
//		return tmp;
//	}
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
		Card c1 = new Card(cardDatas,cardStrings,cardPics,1);
		Card c2 =new Card(cardDatas,cardStrings,cardPics,131182);
		Card c3 = new Card(cardDatas,cardStrings,cardPics,135598);
		Card c4 =new Card(cardDatas,cardStrings,cardPics,168917);
		Card c5 = new Card(cardDatas,cardStrings,cardPics,176392);
		Card c6 = new Card(cardDatas,cardStrings,cardPics,191749);
		Card c7 = new Card(cardDatas,cardStrings,cardPics,218704);
		Card c8 =new Card(cardDatas,cardStrings,cardPics,27551);
		Card c9 = new Card(cardDatas,cardStrings,cardPics,50755);
		Card c10 = new Card(cardDatas,cardStrings,cardPics,135598);
		Card c11 =new Card(cardDatas,cardStrings,cardPics,168917);
		Card c12 = new Card(cardDatas,cardStrings,cardPics,176392);
		Card c13 = new Card(cardDatas,cardStrings,cardPics,191749);
		Card c14 = new Card(cardDatas,cardStrings,cardPics,218704);
		Card c15 =new Card(cardDatas,cardStrings,cardPics,27551);
		Card c16 = new Card(cardDatas,cardStrings,cardPics,50755);
		Card c17 =new Card(cardDatas,cardStrings,cardPics,168917);
		Card c18 = new Card(cardDatas,cardStrings,cardPics,176392);
		Card c19 =new Card(cardDatas,cardStrings,cardPics,27551);
		Card c20 = new Card(cardDatas,cardStrings,cardPics,50755);
		Card c21 =new Card(cardDatas,cardStrings,cardPics,168917);
		Card c22 = new Card(cardDatas,cardStrings,cardPics,176392);
		Card c23 =new Card(cardDatas,cardStrings,cardPics,168917);
		
		c1.setCardZone(zone_0_remove);
		player00.getCardsBanish().addCard(c1);
		
		c2.setCardZone(zone_0_deck);
		c3.setCardZone(zone_0_deck);
		c4.setCardZone(zone_0_deck);
		c5.setCardZone(zone_0_deck);
		player00.getCardsDeck().addCard(c2);
		player00.getCardsDeck().addCard(c3);
		player00.getCardsDeck().addCard(c4);
		player00.getCardsDeck().addCard(c5);
		player00.getCardsDeck().addCard(c19);
		player00.getCardsDeck().addCard(c20);
		player00.getCardsDeck().addCard(c21);
		int n =20;
		while(n > 0){
			player00.getCardsDeck().addCard(c22);
			--n;
		}
		
		n = 40;
		while(n > 0){
			player01.getCardsDeck().addCard(c23);
			--n;
		}
		
		c6.setCardZone(zone_0_hand);
		c7.setCardZone(zone_0_hand);
		c8.setCardZone(zone_0_hand);
		player00.getCardsHand().addCard(c6);
		player00.getCardsHand().addCard(c7);
		player00.getCardsHand().addCard(c8);
		
		c9.setCardZone(zone_0_mzone_2);
		player00.getCardsMZONE().addCard(c9);
		
		c10.setCardZone(zone_0_szone_1);
		player00.getCardsST().addCard(c10);
		
		c11.setCardZone(zone_0_extra);
		n = 15;
		while(n > 0){
			player00.getCardsExtra().addCard(c11);
			--n;
		}
		
		c12.setCardZone(zone_0_szone_5);
		player00.getCardsST().addCard(c12);
		
		c13.setCardZone(zone_1_extra);
		n = 15;
		while(n > 0){
			player01.getCardsExtra().addCard(c13);
			--n;
		}
		
		c14.setCardZone(zone_1_grave);
		player01.getCardsGrave().addCard(c14);
		
		c15.setCardZone(zone_1_hand);
		player01.getCardsHand().addCard(c15);
		c17.setCardZone(zone_1_hand);
		player01.getCardsHand().addCard(c17);
		c16.setCardZone(zone_1_hand);
		player01.getCardsHand().addCard(c16);
		c18.setCardZone(zone_1_hand);
		player01.getCardsHand().addCard(c18);
	}
	//inital
	public void initialCardGroup(){
		
		player00.setCardsBanish(new CardGroup(CARD_BANISH_MAX_SIZE ));
		player00.setCardsDeck( new CardGroup(CARD_DECK_MAX_SIZE ));
		player00.setCardsExtra( new CardGroup(CARD_EXTRA_MAX_SIZE ));
		player00.setCardsGrave( new CardGroup(CARD_GRAVE_MAX_SIZE ));
		player00.setCardsHand( new CardGroup(CARD_HAND_MAX_SIZE ));
		player00.setCardsMZONE( new CardGroup(CARD_MZONE_MAXSIZE ));
		player00.setCardsST( new CardGroup(CARD_SZONE_MAXSIZE ));;
		
		player01.setCardsBanish( new CardGroup(CARD_BANISH_MAX_SIZE ));
		player01.setCardsDeck( new CardGroup(CARD_DECK_MAX_SIZE ));
		player01.setCardsExtra( new CardGroup(CARD_EXTRA_MAX_SIZE ));
		player01.setCardsGrave( new CardGroup(CARD_GRAVE_MAX_SIZE ));
		player01.setCardsHand( new CardGroup(CARD_HAND_MAX_SIZE ));
		player01.setCardsMZONE( new CardGroup(CARD_MZONE_MAXSIZE ));
		player01.setCardsST( new CardGroup(CARD_SZONE_MAXSIZE ));
	}
	
	//onTouch
	public ArrayList<Rect> onTouchEvent(MotionEvent event){

		final ArrayList<Rect> rectInvaliate = new ArrayList<Rect>();
		float selX = event.getX();
		float selY = event.getY();
		switch (event.getAction()) 
		{
			case MotionEvent.ACTION_DOWN:
		    {
					cardOnShow = getCard(selX,selY);
					
//					//button
//					if(btn_lp.getRect().contains((int)selX, (int)selY))
//					{
//						AlertDialog.Builder dlg = new AlertDialog.Builder(context);
//						final EditText editText =  new EditText(context);
//						editText.setId(100);
//						dlg.setTitle("LP:").setIcon(
//							     android.R.drawable.ic_dialog_info).setView(editText).setPositiveButton("确定", new DialogInterface.OnClickListener() {
//									
//									@Override
//									public void onClick(DialogInterface dialog, int which) {
//										// TODO Auto-generated method stub
//										String text = editText.getText().toString();
//										try{
//											int i = 0;
//											i = Integer.parseInt(text);
//											player00.setLP(player00.getLP() + i);
//											rectInvaliate.add(player00.getLpRect());
//										}
//										catch (NumberFormatException e) {
//											e.printStackTrace();
//										}
//										Log.d("YGO",text);
//									}
//								}
//							    		 )
//							     .setNegativeButton("取消", null).show();	
//					}
//					for(MyButton btn : buttons){
//						if(btn.getRect().contains((int)selX, (int)selY)){
//							//button work
//							message = btn.toString() + " is working";
//							break;
//						}
//						else
//						{
//							message = "";
//						}
//					}
			      break;
			    }
        }
        
		rectInvaliate.add(rect_desc);
		rectInvaliate.add(zone_on_show.getRect());
		//button
		rectInvaliate.add(new Rect(626, 75,	876, 90));
		
		return rectInvaliate;
	}
	//draw
	public void onDraw(Canvas canvas){
		//background
		canvas.drawBitmap(bgImg, null,rect_all, pen);
		//card lines
		canvas.drawBitmap(bgCardImg, null,rect_all, pen);
		//draw cardOnShow		
		if(cardOnShow!=null){
			canvas.drawBitmap(cardOnShow.getCardPic(), null, zone_on_show.getRect(), pen);
		}
		else
		{						
			//bmp = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.cover)).getBitmap();
			canvas.drawBitmap(coverImg,null,zone_on_show.getRect(), pen);
		}
		//draw desc
//		if(cardOnShow!=null)
//		{
//		    String name = cardOnShow.getName();
//		    String attr = cardOnShow.getAttr();
//		    int level =cardOnShow.getLevel();
//		    int atk = cardOnShow.getAttack();
//		    int def = cardOnShow.getDefence();
//		    String race = cardOnShow.getRace();
//		    String desc = cardOnShow.getDesc();
//		    String loc = FormatLocation(cardOnShow.getLoc().getValue());
//		    String text = null;
//		    TextUtil txtUtil = null;
//		    //name
//		    Log.d(TAG,"name:"+name);
//		    text = name;
//		    txtUtil = new TextUtil(text, 5, 250,	150, 20, Color.WHITE, Color.BLACK, 0, 15);
//		    txtUtil.InitText();
//		    txtUtil.DrawText(canvas);
//		    //attr
//		    text = attr + "  LV:" + level + "  ATK:" + atk + " /DEF:" + def;
//		    Log.d(TAG,"attr:"+text);
//		    txtUtil = new TextUtil(text, 5, 270, 150, 15, Color.WHITE, Color.BLACK, 0, 10);
//		    txtUtil.InitText();
//		    txtUtil.DrawText(canvas);
//		    //race
//		    text = "[" + race + "]";
//		    Log.d(TAG,"race:"+race);
//		    txtUtil = new TextUtil(text, 5, 285, 150, 15, Color.WHITE, Color.BLACK, 0, 10);
//		    txtUtil.InitText();
//		    txtUtil.DrawText(canvas);
//		    //位置，counter
//		    //TODO 原拥有者
//		    text = "位置: " + loc + "    原拥有者:"+"Player00";
//		    Log.d(TAG,"位置:"+text);
//		    txtUtil = new TextUtil(text, 5, 300, 150, 15, Color.WHITE, Color.BLACK, 0, 10);
//		    txtUtil.InitText();
//		    txtUtil.DrawText(canvas);
//		    //TODO counter
//		    //desc
//		    text = "描述";
//		    txtUtil = new TextUtil(text, 5, 330, 150, 15, Color.WHITE, Color.BLACK, 0, 10);
//		    txtUtil.InitText();
//		    txtUtil.DrawText(canvas);
//		    text = desc;
//		    Log.d(TAG,"desc:"+desc);
//		    txtUtil = new TextUtil(text, 5, 345, 150, 100, Color.WHITE, Color.BLACK, 0, 10);
//		    txtUtil.InitText();
//		    txtUtil.DrawText(canvas);
//		}
	    //EXTRA
	    if(player00.getCardsExtra()!=null &&player01.getCardsExtra()!=null){
	    	if(player00.getCardsExtra().getCount() > 0){
	    		int n = player00.getCardsExtra().getCount() /5 + 1;
	    		int bottom = zone_0_extra.getRect().bottom;
	    		int top = zone_0_extra.getRect().top;
	    		int left = zone_0_extra.getRect().left;
	    		int right = zone_0_extra.getRect().right;
	    		for(int i=0;i<n;++i){
	    			canvas.drawBitmap(coverImg, null, new Rect(left-i,top-i,right-i,bottom-i), pen);	    	
	    		}
	    	}
	    	if(player01.getCardsExtra().getCount() > 0){
	    		int n = player01.getCardsExtra().getCount() /5 + 1;
	    		int bottom = zone_1_extra.getRect().bottom;
	    		int top = zone_1_extra.getRect().top;
	    		int left = zone_1_extra.getRect().left;
	    		int right = zone_1_extra.getRect().right;
	    		for(int i=0;i<n;++i){
	    			canvas.drawBitmap(coverImg, null, new Rect(left+i,top-i,right+i,bottom-i), pen);	    	
	    		}
	    	}
	    }
	    //GRAVE
	    if(player00.getCardsGrave()!=null &&player01.getCardsGrave()!=null){
	    	if(player00.getCardsGrave().getCount() > 0){
	    		canvas.drawBitmap(player00.getCardsGrave().getLastCard().getCardPic(), null, zone_0_grave.getRect(), pen);
	    	}
	    	if(player01.getCardsGrave().getCount() > 0){
	    		canvas.drawBitmap(player01.getCardsGrave().getLastCard().getCardPic(), null, zone_1_grave.getRect(), pen);
	    	}
	    }
	    //DECK
	    if(player00.getCardsDeck()!=null &&player01.getCardsDeck()!=null){
	    	if(player00.getCardsDeck().getCount() > 0){
	    		int n = player00.getCardsDeck().getCount() /5 + 1;
	    		int bottom = zone_0_deck.getRect().bottom;
	    		int top = zone_0_deck.getRect().top;
	    		int left = zone_0_deck.getRect().left;
	    		int right = zone_0_deck.getRect().right;
	    		for(int i=0;i<n;++i){
	    			canvas.drawBitmap(coverImg, null, new Rect(left+i,top-i,right+i,bottom-i), pen);	    	
	    		}
	    	}
	    	if(player01.getCardsDeck().getCount() > 0){
	    		int n = player01.getCardsDeck().getCount() /5 + 1;
	    		int bottom = zone_1_deck.getRect().bottom;
	    		int top = zone_1_deck.getRect().top;
	    		int left = zone_1_deck.getRect().left;
	    		int right = zone_1_deck.getRect().right;
	    		for(int i=0;i<n;++i){
	    			canvas.drawBitmap(coverImg, null, new Rect(left-i,top-i,right-i,bottom-i), pen);	    	
	    		}
	    	}
	    }
	    //REMOVE BANNISH
	    if(player00.getCardsBanish()!=null &&player01.getCardsBanish()!=null){
	    	if(player00.getCardsBanish().getCount() > 0){
	    		canvas.drawBitmap(player00.getCardsBanish().getLastCard().getCardPic(), null, zone_0_remove.getRect(), pen);
	    	}
	    	if(player01.getCardsBanish().getCount() > 0){
	    		canvas.drawBitmap(player01.getCardsBanish().getLastCard().getCardPic(), null, zone_1_remove.getRect(), pen);
	    	}
	    }
	    //MZONE
	    if(player00.getCardsMZONE()!=null &&player01.getCardsMZONE()!=null){
	    	if(player00.getCardsMZONE().getCount() > 0){
	    		for(Card c : player00.getCardsMZONE().getCards()){
	    			canvas.drawBitmap(c.getCardPic(), null, c.getRect(), pen);
	    		}
	    	}
	    	if(player01.getCardsMZONE().getCount() > 0){
	    		for(Card c : player01.getCardsMZONE().getCards()){
	    			canvas.drawBitmap(c.getCardPic(), null, c.getRect(), pen);
	    		}
	    	}
	    }
	    //SZONE
	    if(player00.getCardsST()!=null &&player01.getCardsST()!=null){
	    	if(player00.getCardsST().getCount() > 0){
	    		for(Card c : player00.getCardsST().getCards()){
	    			canvas.drawBitmap(c.getCardPic(), null, c.getRect(), pen);
	    		}
	    	}
	    	if(player01.getCardsST().getCount() > 0){
	    		for(Card c : player01.getCardsST().getCards()){
	    			canvas.drawBitmap(c.getCardPic(), null, c.getRect(), pen);
	    		}
	    	}
	    }
	    //Hand
	    if(player00.getCardsHand()!=null &&player01.getCardsHand()!=null){
	    	int n00 = player00.getCardsHand().getCount() ;
	    	int n01= player01.getCardsHand().getCount();
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
	    		int n = 0;
	    		for(Card c : player00.getCardsHand().getCards()){
	    			++n;
	    			canvas.drawBitmap(c.getCardPic(), null,new Rect(i, top, i + smallCardWidth, bottom), pen);
	    			if(n < n00){
	    				c.setRect(new Rect(i,top,i + seam-1, bottom));	    			
	    			}
	    			else
	    			{
	    				c.setRect(new Rect(i,top,i + smallCardWidth, bottom));	    
	    			}
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
	    		int n = 0;
	    		for(Card c : player01.getCardsHand().getCards()){
	    			++n;
	    			canvas.drawBitmap(c.getCardPic(), null,new Rect(i - smallCardWidth, top, i  , bottom), pen);
	    			if(n < n01){
	    				c.setRect(new Rect(i - seam + 1,top,i, bottom));	    			
	    			}
	    			else
	    			{
	    				c.setRect(new Rect(i -smallCardWidth ,top,i, bottom));	    
	    			}
	    			 i -= seam;
	    		}
	    	}
	    }    
	    // lp
	    if(player00 != null )
	    {
	    	TextUtil txtUtil = null;
	    	//player00
	    	Rect rect_player00 = player00.getLpRect();
	    	int left = rect_player00.left;
	    	int top = rect_player00.top;
	    	int right = rect_player00.right;
	    	int bottom = rect_player00.bottom;
	    	int w = rect_player00.width();
	    	int h = rect_player00.height();
	    	String lp = "LP: " + String.valueOf(player00.getLP());
		    Log.d(TAG,"player00 LP:"+lp);
		    txtUtil = new TextUtil(lp, left, bottom,	w, h, Color.WHITE, Color.BLACK, 0, h-10);
		    txtUtil.InitText();
		    txtUtil.DrawText(canvas);
	    }
	    if(player01 != null )
	    {
	    	TextUtil txtUtil = null;
	    	//player01
	    	Rect rect_player01 = player01.getLpRect();
	    	int left = rect_player01.left;
	    	int top = rect_player01.top;
	    	int right = rect_player01.right;
	    	int bottom = rect_player01.bottom;
	    	int w = rect_player01.width();
	    	int h = rect_player01.height();
	    	String lp =  "LP: " + String.valueOf(player01.getLP());
		    Log.d(TAG,"player01 LP:"+lp);
		    txtUtil = new TextUtil(lp, left, bottom,	w, h, Color.WHITE, Color.BLACK, 0, h-10);
		    txtUtil.InitText();
		    txtUtil.DrawText(canvas);
	    }
//	    //button
//	    canvas.drawBitmap(btn_dp.getBmp(), null, btn_dp.getRect(), pen);
//	    canvas.drawBitmap(btn_sp.getBmp(), null, btn_sp.getRect(), pen);
//	    canvas.drawBitmap(btn_m1.getBmp(), null, btn_m1.getRect(), pen);
//	    canvas.drawBitmap(btn_bp.getBmp(), null, btn_bp.getRect(), pen);
//	    canvas.drawBitmap(btn_m2.getBmp(), null, btn_m2.getRect(), pen);
//	    canvas.drawBitmap(btn_ep.getBmp(), null, btn_ep.getRect(), pen);
//	    canvas.drawBitmap(btn_coin.getBmp(), null, btn_coin.getRect(), pen);
//	    canvas.drawBitmap(btn_dice.getBmp(), null, btn_dice.getRect(), pen);
//	    canvas.drawBitmap(btn_token.getBmp(), null, btn_token.getRect(), pen);
//	    canvas.drawBitmap(btn_lp.getBmp(), null, btn_lp.getRect(), pen);
//	    canvas.drawBitmap(btn_endTurn.getBmp(), null, btn_endTurn.getRect(), pen);  
//	    

	}	
	
	public void onLayout()
	{
		TextView txtDesc = (TextView) view.findViewById(R.id.txtDesc);
		 if(txtDesc!=null)
		 {
			 txtDesc.setMovementMethod(ScrollingMovementMethod.getInstance()); 
			 Rect r = rect_desc;
			 txtDesc.layout(r.left,r.top,r.right,r.bottom);
		 }
		 TextView txtMessage = (TextView) view.findViewById(R.id.txtMessage);
		 if(txtMessage!=null)
		 {
			 txtDesc.setMovementMethod(ScrollingMovementMethod.getInstance()); 
			 txtMessage.setTextSize(10);
			 Rect r = rect_message;
			 txtMessage.layout(r.left,r.top,r.right,r.bottom);
		 }
		 
		 
		 ImageButton btnDp = (ImageButton) view.findViewById(R.id.btnDp);
		 if(btnDp!=null)
		 {
			 Rect r = btn_dp.getRect();
			 btnDp.layout(r.left,r.top,r.right,r.bottom);
			 btnDp.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch(event.getAction())
					{
						case (MotionEvent.ACTION_DOWN):						
						{
							message = "button dp is working";
						}
						break;
					}
					return false;
				}
			});
			 btnDp.setBackgroundDrawable(view.getResources().getDrawable(R.drawable.ic_dp));
		 }
		 ImageButton btnSp = (ImageButton) view.findViewById(R.id.btnSp);
		 if(btnSp!=null)
		 {
			 Rect r = btn_sp.getRect();
			 btnSp.layout(r.left,r.top,r.right,r.bottom);
			 btnSp.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch(event.getAction())
					{
						case (MotionEvent.ACTION_DOWN):						
						{
							message = "button sp is working";
						}
						break;
					}
					return false;
				}
			});
			 btnSp.setBackgroundDrawable(view.getResources().getDrawable(R.drawable.ic_sp));
		 }
		 ImageButton btnM1 = (ImageButton) view.findViewById(R.id.btnM1);
		 if(btnM1!=null)
		 {
			 Rect r = btn_m1.getRect();
			 btnM1.layout(r.left,r.top,r.right,r.bottom);
			 btnM1.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch(event.getAction())
					{
						case (MotionEvent.ACTION_DOWN):						
						{
							message = "button m1 is working";
						}
						break;
					}
					return false;
				}
			});
			 btnM1.setBackgroundDrawable(view.getResources().getDrawable(R.drawable.ic_m1));
		 }
		 ImageButton btnBp = (ImageButton) view.findViewById(R.id.btnBp);
		 if(btnBp!=null)
		 {
			 Rect r = btn_bp.getRect();
			 btnBp.layout(r.left,r.top,r.right,r.bottom);
			 btnBp.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch(event.getAction())
					{
						case (MotionEvent.ACTION_DOWN):						
						{
							message = "button bp is working";
						}
						break;
					}
					return false;
				}
			});
			 btnBp.setBackgroundDrawable(view.getResources().getDrawable(R.drawable.ic_bp));
		 }
		 ImageButton btnM2 = (ImageButton) view.findViewById(R.id.btnM2);
		 if(btnM2!=null)
		 {
			 Rect r = btn_m2.getRect();
			 btnM2.layout(r.left,r.top,r.right,r.bottom);
			 btnM2.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch(event.getAction())
					{
						case (MotionEvent.ACTION_DOWN):						
						{
							message = "button m2 is working";
						}
						break;
					}
					return false;
				}
			});
			 btnM2.setBackgroundDrawable(view.getResources().getDrawable(R.drawable.ic_m2));
		 }
		 ImageButton btnEp = (ImageButton) view.findViewById(R.id.btnEp);
		 if(btnEp!=null)
		 {
			 Rect r = btn_ep.getRect();
			 btnEp.layout(r.left,r.top,r.right,r.bottom);
			 btnEp.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch(event.getAction())
					{
						case (MotionEvent.ACTION_DOWN):						
						{
							message = "button ep is working";
						}
						break;
					}
					return false;
				}
			});
			 btnEp.setBackgroundDrawable(view.getResources().getDrawable(R.drawable.ic_ep));
		 }
		 ImageButton btnCoin = (ImageButton) view.findViewById(R.id.btnCoin);
		 if(btnCoin!=null)
		 {
			 Rect r = btn_coin.getRect();
			 btnCoin.layout(r.left,r.top,r.right,r.bottom);
			 btnCoin.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch(event.getAction())
					{
						case (MotionEvent.ACTION_DOWN):						
						{
							message = "button coin is working";
						}
						break;
					}
					return false;
				}
			});
			 btnCoin.setBackgroundDrawable(view.getResources().getDrawable(R.drawable.ic_coin));
		 }
		 ImageButton btnDice = (ImageButton) view.findViewById(R.id.btnDice);
		 if(btnDice!=null)
		 {
			 Rect r = btn_dice.getRect();
			 btnDice.layout(r.left,r.top,r.right,r.bottom);
			 btnDice.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch(event.getAction())
					{
						case (MotionEvent.ACTION_DOWN):						
						{
							message = "button dice is working";
						}
						break;
					}
					return false;
				}
			});
			 btnDice.setBackgroundDrawable(view.getResources().getDrawable(R.drawable.ic_dice));
		 }
		 ImageButton btnToken = (ImageButton) view.findViewById(R.id.btnToken);
		 if(btnToken!=null)
		 {
			 Rect r = btn_token.getRect();
			 btnToken.layout(r.left,r.top,r.right,r.bottom);
			 btnToken.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch(event.getAction())
					{
						case (MotionEvent.ACTION_DOWN):						
						{
							message = "button token is working";
						}
						break;
					}
					return false;
				}
			});
			 btnToken.setBackgroundDrawable(view.getResources().getDrawable(R.drawable.ic_token));
		 }
		 ImageButton btnLp = (ImageButton) view.findViewById(R.id.btnLp);
		 if(btnLp!=null)
		 {
			 Rect r = btn_lp.getRect();
			 btnLp.layout(r.left,r.top,r.right,r.bottom);
			 btnLp.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch(event.getAction())
					{
						case (MotionEvent.ACTION_DOWN):						
						{
							AlertDialog.Builder dlg = new AlertDialog.Builder(context);
							final EditText editText =  new EditText(context);
							editText.setId(100);
							dlg.setTitle("LP:").setIcon(
								     android.R.drawable.ic_dialog_info).setView(editText).setPositiveButton("确定", new DialogInterface.OnClickListener() {
										
										@Override
										public void onClick(DialogInterface dialog, int which) {
											// TODO Auto-generated method stub
											String text = editText.getText().toString();
											try{
												int i = 0;
												i = Integer.parseInt(text);
												player00.setLP(player00.getLP() + i);
											}
											catch (NumberFormatException e) {
												e.printStackTrace();
											}
											Log.d("YGO",text);
										}
									}
								    		 )
								     .setNegativeButton("取消", null).show();	
						}
						break;
					}
					return false;
				}
			});
			 btnLp.setBackgroundDrawable(view.getResources().getDrawable(R.drawable.ic_lp));
		 }
		 ImageButton btnEndTurn = (ImageButton) view.findViewById(R.id.btnEndTurn);
		 if(btnEndTurn!=null)
		 {
			 Rect r = btn_endTurn.getRect();
			 btnEndTurn.layout(r.left,r.top,r.right,r.bottom);
			 btnEndTurn.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch(event.getAction())
					{
						case (MotionEvent.ACTION_DOWN):						
						{
							message = "button endTurn is working";
						}
						break;
					}
					return false;
				}
			});
			 btnEndTurn.setBackgroundDrawable(view.getResources().getDrawable(R.drawable.ic_endturn));
		 }
	}
}
