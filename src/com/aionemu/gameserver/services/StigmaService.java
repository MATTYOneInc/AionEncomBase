/*

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
package com.aionemu.gameserver.services;

import com.aionemu.gameserver.configs.main.MembershipConfig;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.ItemSlot;
import com.aionemu.gameserver.model.skill.PlayerSkillEntry;
import com.aionemu.gameserver.model.templates.item.RequireSkill;
import com.aionemu.gameserver.model.templates.item.Stigma;
import com.aionemu.gameserver.network.aion.serverpackets.SM_CUBE_UPDATE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INVENTORY_UPDATE_ITEM;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SKILL_LIST;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.item.ItemPacketService;
import com.aionemu.gameserver.skillengine.model.SkillLearnTemplate;
import com.aionemu.gameserver.skillengine.model.SkillTemplate;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.audit.AuditLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Wnkrz (Encom)
 */

public class StigmaService
{
    private static final Logger log = LoggerFactory.getLogger(StigmaService.class);
	
    private static int getPriceByQuality(Item item) {
        int price = 0;
        switch (item.getItemTemplate().getItemQuality()) {
            case RARE:
                price = 35312;
            break;
            case LEGEND:
                price = 70625;
            break;
            case UNIQUE:
                price = 141250;
            break;
            default:
            break;
        }
        return price;
    }
	
