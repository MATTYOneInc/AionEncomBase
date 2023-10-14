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
package com.aionemu.gameserver.model.skill;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.item.Stigma.StigmaSkill;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SKILL_LIST;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.skillengine.model.SkillTemplate;
import com.aionemu.gameserver.utils.PacketSendUtility;

import javolution.util.FastList;

/**
 * @author IceReaper, orfeo087, Avol, AEJTester
 */
public final class PlayerSkillList implements SkillList<Player> {

	private static final Logger log = LoggerFactory.getLogger(PlayerSkillList.class);
	private final Map<Integer, PlayerSkillEntry> basicSkills;
	private final Map<Integer, PlayerSkillEntry> stigmaSkills;
	private final Map<Integer, PlayerSkillEntry> linkedSkills;
	FastList<Integer> linked = FastList.newInstance();

	private final List<PlayerSkillEntry> deletedSkills;

	public PlayerSkillList() {
		this.basicSkills = new HashMap<Integer, PlayerSkillEntry>(0);
		this.stigmaSkills = new HashMap<Integer, PlayerSkillEntry>(0);
		this.linkedSkills = new HashMap<Integer, PlayerSkillEntry>(0);
		this.deletedSkills = new ArrayList<PlayerSkillEntry>(0);
	}

	public PlayerSkillList(List<PlayerSkillEntry> skills) {
		this();
		for (PlayerSkillEntry entry : skills) {
			if (entry.isStigma()) {
				stigmaSkills.put(entry.getSkillId(), entry);
			} else if (entry.isLinked()) {
				linkedSkills.put(entry.getSkillId(), entry);
			} else {
				basicSkills.put(entry.getSkillId(), entry);
			}
		}
	}

	/**
	 * Returns array with all skills
	 */
	public PlayerSkillEntry[] getAllSkills() {
		List<PlayerSkillEntry> allSkills = new ArrayList<PlayerSkillEntry>();
		allSkills.addAll(basicSkills.values());
		allSkills.addAll(stigmaSkills.values());
		allSkills.addAll(linkedSkills.values());
		return allSkills.toArray(new PlayerSkillEntry[allSkills.size()]);
	}

	public List<Integer> getAllSkills2() {
		HashSet<Integer> allSkills = new HashSet<Integer>();
		for (PlayerSkillEntry i : basicSkills.values()) {
			allSkills.add(i.getSkillId());
		}
		for (PlayerSkillEntry i : stigmaSkills.values()) {
			allSkills.add(i.getSkillId());
		}
		for (PlayerSkillEntry i : linkedSkills.values()) {
			allSkills.add(i.getSkillId());
		}
		return Arrays.asList(allSkills.toArray(new Integer[0]));
	}

	public PlayerSkillEntry[] getBasicSkills() {
		return basicSkills.values().toArray(new PlayerSkillEntry[basicSkills.size()]);
	}

	public PlayerSkillEntry[] getStigmaSkills() {
		return stigmaSkills.values().toArray(new PlayerSkillEntry[stigmaSkills.size()]);
	}

	public PlayerSkillEntry[] getLinkedSkills() {
		return linkedSkills.values().toArray(new PlayerSkillEntry[linkedSkills.size()]);
	}

	public PlayerSkillEntry[] getDeletedSkills() {
		return deletedSkills.toArray(new PlayerSkillEntry[deletedSkills.size()]);
	}

	public PlayerSkillEntry getSkillEntry(int skillId) {
		if (basicSkills.containsKey(skillId)) {
			return basicSkills.get(skillId);
		}
		return stigmaSkills.get(skillId);
	}

	public PlayerSkillEntry getLinkedSkillEntry(int skillId) {
		return linkedSkills.get(skillId);
	}

	@Override
	public boolean addSkill(Player player, int skillId, int skillLevel) {
		return addSkill(player, skillId, skillLevel, false, false, PersistentState.NEW);
	}

	public boolean addGMSkill(Player player, int skillId, int skillLevel) {
		return addSkillAct(player, skillId, skillLevel, true, false, PersistentState.NOACTION, true);
	}

