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
package ai.instance.crucibleSpire;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;

import java.util.List;

/****/
/** Author (Encom)
/****/

@AIName("IDInfinity_Teleport_Odd_Number")
public class ChronomancerAI2 extends NpcAI2
{
	@Override
	protected void handleDialogStart(Player player) {
		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1011));
	}
	
	@Override
	public boolean onDialogSelect(Player player, int dialogId, int questId, int extendedRewardIndex) {
		int pfloor = player.getFloor();
		int instanceId = getPosition().getInstanceId();
		if (dialogId == 10000) {
		    switch (getNpcId()) {
				case 247376:
			    case 247386:
				    ThreadPoolManager.getInstance().schedule(new Runnable() {
						@Override
						public void run() {
							despawnNpc(701772); //Stair.
						}
					}, 5000);
					spawnFloor(pfloor + 1);
					spawn(701000, 263.55551f, 1249.5244f, 240.73053f, (byte) 0, 56); //Wall.
					TeleportService2.teleportTo(player, 302400000, instanceId, 219.33264f, 1249.4528f, 240.85301f, (byte) 0);
					if (pfloor == 39) {
						TeleportService2.teleportTo(player, 302400000, instanceId, 210.42656f, 249.58434f, 971.39510f, (byte) 0);
					}
				break;
			}
		}
		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 0));
		return true;
	}
	
	private void spawnFloor(int next) {
		switch (next) {
			case 1:
				spawn(247247, 241.31040f, 1249.5646f, 240.63419f, (byte) 60); //IDInfinity_Normal_01_01.
				spawn(247248, 246.45409f, 1254.9410f, 240.63419f, (byte) 60); //IDInfinity_Normal_01_02.
				spawn(247248, 246.61267f, 1244.2950f, 240.63419f, (byte) 60); //IDInfinity_Normal_01_02.
			break;
			case 2:
				spawn(247249, 241.18533f, 1256.9836f, 240.63419f, (byte) 61); //IDInfinity_Normal_02_01.
				spawn(247249, 241.29503f, 1242.1162f, 240.63419f, (byte) 60); //IDInfinity_Normal_02_01.
				spawn(247250, 244.41614f, 1246.4686f, 240.63419f, (byte) 61); //IDInfinity_Normal_02_02.
                spawn(247250, 244.21953f, 1252.8258f, 240.63419f, (byte) 61); //IDInfinity_Normal_02_02.
			break;
			case 3:
				spawn(247251, 248.74625f, 1249.5614f, 240.63419f, (byte) 60); //IDInfinity_Normal_03_01.
                spawn(247251, 241.17021f, 1257.0828f, 240.63419f, (byte) 61); //IDInfinity_Normal_03_01.
                spawn(247251, 241.17314f, 1242.0288f, 240.63419f, (byte) 61); //IDInfinity_Normal_03_01.
				spawn(247252, 259.44302f, 1249.6051f, 240.71162f, (byte) 60); //IDInfinity_Normal_03_02.
                spawn(247252, 253.85323f, 1262.3983f, 240.71162f, (byte) 76); //IDInfinity_Normal_03_02.
                spawn(247252, 254.24211f, 1236.7045f, 240.71162f, (byte) 45); //IDInfinity_Normal_03_02.
			break;
			case 4:
				spawn(247236, 241.45598f, 1249.5740f, 240.63419f, (byte) 60); //IDInfinity_Named_04.
			break;
			case 5:
				spawn(247351, 254.19934f, 1262.5269f, 240.71162f, (byte) 36); //IDInfinity_Meat_01.
				spawn(247351, 253.87862f, 1236.6519f, 240.71162f, (byte) 9); //IDInfinity_Meat_01.
				spawn(247253, 252.67638f, 1264.4946f, 240.71162f, (byte) 101); //IDInfinity_Normal_05_01.
				spawn(247253, 252.09209f, 1233.2440f, 240.71162f, (byte) 14); //IDInfinity_Normal_05_01.
                spawn(247253, 250.93297f, 1235.5901f, 240.71162f, (byte) 7); //IDInfinity_Normal_05_01.
                spawn(247253, 251.68967f, 1262.4159f, 240.71162f, (byte) 113); //IDInfinity_Normal_05_01.
				spawn(247254, 255.53033f, 1260.8771f, 240.71162f, (byte) 42); //IDInfinity_Normal_05_02.
                spawn(247254, 256.08560f, 1237.6760f, 240.71162f, (byte) 77); //IDInfinity_Normal_05_02.
                spawn(247254, 253.70976f, 1238.7314f, 240.71162f, (byte) 84); //IDInfinity_Normal_05_02.
			break;
			case 6:
				spawn(247352, 253.81844f, 1262.5720f, 240.71162f, (byte) 61); //IDInfinity_Meat_02.
                spawn(247352, 245.95355f, 1231.9944f, 240.71162f, (byte) 25); //IDInfinity_Meat_02.
				spawn(247255, 252.66000f, 1265.1761f, 240.71162f, (byte) 102); //IDInfinity_Normal_06_01.
                spawn(247255, 251.31412f, 1262.7773f, 240.71162f, (byte) 114); //IDInfinity_Normal_06_01.
				spawn(247255, 248.28351f, 1231.3329f, 240.71162f, (byte) 58); //IDInfinity_Normal_06_01.
				spawn(247256, 256.23257f, 1261.0988f, 240.71162f, (byte) 46); //IDInfinity_Normal_06_02.
                spawn(247256, 254.00407f, 1259.5002f, 240.71162f, (byte) 36); //IDInfinity_Normal_06_02.
                spawn(247256, 242.98157f, 1229.6989f, 240.71162f, (byte) 6); //IDInfinity_Normal_06_02.
                spawn(247256, 242.80790f, 1232.9885f, 240.71162f, (byte) 116); //IDInfinity_Normal_06_02.
                spawn(247256, 247.98917f, 1234.1317f, 240.71162f, (byte) 73); //IDInfinity_Normal_06_02.
			break;
			case 7:
				spawn(247354, 254.03172f, 1262.3046f, 240.71162f, (byte) 113); //IDInfinity_Meat_03.
				spawn(247354, 253.98647f, 1236.5012f, 240.71162f, (byte) 23); //IDInfinity_Meat_03.
				spawn(247257, 252.40446f, 1265.0981f, 241.09122f, (byte) 103); //IDInfinity_Normal_07_01.
                spawn(247257, 251.31787f, 1263.1189f, 240.71162f, (byte) 113); //IDInfinity_Normal_07_01.
                spawn(247257, 252.93219f, 1233.4966f, 240.71162f, (byte) 17); //IDInfinity_Normal_07_01.
                spawn(247257, 251.10315f, 1235.8412f, 240.71162f, (byte) 7); //IDInfinity_Normal_07_01.
				spawn(247258, 256.58646f, 1261.8520f, 240.71162f, (byte) 49); //IDInfinity_Normal_07_02.
                spawn(247258, 254.23650f, 1260.2627f, 240.71162f, (byte) 36); //IDInfinity_Normal_07_02.
                spawn(247258, 254.60794f, 1239.1844f, 240.71162f, (byte) 83); //IDInfinity_Normal_07_02.
                spawn(247258, 256.51114f, 1237.3129f, 240.71162f, (byte) 71); //IDInfinity_Normal_07_02.
			break;
			case 8:
				spawn(247237, 241.19528f, 1249.6161f, 240.63419f, (byte) 60); //IDInfinity_Named_08.
				spawn(247353, 236.23781f, 1266.9647f, 240.71162f, (byte) 95); //IDInfinity_Berrel.
                spawn(247353, 254.19570f, 1236.9214f, 240.71162f, (byte) 44); //IDInfinity_Berrel.
                spawn(247353, 253.82140f, 1262.2661f, 240.71162f, (byte) 74); //IDInfinity_Berrel.
                spawn(247353, 236.80017f, 1231.8394f, 240.71162f, (byte) 25); //IDInfinity_Berrel.
				spawn(247401, 241.33926f, 1241.8188f, 240.63419f, (byte) 57); //IDInfinity_Summon_Tog.
				spawn(247401, 241.39090f, 1257.0540f, 240.63419f, (byte) 60); //IDInfinity_Summon_Tog.
                spawn(247401, 249.24400f, 1249.5896f, 240.63419f, (byte) 60); //IDInfinity_Summon_Tog.
                spawn(247401, 245.36331f, 1253.8690f, 240.63419f, (byte) 63); //IDInfinity_Summon_Tog.
                spawn(247401, 245.44702f, 1245.5221f, 240.63419f, (byte) 61); //IDInfinity_Summon_Tog.
			break;
			case 9:
				spawn(247259, 241.29816f, 1246.0281f, 240.63419f, (byte) 60); //IDInfinity_Normal_09_01.
                spawn(247259, 241.24530f, 1253.0829f, 240.63419f, (byte) 60); //IDInfinity_Normal_09_01.
				spawn(247260, 241.01903f, 1268.0564f, 240.71162f, (byte) 60); //IDInfinity_Normal_09_02.
                spawn(247260, 241.35820f, 1231.3574f, 240.71162f, (byte) 60); //IDInfinity_Normal_09_02.
			break;
			case 10:
				spawn(247261, 260.63998f, 1252.6572f, 240.71162f, (byte) 63); //IDInfinity_Normal_10_01.
                spawn(247261, 260.53223f, 1246.9858f, 240.71162f, (byte) 59); //IDInfinity_Normal_10_01.
				spawn(247262, 241.46776f, 1249.6780f, 240.63419f, (byte) 61); //IDInfinity_Normal_10_02.
                spawn(247262, 240.68369f, 1264.4435f, 240.63419f, (byte) 68); //IDInfinity_Normal_10_02.
                spawn(247262, 241.24583f, 1234.4695f, 240.63419f, (byte) 53); //IDInfinity_Normal_10_02.
			break;
			case 11:
				spawn(247263, 240.94872f, 1264.3417f, 240.63419f, (byte) 68); //IDInfinity_Normal_11_01.
                spawn(247263, 241.18830f, 1234.7659f, 240.63419f, (byte) 51); //IDInfinity_Normal_11_01.
				spawn(247264, 238.01044f, 1249.5957f, 240.63419f, (byte) 60); //IDInfinity_Normal_11_02.
                spawn(247264, 245.00499f, 1249.6553f, 240.63419f, (byte) 60); //IDInfinity_Normal_11_02.
                spawn(247264, 241.31288f, 1253.0376f, 240.63419f, (byte) 59); //IDInfinity_Normal_11_02.
                spawn(247264, 241.27722f, 1246.1240f, 240.63419f, (byte) 59); //IDInfinity_Normal_11_02.
			break;
			case 12:
				spawn(247238, 241.50197f, 1249.5283f, 240.63419f, (byte) 60); //IDInfinity_Named_12.
			break;
			case 13:
				spawn(247265, 241.18263f, 1249.5398f, 240.63419f, (byte) 60); //IDInfinity_Normal_13_01.
                spawn(247265, 258.61935f, 1253.3063f, 240.71162f, (byte) 62); //IDInfinity_Normal_13_01.
                spawn(247265, 258.54715f, 1246.3235f, 240.71162f, (byte) 58); //IDInfinity_Normal_13_01.
				spawn(247266, 241.18523f, 1260.1180f, 240.63419f, (byte) 59); //IDInfinity_Normal_13_02.
                spawn(247266, 241.04256f, 1239.2163f, 240.63419f, (byte) 60); //IDInfinity_Normal_13_02.
			break;
			case 14:
			    spawn(247267, 241.15300f, 1260.6820f, 240.63419f, (byte) 60); //IDInfinity_Normal_14_01.
                spawn(247267, 241.20154f, 1238.8978f, 240.63419f, (byte) 60); //IDInfinity_Normal_14_01.
				spawn(247268, 260.02588f, 1244.4471f, 240.71162f, (byte) 56); //IDInfinity_Normal_14_02.
                spawn(247268, 260.64893f, 1249.4431f, 240.71162f, (byte) 58); //IDInfinity_Normal_14_02.
                spawn(247268, 259.95993f, 1254.9393f, 240.71162f, (byte) 63); //IDInfinity_Normal_14_02.
			break;
			case 15:
			    spawn(247269, 241.31754f, 1249.5798f, 240.63419f, (byte) 60); //IDInfinity_Normal_15_01.
				spawn(247270, 260.19244f, 1247.5491f, 240.71162f, (byte) 58); //IDInfinity_Normal_15_02.
                spawn(247270, 259.95400f, 1251.8992f, 240.71162f, (byte) 61); //IDInfinity_Normal_15_02.
                spawn(247270, 241.28294f, 1242.0948f, 240.63419f, (byte) 60); //IDInfinity_Normal_15_02.
                spawn(247270, 241.15080f, 1257.0294f, 240.63419f, (byte) 62); //IDInfinity_Normal_15_02.
			break;
			case 16:
				spawn(247239, 241.06525f, 1249.5922f, 240.63419f, (byte) 59); //IDInfinity_Named_16.
                spawn(247239, 246.20310f, 1244.5913f, 240.63419f, (byte) 61); //IDInfinity_Named_16.
                spawn(247239, 246.25809f, 1254.7360f, 240.63419f, (byte) 60); //IDInfinity_Named_16.
			break;
			case 17:
			    spawn(247271, 241.25314f, 1246.1554f, 240.63419f, (byte) 60); //IDInfinity_Normal_17_01.
                spawn(247271, 241.21352f, 1253.0051f, 240.63419f, (byte) 61); //IDInfinity_Normal_17_01.
				spawn(247272, 248.43343f, 1262.3562f, 240.63419f, (byte) 51); //IDInfinity_Normal_17_02.
                spawn(247272, 248.46645f, 1236.8959f, 240.63419f, (byte) 69); //IDInfinity_Normal_17_02.
			break;
			case 18:
				spawn(247273, 259.59085f, 1254.5885f, 240.71162f, (byte) 64); //IDInfinity_Normal_18_01.
                spawn(247273, 259.67526f, 1244.8475f, 240.71162f, (byte) 57); //IDInfinity_Normal_18_01.
				spawn(247274, 241.08554f, 1264.1904f, 240.63419f, (byte) 61); //IDInfinity_Normal_18_02.
				spawn(247355, 259.31012f, 1249.4916f, 240.71162f, (byte) 59); //IDInfinity_Lava_01.
			break;
			case 19:
			    spawn(247275, 255.78647f, 1238.9512f, 240.71162f, (byte) 56); //IDInfinity_Normal_19_01.
                spawn(247275, 250.05235f, 1242.1001f, 240.63419f, (byte) 0); //IDInfinity_Normal_19_01.
				spawn(247276, 257.48360f, 1246.4463f, 240.71162f, (byte) 76); //IDInfinity_Normal_19_02.
                spawn(247276, 247.59322f, 1232.3746f, 240.71162f, (byte) 18); //IDInfinity_Normal_19_02.
				spawn(247356, 248.72170f, 1237.0615f, 240.63419f, (byte) 59); //IDInfinity_Lava_02.
                spawn(247356, 254.23114f, 1242.8639f, 240.63419f, (byte) 75); //IDInfinity_Lava_02.
			break;
			case 20:
			    spawn(247240, 241.38167f, 1249.6044f, 240.63419f, (byte) 60); //IDInfinity_Named_20.
			break;
			case 21:
			    spawn(247277, 241.23924f, 1249.5948f, 240.63419f, (byte) 60); //IDInfinity_Normal_21_01.
				spawn(247278, 246.15216f, 1254.5807f, 240.63419f, (byte) 60); //IDInfinity_Normal_21_02.
                spawn(247278, 246.55399f, 1244.3586f, 240.63419f, (byte) 60); //IDInfinity_Normal_21_02.
			break;
			case 22:
			    spawn(247279, 241.14186f, 1264.6486f, 240.63419f, (byte) 90); //IDInfinity_Normal_22_01.
                spawn(247279, 241.03874f, 1234.2322f, 240.63419f, (byte) 29); //IDInfinity_Normal_22_01.
				spawn(247280, 259.70460f, 1254.7360f, 240.71162f, (byte) 62); //IDInfinity_Normal_22_02.
                spawn(247280, 259.69165f, 1244.3368f, 240.71162f, (byte) 58); //IDInfinity_Normal_22_02.
			break;
			case 23:
			    spawn(247281, 241.41281f, 1249.5133f, 240.63419f, (byte) 60); //IDInfinity_Normal_23_01.
				spawn(247282, 246.13629f, 1244.5453f, 240.63419f, (byte) 60); //IDInfinity_Normal_23_02.
                spawn(247282, 245.91959f, 1254.5669f, 240.63419f, (byte) 62); //IDInfinity_Normal_23_02.
                spawn(247282, 261.21832f, 1249.5925f, 240.71162f, (byte) 60); //IDInfinity_Normal_23_02.
			break;
			case 24:
			    spawn(247241, 241.41281f, 1249.5133f, 240.63419f, (byte) 60); //IDInfinity_Named_24.
			break;
			case 25:
			    spawn(247283, 261.01346f, 1249.5574f, 240.71162f, (byte) 60); //IDInfinity_Normal_25_01.
				spawn(247284, 254.40323f, 1242.5317f, 240.63419f, (byte) 51); //IDInfinity_Normal_25_02.
                spawn(247284, 254.34718f, 1256.6465f, 240.63419f, (byte) 69); //IDInfinity_Normal_25_02.
			break;
			case 26:
			    spawn(247285, 248.59853f, 1249.5581f, 240.63419f, (byte) 60); //IDInfinity_Normal_26_01.
                spawn(247285, 241.13426f, 1234.3336f, 240.63419f, (byte) 30); //IDInfinity_Normal_26_01.
                spawn(247285, 241.01393f, 1264.6925f, 240.63419f, (byte) 90); //IDInfinity_Normal_26_01.
				spawn(247286, 261.09265f, 1249.5715f, 240.71162f, (byte) 60); //IDInfinity_Normal_26_02.
			break;
			case 27:
			    spawn(247287, 241.04361f, 1249.5405f, 240.63419f, (byte) 60); //IDInfinity_Normal_27_01.
                spawn(247287, 241.02223f, 1254.7899f, 240.63419f, (byte) 60); //IDInfinity_Normal_27_01.
                spawn(247287, 241.13141f, 1244.1161f, 240.63419f, (byte) 60); //IDInfinity_Normal_27_01.
                spawn(247287, 244.44319f, 1246.9247f, 240.63419f, (byte) 60); //IDInfinity_Normal_27_01.
                spawn(247287, 244.40683f, 1252.1543f, 240.63419f, (byte) 60); //IDInfinity_Normal_27_01.
			break;
			case 28:
			    spawn(247242, 241.41281f, 1249.5133f, 240.63419f, (byte) 60); //IDInfinity_Named_28.
			break;
			case 29:
			    switch (Rnd.get(1, 2)) {
				    case 1:
				        spawn(701692, 240.99178f, 1236.1763f, 238.74855f, (byte) 0, 186); //IDInfinity_Pot.
						spawn(701692, 240.99176f, 1262.8556f, 238.74855f, (byte) 0, 196); //IDInfinity_Pot.
						spawn(247360, 240.99178f, 1236.1763f, 242.58624f, (byte) 0, 195); //IDInfinity_Fire_Blue.
						spawn(247360, 240.99176f, 1262.8556f, 242.58624f, (byte) 0, 199); //IDInfinity_Fire_Blue.
						spawn(247289, 241.41281f, 1249.5133f, 240.63419f, (byte) 60); //IDInfinity_Normal_29_01.
					break;
					case 2:
				        spawn(701692, 240.99178f, 1236.1763f, 238.74855f, (byte) 0, 186); //IDInfinity_Pot.
						spawn(701692, 240.99176f, 1262.8556f, 238.74855f, (byte) 0, 196); //IDInfinity_Pot.
						spawn(247359, 240.99178f, 1236.1763f, 242.58624f, (byte) 0, 197); //IDInfinity_Fire_Red.
						spawn(247359, 240.99176f, 1262.8556f, 242.58624f, (byte) 0, 198); //IDInfinity_Fire_Red.
						spawn(247290, 241.41281f, 1249.5133f, 240.63419f, (byte) 60); //IDInfinity_Normal_29_02.
					break;
				}
			break;
			case 30:
			    switch (Rnd.get(1, 2)) {
				    case 1:
				        spawn(701692, 240.99178f, 1236.1763f, 238.74855f, (byte) 0, 186); //IDInfinity_Pot.
						spawn(701692, 240.99176f, 1262.8556f, 238.74855f, (byte) 0, 196); //IDInfinity_Pot.
						spawn(247360, 240.99178f, 1236.1763f, 242.58624f, (byte) 0, 195); //IDInfinity_Fire_Blue.
						spawn(247360, 240.99176f, 1262.8556f, 242.58624f, (byte) 0, 199); //IDInfinity_Fire_Blue.
						spawn(247291, 241.41281f, 1249.5133f, 240.63419f, (byte) 60); //IDInfinity_Normal_30_01.
					break;
					case 2:
				        spawn(701692, 240.99178f, 1236.1763f, 238.74855f, (byte) 0, 186); //IDInfinity_Pot.
						spawn(701692, 240.99176f, 1262.8556f, 238.74855f, (byte) 0, 196); //IDInfinity_Pot.
						spawn(247359, 240.99178f, 1236.1763f, 242.58624f, (byte) 0, 197); //IDInfinity_Fire_Red.
						spawn(247359, 240.99176f, 1262.8556f, 242.58624f, (byte) 0, 198); //IDInfinity_Fire_Red.
						spawn(247292, 241.41281f, 1249.5133f, 240.63419f, (byte) 60); //IDInfinity_Normal_30_02.
					break;
				}
			break;
			case 31:
			    spawn(701692, 240.99178f, 1236.1763f, 238.74855f, (byte) 0, 186); //IDInfinity_Pot.
				spawn(247359, 240.99178f, 1236.1763f, 242.58624f, (byte) 0, 197); //IDInfinity_Fire_Red.
			    spawn(701692, 240.99176f, 1262.8556f, 238.74855f, (byte) 0, 196); //IDInfinity_Pot.
				spawn(247360, 240.99176f, 1262.8556f, 242.58624f, (byte) 0, 199); //IDInfinity_Fire_Blue.
				spawn(247293, 259.91907f, 1254.8942f, 240.71162f, (byte) 62); //IDInfinity_Normal_31_01.
				spawn(247294, 260.09055f, 1244.4081f, 240.71162f, (byte) 61); //IDInfinity_Normal_31_02.
			break;
			case 32:
			    spawn(701692, 240.99178f, 1236.1763f, 238.74855f, (byte) 0, 186); //IDInfinity_Pot.
				spawn(247359, 240.99178f, 1236.1763f, 242.58624f, (byte) 0, 197); //IDInfinity_Fire_Red.
			    spawn(701692, 240.99176f, 1262.8556f, 238.74855f, (byte) 0, 196); //IDInfinity_Pot.
				spawn(247360, 240.99176f, 1262.8556f, 242.58624f, (byte) 0, 199); //IDInfinity_Fire_Blue.
				spawn(247243, 241.41281f, 1249.5133f, 240.63419f, (byte) 60); //IDInfinity_Named_32.
			break;
			case 33:
			    switch (Rnd.get(1, 2)) {
				    case 1:
				        spawn(247295, 257.12485f, 1258.1670f, 240.71162f, (byte) 70); //IDInfinity_Normal_33_01.
						spawn(247295, 257.45935f, 1240.7285f, 240.71162f, (byte) 49); //IDInfinity_Normal_33_01.
						spawn(247295, 245.13070f, 1245.4384f, 240.63419f, (byte) 60); //IDInfinity_Normal_33_01.
                        spawn(247295, 245.39058f, 1253.8103f, 240.63419f, (byte) 60); //IDInfinity_Normal_33_01.
					break;
					case 2:
				        spawn(247296, 257.12485f, 1258.1670f, 240.71162f, (byte) 70); //IDInfinity_Normal_33_02.
						spawn(247296, 257.45935f, 1240.7285f, 240.71162f, (byte) 49); //IDInfinity_Normal_33_02.
						spawn(247296, 245.13070f, 1245.4384f, 240.63419f, (byte) 60); //IDInfinity_Normal_33_02.
                        spawn(247296, 245.39058f, 1253.8103f, 240.63419f, (byte) 60); //IDInfinity_Normal_33_02.
					break;
				}
			break;
			case 34:
			    spawn(247297, 236.40814f, 1244.7013f, 240.63419f, (byte) 60); //IDInfinity_Normal_34_01.
                spawn(247297, 236.40082f, 1254.6990f, 240.63419f, (byte) 60); //IDInfinity_Normal_34_01.
				spawn(247298, 241.12361f, 1249.5305f, 240.63419f, (byte) 60); //IDInfinity_Normal_34_02.
                spawn(247298, 248.58983f, 1257.3235f, 240.63419f, (byte) 60); //IDInfinity_Normal_34_02.
                spawn(247298, 248.41464f, 1242.6595f, 240.63419f, (byte) 59); //IDInfinity_Normal_34_02.
			break;
			case 35:
			    spawn(247299, 241.43329f, 1249.5488f, 240.63419f, (byte) 60); //IDInfinity_Normal_35_01.
                spawn(247299, 241.20960f, 1257.1047f, 240.63419f, (byte) 66); //IDInfinity_Normal_35_01.
                spawn(247299, 241.27136f, 1241.9293f, 240.63419f, (byte) 55); //IDInfinity_Normal_35_01.
				spawn(247300, 253.67844f, 1262.4369f, 240.71162f, (byte) 75); //IDInfinity_Normal_35_02.
				spawn(247300, 259.53360f, 1249.4660f, 240.71162f, (byte) 59); //IDInfinity_Normal_35_02.
                spawn(247300, 254.33441f, 1236.7448f, 240.71162f, (byte) 44); //IDInfinity_Normal_35_02.
			break;
			case 36:
			    spawn(247244, 241.41281f, 1249.5133f, 240.63419f, (byte) 60); //IDInfinity_Named_36.
				spawn(247373, 246.26080f, 1254.5687f, 240.63419f, (byte) 59); //IDInfinity_Named_36_Summon_10.
                spawn(247373, 246.05994f, 1244.3612f, 240.63419f, (byte) 57); //IDInfinity_Named_36_Summon_10.
			break;
			case 37:
			    spawn(247301, 246.11182f, 1254.5605f, 240.63419f, (byte) 59); //IDInfinity_Normal_37_01.
                spawn(247301, 246.02896f, 1244.5856f, 240.63419f, (byte) 59); //IDInfinity_Normal_37_01.
				spawn(247302, 241.12560f, 1247.9340f, 240.63419f, (byte) 59); //IDInfinity_Normal_37_02.
                spawn(247302, 241.15268f, 1251.1821f, 240.63419f, (byte) 59); //IDInfinity_Normal_37_02.
			break;
			case 38:
			    spawn(247303, 241.44724f, 1249.6206f, 240.63419f, (byte) 60); //IDInfinity_Normal_38_01.
                spawn(247303, 254.86494f, 1242.2816f, 240.63419f, (byte) 50); //IDInfinity_Normal_38_01.
                spawn(247303, 254.50504f, 1256.7817f, 240.63419f, (byte) 70); //IDInfinity_Normal_38_01.
				spawn(247304, 241.28506f, 1260.7113f, 240.63419f, (byte) 61); //IDInfinity_Normal_38_02.
                spawn(247304, 241.32637f, 1238.9640f, 240.63419f, (byte) 61); //IDInfinity_Normal_38_02.
			break;
			case 39:
			    spawn(247305, 246.26220f, 1244.2563f, 240.63419f, (byte) 60); //IDInfinity_Normal_39_01.
                spawn(247305, 246.33160f, 1254.7949f, 240.63419f, (byte) 62); //IDInfinity_Normal_39_01.
				spawn(247306, 241.41690f, 1249.6360f, 240.63419f, (byte) 59); //IDInfinity_Normal_39_02.
			break;
			case 40:
				spawn(247245, 241.05147f, 249.50418f, 971.14140f, (byte) 60); //마녀 그렌달.
			break;
		}
	}
	
	private void despawnNpc(int npcId) {
		if (getPosition().getWorldMapInstance().getNpcs(npcId) != null) {
			List<Npc> npcs = getPosition().getWorldMapInstance().getNpcs(npcId);
			for (Npc npc: npcs) {
				npc.getController().onDelete();
			}
		}
	}
}