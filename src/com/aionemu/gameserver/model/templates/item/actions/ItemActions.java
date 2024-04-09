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
package com.aionemu.gameserver.model.templates.item.actions;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ItemActions")
public class ItemActions {
	@XmlElements({ @XmlElement(name = "skilllearn", type = SkillLearnAction.class),
			@XmlElement(name = "extract", type = ExtractAction.class),
			@XmlElement(name = "extractabyss", type = ExtractAbyssAction.class),
			@XmlElement(name = "extractexp", type = ExtractExpAction.class),
			@XmlElement(name = "idian", type = IdianAction.class),
			@XmlElement(name = "bonusexp", type = BonusAddExpAction.class),
			@XmlElement(name = "houselimit", type = HouseLimitAction.class),
			@XmlElement(name = "skilluse", type = SkillUseAction.class),
			@XmlElement(name = "enchant", type = EnchantItemAction.class),
			@XmlElement(name = "queststart", type = QuestStartAction.class),
			@XmlElement(name = "dye", type = DyeAction.class),
			@XmlElement(name = "craftlearn", type = CraftLearnAction.class),
			@XmlElement(name = "toypetspawn", type = ToyPetSpawnAction.class),
			@XmlElement(name = "disassemble", type = DisassemblyAction.class),
			@XmlElement(name = "titleadd", type = TitleAddAction.class),
			@XmlElement(name = "learnemotion", type = EmotionLearnAction.class),
			@XmlElement(name = "read", type = ReadAction.class),
			@XmlElement(name = "fireworkact", type = FireworksUseAction.class),
			@XmlElement(name = "instancetimeclear", type = InstanceTimeClear.class),
			@XmlElement(name = "expandinventory", type = ExpandInventoryAction.class),
			@XmlElement(name = "animation", type = AnimationAddAction.class),
			@XmlElement(name = "cosmetic", type = CosmeticItemAction.class),
			@XmlElement(name = "charge", type = ChargeAction.class),
			@XmlElement(name = "ride", type = RideAction.class),
			@XmlElement(name = "houseobject", type = SummonHouseObjectAction.class),
			@XmlElement(name = "housedeco", type = DecorateAction.class),
			@XmlElement(name = "assemble", type = AssemblyItemAction.class),
			@XmlElement(name = "adoptpet", type = AdoptPetAction.class),
			@XmlElement(name = "composition", type = CompositionAction.class),
			@XmlElement(name = "retuning", type = RetuningAction.class),
			@XmlElement(name = "wrapping", type = WrappingAction.class),
			@XmlElement(name = "f2p", type = F2pAction.class),
			@XmlElement(name = "tempering", type = TemperingAction.class),
			@XmlElement(name = "multireturn", type = MultiReturnAction.class),
			@XmlElement(name = "purifierexp", type = PurifierExpAction.class),
			@XmlElement(name = "unbinding", type = UnbindingAction.class),
			@XmlElement(name = "reductlevel", type = EquipedLevelAdjAction.class),
			@XmlElement(name = "unseal", type = UnSealAction.class),
			@XmlElement(name = "luna", type = LunaChestAction.class),
			@XmlElement(name = "enhance", type = EnhanceAction.class),
			@XmlElement(name = "enchant_stigma", type = EnchantStigmaAction.class),
			@XmlElement(name = "sweep", type = ShugoSweepAction.class),
			@XmlElement(name = "skill_skin", type = SkillAnimationAction.class),
			@XmlElement(name = "adoptminion", type = AdoptMinionAction.class),
	})
	protected List<AbstractItemAction> itemActions;

	public List<AbstractItemAction> getItemActions() {
		if (itemActions == null) {
			itemActions = new ArrayList<AbstractItemAction>();
		}
		return this.itemActions;
	}

	public List<ToyPetSpawnAction> getToyPetSpawnActions() {
		List<ToyPetSpawnAction> result = new ArrayList<ToyPetSpawnAction>();
		if (itemActions == null) {
			return result;
		}
		for (AbstractItemAction action : itemActions) {
			if (action instanceof ToyPetSpawnAction) {
				result.add((ToyPetSpawnAction) action);
			}
		}
		return result;
	}

	public EnchantItemAction getEnchantAction() {
		if (itemActions == null) {
			return null;
		}
		for (AbstractItemAction action : itemActions) {
			if ((action instanceof EnchantItemAction)) {
				return (EnchantItemAction) action;
			}
		}
		return null;
	}

	public EnchantStigmaAction getEnchantStigmaAction() {
		if (itemActions == null) {
			return null;
		}
		for (AbstractItemAction action : itemActions) {
			if ((action instanceof EnchantStigmaAction)) {
				return (EnchantStigmaAction) action;
			}
		}
		return null;
	}

	public SummonHouseObjectAction getHouseObjectAction() {
		if (itemActions == null) {
			return null;
		}
		for (AbstractItemAction action : itemActions) {
			if ((action instanceof SummonHouseObjectAction)) {
				return (SummonHouseObjectAction) action;
			}
		}
		return null;
	}

	public CraftLearnAction getCraftLearnAction() {
		if (itemActions == null) {
			return null;
		}
		for (AbstractItemAction action : itemActions) {
			if ((action instanceof CraftLearnAction)) {
				return (CraftLearnAction) action;
			}
		}
		return null;
	}

	public DecorateAction getDecorateAction() {
		if (itemActions == null) {
			return null;
		}
		for (AbstractItemAction action : itemActions) {
			if ((action instanceof DecorateAction)) {
				return (DecorateAction) action;
			}
		}
		return null;
	}

	public DyeAction getDyeAction() {
		if (itemActions == null) {
			return null;
		}
		for (AbstractItemAction action : itemActions) {
			if (action instanceof DyeAction) {
				return (DyeAction) action;
			}
		}
		return null;
	}

	public AdoptPetAction getAdoptPetAction() {
		if (itemActions == null) {
			return null;
		}
		for (AbstractItemAction action : itemActions) {
			if (action instanceof AdoptPetAction) {
				return (AdoptPetAction) action;
			}
		}
		return null;
	}

	public AdoptMinionAction getAdoptMinionAction() {
		if (itemActions == null) {
			return null;
		}
		for (AbstractItemAction action : itemActions) {
			if (action instanceof AdoptMinionAction) {
				return (AdoptMinionAction) action;
			}
		}
		return null;
	}

	public RetuningAction getTuningAction() {
		if (itemActions == null) {
			return null;
		}
		for (AbstractItemAction action : itemActions) {
			if (action instanceof RetuningAction) {
				return (RetuningAction) action;
			}
		}
		return null;
	}

	public IdianAction getPolishAction() {
		if (itemActions == null) {
			return null;
		}
		for (AbstractItemAction action : itemActions) {
			if (action instanceof IdianAction) {
				return (IdianAction) action;
			}
		}
		return null;
	}

	public TemperingAction getTempering() {
		if (itemActions == null) {
			return null;
		}
		for (AbstractItemAction action : itemActions) {
			if (action instanceof TemperingAction) {
				return (TemperingAction) action;
			}
		}
		return null;
	}

	public UnbindingAction getUnbinding() {
		if (itemActions == null) {
			return null;
		}
		for (AbstractItemAction action : itemActions) {
			if (action instanceof UnbindingAction) {
				return (UnbindingAction) action;
			}
		}
		return null;
	}
}
