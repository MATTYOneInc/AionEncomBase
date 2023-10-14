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
package com.aionemu.gameserver.services.craft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.configs.main.CraftConfig;
import com.aionemu.gameserver.dao.PlayerRecipesDAO;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.RecipeList;
import com.aionemu.gameserver.model.gameobjects.player.RequestResponseHandler;
import com.aionemu.gameserver.model.skill.PlayerSkillList;
import com.aionemu.gameserver.model.templates.CraftLearnTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_LEARN_RECIPE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SKILL_LIST;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;

public class CraftSkillUpdateService
{
	private static final Logger log = LoggerFactory.getLogger(CraftSkillUpdateService.class);
	
	protected static final Map<Integer, CraftLearnTemplate> npcBySkill = new HashMap<Integer, CraftLearnTemplate>();
	private static final Map<Integer, Integer> cost = new HashMap<Integer, Integer>();
	private static final List<Integer> craftingSkillIds = new ArrayList<Integer>();
	
	public static final CraftSkillUpdateService getInstance() {
		return SingletonHolder.instance;
	}
	
	private CraftSkillUpdateService() {
		//CRAFT ASMODIANS.
		npcBySkill.put(204096, new CraftLearnTemplate(30002, false, "Essencetapping")); //Essencetapping (You can obtain materials through extracting the vitality of objects)
		npcBySkill.put(830158, new CraftLearnTemplate(30002, false, "Essencetapping")); //Essencetapping (You can obtain materials through extracting the vitality of objects)
		npcBySkill.put(204257, new CraftLearnTemplate(30003, false, "Aethertapping")); //Aethertapping (You can obtain the Aether Crystals floating in the sky)
		npcBySkill.put(830148, new CraftLearnTemplate(30003, false, "Aethertapping")); //Aethertapping (You can obtain the Aether Crystals floating in the sky)
		npcBySkill.put(204100, new CraftLearnTemplate(40001, true, "Cooking")); //Cooking (Allows you to make food and beverages that have effects when consumed. You may also purify contaminated materials)
		npcBySkill.put(830142, new CraftLearnTemplate(40001, true, "Cooking")); //Cooking (Allows you to make food and beverages that have effects when consumed. You may also purify contaminated materials)
		npcBySkill.put(204104, new CraftLearnTemplate(40002, true, "Weaponsmithing")); //Weaponsmithing (Allows you to craft metal weapons)
		npcBySkill.put(830146, new CraftLearnTemplate(40002, true, "Weaponsmithing")); //Weaponsmithing (Allows you to craft metal weapons)
		npcBySkill.put(204106, new CraftLearnTemplate(40003, true, "Armorsmithing")); //Armorsmithing (Allows you to craft metal armor, helmets and shields)
		npcBySkill.put(830144, new CraftLearnTemplate(40003, true, "Armorsmithing")); //Armorsmithing (Allows you to craft metal armor, helmets and shields)
		npcBySkill.put(204110, new CraftLearnTemplate(40004, true, "Tailoring")); //Tailoring (Allows you to make cloth armor, leather armor and clothing with cloth and leather)
		npcBySkill.put(830136, new CraftLearnTemplate(40004, true, "Tailoring")); //Tailoring (Allows you to make cloth armor, leather armor and clothing with cloth and leather)
		npcBySkill.put(204102, new CraftLearnTemplate(40007, true, "Alchemy")); //Alchemy (Allows you to make items that enhance various magical weapons, items and equipment)
		npcBySkill.put(830138, new CraftLearnTemplate(40007, true, "Alchemy")); //Alchemy (Allows you to make items that enhance various magical weapons, items and equipment)
		npcBySkill.put(204108, new CraftLearnTemplate(40008, true, "Handicrafting")); //Handicrafting (Allows you to craft precious metal, wooden weapons and small parts)
		npcBySkill.put(830140, new CraftLearnTemplate(40008, true, "Handicrafting")); //Handicrafting (Allows you to craft precious metal, wooden weapons and small parts)
		npcBySkill.put(798452, new CraftLearnTemplate(40010, true, "Construction")); //Construction Asmodians Pandaemonium.
		npcBySkill.put(798456, new CraftLearnTemplate(40010, true, "Construction")); //Construction Asmodians Pernon.
		
		//CRAFT ELYOS.
		npcBySkill.put(203780, new CraftLearnTemplate(30002, false, "Essencetapping")); //Essencetapping (You can obtain materials through extracting the vitality of objects)
		npcBySkill.put(830066, new CraftLearnTemplate(30002, false, "Essencetapping")); //Essencetapping (You can obtain materials through extracting the vitality of objects)
		npcBySkill.put(203782, new CraftLearnTemplate(30003, false, "Aethertapping")); //Aethertapping (You can obtain the Aether Crystals floating in the sky)
		npcBySkill.put(830064, new CraftLearnTemplate(30003, false, "Aethertapping")); //Aethertapping (You can obtain the Aether Crystals floating in the sky)
		npcBySkill.put(203784, new CraftLearnTemplate(40001, true, "Cooking")); //Cooking (Allows you to make food and beverages that have effects when consumed. You may also purify contaminated materials)
		npcBySkill.put(830058, new CraftLearnTemplate(40001, true, "Cooking")); //Cooking (Allows you to make food and beverages that have effects when consumed. You may also purify contaminated materials)
		npcBySkill.put(203788, new CraftLearnTemplate(40002, true, "Weaponsmithing")); //Weaponsmithing (Allows you to craft metal weapons)
		npcBySkill.put(830062, new CraftLearnTemplate(40002, true, "Weaponsmithing")); //Weaponsmithing (Allows you to craft metal weapons)
		npcBySkill.put(203790, new CraftLearnTemplate(40003, true, "Armorsmithing")); //Armorsmithing (Allows you to craft metal armor, helmets and shields)
		npcBySkill.put(830060, new CraftLearnTemplate(40003, true, "Armorsmithing")); //Armorsmithing (Allows you to craft metal armor, helmets and shields)
		npcBySkill.put(203793, new CraftLearnTemplate(40004, true, "Tailoring")); //Tailoring (Allows you to make cloth armor, leather armor and clothing with cloth and leather)
		npcBySkill.put(830052, new CraftLearnTemplate(40004, true, "Tailoring")); //Tailoring (Allows you to make cloth armor, leather armor and clothing with cloth and leather)
		npcBySkill.put(203786, new CraftLearnTemplate(40007, true, "Alchemy")); //Alchemy (Allows you to make items that enhance various magical weapons, items and equipment)
		npcBySkill.put(830054, new CraftLearnTemplate(40007, true, "Alchemy")); //Alchemy (Allows you to make items that enhance various magical weapons, items and equipment)
		npcBySkill.put(203792, new CraftLearnTemplate(40008, true, "Handicrafting")); //Handicrafting (Allows you to craft precious metal, wooden weapons and small parts)
		npcBySkill.put(830056, new CraftLearnTemplate(40008, true, "Handicrafting")); //Handicrafting (Allows you to craft precious metal, wooden weapons and small parts)
		npcBySkill.put(798450, new CraftLearnTemplate(40010, true, "Construction")); //Construction Elyos Sanctum.
		npcBySkill.put(798454, new CraftLearnTemplate(40010, true, "Construction")); //Construction Elyos Oriel.
		
		//PRICE CRAFT KINAH.
		cost.put(0, 3500);
		cost.put(99, 17000);
		cost.put(199, 115000);
		cost.put(299, 460000);
		cost.put(399, 0);
		cost.put(499, 10000000);
		
		craftingSkillIds.add(40001);
		craftingSkillIds.add(40002);
		craftingSkillIds.add(40003);
		craftingSkillIds.add(40004);
		craftingSkillIds.add(40007);
		craftingSkillIds.add(40008);
		craftingSkillIds.add(40010);
		log.info("Craft System: Initialized.");
	}
	
