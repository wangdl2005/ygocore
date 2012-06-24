package com.dl.ocg;



public final class Common {
	//卡片位置：牌组、手牌、怪兽区、魔陷区、墓地、除外、额外、叠放
	public enum CardLocation{
		LOCATION_DECK(0x01),
		LOCATION_HAND(0x02),
		LOCATION_MZONE(0x04),
		LOCATION_SZONE (0x08),
		LOCATION_GRAVE (0x10),
		LOCATION_REMOVED(0x20),
		LOCATION_EXTRA (0x40),
		LOCATION_OVERLAY (0x80),
		LOCATION_ONFIELD (0x0c),
		LOCATION_OFFFIELD(0x100);
		private int value;
		CardLocation(int value)
		{
			this.value = value;			
		}
		public int getValue()
		{
			return this.value;			
		}	
		public static CardLocation toCardLocation(int value)
		{
			CardLocation cL = CardLocation.LOCATION_OFFFIELD;
			switch(value)
			{
				case 0x01:
					cL = CardLocation.LOCATION_DECK;
					break;
				case 0x02:
					cL = CardLocation.LOCATION_HAND;
					break;
				case 0x04:
					cL = CardLocation.LOCATION_MZONE;
					break;
				case 0x08:
					cL = CardLocation.LOCATION_SZONE;
					break;
				case 0x10:
					cL = CardLocation.LOCATION_GRAVE;
					break;
				case 0x20:
					cL = CardLocation.LOCATION_REMOVED;
					break;
				case 0x40:
					cL = CardLocation.LOCATION_EXTRA;
					break;
				case 0x80:
					cL = CardLocation.LOCATION_OVERLAY;
					break;
				case 0x0c:
					cL = CardLocation.LOCATION_ONFIELD;
					break;
				case 0x100:				
					cL = CardLocation.LOCATION_OFFFIELD;
					break;
				default:
					break;
			}
			return cL;			
		}
	}
	
