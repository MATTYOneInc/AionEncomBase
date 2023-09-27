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

import com.aionemu.gameserver.model.templates.atreian_bestiary.AtreianBestiaryTemplate;
import javolution.util.FastMap;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Map;

/**
 * @author Ranastic
 */
@XmlRootElement(name = "monster_books")
@XmlAccessorType(XmlAccessType.FIELD)
public class AtreianBestiaryData {

	@XmlElement(name = "monster_book", type = AtreianBestiaryTemplate.class)
	private List<AtreianBestiaryTemplate> templates;
	
	private final Map<Integer, AtreianBestiaryTemplate> idsHolder = new FastMap<Integer, AtreianBestiaryTemplate>().shared();
	private final Map<Integer, AtreianBestiaryTemplate> npcIdsHolder = new FastMap<Integer, AtreianBestiaryTemplate>().shared();

	void afterUnmarshal(Unmarshaller u, Object parent) {
		for (AtreianBestiaryTemplate template : templates) {
			idsHolder.put(template.getId(), template);
			if (!template.getNpcIds().isEmpty()) {
				for (int npcId : template.getNpcIds()) {
					npcIdsHolder.put(npcId, template);
    			}
			}
		}
		templates.clear();
		templates = null;
	}

	public int size() {
		return idsHolder.size();
	}

	public AtreianBestiaryTemplate getAtreianBestiaryTemplate(int id) {
		return idsHolder.get(id);
	}
	
	public int sizeByNpcId() {
		return npcIdsHolder.size();
	}

	public AtreianBestiaryTemplate getAtreianBestiaryTemplateByNpcId(int id) {
		return npcIdsHolder.get(id);
	}
}