	public void setMorphRecipe(Player player) {
		int object = player.getObjectId();
		Race race = player.getCommonData().getRace();
		if (player.getLevel() == 10) {
			RecipeList recipelist = null;
			recipelist = DAOManager.getDAO(PlayerRecipesDAO.class).load(object);
			if (race == Race.ELYOS) {
				if (!recipelist.isRecipePresent(155000005)) { //Morph Method: Iron Ore.
					DAOManager.getDAO(PlayerRecipesDAO.class).addRecipe(object, 155000005);
					PacketSendUtility.sendPacket(player, new SM_LEARN_RECIPE(155000005));
				} if (!recipelist.isRecipePresent(155000002)) { //Morph Method: Inina.
					DAOManager.getDAO(PlayerRecipesDAO.class).addRecipe(object, 155000002);
					PacketSendUtility.sendPacket(player, new SM_LEARN_RECIPE(155000002));
				} if (!recipelist.isRecipePresent(155000001)) { //Morph Method: Aria.
					DAOManager.getDAO(PlayerRecipesDAO.class).addRecipe(object, 155000001);
					PacketSendUtility.sendPacket(player, new SM_LEARN_RECIPE(155000001));
				}
			} else if (race == Race.ASMODIANS) {
				if (!recipelist.isRecipePresent(155005005)) { //Morph Method: Iron Ore.
					DAOManager.getDAO(PlayerRecipesDAO.class).addRecipe(object, 155005005);
					PacketSendUtility.sendPacket(player, new SM_LEARN_RECIPE(155005005));
				} if (!recipelist.isRecipePresent(155005002)) { //Morph Method: Conide.
					DAOManager.getDAO(PlayerRecipesDAO.class).addRecipe(object, 155005002);
					PacketSendUtility.sendPacket(player, new SM_LEARN_RECIPE(155005002));
				} if (!recipelist.isRecipePresent(155005001)) { //Morph Method: Azpha.
					DAOManager.getDAO(PlayerRecipesDAO.class).addRecipe(object, 155005001);
					PacketSendUtility.sendPacket(player, new SM_LEARN_RECIPE(155005001));
				}
			}
		}
	}
	
