package com.aionemu.gameserver.services.player;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.commons.versionning.Version;
import com.aionemu.gameserver.GameServer;
import com.aionemu.gameserver.cache.HTMLCache;
import com.aionemu.gameserver.configs.administration.AdminConfig;
import com.aionemu.gameserver.configs.main.AStationConfig;
import com.aionemu.gameserver.configs.main.AutoGroupConfig;
import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.configs.main.EventsConfig;
import com.aionemu.gameserver.configs.main.GSConfig;
import com.aionemu.gameserver.configs.main.HTMLConfig;
import com.aionemu.gameserver.configs.main.PeriodicSaveConfig;
import com.aionemu.gameserver.configs.main.SecurityConfig;
import com.aionemu.gameserver.dao.PlayerDAO;
import com.aionemu.gameserver.dao.PlayerPasskeyDAO;
import com.aionemu.gameserver.dao.PlayerPunishmentsDAO;
import com.aionemu.gameserver.dao.WeddingDAO;
import com.aionemu.gameserver.model.ChatType;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.account.Account;
import com.aionemu.gameserver.model.account.CharacterBanInfo;
import com.aionemu.gameserver.model.account.CharacterPasskey.ConnectType;
import com.aionemu.gameserver.model.account.PlayerAccountData;
import com.aionemu.gameserver.model.bonus_service.PlayersBonus;
import com.aionemu.gameserver.model.bonus_service.ServiceBuff;
import com.aionemu.gameserver.model.gameobjects.HouseObject;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PlayerCommonData;
import com.aionemu.gameserver.model.gameobjects.player.emotion.Emotion;
import com.aionemu.gameserver.model.gameobjects.player.motion.Motion;
import com.aionemu.gameserver.model.gameobjects.player.title.Title;
import com.aionemu.gameserver.model.gameobjects.state.CreatureSeeState;
import com.aionemu.gameserver.model.gameobjects.state.CreatureState;
import com.aionemu.gameserver.model.gameobjects.state.CreatureVisualState;
import com.aionemu.gameserver.model.items.storage.IStorage;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.model.items.storage.StorageType;
import com.aionemu.gameserver.model.skill.PlayerSkillEntry;
import com.aionemu.gameserver.model.skinskill.SkillSkin;
import com.aionemu.gameserver.model.stats.container.StatEnum;
import com.aionemu.gameserver.model.team2.alliance.PlayerAllianceService;
import com.aionemu.gameserver.model.team2.group.PlayerGroupService;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ABYSS_RANK;
import com.aionemu.gameserver.network.aion.serverpackets.SM_A_STATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_CHANNEL_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_CHARACTER_SELECT;
import com.aionemu.gameserver.network.aion.serverpackets.SM_CUBE_UPDATE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION_LIST;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ENTER_WORLD_CHECK;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EVERGALE_CANYON;
import com.aionemu.gameserver.network.aion.serverpackets.SM_FRIEND_STATUS;
import com.aionemu.gameserver.network.aion.serverpackets.SM_GAME_TIME;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INSTANCE_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INVENTORY_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_COOLDOWN;
import com.aionemu.gameserver.network.aion.serverpackets.SM_LEGION_REQUEST_LIST;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MACRO_LIST;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MESSAGE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAYER_SPAWN;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAYER_STATE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PRICES;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUEST_COMPLETED_LIST;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUEST_LIST;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUIT_RESPONSE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_RECIPE_LIST;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SKILL_ANIMATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SKILL_COOLDOWN;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SKILL_LIST;
import com.aionemu.gameserver.network.aion.serverpackets.SM_STATS_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_TITLE_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_UI_SETTINGS;
import com.aionemu.gameserver.network.aion.serverpackets.SM_YOUTUBE_VIDEO;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.AStationService;
import com.aionemu.gameserver.services.AutoGroupService;
import com.aionemu.gameserver.services.BrokerService;
import com.aionemu.gameserver.services.ClassChangeService;
import com.aionemu.gameserver.services.DisputeLandService;
import com.aionemu.gameserver.services.EnchantService;
import com.aionemu.gameserver.services.EventService;
import com.aionemu.gameserver.services.F2pService;
import com.aionemu.gameserver.services.HTMLService;
import com.aionemu.gameserver.services.HousingService;
import com.aionemu.gameserver.services.KiskService;
import com.aionemu.gameserver.services.LegionService;
import com.aionemu.gameserver.services.PetitionService;
import com.aionemu.gameserver.services.ProtectorConquerorService;
import com.aionemu.gameserver.services.PunishmentService;
import com.aionemu.gameserver.services.PunishmentService.PunishmentType;
import com.aionemu.gameserver.services.SiegeService;
import com.aionemu.gameserver.services.SkillLearnService;
import com.aionemu.gameserver.services.StigmaService;
import com.aionemu.gameserver.services.SurveyService;
import com.aionemu.gameserver.services.VortexService;
import com.aionemu.gameserver.services.WindyGorgeService;
import com.aionemu.gameserver.services.abyss.AbyssPointsService;
import com.aionemu.gameserver.services.abyss.AbyssSkillService;
import com.aionemu.gameserver.services.craft.RelinquishCraftStatus;
import com.aionemu.gameserver.services.events.ArcadeUpgradeService;
import com.aionemu.gameserver.services.events.AtreianPassportService;
import com.aionemu.gameserver.services.events.BoostEventService;
import com.aionemu.gameserver.services.events.EventWindowService;
import com.aionemu.gameserver.services.events.FFAService;
import com.aionemu.gameserver.services.events.ShugoSweepService;
import com.aionemu.gameserver.services.gmservice.GmSpecialSkills;
import com.aionemu.gameserver.services.instance.InstanceService;
import com.aionemu.gameserver.services.mail.MailService;
import com.aionemu.gameserver.services.player.CreativityPanel.CreativityEssenceService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.services.territory.TerritoryService;
import com.aionemu.gameserver.services.minion.MinionService;
import com.aionemu.gameserver.services.toypet.PetService;
import com.aionemu.gameserver.services.transfers.PlayerTransferService;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.skillengine.effect.AbnormalState;
import com.aionemu.gameserver.taskmanager.tasks.ExpireTimerTask;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.utils.audit.AuditLogger;
import com.aionemu.gameserver.utils.collections.ListSplitter;
import com.aionemu.gameserver.utils.rates.Rates;
import com.aionemu.gameserver.utils.stats.AbyssRankEnum;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

