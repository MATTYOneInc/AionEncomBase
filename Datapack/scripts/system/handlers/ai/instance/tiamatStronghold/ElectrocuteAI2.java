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
package ai.instance.tiamatStronghold;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;

import java.util.concurrent.Future;

/****/
/** Author (Encom)
/****/

@AIName("electrocute")
public class ElectrocuteAI2 extends NpcAI2
{
	private Future<?> task;
	
	@Override
	public void think() {
	}
	
    @Override
    protected void handleSpawned() {
  	    super.handleSpawned();
		task = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				AI2Actions.useSkill(ElectrocuteAI2.this, 20757);
			}
		},0, 2000);
  	    despawn();
    }
	
    private void despawn() {
  	    ThreadPoolManager.getInstance().schedule(new Runnable() {
  		    @Override
  		    public void run() {
  			    getOwner().getController().onDelete();
  		    }
  	    }, 10000);
    }
	
	@Override
	public boolean isMoveSupported() {
		return false;
	}
	
	@Override
	public void handleDespawned() {
		task.cancel(true);
		super.handleDespawned();
	}
}