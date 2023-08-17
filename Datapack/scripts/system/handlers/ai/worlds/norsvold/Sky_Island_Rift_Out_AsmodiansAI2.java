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
package ai.worlds.norsvold;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.controllers.observer.ItemUseObserver;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_USE_OBJECT;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;

/****/
/** Author (Encom)
/****/

@AIName("sky_island_rift_out_A")
public class Sky_Island_Rift_Out_AsmodiansAI2 extends NpcAI2
{
	protected int startBarAnimation = 1;
	protected int cancelBarAnimation = 2;
	
	@Override
	protected void handleDialogStart(Player player) {
		handleUseItemStart(player);
	}
	
	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		switch (getNpcId()) {
			case 805872: //Feather Bough Forest Scout Post.
            case 805873: //Territory Of Spiritus Scout Post.
			case 805874: //Kalidag Canyon Scout Post.
            case 805875: //Blue Illusion Forest Scout Post.
			case 805876: //Ruins Of Lost Time Scout Post.
			case 805877: //Black Mane Mountains Scout Post.
			case 805878: //Canyon Of Lost Souls.
			case 805879: //Plateau Of Aetheric Gales.
			case 805880: //Aetherspring Lake.
			case 805881: //Nightbloom Forest.
				startLifeTask();
			break;
        }
	}
	
	private void startLifeTask() {
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				AI2Actions.deleteOwner(Sky_Island_Rift_Out_AsmodiansAI2.this);
			}
		}, 3600000); //1Hrs.
	}
	
	protected void handleUseItemStart(final Player player) {
		final int delay = getTalkDelay();
		if (delay != 0) {
			final ItemUseObserver observer = new ItemUseObserver() {
				@Override
				public void abort() {
					player.getController().cancelTask(TaskId.ACTION_ITEM_NPC);
					PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.END_QUESTLOOT, 0, getObjectId()), true);
					PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), getObjectId(), 0, cancelBarAnimation));
					player.getObserveController().removeObserver(this);
				}
			};
			player.getObserveController().attach(observer);
			PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), getObjectId(), getTalkDelay(), startBarAnimation));
			PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_QUESTLOOT, 0, getObjectId()), true);
			player.getController().addTask(TaskId.ACTION_ITEM_NPC, ThreadPoolManager.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.END_QUESTLOOT, 0, getObjectId()), true);
					PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), getObjectId(), getTalkDelay(), cancelBarAnimation));
					player.getObserveController().removeObserver(observer);
					handleUseItemFinish(player);
				}
			}, delay));
		} else {
			handleUseItemFinish(player);
		}
	}
	
    protected void handleUseItemFinish(Player player) {
        switch (getNpcId()) {
			case 805872: //Feather Bough Forest Scout Post.
                TeleportService2.teleportTo(player, 220110000, 2053.476f, 842.8735f, 268.5f, (byte) 114);
            break;
            case 805873: //Territory Of Spiritus Scout Post.
                TeleportService2.teleportTo(player, 220110000, 2619.7324f, 594.37354f, 238.17648f, (byte) 57);
            break;
			case 805874: //Kalidag Canyon Scout Post.
                TeleportService2.teleportTo(player, 220110000, 2045.9015f, 2641.2766f, 317.80136f, (byte) 115);
            break;
            case 805875: //Blue Illusion Forest Scout Post.
                TeleportService2.teleportTo(player, 220110000, 850.34265f, 2576.774f, 227.02272f, (byte) 28);
            break;
			case 805876: //Ruins Of Lost Time Scout Post.
                TeleportService2.teleportTo(player, 220110000, 306.48965f, 2216.8728f, 243.47606f, (byte) 6);
            break;
			case 805877: //Black Mane Mountains Scout Post.
                TeleportService2.teleportTo(player, 220110000, 437.31454f, 1011.05194f, 296.10208f, (byte) 4);
            break;
			case 805878: //Canyon Of Lost Souls.
                TeleportService2.teleportTo(player, 220110000, 2194.9136f, 1848.3369f, 237.04684f, (byte) 4);
            break;
			case 805879: //Plateau Of Aetheric Gales.
                TeleportService2.teleportTo(player, 220110000, 1085.9058f, 2599.513f, 232.65544f, (byte) 9);
            break;
			case 805880: //Aetherspring Lake.
                TeleportService2.teleportTo(player, 220110000, 998.65436f, 1381.0592f, 287.3571f, (byte) 62);
            break;
			case 805881: //Nightbloom Forest.
                TeleportService2.teleportTo(player, 220110000, 1446.3533f, 960.66296f, 226.66644f, (byte) 9);
            break;
        }
    }
	
	protected int getTalkDelay() {
		return getObjectTemplate().getTalkDelay() * 1000;
	}
	
	@Override
	public boolean isMoveSupported() {
		return false;
	}
}