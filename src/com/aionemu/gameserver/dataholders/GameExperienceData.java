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
package com.aionemu.gameserver.dataholders;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.aionemu.gameserver.model.templates.event.GameExperience;

import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * @author Rinzler (Encom)
 */

@XmlRootElement(name = "game_experience_items")
@XmlAccessorType(XmlAccessType.FIELD)
public class GameExperienceData {
	@XmlElement(name = "game_experience_item")
	private List<GameExperience> glist;

	@XmlTransient
	private TIntObjectHashMap<GameExperience> experienceData = new TIntObjectHashMap<GameExperience>();

	@XmlTransient
	private Map<Integer, GameExperience> experienceDataMap = new HashMap<Integer, GameExperience>(1);

	void afterUnmarshal(Unmarshaller paramUnmarshaller, Object paramObject) {
		for (GameExperience gameExperience : glist) {
			experienceData.put(gameExperience.getId(), gameExperience);
			experienceDataMap.put(gameExperience.getId(), gameExperience);
		}
	}

	public int size() {
		return experienceData.size();
	}

	public GameExperience getGameExperienceId(int id) {
		return experienceData.get(id);
	}

	public Map<Integer, GameExperience> getAll() {
		return experienceDataMap;
	}
}