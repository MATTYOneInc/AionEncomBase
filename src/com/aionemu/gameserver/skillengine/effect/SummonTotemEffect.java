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
package com.aionemu.gameserver.skillengine.effect;

import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.NpcObjectType;
import com.aionemu.gameserver.model.gameobjects.Servant;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.spawnengine.VisibleObjectSpawner;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.ThreadPoolManager;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.util.concurrent.Future;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SummonTotemEffect")
public class SummonTotemEffect extends SummonServantEffect
{
    @Override
    public void applyEffect(Effect effect) {
        Creature effector = effect.getEffector();
        switch (effect.getSkillId()) {
            case 657:
			case 658:
			case 659:
			case 660:
			case 661:
			case 662:
                if (effect.getEffector().getTarget() == null) {
                    effect.getEffector().setTarget(effect.getEffector());
                }
                double radian = Math.toRadians(MathUtil.convertHeadingToDegree((byte) effect.getEffector().getHeading()));
                float x = effect.getX();
                float y = effect.getY();
                float z = effect.getZ();
                if (x == 0 && y == 0) {
                    Creature effected = effect.getEffected();
                    x = effected.getX() + (float) (Math.cos(radian) * 2);
                    y = effected.getY() + (float) (Math.sin(radian) * 2);
                    z = effected.getZ();
                }
                byte heading = effector.getHeading();
                int worldId = effector.getWorldId();
                int instanceId = effector.getInstanceId();
                SpawnTemplate spawn = SpawnEngine.addNewSingleTimeSpawn(worldId, npcId, x, y, z, heading);
                final Servant servant = VisibleObjectSpawner.spawnServant(spawn, instanceId, effector, effect.getSkillId(), effect.getSkillLevel(), NpcObjectType.SKILLAREA);
                Future<?> task = ThreadPoolManager.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        servant.getController().delete();
                    }
                }, time * 1000);
                servant.getController().addTask(TaskId.DESPAWN, task);
            return;
            default:
                float x1 = effector.getX();
                float y1 = effector.getY();
                float z1 = effector.getZ();
            spawnServant(effect, time, NpcObjectType.TOTEM, x1, y1, z1);
        }
    }
}