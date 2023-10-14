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
package com.aionemu.gameserver.services.outpost;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.ai2.AbstractAI;
import com.aionemu.gameserver.ai2.eventcallback.OnDieEventCallback;
import com.aionemu.gameserver.dao.OutpostDAO;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.AionObject;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.team2.TemporaryPlayerTeam;
import com.aionemu.gameserver.services.OutpostService;

/**
 * Created by Wnkrz on 27/08/2017.
 */

public class OutpostBossDeathListener extends OnDieEventCallback
{
    private final Outpost<?> outpost;
	
    public OutpostBossDeathListener(Outpost outpost) {
        this.outpost = outpost;
    }
	
    @Override
    public void onBeforeDie(AbstractAI obj) {
        AionObject winner = outpost.getBoss().getAggroList().getMostDamage();
        if (winner instanceof Creature) {
            final Creature kill = (Creature) winner;
            if (kill.getRace().isPlayerRace()) {
                outpost.setRace(kill.getRace());
            }
        } else if (winner instanceof TemporaryPlayerTeam) {
            final TemporaryPlayerTeam team = (TemporaryPlayerTeam) winner;
            if (team.getRace().isPlayerRace()) {
                outpost.setRace(team.getRace());
            }
        } else {
            outpost.setRace(Race.NPC);
        }
        OutpostService.getInstance().capture(outpost.getId(), outpost.getRace());
    }
	
    @Override
    public void onAfterDie(AbstractAI obj) {
    }
	
    private OutpostDAO getDAO() {
        return DAOManager.getDAO(OutpostDAO.class);
    }
}