    public static boolean notifyEquipAction(final Player player, Item resultItem, long slot) {
        int itemId = resultItem.getItemId();
        if (resultItem.getItemTemplate().isStigma()) {
            if (ItemSlot.isRegularStigma(slot)) {
                if (getPossibleStigmaCount(player) <= player.getEquipment().getEquippedItemsRegularStigma().size()) {
                    AuditLogger.info(player, "Possible client hack stigma count big :O");
                    return false;
                }
            } if (!resultItem.getItemTemplate().isClassSpecific(player.getCommonData().getPlayerClass())) {
                AuditLogger.info(player, "Possible client hack not valid for class.");
                return false;
            }
            Stigma stigmaInfo = resultItem.getItemTemplate().getStigma();
            if (stigmaInfo == null) {
                log.warn("Stigma info missing for item: " + resultItem.getItemTemplate().getTemplateId());
                return false;
            } if (player.getInventory().getKinah() < getPriceByQuality(resultItem)) {
                PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_STIGMA_NOT_ENOUGH_MONEY);
                return false;
            } else {
                player.getInventory().decreaseKinah(getPriceByQuality(resultItem));
            } for (int i = 1; i <= player.getLevel(); i++) {
                SkillLearnTemplate[] skillTemplates = DataManager.SKILL_TREE_DATA.getTemplatesFor(player.getPlayerClass(), i, player.getRace());
                for (SkillLearnTemplate skillTree : skillTemplates) {
                    if (resultItem.getSkillGroup().equals(skillTree.getSkillGroup())) {
                        //PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300401, new DescriptionId(resultItem.getNameId()), skillTree.getSkillLevel() + resultItem.getEnchantLevel()));
                        player.getSkillList().addStigmaSkill(player, skillTree.getSkillId(), skillTree.getSkillLevel() + resultItem.getEnchantLevel());
                        PacketSendUtility.sendPacket(player, new SM_SKILL_LIST(player, player.getSkillList().getStigmaSkills()));
                    }
                }
            }
            List<Integer> sStigma = player.getEquipment().getEquippedItemsAllStigmaIds();
            sStigma.add(resultItem.getItemId());
            StigmaLinkedService.checkEquipConditions(player, sStigma);
            checkStigmaEnchant(player, sStigma);
            if (player.getStigmaSet() != 0) {
                addStigmaSetEnchant(player, resultItem.getEnchantLevel());
            }
        }
        return true;
    }
	
    public static boolean notifyUnequipAction(Player player, Item resultItem) {
        if (player.getEquipment().isSlotEquipped(ItemSlot.STIGMA_SPECIAL.getSlotIdMask()) && resultItem.getEquipmentSlot() != ItemSlot.STIGMA_SPECIAL.getSlotIdMask()) {
            return false;
        } if (player.getStigmaSet() != 0 && player.getEquipment().getEquippedItemsAllStigmaIds().size() == 6) {
            removeStigmaSetEnchant(player);
        } if (resultItem.getItemTemplate().isStigma()) {
            int itemId = resultItem.getItemId();
            PacketSendUtility.sendPacket(player, SM_CUBE_UPDATE.stigmaSlots(player.getCommonData().getAdvancedStigmaSlotSize()));
            PacketSendUtility.sendPacket(player, new SM_INVENTORY_UPDATE_ITEM(player, resultItem));
            for (int i = 1; i <= player.getLevel(); i++) {
                SkillLearnTemplate[] skillTemplates = DataManager.SKILL_TREE_DATA.getTemplatesFor(player.getPlayerClass(), i, player.getRace());
                for (SkillLearnTemplate skillTree : skillTemplates) {
                    if (resultItem.getSkillGroup().equals(skillTree.getSkillGroup())) {
                        //PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300401, new DescriptionId(resultItem.getNameId()), resultItem.getEnchantLevel()));
                        player.getSkillList().addStigmaSkill(player, skillTree.getSkillId(), skillTree.getSkillLevel());
                        PacketSendUtility.sendPacket(player, new SM_SKILL_LIST(player, player.getSkillList().getStigmaSkills()));
                        SkillLearnService.removeSkill(player, skillTree.getSkillId());
                    }
                }
            }
            //PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300403, new DescriptionId(resultItem.getNameId())));
            player.getEquipedStigmaList().remove(player, itemId);
            if (player.getEquipment().getEquippedItemsAllStigma().size() <= 6 && player.getLinkedSkill() != 0) {
                SkillTemplate linked = DataManager.SKILL_DATA.getSkillTemplate(player.getLinkedSkill());
                PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_STIGMA_DELETE_LINKED_SKILL(new DescriptionId(DataManager.SKILL_DATA.getSkillTemplate(linked.getSkillId()).getNameId()), 1));
                StigmaLinkedService.DeleteLinkedSkills(player);
            } if (player.getEquipment().getEquippedItemsAllStigma().size() <= 6 && player.getStigmaSet() != 0) {
                player.setStigmaSet(0);
            }
        }
        return true;
    }
	
    public static void addStigmaSetEnchant(Player player, int enchantLevel) {
        for (PlayerSkillEntry skill : player.getSkillList().getStigmaSkills()) {
            player.getSkillList().addStigmaSkill(player, skill.getSkillId(), 1 + enchantLevel + player.getStigmaSet());
            PacketSendUtility.sendPacket(player, new SM_SKILL_LIST(player, player.getSkillList().getStigmaSkills()));
        }
    }
	
    public static void enchanteLinkedSkill(Player player, int enchantLevel) {
        for (PlayerSkillEntry skill : player.getSkillList().getStigmaSkills()) {
            player.getSkillList().addStigmaSkill(player, skill.getSkillId(), 1 + enchantLevel + player.getStigmaSet());
            PacketSendUtility.sendPacket(player, new SM_SKILL_LIST(player, player.getSkillList().getStigmaSkills()));
        }
    }
	
    public static void removeStigmaSetEnchant(Player player) {
        for (Item resultItem : player.getEquipment().getEquippedItemsAllStigma()) {
            for (int i = 1; i <= player.getLevel(); i++) {
                SkillLearnTemplate[] skillTemplates = DataManager.SKILL_TREE_DATA.getTemplatesFor(player.getPlayerClass(), i, player.getRace());
                for (SkillLearnTemplate skillTree : skillTemplates) {
                    if (resultItem.getSkillGroup().equals(skillTree.getSkillGroup())) {
                        player.getSkillList().addStigmaSkill(player, skillTree.getSkillId(), skillTree.getSkillLevel() + resultItem.getEnchantLevel());
                        PacketSendUtility.sendPacket(player, new SM_SKILL_LIST(player, player.getSkillList().getStigmaSkills()));
                    }
                }
            }
        }
    }
	
    public static void checkStigmaEnchant(Player player, List<Integer> list) {
        for (Item item : player.getEquipment().getEquippedItemsAllStigma()) {
            if (list.size() >= 6) {
                if (item.getEnchantLevel() == 6) {
                    player.setStigmaSet(1);
                } else if (item.getEnchantLevel() == 7) {
                    player.setStigmaSet(2);
                } else if (item.getEnchantLevel() == 8) {
                    player.setStigmaSet(3);
                } else if (item.getEnchantLevel() == 9) {
                    player.setStigmaSet(3);
                } else if (item.getEnchantLevel() >= 10) {
                    player.setStigmaSet(5);
                } else {
                    player.setStigmaSet(0);
                }
            }
        }
    }
	
    public static void onPlayerLogin(Player player) {
        List<Item> equippedItems = player.getEquipment().getEquippedItemsAllStigma();
        List<Integer> Stigma = player.getEquipment().getEquippedItemsAllStigmaIds();
        checkStigmaEnchant(player, Stigma);
        for (Item item : equippedItems) {
            for (int i = 1; i <= player.getLevel(); i++) {
                SkillLearnTemplate[] skillTemplates = DataManager.SKILL_TREE_DATA.getTemplatesFor(player.getPlayerClass(), i, player.getRace());
                for (SkillLearnTemplate skillTree : skillTemplates) {
                    if (item.getItemTemplate().isStigma() && item.getSkillGroup().equals(skillTree.getSkillGroup())) {
                        player.getSkillList().addStigmaSkill(player, skillTree.getSkillId(), skillTree.getSkillLevel() + item.getEnchantLevel() + player.getStigmaSet());
                        PacketSendUtility.sendPacket(player, new SM_SKILL_LIST(player, player.getSkillList().getStigmaSkills()));
                    }
                }
            }
        } for (Item item : equippedItems) {
            if (item.getItemTemplate().isStigma()) {
                if (!isPossibleEquippedStigma(player, item)) {
                    AuditLogger.info(player, "Possible client hack stigma count big :O");
                    player.getEquipment().unEquipItem(item.getObjectId(), 0);
                    continue;
                }
                Stigma stigmaInfo = item.getItemTemplate().getStigma();
                if (stigmaInfo == null) {
                    player.getEquipment().unEquipItem(item.getObjectId(), 0);
                    continue;
                }
                int needSkill = stigmaInfo.getRequireSkill().size();
                for (RequireSkill rs : stigmaInfo.getRequireSkill()) {
                    for (int id : rs.getSkillIds()) {
                        if (player.getSkillList().isSkillPresent(id)) {
                            needSkill--;
                            break;
                        }
                    }
                } if (needSkill != 0) {
                    AuditLogger.info(player, "Possible client hack advenced stigma skill.");
                    player.getEquipment().unEquipItem(item.getObjectId(), 0);
                    continue;
                } if (!item.getItemTemplate().isClassSpecific(player.getCommonData().getPlayerClass())) {
                    AuditLogger.info(player, "Possible client hack not valid for class.");
                    player.getEquipment().unEquipItem(item.getObjectId(), 0);
                    continue;
                }
            }
        }
        /** Stigma Linked Skills **/
        StigmaLinkedService.checkEquipConditions(player, Stigma);
    }

    private static int getPossibleStigmaCount(Player player) {
        if (player == null || player.getLevel() < 20) {
            return 0;
        }

		if (player.havePermission(MembershipConfig.STIGMA_SLOT_QUEST)) {
            return 7;
        }

        boolean isCompleteQuest = false;
        if (player.getRace() == Race.ELYOS) {
            isCompleteQuest = player.isCompleteQuest(1929) || player.getQuestStateList().getQuestState(1929).getStatus() == QuestStatus.START &&
            player.getQuestStateList().getQuestState(1929).getQuestVars().getQuestVars() == 98;
        } else {
            isCompleteQuest = player.isCompleteQuest(2900) || player.getQuestStateList().getQuestState(2900).getStatus() == QuestStatus.START &&
            player.getQuestStateList().getQuestState(2900).getQuestVars().getQuestVars() == 99;
        }
        int playerLevel = player.getLevel();
        if (isCompleteQuest) {
            if (playerLevel < 30) {
                return 2;
            } else if (playerLevel < 40) {
                return 3;
            } else if (playerLevel < 45) {
                return 4;
            } else if (playerLevel < 50) {
                return 5;
            } else if (playerLevel < 55) {
                return 6;
            } else if (playerLevel < 55 && player.getStigmaSet() >= 3) {
                return 7;
            } else {
                return 7;
            }
        }
        return 0;
    }
	
    private static boolean isPossibleEquippedStigma(Player player, Item item) {
        if (player == null || item == null || !item.getItemTemplate().isStigma()) {
            return false;
        }
        long itemSlotToEquip = item.getEquipmentSlot();
        if (ItemSlot.isRegularStigma(itemSlotToEquip)) {
            int stigmaCount = getPossibleStigmaCount(player);
            if (stigmaCount > 0) {
                if (stigmaCount == 1) {
                    if (itemSlotToEquip == ItemSlot.STIGMA1.getSlotIdMask()) {
                        return true;
                    }
                } else if (stigmaCount == 2) {
                    if (itemSlotToEquip == ItemSlot.STIGMA1.getSlotIdMask()
                        || itemSlotToEquip == ItemSlot.STIGMA2.getSlotIdMask()) {
                        return true;
                    }
                } else if (stigmaCount == 3) {
                    if (itemSlotToEquip == ItemSlot.STIGMA1.getSlotIdMask()
                        || itemSlotToEquip == ItemSlot.STIGMA2.getSlotIdMask()
                        || itemSlotToEquip == ItemSlot.STIGMA3.getSlotIdMask()) {
                        return true;
                    }
                } else if (stigmaCount == 4) {
                    if (itemSlotToEquip == ItemSlot.STIGMA1.getSlotIdMask()
                        || itemSlotToEquip == ItemSlot.STIGMA2.getSlotIdMask()
                        || itemSlotToEquip == ItemSlot.STIGMA3.getSlotIdMask()
                        || itemSlotToEquip == ItemSlot.STIGMA4.getSlotIdMask()) {
                        return true;
                    }
                } else if (stigmaCount == 5) {
                    if (itemSlotToEquip == ItemSlot.STIGMA1.getSlotIdMask()
                        || itemSlotToEquip == ItemSlot.STIGMA2.getSlotIdMask()
                        || itemSlotToEquip == ItemSlot.STIGMA3.getSlotIdMask()
                        || itemSlotToEquip == ItemSlot.STIGMA4.getSlotIdMask()
                        || itemSlotToEquip == ItemSlot.STIGMA5.getSlotIdMask()) {
                        return true;
                    }
                } else if (stigmaCount == 6) {
                    if (itemSlotToEquip == ItemSlot.STIGMA1.getSlotIdMask()
                        || itemSlotToEquip == ItemSlot.STIGMA2.getSlotIdMask()
                        || itemSlotToEquip == ItemSlot.STIGMA3.getSlotIdMask()
                        || itemSlotToEquip == ItemSlot.STIGMA4.getSlotIdMask()
                        || itemSlotToEquip == ItemSlot.STIGMA5.getSlotIdMask()
                        || itemSlotToEquip == ItemSlot.STIGMA6.getSlotIdMask()) {
                        return true;
                    }
                } else if (stigmaCount == 7) {
                    if (itemSlotToEquip == ItemSlot.STIGMA1.getSlotIdMask()
                        || itemSlotToEquip == ItemSlot.STIGMA2.getSlotIdMask()
                        || itemSlotToEquip == ItemSlot.STIGMA3.getSlotIdMask()
                        || itemSlotToEquip == ItemSlot.STIGMA4.getSlotIdMask()
                        || itemSlotToEquip == ItemSlot.STIGMA5.getSlotIdMask()
                        || itemSlotToEquip == ItemSlot.STIGMA6.getSlotIdMask()
                        || itemSlotToEquip == ItemSlot.STIGMA_SPECIAL.getSlotIdMask()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}