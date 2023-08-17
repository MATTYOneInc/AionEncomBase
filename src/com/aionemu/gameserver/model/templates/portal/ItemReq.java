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
package com.aionemu.gameserver.model.templates.portal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author xTz
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ItemReq")
public class ItemReq {

    @XmlAttribute(name = "item_id")
    protected int itemId;
    @XmlAttribute(name = "item_count")
    protected int itemCount;
	@XmlAttribute(name = "err_item")
	protected int errItem;

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int value) {
        this.itemId = value;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int value) {
        this.itemCount = value;
    }

	public int getErrItem() {
		return errItem;
	}
}