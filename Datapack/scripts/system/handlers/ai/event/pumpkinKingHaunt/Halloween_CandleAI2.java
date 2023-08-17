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
package ai.event.pumpkinKingHaunt;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.controllers.observer.ItemUseObserver;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.NpcType;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_CUSTOM_SETTINGS;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_USE_OBJECT;
import com.aionemu.gameserver.utils.PacketSendUtility;

import javolution.util.FastMap;
import java.util.List;

/****/
/** Author (Encom)
/****/

@AIName("Halloween_Candle")
public class Halloween_CandleAI2 extends NpcAI2
{
	protected int startBarAnimation = 1;
	protected int cancelBarAnimation = 2;
	
	@Override
	protected void handleDialogStart(Player player) {
		handleUseItemStart(player);
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
	
	protected void handleUseItemFinish(final Player player) {
		switch (getNpcId()) {
		    case 835619: //World_event_halloween_candle_01.
				spawn(835624, getOwner().getX(), getOwner().getY(), getOwner().getZ(), (byte) 0); //Candle Flame.
				ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						getOwner().setNpcType(NpcType.INVULNERABLE);
						PacketSendUtility.sendPacket(player, new SM_CUSTOM_SETTINGS(getOwner().getObjectId(), 0, NpcType.INVULNERABLE.getId(), 0));
					}
				}, 1000);
				ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						despawnNpc(835624); //Candle Flame.
					}
				}, 300000); //...5Min
				ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						getOwner().setNpcType(NpcType.NON_ATTACKABLE);
						PacketSendUtility.sendPacket(player, new SM_CUSTOM_SETTINGS(getOwner().getObjectId(), 0, NpcType.NON_ATTACKABLE.getId(), 0));
					}
				}, 300000); //...1Hr 3600000
			break;
			case 835620: //World_event_halloween_candle_02.
				spawn(835625, getOwner().getX(), getOwner().getY(), getOwner().getZ(), (byte) 0); //Candle Flame.
				ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						getOwner().setNpcType(NpcType.INVULNERABLE);
						PacketSendUtility.sendPacket(player, new SM_CUSTOM_SETTINGS(getOwner().getObjectId(), 0, NpcType.INVULNERABLE.getId(), 0));
					}
				}, 1000);
				ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						despawnNpc(835625); //Candle Flame.
					}
				}, 300000); //...5Min
				ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						getOwner().setNpcType(NpcType.NON_ATTACKABLE);
						PacketSendUtility.sendPacket(player, new SM_CUSTOM_SETTINGS(getOwner().getObjectId(), 0, NpcType.NON_ATTACKABLE.getId(), 0));
					}
				}, 300000); //...1Hr
			break;
			case 835621: //World_event_halloween_candle_03.
				spawn(835626, getOwner().getX(), getOwner().getY(), getOwner().getZ(), (byte) 0); //Candle Flame.
				ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						getOwner().setNpcType(NpcType.INVULNERABLE);
						PacketSendUtility.sendPacket(player, new SM_CUSTOM_SETTINGS(getOwner().getObjectId(), 0, NpcType.INVULNERABLE.getId(), 0));
					}
				}, 1000);
				ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						despawnNpc(835626); //Candle Flame.
					}
				}, 300000); //...5Min
				ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						getOwner().setNpcType(NpcType.NON_ATTACKABLE);
						PacketSendUtility.sendPacket(player, new SM_CUSTOM_SETTINGS(getOwner().getObjectId(), 0, NpcType.NON_ATTACKABLE.getId(), 0));
					}
				}, 300000); //...1Hr
			break;
			case 835622: //World_event_halloween_candle_04.
				spawn(835627, getOwner().getX(), getOwner().getY(), getOwner().getZ(), (byte) 0); //Candle Flame.
				ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						getOwner().setNpcType(NpcType.INVULNERABLE);
						PacketSendUtility.sendPacket(player, new SM_CUSTOM_SETTINGS(getOwner().getObjectId(), 0, NpcType.INVULNERABLE.getId(), 0));
					}
				}, 1000);
				ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						despawnNpc(835627); //Candle Flame.
					}
				}, 300000); //...5Min
				ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						getOwner().setNpcType(NpcType.NON_ATTACKABLE);
						PacketSendUtility.sendPacket(player, new SM_CUSTOM_SETTINGS(getOwner().getObjectId(), 0, NpcType.NON_ATTACKABLE.getId(), 0));
					}
				}, 300000); //...1Hr
			break;
			case 835623: //World_event_halloween_candle_05.
				spawn(835628, getOwner().getX(), getOwner().getY(), getOwner().getZ(), (byte) 0); //Candle Flame.
				ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						getOwner().setNpcType(NpcType.INVULNERABLE);
						PacketSendUtility.sendPacket(player, new SM_CUSTOM_SETTINGS(getOwner().getObjectId(), 0, NpcType.INVULNERABLE.getId(), 0));
					}
				}, 1000);
				ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						despawnNpc(835628); //Candle Flame.
					}
				}, 300000); //...5Min
				ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						getOwner().setNpcType(NpcType.NON_ATTACKABLE);
						PacketSendUtility.sendPacket(player, new SM_CUSTOM_SETTINGS(getOwner().getObjectId(), 0, NpcType.NON_ATTACKABLE.getId(), 0));
					}
				}, 300000); //...1Hr
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
	
	protected int getTalkDelay() {
		return getObjectTemplate().getTalkDelay() * 1000;
	}
	
	@Override
	public boolean isMoveSupported() {
		return false;
	}
}