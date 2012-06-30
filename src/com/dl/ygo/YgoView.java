package com.dl.ygo;

import static com.dl.ocg.Common.FormatLocation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnTouchListener;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dl.ocg.CardDataC;
import com.dl.ocg.CardString;
import com.dl.ocg.Zone;
import com.dl.ocg.Common.CardAttribute;
import com.dl.ocg.Common.CardLocation;
import com.dl.ocg.Common.CardRace;
import com.dl.ocg.Common.CardType;
import com.dl.ygo.*;
import static com.dl.ocg.Common.*;;



public class YgoView extends ViewGroup implements OnClickListener{
	int width = 0;
	int height = 0;
	//
	private Player player00;
	private Player player01;
	private Card cardOnShow;
	

	private static Bitmap bgImg;
	private static Bitmap bgCardImg;
	private Bitmap coverImg;
	
	private Paint pen;
	private String message = "";
	//card data
	private HashMap<String,Bitmap> textures = new HashMap<String, Bitmap>();
	private HashMap<Integer,Bitmap> cardSmallPics = new HashMap<Integer, Bitmap>();
	private HashMap<Integer,Bitmap> cardPics = new HashMap<Integer, Bitmap>();
	private HashMap<Integer,CardDataC> cardDatas = new HashMap<Integer,CardDataC>();
	private HashMap<Integer,CardString> cardStrings = new HashMap<Integer, CardString>();
	private HashMap<Integer,String> counterStrings = new HashMap<Integer, String>();
	private HashMap<Integer,String> victoryStrings = new HashMap<Integer, String>();
	private HashMap<Integer,String> sysStrings = new HashMap<Integer, String>();	
	
	
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
	//卡片描述
	private Rect rect_desc = new Rect();
	//卡片信息，包含卡片描述
	private Rect rect_card_info = new Rect();
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
	
	private Rect rect_cardsView = new Rect();
	
	
	private ImageButton btnDp;
	private ImageButton btnSp;
	private ImageButton btnM1;
	private ImageButton btnBp;
	private ImageButton btnM2;
	private ImageButton btnEp;
	
	private ImageButton btnCoin;
	private ImageButton btnDice;
	private ImageButton btnToken;
	private ImageButton btnLp;
	private ImageButton btnEndTurn;
	
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
	
	private TextView txtDesc;
	private TextView txtMessage;
	
	private CardsView cardsView;
	private boolean isEnabledCardsView = false;
	
	private static float lastX, lastY;  