import javolution.util.FastList;

public final class PlayerEnterWorldService {

	private static final Logger log = LoggerFactory.getLogger("GAMECONNECTION_LOG");
	// private static final String serverIntro = "";
	private static final String serverInfo;
	private static final String alInfo;
	private static final Set<Integer> pendingEnterWorld = new HashSet<Integer>();
	private static ServiceBuff serviceBuff;
	private static PlayersBonus playersBonus;
	static ScheduledFuture<?> adv = null;

	static {
		String infoBuffer;
		String alBuffer;
		alBuffer = "\n";
		infoBuffer = "";
		if (GSConfig.SERVER_MOTD_DISPLAYREV) {
			alBuffer += "----------------------------\n";
			alBuffer += "Server Revision: "
					+ String.format(" ", new Object[] { new Version(GameServer.class).getRevision() }) + "\n";
		}
		serverInfo = infoBuffer;
		alInfo = alBuffer;
		infoBuffer = null;
		alBuffer = null;
	}

	public static final void startEnterWorld(final int objectId, final AionConnection client) {
		PlayerAccountData playerAccData = client.getAccount().getPlayerAccountData(objectId);
		Timestamp lastOnline = playerAccData.getPlayerCommonData().getLastOnline();
		if (lastOnline != null && client.getAccount().getAccessLevel() < AdminConfig.GM_LEVEL) {
			if (System.currentTimeMillis() - lastOnline.getTime() < (GSConfig.CHARACTER_REENTRY_TIME * 1000)) {
				client.sendPacket(new SM_ENTER_WORLD_CHECK((byte) 6)); // 20 sec time
				return;
			}
		}
		CharacterBanInfo cbi = client.getAccount().getPlayerAccountData(objectId).getCharBanInfo();
		if (cbi != null) {
			if (cbi.getEnd() > System.currentTimeMillis() / 1000) {
				client.close(new SM_QUIT_RESPONSE(), false);
				return;
			} else {
				DAOManager.getDAO(PlayerPunishmentsDAO.class).unpunishPlayer(objectId, PunishmentType.CHARBAN);
			}
		}
		if (SecurityConfig.PASSKEY_ENABLE && !client.getAccount().getCharacterPasskey().isPass()) {
			showPasskey(objectId, client);
		} else {
			validateAndEnterWorld(objectId, client);
		}
	}

	private static final void showPasskey(final int objectId, final AionConnection client) {
		client.getAccount().getCharacterPasskey().setConnectType(ConnectType.ENTER);
		client.getAccount().getCharacterPasskey().setObjectId(objectId);
		boolean isExistPasskey = DAOManager.getDAO(PlayerPasskeyDAO.class).existCheckPlayerPasskey(client.getAccount().getId());
		if (!isExistPasskey) {
			client.sendPacket(new SM_CHARACTER_SELECT(0));
		} else {
			client.sendPacket(new SM_CHARACTER_SELECT(1));
		}
	}

