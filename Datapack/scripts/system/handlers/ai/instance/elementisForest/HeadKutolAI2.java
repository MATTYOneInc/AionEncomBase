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
package ai.instance.elementisForest;

import ai.AggressiveNpcAI2;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;

/**
 * @author Romanz
 */
@AIName("kutol")
public class HeadKutolAI2 extends AggressiveNpcAI2 {

    @Override
    protected void handleAttack(Creature creature) {
        super.handleAttack(creature);

        if (Rnd.get(1, 100) < 1) {
            spawnClone();
        }
    }

    private void spawnClone() {
        Npc KutolClone = getPosition().getWorldMapInstance().getNpc(282302);
        int random = Rnd.get(1, 3);
        if (KutolClone == null) {
            switch (random) {
                case 1:
                    spawn(282302, getOwner().getX(), getOwner().getY(), getOwner().getZ() + 2, (byte) 3);
                    break;
                case 2:
                    spawn(282302, getOwner().getX(), getOwner().getY(), getOwner().getZ() + 2, (byte) 3);
                    spawn(282302, getOwner().getX() - 5, getOwner().getY() - 3, getOwner().getZ() + 2, (byte) 3);
                    break;
                default:
                    spawn(282302, getOwner().getX(), getOwner().getY(), getOwner().getZ() + 2, (byte) 3);
                    spawn(282302, getOwner().getX() - 5, getOwner().getY() - 3, getOwner().getZ() + 2, (byte) 3);
                    spawn(282302, getOwner().getX() + 5, getOwner().getY() - 3, getOwner().getZ() + 2, (byte) 3);
                    break;
            }
        }
    }
}