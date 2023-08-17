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
package com.aionemu.gameserver.dataholders;

import com.aionemu.gameserver.model.templates.tower_reward.TowerStageRewardTemplate;
import gnu.trove.map.hash.TIntObjectHashMap;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Wnkrz on 17/10/2017.
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "tower_reward_templates")
public class TowerRewardData
{
    @XmlElement(name = "tower_reward_template")
    private List<TowerStageRewardTemplate> TowerReward;
	
    @XmlTransient
    private TIntObjectHashMap<TowerStageRewardTemplate> templates = new TIntObjectHashMap<TowerStageRewardTemplate>();
	
    @XmlTransient
    private Map<Integer, TowerStageRewardTemplate> templatesMap = new HashMap<Integer, TowerStageRewardTemplate>();
	
    void afterUnmarshal(Unmarshaller u, Object parent) {
        for (TowerStageRewardTemplate template : TowerReward) {
            templates.put(template.getFloor(), template);
            templatesMap.put(template.getFloor(), template);
        }
        TowerReward.clear();
        TowerReward = null;
    }
	
    public int size() {
        return templates.size();
    }
	
    public TowerStageRewardTemplate getTowerReward(int towerId) {
        return templates.get(towerId);
    }
	
    public Map<Integer, TowerStageRewardTemplate> getAll() {
        return templatesMap;
    }
}