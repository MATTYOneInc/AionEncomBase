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
package com.aionemu.gameserver.model.gameobjects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.ai2.AI2;
import com.aionemu.gameserver.ai2.AI2Engine;
import com.aionemu.gameserver.controllers.CreatureController;
import com.aionemu.gameserver.controllers.ObserveController;
import com.aionemu.gameserver.controllers.attack.AggroList;
import com.aionemu.gameserver.controllers.effect.EffectController;
import com.aionemu.gameserver.controllers.movement.MoveController;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.TribeClass;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.state.CreatureSeeState;
import com.aionemu.gameserver.model.gameobjects.state.CreatureState;
import com.aionemu.gameserver.model.gameobjects.state.CreatureVisualState;
import com.aionemu.gameserver.model.stats.container.CreatureGameStats;
import com.aionemu.gameserver.model.stats.container.CreatureLifeStats;
import com.aionemu.gameserver.model.templates.VisibleObjectTemplate;
import com.aionemu.gameserver.model.templates.item.ItemAttackType;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.model.templates.zone.ZoneType;
import com.aionemu.gameserver.skillengine.effect.AbnormalState;
import com.aionemu.gameserver.skillengine.model.Skill;
import com.aionemu.gameserver.skillengine.model.SkillTemplate;
import com.aionemu.gameserver.taskmanager.tasks.PacketBroadcaster;
import com.aionemu.gameserver.taskmanager.tasks.PacketBroadcaster.BroadcastMode;
import com.aionemu.gameserver.world.MapRegion;
import com.aionemu.gameserver.world.WorldPosition;
import com.aionemu.gameserver.world.zone.ZoneName;

import javolution.util.FastMap;

public abstract class Creature extends VisibleObject {
	private static final Logger log = LoggerFactory.getLogger(Creature.class);
	protected AI2 ai2;
	private boolean isDespawnDelayed = false;
	private CreatureLifeStats<? extends Creature> lifeStats;
	private CreatureGameStats<? extends Creature> gameStats;
	private EffectController effectController;
	protected MoveController moveController;
	private int state = CreatureState.ACTIVE.getId();
	private int visualState = CreatureVisualState.VISIBLE.getId();
	private int seeState = CreatureSeeState.NORMAL.getId();
	private Skill castingSkill;
	private FastMap<Integer, Long> skillCoolDowns;
	private FastMap<Integer, Long> skillCoolDownsBase;
	private ObserveController observeController;
	private TransformModel transformModel;
	private final AggroList aggroList;
	private byte adminFlags = 0;
	private Item usingItem;
	private final transient byte[] zoneTypes = new byte[ZoneType.values().length];
	private int skillNumber;
	private int attackedCount;
	private long spawnTime = System.currentTimeMillis();
	private int PulledMulti = 1;

	/**
	 * @param objId
	 * @param controller
	 * @param spawnTemplate
	 * @param objectTemplate
	 * @param position
	 */
	public Creature(int objId, CreatureController<? extends Creature> controller, SpawnTemplate spawnTemplate,
			VisibleObjectTemplate objectTemplate, WorldPosition position) {
		super(objId, controller, spawnTemplate, objectTemplate, position);
		this.observeController = new ObserveController();
		this.setTransformModel(new TransformModel(this));
		if (spawnTemplate != null && spawnTemplate.getModel() != null) {
			if (spawnTemplate.getModel().getTribe() != null) {
				getTransformModel().setTribe(spawnTemplate.getModel().getTribe(), true);
			}
		}
		this.aggroList = createAggroList();
	}

	public MoveController getMoveController() {
		return this.moveController;
	}

	protected AggroList createAggroList() {
		return new AggroList(this);
	}

	/**
	 * Return CreatureController of this Creature object.
	 * 
	 * @return CreatureController.
	 */
	@Override
	public CreatureController<? extends Creature> getController() {
		return (CreatureController<?>) super.getController();
	}

