/*
 * This file is part of Encom. **ENCOM FUCK OTHER SVN**
 *
 *  Encom is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Encom is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser Public License
 *  along with Encom.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.configs.main;

import com.aionemu.commons.configuration.Property;

public class RateConfig
{

	/**
	 * Player glory Points Rates - Regular (1), Premium (2), VIP (3)
	 */
	@Property(key = "gameserver.rate.regular.gp.player.loss", defaultValue = "1.0")
	public static float GP_PLAYER_LOSS_RATE;
	@Property(key = "gameserver.rate.premium.gp.player.loss", defaultValue = "2.0")
	public static float PREMIUM_GP_PLAYER_LOSS_RATE;
	@Property(key = "gameserver.rate.vip.gp.player.loss", defaultValue = "3.0")
	public static float VIP_GP_PLAYER_LOSS_RATE;
	@Property(key = "gameserver.rate.regular.xp", defaultValue = "1")
	public static float XP_RATE;
	@Property(key = "gameserver.rate.premium.xp", defaultValue = "2")
	public static float PREMIUM_XP_RATE;
	@Property(key = "gameserver.rate.vip.xp", defaultValue = "3")
	public static float VIP_XP_RATE;
	@Property(key = "gameserver.rate.regular.group.xp", defaultValue = "1")
	public static float GROUPXP_RATE;
	@Property(key = "gameserver.rate.premium.group.xp", defaultValue = "2")
	public static float PREMIUM_GROUPXP_RATE;
	@Property(key = "gameserver.rate.vip.group.xp", defaultValue = "3")
	public static float VIP_GROUPXP_RATE;
	@Property(key = "gameserver.rate.regular.quest.xp", defaultValue = "1")
	public static float QUEST_XP_RATE;
	@Property(key = "gameserver.rate.premium.quest.xp", defaultValue = "2")
	public static float PREMIUM_QUEST_XP_RATE;
	@Property(key = "gameserver.rate.vip.quest.xp", defaultValue = "3")
	public static float VIP_QUEST_XP_RATE;
	@Property(key = "gameserver.rate.regular.gathering.xp", defaultValue = "1")
	public static float GATHERING_XP_RATE;
	@Property(key = "gameserver.rate.premium.gathering.xp", defaultValue = "2")
	public static float PREMIUM_GATHERING_XP_RATE;
	@Property(key = "gameserver.rate.vip.gathering.xp", defaultValue = "3")
	public static float VIP_GATHERING_XP_RATE;
    @Property(key="gameserver.rate.regular.gathering.count", defaultValue="1")
    public static int GATHERING_COUNT_RATE;
    @Property(key="gameserver.rate.premium.gathering.count", defaultValue="1")
    public static int PREMIUM_GATHERING_COUNT_RATE;
    @Property(key="gameserver.rate.vip.gathering.count", defaultValue="1")
    public static int VIP_GATHERING_COUNT_RATE;
	@Property(key = "gameserver.rate.regular.crafting.xp", defaultValue = "1")
	public static float CRAFTING_XP_RATE;
	@Property(key = "gameserver.rate.premium.crafting.xp", defaultValue = "2")
	public static float PREMIUM_CRAFTING_XP_RATE;
	@Property(key = "gameserver.rate.vip.crafting.xp", defaultValue = "3")
	public static float VIP_CRAFTING_XP_RATE;
	@Property(key = "gameserver.rate.regular.quest.kinah", defaultValue = "1")
	public static float QUEST_KINAH_RATE;
	@Property(key = "gameserver.rate.premium.quest.kinah", defaultValue = "2")
	public static float PREMIUM_QUEST_KINAH_RATE;
	@Property(key = "gameserver.rate.vip.quest.kinah", defaultValue = "3")
	public static float VIP_QUEST_KINAH_RATE;
	@Property(key = "gameserver.rate.regular.drop", defaultValue = "1")
	public static float DROP_RATE;
	@Property(key = "gameserver.rate.premium.drop", defaultValue = "2")
	public static float PREMIUM_DROP_RATE;
	@Property(key = "gameserver.rate.vip.drop", defaultValue = "3")
	public static float VIP_DROP_RATE;
	@Property(key = "gameserver.rate.reduce.drop", defaultValue = "0.0")
	public static float DROP_RATE_REDUCE;
	@Property(key = "gameserver.rate.regular.ap.player.gain", defaultValue = "1")
	public static float AP_PLAYER_GAIN_RATE;
	@Property(key = "gameserver.rate.premium.ap.player.gain", defaultValue = "2")
	public static float PREMIUM_AP_PLAYER_GAIN_RATE;
	@Property(key = "gameserver.rate.vip.ap.player.gain", defaultValue = "3")
	public static float VIP_AP_PLAYER_GAIN_RATE;
	@Property(key = "gameserver.rate.regular.xp.player.gain", defaultValue = "1")
	public static float XP_PLAYER_GAIN_RATE;
	@Property(key = "gameserver.rate.premium.xp.player.gain", defaultValue = "2")
	public static float PREMIUM_XP_PLAYER_GAIN_RATE;
	@Property(key = "gameserver.rate.vip.xp.player.gain", defaultValue = "3")
	public static float VIP_XP_PLAYER_GAIN_RATE;
	@Property(key = "gameserver.rate.regular.ap.player.loss", defaultValue = "1")
	public static float AP_PLAYER_LOSS_RATE;
	@Property(key = "gameserver.rate.premium.ap.player.loss", defaultValue = "2")
	public static float PREMIUM_AP_PLAYER_LOSS_RATE;
	@Property(key = "gameserver.rate.vip.ap.player.loss", defaultValue = "3")
	public static float VIP_AP_PLAYER_LOSS_RATE;
	@Property(key = "gameserver.rate.regular.ap.npc", defaultValue = "1")
	public static float AP_NPC_RATE;
	@Property(key = "gameserver.rate.premium.ap.npc", defaultValue = "2")
	public static float PREMIUM_AP_NPC_RATE;
	@Property(key = "gameserver.rate.vip.ap.npc", defaultValue = "3")
	public static float VIP_AP_NPC_RATE;
	@Property(key = "gameserver.rate.regular.dp.npc", defaultValue = "1")
	public static float DP_NPC_RATE;
	@Property(key = "gameserver.rate.premium.dp.npc", defaultValue = "2")
	public static float PREMIUM_DP_NPC_RATE;
	@Property(key = "gameserver.rate.vip.dp.npc", defaultValue = "3")
	public static float VIP_DP_NPC_RATE;
	@Property(key = "gameserver.rate.regular.dp.player", defaultValue = "1")
	public static float DP_PLAYER_RATE;
	@Property(key = "gameserver.rate.premium.dp.player", defaultValue = "2")
	public static float PREMIUM_DP_PLAYER_RATE;
	@Property(key = "gameserver.rate.vip.dp.player", defaultValue = "3")
	public static float VIP_DP_PLAYER_RATE;
	@Property(key = "gameserver.rate.dredgion", defaultValue = "1.6")
	public static float DREDGION_REWARD_RATE;
	
	//Arena Pvp.
	@Property(key = "gameserver.rate.regular.pvparena.discipline", defaultValue = "1")
	public static float PVP_ARENA_DISCIPLINE_REWARD_RATE;
	@Property(key = "gameserver.rate.premium.pvparena.discipline", defaultValue = "1")
	public static float PREMIUM_PVP_ARENA_DISCIPLINE_REWARD_RATE;
	@Property(key = "gameserver.rate.vip.pvparena.discipline", defaultValue = "1")
	public static float VIP_PVP_ARENA_DISCIPLINE_REWARD_RATE;
	@Property(key = "gameserver.rate.regular.pvparena.chaos", defaultValue = "1")
	public static float PVP_ARENA_CHAOS_REWARD_RATE;
	@Property(key = "gameserver.rate.premium.pvparena.chaos", defaultValue = "1")
	public static float PREMIUM_PVP_ARENA_CHAOS_REWARD_RATE;
	@Property(key = "gameserver.rate.vip.pvparena.chaos", defaultValue = "1")
	public static float VIP_PVP_ARENA_CHAOS_REWARD_RATE;
	@Property(key = "gameserver.rate.regular.pvparena.harmony", defaultValue = "1")
	public static float PVP_ARENA_HARMONY_REWARD_RATE;
	@Property(key = "gameserver.rate.premium.pvparena.harmony", defaultValue = "1")
	public static float PREMIUM_PVP_ARENA_HARMONY_REWARD_RATE;
	@Property(key = "gameserver.rate.vip.pvparena.harmony", defaultValue = "1")
	public static float VIP_PVP_ARENA_HARMONY_REWARD_RATE;
	@Property(key = "gameserver.rate.regular.pvparena.glory", defaultValue = "1")
	public static float PVP_ARENA_GLORY_REWARD_RATE;
	@Property(key = "gameserver.rate.premium.pvparena.glory", defaultValue = "1")
	public static float PREMIUM_PVP_ARENA_GLORY_REWARD_RATE;
	@Property(key = "gameserver.rate.vip.pvparena.glory", defaultValue = "1")
	public static float VIP_PVP_ARENA_GLORY_REWARD_RATE;
	
	//Ingameshop.
	@Property(key="gameserver.rate.regular.toll.reward", defaultValue="1")
    public static float TOLL_REWARD_RATE;
	@Property(key="gameserver.rate.premium.toll.reward", defaultValue="1")
    public static float PREMIUM_TOLL_REWARD_RATE;
	@Property(key="gameserver.rate.vip.toll.reward", defaultValue="1")
    public static float VIP_TOLL_REWARD_RATE;
	
	//Abyss Points.
	@Property(key = "gameserver.rate.regular.quest.ap", defaultValue = "1")
	public static float QUEST_AP_RATE;
	@Property(key = "gameserver.rate.premium.quest.ap", defaultValue = "2")
	public static float PREMIUM_QUEST_AP_RATE;
	@Property(key = "gameserver.rate.vip.quest.ap", defaultValue = "3")
	public static float VIP_QUEST_AP_RATE;
	
	//Glory Points.
	@Property(key = "gameserver.rate.regular.quest.gp", defaultValue = "1")
	public static float QUEST_GP_RATE;
	@Property(key = "gameserver.rate.premium.quest.gp", defaultValue = "2")
	public static float PREMIUM_QUEST_GP_RATE;
	@Property(key = "gameserver.rate.vip.quest.gp", defaultValue = "3")
	public static float VIP_QUEST_GP_RATE;
	
	//Abyss Landing
	@Property(key = "gameserver.rate.regular.quest.abyss_op", defaultValue = "1")
	public static float QUEST_ABYSS_OP_RATE;
	@Property(key = "gameserver.rate.premium.quest.abyss_op", defaultValue = "2")
	public static float PREMIUM_QUEST_ABYSS_OP_RATE;
	@Property(key = "gameserver.rate.vip.quest.abyss_op", defaultValue = "3")
	public static float VIP_QUEST_ABYSS_OP_RATE;
	
	//Aura Of Growth
	@Property(key = "gameserver.rate.aura.of.growth.chance", defaultValue = "50")
	public static int AURA_OF_GROWTH;
	
	@Property(key = "gameserver.rate.regular.quest.exp_boost", defaultValue = "1")
	public static float QUEST_EXP_BOOST_RATE;
	@Property(key = "gameserver.rate.premium.quest.exp_boost", defaultValue = "2")
	public static float PREMIUM_QUEST_EXP_BOOST_RATE;
	@Property(key = "gameserver.rate.vip.quest.exp_boost", defaultValue = "3")
	public static float VIP_QUEST_EXP_BOOST_RATE;
	
	//Global Drop Rates
	@Property(key = "gameserver.rate.regular.global.drop", defaultValue = "1")
	public static float GLOBAL_DROP_RATE;
	@Property(key = "gameserver.rate.premium.global.drop", defaultValue = "2")
	public static float PREMIUM_GLOBAL_DROP_RATE;
	@Property(key = "gameserver.rate.vip.global.drop", defaultValue = "3")
	public static float VIP_GLOBAL_DROP_RATE;
	
	//Atreian Bestiary
	@Property(key = "gameserver.rate.regular.book", defaultValue = "1")
	public static float BOOK_RATE;
	@Property(key = "gameserver.rate.premium.book", defaultValue = "2")
	public static float PREMIUM_BOOK_RATE;
	@Property(key = "gameserver.rate.vip.book", defaultValue = "3")
	public static float VIP_BOOK_RATE;
	@Property(key = "gameserver.rate.normal.monster.hp", defaultValue = "0.5")
	public static double NORMAL_MOBS_RATE_HP;
	@Property(key = "gameserver.rate.normal.monster.pw", defaultValue = "0.5")
	public static double NORMAL_MOBS_RATE_PW;
	@Property(key = "gameserver.rate.elite.monster.hp", defaultValue = "1.5")
	public static double ELITE_MOBS_RATE_HP;
	@Property(key = "gameserver.rate.elite.monster.pw", defaultValue = "1.5")
	public static double ELITE_MOBS_RATE_PW;
	
	//gp get
	@Property(key = "gameserver.rate.regular.gp.player.gain", defaultValue = "1")
	public static float GP_PLAYER_GAIN_RATE;
	@Property(key = "gameserver.rate.premium.gp.player.gain", defaultValue = "2")
	public static float PREMIUM_GP_PLAYER_GAIN_RATE;
	@Property(key = "gameserver.rate.vip.gp.player.gain", defaultValue = "3")
	public static float VIP_GP_PLAYER_GAIN_RATE;
}