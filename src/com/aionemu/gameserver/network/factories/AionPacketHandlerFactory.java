/**
 * This file is part of Encom.
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
package com.aionemu.gameserver.network.factories;

import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.AionPacketHandler;
import com.aionemu.gameserver.network.aion.clientpackets.*;

public class AionPacketHandlerFactory {

	private AionPacketHandler handler;
	
	public static AionPacketHandlerFactory getInstance() {
		return SingletonHolder.instance;
	}
	
	public AionPacketHandlerFactory() {//5.5 opcodes (-1 || +3)
		handler = new AionPacketHandler();
		addPacket(new CM_VERSION_CHECK(0x00db, State.CONNECTED)); //5.8
		addPacket(new CM_TIME_CHECK(0x00e9, State.CONNECTED, State.AUTHED, State.IN_GAME)); //5.8
		addPacket(new CM_L2AUTH_LOGIN_CHECK(0x015d, State.CONNECTED)); //5.8
		addPacket(new CM_MAC_ADDRESS(0x0185, State.CONNECTED, State.AUTHED, State.IN_GAME)); //5.8
		addPacket(new CM_CHARACTER_LIST(0x0152, State.AUTHED)); //5.8
		addPacket(new CM_PING(0x02f7, State.AUTHED, State.IN_GAME)); //5.8
		addPacket(new CM_S_REP_WEB_SESSIONKEY(0x0105, State.CONNECTED, State.AUTHED, State.IN_GAME)); //5.8
		addPacket(new CM_CHARACTER_PASSKEY(0x019e, State.AUTHED)); //5.8
		addPacket(new CM_MAY_LOGIN_INTO_GAME(0x0186, State.AUTHED)); //5.8
		addPacket(new CM_ENTER_WORLD(0x00c3, State.AUTHED)); //5.8
		addPacket(new CM_LEVEL_READY(0x00c0, State.IN_GAME)); //5.8
		addPacket(new CM_MAY_QUIT(0x00cf, State.AUTHED, State.IN_GAME)); //5.8
		addPacket(new CM_QUIT(0x00ce, State.AUTHED, State.IN_GAME)); //5.8
		addPacket(new CM_GET_HOUSE_BIDS(0x01a6, State.IN_GAME)); //5.8
		addPacket(new CM_SHOW_BLOCKLIST(0x017a, State.IN_GAME)); //5.8
		addPacket(new CM_FRIEND_STATUS(0x0176, State.IN_GAME)); //5.8
		addPacket(new CM_SHOW_FRIENDLIST(0x01a2, State.IN_GAME)); //5.8
		addPacket(new CM_LEGION_SEND_EMBLEM(0x00eb, State.IN_GAME)); //5.8
		addPacket(new CM_ATREIAN_PASSPORT(0x01b0, State.IN_GAME)); //5.8
		addPacket(new CM_MOVE_ITEM(0x0164, State.IN_GAME)); //5.8
		addPacket(new CM_CHAT_MESSAGE_PUBLIC(0x00e6, State.IN_GAME)); //5.8
		addPacket(new CM_INSTANCE_INFO(0x0198, State.IN_GAME)); //5.8
		addPacket(new CM_TARGET_SELECT(0x00fa, State.IN_GAME)); //5.8
		addPacket(new CM_SHOW_DIALOG(0x02ff, State.IN_GAME)); //5.8
		addPacket(new CM_DIALOG_SELECT(0x02fd, State.IN_GAME)); //5.8
		addPacket(new CM_CLOSE_DIALOG(0x02fc, State.IN_GAME)); //5.8
		addPacket(new CM_EMOTION(0x02f6, State.IN_GAME)); //5.8
		addPacket(new CM_CHAT_AUTH(0x018a, State.IN_GAME)); //5.8
		addPacket(new CM_BUY_ITEM(0x02fe, State.IN_GAME)); //5.8
		addPacket(new CM_UI_SETTINGS(0x00c1, State.IN_GAME)); //5.8
		addPacket(new CM_CREATE_CHARACTER(0x0153, State.AUTHED)); //5.8
		addPacket(new CM_CHECK_NICKNAME(0x0189, State.AUTHED)); //5.8
		addPacket(new CM_RECONNECT_AUTH(0x0173, State.AUTHED)); //5.8
		addPacket(new CM_PLAY_MOVIE_END(0x0128, State.IN_GAME)); //5.8
		addPacket(new CM_MOVE(0x010b, State.IN_GAME)); //5.8
		addPacket(new CM_MINIONS(0x1D1, State.IN_GAME)); //5.8
		addPacket(new CM_CASTSPELL(0x00f8, State.IN_GAME)); //5.8
		addPacket(new CM_ATTACK(0x00fb, State.IN_GAME)); //5.8
		addPacket(new CM_CHAT_MESSAGE_WHISPER(0x00e7, State.IN_GAME)); //5.8
		addPacket(new CM_USE_ITEM(0xEC, State.IN_GAME)); //5.8
		addPacket(new CM_EQUIP_ITEM(0x00ed, State.IN_GAME)); //5.8
		addPacket(new CM_ABYSS_RANKING_PLAYERS(0x0184, State.IN_GAME)); //5.8
		addPacket(new CM_ABYSS_RANKING_LEGIONS(0x0132, State.IN_GAME)); //5.8
		addPacket(new CM_SET_NOTE(0x02f1, State.IN_GAME)); //5.8
		addPacket(new CM_PLAYER_SEARCH(0x017b, State.IN_GAME)); //5.8
		addPacket(new CM_PING_REQUEST(0x0123, State.IN_GAME)); //5.8
		addPacket(new CM_DELETE_ITEM(0x013c, State.IN_GAME)); //5.8
		addPacket(new CM_MACRO_CREATE(0x018b, State.IN_GAME)); //5.8
		addPacket(new CM_SPLIT_ITEM(0x0165, State.IN_GAME)); //5.8
		addPacket(new CM_HOTSPOT_TELEPORT(0x01bc, State.IN_GAME)); //5.8
		addPacket(new CM_CUBE_EXPAND(0x01c7, State.IN_GAME)); //5.8
		addPacket(new CM_MACRO_DELETE(0x0188, State.IN_GAME)); //5.8
		addPacket(new CM_GATHER(0x00de, State.IN_GAME)); //5.8
		addPacket(new CM_START_LOOT(0x0166, State.IN_GAME)); //5.8
		addPacket(new CM_LOOT_ITEM(0x0167, State.IN_GAME)); //5.8
		addPacket(new CM_ABYSS_LANDING(0x0107, State.IN_GAME)); //5.8
		addPacket(new CM_QUESTIONNAIRE(0x0169, State.IN_GAME)); //5.8
		addPacket(new CM_OBJECT_SEARCH(0x00d6, State.IN_GAME)); //5.8
		addPacket(new CM_PRIVATE_STORE(0x0133, State.IN_GAME)); //5.8
		addPacket(new CM_PRIVATE_STORE_NAME(0x130, State.IN_GAME)); //5.8
		addPacket(new CM_TELEPORT_SELECT(0x015c, State.IN_GAME)); //5.8
		addPacket(new CM_TEAM_INVITE(0x0139, State.IN_GAME)); //5.8
		addPacket(new CM_VIEW_PLAYER_DETAILS(0x012c, State.IN_GAME)); //5.8
		addPacket(new CM_DUEL_REQUEST(0x013e, State.IN_GAME)); //5.8
		addPacket(new CM_SHOW_BRAND(0x017d, State.IN_GAME)); //5.8
		addPacket(new CM_TOGGLE_SKILL_DEACTIVATE(0x00f9, State.IN_GAME)); //5.8
		addPacket(new CM_CUSTOM_SETTINGS(0x00d7, State.IN_GAME)); //5.8
		addPacket(new CM_QUESTION_RESPONSE(0x0109, State.IN_GAME)); //5.8
		addPacket(new CM_MOVE_IN_AIR(0x0108, State.IN_GAME)); //5.8
		addPacket(new CM_TELEPORT_DONE(0x00ea, State.IN_GAME)); //5.8
		addPacket(new CM_STOP_TRAINING(0x011f, State.IN_GAME)); //5.8
		addPacket(new CM_REVIVE(0x00cc, State.IN_GAME)); //5.8
		addPacket(new CM_INSTANCE_LEAVE(0x02f5, State.IN_GAME)); //5.8
		addPacket(new CM_ENCHANMENT_STONES(0x0101, State.IN_GAME)); //5.8
		addPacket(new CM_FRIEND_ADD(0x014b, State.IN_GAME)); //5.8
		addPacket(new CM_FRIEND_EDIT(0x1CB, State.IN_GAME));
		addPacket(new CM_BLOCK_ADD(0x0162, State.IN_GAME)); //5.8
		addPacket(new CM_TITLE_SET(0x0157, State.IN_GAME)); //5.8
		addPacket(new CM_BONUS_TITLE(0x01a1, State.IN_GAME)); //5.8
		addPacket(new CM_MOTION(0x0102, State.IN_GAME)); //5.8
		addPacket(new CM_MEGAPHONE_MESSAGE(0x01b5, State.IN_GAME)); //5.8
		addPacket(new CM_AETHERFORGING(0x01d4, State.IN_GAME)); //5.8
		addPacket(new CM_WINDSTREAM(0x010d, State.IN_GAME)); //5.8
		addPacket(new CM_FUSION_WEAPONS(0x01aa, State.IN_GAME)); //5.8
		addPacket(new CM_TUNE(0x1B7, State.IN_GAME)); //5.8
		addPacket(new CM_ENCHANTMENT_EXTRACTION(0x01cc, State.IN_GAME)); //5.8
		addPacket(new CM_REMOVE_ALTERED_STATE(0x00ee, State.IN_GAME)); //5.8
		addPacket(new CM_REPLACE_ITEM(0x17e, State.IN_GAME)); //5.8
		addPacket(new CM_SELL_TERMINATED_ITEMS(0x1C0, State.IN_GAME)); // 5.6 TODO
		addPacket(new CM_BUY_TRADE_IN_TRADE(0x113, State.IN_GAME)); //5.8
		addPacket(new CM_PET(0xDD, State.IN_GAME)); //5.8
		addPacket(new CM_CHARGE_ITEM(0x0115, State.IN_GAME)); //5.8
		addPacket(new CM_GM_BOOKMARK(0x00e0, State.IN_GAME)); //5.8
		addPacket(new CM_DELETE_QUEST(0x012b, State.IN_GAME)); //5.8
		addPacket(new CM_QUEST_SHARE(0x016c, State.IN_GAME)); //5.8
		addPacket(new CM_EVERGALE_CANYON(0x01d0, State.IN_GAME)); //5.8
		addPacket(new CM_ITEM_REMODEL(0x0111, State.IN_GAME)); //5.8
		addPacket(new CM_OPEN_STATICDOOR(0x00d2, State.IN_GAME)); //5.8
		addPacket(new CM_APPEARANCE(0x018d, State.IN_GAME)); //5.8
		addPacket(new CM_CRAFT(0x0155, State.IN_GAME)); //5.8
		addPacket(new CM_CHARACTER_EDIT(0x00c2, State.AUTHED)); //5.8
		addPacket(new CM_RECIPE_DELETE(0x0110, State.IN_GAME)); //5.8
		addPacket(new CM_SELECT_ITEM(0x01b4, State.IN_GAME)); //5.8
		addPacket(new CM_CHARGE_SKILL(0x01b6, State.IN_GAME)); //5.8
		addPacket(new CM_COMPOSITE_STONES(0x1a8, State.IN_GAME)); //5.8
		addPacket(new CM_LEGION(0x02f4, State.IN_GAME)); //5.8
		addPacket(new CM_LEGION_SEND_EMBLEM_INFO(0x010a, State.IN_GAME)); //5.8
		addPacket(new CM_LEGION_TABS(0x02f2, State.IN_GAME)); //5.8
		addPacket(new CM_CHALLENGE_LIST(0x01a0, State.IN_GAME)); //5.8
		addPacket(new CM_FRIEND_DEL(0x0148, State.IN_GAME)); //5.8
		addPacket(new CM_CHAT_PLAYER_INFO(0x00E2, State.IN_GAME)); //5.8
		addPacket(new CM_BREAK_WEAPONS(0x01ab, State.IN_GAME)); //5.8
		addPacket(new CM_PET_EMOTE(0x00dc, State.IN_GAME)); //5.8
		addPacket(new CM_MARK_FRIENDLIST(0x14a, State.IN_GAME)); //5.8
		addPacket(new CM_BLOCK_SET_REASON(0x017f, State.IN_GAME)); //5.8
		addPacket(new CM_BLOCK_DEL(0x0163, State.IN_GAME)); //5.8
		addPacket(new CM_GG(0x0120, State.IN_GAME)); //5.8
		addPacket(new CM_SHOW_MAP(0x00e3, State.IN_GAME)); //5.8
		addPacket(new CM_ATREIAN_BESTIARY_LVLUP(0x01dc, State.IN_GAME)); //5.8
		addPacket(new CM_COMPETITION_RANKING(0x01e8, State.IN_GAME)); //5.8
		addPacket(new CM_HOT_SPECTATE(0x0172, State.IN_GAME)); //5.8
		addPacket(new CM_DELETE_CHARACTER(0x0150, State.AUTHED)); //5.8
		addPacket(new CM_PURIFICATION_ITEM(0x01B3, State.IN_GAME)); //5.8
		addPacket(new CM_UPGRADE_ARCADE(0x01b2, State.IN_GAME)); //5.8
		addPacket(new CM_RESTORE_CHARACTER(0x151, State.AUTHED)); //5.8
		addPacket(new CM_LEGION_JOIN_REQUEST(0x1C5, State.IN_GAME)); //5.8
		addPacket(new CM_LEGION_JOIN_CANCEL(0x1C6, State.IN_GAME)); //5.8
		addPacket(new CM_STONESPEAR_SIEGE(0x00E4, State.IN_GAME)); //5.8
		addPacket(new CM_SEASON_RANKING(0x1E8, State.IN_GAME)); //5.8
		addPacket(new CM_MY_HISTORY(0x1E9, State.IN_GAME)); //5.8
		addPacket(new CM_CHECK_MAIL_SIZE(0x014d, State.IN_GAME)); //5.8
		addPacket(new CM_CHECK_MAIL_SIZE_2(0x019d, State.IN_GAME)); //5.8
		addPacket(new CM_READ_MAIL(0x0142, State.IN_GAME)); //5.8
		addPacket(new CM_GET_MAIL_ATTACHMENT(0x0140, State.IN_GAME)); //5.8
		addPacket(new CM_DELETE_MAIL(0x0141, State.IN_GAME)); //5.8
		addPacket(new CM_READ_EXPRESS_MAIL(0x016e, State.IN_GAME)); //5.8
		addPacket(new CM_SEND_MAIL(0x014c, State.IN_GAME)); //5.8
		addPacket(new CM_BROKER_LIST(0x0147, State.IN_GAME)); //5.8
		addPacket(new CM_BROKER_SEARCH(0x01c1, State.IN_GAME)); //5.8
		addPacket(new CM_BROKER_REGISTERED_LIST(0x0145, State.IN_GAME)); //5.8
		addPacket(new CM_BROKER_START_REGISTER(0x013d, State.IN_GAME)); //5.8
		addPacket(new CM_BROKER_SOLD_LIST(0x0159, State.IN_GAME)); //5.8
		addPacket(new CM_REGISTER_BROKER_ITEM(0x015b, State.IN_GAME)); //5.8
		addPacket(new CM_BROKER_CANCEL_REGISTERED(0x0158, State.IN_GAME)); //5.8
		addPacket(new CM_BUY_BROKER_ITEM(0x015a, State.IN_GAME)); //5.8
		addPacket(new CM_BROKER_COLLECT_SOLD_ITEMS(0x014e, State.IN_GAME)); //5.8
		addPacket(new CM_SUMMON_MOVE(0x0181, State.IN_GAME)); //5.8
		addPacket(new CM_SUMMON_COMMAND(0x0131, State.IN_GAME)); //5.8
		addPacket(new CM_SUMMON_ATTACK(0x0197, State.IN_GAME)); //5.8
		addPacket(new CM_SUMMON_CASTSPELL(0x0195, State.IN_GAME)); //5.8
		addPacket(new CM_SUMMON_EMOTION(0x0196, State.IN_GAME)); //5.8
		addPacket(new CM_LEGION_MODIFY_EMBLEM(0x0106, State.IN_GAME)); //5.8
		addPacket(new CM_LEGION_UPLOAD_INFO(0x0178, State.IN_GAME)); //5.8
		addPacket(new CM_LEGION_UPLOAD_EMBLEM(0x0179, State.IN_GAME)); //5.8
		addPacket(new CM_LEGION_SEARCH(0x01c4, State.IN_GAME)); //5.8
		addPacket(new CM_LEGION_WH_KINAH(0x0117, State.IN_GAME)); //5.8
		addPacket(new CM_EXCHANGE_REQUEST(0x011a, State.IN_GAME)); //5.8
		addPacket(new CM_EXCHANGE_ADD_ITEM(0x011b, State.IN_GAME)); //5.8
		addPacket(new CM_EXCHANGE_ADD_KINAH(0x0119, State.IN_GAME)); //5.8
		addPacket(new CM_EXCHANGE_CANCEL(0x010c, State.IN_GAME)); //5.8
		addPacket(new CM_EXCHANGE_LOCK(0x010e, State.IN_GAME)); //5.8
		addPacket(new CM_EXCHANGE_OK(0x010f, State.IN_GAME)); //5.8
		addPacket(new CM_BUTLER_SALUTE(0x012a, State.IN_GAME)); //5.8
		addPacket(new CM_HOUSE_EDIT(0x0129, State.IN_GAME)); //5.8
		addPacket(new CM_HOUSE_SCRIPT(0x00e5, State.IN_GAME)); //5.8
		addPacket(new CM_HOUSE_KICK(0x0103, State.IN_GAME)); //5.8
		addPacket(new CM_HOUSE_SETTINGS(0x0100, State.IN_GAME)); //5.8
		addPacket(new CM_HOUSE_TELEPORT(0x01ba, State.IN_GAME)); //5.8
		addPacket(new CM_PLACE_BID(0x01a5, State.IN_GAME)); //5.8
		addPacket(new CM_HOUSE_DECORATE(0x116, State.IN_GAME)); //5.8
		addPacket(new CM_USE_HOUSE_OBJECT(0x1b8, State.IN_GAME)); //5.8
		addPacket(new CM_RELEASE_HOUSE_OBJECT(0x1b9, State.IN_GAME)); //5.8
		addPacket(new CM_HOUSE_TELEPORT_BACK(0x13a, State.IN_GAME)); //5.8
		addPacket(new CM_HOUSE_PAY_RENT(0x01bb, State.IN_GAME)); //5.8
		addPacket(new CM_HOUSE_OPEN_DOOR(0x01AE, State.IN_GAME)); //5.8
		addPacket(new CM_REGISTER_HOUSE(0x01A7, State.IN_GAME)); //5.8
		addPacket(new CM_AUTO_GROUP(0x180, State.IN_GAME)); //5.8
		addPacket(new CM_DISTRIBUTION_SETTINGS(0x0171, State.IN_GAME)); //5.8
		addPacket(new CM_GROUP_DISTRIBUTION(0x0134, State.IN_GAME)); //5.8
		addPacket(new CM_FIND_GROUP(0x0114, State.IN_GAME)); //5.8
		addPacket(new CM_GROUP_PLAYER_STATUS_INFO(0x0138, State.IN_GAME)); //5.8
		addPacket(new CM_CLIENT_COMMAND_ROLL(0x0137, State.IN_GAME)); //5.8
		addPacket(new CM_CHAT_GROUP_INFO(0x104, State.IN_GAME)); //5.8
		addPacket(new CM_GROUP_LOOT(0x175, State.IN_GAME)); //5.8
		addPacket(new CM_GROUP_DATA_EXCHANGE(0xB3, State.IN_GAME)); //5.8
		addPacket(new CM_SKILL_ANIMATION(0x1D5, State.IN_GAME)); //5.8
		addPacket(new CM_TUNE_RESULT(0x1CA, State.IN_GAME)); //5.8
		addPacket(new CM_INTRUDER_SCAN(0x018F, State.IN_GAME)); //5.8
		addPacket(new CM_BATTLEFIELD_UNION_REGISTER(0x1EB, State.IN_GAME)); //5.8
		addPacket(new CM_AUTOMATIC_BERDIN_STAR(0x1EA, State.IN_GAME)); //5.8
		addPacket(new CM_SHUGO_SWEEP(0x1D6, State.IN_GAME)); //5.8
		addPacket(new CM_COALESCENCE_STARTUP(0x01DE, State.IN_GAME)); //5.8
		addPacket(new CM_COALESCENCE(0x01D7, State.IN_GAME)); //5.8
		addPacket(new CM_SECURITY_TOKEN(0x1AD, State.IN_GAME)); // 5.8 EU
		addPacket(new CM_LUNA_SHOP(0x1C3, State.IN_GAME)); // 5.8 EU
		addPacket(new CM_REPORT_PLAYER(0x19B, State.IN_GAME)); //5.8
		addPacket(new CM_CREATIVITY_POINTS(0x122, State.IN_GAME)); //5.8
        addPacket(new CM_GAMEGUARD(0x120, State.IN_GAME)); // 5.8

		addPacket(new CM_A_STATION(0x191, State.IN_GAME)); //To Do KR 5.8
		addPacket(new CM_A_STATION_CHECK(0x193, State.IN_GAME)); //5.8 To Do
		addPacket(new CM_CHANGE_CHANNEL(0x174, State.IN_GAME)); //5.8 To Do Was 176
		addPacket(new CM_TELEPORT_BACK(0x13B, State.IN_GAME));
		addPacket(new CM_PLAYER_STATUS_INFO(0x138, State.IN_GAME));
		
		//draft
		addPacket(new CM_UNK_127(0x127, State.AUTHED)); // 5.8
	}
	
	public AionPacketHandler getPacketHandler() {
		return handler;
	}
	
	private void addPacket(AionClientPacket prototype) {
		handler.addPacketPrototype(prototype);
	}
	
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder {
		protected static final AionPacketHandlerFactory instance = new AionPacketHandlerFactory();
	}
}