	/**
	 * @return the lifeStats
	 */
	public CreatureLifeStats<? extends Creature> getLifeStats() {
		return lifeStats;
	}

	/**
	 * @param lifeStats the lifeStats to set
	 */
	public void setLifeStats(CreatureLifeStats<? extends Creature> lifeStats) {
		this.lifeStats = lifeStats;
	}

	/**
	 * @return the gameStats
	 */
	public CreatureGameStats<? extends Creature> getGameStats() {
		return gameStats;
	}

	/**
	 * @param gameStats the gameStats to set
	 */
	public void setGameStats(CreatureGameStats<? extends Creature> gameStats) {
		this.gameStats = gameStats;
	}

	public abstract byte getLevel();

	/**
	 * @return the effectController
	 */
	public EffectController getEffectController() {
		return effectController;
	}

	/**
	 * @param effectController the effectController to set
	 */
	public void setEffectController(EffectController effectController) {
		this.effectController = effectController;
	}

	public AI2 getAi2() {
		return ai2 != null ? ai2 : AI2Engine.getInstance().setupAI("dummy", this);
	}

	public void setAi2(AI2 ai2) {
		this.ai2 = ai2;
	}

	public boolean isDeleteDelayed() {
		return isDespawnDelayed;
	}

	public void setDespawnDelayed(boolean delayed) {
		isDespawnDelayed = delayed;
	}

	public boolean isFlag() {
		return false;
	}

	/**
	 * Is creature casting some skill
	 * 
	 * @return
	 */
	public boolean isCasting() {
		return castingSkill != null;
	}

	/**
	 * Set current casting skill or null when skill ends
	 * 
	 * @param castingSkill
	 */
	public void setCasting(Skill castingSkill) {
		if (castingSkill != null) {
			skillNumber++;
		}
		this.castingSkill = castingSkill;
	}

	/**
	 * Current casting skill id
	 * 
	 * @return
	 */
	public int getCastingSkillId() {
		return castingSkill != null ? castingSkill.getSkillTemplate().getSkillId() : 0;
	}

	/**
	 * Current casting skill
	 * 
	 * @return
	 */
	public Skill getCastingSkill() {
		return castingSkill;
	}

	public int getSkillNumber() {
		return skillNumber;
	}

	public void setSkillNumber(int skillNumber) {
		this.skillNumber = skillNumber;
	}

	public int getAttackedCount() {
		return this.attackedCount;
	}

	public void incrementAttackedCount() {
		this.attackedCount++;
	}

	public void clearAttackedCount() {
		attackedCount = 0;
	}

	/**
	 * Is using item
	 * 
	 * @return
	 */
	public boolean isUsingItem() {
		return usingItem != null;
	}

	/**
	 * Set using item
	 * 
	 * @param usingItem
	 */
	public void setUsingItem(Item usingItem) {
		this.usingItem = usingItem;
	}

	/**
	 * get Using ItemId
	 * 
	 * @return
	 */
	public int getUsingItemId() {
		return usingItem != null ? usingItem.getItemTemplate().getTemplateId() : 0;
	}

	/**
	 * Using Item
	 * 
	 * @return
	 */
	public Item getUsingItem() {
		return usingItem;
	}

	/**
	 * All abnormal effects are checked that disable movements
	 * 
	 * @return
	 */
	public boolean canPerformMove() {
		return !(getEffectController().isAbnormalState(AbnormalState.CANT_MOVE_STATE) || !isSpawned());
	}

	/**
	 * All abnormal effects are checked that disable attack
	 * 
	 * @return
	 */
	public boolean canAttack() {
		return !(getEffectController().isAbnormalState(AbnormalState.CANT_ATTACK_STATE) || isCasting()
				|| isInState(CreatureState.RESTING) || isInState(CreatureState.PRIVATE_SHOP));
	}

	/**
	 * @return state
	 */
	public int getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(CreatureState state) {
		this.state |= state.getId();
	}

