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
package com.aionemu.gameserver.model.gameobjects.player;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.GameServer;
import com.aionemu.gameserver.configs.main.AdvCustomConfig;
import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.configs.main.GSConfig;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.Gender;
import com.aionemu.gameserver.model.PlayerClass;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.team.legion.LegionJoinRequestState;
import com.aionemu.gameserver.model.templates.BoundRadius;
import com.aionemu.gameserver.model.templates.VisibleObjectTemplate;
import com.aionemu.gameserver.model.templates.event.AtreianPassport;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ABYSS_FAVOR;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DP_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_STATUPDATE_DP;
import com.aionemu.gameserver.network.aion.serverpackets.SM_STATUPDATE_EXP;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.player.CreativityPanel.CreativityEssenceService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.stats.XPLossEnum;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.WorldPosition;

public class PlayerCommonData extends VisibleObjectTemplate
{
	static Logger log = LoggerFactory.getLogger(PlayerCommonData.class);
	private final int playerObjId;
	private Race race;
	private String name;
	private PlayerClass playerClass;
	private int level = 0;
	private long exp = 0;
	private long expRecoverable = 0;
	private Gender gender;
	private Timestamp lastOnline = new Timestamp(Calendar.getInstance().getTime().getTime() - 20);
	private Timestamp lastStamp = new Timestamp(Calendar.getInstance().getTime().getTime() - 20);
	private boolean online;
	private String note;
	private WorldPosition position;
	private int questExpands = 0;
	private int npcExpands = AdvCustomConfig.CUBE_SIZE;
	private int warehouseSize = 0;
	private int AdvancedStigmaSlotSize = 0;
	private int titleId = -1;
	private int bonusTitleId = -1;
	private int dp = 0;
	private int mailboxLetters;
	private int soulSickness = 0;
	private boolean noExp = false;
	private long reposteCurrent;
	private long reposteMax;
	private long salvationPoint;
	private int mentorFlagTime;
	private int worldOwnerId;
	private BoundRadius boundRadius;
	private long lastTransferTime;
	private int stamps = 0;
	private int passportReward = 0;
	public Map<Integer, AtreianPassport> playerPassports = new HashMap<Integer, AtreianPassport>(1);
	private PlayerPassports completedPassports;
	private boolean isArchDaeva = false;
	private int creativityPoint;
	private int cp_step = 0;
	private int stoneCreativityPoint;
	private int joinRequestLegionId = 0;
	private LegionJoinRequestState joinRequestState = LegionJoinRequestState.NONE;
	private int lunaConsumePoint;
	private int muni_keys;
	private int consumeCount = 0;
	private int wardrobeSlot;
	private PlayerUpgradeArcade upgradeArcade;
	//Aura Of Growth 5.0
	private long auraOfGrowth;
	private long auraOfGrowthMax;
	//Berdin's Star 5.1
	private long berdinStar;
	private long berdinStarMax = 1125000000; //5.6
	private boolean BerdinStarBoost = false;
	//Abyss Favor 5.3
	private long abyssFavor;
	private long abyssFavorMax = 1000000;
	private boolean AbyssFavorBoost = false;
	//Tower Of Challenge 5.6
	private int floor;
	//Shugo Sweep 5.1
	private int goldenDice;
	private int resetBoard;
	//Atreian Passport Creation Date
	private Timestamp creationDate;
	private int minionSkillPoints;
    private Timestamp minionFunctionTime;

	public PlayerCommonData(int objId) {
		this.playerObjId = objId;
	}

	public int getPlayerObjId() {
		return playerObjId;
	}

	public long getExp() {
		return this.exp;
	}

	public int getQuestExpands() {
		return this.questExpands;
	}

	public void setQuestExpands(int questExpands) {
		this.questExpands = questExpands;
	}

	public void setNpcExpands(int npcExpands) {
		this.npcExpands = npcExpands;
	}

	public int getNpcExpands() {
		return npcExpands;
	}

	/**
	 * @return the AdvancedStigmaSlotSize
	 */
	public int getAdvancedStigmaSlotSize() {
		return AdvancedStigmaSlotSize;
	}

