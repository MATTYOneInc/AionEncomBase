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
package ai.instance.empyreanCrucible;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.instance.handlers.InstanceHandler;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.instance.StageType;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.services.NpcShoutsService;
import com.aionemu.gameserver.utils.PacketSendUtility;

/****/
/** Author (Encom)
/****/

@AIName("empyrean_record_keeper")
public class Empyrean_Record_KeeperAI2 extends NpcAI2
{
	@Override
	protected void handleDialogStart(Player player) {
		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1011));
	}
	
	@Override
	public boolean onDialogSelect(Player player, int dialogId, int questId, int extendedRewardIndex) {
		InstanceHandler instanceHandler = getPosition().getWorldMapInstance().getInstanceHandler();
		if (dialogId == 10000) {
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 10));
			switch (getNpcId()) {
				case 799568:
					instanceHandler.onChangeStage(StageType.START_STAGE_2_ELEVATOR);
				break;
				case 799569:
					instanceHandler.onChangeStage(StageType.START_STAGE_3_ELEVATOR);
				break;
				case 205331:
					instanceHandler.onChangeStage(StageType.START_STAGE_4_ELEVATOR);
				break;
				case 205338:
					instanceHandler.onChangeStage(StageType.START_STAGE_5);
				break;
				case 205332:
					switch (Rnd.get(1, 2)) {
					    case 1:
						    instanceHandler.onChangeStage(StageType.START_AZOTURAN_STAGE_5_ROUND_1);
					    break;
					    case 2:
						    instanceHandler.onChangeStage(StageType.START_STEEL_RAKE_STAGE_5_ROUND_1);
					    break;
					}
				break;
				case 205339:
					instanceHandler.onChangeStage(StageType.START_STAGE_6);
				break;
				case 205333:
					instanceHandler.onChangeStage(StageType.START_STAGE_6_ROUND_1);
				break;
				case 205340:
					instanceHandler.onChangeStage(StageType.START_STAGE_7);
				break;
				case 205334:
					instanceHandler.onChangeStage(StageType.START_STAGE_7_ROUND_1);
					switch (player.getRace()) {
					    case ELYOS:
						    spawn(217582, 1783.0873f, 796.8426f, 469.35013f, (byte) 0);
					    break;
					    case ASMODIANS:
						    spawn(217578, 1783.0873f, 796.8426f, 469.35013f, (byte) 0);
					    break;
					}
				break;
				case 205341:
					instanceHandler.onChangeStage(StageType.START_STAGE_8);
				break;
				case 205335:
					instanceHandler.onChangeStage(StageType.START_STAGE_8_ROUND_1);
				break;
				case 205342:
					instanceHandler.onChangeStage(StageType.START_STAGE_9);
				break;
				case 205336:
					instanceHandler.onChangeStage(StageType.START_STAGE_9_ROUND_1);
				break;
				case 205343:
					instanceHandler.onChangeStage(StageType.START_STAGE_10);
				break;
				case 205337:
					instanceHandler.onChangeStage(StageType.START_STAGE_10_ROUND_1);
				break;
			}
		}
		AI2Actions.deleteOwner(this);
		return true;
	}
	
	@Override
    protected void handleSpawned() {
		switch (getNpcId()) {
			case 799568:
			    //You have completed Stage 1, nyerk.
				sendMsg(1111460, getObjectId(), false, 2000);
				//I hope you got yourself a Worthiness Ticket, nyerk.
				sendMsg(1111451, getObjectId(), false, 6000);
			break;
			case 799569:
			    //You have completed Stage 2, nyerk.
				sendMsg(1111461, getObjectId(), false, 2000);
				//Stage 3 begins, nyerk!
				sendMsg(1111452, getObjectId(), false, 6000);
			break;
			case 205331:
			    //You have completed Stage 3, nyerk.
				sendMsg(1111462, getObjectId(), false, 2000);
				//Hope you are ready, because Stage 4 is about to begin!
				sendMsg(1111453, getObjectId(), false, 6000);
			break;
			case 205332:
				//Good progress. Let me know when you want to begin Stage 5!
				sendMsg(1111454, getObjectId(), false, 2000);
			break;
			case 205333:
				//Five down! Stage 6 about to begin!
				sendMsg(1111455, getObjectId(), false, 2000);
			break;
			case 205334:
				//Focus. Stage 7 will be more difficult!
				sendMsg(1111456, getObjectId(), false, 2000);
			break;
			case 205335:
				//Stage 8 ready for you! Are you ready for it?
				sendMsg(1111457, getObjectId(), false, 2000);
			break;
			case 205336:
				//Stay sharp. Tell me when you're ready for Stage 9, nyerk.
				sendMsg(1111458, getObjectId(), false, 2000);
			break;
			case 205337:
				//Are you ready for final Stage 10?
				sendMsg(1111459, getObjectId(), false, 2000);
			break;
			case 205338:
			    //You have completed Stage 4, nyerk.
				sendMsg(1111463, getObjectId(), false, 2000);
			break;
			case 205339:
			    //Congratulations, you have passed Stage 5!
				sendMsg(1111464, getObjectId(), false, 2000);
			break;
			case 205340:
			    //Congratulations, you have passed Stage 6!
				sendMsg(1111465, getObjectId(), false, 2000);
			break;
			case 205341:
			    //Congratulations, you have passed Stage 7!
				sendMsg(1111466, getObjectId(), false, 2000);
			break;
			case 205342:
			    //Great! You passed Stage 8!
				sendMsg(1111467, getObjectId(), false, 2000);
			break;
			case 205343:
				//Excellent! You passed Stage 9!
				sendMsg(1111468, getObjectId(), false, 2000);
			break;
		}
		super.handleSpawned();
    }
	
	private void sendMsg(int msg, int Obj, boolean isShout, int time) {
		NpcShoutsService.getInstance().sendMsg(getPosition().getWorldMapInstance(), msg, Obj, isShout, 0, time);
	}
}