	/**
	 * @param state taken usually from templates
	 */
	public void setState(int state) {
		this.state = state;
	}

	public void unsetState(CreatureState state) {
		this.state &= ~state.getId();
	}

	public boolean isInState(CreatureState state) {
		int isState = this.state & state.getId();

		if (isState == state.getId()) {
			return true;
		}
		return false;
	}

	/**
	 * @return visualState
	 */
	public int getVisualState() {
		return visualState;
	}

	/**
	 * @param visualState the visualState to set
	 */
	public void setVisualState(CreatureVisualState visualState) {
		this.visualState |= visualState.getId();
	}

	public void unsetVisualState(CreatureVisualState visualState) {
		this.visualState &= ~visualState.getId();
	}

	public boolean isInVisualState(CreatureVisualState visualState) {
		int isVisualState = this.visualState & visualState.getId();

		if (isVisualState == visualState.getId()) {
			return true;
		}
		return false;
	}

	/**
	 * @return seeState
	 */
	public int getSeeState() {
		return seeState;
	}

	/**
	 * @param seeState the seeState to set
	 */
	public void setSeeState(CreatureSeeState seeState) {
		this.seeState |= seeState.getId();
	}

	public void unsetSeeState(CreatureSeeState seeState) {
		this.seeState &= ~seeState.getId();
	}

	public boolean isInSeeState(CreatureSeeState seeState) {
		int isSeeState = this.seeState & seeState.getId();

		if (isSeeState == seeState.getId()) {
			return true;
		}
		return false;
	}

	/**
	 * @return the transformModel
	 */
	public TransformModel getTransformModel() {
		return transformModel;
	}

	/**
	 * @param transformModel the transformedModel to set
	 */
	public final void setTransformModel(TransformModel model) {
		this.transformModel = model;
	}

	/**
	 * @return the aggroList
	 */
	public final AggroList getAggroList() {
		return aggroList;
	}

	/**
	 * PacketBroadcasterMask
	 */
	private volatile byte packetBroadcastMask;

	/**
	 * This is adding broadcast to player.
	 */
	public final void addPacketBroadcastMask(BroadcastMode mode) {
		packetBroadcastMask |= mode.mask();

		PacketBroadcaster.getInstance().add(this);

		// Debug
		if (log.isDebugEnabled()) {
			log.debug("PacketBroadcaster: Packet " + mode.name() + " added to player " + this.getName());
		}
	}

	/**
	 * This is removing broadcast from player.
	 */
	public final void removePacketBroadcastMask(BroadcastMode mode) {
		packetBroadcastMask &= ~mode.mask();

		// Debug
		if (log.isDebugEnabled()) {
			log.debug("PacketBroadcaster: Packet " + mode.name() + " removed from player " + this.getName()); // fix
		}
	}

	/**
	 * Broadcast getter.
	 */
	public final byte getPacketBroadcastMask() {
		return packetBroadcastMask;
	}

	/**
	 * @return the observeController
	 */
	public ObserveController getObserveController() {
		return observeController;
	}

	/**
	 * Double dispatch like method
	 * 
	 * @param creature
	 * @return
	 */
	public boolean isEnemy(Creature creature) {
		return creature.isEnemyFrom(this);
	}

	/**
	 * @param creature
	 */
	public boolean isEnemyFrom(Creature creature) {
		return false;
	}

	/**
	 * @param player
	 * @return
	 */
	public boolean isEnemyFrom(Player player) {
		return false;
	}

	/**
	 * @param npc
	 * @return
	 */
	public boolean isEnemyFrom(Npc npc) {
		return false;
	}

	public TribeClass getTribe() {
		return TribeClass.GENERAL;
	}

	/**
	 * Double dispatch like method
	 * 
	 * @param creature
	 * @return
	 */
	public boolean isAggressiveTo(Creature creature) {
		return creature.isAggroFrom(this);
	}

	/**
	 * @param creature
	 * @return
	 */
	public boolean isAggroFrom(Creature creature) {
		return false;
	}