	//"里侧攻击""表侧攻击"; "表侧守备";"里侧守备";"表侧";"里侧";"攻击"; "守备";	
	public enum CardPosition{
		POS_FACEUP_ATTACK (0x1),
		POS_FACEDOWN_ATTACK (0x2),
		POS_FACEUP_DEFENCE (0x4),
		POS_FACEDOWN_DEFENCE (0x8),
		POS_FACEUP (0x5),
		POS_FACEDOWN (0xa),
		POS_ATTACK (0x3),
		POS_DEFENCE (0xc);
		private int value;
		private CardPosition(int value)
		{
			this.value = value;			
		}
		public int getValue()
		{
			return this.value;			
		}	
		public static CardPosition toCardPosition(int value)
		{
			CardPosition cP = CardPosition.POS_ATTACK;
			switch(value)
			{
				case 0x1:
					cP = CardPosition.POS_FACEUP_ATTACK;
					break;
				case 0x2:
					cP = CardPosition.POS_FACEDOWN_ATTACK;
					break;
				case 0x4:
					cP = CardPosition.POS_FACEUP_DEFENCE;
					break;
				case 0x8:
					cP = CardPosition.POS_FACEDOWN_DEFENCE;
					break;
				case 0x5:
					cP = CardPosition.POS_FACEUP;
					break;
				case 0xa:
					cP = CardPosition.POS_FACEDOWN;
					break;
				case 0x3:
					cP = CardPosition.POS_ATTACK;
					break;
				case 0xc:
					cP = CardPosition.POS_DEFENCE;
					break;
				default:
					break;
			}
			return cP;
		}
	}	
	//"怪物|";"魔法|";"陷阱|";"通常|";"效果|";"融合|";"仪式|";"陷阱怪物|";
	//"灵魂|";"同盟|";"二重|";"协调|";"同调|";"TOKEN|";"速攻|"; "永续|";
	//"装备|";"场地|";"反击|";"翻转|";"卡通|";"超量|"
	public enum CardType{
		TYPE_MONSTER (0x1),
		TYPE_SPELL (0x2),
		TYPE_TRAP (0x4),
		TYPE_NORMAL (0x10),
		TYPE_EFFECT (0x20),
		TYPE_FUSION (0x40),
		TYPE_RITUAL (0x80),
		TYPE_TRAPMONSTER (0x100),
		TYPE_SPIRIT (0x200),
		TYPE_UNION (0x400),
		TYPE_DUAL (0x800),
		TYPE_TUNER (0x1000),
		TYPE_SYNCHRO (0x2000),
		TYPE_TOKEN (0x4000),
		TYPE_QUICKPLAY (0x10000),
		TYPE_CONTINUOUS (0x20000),
		TYPE_EQUIP (0x40000),
		TYPE_FIELD (0x80000),
		TYPE_COUNTER (0x100000),
		TYPE_FLIP (0x200000),
		TYPE_TOON (0x400000),
		TYPE_XYZ (0x800000);
		private int value;
		private CardType(int value)
		{
			this.value = value;			
		}
		public int getValue()
		{
			return this.value;			
		}
		public static CardType toCardType(int value)
		{
			CardType cT = CardType.TYPE_MONSTER;
			switch(value)
			{
				case 0x1:
					cT = CardType.TYPE_MONSTER;
					break;
				case 0x2:
					cT = CardType.TYPE_SPELL;
					break;
				case 0x4:
					cT = CardType.TYPE_TRAP;
					break;
				case 0x10:
					cT = CardType.TYPE_NORMAL;
					break;
				case 0x20:
					cT = CardType.TYPE_EFFECT;
					break;
				case 0x40:
					cT = CardType.TYPE_FUSION;
					break;
				case 0x80:
					cT = CardType.TYPE_RITUAL;
					break;
				case 0x100:
					cT = CardType.TYPE_TRAPMONSTER;
					break;
				case 0x200:
					cT = CardType.TYPE_SPIRIT;
					break;
				case 0x400:
					cT = CardType.TYPE_UNION;
					break;
				case 0x800:
					cT = CardType.TYPE_DUAL;
					break;
				case 0x1000:
					cT = CardType.TYPE_TUNER;
					break;
				case 0x2000:
					cT = CardType.TYPE_SYNCHRO;
					break;
				case 0x4000:
					cT = CardType.TYPE_TOKEN;
					break;
				case 0x10000:
					cT = CardType.TYPE_QUICKPLAY;
					break;
				case 0x20000:
					cT = CardType.TYPE_CONTINUOUS;
					break;
				case 0x40000:
					cT = CardType.TYPE_EQUIP;
					break;
				case 0x80000:
					cT = CardType.TYPE_FIELD;
					break;
				case 0x100000:
					cT = CardType.TYPE_COUNTER;
					break;
				case 0x200000:
					cT = CardType.TYPE_FLIP;
					break;
				case 0x400000:
					cT = CardType.TYPE_TOON;
					break;
				case 0x800000:
					cT = CardType.TYPE_XYZ;
					break;
				default:
					break;
			}
			return cT;
		}
	}
	//地，水，火，风，光，暗，神
	public enum CardAttribute{
			ATTRIBUTE_EARTH (0x01),
			ATTRIBUTE_WATER (0x02),
			ATTRIBUTE_FIRE (0x04),
			ATTRIBUTE_WIND (0x08),
			ATTRIBUTE_LIGHT (0x10),
			ATTRIBUTE_DARK (0x20),
			ATTRIBUTE_DEVINE (0x40);
			private int value;
			private CardAttribute(int value)
			{
				this.value = value;			
			}
			public int getValue()
			{
				return this.value;			
			}
			public static CardAttribute toCardAttribute(int value)
			{
				CardAttribute cA = CardAttribute.ATTRIBUTE_EARTH;
				switch(value)
				{
					case 0x1:
						cA = CardAttribute.ATTRIBUTE_EARTH;
						break;
					case 0x2:
						cA = CardAttribute.ATTRIBUTE_WATER;
						break;
					case 0x4:
						cA = CardAttribute.ATTRIBUTE_FIRE;
						break;
					case 0x8:
						cA = CardAttribute.ATTRIBUTE_WIND;
						break;
					case 0x10:
						cA = CardAttribute.ATTRIBUTE_LIGHT;
						break;
					case 0x20:
						cA = CardAttribute.ATTRIBUTE_DARK;
						break;
					case 0x40:
						cA = CardAttribute.ATTRIBUTE_DEVINE;
						break;					
					default:
						break;
				}
				return cA;
			}
		}
	//战士|";"魔法使|";"天使|";"恶魔|";"不死|";"机械|";"水|";
	//"炎|";"岩石|";"鸟兽|";"植物|";"昆虫|";"雷|";"龙|";"兽|";
	//"兽战士|";"恐龙|";"鱼|";"海龙|";"爬虫|";"念动力|";"幻神兽|
	public enum CardRace{
			RACE_WARRIOR (0x1),
			RACE_SPELLCASTER (0x2),
			RACE_FAIRY (0x4),
			RACE_FIEND (0x8),
			RACE_ZOMBIE (0x10),
			RACE_MACHINE (0x20),
			RACE_AQUA (0x40),
			RACE_PYRO (0x80),
			RACE_ROCK (0x100),
			RACE_WINDBEAST (0x200),
			RACE_PLANT (0x400),
			RACE_INSECT (0x800),
			RACE_THUNDER (0x1000),
			RACE_DRAGON (0x2000),
			RACE_BEAST (0x4000),
			RACE_BEASTWARRIOR (0x8000),
			RACE_DINOSAUR (0x10000),
			RACE_FISH (0x20000),
			RACE_SEASERPENT (0x40000),
			RACE_REPTILE (0x80000),
			RACE_PSYCHO (0x100000),
			RACE_DEVINE (0x200000),
			RACE_CREATORGOD(0x400000);
			private int value;
			private CardRace(int value)
			{
				this.value = value;			
			}
			public int getValue()
			{
				return this.value;			
			}
			public static CardRace toCardRace(int value)
			{
				CardRace cR = CardRace.RACE_WARRIOR;
				switch(value)
				{
					case 0x1:
						cR = CardRace.RACE_WARRIOR;
						break;
					case 0x2:
						cR = CardRace.RACE_SPELLCASTER;
						break;
					case 0x4:
						cR = CardRace.RACE_FAIRY;
						break;
					case 0x8:
						cR = CardRace.RACE_FIEND;
						break;
					case 0x10:
						cR = CardRace.RACE_ZOMBIE;
						break;
					case 0x20:
						cR = CardRace.RACE_MACHINE;
						break;
					case 0x40:
						cR = CardRace.RACE_AQUA;
						break;
					case 0x80:
						cR = CardRace.RACE_PYRO;
						break;
					case 0x100:
						cR = CardRace.RACE_ROCK;
						break;
					case 0x200:
						cR = CardRace.RACE_WINDBEAST;
						break;
					case 0x400:
						cR = CardRace.RACE_PLANT;
						break;
					case 0x800:
						cR = CardRace.RACE_INSECT;
						break;
					case 0x1000:
						cR = CardRace.RACE_THUNDER;
						break;
					case 0x2000:
						cR = CardRace.RACE_DRAGON;
						break;
					case 0x4000:
						cR = CardRace.RACE_BEAST;
						break;
					case 0x8000:
						cR = CardRace.RACE_BEASTWARRIOR;
						break;
					case 0x10000:
						cR = CardRace.RACE_DINOSAUR;
						break;
					case 0x20000:
						cR = CardRace.RACE_FISH;
						break;
					case 0x40000:
						cR = CardRace.RACE_SEASERPENT;
						break;
					case 0x80000:
						cR = CardRace.RACE_REPTILE;
						break;
					case 0x100000:
						cR = CardRace.RACE_PSYCHO;
						break;
					case 0x200000:
						cR = CardRace.RACE_DEVINE;
						break;
					case 0x400000:
						cR = CardRace.RACE_CREATORGOD;
						break;
					default:
						break;
				}
				return cR;
			}
		}
	//"破坏|";"解放|";"暂时|";"素材|";"召唤|";"战斗|";"效果|";"代价|";"手牌调整|";
	//"失去装备对象|";"规则|";"特殊召唤|";"召唤无效化|";"翻转|";"丢弃|";"--|";"--|";
	//"返回|";"融合|";"同调|";"仪式|";"超量|";
	public enum CardReason{
			REASON_DESTROY (0x1),
			REASON_RELEASE (0x2),
			REASON_TEMPORARY (0x4),
			REASON_MATERIAL (0x8),
			REASON_SUMMON (0x10),
			REASON_BATTLE (0x20),
			REASON_EFFECT (0x40),
			REASON_COST (0x80),
			REASON_ADJUST (0x100),
			REASON_LOST_EQUIP (0x200),
			REASON_RULE (0x400),
			REASON_SPSUMMON (0x800),
			REASON_DISSUMMON (0x1000),
			REASON_FLIP (0x2000),
			REASON_DISCARD (0x4000),
			REASON_RDAMAGE (0x8000),
			REASON_RRECOVER (0x10000),
			REASON_RETURN (0x20000),
			REASON_FUSION (0x40000),
			REASON_SYNCHRO (0x80000),
			REASON_RITUAL (0x100000),
			REASON_XYZ (0x200000),
			REASON_REPLACE (0x1000000),
			REASON_DRAW (0x2000000),
			REASON_REDIRECT (0x4000000);
			private int value;
			private CardReason(int value)
			{
				this.value = value;			
			}
			public int getValue()
			{
				return this.value;			
			}
			public static CardReason toCardReason(int value)
			{
				CardReason cR = CardReason.REASON_DESTROY;
				switch(value)
				{
					case 0x1:
						cR = CardReason.REASON_DESTROY;
						break;
					case 0x2:
						cR = CardReason.REASON_RELEASE;
						break;
					case 0x4:
						cR = CardReason.REASON_TEMPORARY;
						break;
					case 0x8:
						cR = CardReason.REASON_MATERIAL;
						break;
					case 0x10:
						cR = CardReason.REASON_SUMMON;
						break;
					case 0x20:
						cR = CardReason.REASON_BATTLE;
						break;
					case 0x40:
						cR = CardReason.REASON_EFFECT;
						break;
					case 0x80:
						cR = CardReason.REASON_COST;
						break;
					case 0x100:
						cR = CardReason.REASON_ADJUST;
						break;
					case 0x200:
						cR = CardReason.REASON_LOST_EQUIP;
						break;
					case 0x400:
						cR = CardReason.REASON_RULE;
						break;
					case 0x800:
						cR = CardReason.REASON_SPSUMMON;
						break;
					case 0x1000:
						cR = CardReason.REASON_DISSUMMON;
						break;
					case 0x2000:
						cR = CardReason.REASON_FLIP;
						break;
					case 0x4000:
						cR = CardReason.REASON_DISCARD;
						break;
					case 0x8000:
						cR = CardReason.REASON_RDAMAGE;
						break;
					case 0x10000:
						cR = CardReason.REASON_RRECOVER;
						break;
					case 0x20000:
						cR = CardReason.REASON_RETURN;
						break;
					case 0x40000:
						cR = CardReason.REASON_FUSION;
						break;
					case 0x80000:
						cR = CardReason.REASON_SYNCHRO;
						break;
					case 0x100000:
						cR = CardReason.REASON_RITUAL;
						break;
					case 0x200000:
						cR = CardReason.REASON_XYZ;
						break;
					case 0x1000000:
						cR = CardReason.REASON_REPLACE;
						break;
					case 0x2000000:
						cR = CardReason.REASON_DRAW;
						break;
					case 0x4000000:
						cR = CardReason.REASON_REDIRECT;
						break;
					default:
						break;
				}
				return cR;
			}
		}
	public enum CardStatus{
			STATUS_DISABLED (0x0001),
			STATUS_TO_ENABLE (0x0002),
			STATUS_TO_DISABLE (0x0004),
			STATUS_PROC_COMPLETE (0x0008),
			STATUS_SET_TURN (0x0010),
			STATUS_ACTIVATING (0x0020),
			STATUS_REVIVE_LIMIT (0x0040),
			STATUS_ATTACKED (0x0080),
			STATUS_FORM_CHANGED (0x0100),
			STATUS_SUMMONING (0x0200),
			STATUS_EFFECT_ENABLED (0x0400),
			STATUS_SUMMON_TURN (0x0800),
			STATUS_DESTROY_CONFIRMED (0x1000),
			STATUS_LEAVE_CONFIRMED (0x2000),
			STATUS_BATTLE_DESTROYED (0x4000),
			STATUS_COPYING_EFFECT (0x8000),
			STATUS_CHAINING (0x10000),
			STATUS_SUMMON_DISABLED (0x20000),
			STATUS_ACTIVATE_DISABLED (0x40000),
			STATUS_UNSUMMONABLE_CARD (0x80000),
			STATUS_UNION (0x100000),
			STATUS_ATTACK_CANCELED (0x200000),
			STATUS_INITIALIZING (0x400000),
			STATUS_ACTIVATED (0x800000),
			STATUS_JUST_POS (0x1000000),
			STATUS_CONTINUOUS_POS (0x2000000),
			STATUS_IS_PUBLIC (0x4000000);
			private int value;
			private CardStatus(int value)
			{
				this.value = value;			
			}
			public int getValue()
			{
				return this.value;			
			}
			public static CardStatus toCardStatus(int value)
			{
				CardStatus cR = CardStatus.STATUS_DISABLED;
				switch(value)
				{
					case 0x1:
						cR = CardStatus.STATUS_DISABLED;
						break;
					case 0x2:
						cR = CardStatus.STATUS_TO_ENABLE;
						break;
					case 0x4:
						cR = CardStatus.STATUS_TO_DISABLE;
						break;
					case 0x8:
						cR = CardStatus.STATUS_PROC_COMPLETE;
						break;
					case 0x10:
						cR = CardStatus.STATUS_SET_TURN;
						break;
					case 0x20:
						cR = CardStatus.STATUS_ACTIVATING;
						break;
					case 0x40:
						cR = CardStatus.STATUS_REVIVE_LIMIT;
						break;
					case 0x80:
						cR = CardStatus.STATUS_ATTACKED;
						break;
					case 0x100:
						cR = CardStatus.STATUS_FORM_CHANGED;
						break;
					case 0x200:
						cR = CardStatus.STATUS_SUMMONING;
						break;
					case 0x400:
						cR = CardStatus.STATUS_EFFECT_ENABLED;
						break;
					case 0x800:
						cR = CardStatus.STATUS_SUMMON_TURN;
						break;
					case 0x1000:
						cR = CardStatus.STATUS_DESTROY_CONFIRMED;
						break;
					case 0x2000:
						cR = CardStatus.STATUS_LEAVE_CONFIRMED;
						break;
					case 0x4000:
						cR = CardStatus.STATUS_BATTLE_DESTROYED;
						break;
					case 0x8000:
						cR = CardStatus.STATUS_COPYING_EFFECT;
						break;
					case 0x10000:
						cR = CardStatus.STATUS_CHAINING;
						break;
					case 0x20000:
						cR = CardStatus.STATUS_SUMMON_DISABLED;
						break;
					case 0x40000:
						cR = CardStatus.STATUS_ACTIVATE_DISABLED;
						break;
					case 0x80000:
						cR = CardStatus.STATUS_UNSUMMONABLE_CARD;
						break;
					case 0x100000:
						cR = CardStatus.STATUS_UNION;
						break;
					case 0x200000:
						cR = CardStatus.STATUS_ATTACK_CANCELED;
						break;
					case 0x400000:
						cR = CardStatus.STATUS_INITIALIZING;
						break;
					case 0x800000:
						cR = CardStatus.STATUS_ACTIVATED;
						break;
					case 0x1000000:
						cR = CardStatus.STATUS_JUST_POS;
						break;
					case 0x2000000:
						cR = CardStatus.STATUS_CONTINUOUS_POS;
						break;
					case 0x4000000:
						cR = CardStatus.STATUS_IS_PUBLIC;
						break;
					default:
						break;
				}
				return cR;
			}
		}	
	public enum Phase{
		PHASE_DRAW (0x1),
		PHASE_STANDBY(0x2),
		PHASE_MAIN1(0x4),
		PHASE_BATTLE(0x8),
		PHASE_DAMAGE(0x10),
		PHASE_DAMAGE_CAL(0x20),
		PHASE_MAIN2 (0x40),
		PHASE_END (0x80);
		private int value;
		private Phase(int value){this.value = value;}
		public int getValue()
		{
			return this.value;
		}
		public static Phase toPhase(int value)
		{
			Phase phase= Phase.PHASE_DRAW;
			switch(value)
			{
				case 0x1:
					phase = Phase.PHASE_DRAW;
					break;
				case 0x2:
					phase = Phase.PHASE_STANDBY;
					break;
				case 0x4:
					phase = Phase.PHASE_MAIN1;
					break;
				case 0x8:
					phase = Phase.PHASE_BATTLE;
					break;
				case 0x10:
					phase = Phase.PHASE_DAMAGE;
					break;
				case 0x20:
					phase = Phase.PHASE_DAMAGE_CAL;
					break;
				case 0x40:
					phase = Phase.PHASE_MAIN2;
					break;
				case 0x80:
					phase = Phase.PHASE_END;
					break;
				default:
					break;
			}
			return phase;
		}
	}
	public static String FormatPhase(int phase) {
		String result = "";
		if (phase == 0x1) result = "抽卡阶段";
		if (phase == 0x2) result = "准备阶段";
		if (phase == 0x4) result = "主要阶段1";
		if (phase == 0x8) result = "战斗阶段";
		if (phase == 0x10) result = "伤害阶段";
		if (phase == 0x20) result = "伤害计算阶段";
		if (phase == 0x40) result = "主要阶段2";
		if (phase == 0x80) result = "结束阶段";
		return result;
	}
	public static String FormatLocation(int location) {
		CardLocation loc = CardLocation.toCardLocation(location);
		switch (loc) {
			case LOCATION_GRAVE: return "墓地";
			case LOCATION_DECK: return "卡组";
			case LOCATION_HAND : return "手牌";
			case LOCATION_MZONE : return "怪物";
			case LOCATION_SZONE : return "魔陷";
			case LOCATION_REMOVED : return "除外";
			case LOCATION_EXTRA : return "额外";
			case LOCATION_OVERLAY : return "叠放";
		}
		return "";
	}
	public static String FormatPosition(int position) {
		CardPosition pos = CardPosition.toCardPosition(position);
		switch (pos) {
			case POS_FACEDOWN_ATTACK : return "里侧攻击";
			case POS_FACEUP_ATTACK : return "表侧攻击";
			case POS_FACEUP_DEFENCE : return "表侧守备";
			case POS_FACEDOWN_DEFENCE : return "里侧守备";
			case POS_ATTACK : return "攻击";
			case POS_DEFENCE : return "守备";
			case POS_FACEUP : return "表侧";
			case POS_FACEDOWN : return "里侧";
		}
		return "";
	}
	public static String FormatPosition2(int position) {
		//CardPosition pos = (CardPosition)position;
		if ((position & CardPosition.POS_FACEUP.getValue() ) != 0)
			return "表侧";
		else if ((position & CardPosition.POS_FACEDOWN.getValue() ) != 0)
			return "里侧";
		else
			return "";
	}
	public static String FormatType(int type) {
		String result = "";
		if ((type & 0x1) != 0) result += "怪物|";
		if ((type & 0x2) != 0) result += "魔法|";
		if ((type & 0x4) != 0) result += "陷阱|";
		if ((type & 0x10) != 0) result += "通常|";
		if ((type & 0x20) != 0) result += "效果|";
		if ((type & 0x40) != 0) result += "融合|";
		if ((type & 0x80) != 0) result += "仪式|";
		if ((type & 0x100) != 0) result += "陷阱怪物|";
		if ((type & 0x200) != 0) result += "灵魂|";
		if ((type & 0x400) != 0) result += "同盟|";
		if ((type & 0x800) != 0) result += "二重|";
		if ((type & 0x1000) != 0) result += "协调|";
		if ((type & 0x2000) != 0) result += "同调|";
		if ((type & 0x4000) != 0) result += "TOKEN|";
		if ((type & 0x10000) != 0) result += "速攻|";
		if ((type & 0x20000) != 0) result += "永续|";
		if ((type & 0x40000) != 0) result += "装备|";
		if ((type & 0x80000) != 0) result += "场地|";
		if ((type & 0x100000) != 0) result += "反击|";
		if ((type & 0x200000) != 0) result += "翻转|";
		if ((type & 0x400000) != 0) result += "卡通|";
		if ((type & 0x800000) != 0) result += "超量|";
		return result.endsWith("|")?result.substring(0, result.length()-1):result;
	}
	public static String FormatAttribute(int attribute) {
		String result = "";
		if ((attribute & 0x1) != 0) result += "地|";
		if ((attribute & 0x2) != 0) result += "水|";
		if ((attribute & 0x4) != 0) result += "炎|";
		if ((attribute & 0x8) != 0) result += "风|";
		if ((attribute & 0x10) != 0) result += "光|";
		if ((attribute & 0x20) != 0) result += "暗|";
		if ((attribute & 0x40) != 0) result += "神|";
		//return result.TrimEnd('|');
		return result.endsWith("|")?result.substring(0, result.length()-1):result;
	}
	public static String FormatRace(int race) {
		String result = "";
		if ((race & 0x1) != 0) result += "战士|";
		if ((race & 0x2) != 0) result += "魔法使|";
		if ((race & 0x4) != 0) result += "天使|";
		if ((race & 0x8) != 0) result += "恶魔|";
		if ((race & 0x10) != 0) result += "不死|";
		if ((race & 0x20) != 0) result += "机械|";
		if ((race & 0x40) != 0) result += "水|";
		if ((race & 0x80) != 0) result += "炎|";
		if ((race & 0x100) != 0) result += "岩石|";
		if ((race & 0x200) != 0) result += "鸟兽|";
		if ((race & 0x400) != 0) result += "植物|";
		if ((race & 0x800) != 0) result += "昆虫|";
		if ((race & 0x1000) != 0) result += "雷|";
		if ((race & 0x2000) != 0) result += "龙|";
		if ((race & 0x4000) != 0) result += "兽|";
		if ((race & 0x8000) != 0) result += "兽战士|";
		if ((race & 0x10000) != 0) result += "恐龙|";
		if ((race & 0x20000) != 0) result += "鱼|";
		if ((race & 0x40000) != 0) result += "海龙|";
		if ((race & 0x80000) != 0) result += "爬虫|";
		if ((race & 0x100000) != 0) result += "念动力|";
		if ((race & 0x200000) != 0) result += "幻神兽|";
		return result.endsWith("|")?result.substring(0, result.length()-1):result;
	}
	public static String FormatReason(int reason) {
		String result = "";
		if ((reason & 0x1) != 0) result += "破坏|";
		if ((reason & 0x2) != 0) result += "解放|";
		if ((reason & 0x4) != 0) result += "暂时|";
		if ((reason & 0x8) != 0) result += "素材|";
		if ((reason & 0x10) != 0) result += "召唤|";
		if ((reason & 0x20) != 0) result += "战斗|";
		if ((reason & 0x40) != 0) result += "效果|";
		if ((reason & 0x80) != 0) result += "代价|";
		if ((reason & 0x100) != 0) result += "手牌调整|";
		if ((reason & 0x200) != 0) result += "失去装备对象|";
		if ((reason & 0x400) != 0) result += "规则|";
		if ((reason & 0x800) != 0) result += "特殊召唤|";
		if ((reason & 0x1000) != 0) result += "召唤无效化|";
		if ((reason & 0x2000) != 0) result += "翻转|";
		if ((reason & 0x4000) != 0) result += "丢弃|";
		if ((reason & 0x8000) != 0) result += "--|";
		if ((reason & 0x10000) != 0) result += "--|";
		if ((reason & 0x20000) != 0) result += "返回|";
		if ((reason & 0x40000) != 0) result += "融合|";
		if ((reason & 0x80000) != 0) result += "同调|";
		if ((reason & 0x100000) != 0) result += "仪式|";
		if ((reason & 0x200000) != 0) result += "超量|";
		return result.endsWith("|")?result.substring(0, result.length()-1):result;
	}
}