	/**
	 * @param AdvancedStigmaSlotSize
	 *            the AdvancedStigmaSlotSize to set
	 */
	public void setAdvancedStigmaSlotSize(int AdvancedStigmaSlotSize) {
		this.AdvancedStigmaSlotSize = AdvancedStigmaSlotSize;
	}
	public long getExpShown() {
		return this.exp - DataManager.PLAYER_EXPERIENCE_TABLE.getStartExpForLevel(this.level);
	}

	public long getExpNeed() {
		if (this.level == DataManager.PLAYER_EXPERIENCE_TABLE.getMaxLevel()) {
			return 0;
		}
		return DataManager.PLAYER_EXPERIENCE_TABLE.getStartExpForLevel(this.level + 1) - DataManager.PLAYER_EXPERIENCE_TABLE.getStartExpForLevel(this.level);
	}

	/**
	 * calculate the lost experience must be called before setexp
	 *
	 * @author Jangan
	 */
	public void calculateExpLoss() {
		long expLost = XPLossEnum.getExpLoss(this.level, this.getExpNeed());
		int unrecoverable = (int) (expLost * 0.33333333);
		int recoverable = (int) expLost - unrecoverable;
		long allExpLost = recoverable + this.expRecoverable;
		if (this.getExpShown() > unrecoverable) {
			this.exp = this.exp - unrecoverable;
		} else {
			this.exp = this.exp - this.getExpShown();
		}
		if (this.getExpShown() > recoverable) {
			this.expRecoverable = allExpLost;
			this.exp = this.exp - recoverable;
		} else {
			this.expRecoverable = this.expRecoverable + this.getExpShown();
			this.exp = this.exp - this.getExpShown();
		}
		if (expRecoverable > getExpNeed() * 0.25D) {
			expRecoverable = Math.round(getExpNeed() * 0.25D);
		}
		if (this.getPlayer() != null) {
			PacketSendUtility.sendPacket(getPlayer(), new SM_STATUPDATE_EXP(getExpShown(), getExpRecoverable(), getExpNeed(), this.getCurrentReposteEnergy(), this.getMaxReposteEnergy(), this.getBerdinStar(), this.getAuraOfGrowth()));
		}
	}

	public void setRecoverableExp(long expRecoverable) {
		this.expRecoverable = expRecoverable;
	}

	public void resetRecoverableExp() {
		long el = this.expRecoverable;
		this.expRecoverable = 0;
		this.setExp(this.exp + el, false);
	}

	public long getExpRecoverable() {
		return this.expRecoverable;
	}

	/**
	 * @param value
	 */
	public void addExp(long value, int npcNameId) {
		this.addExp(value, null, npcNameId, "");
	}

	public void addExp(long value, RewardType rewardType) {
		this.addExp(value, rewardType, 0, "");
	}

	public void addExp(long value, RewardType rewardType, int npcNameId) {
		this.addExp(value, rewardType, npcNameId, "");
	}

	public void addExp(long value, RewardType rewardType, String name) {
		this.addExp(value, rewardType, 0, name);
	}