	private static final void validateAndEnterWorld(final int objectId, final AionConnection client) {
		synchronized (pendingEnterWorld) {
			if (pendingEnterWorld.contains(objectId)) {
				log.warn("Skipping enter world " + objectId);
				return;
			}
			pendingEnterWorld.add(objectId);
		}
		int delay = 0;
		if (World.getInstance().findPlayer(objectId) != null) {
			delay = 15000;
			log.warn("Postponed enter world " + objectId);
		}
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				try {
					Player player = World.getInstance().findPlayer(objectId);
					if (player != null) {
						AuditLogger.info(player, "Duplicate player in world");
						client.close(new SM_QUIT_RESPONSE(), false);
						return;
					}
					enterWorld(client, objectId);
				} catch (Throwable ex) {
					log.error("Error during enter world " + objectId, ex);
				} finally {
					synchronized (pendingEnterWorld) {
						pendingEnterWorld.remove(objectId);
					}
				}
			}
		}, delay);
	}

	public static final void enterWorld(AionConnection client, int objectId) {
		Account account = client.getAccount();
		PlayerAccountData playerAccData = client.getAccount().getPlayerAccountData(objectId);
		if (playerAccData == null) {
			return;
		}
		final Player player = PlayerService.getPlayer(objectId, account);
		if (player != null && client.setActivePlayer(player)) {
			player.setClientConnection(client);
			log.info("[MAC_AUDIT] Player " + player.getName() + " (account " + account.getName() + ") has entered world with " + client.getMacAddress() + " MAC.");
			World.getInstance().storeObject(player);
			StigmaService.onPlayerLogin(player);
			if (playerAccData.getPlayerCommonData().getLastOnline() != null) {
				long lastOnline = playerAccData.getPlayerCommonData().getLastOnline().getTime();
				PlayerCommonData pcd = player.getCommonData();
				long secondsOffline = (System.currentTimeMillis() / 1000) - lastOnline / 1000;
				if (pcd.isReadyForSalvationPoints()) {
					// The level of Energy of Salvation will now be maintained up to 1 hour after
					// disconnect.
					if (secondsOffline > 60 * 60) {
						player.getCommonData().resetSalvationPoints();
					}
				}
				if (pcd.isReadyForBerdinStar()) {
					// The level of "Berdin's Star" will now be maintained up to 4 hour after
					// disconnect.
					if (secondsOffline > 240 * 60) {
						pcd.checkBerdinStarPercent();
						player.getCommonData().setBerdinStar(0);
					}
				}
				if (pcd.isReadyForAbyssFavor()) {
					// The level of "Abyss Favor" will now be maintained up to 1 hour after
					// disconnect.
					if (secondsOffline > 60 * 60) {
						pcd.checkAbyssFavorPercent();
						player.getCommonData().setAbyssFavor(0);
					}
				}
				if (pcd.isReadyForAuraOfGrowth()) {
					pcd.updateMaxAuraOfGrowth();
				}
				if (pcd.isReadyForReposteEnergy()) {
					pcd.updateMaxReposte();
					if (secondsOffline > 14400) {
						double hours = secondsOffline / 3600.0;
						long maxRespose = player.getCommonData().getMaxReposteEnergy();
						if (hours > 24.0) {
							hours = 24.0;
						}
						long addResposeEnergy = (long) (hours / 24.0 * maxRespose);
						if (player.getHouseOwnerId() / 10000 * 10000 == player.getWorldId()) {
							switch (player.getActiveHouse().getHouseType()) {
							case STUDIO:
								addResposeEnergy = (long) ((float) addResposeEnergy * 1.05);
								break;
							case MANSION:
								addResposeEnergy = (long) ((float) addResposeEnergy * 1.08);
								break;
							case ESTATE:
								addResposeEnergy = (long) ((float) addResposeEnergy * 1.15);
								break;
							case PALACE:
								addResposeEnergy = (long) ((float) addResposeEnergy * 1.50);
								break;
							default:
								addResposeEnergy = (long) ((float) addResposeEnergy * 1.1);
							}
						}
						pcd.addReposteEnergy(addResposeEnergy > maxRespose ? maxRespose : addResposeEnergy);
					}
				}
				if (System.currentTimeMillis() / 1000 - lastOnline > 300) {
					player.getCommonData().setDp(0);
				}
				if (System.currentTimeMillis() / 1000 - lastOnline > 3600) {
					player.getCommonData().setAuraOfGrowth(0);
				}
			}
			InstanceService.onPlayerLogin(player);
			client.sendPacket(new SM_A_STATION(1, 1, true));
			AbyssSkillService.onEnterWorld(player);
			client.sendPacket(new SM_SKILL_LIST(player, player.getSkillList().getBasicSkills()));
			for (PlayerSkillEntry stigmaSkill : player.getSkillList().getStigmaSkills()) {
				client.sendPacket(new SM_SKILL_LIST(player, stigmaSkill));
			}
			// SM_SKILL_COOLDOWN
			if (player.getSkillCoolDowns() != null) {
				client.sendPacket(new SM_SKILL_COOLDOWN(player.getSkillCoolDowns()));
			}
			// SM_ITEM_COOLDOWN
			if (player.getItemCoolDowns() != null) {
				client.sendPacket(new SM_ITEM_COOLDOWN(player.getItemCoolDowns()));
			}
			// Upgrade Arcade 4.7
			if (EventsConfig.ENABLE_EVENT_ARCADE) {
				ArcadeUpgradeService.getInstance().onEnterWorld(player);
			}
			FastList<QuestState> questList = FastList.newInstance();
			FastList<QuestState> completeQuestList = FastList.newInstance();
			for (QuestState qs : player.getQuestStateList().getAllQuestState()) {
				if (qs.getStatus() == QuestStatus.NONE && qs.getCompleteCount() == 0) {
					continue;
				}
				if (qs.getStatus() != QuestStatus.COMPLETE && qs.getStatus() != QuestStatus.NONE) {
					questList.add(qs);
				}
				if (qs.getCompleteCount() > 0) {
					completeQuestList.add(qs);
				}
			}
			// SM_QUEST_COMPLETED_LIST
			client.sendPacket(new SM_QUEST_COMPLETED_LIST(completeQuestList));

			// SM_QUEST_LIST
			client.sendPacket(new SM_QUEST_LIST(questList));

			// SM_SKILL_ANIMATION
			client.sendPacket(new SM_SKILL_ANIMATION(player));

			// SM_TITLE_INFO
			client.sendPacket(new SM_TITLE_INFO(player.getCommonData().getTitleId()));
			client.sendPacket(new SM_TITLE_INFO(6, player.getCommonData().getBonusTitleId()));
			client.sendPacket(new SM_MOTION(player.getMotions().getMotions().values()));
			client.sendPacket(new SM_ENTER_WORLD_CHECK());
			byte[] uiSettings = player.getPlayerSettings().getUiSettings();
			byte[] shortcuts = player.getPlayerSettings().getShortcuts();
			byte[] houseBuddies = player.getPlayerSettings().getHouseBuddies();
			if (uiSettings != null) {
				client.sendPacket(new SM_UI_SETTINGS(uiSettings, 0));
			}
			if (shortcuts != null) {
				client.sendPacket(new SM_UI_SETTINGS(shortcuts, 1));
			}
			if (houseBuddies != null) {
				client.sendPacket(new SM_UI_SETTINGS(houseBuddies, 2));
			}
			CreativityEssenceService.getInstance().onLogin(player);
			sendItemInfos(client, player);
			if (AStationConfig.A_STATION_ENABLE) {
				AStationService.getInstance().checkAuthorizationRequest(player);
			}
			playerLoggedIn(player);
			client.sendPacket(new SM_INSTANCE_INFO(player, false, player.getCurrentTeam()));

			// SM_CHANNEL_INFO
			client.sendPacket(new SM_CHANNEL_INFO(player.getPosition()));

			KiskService.getInstance().onLogin(player);

			// SM_TERRITORY_LIST
			TerritoryService.getInstance().onEnterWorld(player);

			// SM_YOUTUBE_VIDEO
			client.sendPacket(new SM_YOUTUBE_VIDEO());

			/**
			 * If a user logs out in any hostile territory, they will be transported back to
			 * the last registered Obelisk.
			 */
			if (CustomConfig.ENABLE_RECONNECT_TO_BIND_POINT) {
				TeleportService2.moveToBindLocation(player, true);
			}
			/**
			 * http://static.ncsoft.com/aion/store/PatchNotes/AION_Patch_Notes_071316.pdf If
			 * a user logs out in hostile territory in Iluma/Norsvold, they will be
			 * transported back to the last registered Obelisk.
			 */
			TeleportService2.onLogOutOppositeMap(player);
			// TeleportService2.sendSetBindPoint(player);
			World.getInstance().preSpawn(player);
			VortexService.getInstance().validateLoginZone(player);
			client.sendPacket(new SM_PLAYER_SPAWN(player));
			client.sendPacket(new SM_GAME_TIME());
			ProtectorConquerorService.getInstance().onProtectorConquerorLogin(player);
			// Legion Request 4.9.1
			if (player.isLegionMember()) {
				LegionService.getInstance().onLogin(player);
				if (player.getLegionMember().isBrigadeGeneral() && !player.getLegion().getJoinRequestMap().isEmpty()) {
					client.sendPacket(new SM_LEGION_REQUEST_LIST(player.getLegion().getJoinRequestMap().values()));
				}
			} else {
				DAOManager.getDAO(PlayerDAO.class).getJoinRequestState(player);
				LegionService.getInstance().handleJoinRequestGetAnswer(player);
			}
			client.sendPacket(new SM_TITLE_INFO(player));
			client.sendPacket(new SM_EMOTION_LIST((byte) 0, player.getEmotions().getEmotions()));
			SiegeService.getInstance().onPlayerLogin(player);

			// SM_ATREIAN_PASSPORT
			AtreianPassportService.getInstance().onLogin(player);

			AbyssPointsService.AbyssRankCheck(player);
			// TODO: Send Rift Announce Here
			client.sendPacket(new SM_PRICES());

			// DisputeLand
			DisputeLandService.getInstance().onLogin(player);

			// Event Window
			EventWindowService.getInstance().onLogin(player);

			// Abyss Rank
			client.sendPacket(new SM_ABYSS_RANK(player.getAbyssRank()));

			// Intro message
			// PacketSendUtility.sendWhiteMessage(player, serverName);
			// PacketSendUtility.sendYellowMessage(player, serverIntro);
			// PacketSendUtility.sendBrightYellowMessage(player, serverInfo);
			// PacketSendUtility.sendWhiteMessage(player, alInfo);
			// "\uE026" //Timer.
			// "\uE027" //Speaker.

			player.setRates(Rates.getRatesFor(client.getAccount().getMembership()));
			if (CustomConfig.PREMIUM_NOTIFY_ENABLE) {
				showPremiumAccountInfo(client, account);
			}

			if (player.getAccessLevel() == 0) {
				for (int al : GmSpecialSkills.getAlType(GmSpecialSkills.AccessLevel5.getLevel()).getSkills()) {
					if (player.getSkillList().isSkillPresent(al)) {
						SkillLearnService.removeSkill(player, al);
					}
				}
			}

			if (player.isGM()) {
				if (AdminConfig.INVULNERABLE_GM_CONNECTION || AdminConfig.INVISIBLE_GM_CONNECTION || AdminConfig.ENEMITY_MODE_GM_CONNECTION.equalsIgnoreCase("Neutral") || AdminConfig.ENEMITY_MODE_GM_CONNECTION.equalsIgnoreCase("Enemy") || AdminConfig.VISION_GM_CONNECTION || AdminConfig.WHISPER_GM_CONNECTION) {
					PacketSendUtility.sendMessage(player, "=============================");
					if (AdminConfig.INVULNERABLE_GM_CONNECTION) {
						player.setInvul(true);
						PacketSendUtility.sendMessage(player, ">> Connection in Invulnerable mode <<");
					}
					if (AdminConfig.INVISIBLE_GM_CONNECTION) {
						player.getEffectController().setAbnormal(AbnormalState.HIDE.getId());
						player.setVisualState(CreatureVisualState.HIDE3);
						PacketSendUtility.broadcastPacket(player, new SM_PLAYER_STATE(player), true);
						PacketSendUtility.sendMessage(player, ">> Connection in Invisible mode <<");
					}
					if (AdminConfig.ENEMITY_MODE_GM_CONNECTION.equalsIgnoreCase("Neutral")) {
						player.setAdminNeutral(3);
						player.setAdminEnmity(0);
						PacketSendUtility.sendMessage(player, ">> Connection in Neutral mode <<");
					}
					if (AdminConfig.ENEMITY_MODE_GM_CONNECTION.equalsIgnoreCase("Enemy")) {
						player.setAdminNeutral(0);
						player.setAdminEnmity(3);
						PacketSendUtility.sendMessage(player, ">> Connection in Enemy mode <<");
					}
					if (AdminConfig.VISION_GM_CONNECTION) {
						player.setSeeState(CreatureSeeState.SEARCH10);
						PacketSendUtility.broadcastPacket(player, new SM_PLAYER_STATE(player), true);
						PacketSendUtility.sendMessage(player, ">> Connection in Vision mode <<");
					}
					if (AdminConfig.WHISPER_GM_CONNECTION) {
						player.setUnWispable();
						PacketSendUtility.sendMessage(player, ">> Accepting Whisper : OFF <<");
					}
					if (AdminConfig.GM_MODE_CONNECTION) {
						player.setGmMode(true);
						PacketSendUtility.sendMessage(player, ">> GM Mode : Enable <<");
					}
					PacketSendUtility.sendMessage(player, "=============================");
				}

				if (player.getAccessLevel() >= AdminConfig.GM_SPECIAL_SKILLS) {
					for (int al : GmSpecialSkills.getAlType(player.getAccessLevel()).getSkills()) {
						player.getSkillList().addGMSkill(player, al, 1);
					}
				}
			}

			PacketSendUtility.sendPacket(player, new SM_EVERGALE_CANYON(2));

			// Service Security Buff.
			if (player.getMembership() >= 0) {
				serviceBuff = new ServiceBuff(2);
				serviceBuff.applyEffect(player, 2);
				// Homerun Energy.
				// SkillEngine.getInstance().applyEffectDirectly(323, player, player, 0);
                // [Event] Stigma Preservation.
				// SkillEngine.getInstance().applyEffectDirectly(4714, player, player, 0);
				// [Event] Accessory Ascension
				SkillEngine.getInstance().applyEffectDirectly(4843, player, player, 0); 
			}
			// Service Security Buff.
			if (player.getMembership() >= 1) {
				serviceBuff = new ServiceBuff(220599);
				serviceBuff.applyEffect(player, 220599);
				// Homerun Energy.
				// SkillEngine.getInstance().applyEffectDirectly(323, player, player, 0);
                // [Event] Stigma Preservation.
				// SkillEngine.getInstance().applyEffectDirectly(4714, player, player, 0);
				// [Event] Accessory Ascension
				SkillEngine.getInstance().applyEffectDirectly(4843, player, player, 0);
			}
			// Service Security Buff.
			if (player.getMembership() >= 2) {
				serviceBuff = new ServiceBuff(230599);
				serviceBuff.applyEffect(player, 230599);
				// Homerun Energy.
				// SkillEngine.getInstance().applyEffectDirectly(323, player, player, 0);
                // [Event] Stigma Preservation.
				// SkillEngine.getInstance().applyEffectDirectly(4714, player, player, 0);
				// [Event] Accessory Ascension
				SkillEngine.getInstance().applyEffectDirectly(4843, player, player, 0);
			}
			// PC Cafe Login Benefits.
			if (player.getClientConnection().getAccount().getMembership() == 2 && player.getLevel() >= 66 && player.getLevel() <= 83) {
				serviceBuff = new ServiceBuff(4);
				serviceBuff.applyEffect(player, 4);
				PacketSendUtility.sendBrightYellowMessageOnCenter(player, "Your account vip ! You get extra service!");
			}
			// Ascension Boost.
			if (player.getLevel() >= 1 && player.getLevel() <= 34) {
				playersBonus = new PlayersBonus(2);
				playersBonus.applyEffect(player, 2);
				player.setPlayersBonusId(2);
			}
			// Veteran Boost.
			else if (player.getLevel() >= 35 && player.getLevel() <= 65) {
				playersBonus = new PlayersBonus(3);
				playersBonus.applyEffect(player, 3);
				player.setPlayersBonusId(3);
			}
			// Eminence Of The Beaver.
			else if (player.getLevel() >= 66 && player.getLevel() <= 83) {
				playersBonus = new PlayersBonus(10);
				playersBonus.applyEffect(player, 10);
				player.setPlayersBonusId(10);
			} else {
				playersBonus = new PlayersBonus(1);
				playersBonus.endEffect(player, 1);
			}
			// Abyss Logon 4.9
			if (player.getRace() == Race.ELYOS) {
				abyssLightLogon(player);
			} else if (player.getRace() == Race.ASMODIANS) {
				abyssDarkLogon(player);
			}
			BoostEventService.getInstance().sendPacket(player);

			if (CustomConfig.LOGIN_SERVER_INFO) {
				LoginServerInfo(player); // Show LoginServerinfo + Chat
			}
			// GloryPointLoseMsg(player);
			F2pService.getInstance().onEnterWorld(player);
			// Aura Of Growth.
			// Players can gain additional XP from hunting, gathering or crafting by
			// obtaining Growth Aura.
			// Growth Aura can be obtained from hunting monsters, acquiring essence, and
			// through login and quest rewards.
			// For more information on Growth Aura, check the Character XP Status Bar Tool
			// Tip.
			PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_CHARGE_EXP_POINT, 60000);
			// "Auto PowerShard ON"
			if (player.getEquipment().isPowerShardEquipped() && CustomConfig.ENABLE_AUTO_POWERSHARD) {
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_WEAPON_BOOST_BOOST_MODE_STARTED, 7000);
				player.setState(CreatureState.POWERSHARD);
				PacketSendUtility.playerSendPacketTime(player, new SM_EMOTION(player, EmotionType.POWERSHARD_ON, 0, 0), 7000);
			}
			// Alliance Packet after SetBindPoint.
			PlayerAllianceService.onPlayerLogin(player);
			if (player.isInPrison()) {
				PunishmentService.updatePrisonStatus(player);
			}
			if (player.isNotGatherable()) {
				PunishmentService.updateGatherableStatus(player);
			}
			PlayerGroupService.onPlayerLogin(player);
			// SM_PET
			PetService.getInstance().onPlayerLogin(player);
			// SM_Minions
			MinionService.getInstance().onPlayerLogin(player);
			WindyGorgeService.getInstance().onLogin(player);
			MailService.getInstance().onPlayerLogin(player);
			HousingService.getInstance().onPlayerLogin(player);
			BrokerService.getInstance().onPlayerLogin(player);
			sendMacroList(client, player);
			client.sendPacket(new SM_FRIEND_STATUS((byte) 1));
			client.sendPacket(new SM_RECIPE_LIST(player.getRecipeList().getRecipeList()));
			PetitionService.getInstance().onPlayerLogin(player);
			if (AutoGroupConfig.AUTO_GROUP_ENABLED) {
				AutoGroupService.getInstance().onPlayerLogin(player);
			}
			ClassChangeService.showClassChangeDialog(player);
			// GMService.getInstance().onPlayerLogin(player); TODO Make Config File!!
			player.getLifeStats().updateCurrentStats();
			player.getEquipment().checkRankLimitItems();
			if (HTMLConfig.ENABLE_HTML_WELCOME) {
				HTMLService.showHTML(player, HTMLCache.getInstance().getHTML("welcome.xhtml"));
			}
			player.getNpcFactions().sendDailyQuest();
			if (HTMLConfig.ENABLE_GUIDES) {
				HTMLService.onPlayerLogin(player);
			}
			for (StorageType st : StorageType.values()) {
				if (st == StorageType.LEGION_WAREHOUSE) {
					continue;
				}
				IStorage storage = player.getStorage(st.getId());
				if (storage != null) {
					for (Item item : storage.getItemsWithKinah()) {
						if (item.getExpireTime() > 0) {
							ExpireTimerTask.getInstance().addTask(item, player);
						}
					}
				}
			}
			for (Item item : player.getEquipment().getEquippedItems()) {
				if (item.getExpireTime() > 0) {
					ExpireTimerTask.getInstance().addTask(item, player);
				}
			}
			for (Motion motion : player.getMotions().getMotions().values()) {
				if (motion.getExpireTime() != 0) {
					ExpireTimerTask.getInstance().addTask(motion, player);
				}
			}
			for (Emotion emotion : player.getEmotions().getEmotions()) {
				if (emotion.getExpireTime() != 0) {
					ExpireTimerTask.getInstance().addTask(emotion, player);
				}
			}
			for (Title title : player.getTitleList().getTitles()) {
				if (title.getExpireTime() != 0) {
					ExpireTimerTask.getInstance().addTask(title, player);
				}
			}
			for (SkillSkin skillSkin : player.getSkillSkinList().getSkillSkins()) {
				if (skillSkin.getExpireTime() != 0) {
					ExpireTimerTask.getInstance().addTask(skillSkin, player);
				}
			}
			if (player.getHouseRegistry() != null) {
				for (HouseObject<?> obj : player.getHouseRegistry().getObjects()) {
					if (obj.getPersistentState() != PersistentState.DELETED) {
						if (obj.getObjectTemplate().getUseDays() > 0) {
							ExpireTimerTask.getInstance().addTask(obj, player);
						}
					}
				}
			}
			player.getController().addTask(TaskId.PLAYER_UPDATE, ThreadPoolManager.getInstance().scheduleAtFixedRate(new GeneralUpdateTask(player.getObjectId()), PeriodicSaveConfig.PLAYER_GENERAL * 1000, PeriodicSaveConfig.PLAYER_GENERAL * 1000));
			player.getController().addTask(TaskId.INVENTORY_UPDATE, ThreadPoolManager.getInstance().scheduleAtFixedRate(new ItemUpdateTask(player.getObjectId()), PeriodicSaveConfig.PLAYER_ITEMS * 1000, PeriodicSaveConfig.PLAYER_ITEMS * 1000));
			SurveyService.getInstance().showAvailable(player);
			if (EventsConfig.ENABLE_EVENT_SERVICE) {
				EventService.getInstance().onPlayerLogin(player);
			}
			RelinquishCraftStatus.removeExcessCraftStatus(player, false);
			PlayerTransferService.getInstance().onEnterWorld(player);
			player.setPartnerId(DAOManager.getDAO(WeddingDAO.class).loadPartnerId(player));
			EnchantService.GloryShieldSkill(player);
			ShugoSweepService.getInstance().onLogin(player);
			LunaShopService.getInstance().onLogin(player);
			if (FFAService.getInstance().isInArena(player)) {
				TeleportService2.moveToBindLocation(player, true);
			}
			player.setBattleground(null);
			player.getController().updateZone();
			player.getController().updateNearbyQuests();
			AtreianBestiaryService.getInstance().onLogin(player);
		} else {
			log.info("[DEBUG] enter world" + objectId + ", Player: " + player);
		}
	}

	/**
	 * [Abyss Logon] 4.9
	 */
	public static final void abyssLightLogon(final Player player) {
		if (player.getAbyssRank().getRank().getId() == AbyssRankEnum.SUPREME_COMMANDER.getId()) {
			World.getInstance().doOnAllPlayers(new Visitor<Player>() {
				@Override
				public void visit(Player players) {
					// Elyos Governor "Player Name" has graced Atreia.
					PacketSendUtility.sendPacket(players, new SM_SYSTEM_MESSAGE(1403134, player.getName()));
				}
			});
		}
	}

	public static final void abyssDarkLogon(final Player player) {
		if (player.getAbyssRank().getRank().getId() == AbyssRankEnum.SUPREME_COMMANDER.getId()) {
			World.getInstance().doOnAllPlayers(new Visitor<Player>() {
				@Override
				public void visit(Player players) {
					// Asmodian Governor "Player Name" has graced Atreia.
					PacketSendUtility.sendPacket(players, new SM_SYSTEM_MESSAGE(1403135, player.getName()));
				}
			});
		}
	}

	private static void sendItemInfos(AionConnection client, Player player) {
		int questExpands = player.getQuestExpands();
		int npcExpands = player.getNpcExpands();
		player.getInventory().setLimit(StorageType.CUBE.getLimit() + (questExpands + npcExpands) * 9);
		player.getWarehouse().setLimit(StorageType.REGULAR_WAREHOUSE.getLimit() + player.getWarehouseSize() * 8);
		Storage inventory = player.getInventory();
		List<Item> allItems = new ArrayList<Item>();
		if (inventory.getKinah() == 0) {
			inventory.increaseKinah(0);
		}
		allItems.add(inventory.getKinahItem());
		allItems.addAll(player.getEquipment().getEquippedItems());
		allItems.addAll(inventory.getItems());
		client.sendPacket(new SM_INVENTORY_INFO(true, new ArrayList<Item>(0), npcExpands, questExpands, player));
		ListSplitter<Item> splitter = new ListSplitter<Item>(allItems, 10);
		while (!splitter.isLast()) {
			client.sendPacket(new SM_INVENTORY_INFO(false, splitter.getNext(), npcExpands, questExpands, player));
		}
		client.sendPacket(new SM_INVENTORY_INFO(false, new ArrayList<Item>(0), npcExpands, questExpands, player));
		client.sendPacket(new SM_STATS_INFO(player));
		client.sendPacket(SM_CUBE_UPDATE.stigmaSlots(player.getCommonData().getAdvancedStigmaSlotSize()));
	}

	private static void sendMacroList(AionConnection client, Player player) {
		client.sendPacket(new SM_MACRO_LIST(player, 1));
		client.sendPacket(new SM_MACRO_LIST(player, 2));
		client.sendPacket(new SM_MACRO_LIST(player, 3));
		client.sendPacket(new SM_MACRO_LIST(player, 4));
	}

	private static void playerLoggedIn(Player player) {
		log.info("Player logged in: " + player.getName() + " Account: " + player.getClientConnection().getAccount().getName());
		player.getCommonData().setOnline(true);
		DAOManager.getDAO(PlayerDAO.class).onlinePlayer(player, true);
		player.onLoggedIn();
		player.setOnlineTime();
	}

	private static void showPremiumAccountInfo(AionConnection client, Account account) {
		byte membership = account.getMembership();
		if (membership > 0) {
			String accountType = "";
			switch (account.getMembership()) {
			case 1:
				accountType = "PREMIUM";
				break;
			case 2:
				accountType = "VIP";
				break;
			}
			client.sendPacket(new SM_MESSAGE(0, null, "Your account is " + accountType, ChatType.YELLOW));
		}
	}

	public static final void LoginServerInfo(Player player) {
		float pvpAttackRatio = player.getGameStats().getStat(StatEnum.PVP_ATTACK_RATIO, 0).getCurrent();
		float pvpDefenseRatio = player.getGameStats().getStat(StatEnum.PVP_DEFEND_RATIO, 0).getCurrent();
		float pvpAttackPhyscRatio = player.getGameStats().getStat(StatEnum.PVP_ATTACK_RATIO_PHYSICAL, 0).getCurrent();
		float pvpAttackMagicRatio = player.getGameStats().getStat(StatEnum.PVP_ATTACK_RATIO_MAGICAL, 0).getCurrent();
		float pvpDefensePhyscRatio = player.getGameStats().getStat(StatEnum.PVP_DEFEND_RATIO_PHYSICAL, 0).getCurrent();
		float pvpDefenseMagicRatio = player.getGameStats().getStat(StatEnum.PVP_DEFEND_RATIO_MAGICAL, 0).getCurrent();
		PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300564, "============================"));
		PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300564, "[color:Info：;0 1 0] Welcome to Aion 5.8"));
		PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300564, "============================"));
		PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300564, "[color:Info：;0 1 0] Aion Account Information"));
		PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300564, "[color:Info：;0 1 0][color:Toll Points：;0 1 0] " + player.getClientConnection().getAccount().getToll()));
		PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300564, "[color:Info：;0 1 0][color:Luna Points：;0 1 0] " + player.getLunaAccount()));
		PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300564, "============================"));
		PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300564, "[color:Info：;0 1 0][color:Player Name：;0 1 0] " + player.getName()));
		if (player.getAccessLevel() > 0) {
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300564, "[color:Info：;0 1 0][color:PvP Attack Ratio：;0 1 0] " + pvpAttackRatio * 0.01f + "%"));
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300564, "[color:Info：;0 1 0][color:PvP Defense Ratio：;0 1 0] " + pvpDefenseRatio * 0.01f + "%"));
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300564, "[color:Info：;0 1 0][color:PvP Physc Attack Ratio：;0 1 0] " + pvpAttackPhyscRatio * 0.1f + "%"));
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300564, "[color:Info：;0 1 0][color:PvP Magic Attack Ratio：;0 1 0] " + pvpAttackMagicRatio * 0.1f + "%"));
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300564, "[color:Info：;0 1 0][color:PvP Physc Defend Ratio：;0 1 0] " + pvpDefensePhyscRatio * 0.1f + "%"));
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300564, "[color:Info：;0 1 0][color:PvP Magic Defend Ratio：;0 1 0] " + pvpDefenseMagicRatio * 0.1f + "%"));
		}
	}
}