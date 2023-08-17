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

import com.aionemu.gameserver.model.templates.globaldrops.GlobalRule;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanke on 19/02/2017.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GlobalDropData", propOrder = { "globalDropRules" })
@XmlRootElement(name = "global_rules")
public class GlobalDropData
{
    @XmlElementWrapper(name = "gd_rules")
    @XmlElement(name = "gd_rule")
    protected List<GlobalRule> globalDropRules;
	
    public List<GlobalRule> getAllRules() {
        if (globalDropRules == null) {
            globalDropRules = new ArrayList<GlobalRule>();
        }
        return this.globalDropRules;
    }
	
    public int size() {
        return globalDropRules.size();
    }
}