	public void addExp(long value, RewardType rewardType, int npcNameId, String name) {
		if (this.noExp) {
			return;
		}
		long reward = value;
		if ((getPlayer() != null) && (rewardType != null)) {
			reward = rewardType.calcReward(getPlayer(), value);
		}
		long repose = 0;
		if ((isReadyForReposteEnergy()) && (getCurrentReposteEnergy() > 0)) {
			repose = (long) (reward / 100.0 * 40.0);
			addReposteEnergy(-repose);
		}
		long salvation = 0;
		if ((isReadyForSalvationPoints()) && (getCurrentSalvationPercent() > 0)) {
			salvation = (long) (reward / 100.0 * getCurrentSalvationPercent());
		}
		long berdinStar = 0;
		long berdinStarBoost = 0;
		if ((isReadyForBerdinStar()) && (getBerdinStar() > 0)) {
			berdinStar = reward;
			addBerdinStar(1575000); //0.14%
			if (BerdinStarBoost) {
				berdinStarBoost = (long) (reward / 100.0 * 50.0);
			}
		}
		long abyssFavor = 0;
		long abyssFavorBoost = 0;
		if ((isReadyForAbyssFavor()) && (getAbyssFavor() > 0)) {
			abyssFavor = reward;
			addAbyssFavor(1500); //0.15%
			if (AbyssFavorBoost) {
				abyssFavorBoost = (long) (reward / 100.0 * 50.0);
			}
		}
		long auraOfGrowth = 0;
		if ((isReadyForAuraOfGrowth()) && (getAuraOfGrowth() > 0)) {
			auraOfGrowth = (long) (reward / 100.0 * 60.0);
		} if ((getPlayer() != null) && (rewardType != null)) {
			if ((rewardType == RewardType.HUNTING) ||
					(rewardType == RewardType.GROUP_HUNTING) ||
					(rewardType == RewardType.CRAFTING) ||
					(rewardType == RewardType.GATHERING) ||
					(rewardType == RewardType.MONSTER_BOOK)) {
				reward += repose + berdinStar + berdinStarBoost + auraOfGrowth + abyssFavor + abyssFavorBoost;
			} else {
				reward += repose;
			}
		}
		setExp(exp + reward, false);
		if ((getPlayer() != null) && (rewardType != null)) {
			switch (rewardType) {
				case HUNTING:
				case GROUP_HUNTING:
				case CRAFTING:
				case GATHERING:
				case MONSTER_BOOK:
					if (npcNameId == 0) {
						PacketSendUtility.sendPacket(getPlayer(), SM_SYSTEM_MESSAGE.STR_GET_EXP2(reward));
					} else {
						PacketSendUtility.sendPacket(getPlayer(), SM_SYSTEM_MESSAGE.STR_GET_EXP_DESC(new DescriptionId(npcNameId * 2 + 1), reward));
						if (repose > 0) {
							PacketSendUtility.sendPacket(getPlayer(), SM_SYSTEM_MESSAGE.STR_GET_EXP_DESC(new DescriptionId(2805577), repose));
						} if (auraOfGrowth > 0) {
							PacketSendUtility.sendPacket(getPlayer(), SM_SYSTEM_MESSAGE.STR_GET_EXP_DESC(new DescriptionId(2806377), auraOfGrowth));
						} if (berdinStar > 0) {
							PacketSendUtility.sendPacket(getPlayer(), SM_SYSTEM_MESSAGE.STR_GET_EXP_DESC(new DescriptionId(2806671), berdinStar));
						} if (abyssFavor > 0) {
							PacketSendUtility.sendPacket(getPlayer(), SM_SYSTEM_MESSAGE.STR_GET_EXP_DESC(new DescriptionId(2808053), abyssFavor));
						}
					}
					break;
				case QUEST:
					if (npcNameId == 0) {
						PacketSendUtility.sendPacket(getPlayer(), SM_SYSTEM_MESSAGE.STR_GET_EXP2(reward));
					} else if ((repose > 0) && (salvation > 0)) {
						PacketSendUtility.sendPacket(getPlayer(), SM_SYSTEM_MESSAGE.STR_GET_EXP_VITAL_MAKEUP_BONUS_DESC(new DescriptionId(npcNameId * 2 + 1), reward, repose, salvation));
					} else if ((repose > 0) && (salvation == 0)) {
						PacketSendUtility.sendPacket(getPlayer(), SM_SYSTEM_MESSAGE.STR_GET_EXP_VITAL_BONUS_DESC(new DescriptionId(npcNameId * 2 + 1), reward, repose));
					} else if ((repose == 0) && (salvation > 0)) {
						PacketSendUtility.sendPacket(getPlayer(), SM_SYSTEM_MESSAGE.STR_GET_EXP_MAKEUP_BONUS_DESC(new DescriptionId(npcNameId * 2 + 1), reward, salvation));
					} else {
						PacketSendUtility.sendPacket(getPlayer(), SM_SYSTEM_MESSAGE.STR_GET_EXP_DESC(new DescriptionId(npcNameId * 2 + 1), reward));
					}
					break;
				case PVP_KILL:
					if ((repose > 0) && (salvation > 0)) {
						PacketSendUtility.sendPacket(getPlayer(), SM_SYSTEM_MESSAGE.STR_GET_EXP_VITAL_MAKEUP_BONUS(name, reward, repose, salvation));
					} else if ((repose > 0) && (salvation == 0)) {
						PacketSendUtility.sendPacket(getPlayer(), SM_SYSTEM_MESSAGE.STR_GET_EXP_VITAL_BONUS(name, reward, repose));
					} else if ((repose == 0) && (salvation > 0)) {
						PacketSendUtility.sendPacket(getPlayer(), SM_SYSTEM_MESSAGE.STR_GET_EXP_MAKEUP_BONUS(name, reward, salvation));
					} else {
						PacketSendUtility.sendPacket(getPlayer(), SM_SYSTEM_MESSAGE.STR_GET_EXP(name, reward));
					}
					break;
			}
			if (this.isArchDaeva()) {
				CreativityEssenceService.getInstance().pointPerExp(this.getPlayer());
			}
		}
	}

