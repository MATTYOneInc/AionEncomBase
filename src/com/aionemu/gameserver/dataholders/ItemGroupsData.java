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
package com.aionemu.gameserver.dataholders;

import com.aionemu.gameserver.model.templates.itemgroups.*;
import com.aionemu.gameserver.model.templates.pet.FoodType;
import com.aionemu.gameserver.model.templates.rewards.CraftItem;
import com.aionemu.gameserver.model.templates.rewards.CraftRecipe;
import com.aionemu.gameserver.model.templates.rewards.CraftReward;
import com.aionemu.gameserver.model.templates.rewards.IdLevelReward;
import javolution.util.FastMap;
import javolution.util.FastSet;
import org.apache.commons.lang.math.IntRange;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.util.*;

import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.selectDistinct;
import static ch.lambdaj.collection.LambdaCollections.with;

@XmlRootElement(name = "item_groups")
@XmlType(name = "", propOrder = {"craftMaterials", "craftShop", "craftBundles", "craftRecipes", "manastonesCommon", "manastonesRare", "manastonesLegend", "manastonesEpic", "medals", "foodCommon", "foodRare", "foodLegendary",
"medicineCommon", "medicineRare", "medicineLegendary", "oresRare", "oresUnique", "oresLegendary", "oresEpic", "gatherCommon", "gatherRare", "gatherUnique", "gatherLegendary", "gatherEpic", "enchants", "boss", "feedFluids", "feedArmor",
"feedThorns", "feedBones", "feedBalaurScales", "feedSouls", "feedExcludes", "stinkingJunk", "healthyFoodAll", "healthyFoodSpicy", "aetherPowderBiscuit", "aetherCrystalBiscuit", "aetherGemBiscuit",
"poppySnack", "poppySnackTasty", "poppySnackNutritious", "infernalDiabolAp", "innocentMerekXp", "shugoCoin", "newYearPetFood", "highCraftStep"})
@XmlAccessorType(XmlAccessType.NONE)
public class ItemGroupsData
{
	static int RECIPE_UPPER = 40;
	
	@XmlElement(name = "craft_materials")
	protected CraftItemGroup craftMaterials;
	
	@XmlElement(name = "craft_shop")
	protected CraftItemGroup craftShop;
	
	@XmlElement(name = "craft_bundles")
	protected CraftRecipeGroup craftBundles;
	
	@XmlElement(name = "craft_recipes")
	protected CraftRecipeGroup craftRecipes;
	
	@XmlElement(name = "manastones_common")
	protected ManastoneGroup manastonesCommon;
	
	@XmlElement(name = "manastones_rare")
	protected ManastoneGroup manastonesRare;
	
	@XmlElement(name = "manastones_legend")
	protected ManastoneGroup manastonesLegend;
	
	@XmlElement(name = "manastones_epic")
	protected ManastoneGroup manastonesEpic;
	
	@XmlElement(name = "medals")
	protected MedalGroup medals;
	
	@XmlElement(name = "food_common")
	protected FoodGroup foodCommon;
	
	@XmlElement(name = "food_rare")
	protected FoodGroup foodRare;
	
	@XmlElement(name = "food_legendary")
	protected FoodGroup foodLegendary;
	
	@XmlElement(name = "medicine_common")
	protected MedicineGroup medicineCommon;
	
	@XmlElement(name = "medicine_rare")
	protected MedicineGroup medicineRare;
	
	@XmlElement(name = "medicine_legendary")
	protected MedicineGroup medicineLegendary;
	
	//Ores
	@XmlElement(name = "ores_rare")
	protected OreGroup oresRare;
	@XmlElement(name = "ores_unique")
	protected OreGroup oresUnique;
	@XmlElement(name = "ores_legendary")
	protected OreGroup oresLegendary;
	@XmlElement(name = "ores_epic")
	protected OreGroup oresEpic;
	