	public void learnSkill(Player player, Npc npc) {
		if (player.getLevel() < 10) {
			return;
		}
		final CraftLearnTemplate template = npcBySkill.get(npc.getNpcId());
		if (template == null) {
			return;
		}
		final int skillId = template.getSkillId();
		if (skillId == 0) {
			return;
		}
		int skillLvl = 0;
		PlayerSkillList skillList = player.getSkillList();
		if (skillList.isSkillPresent(skillId)) {
			skillLvl = skillList.getSkillLevel(skillId);
		} if (!cost.containsKey(skillLvl)) {
			//You cannot be promoted as your skill level is too low.
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1390233));
			return;
		} if (isCraftingSkill(skillId) && (!canLearnMoreExpertCraftingSkill(player) && skillLvl == 399)) {
			PacketSendUtility.sendMessage(player, "You can only have " + CraftConfig.MAX_EXPERT_CRAFTING_SKILLS + " Expert crafting skills.");
			return;
		} if (isCraftingSkill(skillId) && (!canLearnMoreMasterCraftingSkill(player) && skillLvl == 499)) {
			PacketSendUtility.sendMessage(player, "You can only have " + CraftConfig.MAX_MASTER_CRAFTING_SKILLS + " Master crafting skill.");
			return;
		}
		//ESSENCETAPPING
		if (skillLvl == 399 && ((skillId == 30002 && //Essencetapping [Journeyman]
		    (!player.isCompleteQuest(19001) || !player.isCompleteQuest(29001))))) {
			//You must pass the Expert test in order to be promoted.
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400284));
			return;
		} if (skillLvl == 499 && (skillId == 30002)) { //Essencetapping [Artisan]
			//You cannot be promoted any more.
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1330069));
			return;
		}
		//AETHERTAPPING
		if (skillLvl == 399 && ((skillId == 30003 && //[Journeyman]
		    (!player.isCompleteQuest(19003) || !player.isCompleteQuest(29003))))) {
			//You must pass the Expert test in order to be promoted.
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400284));
			return;
		} if (skillLvl == 499 && (skillId == 30003)) { //Aethertapping [Artisan]
			//You cannot be promoted any more.
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1330069));
			return;
		}
		//CRAFTING
		if (skillLvl == 499 //[Artisan]
			&& ((skillId == 40001 && (!player.isCompleteQuest(19039) || !player.isCompleteQuest(29039)))
			|| (skillId == 40002 && (!player.isCompleteQuest(19009) || !player.isCompleteQuest(29009)))
			|| (skillId == 40003 && (!player.isCompleteQuest(19015) || !player.isCompleteQuest(29015)))
			|| (skillId == 40004 && (!player.isCompleteQuest(19021) || !player.isCompleteQuest(29021)))
			|| (skillId == 40007 && (!player.isCompleteQuest(19033) || !player.isCompleteQuest(29033)))
			|| (skillId == 40008 && (!player.isCompleteQuest(19027) || !player.isCompleteQuest(29027)))
			|| (skillId == 40010 && (!player .isCompleteQuest(19058) || !player.isCompleteQuest(29058))))) {
			//You must pass the Master test in order to be promoted.
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400286));
			return;
		} if (skillLvl == 549) { //[Master]
			//You cannot be promoted any more.
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1330069));
			return;
		}
		final int price = cost.get(skillLvl);
		final long kinah = player.getInventory().getKinah();
		final int skillLevel = skillLvl;
		RequestResponseHandler responseHandler = new RequestResponseHandler(npc) {
			@Override
			public void acceptRequest(Creature requester, Player responder) {
				if (price < kinah && responder.getInventory().tryDecreaseKinah(price)) {
					PlayerSkillList skillList = responder.getSkillList();
					skillList.addSkill(responder, skillId, skillLevel + 1);
					responder.getRecipeList().autoLearnRecipe(responder, skillId, skillLevel + 1);
					//You have learned the %0 skill.
					PacketSendUtility.sendPacket(responder, new SM_SKILL_LIST(skillList.getSkillEntry(skillId), 1330004, false));
				} else {
					//You do not have enough Kinah.
					PacketSendUtility.sendPacket(responder, new SM_SYSTEM_MESSAGE(1300388));
				}
			}
			@Override
			public void denyRequest(Creature requester, Player responder) {
			}
		};
		boolean result = player.getResponseRequester().putRequest(SM_QUESTION_WINDOW.STR_CRAFT_ADDSKILL_CONFIRM, responseHandler);
		if (result) {
			PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_CRAFT_ADDSKILL_CONFIRM, 0, 0, new DescriptionId(DataManager.SKILL_DATA.getSkillTemplate(skillId).getNameId()), String.valueOf(price)));
		}
	}
	
	public static boolean isCraftingSkill(int skillId) {
		Iterator<Integer> it = craftingSkillIds.iterator();
		while (it.hasNext()) {
			if (it.next() == skillId) {
				return true;
			}
		}
		return false;
	}
	
	static int getTotalExpertCraftingSkills(Player player) {
		int mastered = 0;
		Iterator<Integer> it = craftingSkillIds.iterator();
		while (it.hasNext()) {
			int skillId = it.next();
			int skillLvl = 0;
			if (player.getSkillList().isSkillPresent(skillId)) {
				skillLvl = player.getSkillList().getSkillLevel(skillId);
				if (skillLvl > 399 && skillLvl <= 499) {
					mastered++;
				}
			}
		}
		return mastered;
	}
	
	static int getTotalMasterCraftingSkills(Player player) {
		int mastered = 0;
		Iterator<Integer> it = craftingSkillIds.iterator();
		while (it.hasNext()) {
			int skillId = it.next();
			int skillLvl = 0;
			if (player.getSkillList().isSkillPresent(skillId)) {
				skillLvl = player.getSkillList().getSkillLevel(skillId);
				if (skillLvl > 499) {
					mastered++;
				}
			}
		}
		return mastered;
	}
	
	public static boolean canLearnMoreExpertCraftingSkill(Player player) {
		if (getTotalExpertCraftingSkills(player) + getTotalMasterCraftingSkills(player) < CraftConfig.MAX_EXPERT_CRAFTING_SKILLS) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public static boolean canLearnMoreMasterCraftingSkill(Player player) {
		if (getTotalMasterCraftingSkills(player) < CraftConfig.MAX_MASTER_CRAFTING_SKILLS) {
			return true;
		}
		else {
			return false;
		}
	}
	
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder {
		protected static final CraftSkillUpdateService instance = new CraftSkillUpdateService();
	}
}