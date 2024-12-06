package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.configs.administration.AdminConfig;
import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.configs.main.MembershipConfig;
import com.aionemu.gameserver.dao.PlayerDAO;
import com.aionemu.gameserver.model.Gender;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PlayerAppearance;
import com.aionemu.gameserver.model.gameobjects.player.PlayerCommonData;
import com.aionemu.gameserver.model.items.GodStone;
import com.aionemu.gameserver.model.items.ItemSlot;
import com.aionemu.gameserver.model.stats.calc.Stat2;
import com.aionemu.gameserver.model.team.legion.LegionEmblem;
import com.aionemu.gameserver.model.team.legion.LegionEmblemType;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.services.EnchantService;
import com.aionemu.gameserver.services.events.DisplayService;
import com.aionemu.gameserver.services.events.FFAService;
import com.aionemu.gameserver.services.events.LadderService;
import com.aionemu.gameserver.services.events.bg.DeathmatchBg;
import com.aionemu.gameserver.services.events.bg.SoloSurvivorBg;
import com.aionemu.gameserver.model.account.Account;

import javolution.util.FastList;

public class SM_PLAYER_INFO extends AionServerPacket {

	private final Player player;
	private boolean enemy;

	public SM_PLAYER_INFO(Player player, boolean enemy) {
		this.player = player;
		this.enemy = enemy;
	}

