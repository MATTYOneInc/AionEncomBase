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
package com.aionemu.gameserver.model.bonus_service;

import com.aionemu.gameserver.model.stats.calc.StatOwner;

/**
 * Created by wanke on 02/03/2017.
 */

public class BoostEventBonus implements StatOwner
{
   /*
   private static final Logger log = LoggerFactory.getLogger(BoostEventBonus.class);
    private List<IStatFunction> functions = new ArrayList<IStatFunction>();
    private BoostEvents boostBonusattr;
	
    public BoostEventBonus(int buffId) {
        boostBonusattr = DataManager.BOOST_EVENT_DATA.getInstanceBonusattr(buffId);
    }
	
    public void applyEffect(Player player, int buffId) {
        if (boostBonusattr == null) {
            return;
        } for (BonusPenaltyAttr BonusPenaltyAttr: boostBonusattr.getPenaltyAttr()) {
            if (BonusPenaltyAttr.getFunc().equals(Func.PERCENT)) {
                functions.add(new StatRateFunction(BonusPenaltyAttr.getStat(), BonusPenaltyAttr.getValue(), true));
            } else {
                functions.add(new StatAddFunction(BonusPenaltyAttr.getStat(), BonusPenaltyAttr.getValue(), true));
            }
        }
        player.getGameStats().addEffect(this, functions);
    }
	
    public void endEffect(Player player, int buffId) {
        functions.clear();
        player.getGameStats().endEffect(this);
    }*/
}