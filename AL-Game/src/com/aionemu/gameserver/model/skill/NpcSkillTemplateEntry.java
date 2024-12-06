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

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.model.templates.npcskill.NpcSkillTemplate;

class NpcSkillTemplateEntry extends NpcSkillEntry {

	private final NpcSkillTemplate template;

	public NpcSkillTemplateEntry(NpcSkillTemplate template) {
		super(template.getSkillid(), template.getSkillLevel());
		this.template = template;
	}

	@Override
	public boolean isReady(int hpPercentage, long fightingTimeInMSec) {
		if (hasCooldown() || !chanceReady()) {
			return false;
		}

		switch (template.getConjunctionType()) {
		case XOR:
			return (hpReady(hpPercentage) && !timeReady(fightingTimeInMSec))
					|| (!hpReady(hpPercentage) && timeReady(fightingTimeInMSec));
		case OR:
			return hpReady(hpPercentage) || timeReady(fightingTimeInMSec);
		case AND:
			return hpReady(hpPercentage) && timeReady(fightingTimeInMSec);
		default:
			return false;
		}
	}

	@Override
	public boolean chanceReady() {
		return Rnd.get(0, 100) < template.getProbability();
	}

	@Override
	public boolean hpReady(int hpPercentage) {
		if (template.getMaxhp() == 0 && template.getMinhp() == 0) {
			return true;
		} else if (template.getMaxhp() >= hpPercentage && template.getMinhp() <= hpPercentage) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean timeReady(long fightingTimeInMSec) {
		if (template.getMaxTime() == 0 && template.getMinTime() == 0) {
			return true;
		} else if (template.getMaxTime() >= fightingTimeInMSec && template.getMinTime() <= fightingTimeInMSec) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean hasCooldown() {
		return template.getCooldown() > (System.currentTimeMillis() - lastTimeUsed);
	}

	@Override
	public boolean UseInSpawned() {
		return template.getUseInSpawned();
	}
}