	/**
	 * @param npc
	 * @return
	 */
	public boolean isAggroFrom(Npc npc) {
		return false;
	}

	/**
	 * @param npc
	 * @return
	 */
	public boolean isHostileFrom(Npc npc) {
		return false;
	}

	/**
	 * @param npc
	 */
	public boolean isSupportFrom(Npc npc) {
		return false;
	}

	/**
	 * @param npc
	 */
	public boolean isFriendFrom(Npc npc) {
		return false;
	}

	/**
	 * @param visibleObject
	 * @return
	 */

	@Override
	public boolean canSee(Creature creature) {
		if (creature == null) {
			return false;
		}
		return creature.getVisualState() <= getSeeState();
	}

	public boolean isSeeObject(VisibleObject object) {
		return getKnownList().getVisibleObjects().containsKey(object.getObjectId());
	}

	public boolean isSeePlayer(Player player) {
		return getKnownList().getVisiblePlayers().containsKey(player.getObjectId());
	}

	/**
	 * @return NpcObjectType.NORMAL
	 */
	public NpcObjectType getNpcObjectType() {
		return NpcObjectType.NORMAL;
	}

	/**
	 * For summons and different kind of servants<br>
	 * it will return currently acting player.<br>
	 * This method is used for duel and enemy relations,<br>
	 * rewards<br>
	 * 
	 * @return Master of this creature or self
	 */
	public Creature getMaster() {
		return this;
	}

	/**
	 * For summons it will return summon object and for <br>
	 * servants - player object.<br>
	 * Used to find attackable target for npcs.<br>
	 * 
	 * @return acting master - player in case of servants
	 */
	public Creature getActingCreature() {
		return this;
	}