	private synchronized boolean addSkillAct(Player player, int skillId, int skillLevel, boolean isStigma,
			boolean isLinked, PersistentState state, boolean isGMSkill) {
		PlayerSkillEntry existingSkill = isStigma ? stigmaSkills.get(skillId) : basicSkills.get(skillId);

		boolean isNew = false;
		if (existingSkill != null) {
			// if (existingSkill.getSkillLevel() >= skillLevel) {
			// return false;
			// }
			existingSkill.setSkillLvl(skillLevel);
		} else {
			if (isStigma) {
				stigmaSkills.put(skillId,
						new PlayerSkillEntry(skillId, true, false, skillLevel, 0, null, 0, false, state));
			} else if (isLinked) {
				basicSkills.put(skillId,
						new PlayerSkillEntry(skillId, false, true, skillLevel, 0, null, 0, false, state));
				linkedSkills.put(skillId,
						new PlayerSkillEntry(skillId, false, true, skillLevel, 0, null, 0, false, state));
				isNew = true;
			} else {
				basicSkills.put(skillId,
						new PlayerSkillEntry(skillId, false, false, skillLevel, 0, null, 0, false, state));
				isNew = true;
			}
		}
		if (player.isSpawned()) {
			if (!isStigma || isGMSkill) {
				sendMessage(player, skillId, isNew);
			}
		}
		return true;
	}

