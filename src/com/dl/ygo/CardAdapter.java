package com.dl.ygo;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class CardAdapter extends BaseAdapter{

	private CardGroup cards;
	private Context context;
	public CardAdapter(Context context,CardGroup cards){
		this.context = context;
		this.cards = cards;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return cards.getCount();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return cards.getCards().get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imgView = null;
		if(convertView == null){
			imgView = new ImageView(context);
			imgView.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.WRAP_CONTENT
					,GridView.LayoutParams.WRAP_CONTENT));
			imgView.setScaleType(ImageView.ScaleType.FIT_CENTER);
			imgView.setPadding(8, 8, 8, 8);
		}
		else{
			imgView = (ImageView) convertView;
		}
		imgView.setImageBitmap(cards.getCards().get(position).getCardPic());
		//imgView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_m1));
		return imgView;
	}

}