	//Gatherable
	@XmlElement(name = "gather_common")
	protected GatherGroup gatherCommon;
	@XmlElement(name = "gather_rare")
	protected GatherGroup gatherRare;
	@XmlElement(name = "gather_unique")
	protected GatherGroup gatherUnique;
	@XmlElement(name = "gather_legendary")
	protected GatherGroup gatherLegendary;
	@XmlElement(name = "gather_epic")
	protected GatherGroup gatherEpic;
	
	@XmlElement(name = "enchants")
	protected EnchantGroup enchants;
	
	@XmlElement(name = "boss")
	protected BossGroup boss;
	
	@XmlElement(name = "feed_fluid")
	protected FeedGroups.FeedFluidGroup feedFluids;
	
	@XmlElement(name = "feed_armor")
	protected FeedGroups.FeedArmorGroup feedArmor;
	
	@XmlElement(name = "feed_thorn")
	protected FeedGroups.FeedThornGroup feedThorns;
	
	@XmlElement(name = "feed_bone")
	protected FeedGroups.FeedBoneGroup feedBones;
	
	@XmlElement(name = "feed_balaur_material")
	protected FeedGroups.FeedBalaurGroup feedBalaurScales;
	
	@XmlElement(name = "feed_soul")
	protected FeedGroups.FeedSoulGroup feedSouls;
	
	@XmlElement(name = "feed_exclude")
	protected FeedGroups.FeedExcludeGroup feedExcludes;
	
	@XmlElement(name = "stinking_junk")
	protected FeedGroups.StinkingJunkGroup stinkingJunk;
	
	@XmlElement(name = "feed_healthy_all")
	protected FeedGroups.HealthyFoodAllGroup healthyFoodAll;
	
	@XmlElement(name = "feed_healthy_spicy")
	protected FeedGroups.HealthyFoodSpicyGroup healthyFoodSpicy;
	
	@XmlElement(name = "feed_powder_biscuit")
	protected FeedGroups.AetherPowderBiscuitGroup aetherPowderBiscuit;
	
	@XmlElement(name = "feed_crystal_biscuit")
	protected FeedGroups.AetherCrystalBiscuitGroup aetherCrystalBiscuit;
	
	@XmlElement(name = "feed_gem_biscuit")
	protected FeedGroups.AetherGemBiscuitGroup aetherGemBiscuit;
	
	@XmlElement(name = "poppy_snack")
	protected FeedGroups.PoppySnackGroup poppySnack;
	
	@XmlElement(name = "tasty_poppy_snack")
	protected FeedGroups.PoppySnackTastyGroup poppySnackTasty;
	
	@XmlElement(name = "nutritious_poppy_snack")
	protected FeedGroups.PoppySnackNutritiousGroup poppySnackNutritious;
	
	@XmlElement(name = "infernal_diabol_ap")
	protected FeedGroups.InfernalDiabolApGroup infernalDiabolAp;
	
	@XmlElement(name = "innocent_merek_xp")
	protected FeedGroups.InnocentMerekXpGroup innocentMerekXp;
	
	@XmlElement(name = "shugo_coin")
	protected FeedGroups.ShugoCoinGroup shugoCoin;
	
	@XmlElement(name = "new_year_pet_food")
	protected FeedGroups.NewYearPetFoodGroup newYearPetFood;
	
	@XmlElement(name = "high_craft_step")
	protected FeedGroups.HighCraftStepGroup highCraftStep;
	
	FastMap<Integer, FastMap<IntRange, List<CraftReward>>> craftMaterialsBySkill = new FastMap<Integer, FastMap<IntRange, List<CraftReward>>>();
	FastMap<Integer, FastMap<IntRange, List<CraftReward>>> craftShopBySkill = new FastMap<Integer, FastMap<IntRange, List<CraftReward>>>();
	FastMap<Integer, FastMap<IntRange, List<CraftReward>>> craftBundlesBySkill = new FastMap<Integer, FastMap<IntRange, List<CraftReward>>>();
	FastMap<Integer, FastMap<IntRange, List<CraftReward>>> craftRecipesBySkill = new FastMap<Integer, FastMap<IntRange, List<CraftReward>>>();
	
