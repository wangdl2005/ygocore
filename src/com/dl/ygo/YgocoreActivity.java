package com.dl.ygo;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager;

public class YgocoreActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
//        CardsView v = new CardsView(this, new CardGroup(40));
//        setContentView(v);
		/**全屏设置，隐藏窗口所有装饰*/  
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,   
		WindowManager.LayoutParams.FLAG_FULLSCREEN);  
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	YgoView view = (YgoView) findViewById(R.id.ygoview);
    	if(view.onKeyDown(keyCode, event) ){
    		return true;
    	}
		return super.onKeyDown(keyCode, event);
    }
}