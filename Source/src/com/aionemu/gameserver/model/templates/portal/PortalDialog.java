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

import java.util.List;
import javax.xml.bind.annotation.*;

/**
 * @author xTz
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PortalDialog", propOrder = {
    "portalPath"
})
public class PortalDialog {

    @XmlElement(name = "portal_path")
    protected List<PortalPath> portalPath;
    @XmlAttribute(name = "npc_id")
    protected int npcId;
    @XmlAttribute(name = "siege_id")
    protected int siegeId;
    @XmlAttribute(name = "teleport_dialog_id")
    protected int teleportDialogId = 1011;

    public List<PortalPath> getPortalPath() {
        return portalPath;
    }

    public int getNpcId() {
        return npcId;
    }

    public void setNpcId(int value) {
        this.npcId = value;
    }

    public int getSiegeId() {
        return siegeId;
    }

    public void setSiegeId(int value) {
        this.siegeId = value;
    }
    
    public int getTeleportDialogId() {
      return teleportDialogId;
    }
}