	BonusItemGroup[] craftGroups;
	BonusItemGroup[] manastoneGroups;
	BonusItemGroup[] medalGroups;
	BonusItemGroup[] foodGroups;
	BonusItemGroup[] medicineGroups;
	BonusItemGroup[] oreGroups;
	BonusItemGroup[] gatherGroups;
	BonusItemGroup[] enchantGroups;
	BonusItemGroup[] bossGroups;
	Map<FoodType, FastSet<Integer>> petFood = new HashMap<FoodType, FastSet<Integer>>();
	
	private int count = 0;
	private int petFoodCount = 0;
	
	void afterUnmarshal(Unmarshaller u, Object parent) {
		for (CraftItem item: craftMaterials.getItems()) {
			MapCraftReward(craftMaterialsBySkill, item);
		}
		count += craftMaterials.getItems().size();
		craftMaterials.getItems().clear();
		craftMaterials.setDataHolder(craftMaterialsBySkill);
		for (CraftItem item: craftShop.getItems()) {
			MapCraftReward(craftShopBySkill, item);
		}
		count += craftShop.getItems().size();
		craftShop.getItems().clear();
		craftShop.setDataHolder(craftShopBySkill);
		for (CraftRecipe recipe: craftBundles.getItems()) {
			MapCraftReward(craftBundlesBySkill, recipe);
		}
		count += craftBundles.getItems().size();
		craftBundles.getItems().clear();
		craftBundles.setDataHolder(craftBundlesBySkill);
		for (CraftRecipe recipe: craftRecipes.getItems()) {
			MapCraftReward(craftRecipesBySkill, recipe);
		}
		count += craftRecipes.getItems().size();
		craftRecipes.getItems().clear();
		craftRecipes.setDataHolder(craftRecipesBySkill);
		craftGroups = new BonusItemGroup[] {craftMaterials, craftShop, craftBundles, craftRecipes};
		manastoneGroups = new BonusItemGroup[] {manastonesCommon, manastonesRare, manastonesLegend, manastonesEpic};
		medalGroups = new BonusItemGroup[] {medals};
		foodGroups = new BonusItemGroup[] {foodCommon, foodRare, foodLegendary};
		medicineGroups = new BonusItemGroup[] {medicineCommon, medicineRare, medicineLegendary};
		oreGroups = new BonusItemGroup[] {oresRare, oresUnique, oresLegendary, oresEpic};
		gatherGroups = new BonusItemGroup[] {gatherCommon, gatherRare, gatherUnique, gatherLegendary, gatherEpic};
		enchantGroups = new BonusItemGroup[] {enchants};
		bossGroups = new BonusItemGroup[] {boss};
		for (FoodType foodType: FoodType.values()) {
			List<ItemRaceEntry> food = getPetFood(foodType);
			if (food != null) {
				FastSet<Integer> itemIds = FastSet.newInstance();
				itemIds.addAll(selectDistinct(with(food).extract(on(ItemRaceEntry.class).getId())));
				petFood.put(foodType, itemIds);
				if (foodType != FoodType.EXCLUDES && foodType != FoodType.STINKY) {
					petFoodCount += itemIds.size();
				}
				food.clear();
			}
		}
	}
	
