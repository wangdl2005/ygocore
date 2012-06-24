package com.dl.ocg;

import com.dl.ocg.Common.CardAttribute;
import com.dl.ocg.Common.CardRace;
import com.dl.ocg.Common.CardType;

public class CardData
{
	//当前code
	public int code;
	// 别名，用于"...这张卡在场上可以当做..."
	public int alias;
	public int setcode;
	//卡片类型：怪兽。。
	public CardType type;
	//等级
	public int level;
	//属性
	public CardAttribute attribute;
	//种族
	public CardRace race;
	//攻击
	public int attack;
	//防守
	public int defence;
}