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

import com.aionemu.gameserver.model.Race;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author xTz
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PortalPath")
public class PortalPath {

    @XmlElement(name = "portal_req")
    protected PortalReq portalReq;
    @XmlAttribute(name = "dialog")
    protected int dialog;
    @XmlAttribute(name = "loc_id")
    protected int locId;
    @XmlAttribute(name = "player_count")
    protected int playerCount;
    @XmlAttribute(name = "instance")
    protected boolean instance;
	@XmlAttribute(name = "siege_id")
	protected int siegeId;
	@XmlAttribute(name = "race")
	protected Race race = Race.PC_ALL;
	@XmlAttribute(name = "err_group")
	protected int errGroup;

    public PortalReq getPortalReq() {
        return portalReq;
    }

    public int getDialog() {
        return dialog;
    }

    public void setDialog(int value) {
        this.dialog = value;
    }

    public int getLocId() {
        return locId;
    }

    public void setLocId(int value) {
        this.locId = value;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public void setPlayerCount(int value) {
        this.playerCount = value;
    }

    public boolean isInstance() {
		return instance;
    }

    public void setInstance(boolean value) {
        this.instance = value;
    }

	public int getSiegeId() {
		return siegeId;
	}

	public Race getRace() {
		return race;
	}

	public int getErrGroup() {
		return errGroup;
	}
}