	void MapCraftReward(FastMap<Integer, FastMap<IntRange, List<CraftReward>>> dataHolder, CraftReward reward) {
		int lowerBound = 0, upperBound = 0;
		if (reward instanceof CraftRecipe) {
			CraftRecipe recipe = (CraftRecipe) reward;
			lowerBound = recipe.getLevel();
			upperBound = lowerBound + RECIPE_UPPER;
			if (upperBound / 100 != lowerBound / 100) {
				upperBound = lowerBound / 100 + 99;
			}
		} else {
			CraftItem item = (CraftItem) reward;
			lowerBound = item.getMinLevel();
			upperBound = item.getMaxLevel();
		}
		IntRange range = new IntRange(lowerBound, upperBound);
		FastMap<IntRange, List<CraftReward>> ranges;
		if (dataHolder.containsKey(reward.getSkill())) {
			ranges = dataHolder.get(reward.getSkill());
		} else {
			ranges = new FastMap<IntRange, List<CraftReward>>();
			dataHolder.put(reward.getSkill(), ranges);
		}
		List<CraftReward> items;
		if (ranges.containsKey(range)) {
			items = ranges.get(range);
		} else {
			items = new ArrayList<CraftReward>();
			ranges.put(range, items);
		}
		items.add(reward);
	}
	
	public Collection<CraftReward> getCraftMaterials(int skillId) {
		if (craftMaterialsBySkill.containsKey(skillId)) {
			return Collections.emptyList();
		}
		List<CraftReward> result = new ArrayList<CraftReward>();
		for (List<CraftReward> items: craftMaterialsBySkill.get(skillId).values()) {
			result.addAll(items);
		}
		return result;
	}
	
	public float getCraftMaterialsChance() {
		return craftMaterials.getChance();
	}
	
	public Collection<CraftReward> getCraftShopItems(int skillId) {
		if (craftShopBySkill.containsKey(skillId)) {
			return Collections.emptyList();
		}
		List<CraftReward> result = new ArrayList<CraftReward>();
		for (List<CraftReward> items: craftShopBySkill.get(skillId).values()) {
			result.addAll(items);
		}
		return result;
	}
	
	public float getCraftShopItemsChance() {
		return craftShop.getChance();
	}
	
	public Collection<CraftReward> getCraftBundles(int skillId) {
		if (craftBundlesBySkill.containsKey(skillId)) {
			return Collections.emptyList();
		}
		List<CraftReward> result = new ArrayList<CraftReward>();
		for (List<CraftReward> items: craftBundlesBySkill.get(skillId).values()) {
			result.addAll(items);
		}
		return result;
	}
	
	public float getCraftBundlesChance() {
		return craftBundles.getChance();
	}
	
	public Collection<CraftReward> getCraftRecipes(int skillId) {
		if (craftRecipesBySkill.containsKey(skillId)) {
			return Collections.emptyList();
		}
		List<CraftReward> result = new ArrayList<CraftReward>();
		for (List<CraftReward> items: craftRecipesBySkill.get(skillId).values()) {
			result.addAll(items);
		}
		return result;
	}
	
	public float getCraftRecipesChance() {
		return craftRecipes.getChance();
	}
	
	public Collection<ItemRaceEntry> getManastonesCommon() {
		return manastonesCommon.getItems();
	}
	
	public float getManastonesCommonChance() {
		return manastonesCommon.getChance();
	}
	
	public Collection<ItemRaceEntry> getManastonesRare() {
		return manastonesRare.getItems();
	}
	
	public float getManastonesRareChance() {
		return manastonesRare.getChance();
	}
	
	public Collection<ItemRaceEntry> getManastonesLegend() {
		return manastonesLegend.getItems();
	}
	
	public float getManastonesLegendChance() {
		return manastonesLegend.getChance();
	}
	
	public Collection<ItemRaceEntry> getManastonesEpic() {
		return manastonesEpic.getItems();
	}
	
	public float getManastonesEpicChance() {
		return manastonesEpic.getChance();
	}
	
	public Collection<IdLevelReward> getFoodCommon() {
		return foodCommon.getItems();
	}
	
	public float getFoodCommonChance() {
		return foodCommon.getChance();
	}
	
	public Collection<IdLevelReward> getFoodRare() {
		return foodRare.getItems();
	}
	
	public float getFoodRareChance() {
		return foodRare.getChance();
	}
	
