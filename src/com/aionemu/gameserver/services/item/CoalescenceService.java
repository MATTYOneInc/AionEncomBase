/*
 * This file is part of Encom. **ENCOM FUCK OTHER SVN**
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
package com.aionemu.gameserver.services.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.controllers.observer.ItemUseObserver;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_COALESCENCE_RESULT;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.item.ItemPacketService.ItemDeleteType;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.utils.audit.AuditLogger;

/**
 * @author Ranastic
 */

public class CoalescenceService
{
	private Logger log = LoggerFactory.getLogger(CoalescenceService.class);
	
	public void letsCoalescence(final Player player, int core_item_object_id, final List<Integer> material_item_object_id_collection) {
		final Item core_item = player.getInventory().getItemByObjId(core_item_object_id);
		if (core_item.getEnchantLevel() == 25) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANT_ENCHANT_ITEM);
			return;
		} if (material_item_object_id_collection.size() == 0) {
			AuditLogger.info(player.getName(), player.getObjectId(), "Possible hack Coalescence. His material equals 0");
			return;
		}
		PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), core_item.getObjectId(), core_item.getItemId(), 4000, 23, 68), true);
		final ItemUseObserver observer = new ItemUseObserver() {
            @Override
            public void abort() {
                player.getController().cancelTask(TaskId.ITEM_USE);
                player.removeItemCoolDown(core_item.getItemTemplate().getUseLimits().getDelayId());
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_ITEM_CANCELED(new DescriptionId(core_item.getItemTemplate().getNameId())));
                PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), core_item.getObjectId(), core_item.getItemId(), 0, 2, 0), true);
                player.getObserveController().removeObserver(this);
            }
        };
		player.getObserveController().attach(observer);
		player.getController().addTask(TaskId.ITEM_USE, ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				player.getObserveController().removeObserver(observer);
				player.getInventory().delete(core_item, ItemDeleteType.COALESCENCE);
				for (int i=0;i<material_item_object_id_collection.size();i++) {
					final Item mats = player.getInventory().getItemByObjId(material_item_object_id_collection.get(i));
					player.getInventory().delete(mats, ItemDeleteType.COALESCENCE);
				}
				List<Integer> ids_collections = new ArrayList<Integer>();
				int item_id_taken = 0;
				int bonus_item_id_taken = 0;
				int bonus_item_count = 0;
				Map<Integer, ItemTemplate> item_templates = DataManager.ITEM_DATA.getAllItems();
				for (ItemTemplate item_template : item_templates.values()) { 
					if (item_template.isArchdaeva() && item_template.getEquipmentType() == core_item.getEquipmentType() && (item_template.getLevel() >= 66 && item_template.getLevel() <= 74) && !item_template.getName().contains("n_m3_") && !item_template.getName().contains("npc_") && !item_template.getName().contains("Pvp_") && !item_template.getName().contains("dagger_") && !item_template.getName().contains("polearm_d_") && !item_template.getName().contains("polearm_a_") && !item_template.getName().contains("polearm_")) {
						ids_collections.add(item_template.getTemplateId());
					}
				}
				Collections.shuffle(ids_collections);
				item_id_taken = ids_collections.get(0);
				if (item_id_taken == 0) {
					return;
				}
				ItemService.addItem(player, item_id_taken, 1);
				//player.getInventory().decreaseKinah(amount);//TODO
				float success = 15;
				if (material_item_object_id_collection.size() == 1) {
					success += 5;
				} else if (material_item_object_id_collection.size() == 2) {
					success += 10;
				} else if (material_item_object_id_collection.size() == 3) {
					success += 15;
				} else if (material_item_object_id_collection.size() == 4) {
					success += 20;
				} else if (material_item_object_id_collection.size() == 5) {
					success += 25;
				} else if (material_item_object_id_collection.size() == 6) {
					success += 30;
				} if (success >= 95) {
					success = 95;
				}
				boolean result_of_random = false;
				float random = Rnd.get(1, 1000) / 10f;
				if (random <= success) {
					result_of_random = true;
					Random rand = new Random();
					int[] bonus_item_id_collection = new int[] { 
					    166100009, 166100010, 166100011
					};
					bonus_item_id_taken = bonus_item_id_collection[rand.nextInt(bonus_item_id_collection.length)];
					bonus_item_count = Rnd.get(1, 200);
					ItemService.addItem(player, bonus_item_id_taken, bonus_item_count);
					//TODO message for bonus
				}
				PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), core_item.getObjectId(), core_item.getItemId(), 0, 24, 0), true);
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1403620, new DescriptionId(core_item.getItemTemplate().getNameId())));
				PacketSendUtility.sendPacket(player, new SM_COALESCENCE_RESULT(core_item.getItemId(), core_item.getObjectId(), bonus_item_id_taken, bonus_item_count, result_of_random));
			}
		}, 4000));
	}
	
	public static CoalescenceService getInstance() {
		return NewSingletonHolder.INSTANCE;
	}
	
	private static class NewSingletonHolder {
		private static final CoalescenceService INSTANCE = new CoalescenceService();
	}
}