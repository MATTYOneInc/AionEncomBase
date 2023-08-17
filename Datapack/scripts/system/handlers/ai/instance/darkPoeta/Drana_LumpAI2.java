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
package ai.instance.darkPoeta;

import ai.AggressiveNpcAI2;
import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.world.WorldMapInstance;

import java.util.concurrent.Future;

/****/
/** Author (Encom)
/****/

@AIName("drana_lump")
public class Drana_LumpAI2 extends AggressiveNpcAI2
{
	private Future<?> dranaBreakTask;
	
	@Override
	public void think() {
	}
	
	@Override
	protected void handleSpawned() {
		startDranaBreak();
		super.handleSpawned();
	}
	
	private void startDranaBreak() {
		final Npc spallerEchtra = getPosition().getWorldMapInstance().getNpc(214880); //Spaller Echtra.
		final Npc spallerRakanatra = getPosition().getWorldMapInstance().getNpc(215388); //Spaller Rakanatra.
		final Npc spallerDhatra = getPosition().getWorldMapInstance().getNpc(215389); //Spaller Dhatra.
		WorldMapInstance instance = getPosition().getWorldMapInstance();
		for (Player player: instance.getPlayersInside()) {
			if (MathUtil.isIn3dRange(player, spallerEchtra, 8)) {
				dranaBreak();
			} if (MathUtil.isIn3dRange(player, spallerRakanatra, 8)) {
				dranaBreak();
			} if (MathUtil.isIn3dRange(player, spallerDhatra, 8)) {
				dranaBreak();
			}
		}
	}
	
	private void dranaBreak() {
		dranaBreakTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				AI2Actions.targetCreature(Drana_LumpAI2.this, getPosition().getWorldMapInstance().getNpc(214880)); //Spaller Echtra.
				AI2Actions.targetCreature(Drana_LumpAI2.this, getPosition().getWorldMapInstance().getNpc(215388)); //Spaller Rakanatra.
				AI2Actions.targetCreature(Drana_LumpAI2.this, getPosition().getWorldMapInstance().getNpc(215389)); //Spaller Dhatra.
				AI2Actions.useSkill(Drana_LumpAI2.this, 18536); //Drana Break.
			}
		}, 1000, 6000);
	}
	
	@Override
	public boolean isMoveSupported() {
		return false;
	}
}