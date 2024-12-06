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
package admincommands;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.spawns.Spawn;
import com.aionemu.gameserver.model.templates.spawns.SpawnGroup2;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_FORCED_MOVE;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;
import com.aionemu.gameserver.world.knownlist.Visitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

public class FixNpc extends AdminCommand
{
	private static final Logger log = LoggerFactory.getLogger(FixNpc.class);
	private Npc npc = null;
    private int numofspawns = 0;
    private int spawned = 0;
    private Future<?> task = null;
	public FixNpc() {
		super("fixnpc");
	}
	
	@Override
	public void execute(final Player admin, String... params) {
		if (admin.getAccessLevel() < 5) {
            PacketSendUtility.sendMessage(admin, "You dont have enough rights to use this command!");
            return;
        } if (params.length == 0 && admin.getTarget() != null) {
			if (admin.getTarget() instanceof Npc) {
				final Npc target = (Npc) admin.getTarget();
				final SpawnTemplate temp = target.getSpawn();
                final float adminZ = admin.getZ();
                List<SpawnGroup2> spawnId = DataManager.SPAWNS_DATA2.getSpawnsByWorldId(admin.getWorldId());
                PacketSendUtility.sendMessage(admin, "SpawnId: " + spawnId);
                if (spawnId != null) {
                } for (final SpawnGroup2 spawn : spawnId) {
                	StringBuilder comment = new StringBuilder();
                    comment.append(target.getObjectTemplate().getName()).append(" (");
                    int isObject = target.getSpawn().getEntityId();
                    if (isObject > 0) {
                        comment.append("Object");
                    } else {
                        comment.append("Npc");
                    }
                    comment.append(" ").append(target.getObjectTemplate().getRank().name()).append(" ");
                    comment.append("lvl:").append(target.getLevel()).append(")");
                    int time = 9000;
                    task = ThreadPoolManager.getInstance().schedule(new Runnable() {
                        @Override
                        public void run() {
                        	SpawnTemplate spawn2 = SpawnEngine.addNewSpawn(admin.getWorldId(), spawn.getNpcId(), temp.getX(), temp.getY(), adminZ, temp.getHeading(), temp.getRespawnTime());
                        	VisibleObject visibleObject = SpawnEngine.spawnObject(spawn2, admin.getInstanceId());
							target.getController().delete();
                			try {
                				DataManager.SPAWNS_DATA2.saveSpawn(admin, visibleObject, false);
                			} catch (IOException e) {
                				e.printStackTrace();
                				PacketSendUtility.sendMessage(admin, "Could not save spawn");
                			}
                        }
                    }, time);
                    PacketSendUtility.sendMessage(admin, comment.toString() + " [Spawned] ");
                }
			} else {
                PacketSendUtility.sendMessage(admin, "Only instances of Npc are allowed as target!");
                return;
            }
		} else if ((params.length == 1 || params.length == 2) && "start".equalsIgnoreCase(params[0])) {
			int stop = 0;
            if (params.length == 1) {
                stop = -1;
            } else if (params.length == 2 && "start".equalsIgnoreCase(params[0])) {
                stop = Integer.parseInt(params[1]);
            }
            final Player admin2 = admin;
            List<SpawnGroup2> spawngroups = DataManager.SPAWNS_DATA2.getSpawnsByWorldId(admin2.getWorldId());
            List<SpawnTemplate> templates = new ArrayList<SpawnTemplate>();
            PacketSendUtility.sendMessage(admin2, "[Auto Spawn]: will start in 10 seconds.");
            for (final SpawnGroup2 spawngroup : spawngroups) {
                templates.addAll(spawngroup.getSpawnTemplates());
                numofspawns += spawngroup.getSpawnTemplates().size();
            }
            PacketSendUtility.sendMessage(admin2, "[Aprox Time]: " + ((numofspawns * 3.6) / 60) + " Minutes.");
            int time = 9000;
            int counter = 0;
            for (final SpawnTemplate template : templates) {
            	if (counter >= stop && stop >= 0) {
                    counter = 0;
                    break;
                }
                ++counter;
                time += 3000;
                task = ThreadPoolManager.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        TeleportService2.teleportTo(admin2, template.getWorldId(), template.getX(), template.getY(), template.getZ(), (byte) 0);
                        admin2.getKnownList().doOnAllNpcs(new Visitor<Npc>() {
                            @Override
                            public void visit(Npc n) {
                                if (MathUtil.getDistance((int) n.getX(), (int) n.getY(), (int) admin2.getX(), (int) admin2.getY()) < 3) {
                                    npc = n;
                                    return;
                                }
                            }
                        });
                    }
                }, time);
                time += 3000;
                task = ThreadPoolManager.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        if (npc != null) {
                            PacketSendUtility.broadcastPacketAndReceive(admin2, new SM_FORCED_MOVE(npc, admin2));
                        }
                    }
                }, time);
                time += 3000;
                task = ThreadPoolManager.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        if (npc != null) {
                            StringBuilder comment = new StringBuilder();
                            comment.append(npc.getObjectTemplate().getName()).append(" (");
                            int isObject = npc.getSpawn().getEntityId();
                            if (isObject != 0) {
                                comment.append("Object");
                            } else {
                                comment.append("Npc");
                            }
                            comment.append(" ").append(npc.getObjectTemplate().getRank().name()).append(" ");
                            comment.append("lvl:").append(npc.getLevel()).append(")");
                            Spawn spawnId = DataManager.SPAWNS_DATA2.getSpawnsForNpc(admin.getWorldId(), npc.getNpcId());
                            if (spawnId != null) {
                                log.info("[AUDIT] Deleted npc id=" + template.getNpcId() + ": //moveto " + template.getWorldId() + " " + template.getX() + " " + template.getY() + " " + template.getZ());
                            }
                            SpawnTemplate spawn2 = SpawnEngine.addNewSpawn(template.getWorldId(), template.getNpcId(), template.getX(), template.getY(), admin2.getZ(), template.getHeading(), template.getRespawnTime());
                            VisibleObject visibleObject = SpawnEngine.spawnObject(spawn2, admin.getInstanceId());
							npc.getController().delete();
                        	try {
                        		DataManager.SPAWNS_DATA2.saveSpawn(admin, visibleObject, false);
                        	} catch (IOException e) {
                        		e.printStackTrace();
                        		PacketSendUtility.sendMessage(admin, "Could not save spawn");
                        	}
                            ++spawned;
                            PacketSendUtility.sendMessage(admin2, spawned + ". " + comment.toString() + " spawned");
                            npc = null;
                        } else {
                            if (template != null) {
                                log.info("[AUDIT] Missing npc id=" + template.getNpcId() + ": //moveto " + template.getWorldId() + " " + template.getX() + " " + template.getY() + " " + template.getZ());
                            }
                        }
                    }
                }, time);
            }
            templates = null;
            spawngroups = null;
		} else if ((params.length == 1 || params.length == 2) && "stop".equalsIgnoreCase(params[0])) {
			if (task != null) {
				task.cancel(true);
				task = null;
	        }
		} else {
            PacketSendUtility.sendMessage(admin, "Syntax: //fixnpc <start> <counter>");
        }
        PacketSendUtility.sendMessage(admin, "[Number Of Spawns]: " + numofspawns);
	}
	
	@Override
	public void onFail(Player player, String message) {
		PacketSendUtility.sendMessage(player, "syntax //kill <target | all | <range>>");
	}
}