	public boolean isReadyForSalvationPoints() {
		return level >= 15 && level < GSConfig.PLAYER_MAX_LEVEL + 1;
	}

	public boolean isReadyForReposteEnergy() {
		return CustomConfig.ENERGY_OF_REPOSE_ENABLE && level >= 10;
	}

	public void addReposteEnergy(long add) {
		if (!this.isReadyForReposteEnergy()) {
			return;
		}
		reposteCurrent += add;
		if (reposteCurrent < 0) {
			reposteCurrent = 0;
		} else if (reposteCurrent > getMaxReposteEnergy()) {
			reposteCurrent = getMaxReposteEnergy();
		}
	}

	public void updateMaxReposte() {
		if (!isReadyForReposteEnergy()) {
			reposteCurrent = 0;
			reposteMax = 0;
		} else {
			reposteMax = (long) (getExpNeed() * 0.25f); //Retail 99%
		}
	}

	public void setCurrentReposteEnergy(long value) {
		reposteCurrent = value;
	}

	public long getCurrentReposteEnergy() {
		return isReadyForReposteEnergy() ? this.reposteCurrent : 0;
	}

	public long getMaxReposteEnergy() {
		return isReadyForReposteEnergy() ? this.reposteMax : 0;
	}

	public void setExp(long exp, boolean ArchDaeva) {
		int maxLevel = DataManager.PLAYER_EXPERIENCE_TABLE.getMaxLevel();
		long maxExp = DataManager.PLAYER_EXPERIENCE_TABLE.getStartExpForLevel(maxLevel);
		if (getPlayerClass() != null && getPlayerClass().isStartingClass()) {
			maxLevel = 10;
			if (this.getLevel() == 9 && this.getExp() >= 126069) {
				//You can become a Daeva through the class change mission.
				//Once you complete the mission, you will reach level 10, regardless of your EXP.
				PacketSendUtility.sendPacket(this.getPlayer(), SM_SYSTEM_MESSAGE.STR_MSG_CAN_QUEST_DEVA);
			}
		} else if (this.getLevel() == 65 && !this.isArchDaeva()) {
			boolean isCompleteQuest = false;
			if (this.getPlayer().getRace() == Race.ELYOS) {
				isCompleteQuest = this.getPlayer().isCompleteQuest(10520); //Covert Communiques.
			} else {
				isCompleteQuest = this.getPlayer().isCompleteQuest(20520); //Lost Destiny.
			} if (!isCompleteQuest) {
				maxExp = 2066885620;
				if (this.getExp() >= 2066885620) {
					//You can become an Archdaeva through the class change mission.
					//Once you complete the mission, you will reach level 66, regardless of your EXP.
					PacketSendUtility.sendPacket(this.getPlayer(), SM_SYSTEM_MESSAGE.STR_MSG_CAN_QUEST_HIGHDEVA);
				}
			}
		}
		if (exp > maxExp) {
			exp = maxExp;
		}
		int oldLvl = this.level;
		this.exp = exp;
		boolean up = false;
		while ((this.level + 1) < maxLevel
				&& (up = exp >= DataManager.PLAYER_EXPERIENCE_TABLE.getStartExpForLevel(this.level + 1)) || (this.level - 1) >= 0
				&& exp < DataManager.PLAYER_EXPERIENCE_TABLE.getStartExpForLevel(this.level)) {
			if (up) {
				this.level++;
			} else {
				this.level--;
			}
			upgradePlayerData();
		}

		if (this.getPlayer() != null) {
			if (up && GSConfig.ENABLE_RATIO_LIMITATION) {
				if (this.level >= GSConfig.RATIO_MIN_REQUIRED_LEVEL && getPlayer().getPlayerAccount().getNumberOf(getRace()) == 1) {
					GameServer.updateRatio(getRace(), 1);
				}
				if (this.level >= GSConfig.RATIO_MIN_REQUIRED_LEVEL && getPlayer().getPlayerAccount().getNumberOf(getRace()) == 1) {
					GameServer.updateRatio(getRace(), -1);
				}
			}

			if (oldLvl != level) {
				updateMaxReposte();
				updateMaxAuraOfGrowth();
			}
			PacketSendUtility.sendPacket(this.getPlayer(), new SM_STATUPDATE_EXP(getExpShown(), getExpRecoverable(), getExpNeed(), this.getCurrentReposteEnergy(), this.getMaxReposteEnergy(), this.getBerdinStar(), this.getAuraOfGrowth()));
		}
	}

