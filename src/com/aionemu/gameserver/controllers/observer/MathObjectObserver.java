/*
 * Decompiled with CFR 0.150.
 */
package com.aionemu.gameserver.controllers.observer;

import java.util.concurrent.ScheduledFuture;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.math.MathObject;
import com.aionemu.gameserver.model.gameobjects.math.MathObjectType;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.skillengine.model.SkillTemplate;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.ThreadPoolManager;

public class MathObjectObserver extends ActionObserver {
	private final Creature creature;
	private final MathObject mathObject;
	private final MathObjectType type;
	private ScheduledFuture<?> shedules;
	private SkillTemplate template;

	public MathObjectObserver() {
		super(ObserverType.MOVE);
		this.creature = null;
		this.mathObject = null;
		this.type = MathObjectType.SKILL_USE;
	}

	public MathObjectObserver(MathObject mathObject, Creature creature, MathObjectType type) {
		super(ObserverType.MOVE);
		this.creature = creature;
		this.mathObject = mathObject;
		this.type = type;
		this.template = DataManager.SKILL_DATA.getSkillTemplate(mathObject.getSkillId());
	}

	@Override
	public void moved() {
		if (this.creature == null || this.mathObject == null) {
			return;
		}
		if (this.creature instanceof Player && !((Player) this.creature).isOnline()) {
			this.clearShedules();
			return;
		}
		if (this.creature.getLifeStats().isAlreadyDead()) {
			this.clearShedules();
			return;
		}
		double distance = MathUtil.getDistance(this.mathObject.getMaster(), this.creature);
		switch (this.type) {
		case SKILL_USE: {
			if (this.creature.getVisualState() == 20) {
				return;
			}
			if (distance >= this.mathObject.getMinRange() && distance <= this.mathObject.getMaxRange()) {
				if (this.shedules != null) {
					return;
				}
				this.onActionEvent();
				this.shedulesEvent();
				break;
			}
			this.clearShedules();
		}
		}
	}

	private void shedulesEvent() {
		int delay = this.template != null && this.template.getDuration() >= 1000 ? this.template.getDuration()
				: (this.mathObject.getDuration() >= 1000 ? this.mathObject.getDuration() : 1000);
		this.shedules = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				if (!MathObjectObserver.this.mathObject.isSpawned()) {
					MathObjectObserver.this.creature.getObserveController().removeObserver(MathObjectObserver.this);
					MathObjectObserver.this.clearShedules();
					return;
				}
				MathObjectObserver.this.onActionEvent();
			}
		}, delay, delay);
	}

	private void onActionEvent() {
		switch (this.type) {
		case SKILL_USE: {
			if (this.creature.getEffectController().hasAbnormalEffect(this.mathObject.getSkillId()))
				break;
			if (this.template == null) {
				return;
			}
			SkillEngine.getInstance().applyEffectDirectly(this.mathObject.getSkillId(), this.mathObject.getMaster(),
					this.creature, this.template.getDuration());
			break;
		}
		}
	}

	public void clearShedules() {
		if (this.shedules != null) {
			this.shedules.cancel(true);
			this.shedules = null;
		}
	}
}
