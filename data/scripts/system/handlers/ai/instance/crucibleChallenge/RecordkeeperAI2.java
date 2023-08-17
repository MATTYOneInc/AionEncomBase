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
package ai.instance.crucibleChallenge;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.instance.handlers.InstanceHandler;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.instance.StageType;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.services.NpcShoutsService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;

import java.util.concurrent.atomic.AtomicBoolean;

/****/
/** Author (Encom)
/****/

@AIName("recordkeeper")
public class RecordkeeperAI2 extends NpcAI2
{
	private AtomicBoolean startedEvent = new AtomicBoolean(false);
	
	@Override
	protected void handleDialogStart(Player player) {
		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1011));
	}
	
	@Override
	public boolean onDialogSelect(Player player, int dialogId, int questId, int extendedRewardIndex) {
		int instanceId = getPosition().getInstanceId();
		InstanceHandler instanceHandler = getPosition().getWorldMapInstance().getInstanceHandler();
		if (dialogId == 10000) {
			switch (getNpcId()) {
				case 205668:
					instanceHandler.onChangeStage(StageType.START_STAGE_1_ROUND_1);
				break;
				case 205674:
					TeleportService2.teleportTo(player, 300320000, instanceId, 1796.5513f, 306.9967f, 469.25f, (byte) 60);
					spawn(205683, 1821.5643f, 311.92484f, 469.4562f, (byte) 60);
					spawn(205669, 1784.4633f, 306.98645f, 469.25f, (byte) 0);
				break;
				case 205669:
					instanceHandler.onChangeStage(StageType.START_STAGE_2_ROUND_1);
				break; 
				case 205675:
					TeleportService2.teleportTo(player, 300320000, instanceId, 1324.433f, 1738.2279f, 316.476f, (byte) 70);
					spawn(205684, 1358.4021f, 1758.744f, 319.1873f, (byte) 70);
					spawn(205670, 1307.5472f, 1732.9865f, 316.0777f, (byte) 6);
				break;
				case 205670:
					instanceHandler.onChangeStage(StageType.START_STAGE_3_ROUND_1);
				break;
				case 205676:
					switch (Rnd.get(1, 2)) {
						case 1:
							TeleportService2.teleportTo(player, 300320000, instanceId, 1283.1246f, 791.6683f, 436.6403f, (byte) 60);
							spawn(205685, 1308.9664f, 796.20276f, 437.29678f, (byte) 60);
							spawn(205671, 1271.4222f, 791.36145f, 436.64017f, (byte) 0);
						break;
						case 2:
							TeleportService2.teleportTo(player, 300320000, instanceId, 1270.8877f, 237.93307f, 405.38028f, (byte) 60);
							spawn(205663, 1295.7217f, 242.15009f, 406.03677f, (byte) 60);
							spawn(205666, 1258.7214f, 237.85518f, 405.3968f, (byte) 0);
						break;
					}
				break;
				case 205666:
					instanceHandler.onChangeStage(StageType.START_KROMEDE_STAGE_4_ROUND_1);
				break;
				case 205671:
					instanceHandler.onChangeStage(StageType.START_HARAMEL_STAGE_4_ROUND_1);
				break;
				case 205667:
				case 205677:
					TeleportService2.teleportTo(player, 300320000, instanceId, 357.98798f, 349.19116f, 96.09108f, (byte) 60);
					spawn(205686, 383.30933f, 354.07846f, 96.07846f, (byte) 60);
					spawn(205672, 346.52298f, 349.25586f, 96.0098f, (byte) 0);
				break;
				case 205672:
					instanceHandler.onChangeStage(StageType.START_STAGE_5_ROUND_1);
				break;
				case 205678:
					TeleportService2.teleportTo(player, 300320000, instanceId, 1759.5004f, 1273.5414f, 389.11743f, (byte) 10);
					spawn(205687, 1747.3901f, 1250.201f, 389.11765f, (byte) 16);
					spawn(205673, 1767.1036f, 1288.4425f, 389.11728f, (byte) 76);
				break;
				case 205673:
					instanceHandler.onChangeStage(StageType.START_STAGE_6_ROUND_1);
				break;
				case 205679:
					getPosition().getWorldMapInstance().getInstanceHandler().doReward(player);
				break;
			} if (getNpcId() != 205679) {
				AI2Actions.deleteOwner(this);
			}
		}
		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 0));
		return true;
	}
	
	@Override
	protected void handleCreatureMoved(Creature creature) {
		if (creature instanceof Player) {
			final Player player = (Player) creature;
			if (MathUtil.getDistance(getOwner(), player) <= 30) {
				if (startedEvent.compareAndSet(false, true)) {
					switch (getNpcId()) {
						case 205668:
						    //Quick lollygagging! Come over here if you want to start Stage 1.
						    sendMsg(1111470, getObjectId(), false, 2000);
						break;
						case 205669:
						    //Get ready! It's time for Stage 2 to start, nyerk!
						    sendMsg(1111471, getObjectId(), false, 2000);
						break;
						case 205670:
						    //Now, prepare for Stage 3, nyerk.
						    sendMsg(1111472, getObjectId(), false, 2000);
						break;
						case 205666:
						case 205671:
						    //Take a deep breath now... it's time for Stage 4 to begin.
						    sendMsg(1111473, getObjectId(), false, 2000);
						break;
						case 205672:
						    //When you've recovered your composure, let me know and you can move on to Stage 5, nyerk.
						    sendMsg(1111474, getObjectId(), false, 2000);
						break;
						case 205673:
						    //Are you ready? Shall I start up Stage 6?
						    sendMsg(1111475, getObjectId(), false, 2000);
						break;
						case 205674:
						    //You have completed Stage 1, nyerk.
						    sendMsg(1111476, getObjectId(), false, 2000);
						break;
						case 205675:
						    //You have completed Stage 2, nyerk.
						    sendMsg(1111477, getObjectId(), false, 2000);
						break;
						case 205676:
						    //You have completed Stage 3, nyerk.
						    sendMsg(1111478, getObjectId(), false, 2000);
						break;
						case 205667:
						case 205677:
						    //You have completed Stage 4, nyerk.
						    sendMsg(1111479, getObjectId(), false, 2000);
						break;
						case 205678:
						    //Congratulations. You have passed Stage 5!
						    sendMsg(1111480, getObjectId(), false, 2000);
						break;
						case 205679:
						    //Congratulations! You have completed Stage 6, nyerk!
						    sendMsg(1111481, getObjectId(), false, 2000);
						break;
					}
				}
			}
		}
	}
	
	private void sendMsg(int msg, int Obj, boolean isShout, int time) {
		NpcShoutsService.getInstance().sendMsg(getPosition().getWorldMapInstance(), msg, Obj, isShout, 0, time);
	}
}