	private ArrayList<MyButton> buttons = new ArrayList<MyButton>();
	public YgoView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		setWillNotDraw(false);
		init();
	}


	public YgoView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		
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
		//cardOnShow = new Card(cardDatas, cardStrings, cardPics, 1);
		initialCardGroup();
		test();

		addMyView();		
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
		canvas.save();
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
		if(cardOnShow!=null)
		{
		    String name = cardOnShow.getName();
		    String attr = cardOnShow.getAttr();
		    int level =cardOnShow.getLevel();
		    int atk = cardOnShow.getAttack();
		    int def = cardOnShow.getDefence();
		    String race = cardOnShow.getRace();
		    String desc = cardOnShow.getDesc();
		    String loc = FormatLocation(cardOnShow.getLoc().getValue());
		    String type = cardOnShow.getType();
		    String text = null;
		    TextUtil txtUtil = null;
		    //name
		    Log.d(TAG,"name:"+name);
		    text = name;
		    txtUtil = new TextUtil(text, 5, 253,150,22,Color.WHITE, Color.BLACK, 0, 18);
		    txtUtil.InitText();
		    txtUtil.DrawText(canvas);
		    //attr
		    text = "属性: " + attr + "    LV:" + level ;
		    Log.d(TAG,"attr:"+text);
		    txtUtil = new TextUtil(text, 5, 275, 150, 15, Color.WHITE, Color.BLACK, 0, 12);
		    txtUtil.InitText();
		    txtUtil.DrawText(canvas);
		    //atk
		    text = "ATK:" + atk + " /DEF:" + def;
		    Log.d(TAG,"atk:"+text);
		    txtUtil = new TextUtil(text, 5, 290, 150, 15, Color.WHITE, Color.BLACK, 0, 12);
		    txtUtil.InitText();
		    txtUtil.DrawText(canvas);
		    //race type
		    text = "[" + race + "]    "  + type;
		    Log.d(TAG,"race:"+race);
		    txtUtil = new TextUtil(text, 5, 305, 150, 15, Color.WHITE, Color.BLACK, 0, 12);
		    txtUtil.InitText();
		    txtUtil.DrawText(canvas);
		    //位置，counter
		    //TODO 原拥有者
		    text = "位置: " + loc + "    原拥有者:"+"00";
		    Log.d(TAG,text);
		    txtUtil = new TextUtil(text, 5, 320, 150, 15, Color.WHITE, Color.BLACK, 0, 12);
		    txtUtil.InitText();
		    txtUtil.DrawText(canvas);
		    //TODO counter 
		    text = "指示物:  " + "0     " ;
		    Log.d(TAG,text);
		    txtUtil = new TextUtil(text, 5, 335, 150, 15, Color.WHITE, Color.BLACK, 0, 12);
		    txtUtil.InitText();
		    txtUtil.DrawText(canvas);
		    //desc
		    text = "描述";
		    txtUtil = new TextUtil(text, 5, 350, 150, 15, Color.WHITE, Color.BLACK, 0, 10);
		    txtUtil.InitText();
		    txtUtil.DrawText(canvas);
//		    text = desc;
//		    Log.d(TAG,"desc:"+desc);
//		    txtUtil = new TextUtil(text, 5, 345, 150, 100, Color.WHITE, Color.BLACK, 0, 10);
//		    txtUtil.InitText();
//		    txtUtil.DrawText(canvas);
		}
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
	    		int seam = (right-left-SMALL_CARD_WIDTH)/(n00-1);
	    		if(seam > SMALL_CARD_WIDTH){
	    			seam = SMALL_CARD_WIDTH +1;
	    		}
	    		int i = left;
	    		int n = 0;
	    		for(Card c : player00.getCardsHand().getCards()){
	    			++n;
	    			canvas.drawBitmap(c.getCardPic(), null,new Rect(i, top, i + SMALL_CARD_WIDTH, bottom), pen);
	    			if(n < n00){
	    				c.setRect(new Rect(i,top,i + seam-1, bottom));	    			
	    			}
	    			else
	    			{
	    				c.setRect(new Rect(i,top,i + SMALL_CARD_WIDTH, bottom));	    
	    			}
	    			 i += seam;
	    		}
	    	}
	    	if(n01> 0){
	    		int left = zone_1_hand.getRect().left;
	    		int top = zone_1_hand.getRect().top;
	    		int right = zone_1_hand.getRect().right;
	    		int bottom = zone_1_hand.getRect().bottom;
	    		int seam = (right-left-SMALL_CARD_WIDTH)/(n01-1);
	    		if(seam > SMALL_CARD_WIDTH){
	    			seam = SMALL_CARD_WIDTH +1;
	    		}
	    		int i = right;
	    		int n = 0;
	    		for(Card c : player01.getCardsHand().getCards()){
	    			++n;
	    			canvas.drawBitmap(c.getCardPic(), null,new Rect(i - SMALL_CARD_WIDTH, top, i  , bottom), pen);
	    			if(n < n01){
	    				c.setRect(new Rect(i - seam + 1,top,i, bottom));	    			
	    			}
	    			else
	    			{
	    				c.setRect(new Rect(i -SMALL_CARD_WIDTH ,top,i, bottom));	    
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
	  //draw message
	    TextUtil txtUtil = null;
	    Log.d(TAG,"message:"+message);
	    txtUtil = new TextUtil(message, 626, 75,	150, 60, Color.WHITE, Color.BLACK, 0, 15);
	    txtUtil.InitText();
	    txtUtil.DrawText(canvas);
	    
	    canvas.restore();
		super.onDraw(canvas);
	}
	
	
	//根据坐标（x,y），设置contextMenu
	private void setContextMenu(ContextMenu menu,Card c){
		if(isEnabledCardsView == false){
			if(c!=null){
				switch(c.getLoc()){
					case LOCATION_HAND:{
						if(c.getType().contains("怪物")){
							menu.setHeaderTitle("手牌怪物");
							menu.add(Menu.NONE,CONTEXT_MENU_REVEAL,Menu.NONE,STRING_REVEAL);
							menu.add(Menu.NONE,CONTEXT_MENU_TO_S_T,Menu.NONE,STRING_TO_S_T);
							menu.add(Menu.NONE,CONTEXT_MENU_TO_DECK_BOTTOM,Menu.NONE,STRING_TO_DECK_BOTTOM);
							menu.add(Menu.NONE,CONTEXT_MENU_TO_BANISH,Menu.NONE,STRING_TO_BANISH);
							menu.add(Menu.NONE,CONTEXT_MENU_TO_DECK_TOP,Menu.NONE,STRING_TO_DECK_TOP);
							menu.add(Menu.NONE,CONTEXT_MENU_TO_GRAY,Menu.NONE,STRING_TO_GRAY);
							menu.add(Menu.NONE,CONTEXT_MENU_SS_ATK,Menu.NONE,STRING_SS_ATK);
							menu.add(Menu.NONE,CONTEXT_MENU_SS_DEF,Menu.NONE,STRING_SS_DEF);
							menu.add(Menu.NONE,CONTEXT_MENU_SET,Menu.NONE,STRING_SET);
							menu.add(Menu.NONE,CONTEXT_MENU_NORMAL_SUMMON_ATK,Menu.NONE,STRING_NORMAL_SUMMON_ATK);
						}
						if(c.getType().contains("魔法")){
							menu.setHeaderTitle("手牌魔法");
							menu.add(Menu.NONE,CONTEXT_MENU_REVEAL,Menu.NONE,STRING_REVEAL);
							menu.add(Menu.NONE,CONTEXT_MENU_ACTIVATE,Menu.NONE,STRING_ACTIVATE);
							menu.add(Menu.NONE,CONTEXT_MENU_TO_DECK_BOTTOM,Menu.NONE,STRING_TO_DECK_BOTTOM);
							menu.add(Menu.NONE,CONTEXT_MENU_TO_BANISH,Menu.NONE,STRING_TO_BANISH);
							menu.add(Menu.NONE,CONTEXT_MENU_TO_DECK_TOP,Menu.NONE,STRING_TO_DECK_TOP);
							menu.add(Menu.NONE,CONTEXT_MENU_TO_GRAY,Menu.NONE,STRING_TO_GRAY);
							menu.add(Menu.NONE,CONTEXT_MENU_SET,Menu.NONE,STRING_SET);
						}
						if(c.getType().contains("陷阱")){
							menu.setHeaderTitle("手牌陷阱");
							menu.add(Menu.NONE,CONTEXT_MENU_REVEAL,Menu.NONE,STRING_REVEAL);
							menu.add(Menu.NONE,CONTEXT_MENU_TO_DECK_BOTTOM,Menu.NONE,STRING_TO_DECK_BOTTOM);
							menu.add(Menu.NONE,CONTEXT_MENU_TO_BANISH,Menu.NONE,STRING_TO_BANISH);
							menu.add(Menu.NONE,CONTEXT_MENU_TO_DECK_TOP,Menu.NONE,STRING_TO_DECK_TOP);
							menu.add(Menu.NONE,CONTEXT_MENU_TO_GRAY,Menu.NONE,STRING_TO_GRAY);
							menu.add(Menu.NONE,CONTEXT_MENU_SET,Menu.NONE,STRING_SET);						
						}
						break;
					}
					case LOCATION_MZONE:{
						if(c.getType().contains("怪物")){
							//TODO token
							//普通
							menu.setHeaderTitle("怪物");
							menu.add(Menu.NONE,CONTEXT_MENU_TO_HAND,Menu.NONE,STRING_TO_HAND);
							menu.add(Menu.NONE,CONTEXT_MENU_TO_S_T,Menu.NONE,STRING_TO_S_T);
							menu.add(Menu.NONE,CONTEXT_MENU_TO_DECK_BOTTOM,Menu.NONE,STRING_TO_DECK_BOTTOM);
							menu.add(Menu.NONE,CONTEXT_MENU_TO_BANISH,Menu.NONE,STRING_TO_BANISH);
							menu.add(Menu.NONE,CONTEXT_MENU_TO_DECK_TOP,Menu.NONE,STRING_TO_DECK_TOP);
							menu.add(Menu.NONE,CONTEXT_MENU_TO_GRAY,Menu.NONE,STRING_TO_GRAY);
							menu.add(Menu.NONE,CONTEXT_MENU_TO_DEF,Menu.NONE,STRING_TO_DEF);
							menu.add(Menu.NONE,CONTEXT_MENU_SET,Menu.NONE,STRING_SET);
							menu.add(Menu.NONE,CONTEXT_MENU_OVERLAY,Menu.NONE,STRING_OVERLAY);
							menu.add(Menu.NONE,CONTEXT_MENU_CHANGE_CONTROL,Menu.NONE,STRING_CHANGE_CONTROL);
						}
						if(c.getType().contains("魔法")){
							
						}
						if(c.getType().contains("陷阱")){
							
						}
						break;
					}
					case LOCATION_SZONE:{
						if(c.getType().contains("怪物")){
							
						}
						if(c.getType().contains("魔法")){
							
						}
						if(c.getType().contains("陷阱")){
							
						}
						break;
					}
					case LOCATION_OVERLAY:{
						if(c.getType().contains("怪物")){
							
						}
						if(c.getType().contains("魔法")){
							
						}
						if(c.getType().contains("陷阱")){
							
						}
						break;
					}
				}
			}
		}
		else{
			if(c!=null){
				switch(c.getLoc()){
					case LOCATION_DECK:{
						if(c.getType().contains("怪物")){
							menu.setHeaderTitle("Deck怪物");
							menu.add(Menu.NONE,CONTEXT_MENU_REVEAL,Menu.NONE,STRING_REVEAL);
							menu.add(Menu.NONE,CONTEXT_MENU_TO_S_T,Menu.NONE,STRING_TO_S_T);
							menu.add(Menu.NONE,CONTEXT_MENU_TO_DECK_BOTTOM,Menu.NONE,STRING_TO_DECK_BOTTOM);
							menu.add(Menu.NONE,CONTEXT_MENU_TO_BANISH,Menu.NONE,STRING_TO_BANISH);
							menu.add(Menu.NONE,CONTEXT_MENU_TO_DECK_TOP,Menu.NONE,STRING_TO_DECK_TOP);
							menu.add(Menu.NONE,CONTEXT_MENU_TO_GRAY,Menu.NONE,STRING_TO_GRAY);
							menu.add(Menu.NONE,CONTEXT_MENU_SS_ATK,Menu.NONE,STRING_SS_ATK);
							menu.add(Menu.NONE,CONTEXT_MENU_SS_DEF,Menu.NONE,STRING_SS_DEF);
							menu.add(Menu.NONE,CONTEXT_MENU_SET,Menu.NONE,STRING_SET);
							menu.add(Menu.NONE,CONTEXT_MENU_NORMAL_SUMMON_ATK,Menu.NONE,STRING_NORMAL_SUMMON_ATK);
						}
						if(c.getType().contains("魔法")){
							
						}
						if(c.getType().contains("陷阱")){
							
						}
						break;
					}
					case LOCATION_EXTRA:{
						if(c.getType().contains("怪物")){
							
						}
						if(c.getType().contains("魔法")){
							
						}
						if(c.getType().contains("陷阱")){
							
						}
						break;
					}
					case LOCATION_REMOVED:{
						if(c.getType().contains("怪物")){
							
						}
						if(c.getType().contains("魔法")){
							
						}
						if(c.getType().contains("陷阱")){
							
						}
						break;
					}
					case LOCATION_GRAVE:{
						if(c.getType().contains("怪物")){
							
						}
						if(c.getType().contains("魔法")){
							
						}
						if(c.getType().contains("陷阱")){
							
						}
						break;
					}
				}
			}
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub		
		final ArrayList<Rect> rectInvaliate = new ArrayList<Rect>();
		float selX = event.getX();
		float selY = event.getY();
		switch (event.getAction()) 
		{
			case MotionEvent.ACTION_DOWN:
		    {
	    		lastX = selX;  
	            lastY = selY;  
	    		//cardsOnShow
				cardOnShow = getCard(selX,selY);
				if(cardOnShow != null)
				{
						setCardOnShow(cardOnShow);
						//txtDesc.invalidate();
						rectInvaliate.add(rect_card_info);
						rectInvaliate.add(zone_on_show.getRect());
				}
				//extra,grave,deck,remove
				Zone z = getZone(selX, selY);
				if(z != null){
					if(z.isTheSame(zone_0_deck)){
						isEnabledCardsView = true;
						cardsView.setCards(player00.getCardsDeck());
						cardsView.setEnabled(true);
						cardsView.setVisibility(VISIBLE);
					}else if(z.isTheSame(zone_0_extra))
					{
						isEnabledCardsView = true;
						cardsView.setCards(player00.getCardsExtra());
						cardsView.setEnabled(true);
						cardsView.setVisibility(VISIBLE);
					}else if(z.isTheSame(zone_0_grave))
					{
						isEnabledCardsView = true;
						cardsView.setCards(player00.getCardsGrave());
						cardsView.setEnabled(true);
						cardsView.setVisibility(VISIBLE);
					}else if(z.isTheSame(zone_0_remove))
					{
						isEnabledCardsView = true;
						cardsView.setCards(player00.getCardsBanish());
						cardsView.setEnabled(true);
						cardsView.setVisibility(VISIBLE);
					}else if(z.isTheSame(zone_1_deck)){
						isEnabledCardsView = true;
						cardsView.setCards(player01.getCardsDeck());
						cardsView.setEnabled(true);
						cardsView.setVisibility(VISIBLE);
					}else if(z.isTheSame( zone_1_extra))
					{
						isEnabledCardsView = true;
						cardsView.setCards(player01.getCardsExtra());
						cardsView.setEnabled(true);
						cardsView.setVisibility(VISIBLE);
					}else if(z.isTheSame(zone_1_grave))
					{
						isEnabledCardsView = true;
						cardsView.setCards(player01.getCardsGrave());
						cardsView.setEnabled(true);
						cardsView.setVisibility(VISIBLE);
					}else if(z.isTheSame(zone_1_remove))
					{
						isEnabledCardsView = true;
						cardsView.setCards(player01.getCardsBanish());
						cardsView.setEnabled(true);
						cardsView.setVisibility(VISIBLE);
					}
				}
			}
        }        		
		for(Rect r : rectInvaliate){
			invalidate(r);
		}
		//addMyView();
		return super.onTouchEvent(event);
	}


	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		 setChildView();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		 if(keyCode == KeyEvent.KEYCODE_BACK && isEnabledCardsView == true){
				isEnabledCardsView = false;
				cardsView.setEnabled(false);
				cardsView.setVisibility(INVISIBLE);
				return true;
		 }
		 return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onCreateContextMenu(ContextMenu menu) {
		Card c = getCard(lastX,lastY );
		setContextMenu(menu, c);
		super.onCreateContextMenu(menu);
	}
	
	private void setChildView() {
		if(txtDesc!=null)
		 {
			 Rect r1 = rect_desc;
			 txtDesc.layout(r1.left,r1.top,r1.right,r1.bottom);
		 }
		 if(txtMessage!=null)
		 {
			 Rect r1 = rect_message;
			 txtMessage.layout(r1.left,r1.top,r1.right,r1.bottom);
		 }		 
		 if(btnDp!=null)
		 {
			 Rect r1 = btn_dp.getRect();
			 btnDp.layout(r1.left,r1.top,r1.right,r1.bottom);
		 }
		 if(btnSp!=null)
		 {
			 Rect r1 = btn_sp.getRect();
			 btnSp.layout(r1.left,r1.top,r1.right,r1.bottom);
		 }
		 if(btnM1!=null)
		 {
			 Rect r1 = btn_m1.getRect();
			 btnM1.layout(r1.left,r1.top,r1.right,r1.bottom);
		 }
		 if(btnBp!=null)
		 {
			 Rect r1 = btn_bp.getRect();
			 btnBp.layout(r1.left,r1.top,r1.right,r1.bottom);
		 }
		 if(btnM2!=null)
		 {
			 Rect r1 = btn_m2.getRect();
			 btnM2.layout(r1.left,r1.top,r1.right,r1.bottom);
		 }
		 if(btnEp!=null)
		 {
			 Rect r1 = btn_ep.getRect();
			 btnEp.layout(r1.left,r1.top,r1.right,r1.bottom);
		 }
		 if(btnCoin!=null)
		 {
			 Rect r1 = btn_coin.getRect();
			 btnCoin.layout(r1.left,r1.top,r1.right,r1.bottom);
		 }
		 if(btnDice!=null)
		 {
			 Rect r1 = btn_dice.getRect();
			 btnDice.layout(r1.left,r1.top,r1.right,r1.bottom);
		 }
		 if(btnToken!=null)
		 {
			 Rect r1 = btn_token.getRect();
			 btnToken.layout(r1.left,r1.top,r1.right,r1.bottom);
		 }
		 if(btnLp!=null)
		 {
			 Rect r1 = btn_lp.getRect();
			 btnLp.layout(r1.left,r1.top,r1.right,r1.bottom);
		 }
		 if(btnEndTurn!=null)
		 {
			 Rect r1 = btn_endTurn.getRect();
			 btnEndTurn.layout(r1.left,r1.top,r1.right,r1.bottom);
		 }


		 if(cardsView != null){
			Rect r1 = rect_cardsView;
			cardsView.measure(r1.right-r1.left,r1.bottom-r1.top);
			cardsView.layout(r1.left	, r1.top, r1.right, r1.bottom);
		 }
	}


	private void addMyView() {
		//Buuton
		btnDp = new ImageButton(getContext());
		btnSp = new ImageButton(getContext());
		btnM1 = new ImageButton(getContext());
		btnBp = new ImageButton(getContext());
		btnM2 = new ImageButton(getContext());
		btnEp = new ImageButton(getContext());
		
		btnCoin = new ImageButton(getContext());
		btnDice = new ImageButton(getContext());
		btnToken = new ImageButton(getContext());
		btnLp= new ImageButton(getContext());
		btnEndTurn = new ImageButton(getContext());

		btnDp.setOnClickListener(this);
		btnDp.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_dp));
		btnSp.setOnClickListener(this);
		btnSp.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_sp));
		btnM1.setOnClickListener(this);
		btnM1.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_m1));
		btnBp.setOnClickListener(this);
		btnBp.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_bp));
		btnM2.setOnClickListener(this);
		btnM2.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_m2));
		btnEp.setOnClickListener(this);
		btnEp.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_ep));
		btnCoin.setOnClickListener(this);
		btnCoin.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_coin));
		btnDice.setOnClickListener(this);
		btnDice.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_dice));
		btnToken.setOnClickListener(this);
		btnToken.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_token));
		btnLp.setOnClickListener(this);
		btnLp.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_lp));
		btnEndTurn.setOnClickListener(this);
		btnEndTurn.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_endturn));
		
		//TextView
		txtDesc = new TextView(getContext());
		txtMessage = new TextView(getContext());
		
		txtDesc.setTextColor(Color.BLACK);
		txtDesc.setTextSize(9.0f);
		txtDesc.setSingleLine(false);
		txtDesc.setVerticalScrollBarEnabled(true);
		txtDesc.setScrollBarStyle(SCROLLBARS_INSIDE_OVERLAY);
		txtDesc.setMovementMethod(ScrollingMovementMethod.getInstance()); 
		
		txtMessage.setTextColor(Color.BLACK);
		txtMessage.setTextSize(9.0f);
		txtMessage.setBackgroundColor(Color.GRAY);
		txtMessage.setSingleLine(false);
		txtMessage.setVerticalScrollBarEnabled(true);
		txtMessage.setScrollBarStyle(SCROLLBARS_INSIDE_OVERLAY);
		txtMessage.setMovementMethod(ScrollingMovementMethod.getInstance()); 
		
		//cards view
		cardsView = new CardsView(getContext(),player00.getCardsDeck());
		cardsView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				setCardOnShow((Card) parent.getItemAtPosition(position));				
				YgoView.this.invalidate(rect_card_info);
				YgoView.this.invalidate(zone_on_show.getRect());
			}
		});
		cardsView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				final int pos = position;
				View.OnCreateContextMenuListener listener = new OnCreateContextMenuListener() {
					
					@Override
					public void onCreateContextMenu(ContextMenu menu, View v,
							ContextMenuInfo menuInfo) {
						// TODO Auto-generated method stub
						AdapterView.AdapterContextMenuInfo info;
                        try {
                             info = (AdapterView.AdapterContextMenuInfo) menuInfo;
                        } catch (ClassCastException e) {
                            return;
                        }
                		Card c = cardsView.getCards().getCards().get(pos);
                        setContextMenu(menu, c);
					}
				};
				cardsView.setOnCreateContextMenuListener(listener);
				return false;
			}
			
		});
		cardsView.setEnabled(false);
		cardsView.setVisibility(INVISIBLE);
		
	 	addView(btnDp);
		addView(btnSp);
		addView(btnM1);
		addView(btnBp);
		addView(btnM2);
		addView(btnEp);
		
		addView(btnCoin);
		addView(btnDice);
		addView(btnToken);
		addView(btnLp);
		addView(btnEndTurn);
		
		addView(txtDesc);
		addView(txtMessage);
		
		addView(cardsView);
	}
	



	@Override
	public void onClick(View v) {
		//LP BUTTON
		if(v == btnLp)
		{
			AlertDialog.Builder dlg = new AlertDialog.Builder(getContext());
			final EditText editText =  new EditText(getContext());
			editText.setId(100);
			dlg.setTitle("LP:").setIcon(
				     android.R.drawable.ic_dialog_info).setView(editText).setPositiveButton("确定", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							String text = editText.getText().toString();
							try{
								int i = 0;
								i = Integer.parseInt(text.replace('+', ' ').trim());
								player00.setLP(player00.getLP() + i);
								sendMessage("@lp:" 
								+ ((i >= 0)?"+":"-") + i);											
								invalidate(rect_player00_lp);
							}
							catch (NumberFormatException e) {
								e.printStackTrace();
							}
							Log.d("YGO",text);
						}
					}).setNegativeButton("取消", null).show();	
		}
	}
	
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
		Log.d(TAG, "begin to read bmps");
		if(fileList != null){
			for(File f : fileList){
			
				bmp = ((BitmapDrawable)(BitmapDrawable.createFromPath(f.getAbsolutePath()))).getBitmap();  
				try{
					cardPics.put(Integer.parseInt(f.getName().replaceAll(".jpg","")),bmp);				
				}
				catch(NumberFormatException ex){
					continue;
				}
				bmp = Bitmap.createScaledBitmap(bmp, SMALL_CARD_WIDTH, SMALL_CARD_HEIGHT, false);
				try{
					cardSmallPics.put(Integer.parseInt(f.getName().replaceAll(".jpg","")),bmp);				
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
				textures.put(f.getName().replaceAll(".png",""),bmp);				
			}
		}
		Log.d(TAG, "end to read bmps");
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
			cd.type = cursor.getInt(4);
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
		rect_desc.set(3,355, 163, 442);
		rect_card_info.set(3,240,163,442);
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
		
		rect_cardsView.set(170,63,610,377);
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
	private Zone getZone(float x,float y){
		Zone tmp = null;
		for(Zone z : zones){
			if(z.getRect().contains((int)x,(int)y)){
				tmp = z;
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
		Card c1 = new Card(cardDatas,cardStrings,cardSmallPics,1);
		Card c2 =new Card(cardDatas,cardStrings,cardSmallPics,131182);
		Card c3 = new Card(cardDatas,cardStrings,cardSmallPics,135598);
		Card c4 =new Card(cardDatas,cardStrings,cardSmallPics,168917);
		Card c5 = new Card(cardDatas,cardStrings,cardSmallPics,176392);
		Card c6 = new Card(cardDatas,cardStrings,cardSmallPics,191749);
		Card c7 = new Card(cardDatas,cardStrings,cardSmallPics,218704);
		Card c8 =new Card(cardDatas,cardStrings,cardSmallPics,27551);
		Card c9 = new Card(cardDatas,cardStrings,cardSmallPics,50755);
		Card c10 = new Card(cardDatas,cardStrings,cardSmallPics,135598);
		Card c11 =new Card(cardDatas,cardStrings,cardSmallPics,168917);
		Card c12 = new Card(cardDatas,cardStrings,cardSmallPics,176392);
		Card c13 = new Card(cardDatas,cardStrings,cardSmallPics,191749);
		Card c14 = new Card(cardDatas,cardStrings,cardSmallPics,218704);
		Card c15 =new Card(cardDatas,cardStrings,cardSmallPics,27551);
		Card c16 = new Card(cardDatas,cardStrings,cardSmallPics,50755);
		Card c17 =new Card(cardDatas,cardStrings,cardSmallPics,168917);
		Card c18 = new Card(cardDatas,cardStrings,cardSmallPics,176392);
		Card c19 =new Card(cardDatas,cardStrings,cardSmallPics,27551);
		Card c20 = new Card(cardDatas,cardStrings,cardSmallPics,50755);
		Card c21 =new Card(cardDatas,cardStrings,cardSmallPics,168917);
		Card c22 = new Card(cardDatas,cardStrings,cardSmallPics,176392);
		Card c23 =new Card(cardDatas,cardStrings,cardSmallPics,168917);
		
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
	
	public void setCardOnShow(Card c){
		if(c!=null){
			cardOnShow = c;
			cardOnShow.setCardPic(cardPics.get(c.getCode()));
		    String desc = cardOnShow.getDesc();
			StringBuilder text = new StringBuilder();
			text.append(desc);
			txtDesc.setText(text);
		}
	}
	
	public void sendMessage(String text)
	{
		//show on messageBoard
		String message = getTimeNow() + ": player00  " +text + "\n";
		txtMessage.append(message);
		//send message to player01
	}
	//formatTime
	public String getTimeNow()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));		
		return sdf.format(new Date());
	}
}
