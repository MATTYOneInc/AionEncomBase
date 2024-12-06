/*
 * This file is part of Encom.
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
package ai;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.state.CreatureVisualState;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAYER_STATE;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.skillengine.effect.AbnormalState;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Rinzler (Encom)
 */

@AIName("invisible_npc")
public class InvisibleNpcAI2 extends AggressiveNpcAI2
{
	private boolean canThink = true;
	
	@Override
	public boolean canThink() {
		return canThink;
	}
	
	@Override
	protected void handleSpawned() {
        super.handleSpawned();
		switch (getNpcId()) {
			case 214833:
			case 214891:
			case 215225:
			case 215228:
			case 215231:
		        anuhartBravery();
			break;
		} switch (getNpcId()) {
            case 219878:
            case 219884:
            case 219891:
            case 219897:
            case 219903:
            case 219910:
            case 219917:
            case 219923:
			case 234772:
			case 234773:
			case 234790:
			case 234791:
			case 234808:
			case 234809:
			case 234820:
			case 234821:
			case 234832:
			case 234833:
			case 234844:
			case 234845:
			case 234856:
			case 234857:
			case 234868:
			case 234869:
			case 234880:
			case 234881:
			case 234892:
			case 234893:
			case 234904:
			case 234905:
			case 234922:
			case 234923:
			case 234958:
			case 234959:
			case 236026:
			case 236032:
			case 236039:
			case 236045:
			case 236052:
			case 236059:
			case 236065:
			case 236071:
			case 256695:
			case 256696:
			case 256707:
			case 256708:
			case 256719:
			case 256720:
			case 256731:
			case 256732:
			case 257030:
			case 257031:
			case 257120:
			case 257121:
			case 257165:
			case 257166:
			case 257330:
			case 257331:
			case 257420:
			case 257421:
			case 257465:
			case 257466:
			case 257630:
			case 257631:
			case 257720:
			case 257721:
			case 257765:
			case 257766:
			case 257930:
			case 257931:
			case 258020:
			case 258021:
			case 258065:
			case 258066:
			case 279177:
			case 279184:
			case 279275:
			case 279284:
			case 279471:
			case 279478:
			case 279569:
			case 279578:
			case 883080:
			case 883086:
			case 883092:
			case 883098:
			case 883104:
			case 883110:
			case 883116:
			case 883122:
			case 883128:
			case 883134:
			case 883140:
			case 883146:
			case 883152:
			case 883158:
			case 883164:
			case 883170:
			case 883176:
			case 883182:
			case 883188:
			case 883194:
			case 883200:
			case 883206:
			case 883212:
			case 883218:
			case 883224:
			case 883230:
			case 883236:
			case 883242:
			case 883248:
			case 883254:
			case 883260:
			case 883266:
				conquerorPassion();
			break;
		} switch (getNpcId()) {
		    case 230792:
			case 230793:
			case 233277:
			case 233285:
			case 233334:
			case 233335:
			case 235577:
			case 235580:
			case 235581:
			case 235603:
			case 235606:
			case 235607:
			    midnightRobe();
			break;
		} switch (getNpcId()) {
		    case 235565:
			case 235568:
			case 235591:
			case 235594:
			    firmBelief();
			break;
		} switch (getNpcId()) {
			case 882451:
			case 882616:
			case 882617:
			case 882781:
			case 882782:
			case 882984:
			case 882990:
			case 882996:
			case 883002:
			case 883008:
			case 883014:
			case 883056:
			case 883062:
			case 883068:
			case 883074:
				ereshkigalRage();
			break;
		} switch (getNpcId()) {
			case 234771:
			case 234789:
			case 234807:
			case 234819:
			case 234831:
			case 234843:
			case 234855:
			case 234867:
			case 234879:
			case 234891:
			case 234903:
			case 235175:
			case 235181:
			case 883020:
			case 883026:
			case 883032:
			case 883038:
			case 883044:
			case 883050:
			    brokenMorale();
			break;
		}
		getOwner().getEffectController().setAbnormal(AbnormalState.HIDE.getId());
		getOwner().setVisualState(CreatureVisualState.HIDE1);
		PacketSendUtility.broadcastPacket(getOwner(), new SM_PLAYER_STATE(getOwner()));
	}
	
    @Override
    protected void handleAttack(Creature creature) {
        super.handleAttack(creature);
		getOwner().getEffectController().setAbnormal(AbnormalState.HIDE.getId());
		getOwner().unsetVisualState(CreatureVisualState.HIDE1);
		PacketSendUtility.broadcastPacket(getOwner(), new SM_PLAYER_STATE(getOwner()));
    }
    
    @Override
	protected void handleTargetGiveup() {
    	super.handleTargetGiveup();
		getOwner().getEffectController().setAbnormal(AbnormalState.HIDE.getId());
		getOwner().setVisualState(CreatureVisualState.HIDE1);
		PacketSendUtility.broadcastPacket(getOwner(), new SM_PLAYER_STATE(getOwner()));
	}
	
	private void anuhartBravery() {
	    SkillEngine.getInstance().getSkill(getOwner(), 18168, 1, getOwner()).useNoAnimationSkill(); //Anuhart's Bravery.
	}
	private void conquerorPassion() {
		SkillEngine.getInstance().getSkill(getOwner(), 20665, 1, getOwner()).useNoAnimationSkill(); //Conqueror's Passion.
	}
	private void midnightRobe() {
		SkillEngine.getInstance().getSkill(getOwner(), 20700, 1, getOwner()).useNoAnimationSkill(); //Midnight Robe.
	}
	private void firmBelief() {
		SkillEngine.getInstance().getSkill(getOwner(), 20962, 1, getOwner()).useNoAnimationSkill(); //Firm Belief.
	}
	private void ereshkigalRage() {
	    SkillEngine.getInstance().getSkill(getOwner(), 22682, 1, getOwner()).useNoAnimationSkill(); //Ereshkigal Rage.
	}
	private void brokenMorale() {
		SkillEngine.getInstance().getSkill(getOwner(), 22791, 1, getOwner()).useNoAnimationSkill(); //Broken Morale.
	}
	
	@Override
    protected void handleDied() {
        super.handleDied();
	}
}