	@Override
	protected void writeImpl(AionConnection con) {

		Player activePlayer = con.getActivePlayer();

		if (activePlayer == null || player == null) {
			return;
		}

		PlayerCommonData pcd = player.getCommonData();

		final int raceId;
		int bgIndex = 0;

		if (player.getAdminNeutral() > 1 || activePlayer.getAdminNeutral() > 1 || player.isInPvEMode() || activePlayer.isInPvEMode()) {
			raceId = activePlayer.getRace().getRaceId();
		} else if (FFAService.getInstance().isInArena(activePlayer) && activePlayer.isFFA() || activePlayer.isInPkMode() || activePlayer.isBandit()) {
			if (player.getRace() == activePlayer.getRace() && player != activePlayer) {
				raceId = (player.getRace().getRaceId() == 0 ? 1 : 0);
			} else if (player != activePlayer) {
				raceId = player.getRace().getRaceId();
			} else {
				raceId = activePlayer.getRace().getRaceId();
			}
		} else if (activePlayer.isEnemy(player)) {
			raceId = (activePlayer.getRace().getRaceId() == 0 ? 1 : 0);
		} else {
			raceId = player.getRace().getRaceId();
		}

		if (!player.isSpectating() && player.getBattleground() != null && (player.isInGroup2() || player.isInAlliance2())) {
			bgIndex = (player.isInGroup2()) ? player.getPlayerGroup2().getBgIndex() : player.getPlayerAlliance2().getBgIndex();
		} else {
			bgIndex = player.getBgIndex();
		}

		final int genderId = pcd.getGender().getGenderId();
		final PlayerAppearance playerAppearance = player.getPlayerAppearance();

		writeF(player.getX());// x
		writeF(player.getY());// y
		writeF(player.getZ());// z
		writeD(player.getObjectId());
		/**
		 * A3 female asmodian A2 male asmodian A1 female elyos A0 male elyos
		 */
		writeD(pcd.getTemplateId());
		writeD(player.getRobotId());// 4.5 protocol changed

		/**
		 * Transformed state - send transformed model id Regular state - send player
		 * model id (from common data)
		 */
		int model = player.getTransformModel().getModelId();

		writeD(model != 0 ? model : pcd.getTemplateId());
		writeC(0x00);
		writeB(new byte[19]);
		writeD(player.getTransformModel().getType().getId());

		if (player.isInPkMode() || activePlayer.isInPkMode()) {
			writeC(0x00);
		} else {
			writeC(enemy ? 0x00 : 0x26);
		}

		writeC(raceId); // race
		writeC(pcd.getPlayerClass().getClassId());
		writeC(genderId); // sex
		writeH(player.getState());
		writeB(new byte[8]);
		writeC(player.getHeading());
        String nameFormat = "%s";   
		/**
		 * Premium & VIP Membership
		 */
		StringBuilder sb = new StringBuilder(nameFormat);
		if (player.getClientConnection() != null) {
			// * = Premium & VIP Membership
			if (MembershipConfig.PREMIUM_TAG_DISPLAY_ENABLE) {
				switch (player.getClientConnection().getAccount().getMembership()) {
				case 1:
					nameFormat = sb.replace(0, sb.length(), MembershipConfig.TAG_PREMIUM).toString();
					break;
				case 2:
					nameFormat = sb.replace(0, sb.length(), MembershipConfig.TAG_VIP).toString();
					break;
				}
			}

			// * = Wedding
			if (player.isMarried()) {
				String partnerName = DAOManager.getDAO(PlayerDAO.class).getPlayerNameByObjId(player.getPartnerId());
				nameFormat += "\uE020" + partnerName;
			}

			/**
			 * WPvP Related Features
			 */
			if (player.isInPvEMode()) {
				nameFormat = sb.insert(0, CustomConfig.TAG_PVE.substring(0, 2)).toString();
			}

			if (player.isInPkMode()) {
				nameFormat = sb.insert(0, CustomConfig.TAG_PK.substring(0, 2)).toString();
			}

			// * = Server Staff Access Level
			if (AdminConfig.ADMIN_TAG_ENABLE) {
				switch (player.getClientConnection().getAccount().getAccessLevel()) {
				case 1:
					nameFormat = AdminConfig.ADMIN_TAG_1.replace("%s", sb.toString());
					break;
				case 2:
					nameFormat = AdminConfig.ADMIN_TAG_2.replace("%s", sb.toString());
					break;
				case 3:
					nameFormat = AdminConfig.ADMIN_TAG_3.replace("%s", sb.toString());
					break;
				case 4:
					nameFormat = AdminConfig.ADMIN_TAG_4.replace("%s", sb.toString());
					break;
				case 5:
					nameFormat = AdminConfig.ADMIN_TAG_5.replace("%s", sb.toString());
					break;
				}
			}
		}

		writeS(String.format(nameFormat, DisplayService.getDisplayName(player)));
		writeH(pcd.getTitleId());
		writeH(player.getCommonData().isHaveMentorFlag() ? 1 : 0);
		writeH(player.getCastingSkillId());

		if (player.isLegionMember() && !player.isBandit() || player.isLegionMember() && !player.isFFA() || player.isLegionMember() && player.getBattleground() == null) {
			writeD(player.getLegion().getLegionId());
			writeC(player.getLegion().getLegionEmblem().getEmblemId());
			writeC(player.getLegion().getLegionEmblem().getEmblemType().getValue());
			writeC(player.getLegion().getLegionEmblem().getEmblemType() == LegionEmblemType.DEFAULT ? 0x00 : 0xFF);
			writeC(player.getLegion().getLegionEmblem().getColor_r());
			writeC(player.getLegion().getLegionEmblem().getColor_g());
			writeC(player.getLegion().getLegionEmblem().getColor_b());
			writeS(player.getLegion().getLegionName());
		} else if (!player.isSpectating() && player.getBattleground() != null && (player.isInGroup2() || player.isInAlliance2())) {
			bgIndex = (player.isInGroup2()) ? player.getPlayerGroup2().getBgIndex() : player.getPlayerAlliance2().getBgIndex();
			LegionEmblem emblem = LadderService.getInstance().getCapeEmblemByIndex(bgIndex);
			writeD(bgIndex + 1);
			writeC(emblem.getEmblemId());
			writeC(0);
			writeC(0xFF);
			writeC(player.isLegionMember() ? player.getLegion().getLegionEmblem().getColor_r() : 0);
			writeC(player.isLegionMember() ? player.getLegion().getLegionEmblem().getColor_g() : 0);
			writeC(player.isLegionMember() ? player.getLegion().getLegionEmblem().getColor_b() : 0);
			writeS(LadderService.getInstance().getNameByIndex(bgIndex));
		} else if (!player.isSpectating() && player.getBattleground() != null && player.getBattleground().is1v1() && (player.getBattleground() instanceof DeathmatchBg || player.getBattleground() instanceof SoloSurvivorBg)) {
			writeD(bgIndex + 1);
			LegionEmblem emblem = LadderService.getInstance().getCapeEmblemByIndex(player.getBgIndex());
			writeC(emblem.getEmblemId());
			writeC(0);
			writeC(0xFF);
			writeC(player.isLegionMember() ? player.getLegion().getLegionEmblem().getColor_r() : 0);
			writeC(player.isLegionMember() ? player.getLegion().getLegionEmblem().getColor_g() : 0);
			writeC(player.isLegionMember() ? player.getLegion().getLegionEmblem().getColor_b() : 0);
			writeS(LadderService.getInstance().getNameByIndex(bgIndex));
		} else if (player.isBandit() || player.isFFA()) {
			writeD(player.getObjectId());
			writeC(16);
			writeC(0);
			writeC(0xFF);
			writeC(Rnd.get(256));
			writeC(Rnd.get(256));
			writeC(Rnd.get(256));
			writeS(DisplayService.getDisplayLegionName(player));
		} else {
			writeB(new byte[12]);
		}

		int maxHp = player.getLifeStats().getMaxHp();
		int currHp = player.getLifeStats().getCurrentHp();
		writeC(100 * currHp / maxHp);// %hp
		writeH(pcd.getDp());// current dp
		writeC(0x00);// unk (0x00)

		/**
		 * Start Item Appearance
		 */
		int mask = 0;

		FastList<Item> items = player.getEquipment().getEquippedForApparence();

		for (Item item : items) {
			if (item.getItemTemplate().isTwoHandWeapon()) {
				ItemSlot[] slots = ItemSlot.getSlotsFor(item.getEquipmentSlot());
				mask |= slots[0].getSlotIdMask();
			} else {
				mask |= item.getEquipmentSlot();
			}
		}

		writeD(mask); // DBS size

		for (Item item : items) {
			writeD(DisplayService.getDisplayTemplate(player, item));
			GodStone godStone = item.getGodStone();
			writeD(godStone != null ? godStone.getItemId() : 0);
			writeD(item.getItemColor());
			writeH(EnchantService.EnchantLevel(item));
			writeH(0);
		}

		/**
		 * Item Appearance End
		 */
		writeD(playerAppearance.getSkinRGB());
		writeD(playerAppearance.getHairRGB());
		writeD(playerAppearance.getEyeRGB());
		writeD(playerAppearance.getLipRGB());
		writeC(playerAppearance.getFace());
		writeC(playerAppearance.getHair());
		writeC(playerAppearance.getDeco());
		writeC(playerAppearance.getTattoo());
		writeC(playerAppearance.getFaceContour());
		writeC(playerAppearance.getExpression());
		writeC(playerAppearance.getPupilShape());
		writeC(playerAppearance.getRemoveMane());
		writeD(playerAppearance.getRightEyeRGB());
		writeC(playerAppearance.getEyeLashShape());
		if (player.getGender() == Gender.FEMALE) {
			writeC(6);
		} else {
			writeC(5);
		}
		writeC(playerAppearance.getJawLine());
		writeC(playerAppearance.getForehead());
		writeC(playerAppearance.getEyeHeight());
		writeC(playerAppearance.getEyeSpace());
		writeC(playerAppearance.getEyeWidth());
		writeC(playerAppearance.getEyeSize());
		writeC(playerAppearance.getEyeShape());
		writeC(playerAppearance.getEyeAngle());
		writeC(playerAppearance.getBrowHeight());
		writeC(playerAppearance.getBrowAngle());
		writeC(playerAppearance.getBrowShape());
		writeC(playerAppearance.getNose());
		writeC(playerAppearance.getNoseBridge());
		writeC(playerAppearance.getNoseWidth());
		writeC(playerAppearance.getNoseTip());
		writeC(playerAppearance.getCheek());
		writeC(playerAppearance.getLipHeight());
		writeC(playerAppearance.getMouthSize());
		writeC(playerAppearance.getLipSize());
		writeC(playerAppearance.getSmile());
		writeC(playerAppearance.getLipShape());
		writeC(playerAppearance.getJawHeigh());
		writeC(playerAppearance.getChinJut());
		writeC(playerAppearance.getEarShape());
		writeC(playerAppearance.getHeadSize());
		writeC(playerAppearance.getNeck());
		writeC(playerAppearance.getNeckLength());
		writeC(playerAppearance.getShoulderSize()); // shoulderSize
		writeC(playerAppearance.getTorso());
		writeC(playerAppearance.getChest());
		writeC(playerAppearance.getWaist());
		writeC(playerAppearance.getHips());
		writeC(playerAppearance.getArmThickness());
		writeC(playerAppearance.getHandSize());
		writeC(playerAppearance.getLegThickness());
		writeC(playerAppearance.getFootSize());
		writeC(playerAppearance.getFacialRate());
		writeC(0);// unk;
		writeC(playerAppearance.getArmLength()); // armLength
		writeC(playerAppearance.getLegLength()); // legLength
		writeC(playerAppearance.getShoulders());
		writeC(playerAppearance.getFaceShape());
		writeC(playerAppearance.getPupilSize());
		writeC(playerAppearance.getUpperTorso());
		writeC(playerAppearance.getForeArmThickness());
		writeC(playerAppearance.getHandSpan());
		writeC(playerAppearance.getCalfThickness());
		writeC(playerAppearance.getVoice());
		writeF(playerAppearance.getHeight());
		writeF(0.25f); // scale
		writeF(2.0f); // gravity or slide surface o_O
		writeF(player.getGameStats().getMovementSpeedFloat()); // move speed
		Stat2 attackSpeed = player.getGameStats().getAttackSpeed();
		writeH(attackSpeed.getBase());
		writeH(attackSpeed.getCurrent());
		writeC(player.getPortAnimation());// port animation
		writeS(player.hasStore() ? player.getStore().getStoreMessage() : "");// private store message

		/**
		 * Movement
		 */
		writeF(0);
		writeF(0);
		writeF(0);
		writeF(player.getX());// x
		writeF(player.getY());// y
		writeF(player.getZ());// z
		writeC(0x00); // move type
		writeC(player.getVisualState()); // visualState
		writeS(player.getCommonData().getNote()); // note show in right down windows if your target on player
		writeH(player.getLevel()); // [level]
		writeH(player.getPlayerSettings().getDisplay()); // unk - 0x04
		writeH(player.getPlayerSettings().getDeny()); // unk - 0x00
		writeH((player.isFFA() || player.getBattleground() != null || player.isBandit()) ? 0 : player.getAbyssRank().getRank().getId()); // abyss rank
		writeH(0x00); // unk - 0x01
		writeD(player.getTarget() == null ? 0 : player.getTarget().getObjectId());
		writeC(0); // suspect id
		writeD(0);
		writeC(player.isMentor() ? 1 : 0);
		writeD(player.getHouseOwnerId());

		/**
		 * System By Ranastic. Remade DainAvenger
		 */
	    writeD(player.getPlayersBonusId());
        // writeD(10); // Player Buff.
        if (MembershipConfig.PREMIUM_TAG_DISPLAY_ENABLE) {
            if (player.getMembership() == 1) {
		        writeD(10); // Player Buff.
		        writeC(6); // Vip Rank Icon.
            } 
            if (player.getMembership() == 2) {
		        writeD(10); // Player Buff.
		        writeD(0); // New Buff Icons.
            }
        }
		writeC(raceId == 0 ? 3 : 5); // Language: Asmodians 3/Elyos 5
		writeC(player.getConquerorInfo().getRank()); // Conqueror 4.8
		writeC(player.getProtectorInfo().getRank()); // Protector 4.8
        writeD(1); // unk 5.5
	}
}