	public Collection<IdLevelReward> getFoodLegendary() {
		return foodLegendary.getItems();
	}
	
	public float getFoodLegendaryChance() {
		return foodLegendary.getChance();
	}
	
	public Collection<IdLevelReward> getMedicineCommon() {
		return medicineCommon.getItems();
	}
	
	public float getMedicineCommonChance() {
		return medicineCommon.getChance();
	}
	
	public Collection<IdLevelReward> getMedicineRare() {
		return medicineRare.getItems();
	}
	
	public float getMedicineRareChance() {
		return medicineRare.getChance();
	}
	
	public Collection<IdLevelReward> getMedicineLegendary() {
		return medicineLegendary.getItems();
	}
	
	public float getMedicineLegendaryChance() {
		return medicineLegendary.getChance();
	}
	//Ores.
	public Collection<ItemRaceEntry> getOresRare() {
		return oresRare.getItems();
	}
	public float getOresRareChance() {
		return oresRare.getChance();
	}
	public Collection<ItemRaceEntry> getOresUnique() {
		return oresUnique.getItems();
	}
	public float getOresUniqueChance() {
		return oresUnique.getChance();
	}
	public Collection<ItemRaceEntry> getOresLegendary() {
		return oresLegendary.getItems();
	}
	public float getOresLegendaryChance() {
		return oresLegendary.getChance();
	}
	public Collection<ItemRaceEntry> getOresEpic() {
		return oresEpic.getItems();
	}
	public float getOresEpicChance() {
		return oresEpic.getChance();
	}
	
	//Gatherable.
	public Collection<ItemRaceEntry> getGatherCommon() {
		return gatherCommon.getItems();
	}
	public float getGatherCommonChance() {
		return gatherCommon.getChance();
	}
	public Collection<ItemRaceEntry> getGatherRare() {
		return gatherRare.getItems();
	}
	public float getGatherRareChance() {
		return gatherRare.getChance();
	}
	public Collection<ItemRaceEntry> getGatherUnique() {
		return gatherUnique.getItems();
	}
	public float getGatherUniqueChance() {
		return gatherUnique.getChance();
	}
	public Collection<ItemRaceEntry> getGatherLegendary() {
		return gatherLegendary.getItems();
	}
	public float getGatherLegendaryChance() {
		return gatherLegendary.getChance();
	}
	public Collection<ItemRaceEntry> getGatherEpic() {
		return gatherEpic.getItems();
	}
	public float getGatherEpicChance() {
		return gatherEpic.getChance();
	}
	
	public Collection<IdLevelReward> getEnchants() {
		return enchants.getItems();
	}
	
	public float getEnchantsChance() {
		return enchants.getChance();
	}
	
	public Collection<ItemRaceEntry> getBoss() {
		return boss.getItems();
	}
	public float getBossChance() {
		return boss.getChance();
	}
	
	public CraftItemGroup getCraftMaterials() {
		return craftMaterials;
	}
	
	public CraftItemGroup getCraftShop() {
		return craftShop;
	}
	
	public CraftRecipeGroup getCraftBundles() {
		return craftBundles;
	}
	
	public CraftRecipeGroup getCraftRecipes() {
		return craftRecipes;
	}
	
	public BonusItemGroup[] getCraftGroups() {
		return craftGroups;
	}
	
	public BonusItemGroup[] getManastoneGroups() {
		return manastoneGroups;
	}
	
	public BonusItemGroup[] getMedalGroups() {
		return medalGroups;
	}
	
	public BonusItemGroup[] getFoodGroups() {
		return foodGroups;
	}
	
	public BonusItemGroup[] getMedicineGroups() {
		return medicineGroups;
	}
	
	public BonusItemGroup[] getOreGroups() {
		return oreGroups;
	}
	
	public BonusItemGroup[] getGatherGroups() {
		return gatherGroups;
	}
	
	public BonusItemGroup[] getEnchantGroups() {
		return enchantGroups;
	}
	