	/**
	 * @param delayId
	 * @return
	 */
	public boolean isSkillDisabled(SkillTemplate template) {

		if (skillCoolDowns == null) {
			return false;
		}
		int delayId = template.getDelayId();
		Long coolDown = skillCoolDowns.get(delayId);
		if (coolDown == null) {
			return false;
		}

		if (coolDown < System.currentTimeMillis()) {
			removeSkillCoolDown(delayId);
			return false;
		}

		/*
		 * Some shared cooldown skills have indipendent and different cooldown they must
		 * not be blocked
		 */
		if (skillCoolDownsBase != null && skillCoolDownsBase.get(delayId) != null) {
			if ((template.getDuration() + template.getCooldown() * 100 + skillCoolDownsBase.get(delayId)) < System
					.currentTimeMillis()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @param delayId
	 * @return
	 */
	public long getSkillCoolDown(int delayId) {
		if (skillCoolDowns == null || !skillCoolDowns.containsKey(delayId)) {
			return 0;
		}
		return skillCoolDowns.get(delayId);
	}

	/**
	 * @param delayId
	 * @param time
	 */
	public void setSkillCoolDown(int delayId, long time) {

		if (delayId == 0) {
			return;
		}

		if (skillCoolDowns == null) {
			skillCoolDowns = new FastMap<Integer, Long>().shared();
		}
		skillCoolDowns.put(delayId, time);
	}

	/**
	 * @return the skillCoolDowns
	 */
	public FastMap<Integer, Long> getSkillCoolDowns() {
		return skillCoolDowns;
	}

	/**
	 * @param delayId
	 */
	public void removeSkillCoolDown(int delayId) {
		if (skillCoolDowns == null) {
			return;
		}
		skillCoolDowns.remove(delayId);
		if (skillCoolDownsBase != null) {
			skillCoolDownsBase.remove(delayId);
		}
	}

	/**
	 * This function saves the currentMillis of skill that generated the cooldown of
	 * an entire cooldownGroup
	 * 
	 * @param delayId
	 * @param baseTime
	 */
	public void setSkillCoolDownBase(int delayId, long baseTime) {

		if (delayId == 0) {
			return;
		}

		if (skillCoolDownsBase == null) {
			skillCoolDownsBase = new FastMap<Integer, Long>().shared();
		}
		skillCoolDownsBase.put(delayId, baseTime);
	}

	/**
	 * @return isAdminNeutral value
	 */
	public int getAdminNeutral() {
		return adminFlags >> 4;
	}

	/**
	 * @param newValue
	 */
	public void setAdminNeutral(int newValue) {
		adminFlags = (byte) ((adminFlags & 0xF) | (newValue & 0xF) << 4);
	}

	/**
	 * @return isAdminEnmity value
	 */
	public int getAdminEnmity() {
		return adminFlags & 0xF;
	}

	/**
	 * @param newValue
	 */
	public void setAdminEnmity(int newValue) {
		adminFlags = (byte) ((adminFlags & 0xF0) | (newValue & 0xF));
	}

	public float getCollision() {
		return getObjectTemplate().getBoundRadius().getCollision();
	}

	/**
	 * @return
	 */
	public boolean isAttackableNpc() {
		return false;
	}

	public ItemAttackType getAttackType() {
		return ItemAttackType.PHYSICAL;
	}

	/**
	 * Creature is flying (FLY or GLIDE states)
	 */
	public boolean isFlying() {
		return (isInState(CreatureState.FLYING) && !isInState(CreatureState.RESTING))
				|| isInState(CreatureState.GLIDING);
	}

	public boolean isInFlyingState() {
		return isInState(CreatureState.FLYING) && !isInState(CreatureState.RESTING);
	}

	public byte isPlayer() {
		return 0;
	}

	public boolean isPhysClass(Creature creature) {
		if (creature instanceof Player) {
			switch (((Player) creature).getPlayerClass()) {
			case GLADIATOR:
			case TEMPLAR:
			case ASSASSIN:
			case RANGER:
			case CLERIC:
			case CHANTER:
				return true;
			default:
				return false;
			}
		}
		return false;
	}

	public boolean isMagicClass(Creature creature) {
		if (creature instanceof Player) {
			switch (((Player) creature).getPlayerClass()) {
			case SORCERER:
			case SPIRIT_MASTER:
			case AETHERTECH:
			case GUNSLINGER:
			case SONGWEAVER:
				return true;
			default:
				return false;
			}
		}
		return false;
	}

	public boolean isPvpTarget(Creature creature) {
		return getActingCreature() instanceof Player && creature.getActingCreature() instanceof Player;
	}

	public void revalidateZones() {
		MapRegion mapRegion = this.getPosition().getMapRegion();
		if (mapRegion != null) {
			mapRegion.revalidateZones(this);
		}
	}

	public boolean isInsideZone(ZoneName zoneName) {
		if (!isSpawned()) {
			return false;
		}
		return getPosition().getMapRegion().isInsideZone(zoneName, this);
	}

	public void setInsideZoneType(ZoneType zoneType) {
		byte current = zoneTypes[zoneType.getValue()];
		zoneTypes[zoneType.getValue()] = (byte) (current + 1);
	}

	public void unsetInsideZoneType(ZoneType zoneType) {
		byte current = zoneTypes[zoneType.getValue()];
		zoneTypes[zoneType.getValue()] = (byte) (current - 1);
	}

	public boolean isInsideZoneType(ZoneType zoneType) {
		return zoneTypes[zoneType.getValue()] > 0;
	}

	public Race getRace() {
		return Race.NONE;
	}

	public int getSkillCooldown(SkillTemplate template) {
		return template.getCooldown();
	}

	public int getItemCooldown(ItemTemplate template) {
		return template.getUseLimits().getDelayTime();
	}

	public boolean isNewSpawn() {
		return System.currentTimeMillis() - spawnTime < 1500;
	}

	public int getPulledMulti() {
		return PulledMulti;
	}

	public void setPulledMulti(int pulledMulti) {
		PulledMulti = pulledMulti;
	}
}