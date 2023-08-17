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
package com.aionemu.gameserver.services.abysslandingservice.landingspecialservice;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.AbyssSpecialLandingDAO;
import com.aionemu.gameserver.model.landing_special.LandingSpecialLocation;
import com.aionemu.gameserver.model.landing_special.LandingSpecialStateType;

public class SPLanding  extends SpecialLanding<LandingSpecialLocation>
{
    public SPLanding(LandingSpecialLocation landing) {
        super(landing);
    }
	
    @Override
    public void startLanding() {
        getSpecialLandingLocation().setActiveLanding(this);
        if (!getSpecialLandingLocation().getSpawned().isEmpty()) {
            despawn();
        }
        spawn(LandingSpecialStateType.ACTIVE);
    }
	
	public void saveLanding() {
        DAOManager.getDAO(AbyssSpecialLandingDAO.class).updateLocation(getSpecialLandingLocation());
    }
	
    @Override
    public void stopLanding() {
        getSpecialLandingLocation().setActiveLanding(null);
        despawn();
        spawn(LandingSpecialStateType.NO_ACTIVE);
    }
}