	public BonusItemGroup[] getBossGroups() {
		return bossGroups;
	}
	
	public boolean isFood(int itemId, FoodType foodType) {
		FastSet<Integer> food = petFood.get(FoodType.EXCLUDES);
		if (food.contains(itemId)) {
			return false;
		}
		food = petFood.get(FoodType.STINKY);
		if (food.contains(itemId)) {
			return false;
		} if (foodType != FoodType.MISCELLANEOUS) {
			food = petFood.get(foodType);
			return food.contains(itemId);
		}
		food = petFood.get(FoodType.ARMOR);
		if (food.contains(itemId)) {
			return true;
		}
		food = petFood.get(FoodType.BALAUR_SCALES);
		if (food.contains(itemId)) {
			return true;
		}
		food = petFood.get(FoodType.BONES);
		if (food.contains(itemId)) {
			return true;
		}
		food = petFood.get(FoodType.FLUIDS);
		if (food.contains(itemId)) {
			return true;
		}
		food = petFood.get(FoodType.SOULS);
		if (food.contains(itemId)) {
			return true;
		}
		food = petFood.get(FoodType.THORNS);
		if (food.contains(itemId)) {
			return true;
		}
		return false;
	}
	
	private List<ItemRaceEntry> getPetFood(FoodType foodType) {
		switch (foodType) {
			case AETHER_CRYSTAL_BISCUIT:
				return aetherCrystalBiscuit.getItems();
			case AETHER_GEM_BISCUIT:
				return aetherGemBiscuit.getItems();
			case AETHER_POWDER_BISCUIT:
				return aetherPowderBiscuit.getItems();
			case ARMOR:
				return feedArmor.getItems();
			case BALAUR_SCALES:
				return feedBalaurScales.getItems();
			case BONES:
				return feedBones.getItems();
			case FLUIDS:
				return feedFluids.getItems();
			case SOULS:
				return feedSouls.getItems();
			case THORNS:
				return feedThorns.getItems();
			case HIGH_CRAFT_STEP:
				return highCraftStep.getItems();
			case HEALTHY_FOOD_ALL:
				return healthyFoodAll.getItems();
			case HEALTHY_FOOD_SPICY:
				return healthyFoodSpicy.getItems();
			case POPPY_SNACK:
				return poppySnack.getItems();
			case POPPY_SNACK_TASTY:
				return poppySnackTasty.getItems();
			case POPPY_SNACK_NUTRITIOUS:
				return poppySnackNutritious.getItems();
			case INFERNAL_DIABOL_AP:
				return infernalDiabolAp.getItems();
			case INNOCENT_MEREK_XP:
				return innocentMerekXp.getItems();	
			case SHUGO_COIN:
				return shugoCoin.getItems();
			case NEW_YEAR_PET_FOOD:
				return newYearPetFood.getItems();
			case STINKY:
				return stinkingJunk.getItems();
			case EXCLUDES:
				return feedExcludes.getItems();
		}
		return null;
	}
	
	public int bonusSize() {
		return count +
		manastonesCommon.getItems().size() +
		manastonesRare.getItems().size() +
		manastonesLegend.getItems().size() +
		manastonesEpic.getItems().size() +
		foodCommon.getItems().size() +
		foodRare.getItems().size() +
		foodLegendary.getItems().size() +
		medicineCommon.getItems().size() +
		medicineRare.getItems().size() +
		medicineLegendary.getItems().size() +
		oresRare.getItems().size() +
		oresUnique.getItems().size() +
		oresLegendary.getItems().size() +
		oresEpic.getItems().size() +
		gatherCommon.getItems().size() +
		gatherRare.getItems().size() +
		gatherUnique.getItems().size() +
		gatherLegendary.getItems().size() +
		gatherEpic.getItems().size() +
		enchants.getItems().size() +
		boss.getItems().size();
	}
	
	public int petFoodSize() {
		return petFoodCount;
	}
}