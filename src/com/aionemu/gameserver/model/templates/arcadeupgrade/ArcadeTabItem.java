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
package com.aionemu.gameserver.model.templates.arcadeupgrade;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by wanke on 17/02/2017.
 */
@XmlType(name = "ArcadeTabItemList")
public class ArcadeTabItem
{
    @XmlAttribute(name = "item_id")
    protected int item_id;
	
    @XmlAttribute(name = "normalcount")
    protected int normalcount;
	
    @XmlAttribute(name = "frenzycount")
    protected int frenzycount;
	
    public final int getItemId() {
        return item_id;
    }
	
    public final int getNormalCount() {
        return normalcount;
    }
	
    public final int getFrenzyCount() {
        return frenzycount;
    }
}