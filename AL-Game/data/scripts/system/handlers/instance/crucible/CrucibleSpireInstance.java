/*
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
package instance.crucible;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.ai2.manager.WalkManager;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.drop.DropItem;
import com.aionemu.gameserver.model.flyring.FlyRing;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.StaticDoor;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.RewardType;
import com.aionemu.gameserver.model.templates.flyring.FlyRingTemplate;
import com.aionemu.gameserver.model.templates.tower_reward.TowerStageRewardTemplate;
import com.aionemu.gameserver.model.utils3d.Point3D;
import com.aionemu.gameserver.network.aion.serverpackets.*;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.services.abyss.AbyssPointsService;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.ranking.SeasonRankingService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.skillengine.model.DispelCategoryType;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;
import javolution.util.*;

import java.util.*;
import java.util.concurrent.Future;

/****/
/** Author (Encom)
/** Source: https://www.youtube.com/watch?v=KURJ3_EcrB4&feature=youtu.be
/****/

@InstanceID(302400000)
public class CrucibleSpireInstance extends GeneralInstanceHandler {

	private byte floor;
	private Race spawnRace;
	private Map<Integer, StaticDoor> doors;
	protected boolean isInstanceDestroyed = false;
	private final FastList<Future<?>> crucibleTask = FastList.newInstance();

	private long bossTimerStart;
	private long bossTimerEnd;
	
	@Override
    public void onDropRegistered(Npc npc) {
        Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		int npcId = npc.getNpcId();
		int index = dropItems.size() + 1;
        switch (npcId) {
			case 247546: //IDInfinity Heal 02.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 164000530, 1)); //ë?„ì „ì?˜ íƒ‘ìš© ìƒ?ëª…ì?˜ ë¹„ì•½.
		    break;
        }
    }
	
	private void removeItems(Player player) {
		Storage storage = player.getInventory();
		storage.decreaseByItemId(164000530, storage.getItemCountByItemId(164000530)); //ë?„ì „ì?˜ íƒ‘ìš© ìƒ?ëª…ì?˜ ë¹„ì•½.
	}
	
	@Override
	public void onEnterInstance(final Player player) {
		super.onInstanceCreate(instance);
		if (spawnRace == null) {
			spawnRace = player.getRace();
			spawnInggrilInggness1();
			int pfloor = player.getFloor();
			sendPacket(player, "Condition_Infinity_PRE_SEASON_Floor", pfloor);
			sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor", pfloor + 1);
			sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", pfloor);
		}
	}
	
	@Override
	public void onInstanceCreate(WorldMapInstance instance) {
		super.onInstanceCreate(instance);
		doors = instance.getDoors();
		floor = 1;
		spawnFloorRings();
		spawn(247546, 254.38080f, 245.29360f, 241.08308f, (byte) 55);
		spawn(247311, 255.26721f, 249.49001f, 242.03000f, (byte) 0, 71);
		spawn(701773, 263.67166f, 249.42833f, 240.82626f, (byte) 0, 284);
		sp(247310, 279.90976f, 243.26570f, 243.45923f, (byte) 0, 57, 10000, 0, null);
		sp(247310, 279.61618f, 1255.5001f, 243.42058f, (byte) 0, 58, 10000, 0, null);
		sp(247310, 279.62357f, 1243.2299f, 243.50325f, (byte) 0, 59, 10000, 0, null);
		sp(247310, 279.90237f, 255.53593f, 243.45923f, (byte) 0, 60, 10000, 0, null);
		sp(701772, 280.85883f, 249.46001f, 241.08347f, (byte) 0, 115, 10000, 0, null);
    }
	
	private void sendPacket(Player player, final String variable, final int floor) {
		PacketSendUtility.sendPacket(player, new SM_CONDITION_VARIABLE(player, variable, floor));
	}
	
	private void spawnInggrilInggness1() {
		final int Inggril_Inggness1 = spawnRace == Race.ASMODIANS ? 247386 : 247376;
		spawn(Inggril_Inggness1, 255.26721f, 249.49001f, 242.03000f, (byte) 60);
	}
	private void spawnInggrilInggness2() {
		final int Inggril_Inggness2 = spawnRace == Race.ASMODIANS ? 247386 : 247376;
		spawn(Inggril_Inggness2, 255.26721f, 249.49001f, 242.03000f, (byte) 60);
	}
	