	private void upgradePlayerData() {
		Player player = getPlayer();
		if (player != null) {
			player.getController().upgradePlayer();
			resetSalvationPoints();
		}
	}

	public void setNoExp(boolean value) {
		this.noExp = value;
	}

	public boolean getNoExp() {
		return noExp;
	}

	/**
	 * @return Race as from template
	 */
	public final Race getRace() {
		return race;
	}

	public Race getOppositeRace() {
		return race == Race.ELYOS ? Race.ASMODIANS : Race.ELYOS;
	}

	/**
	 * @return the mentorFlagTime
	 */
	public int getMentorFlagTime() {
		return mentorFlagTime;
	}

	public boolean isHaveMentorFlag() {
		return mentorFlagTime > System.currentTimeMillis() / 1000;
	}

	/**
	 * @param mentorFlagTime the mentorFlagTime to set
	 */
	public void setMentorFlagTime(int mentorFlagTime) {
		this.mentorFlagTime = mentorFlagTime;
	}

	public void setRace(Race race) {
		this.race = race;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PlayerClass getPlayerClass() {
		return playerClass;
	}

	public void setPlayerClass(PlayerClass playerClass) {
		this.playerClass = playerClass;
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public WorldPosition getPosition() {
		return position;
	}

	public Timestamp getLastOnline() {
		return lastOnline;
	}

	public void setLastOnline(Timestamp timestamp) {
		lastOnline = timestamp;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		if (level <= DataManager.PLAYER_EXPERIENCE_TABLE.getMaxLevel()) {
			this.setExp(DataManager.PLAYER_EXPERIENCE_TABLE.getStartExpForLevel(level), false);
		}
	}

	//ArchDaeva Update
	public void setArchDaeva() {
		this.setArchDaeva(true);
		if (this.getLevel() < 66) {
			this.setExp(DataManager.PLAYER_EXPERIENCE_TABLE.getStartExpForLevel(66), true);
		} else if (this.getLevel() >= 66) {
			return;
		}
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public int getTitleId() {
		return titleId;
	}

	public void setTitleId(int titleId) {
		this.titleId = titleId;
	}

	public int getBonusTitleId() {
		return bonusTitleId;
	}

	public void setBonusTitleId(int bonusTitleId) {
		this.bonusTitleId = bonusTitleId;
	}

	/**
	 * This method should be called exactly once after creating object of this class
	 *
	 * @param position
	 */
	public void setPosition(WorldPosition position) {
		if (this.position != null) {
			throw new IllegalStateException("position already set");
		}
		this.position = position;
	}

	/**
	 * Gets the cooresponding Player for this common data. Returns null if the player is not online
	 *
	 * @return Player or null
	 */
	public Player getPlayer() {
		if (online && getPosition() != null) {
			return World.getInstance().findPlayer(playerObjId);
		}
		return null;
	}

	public void addDp(int dp) {
		setDp(this.dp + dp);
	}

	/**
	 * //TODO move to lifestats -> db save?
	 *
	 * @param dp
	 */
	public void setDp(int dp) {
		if (getPlayer() != null) {
			if (playerClass.isStartingClass()) {
				return;
            }
			int maxDp = getPlayer().getGameStats().getMaxDp().getCurrent();
			this.dp = dp > maxDp ? maxDp : dp;

			PacketSendUtility.broadcastPacket(getPlayer(), new SM_DP_INFO(playerObjId, this.dp), true);
			getPlayer().getGameStats().updateStatsAndSpeedVisually();
			PacketSendUtility.sendPacket(getPlayer(), new SM_STATUPDATE_DP(this.dp));
		} else {
			log.debug("CHECKPOINT : getPlayer in PCD return null for setDP " + isOnline() + " " + getPosition());
		}
	}

	public int getDp() {
		return this.dp;
	}

	@Override
	public int getTemplateId() {
		return 100000 + race.getRaceId() * 2 + gender.getGenderId();
	}

	@Override
	public int getNameId() {
		return 0;
	}

	/**
	 * @param warehouseSize the warehouseSize to set
	 */
	public void setWarehouseSize(int warehouseSize) {
		this.warehouseSize = warehouseSize;
	}

	/**
	 * @return the warehouseSize
	 */
	public int getWarehouseSize() {
		return warehouseSize;
	}

	public void setMailboxLetters(int count) {
		this.mailboxLetters = count;
	}

	public int getMailboxLetters() {
		return mailboxLetters;
	}

	/**
	 * @param boundRadius
	 */
	public void setBoundingRadius(BoundRadius boundRadius) {
		this.boundRadius = boundRadius;
	}

	@Override
	public BoundRadius getBoundRadius() {
		return boundRadius;
	}

	public void setDeathCount(int count) {
		this.soulSickness = count;
	}

	public int getDeathCount() {
		return this.soulSickness;
	}

	/**
	 * Value returned here means % of exp bonus.
	 * @return
	 */
	public byte getCurrentSalvationPercent() {
		if (salvationPoint <= 0) {
			return 0;
		}
		long per = salvationPoint / 1000;
		if (per > 30) {
			return 30;
		}
		return (byte) per;
	}

	public void addSalvationPoints(long points) {
		salvationPoint += points;
	}

	public void resetSalvationPoints() {
		salvationPoint = 0;
	}

	public void setLastTransferTime(long value) {
		this.lastTransferTime = value;
	}

	public long getLastTransferTime() {
		return this.lastTransferTime;
	}

	public int getWorldOwnerId() {
		return worldOwnerId;
	}

	public void setWorldOwnerId(int worldOwnerId) {
		this.worldOwnerId = worldOwnerId;
	}

	public Timestamp getLastStamp() {
		return lastStamp;
	}

	public void setLastStamp(Timestamp setTime) {
		lastStamp = setTime;
	}

	public int getPassportStamps() {
		return stamps;
	}

	public void setPassportStamps(int stamps) {
		this.stamps = stamps;
	}

	public Map<Integer, AtreianPassport> getPlayerPassports() {
		return playerPassports;
	}

	public PlayerPassports getCompletedPassports() {
		return completedPassports;
	}

	public void addToCompletedPassports(AtreianPassport atreianPassport) {
		completedPassports.addPassport(atreianPassport.getId(), atreianPassport);
	}

	public void setCompletedPassports(PlayerPassports playerPassports) {
		completedPassports = playerPassports;
	}

	public int getPassportReward() {
		return passportReward;
	}

	public void setPassportReward(int passportReward) {
		this.passportReward = passportReward;
	}

	public void setArchDaeva(boolean isArchDaeva) {
		this.isArchDaeva = isArchDaeva;
	}

	public boolean isArchDaeva() {
		return isArchDaeva;
	}

	public int getCreativityPoint() {
		return creativityPoint;
	}

	public void setCreativityPoint(int point) {
		this.creativityPoint = point;
	}

	public int getCPStep() {
		return cp_step;
	}

	public void setCPStep(int step) {
		this.cp_step = step;
	}

	public int getStoneCreativityPoint() {
		return stoneCreativityPoint;
	}

	public void setStoneCreativityPoint(int point) {
		this.stoneCreativityPoint = point;
	}

	public int getJoinRequestLegionId() {
		return joinRequestLegionId;
	}

	public void setJoinRequestLegionId(int joinRequestLegionId) {
		this.joinRequestLegionId = joinRequestLegionId;
	}

	public LegionJoinRequestState getJoinRequestState() {
		return joinRequestState;
	}

	public void setJoinRequestState(LegionJoinRequestState joinRequestState) {
		this.joinRequestState = joinRequestState;
	}

	public void setLunaConsumePoint(int point) {
		this.lunaConsumePoint = point;
	}

	public int getLunaConsumePoint() {
		return lunaConsumePoint;
	}

	public void setMuniKeys(int keys) {
		this.muni_keys = keys;
	}

	public int getMuniKeys() {
		return muni_keys;
	}

	public void setLunaConsumeCount(int count) {
		this.consumeCount = count;
	}

	public int getLunaConsumeCount() {
		return consumeCount;
	}

	public void setWardrobeSlot(int slot) {
		this.wardrobeSlot = slot;
	}

	public int getWardrobeSlot() {
		return wardrobeSlot;
	}

	public PlayerUpgradeArcade getUpgradeArcade() {
		if (upgradeArcade == null) {
			this.upgradeArcade = new PlayerUpgradeArcade();
		}
		return upgradeArcade;
	}

	public void setUpgradeArcade(PlayerUpgradeArcade upgradeArcade) {
		this.upgradeArcade = upgradeArcade;
	}

	public boolean isReadyForAuraOfGrowth() {
		return (level >= 66) && (level < GSConfig.PLAYER_MAX_LEVEL + 1);
	}

	public void addAuraOfGrowth(long add) {
		if (!isReadyForAuraOfGrowth()) {
			return;
		}
		auraOfGrowth += add;
		if (auraOfGrowth < 0) {
			auraOfGrowth = 0;
		} else if (auraOfGrowth > getMaxAuraOfGrowth()) {
			auraOfGrowth = getMaxAuraOfGrowth();
		}
	}

	public void updateMaxAuraOfGrowth() {
		if (!isReadyForAuraOfGrowth()) {
			auraOfGrowth = 0;
			auraOfGrowthMax = 0;
		} else if (level < 70) {
			auraOfGrowthMax = (77000000 + 7000000 * (level - 66));
		} else if (level == 70) {
			this.auraOfGrowthMax = 106000000;
		} else if (level == 71) {
			auraOfGrowthMax = 127000000;
		} else if (level < 83) {
			auraOfGrowthMax = (127000000 + 11000000 * (level - 71));
		} else {
			auraOfGrowthMax = 175000000;
		}
	}

	public void setAuraOfGrowth(long value) {
		auraOfGrowth = value;
	}

	public long getAuraOfGrowth() {
		return isReadyForAuraOfGrowth() ? auraOfGrowth : 0;
	}

	public long getMaxAuraOfGrowth() {
		return isReadyForAuraOfGrowth() ? auraOfGrowthMax : 0;
	}

	public long getAuraOfGrowthPoints() {
		long percent = 0;
		switch (level) {
			case 66:
				percent = 770000;
				break;
			case 67:
				percent = 840000;
				break;
			case 68:
				percent = 910000;
				break;
			case 69:
				percent = 980000;
				break;
			case 70:
				percent = 1060000;
				break;
			case 71:
				percent = 1270000;
				break;
			case 72:
				percent = 1380000;
				break;
			case 73:
				percent = 1490000;
				break;
			case 74:
				percent = 1600000;
				break;
			case 75:
				percent = 1750000;
				break;
			default:
				percent = 0;
			break;
		}
		return percent;
	}

	/**
	 * Berdin's Star 5.1
	 */
	public boolean isReadyForBerdinStar() {
		return this.level >= 10;
	}

	public void addBerdinStar(long add) {
		if (!isReadyForBerdinStar()) {
			return;
		}
		berdinStar += add;
		if (this.berdinStar < 0) {
			berdinStar = 0;
		} else if (berdinStar > getMaxBerdinStar()) {
			berdinStar = getMaxBerdinStar();
		}
		checkBerdinStarPercent();
	}

	public void setBerdinStar(long value) {
		berdinStar = value;
		checkBerdinStarPercent();
	}

	public long getBerdinStar() {
		return isReadyForBerdinStar() ? berdinStar : 0;
	}

	public long getMaxBerdinStar() {
		return isReadyForBerdinStar() ? berdinStarMax : 0;
	}

	public void checkBerdinStarPercent() {
		if ((this.getPlayer() != null) && (isReadyForBerdinStar())) {
			int percent = (int) ((float) berdinStar * 100.0 / (float) getMaxBerdinStar());
			if ((!BerdinStarBoost) && (percent > 50)) {
				BerdinStarBoost = true;
				PacketSendUtility.sendPacket(this.getPlayer(), new SM_SYSTEM_MESSAGE(1403399, new Object[] {Integer.valueOf(50)}));
			} else if ((BerdinStarBoost) && (percent < 50)) {
				BerdinStarBoost = false;
				PacketSendUtility.sendPacket(this.getPlayer(), new SM_SYSTEM_MESSAGE(1403400, new Object[] {Integer.valueOf(50)}));
			} else if (berdinStar <= 0) {
				PacketSendUtility.sendPacket(this.getPlayer(), new SM_SYSTEM_MESSAGE(1403401, new Object[0]));
			}
		}
	}

	/**
	 * Abyss Favor 5.3
	 */
	public boolean isReadyForAbyssFavor() {
		return this.level >= 45;
	}

	public void addAbyssFavor(long add) {
		if (!isReadyForAbyssFavor()) {
			return;
		}
		abyssFavor += add;
		if (this.abyssFavor < 0) {
			abyssFavor = 0;
		} else if (abyssFavor > getMaxAbyssFavor()) {
			abyssFavor = getMaxAbyssFavor();
		}
		checkAbyssFavorPercent();
	}

	public void setAbyssFavor(long value) {
		abyssFavor = value;
		checkAbyssFavorPercent();
	}

	public long getAbyssFavor() {
		return isReadyForAbyssFavor() ? abyssFavor : 0;
	}

	public long getMaxAbyssFavor() {
		return isReadyForAbyssFavor() ? abyssFavorMax : 0;
	}

	public void checkAbyssFavorPercent() {
		if ((this.getPlayer() != null) && (isReadyForAbyssFavor())) {
			int percent = (int) ((float) abyssFavor * 100.0 / (float) getMaxAbyssFavor());
			if ((!AbyssFavorBoost) && (percent > 50)) {
				AbyssFavorBoost = true;
				PacketSendUtility.sendPacket(this.getPlayer(), new SM_ABYSS_FAVOR());
				PacketSendUtility.sendPacket(this.getPlayer(), new SM_SYSTEM_MESSAGE(1404029, new Object[] {Integer.valueOf(50)}));
			} else if ((AbyssFavorBoost) && (percent < 50)) {
				AbyssFavorBoost = false;
				PacketSendUtility.sendPacket(this.getPlayer(), new SM_ABYSS_FAVOR());
				PacketSendUtility.sendPacket(this.getPlayer(), new SM_SYSTEM_MESSAGE(1404030, new Object[] {Integer.valueOf(50)}));
			} else if (abyssFavor <= 0) {
				PacketSendUtility.sendPacket(this.getPlayer(), new SM_ABYSS_FAVOR());
				PacketSendUtility.sendPacket(this.getPlayer(), new SM_SYSTEM_MESSAGE(1404031, new Object[0]));
			}
		}
	}

	public void setFloor(int floor) {
		this.floor = floor;
	}

	public int getFloor() {
		return floor;
	}

	private int time;

	public int getPassportTime() {
		return time;
	}

	public void setPassportTime(int time) {
		this.time = time;
	}

	public int getGoldenDice() {
		return goldenDice;
	}

	public void setGoldenDice(int dice) {
		this.goldenDice = dice;
	}

	public int getResetBoard() {
		return resetBoard;
	}

	public void setResetBoard(int reset) {
		this.resetBoard = reset;
	}

	public void setCreationDate(Timestamp date) {
		creationDate = date;
	}

	public Timestamp getCreationDate() {
		return creationDate;
	}

	/**
	 * @Minions
	 */
    public int getMinionSkillPoints() {
        return minionSkillPoints;
    }
    
    public void setMinionSkillPoints(int minionSkillPoints) {
        this.minionSkillPoints = minionSkillPoints;
    }
    
    public Timestamp getMinionFunctionTime() {
        return minionFunctionTime;
    }
    
    public void setMinionFunctionTime(Timestamp minionFunctionTime) {
        this.minionFunctionTime = minionFunctionTime;
    }
}