	@Override
	public boolean addLinkedSkill(Player player, int skillId) {
		player.setLinkedSkill(skillId);
		SkillTemplate linked = DataManager.SKILL_DATA.getSkillTemplate(skillId);
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_STIGMA_GET_LINKED_SKILL(
				new DescriptionId(DataManager.SKILL_DATA.getSkillTemplate(linked.getSkillId()).getNameId()), 1));
		return addSkill(player, skillId, 1, false, true, PersistentState.NOACTION);
	}

	public boolean addStigmaSkill(Player player, int skillId, int skillLevel) {
		return addSkill(player, skillId, skillLevel, true, false, PersistentState.NOACTION);
	}

	/**
	 * Add temporary skill which will not be saved in db
	 *
	 * @param player
	 * @param skillId
	 * @param skillLevel
	 * @return
	 */
	public boolean addAbyssSkill(Player player, int skillId, int skillLevel) {
		return addSkill(player, skillId, skillLevel, false, false, PersistentState.NOACTION);
	}

	public void addLinkedStigmaSkill(Player player, int skillId, int skillLvl) {
		PlayerSkillEntry skill = new PlayerSkillEntry(skillId, false, true, skillLvl, 0, null, 0, false,
				PersistentState.NOACTION);
		this.stigmaSkills.put(skillId, skill);
		PacketSendUtility.sendPacket(player, new SM_SKILL_LIST(player, skill));
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_STIGMA_GET_LINKED_SKILL(
				new DescriptionId(DataManager.SKILL_DATA.getSkillTemplate(skill.getSkillId()).getNameId()), skillLvl));
	}

	public void addStigmaSkill(Player player, List<StigmaSkill> skills, boolean equipedByNpc) {
		for (StigmaSkill sSkill : skills) {
			PlayerSkillEntry skill = new PlayerSkillEntry(sSkill.getSkillId(), true, false, sSkill.getSkillLvl(), 0,
					null, 0, false, PersistentState.NOACTION);
			this.stigmaSkills.put(sSkill.getSkillId(), skill);
			if (equipedByNpc) {
				PacketSendUtility.sendPacket(player, new SM_SKILL_LIST(skill, 1300401, false));
			}
		}
	}

	public void addStigmaSkill(Player player, int skillId, int skillLevel, boolean withMsg, boolean equipedByNpc) {
		PlayerSkillEntry skill = new PlayerSkillEntry(skillId, true, false, skillLevel, 0, null, 0, false,
				PersistentState.NOACTION);
		this.stigmaSkills.put(skillId, skill);
		if (equipedByNpc) {
			PacketSendUtility.sendPacket(player, new SM_SKILL_LIST(skill, withMsg ? 1300401 : 0, false));
		}
	}

	private synchronized boolean addSkill(Player player, int skillId, int skillLevel, boolean isStigma,
			boolean isLinked, PersistentState state) {
		PlayerSkillEntry existingSkill = isStigma ? stigmaSkills.get(skillId) : basicSkills.get(skillId);

		boolean isNew = false;
		if (existingSkill != null) {
			existingSkill.setSkillLvl(skillLevel);
		} else {
			if (isStigma) {
				stigmaSkills.put(skillId,
						new PlayerSkillEntry(skillId, true, false, skillLevel, 0, null, 0, false, state));
			} else if (isLinked) {
				basicSkills.put(skillId,
						new PlayerSkillEntry(skillId, false, true, skillLevel, 0, null, 0, false, state));
				linkedSkills.put(skillId,
						new PlayerSkillEntry(skillId, false, true, skillLevel, 0, null, 0, false, state));
				isNew = true;
			} else {
				basicSkills.put(skillId,
						new PlayerSkillEntry(skillId, false, false, skillLevel, 0, null, 0, false, state));
				isNew = true;
			}
		}
		if (player.isSpawned()) {
			sendMessage(player, skillId, isNew);
		}
		PacketSendUtility.sendPacket(player, new SM_SKILL_LIST(player, player.getSkillList().getBasicSkills()));
		PacketSendUtility.sendPacket(player, new SM_SKILL_LIST(player, player.getSkillList().getLinkedSkills()));
		return true;
	}

	public boolean isCraftSkill(int skilId) {
		switch (skilId) {
		case 30001:
		case 30002:
		case 30003:
		case 40001:
		case 40002:
		case 40003:
		case 40004:
		case 40005:
		case 40006:
		case 40007:
		case 40008:
		case 40009:
		case 40010:
		case 40011:
			return true;
		default:
			return false;
		}
	}

	/**
	 * @param player
	 * @param skillId
	 * @param xpReward
	 * @return
	 */
	public boolean addSkillXp(Player player, int skillId, int xpReward, int objSkillPoints) {
		PlayerSkillEntry skillEntry = getSkillEntry(skillId);
		int maxDiff = 40;
		int SkillLvlDiff = skillEntry.getSkillLevel() - objSkillPoints;
		if (maxDiff < SkillLvlDiff) {
			return false;
		}
		switch (skillEntry.getSkillId()) {
		case 30001:
			if (skillEntry.getSkillLevel() == 49) {
				return false;
			}
		case 30002:
		case 30003:
			if (skillEntry.getSkillLevel() == 449) {
				break;
			}
		case 40001:
		case 40002:
		case 40003:
		case 40004:
		case 40007:
		case 40008:
		case 40010:
			switch (skillEntry.getSkillLevel()) {
			case 99:
			case 199:
			case 299:
			case 399:
			case 449:
			case 499:
			case 549:
				return false;
			}
			player.getRecipeList().autoLearnRecipe(player, skillId, skillEntry.getSkillLevel());
		}
		boolean updateSkill = skillEntry.addSkillXp(player, xpReward);
		if (updateSkill) {
			sendMessage(player, skillId, false);
		}
		return true;
	}

	@Override
	public boolean isSkillPresent(int skillId) {
		return basicSkills.containsKey(skillId) || stigmaSkills.containsKey(skillId)
				|| linkedSkills.containsKey(skillId);
	}

	@Override
	public int getSkillLevel(int skillId) {
		if (basicSkills.containsKey(skillId)) {
			return basicSkills.get(skillId).getSkillLevel();
		}
		if (linkedSkills.containsKey(skillId)) {
			return linkedSkills.get(skillId).getSkillLevel();
		}
		return stigmaSkills.get(skillId).getSkillLevel();
	}

	@Override
	public synchronized boolean removeSkill(int skillId) {
		PlayerSkillEntry entry = basicSkills.get(skillId);
		if (entry == null) {
			entry = stigmaSkills.get(skillId);
			entry = linkedSkills.get(skillId);
		}
		if (entry != null) {
			entry.setPersistentState(PersistentState.DELETED);
			deletedSkills.add(entry);
			basicSkills.remove(skillId);
			stigmaSkills.remove(skillId);
			linkedSkills.remove(skillId);
		}
		return entry != null;
	}

	@Override
	public int size() {
		return basicSkills.size() + stigmaSkills.size();
	}

	/**
	 * @param player
	 * @param skillId
	 */
	private void sendMessage(Player player, int skillId, boolean isNew) {
		switch (skillId) {
		case 30001:
		case 30002:
			PacketSendUtility.sendPacket(player,
					new SM_SKILL_LIST(player.getSkillList().getSkillEntry(skillId), 1330005, false));
			break;
		case 30003:
			PacketSendUtility.sendPacket(player,
					new SM_SKILL_LIST(player.getSkillList().getSkillEntry(skillId), 1330005, false));
			break;
		case 40001:
		case 40002:
		case 40003:
		case 40004:
		case 40005:
		case 40006:
		case 40007:
		case 40008:
		case 40009:
		case 40010:
			PacketSendUtility.sendPacket(player,
					new SM_SKILL_LIST(player.getSkillList().getSkillEntry(skillId), 1330061, false));
			break;
		default:
			PacketSendUtility.sendPacket(player,
					new SM_SKILL_LIST(player.getSkillList().getSkillEntry(skillId), 1300050, isNew));
		}
	}

	public boolean addSkillWithoutSave(Player player, int skillId, int skillLevel) {
		return addSkill(player, skillId, skillLevel, false, false, PersistentState.NOACTION);
	}
}