	private void teleportCrucibleFloor(Player player) {
		int pfloor = player.getFloor();
		spawnNextFloor(pfloor + 1);
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				deleteNpc(701773);
			}
		}, 2500);
		if (pfloor >= 1 && pfloor <= 38) {
			spawn(701000, 263.55551f, 1249.5244f, 240.73053f, (byte) 0, 56);
			teleportFloor(player, 219.33264f, 1249.4528f, 240.85301f, (byte) 0);
		} else if (pfloor == 39) {
			teleportFloor(player, 210.42656f, 249.58434f, 971.3951f, (byte) 0);
		}
		sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor", pfloor + 1);
	}
	
	private void spawnFloorRings() {
        FlyRing f1 = new FlyRing(new FlyRingTemplate("FLOOR", mapId,
        new Point3D(317.41605, 1254.6891, 258.0014),
		new Point3D(317.88123, 1249.1969, 264.8329),
        new Point3D(317.51993, 1244.0759, 258.0506), 30), instanceId);
        f1.spawn();
    }
	
    private void spawnNextFloor(int next) {
		switch (next) {
			case 2:
				sp(247249, 241.18533f, 1256.9836f, 240.63419f, (byte) 61, 2000, 0, null); //IDInfinity_Normal_02_01.
				sp(247249, 241.29503f, 1242.1162f, 240.63419f, (byte) 60, 2000, 0, null); //IDInfinity_Normal_02_01.
				sp(247250, 244.41614f, 1246.4686f, 240.63419f, (byte) 61, 2000, 0, null); //IDInfinity_Normal_02_02.
                sp(247250, 244.21953f, 1252.8258f, 240.63419f, (byte) 61, 2000, 0, null); //IDInfinity_Normal_02_02.
			break;
			case 3:
				sp(247251, 248.74625f, 1249.5614f, 240.63419f, (byte) 60, 2000, 0, null); //IDInfinity_Normal_03_01.
                sp(247251, 241.17021f, 1257.0828f, 240.63419f, (byte) 61, 2000, 0, null); //IDInfinity_Normal_03_01.
                sp(247251, 241.17314f, 1242.0288f, 240.63419f, (byte) 61, 2000, 0, null); //IDInfinity_Normal_03_01.
				sp(247252, 259.44302f, 1249.6051f, 240.71162f, (byte) 60, 2000, 0, null); //IDInfinity_Normal_03_02.
                sp(247252, 253.85323f, 1262.3983f, 240.71162f, (byte) 76, 2000, 0, null); //IDInfinity_Normal_03_02.
                sp(247252, 254.24211f, 1236.7045f, 240.71162f, (byte) 45, 2000, 0, null); //IDInfinity_Normal_03_02.
			break;
			case 4:
				sp(247236, 241.45598f, 1249.5740f, 240.63419f, (byte) 60, 2000, 0, null); //IDInfinity_Named_04.
			break;
			case 5:
				sp(247351, 254.19934f, 1262.5269f, 240.71162f, (byte) 36, 2000, 0, null); //IDInfinity_Meat_01.
				sp(247351, 253.87862f, 1236.6519f, 240.71162f, (byte) 9, 2000, 0, null); //IDInfinity_Meat_01.
				sp(247253, 252.67638f, 1264.4946f, 240.71162f, (byte) 101, 2000, 0, null); //IDInfinity_Normal_05_01.
				sp(247253, 252.09209f, 1233.2440f, 240.71162f, (byte) 14, 2000, 0, null); //IDInfinity_Normal_05_01.
                sp(247253, 250.93297f, 1235.5901f, 240.71162f, (byte) 7, 2000, 0, null); //IDInfinity_Normal_05_01.
                sp(247253, 251.68967f, 1262.4159f, 240.71162f, (byte) 113, 2000, 0, null); //IDInfinity_Normal_05_01.
				sp(247254, 255.53033f, 1260.8771f, 240.71162f, (byte) 42, 2000, 0, null); //IDInfinity_Normal_05_02.
                sp(247254, 256.08560f, 1237.6760f, 240.71162f, (byte) 77, 2000, 0, null); //IDInfinity_Normal_05_02.
                sp(247254, 253.70976f, 1238.7314f, 240.71162f, (byte) 84, 2000, 0, null); //IDInfinity_Normal_05_02.
			break;
			case 6:
				sp(247352, 253.81844f, 1262.5720f, 240.71162f, (byte) 61, 2000, 0, null); //IDInfinity_Meat_02.
                sp(247352, 245.95355f, 1231.9944f, 240.71162f, (byte) 25, 2000, 0, null); //IDInfinity_Meat_02.
				sp(247255, 252.66000f, 1265.1761f, 240.71162f, (byte) 102, 2000, 0, null); //IDInfinity_Normal_06_01.
                sp(247255, 251.31412f, 1262.7773f, 240.71162f, (byte) 114, 2000, 0, null); //IDInfinity_Normal_06_01.
				sp(247255, 248.28351f, 1231.3329f, 240.71162f, (byte) 58, 2000, 0, null); //IDInfinity_Normal_06_01.
				sp(247256, 256.23257f, 1261.0988f, 240.71162f, (byte) 46, 2000, 0, null); //IDInfinity_Normal_06_02.
                sp(247256, 254.00407f, 1259.5002f, 240.71162f, (byte) 36, 2000, 0, null); //IDInfinity_Normal_06_02.
                sp(247256, 242.98157f, 1229.6989f, 240.71162f, (byte) 6, 2000, 0, null); //IDInfinity_Normal_06_02.
                sp(247256, 242.80790f, 1232.9885f, 240.71162f, (byte) 116, 2000, 0, null); //IDInfinity_Normal_06_02.
                sp(247256, 247.98917f, 1234.1317f, 240.71162f, (byte) 73, 2000, 0, null); //IDInfinity_Normal_06_02.
			break;
			case 7:
				sp(247354, 254.03172f, 1262.3046f, 240.71162f, (byte) 113, 2000, 0, null); //IDInfinity_Meat_03.
				sp(247354, 253.98647f, 1236.5012f, 240.71162f, (byte) 23, 2000, 0, null); //IDInfinity_Meat_03.
				sp(247257, 252.40446f, 1265.0981f, 241.09122f, (byte) 103, 2000, 0, null); //IDInfinity_Normal_07_01.
                sp(247257, 251.31787f, 1263.1189f, 240.71162f, (byte) 113, 2000, 0, null); //IDInfinity_Normal_07_01.
                sp(247257, 252.93219f, 1233.4966f, 240.71162f, (byte) 17, 2000, 0, null); //IDInfinity_Normal_07_01.
                sp(247257, 251.10315f, 1235.8412f, 240.71162f, (byte) 7, 2000, 0, null); //IDInfinity_Normal_07_01.
				sp(247258, 256.58646f, 1261.8520f, 240.71162f, (byte) 49, 2000, 0, null); //IDInfinity_Normal_07_02.
                sp(247258, 254.23650f, 1260.2627f, 240.71162f, (byte) 36, 2000, 0, null); //IDInfinity_Normal_07_02.
                sp(247258, 254.60794f, 1239.1844f, 240.71162f, (byte) 83, 2000, 0, null); //IDInfinity_Normal_07_02.
                sp(247258, 256.51114f, 1237.3129f, 240.71162f, (byte) 71, 2000, 0, null); //IDInfinity_Normal_07_02.
			break;
			case 8:
				sp(247237, 241.19528f, 1249.6161f, 240.63419f, (byte) 60, 2000, 0, null); //IDInfinity_Named_08.
				sp(247353, 236.23781f, 1266.9647f, 240.71162f, (byte) 95, 2000, 0, null); //IDInfinity_Berrel.
                sp(247353, 254.19570f, 1236.9214f, 240.71162f, (byte) 44, 2000, 0, null); //IDInfinity_Berrel.
                sp(247353, 253.82140f, 1262.2661f, 240.71162f, (byte) 74, 2000, 0, null); //IDInfinity_Berrel.
                sp(247353, 236.80017f, 1231.8394f, 240.71162f, (byte) 25, 2000, 0, null); //IDInfinity_Berrel.
				sp(247401, 241.33926f, 1241.8188f, 240.63419f, (byte) 57, 2000, 0, null); //IDInfinity_Summon_Tog.
				sp(247401, 241.39090f, 1257.0540f, 240.63419f, (byte) 60, 2000, 0, null); //IDInfinity_Summon_Tog.
                sp(247401, 249.24400f, 1249.5896f, 240.63419f, (byte) 60, 2000, 0, null); //IDInfinity_Summon_Tog.
                sp(247401, 245.36331f, 1253.8690f, 240.63419f, (byte) 63, 2000, 0, null); //IDInfinity_Summon_Tog.
                sp(247401, 245.44702f, 1245.5221f, 240.63419f, (byte) 61, 2000, 0, null); //IDInfinity_Summon_Tog.
			break;
			case 9:
				sp(247259, 241.29816f, 1246.0281f, 240.63419f, (byte) 60, 2000, 0, null); //IDInfinity_Normal_09_01.
                sp(247259, 241.24530f, 1253.0829f, 240.63419f, (byte) 60, 2000, 0, null); //IDInfinity_Normal_09_01.
				sp(247260, 241.01903f, 1268.0564f, 240.71162f, (byte) 60, 2000, 0, null); //IDInfinity_Normal_09_02.
                sp(247260, 241.35820f, 1231.3574f, 240.71162f, (byte) 60, 2000, 0, null); //IDInfinity_Normal_09_02.
			break;
			case 10:
			    sp(247261, 258.80470f, 1252.6221f, 240.71162f, (byte) 62, 2000, 0, null); //IDInfinity_Normal_10_01.
				sp(247261, 259.15295f, 1247.0538f, 240.71162f, (byte) 60, 2000, 0, null); //IDInfinity_Normal_10_01.
				sp(247262, 241.46776f, 1249.6780f, 240.63419f, (byte) 61, 2000, 0, null); //IDInfinity_Normal_10_02.
                sp(247262, 240.68369f, 1264.4435f, 240.63419f, (byte) 68, 2000, 0, null); //IDInfinity_Normal_10_02.
                sp(247262, 241.24583f, 1234.4695f, 240.63419f, (byte) 53, 2000, 0, null); //IDInfinity_Normal_10_02.
			break;
			case 11:
			    sp(247263, 240.94872f, 1264.3417f, 240.63419f, (byte) 68, 2000, 0, null); //IDInfinity_Normal_11_01.
                sp(247263, 241.18830f, 1234.7659f, 240.63419f, (byte) 51, 2000, 0, null); //IDInfinity_Normal_11_01.
				sp(247264, 238.01044f, 1249.5957f, 240.63419f, (byte) 60, 2000, 0, null); //IDInfinity_Normal_11_02.
                sp(247264, 245.00499f, 1249.6553f, 240.63419f, (byte) 60, 2000, 0, null); //IDInfinity_Normal_11_02.
                sp(247264, 241.31288f, 1253.0376f, 240.63419f, (byte) 59, 2000, 0, null); //IDInfinity_Normal_11_02.
                sp(247264, 241.27722f, 1246.1240f, 240.63419f, (byte) 59, 2000, 0, null); //IDInfinity_Normal_11_02.
			break;
			case 12:
			    sp(247238, 241.50197f, 1249.5283f, 240.63419f, (byte) 60, 3500, 0, null); //IDInfinity_Named_12.
			break;
			case 13:
			    sp(247265, 241.18263f, 1249.5398f, 240.63419f, (byte) 60, 2000, 0, null); //IDInfinity_Normal_13_01.
                sp(247265, 258.61935f, 1253.3063f, 240.71162f, (byte) 62, 2000, 0, null); //IDInfinity_Normal_13_01.
                sp(247265, 258.54715f, 1246.3235f, 240.71162f, (byte) 58, 2000, 0, null); //IDInfinity_Normal_13_01.
				sp(247266, 241.18523f, 1260.1180f, 240.63419f, (byte) 59, 2000, 0, null); //IDInfinity_Normal_13_02.
                sp(247266, 241.04256f, 1239.2163f, 240.63419f, (byte) 60, 2000, 0, null); //IDInfinity_Normal_13_02.
			break;
			case 14:
			    sp(247267, 241.15300f, 1260.6820f, 240.63419f, (byte) 60, 2000, 0, null); //IDInfinity_Normal_14_01.
                sp(247267, 241.20154f, 1238.8978f, 240.63419f, (byte) 60, 2000, 0, null); //IDInfinity_Normal_14_01.
				sp(247268, 260.02588f, 1244.4471f, 240.71162f, (byte) 56, 2000, 0, null); //IDInfinity_Normal_14_02.
                sp(247268, 260.64893f, 1249.4431f, 240.71162f, (byte) 58, 2000, 0, null); //IDInfinity_Normal_14_02.
                sp(247268, 259.95993f, 1254.9393f, 240.71162f, (byte) 63, 2000, 0, null); //IDInfinity_Normal_14_02.
			break;
			case 15:
			    sp(247269, 241.31754f, 1249.5798f, 240.63419f, (byte) 60, 2000, 0, null); //IDInfinity_Normal_15_01.
				sp(247270, 260.19244f, 1247.5491f, 240.71162f, (byte) 58, 2000, 0, null); //IDInfinity_Normal_15_02.
                sp(247270, 259.95400f, 1251.8992f, 240.71162f, (byte) 61, 2000, 0, null); //IDInfinity_Normal_15_02.
                sp(247270, 241.28294f, 1242.0948f, 240.63419f, (byte) 60, 2000, 0, null); //IDInfinity_Normal_15_02.
                sp(247270, 241.15080f, 1257.0294f, 240.63419f, (byte) 62, 2000, 0, null); //IDInfinity_Normal_15_02.
			break;
			case 16:
				sp(247239, 241.06525f, 1249.5922f, 240.63419f, (byte) 59, 2000, 0, null); //IDInfinity_Named_16.
                sp(247239, 246.20310f, 1244.5913f, 240.63419f, (byte) 61, 2000, 0, null); //IDInfinity_Named_16.
                sp(247239, 246.25809f, 1254.7360f, 240.63419f, (byte) 60, 2000, 0, null); //IDInfinity_Named_16.
			break;
			case 17:
			    sp(247271, 241.25314f, 1246.1554f, 240.63419f, (byte) 60, 2000, 0, null); //IDInfinity_Normal_17_01.
                sp(247271, 241.21352f, 1253.0051f, 240.63419f, (byte) 61, 2000, 0, null); //IDInfinity_Normal_17_01.
				sp(247272, 248.43343f, 1262.3562f, 240.63419f, (byte) 51, 2000, 0, null); //IDInfinity_Normal_17_02.
                sp(247272, 248.46645f, 1236.8959f, 240.63419f, (byte) 69, 2000, 0, null); //IDInfinity_Normal_17_02.
			break;
			case 18:
				sp(247273, 259.59085f, 1254.5885f, 240.71162f, (byte) 64, 2000, 0, null); //IDInfinity_Normal_18_01.
                sp(247273, 259.67526f, 1244.8475f, 240.71162f, (byte) 57, 2000, 0, null); //IDInfinity_Normal_18_01.
				sp(247274, 241.08554f, 1264.1904f, 240.63419f, (byte) 61, 2000, 0, null); //IDInfinity_Normal_18_02.
				sp(247355, 259.31012f, 1249.4916f, 240.71162f, (byte) 59, 2000, 0, null); //IDInfinity_Lava_01.
			break;
			case 19:
			    sp(247275, 255.78647f, 1238.9512f, 240.71162f, (byte) 56, 2000, 0, null); //IDInfinity_Normal_19_01.
                sp(247275, 250.05235f, 1242.1001f, 240.63419f, (byte) 0, 2000, 0, null); //IDInfinity_Normal_19_01.
				sp(247276, 257.48360f, 1246.4463f, 240.71162f, (byte) 76, 2000, 0, null); //IDInfinity_Normal_19_02.
                sp(247276, 247.59322f, 1232.3746f, 240.71162f, (byte) 18, 2000, 0, null); //IDInfinity_Normal_19_02.
				sp(247356, 248.72170f, 1237.0615f, 240.63419f, (byte) 59, 2000, 0, null); //IDInfinity_Lava_02.
                sp(247356, 254.23114f, 1242.8639f, 240.63419f, (byte) 75, 2000, 0, null); //IDInfinity_Lava_02.
			break;
			case 20:
			    sp(247240, 241.38167f, 1249.6044f, 240.63419f, (byte) 60, 2000, 0, null); //IDInfinity_Named_20.
			break;
			case 21:
			    sp(247277, 241.23924f, 1249.5948f, 240.63419f, (byte) 60, 2000, 0, null); //IDInfinity_Normal_21_01.
				sp(247278, 246.15216f, 1254.5807f, 240.63419f, (byte) 60, 2000, 0, null); //IDInfinity_Normal_21_02.
                sp(247278, 246.55399f, 1244.3586f, 240.63419f, (byte) 60, 2000, 0, null); //IDInfinity_Normal_21_02.
			break;
			case 22:
			    sp(247279, 241.14186f, 1264.6486f, 240.63419f, (byte) 90, 2000, 0, null); //IDInfinity_Normal_22_01.
                sp(247279, 241.03874f, 1234.2322f, 240.63419f, (byte) 29, 2000, 0, null); //IDInfinity_Normal_22_01.
				sp(247280, 259.70460f, 1254.7360f, 240.71162f, (byte) 62, 2000, 0, null); //IDInfinity_Normal_22_02.
                sp(247280, 259.69165f, 1244.3368f, 240.71162f, (byte) 58, 2000, 0, null); //IDInfinity_Normal_22_02.
			break;
			case 23:
			    sp(247281, 241.41281f, 1249.5133f, 240.63419f, (byte) 60, 2000, 0, null); //IDInfinity_Normal_23_01.
				sp(247282, 246.13629f, 1244.5453f, 240.63419f, (byte) 60, 2000, 0, null); //IDInfinity_Normal_23_02.
                sp(247282, 245.91959f, 1254.5669f, 240.63419f, (byte) 62, 2000, 0, null); //IDInfinity_Normal_23_02.
                sp(247282, 261.21832f, 1249.5925f, 240.71162f, (byte) 60, 2000, 0, null); //IDInfinity_Normal_23_02.
			break;
			case 24:
			    sp(247241, 241.41281f, 1249.5133f, 240.63419f, (byte) 60, 3500, 0, null); //IDInfinity_Named_24.
			break;
			case 25:
			    sp(247283, 261.01346f, 1249.5574f, 240.71162f, (byte) 60, 2000, 0, null); //IDInfinity_Normal_25_01.
				sp(247284, 254.40323f, 1242.5317f, 240.63419f, (byte) 51, 2000, 0, null); //IDInfinity_Normal_25_02.
                sp(247284, 254.34718f, 1256.6465f, 240.63419f, (byte) 69, 2000, 0, null); //IDInfinity_Normal_25_02.
			break;
			case 26:
			    sp(247285, 248.59853f, 1249.5581f, 240.63419f, (byte) 60, 2000, 0, null); //IDInfinity_Normal_26_01.
                sp(247285, 241.13426f, 1234.3336f, 240.63419f, (byte) 30, 2000, 0, null); //IDInfinity_Normal_26_01.
                sp(247285, 241.01393f, 1264.6925f, 240.63419f, (byte) 90, 2000, 0, null); //IDInfinity_Normal_26_01.
				sp(247286, 261.09265f, 1249.5715f, 240.71162f, (byte) 60, 2000, 0, null); //IDInfinity_Normal_26_02.
			break;
			case 27:
			    sp(247287, 241.04361f, 1249.5405f, 240.63419f, (byte) 60, 2000, 0, null); //IDInfinity_Normal_27_01.
                sp(247287, 241.02223f, 1254.7899f, 240.63419f, (byte) 60, 2000, 0, null); //IDInfinity_Normal_27_01.
                sp(247287, 241.13141f, 1244.1161f, 240.63419f, (byte) 60, 2000, 0, null); //IDInfinity_Normal_27_01.
                sp(247287, 244.44319f, 1246.9247f, 240.63419f, (byte) 60, 2000, 0, null); //IDInfinity_Normal_27_01.
                sp(247287, 244.40683f, 1252.1543f, 240.63419f, (byte) 60, 2000, 0, null); //IDInfinity_Normal_27_01.
			break;
			case 28:
			    sp(247242, 241.41281f, 1249.5133f, 240.63419f, (byte) 60, 2000, 0, null); //IDInfinity_Named_28.
			break;
			case 29:
			    switch (Rnd.get(1, 2)) {
				    case 1:
				        sp(701692, 240.99178f, 1236.1763f, 238.74855f, (byte) 0, 186, 2000, 0, null); //IDInfinity_Pot.
						sp(701692, 240.99176f, 1262.8556f, 238.74855f, (byte) 0, 196, 2000, 0, null); //IDInfinity_Pot.
						sp(247360, 240.99178f, 1236.1763f, 242.58624f, (byte) 0, 195, 2000, 0, null); //IDInfinity_Fire_Blue.
						sp(247360, 240.99176f, 1262.8556f, 242.58624f, (byte) 0, 199, 2000, 0, null); //IDInfinity_Fire_Blue.
						sp(247289, 241.41281f, 1249.5133f, 240.63419f, (byte) 60, 2000, 0, null); //IDInfinity_Normal_29_01.
					break;
					case 2:
				        sp(701692, 240.99178f, 1236.1763f, 238.74855f, (byte) 0, 186, 2000, 0, null); //IDInfinity_Pot.
						sp(701692, 240.99176f, 1262.8556f, 238.74855f, (byte) 0, 196, 2000, 0, null); //IDInfinity_Pot.
						sp(247359, 240.99178f, 1236.1763f, 242.58624f, (byte) 0, 197, 2000, 0, null); //IDInfinity_Fire_Red.
						sp(247359, 240.99176f, 1262.8556f, 242.58624f, (byte) 0, 198, 2000, 0, null); //IDInfinity_Fire_Red.
						sp(247290, 241.41281f, 1249.5133f, 240.63419f, (byte) 60, 2000, 0, null); //IDInfinity_Normal_29_02.
					break;
				}
			break;
			case 30:
			    switch (Rnd.get(1, 2)) {
				    case 1:
				        sp(701692, 240.99178f, 1236.1763f, 238.74855f, (byte) 0, 186, 2000, 0, null); //IDInfinity_Pot.
						sp(701692, 240.99176f, 1262.8556f, 238.74855f, (byte) 0, 196, 2000, 0, null); //IDInfinity_Pot.
						sp(247360, 240.99178f, 1236.1763f, 242.58624f, (byte) 0, 195, 2000, 0, null); //IDInfinity_Fire_Blue.
						sp(247360, 240.99176f, 1262.8556f, 242.58624f, (byte) 0, 199, 2000, 0, null); //IDInfinity_Fire_Blue.
						sp(247291, 241.41281f, 1249.5133f, 240.63419f, (byte) 60, 2000, 0, null); //IDInfinity_Normal_30_01.
					break;
					case 2:
				        sp(701692, 240.99178f, 1236.1763f, 238.74855f, (byte) 0, 186, 2000, 0, null); //IDInfinity_Pot.
						sp(701692, 240.99176f, 1262.8556f, 238.74855f, (byte) 0, 196, 2000, 0, null); //IDInfinity_Pot.
						sp(247359, 240.99178f, 1236.1763f, 242.58624f, (byte) 0, 197, 2000, 0, null); //IDInfinity_Fire_Red.
						sp(247359, 240.99176f, 1262.8556f, 242.58624f, (byte) 0, 198, 2000, 0, null); //IDInfinity_Fire_Red.
						sp(247292, 241.41281f, 1249.5133f, 240.63419f, (byte) 60, 2000, 0, null); //IDInfinity_Normal_30_02.
					break;
				}
			break;
			case 31:
			    sp(701692, 240.99178f, 1236.1763f, 238.74855f, (byte) 0, 186, 2000, 0, null); //IDInfinity_Pot.
				sp(247359, 240.99178f, 1236.1763f, 242.58624f, (byte) 0, 197, 2000, 0, null); //IDInfinity_Fire_Red.
			    sp(701692, 240.99176f, 1262.8556f, 238.74855f, (byte) 0, 196, 2000, 0, null); //IDInfinity_Pot.
				sp(247360, 240.99176f, 1262.8556f, 242.58624f, (byte) 0, 199, 2000, 0, null); //IDInfinity_Fire_Blue.
				sp(247293, 259.91907f, 1254.8942f, 240.71162f, (byte) 62, 2000, 0, null); //IDInfinity_Normal_31_01.
				sp(247294, 260.09055f, 1244.4081f, 240.71162f, (byte) 61, 2000, 0, null); //IDInfinity_Normal_31_02.
			break;
			case 32:
			    sp(701692, 240.99178f, 1236.1763f, 238.74855f, (byte) 0, 186, 2000, 0, null); //IDInfinity_Pot.
				sp(247359, 240.99178f, 1236.1763f, 242.58624f, (byte) 0, 197, 2000, 0, null); //IDInfinity_Fire_Red.
			    sp(701692, 240.99176f, 1262.8556f, 238.74855f, (byte) 0, 196, 2000, 0, null); //IDInfinity_Pot.
				sp(247360, 240.99176f, 1262.8556f, 242.58624f, (byte) 0, 199, 2000, 0, null); //IDInfinity_Fire_Blue.
				sp(247243, 241.41281f, 1249.5133f, 240.63419f, (byte) 60, 2000, 0, null); //IDInfinity_Named_32.
			break;
			case 33:
			    switch (Rnd.get(1, 2)) {
				    case 1:
				        sp(247295, 257.12485f, 1258.1670f, 240.71162f, (byte) 70, 3500, 0, null); //IDInfinity_Normal_33_01.
						sp(247295, 257.45935f, 1240.7285f, 240.71162f, (byte) 49, 4500, 0, null); //IDInfinity_Normal_33_01.
						sp(247295, 245.13070f, 1245.4384f, 240.63419f, (byte) 60, 5500, 0, null); //IDInfinity_Normal_33_01.
                        sp(247295, 245.39058f, 1253.8103f, 240.63419f, (byte) 60, 6500, 0, null); //IDInfinity_Normal_33_01.
					break;
					case 2:
				        sp(247296, 257.12485f, 1258.1670f, 240.71162f, (byte) 70, 3500, 0, null); //IDInfinity_Normal_33_02.
						sp(247296, 257.45935f, 1240.7285f, 240.71162f, (byte) 49, 4500, 0, null); //IDInfinity_Normal_33_02.
						sp(247296, 245.13070f, 1245.4384f, 240.63419f, (byte) 60, 5500, 0, null); //IDInfinity_Normal_33_02.
                        sp(247296, 245.39058f, 1253.8103f, 240.63419f, (byte) 60, 6500, 0, null); //IDInfinity_Normal_33_02.
					break;
				}
			break;
			case 34:
			    sp(247297, 236.40814f, 1244.7013f, 240.63419f, (byte) 60, 2000, 0, null); //IDInfinity_Normal_34_01.
                sp(247297, 236.40082f, 1254.6990f, 240.63419f, (byte) 60, 2000, 0, null); //IDInfinity_Normal_34_01.
				sp(247298, 248.58983f, 1257.3235f, 240.63419f, (byte) 60, 3500, 0, null); //IDInfinity_Normal_34_02.
				sp(247298, 241.12361f, 1249.5305f, 240.63419f, (byte) 60, 4500, 0, null); //IDInfinity_Normal_34_02.
                sp(247298, 248.41464f, 1242.6595f, 240.63419f, (byte) 59, 5500, 0, null); //IDInfinity_Normal_34_02.
			break;
			case 35:
			    sp(247299, 241.43329f, 1249.5488f, 240.63419f, (byte) 60, 2000, 0, null); //IDInfinity_Normal_35_01.
                sp(247299, 241.20960f, 1257.1047f, 240.63419f, (byte) 66, 2000, 0, null); //IDInfinity_Normal_35_01.
                sp(247299, 241.27136f, 1241.9293f, 240.63419f, (byte) 55, 2000, 0, null); //IDInfinity_Normal_35_01.
				sp(247300, 253.67844f, 1262.4369f, 240.71162f, (byte) 75, 3500, 0, null); //IDInfinity_Normal_35_02.
				sp(247300, 259.53360f, 1249.4660f, 240.71162f, (byte) 59, 4500, 0, null); //IDInfinity_Normal_35_02.
                sp(247300, 254.33441f, 1236.7448f, 240.71162f, (byte) 44, 5500, 0, null); //IDInfinity_Normal_35_02.
			break;
			case 36:
			    sp(247244, 241.41281f, 1249.5133f, 240.63419f, (byte) 60, 3500, 0, null); //IDInfinity_Named_36.
				sp(247373, 246.26080f, 1254.5687f, 240.63419f, (byte) 59, 2000, 0, null); //IDInfinity_Named_36_Summon_10.
                sp(247373, 246.05994f, 1244.3612f, 240.63419f, (byte) 57, 2000, 0, null); //IDInfinity_Named_36_Summon_10.
			break;
			case 37:
			    sp(247301, 246.11182f, 1254.5605f, 240.63419f, (byte) 59, 2000, 0, null); //IDInfinity_Normal_37_01.
                sp(247301, 246.02896f, 1244.5856f, 240.63419f, (byte) 59, 2000, 0, null); //IDInfinity_Normal_37_01.
				sp(247302, 241.12560f, 1247.9340f, 240.63419f, (byte) 59, 2000, 0, null); //IDInfinity_Normal_37_02.
                sp(247302, 241.15268f, 1251.1821f, 240.63419f, (byte) 59, 2000, 0, null); //IDInfinity_Normal_37_02.
			break;
			case 38:
			    sp(247303, 241.44724f, 1249.6206f, 240.63419f, (byte) 60, 2000, 0, null); //IDInfinity_Normal_38_01.
                sp(247303, 254.86494f, 1242.2816f, 240.63419f, (byte) 50, 2000, 0, null); //IDInfinity_Normal_38_01.
                sp(247303, 254.50504f, 1256.7817f, 240.63419f, (byte) 70, 2000, 0, null); //IDInfinity_Normal_38_01.
				sp(247304, 241.28506f, 1260.7113f, 240.63419f, (byte) 61, 2000, 0, null); //IDInfinity_Normal_38_02.
                sp(247304, 241.32637f, 1238.9640f, 240.63419f, (byte) 61, 2000, 0, null); //IDInfinity_Normal_38_02.
			break;
			case 39:
			    sp(247305, 246.26220f, 1244.2563f, 240.63419f, (byte) 60, 2000, 0, null); //IDInfinity_Normal_39_01.
                sp(247305, 246.33160f, 1254.7949f, 240.63419f, (byte) 62, 2000, 0, null); //IDInfinity_Normal_39_01.
				sp(247306, 241.41690f, 1249.6360f, 240.63419f, (byte) 59, 2000, 0, null); //IDInfinity_Normal_39_02.
			break;
			case 40: //Floor Named 40 [Final Boss]
			    sp(247245, 241.05147f, 249.50418f, 971.14140f, (byte) 60, 2000, 0, null); //ë§ˆë…€ ê·¸ë Œë‹¬.
				bossTimerStart = System.currentTimeMillis();
			break;
		}
	}
	
	@Override
    public void onDie(final Npc npc) {
		Player player = npc.getAggroList().getMostPlayerDamage();
        switch (npc.getNpcId()) {
			//Floor 1
			case 247247:
			case 247248:
			    despawnNpc(npc);
				if (getNpcs(247247).isEmpty() &&
				    getNpcs(247248).isEmpty()) {
					floor = 2;
					deleteNpc(701000);
					sp(701773, 280.65912f, 1249.3933f, 240.99275f, (byte) 0, 114, 1500, 0, null);
					sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", floor - 1);
					player.setFloor(floor - 1);
					rewardForFloorId(player);
				}
			break;
			//Floor 2
			case 247249:
			case 247250:
			    despawnNpc(npc);
			    if (getNpcs(247249).isEmpty() &&
				    getNpcs(247250).isEmpty()) {
					floor = 3;
					deleteNpc(701000);
					sp(701773, 280.65912f, 1249.3933f, 240.99275f, (byte) 0, 114, 1500, 0, null);
					sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", floor - 1);
					player.setFloor(floor - 1);
					rewardForFloorId(player);
				}
			break;
			//Floor 3
			case 247251:
			case 247252:
			    despawnNpc(npc);
			    if (getNpcs(247251).isEmpty() &&
				    getNpcs(247252).isEmpty()) {
					floor = 4;
					deleteNpc(701000);
					sp(701773, 280.65912f, 1249.3933f, 240.99275f, (byte) 0, 114, 1500, 0, null);
					sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", floor - 1);
					player.setFloor(floor - 1);
					rewardForFloorId(player);
				}
			break;
			//Floor 4
			case 247236:
			    despawnNpc(npc);
			    if (getNpcs(247236).isEmpty()) {
					floor = 5;
					deleteNpc(701000);
					sp(701773, 280.65912f, 1249.3933f, 240.99275f, (byte) 0, 114, 1500, 0, null);
					sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", floor - 1);
					player.setFloor(floor - 1);
					rewardForFloorId(player);
				}
			break;
			//Floor 5
			case 247253:
			case 247254:
			    despawnNpc(npc);
			    if (getNpcs(247253).isEmpty() &&
				    getNpcs(247254).isEmpty()) {
					floor = 6;
					deleteNpc(701000);
					despawnNpcs(getNpcs(247351)); //Meat.
					sp(701773, 280.65912f, 1249.3933f, 240.99275f, (byte) 0, 114, 1500, 0, null);
					sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", floor - 1);
					player.setFloor(floor - 1);
					rewardForFloorId(player);
				}
			break;
			//Floor 6
			case 247255:
			case 247256:
			    despawnNpc(npc);
				if (getNpcs(247255).isEmpty() &&
				    getNpcs(247256).isEmpty()) {
					floor = 7;
					deleteNpc(701000);
					despawnNpcs(getNpcs(247352)); //Meat.
					sp(701773, 280.65912f, 1249.3933f, 240.99275f, (byte) 0, 114, 1500, 0, null);
					sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", floor - 1);
					player.setFloor(floor - 1);
					rewardForFloorId(player);
				}
			break;
			//Floor 7
			case 247257:
			case 247258:
			    despawnNpc(npc);
			    if (getNpcs(247257).isEmpty() &&
				    getNpcs(247258).isEmpty()) {
					floor = 8;
					deleteNpc(701000);
					despawnNpcs(getNpcs(247354)); //Meat.
					sp(701773, 280.65912f, 1249.3933f, 240.99275f, (byte) 0, 114, 1500, 0, null);
					sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", floor - 1);
					player.setFloor(floor - 1);
					rewardForFloorId(player);
				}
			break;
			//Floor 8
            case 247353:
			case 247237:
			    despawnNpc(npc);
			    if (getNpcs(247353).isEmpty() &&
				    getNpcs(247237).isEmpty()) {
					floor = 9;
					deleteNpc(701000);
					despawnNpcs(getNpcs(247478)); //IDInfinity_Meat.
					despawnNpcs(getNpcs(247401)); //IDInfinity_Summon_Tog.
					sp(701773, 280.65912f, 1249.3933f, 240.99275f, (byte) 0, 114, 1500, 0, null);
					sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", floor - 1);
					player.setFloor(floor - 1);
					rewardForFloorId(player);
				}
			break;
			//Floor 9
			case 247259:
			case 247260:
			    despawnNpc(npc);
			    if (getNpcs(247259).isEmpty() &&
				    getNpcs(247260).isEmpty()) {
					floor = 10;
					deleteNpc(701000);
					sp(701773, 280.65912f, 1249.3933f, 240.99275f, (byte) 0, 114, 1500, 0, null);
					sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", floor - 1);
					player.setFloor(floor - 1);
					rewardForFloorId(player);
				}
			break;
			//Floor 10
			case 247261:
			case 247262:
			    despawnNpc(npc);
			    if (getNpcs(247261).isEmpty() &&
				    getNpcs(247262).isEmpty()) {
					floor = 11;
					deleteNpc(701000);
					sp(701773, 280.65912f, 1249.3933f, 240.99275f, (byte) 0, 114, 1500, 0, null);
					sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", floor - 1);
					player.setFloor(floor - 1);
					rewardForFloorId(player);
				}
			break;
			//Floor 11
			case 247263:
			case 247264:
			    despawnNpc(npc);
			    if (getNpcs(247263).isEmpty() &&
				    getNpcs(247264).isEmpty()) {
					floor = 12;
					deleteNpc(701000);
					sp(701773, 280.65912f, 1249.3933f, 240.99275f, (byte) 0, 114, 1500, 0, null);
					sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", floor - 1);
					player.setFloor(floor - 1);
					rewardForFloorId(player);
				}
			break;
			//Floor 12
			case 247361:
			    despawnNpc(npc);
				if (getNpcs(247361).isEmpty()) {
					spawn(247362, npc.getX(), npc.getY(), npc.getZ(), npc.getHeading());
					spawn(247362, npc.getX(), npc.getY(), npc.getZ(), npc.getHeading());
				}
			break;
			case 247362:
			    despawnNpc(npc);
				if (getNpcs(247362).isEmpty()) {
					spawn(247363, npc.getX(), npc.getY(), npc.getZ(), npc.getHeading());
					spawn(247363, npc.getX(), npc.getY(), npc.getZ(), npc.getHeading());
				}
			break;
			case 247363:
			    despawnNpc(npc);
				if (getNpcs(247363).isEmpty()) {
					spawn(247400, npc.getX(), npc.getY(), npc.getZ(), npc.getHeading());
					spawn(247400, npc.getX(), npc.getY(), npc.getZ(), npc.getHeading());
				}
			break;
			case 247400:
			    despawnNpc(npc);
			    if (getNpcs(247400).isEmpty()) {
					floor = 13;
					deleteNpc(701000);
					sp(701773, 280.65912f, 1249.3933f, 240.99275f, (byte) 0, 114, 1500, 0, null);
					sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", floor - 1);
					player.setFloor(floor - 1);
					rewardForFloorId(player);
				}
			break;
			//Floor 13
			case 247265:
			case 247266:
			    despawnNpc(npc);
			    if (getNpcs(247265).isEmpty() &&
				    getNpcs(247266).isEmpty()) {
					floor = 14;
					deleteNpc(701000);
					sp(701773, 280.65912f, 1249.3933f, 240.99275f, (byte) 0, 114, 1500, 0, null);
					sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", floor - 1);
					player.setFloor(floor - 1);
					rewardForFloorId(player);
				}
			break;
			//Floor 14
			case 247267:
			case 247268:
			    despawnNpc(npc);
			    if (getNpcs(247267).isEmpty() &&
				    getNpcs(247268).isEmpty()) {
					floor = 15;
					deleteNpc(701000);
					sp(701773, 280.65912f, 1249.3933f, 240.99275f, (byte) 0, 114, 1500, 0, null);
					sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", floor - 1);
					player.setFloor(floor - 1);
					rewardForFloorId(player);

				}
			break;
			//Floor 15
			case 247269:
			case 247270:
			    despawnNpc(npc);
			    if (getNpcs(247269).isEmpty() &&
				    getNpcs(247270).isEmpty()) {
					floor = 16;
					deleteNpc(701000);
					sp(701773, 280.65912f, 1249.3933f, 240.99275f, (byte) 0, 114, 1500, 0, null);
					sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", floor - 1);
					player.setFloor(floor - 1);
					rewardForFloorId(player);
				}
			break;
			//Floor 16
			case 247239:
			    despawnNpc(npc);
				if (getNpcs(247239).isEmpty()) {
					floor = 17;
					deleteNpc(701000);
					sp(701773, 280.65912f, 1249.3933f, 240.99275f, (byte) 0, 114, 1500, 0, null);
					sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", floor - 1);
					player.setFloor(floor - 1);
					rewardForFloorId(player);
				}
			break;
			//Floor 17
			case 247271:
			case 247272:
			    despawnNpc(npc);
			    if (getNpcs(247271).isEmpty() &&
				    getNpcs(247272).isEmpty()) {
					floor = 18;
					deleteNpc(701000);
					sp(701773, 280.65912f, 1249.3933f, 240.99275f, (byte) 0, 114, 1500, 0, null);
					sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", floor - 1);
					player.setFloor(floor - 1);
					rewardForFloorId(player);
				}
			break;
			//Floor 18
			case 247273:
			case 247274:
			case 247355:
			    despawnNpc(npc);
			    if (getNpcs(247273).isEmpty() &&
				    getNpcs(247274).isEmpty() &&
					getNpcs(247355).isEmpty()) {
					floor = 19;
					deleteNpc(701000);
					sp(701773, 280.65912f, 1249.3933f, 240.99275f, (byte) 0, 114, 1500, 0, null);
					sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", floor - 1);
					player.setFloor(floor - 1);
					rewardForFloorId(player);
				}
			break;
			//Floor 19
			case 247275:
			case 247276:
			case 247356:
			    despawnNpc(npc);
				if (getNpcs(247275).isEmpty() &&
				    getNpcs(247276).isEmpty() &&
					getNpcs(247356).isEmpty()) {
					floor = 20;
					deleteNpc(701000);
					sp(701773, 280.65912f, 1249.3933f, 240.99275f, (byte) 0, 114, 1500, 0, null);
					sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", floor - 1);
					player.setFloor(floor - 1);
					rewardForFloorId(player);
				}
			break;
			//Floor 20
			case 247240:
			    despawnNpc(npc);
			    if (getNpcs(247240).isEmpty()) {
					floor = 21;
					deleteNpc(701000);
					sp(701773, 280.65912f, 1249.3933f, 240.99275f, (byte) 0, 114, 1500, 0, null);
					sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", floor - 1);
					player.setFloor(floor - 1);
					rewardForFloorId(player);
				}
			break;
			//Floor 21
			case 247277:
			case 247278:
			    despawnNpc(npc);
			    if (getNpcs(247277).isEmpty() &&
				    getNpcs(247278).isEmpty()) {
					floor = 22;
					deleteNpc(701000);
					sp(701773, 280.65912f, 1249.3933f, 240.99275f, (byte) 0, 114, 1500, 0, null);
					sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", floor - 1);
					player.setFloor(floor - 1);
					rewardForFloorId(player);
				}
			break;
			//Floor 22
			case 247279:
			case 247280:
			    despawnNpc(npc);
			    if (getNpcs(247279).isEmpty() &&
				    getNpcs(247280).isEmpty()) {
					floor = 23;
					deleteNpc(701000);
					sp(701773, 280.65912f, 1249.3933f, 240.99275f, (byte) 0, 114, 1500, 0, null);
					sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", floor - 1);
					player.setFloor(floor - 1);
					rewardForFloorId(player);
				}
			break;
			//Floor 23
			case 247281:
			case 247282:
			    despawnNpc(npc);
			    if (getNpcs(247281).isEmpty() &&
				    getNpcs(247282).isEmpty()) {
					floor = 24;
					deleteNpc(701000);
					sp(701773, 280.65912f, 1249.3933f, 240.99275f, (byte) 0, 114, 1500, 0, null);
					sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", floor - 1);
					player.setFloor(floor - 1);
					rewardForFloorId(player);
				}
			break;
			//Floor 24
			case 247241:
			    despawnNpc(npc);
			    if (getNpcs(247241).isEmpty()) {
					floor = 25;
					deleteNpc(701000);
					sp(701773, 280.65912f, 1249.3933f, 240.99275f, (byte) 0, 114, 1500, 0, null);
					sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", floor - 1);
					player.setFloor(floor - 1);
					rewardForFloorId(player);
				}
			break;
			//Floor 25
			case 247283:
			case 247284:
			    despawnNpc(npc);
			    if (getNpcs(247283).isEmpty() &&
				    getNpcs(247284).isEmpty()) {
					floor = 26;
					deleteNpc(701000);
					sp(701773, 280.65912f, 1249.3933f, 240.99275f, (byte) 0, 114, 1500, 0, null);
					sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", floor - 1);
					player.setFloor(floor - 1);
					rewardForFloorId(player);
				}
			break;
			//Floor 26
			case 247285:
			case 247286:
			    despawnNpc(npc);
			    if (getNpcs(247285).isEmpty() &&
				    getNpcs(247286).isEmpty()) {
					floor = 27;
					deleteNpc(701000);
					sp(701773, 280.65912f, 1249.3933f, 240.99275f, (byte) 0, 114, 1500, 0, null);
					sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", floor - 1);
					player.setFloor(floor - 1);
					rewardForFloorId(player);
				}
			break;
			//Floor 27
			case 247287:
			    despawnNpc(npc);
			    if (getNpcs(247287).isEmpty()) {
					floor = 28;
					deleteNpc(701000);
					sp(701773, 280.65912f, 1249.3933f, 240.99275f, (byte) 0, 114, 1500, 0, null);
					sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", floor - 1);
					player.setFloor(floor - 1);
					rewardForFloorId(player);
				}
			break;
			//Floor 28
			case 247242:
			    despawnNpc(npc);
			    if (getNpcs(247242).isEmpty()) {
					floor = 29;
					deleteNpc(701000);
					sp(701773, 280.65912f, 1249.3933f, 240.99275f, (byte) 0, 114, 1500, 0, null);
					sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", floor - 1);
					player.setFloor(floor - 1);
					rewardForFloorId(player);
				}
			break;
			//Floor 29
			case 247289:
			case 247290:
			    despawnNpc(npc);
			    if (getNpcs(247289).isEmpty() &&
				    getNpcs(247290).isEmpty()) {
					floor = 30;
					deleteNpc(701000);
					despawnNpcs(getNpcs(701692)); //IDInfinity_Pot.
				    despawnNpcs(getNpcs(247359)); //IDInfinity_Fire_Red.
				    despawnNpcs(getNpcs(247360)); //IDInfinity_Fire_Blue.
					sp(701773, 280.65912f, 1249.3933f, 240.99275f, (byte) 0, 114, 1500, 0, null);
					sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", floor - 1);
					player.setFloor(floor - 1);
					rewardForFloorId(player);
				}
			break;
			//Floor 30
			case 247291:
			case 247292:
			    despawnNpc(npc);
			    if (getNpcs(247291).isEmpty() &&
				    getNpcs(247292).isEmpty()) {
					floor = 31;
					deleteNpc(701000);
					despawnNpcs(getNpcs(701692)); //IDInfinity_Pot.
				    despawnNpcs(getNpcs(247359)); //IDInfinity_Fire_Red.
				    despawnNpcs(getNpcs(247360)); //IDInfinity_Fire_Blue.
					sp(701773, 280.65912f, 1249.3933f, 240.99275f, (byte) 0, 114, 1500, 0, null);
					sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", floor - 1);
					player.setFloor(floor - 1);
					rewardForFloorId(player);
				}
			break;
			//Floor 31
			case 247293:
			case 247294:
			    despawnNpc(npc);
			    if (getNpcs(247293).isEmpty() &&
				    getNpcs(247294).isEmpty()) {
					floor = 32;
					deleteNpc(701000);
					despawnNpcs(getNpcs(701692)); //IDInfinity_Pot.
				    despawnNpcs(getNpcs(247359)); //IDInfinity_Fire_Red.
				    despawnNpcs(getNpcs(247360)); //IDInfinity_Fire_Blue.
					sp(701773, 280.65912f, 1249.3933f, 240.99275f, (byte) 0, 114, 1500, 0, null);
					sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", floor - 1);
					player.setFloor(floor - 1);
					rewardForFloorId(player);
				}
			break;
			//Floor Named 32
			case 247243:
			    despawnNpc(npc);
			    if (getNpcs(247243).isEmpty()) {
					floor = 33;
					deleteNpc(701000);
					despawnNpcs(getNpcs(701692)); //IDInfinity_Pot.
				    despawnNpcs(getNpcs(247359)); //IDInfinity_Fire_Red.
				    despawnNpcs(getNpcs(247360)); //IDInfinity_Fire_Blue.
					sp(701773, 280.65912f, 1249.3933f, 240.99275f, (byte) 0, 114, 1500, 0, null);
					sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", floor - 1);
					player.setFloor(floor - 1);
					rewardForFloorId(player);
				}
			break;
			//Floor 33
			case 247295:
			case 247296:
			    despawnNpc(npc);
			    if (getNpcs(247295).isEmpty() &&
				    getNpcs(247296).isEmpty()) {
					floor = 34;
					deleteNpc(701000);
					sp(701773, 280.65912f, 1249.3933f, 240.99275f, (byte) 0, 114, 1500, 0, null);
					sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", floor - 1);
					player.setFloor(floor - 1);
					rewardForFloorId(player);
				}
			break;
			//Floor 34
			case 247297:
			case 247298:
			    despawnNpc(npc);
			    if (getNpcs(247297).isEmpty() &&
				    getNpcs(247298).isEmpty()) {
					floor = 35;
					deleteNpc(701000);
					sp(701773, 280.65912f, 1249.3933f, 240.99275f, (byte) 0, 114, 1500, 0, null);
					sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", floor - 1);
					player.setFloor(floor - 1);
					rewardForFloorId(player);
				}
			break;
			//Floor 35
			case 247299:
			case 247300:
			    despawnNpc(npc);
			    if (getNpcs(247299).isEmpty() &&
				    getNpcs(247300).isEmpty()) {
					floor = 36;
					deleteNpc(701000);
					sp(701773, 280.65912f, 1249.3933f, 240.99275f, (byte) 0, 114, 1500, 0, null);
					sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", floor - 1);
					player.setFloor(floor - 1);
					rewardForFloorId(player);
				}
			break;
			//Floor 36
			case 247244:
			    despawnNpc(npc);
			    if (getNpcs(247244).isEmpty()) {
					floor = 37;
					deleteNpc(701000);
					despawnNpcs(getNpcs(247373)); //IDInfinity_Named_36_Summon_10.
					sp(701773, 280.65912f, 1249.3933f, 240.99275f, (byte) 0, 114, 1500, 0, null);
					sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", floor - 1);
					player.setFloor(floor - 1);
					rewardForFloorId(player);
				}
			break;
			//Floor 37
			case 247301:
			case 247302:
			    despawnNpc(npc);
			    if (getNpcs(247301).isEmpty() &&
				    getNpcs(247302).isEmpty()) {
					floor = 38;
					deleteNpc(701000);
					sp(701773, 280.65912f, 1249.3933f, 240.99275f, (byte) 0, 114, 1500, 0, null);
					sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", floor - 1);
					player.setFloor(floor - 1);
					rewardForFloorId(player);
				}
			break;
			//Floor 38
			case 247303:
			case 247304:
			    despawnNpc(npc);
			    if (getNpcs(247303).isEmpty() &&
				    getNpcs(247304).isEmpty()) {
					floor = 39;
					deleteNpc(701000);
					sp(701773, 280.65912f, 1249.3933f, 240.99275f, (byte) 0, 114, 1500, 0, null);
					sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", floor - 1);
					player.setFloor(floor - 1);
					rewardForFloorId(player);
				}
			break;
			//Floor 39
			case 247305:
			case 247306:
			    despawnNpc(npc);
			    if (getNpcs(247305).isEmpty() &&
				    getNpcs(247306).isEmpty()) {
					floor = 40;
					deleteNpc(701000);
					sp(701773, 280.65912f, 1249.3933f, 240.99275f, (byte) 0, 114, 1500, 0, null);
					sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", floor - 1);
					player.setFloor(floor - 1);
					rewardForFloorId(player);
				}
			break;
			//Floor 40
			case 247245: //ë§ˆë…€ ê·¸ë Œë‹¬.
			    despawnNpc(npc);
			    if (getNpcs(247245).isEmpty()) {
					sendPacket(player, "Condition_Infinity_THIS_SEASON_Floor_Reward", 100);
			        floor = 41;
                    player.setFloor(floor - 1); 
					rewardForFloorId(player);
					bossTimerEnd = System.currentTimeMillis() - bossTimerStart;
					// SeasonRankingService.getInstance().saveCrusibleSpireTime(player, (int)bossTimerEnd);
				}
			break;
		}
    }
	
    public void rewardForFloorId(Player player) {
		final TowerStageRewardTemplate reward = DataManager.TOWER_REWARD_DATA.getTowerReward(player.getFloor());
		int itemId1 = reward.getItemId();
		int itemCount1 = reward.getItemCount();
		if (itemId1 != 0 && itemCount1 != 0) {
			ItemService.addItem(player, itemId1, itemCount1);
		}
		int itemId2 = reward.getItemId2();
		int itemCount2 = reward.getItemCount2();
		if (itemId2 != 0 && itemCount2 != 0) {
			ItemService.addItem(player, itemId2, itemCount2);
		}
		int kinahCount = reward.getKinahCount();
		if (kinahCount != 0) {
			ItemService.addItem(player, 182400001, kinahCount);
		}
		int expCount = reward.getExpCount();
		if (expCount != 0) {
			player.getCommonData().addExp(expCount, RewardType.QUEST);
		}
		int apCount = reward.getApCount();
		if (apCount != 0) {
			AbyssPointsService.addAp(player, apCount);
		}
		int gpCount = reward.getGpCount();
		if (gpCount != 0) {
			AbyssPointsService.addGp(player, gpCount);
		}
	}
	
	@Override
    public boolean onPassFlyingRing(Player player, String flyingRing) {
        if (flyingRing.equals("FLOOR")) {
			instance.doOnAllPlayers(new Visitor<Player>() {
				@Override
				public void visit(Player player) {
					if (player.isOnline()) {
						teleportCrucibleFloor(player);
					}
				}
			});
		}
		return false;
	}
	
	private void sendMsg(final String str) {
		instance.doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				PacketSendUtility.sendWhiteMessageOnCenter(player, str);
			}
		});
	}
	
	protected void sendMsgByRace(final int msg, final Race race, int time) {
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				instance.doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						if (player.getRace().equals(race) || race.equals(Race.PC_ALL)) {
							PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(msg));
						}
					}
				});
			}
		}, time);
	}
	
	private void sendMessage(final int msgId, long delay) {
        if (delay == 0) {
            this.sendMsg(msgId);
        } else {
            ThreadPoolManager.getInstance().schedule(new Runnable() {
                public void run() {
                    sendMsg(msgId);
                }
            }, delay);
        }
    }
	
	private void deleteNpc(int npcId) {
		if (getNpc(npcId) != null) {
			getNpc(npcId).getController().onDelete();
		}
	}
	
	private void despawnNpc(Npc npc) {
		if (npc != null) {
			npc.getController().onDelete();
		}
	}
	
	protected void despawnNpcs(List<Npc> npcs) {
		for (Npc npc: npcs) {
			npc.getController().onDelete();
		}
	}
	
	protected List<Npc> getNpcs(int npcId) {
		if (!isInstanceDestroyed) {
			return instance.getNpcs(npcId);
		}
		return null;
	}
	
	public void onFailCrucible(Player player) {
		TeleportService2.moveToInstanceExit(player, mapId, player.getRace());
	}
	
	public void onExitInstance(Player player) {
		removeItems(player);
		TeleportService2.moveToInstanceExit(player, mapId, player.getRace());
	}
	
    public void onPlayerLogOut(Player player) {
		removeItems(player);
		TeleportService2.moveToInstanceExit(player, mapId, player.getRace());
    }
	
	@Override
	public void onInstanceDestroy() {
		isInstanceDestroyed = true;
		doors.clear();
	}
	
	private void teleportFloor(float x, float y, float z, byte h) {
		for (Player playerInside: instance.getPlayersInside()) {
			if (playerInside.isOnline()) {
				teleportCrucibleFloor(playerInside);
			}
		}
	}
	
	protected void teleportFloor(Player player, float x, float y, float z, byte h) {
		TeleportService2.teleportTo(player, mapId, instanceId, x, y, z, h);
	}
	protected void sp(final int npcId, final float x, final float y, final float z, final byte h, final int time) {
        sp(npcId, x, y, z, h, 0, time, 0, null);
    }
	
    protected void sp(final int npcId, final float x, final float y, final float z, final byte h, final int time, final int msg, final Race race) {
        sp(npcId, x, y, z, h, 0, time, msg, race);
    }
	
	protected void sp(final int npcId, final float x, final float y, final float z, final byte h, final int entityId, final int time, final int msg, final Race race) {
        crucibleTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                if (!isInstanceDestroyed) {
                    spawn(npcId, x, y, z, h, entityId);
                    if (msg > 0) {
                        sendMsgByRace(msg, race, 0);
                    }
                }
            }
        }, time));
    }
	
    protected void sp(final int npcId, final float x, final float y, final float z, final byte h, final int time, final String walkerId) {
        crucibleTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                if (!isInstanceDestroyed) {
                    Npc npc = (Npc) spawn(npcId, x, y, z, h);
                    npc.getSpawn().setWalkerId(walkerId);
                    WalkManager.startWalking((NpcAI2) npc.getAi2());
                }
            }
        }, time));
    }
	
	@Override
    public boolean onReviveEvent(Player player) {
		for (Npc npc: instance.getNpcs()) {
			npc.getController().onDelete();
		}
		spawnInggrilInggness2();
		player.getGameStats().updateStatsAndSpeedVisually();
		PlayerReviveService.revive(player, 100, 100, false, 0);
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
		PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_INFINITY_INDUN_RESURRECT, 0, 0));
		onFailCrucible(player);
		return true;
    }
}