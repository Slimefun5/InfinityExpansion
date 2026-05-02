import os
import re

base = r'D:\Documents\GitHub\slimefun\InfinityExpansion\src\main\java\io\github\mooy1\infinityexpansion'

# 1. InfinityGroup.java
p = os.path.join(base, 'categories', 'InfinityGroup.java')
c = open(p, 'r', encoding='utf-8').read()
c = c.replace('SlimefunItemStack sfStack = (SlimefunItemStack) output;', 'ItemStack sfStack = output;')
c = re.sub(r'CustomItemStack\.create\(ChestMenuUtils\.getBackButton\([\s\S]*?\"\"\)\)', r'ChestMenuUtils.getBackButton(this.mainMenu.getPlayer(), "")', c)
c = c.replace('menu.addItem(i, item.getFirstValue(), (p, slot, item1, action) -> {', 'menu.addItem(i, item.getFirstValue().item(), (p, slot, item1, action) -> {')
c = c.replace('menu.addItem(INFINITY_OUTPUT, pair.getFirstValue(), ChestMenuUtils.getEmptyClickHandler());', 'menu.addItem(INFINITY_OUTPUT, pair.getFirstValue().item(), ChestMenuUtils.getEmptyClickHandler());')
open(p, 'w', encoding='utf-8').write(c)

# 2. ResourceSynthesizer.java
p = os.path.join(base, 'items', 'machines', 'ResourceSynthesizer.java')
c = open(p, 'r', encoding='utf-8').read()
c = c.replace('items.add(this.recipes[i]);', 'items.add(this.recipes[i].item());')
c = c.replace('items.add(this.recipes[i + 1]);', 'items.add(this.recipes[i + 1].item());')
c = c.replace('items.add(this.recipes[i + 2]);', 'items.add(this.recipes[i + 2].item());')
c = c.replace('recipe = this.recipes[i + 2];', 'recipe = this.recipes[i + 2].item();')
open(p, 'w', encoding='utf-8').write(c)

# 3. SingularityConstructor.java
p = os.path.join(base, 'items', 'machines', 'SingularityConstructor.java')
c = open(p, 'r', encoding='utf-8').read()
c = c.replace('RecipeType(InfinityExpansion.createKey("singularity_constructor"),\r\n                                          SlimefunItems.ENERGIZED_GALACTIC_ALLOY', 'RecipeType(InfinityExpansion.createKey("singularity_constructor"),\r\n                                          SlimefunItems.ENERGIZED_GALACTIC_ALLOY.item()')
c = c.replace('RecipeType(InfinityExpansion.createKey("singularity_constructor"),\n                                          SlimefunItems.ENERGIZED_GALACTIC_ALLOY', 'RecipeType(InfinityExpansion.createKey("singularity_constructor"),\n                                          SlimefunItems.ENERGIZED_GALACTIC_ALLOY.item()')
c = c.replace('menu.fits(triplet.output, OUTPUT_SLOT)', 'menu.fits(triplet.output.item(), OUTPUT_SLOT)')
c = c.replace('menu.pushItem(triplet.output.clone(), OUTPUT_SLOT);', 'menu.pushItem(triplet.output.item().clone(), OUTPUT_SLOT);')
c = c.replace('items.add(recipe.output);', 'items.add(recipe.output.item());')
open(p, 'w', encoding='utf-8').write(c)

# 4. VoidHarvester.java
p = os.path.join(base, 'items', 'machines', 'VoidHarvester.java')
c = open(p, 'r', encoding='utf-8').read()
c = c.replace('ItemStack output = Materials.VOID_BIT;', 'ItemStack output = Materials.VOID_BIT.item();')
c = c.replace('items.add(Materials.VOID_BIT);', 'items.add(Materials.VOID_BIT.item());')
open(p, 'w', encoding='utf-8').write(c)

# 5. Singularity.java
p = os.path.join(base, 'items', 'materials', 'Singularity.java')
c = open(p, 'r', encoding='utf-8').read()
c = c.replace('makeRecipe(recipe, (int) (amount * COST_MULTIPLIER)));', 'makeRecipe(recipe.item(), (int) (amount * COST_MULTIPLIER)));')
open(p, 'w', encoding='utf-8').write(c)

# 6. MobDataInfuser.java
p = os.path.join(base, 'items', 'mobdata', 'MobDataInfuser.java')
c = open(p, 'r', encoding='utf-8').read()
c = c.replace('new MachineRecipeType("mob_data_infuser", MobData.INFUSER);', 'new MachineRecipeType("mob_data_infuser", MobData.INFUSER.item());')
open(p, 'w', encoding='utf-8').write(c)

# 7. Oscillator.java
p = os.path.join(base, 'items', 'quarries', 'Oscillator.java')
c = open(p, 'r', encoding='utf-8').read()
c = c.replace('Materials.MACHINE_PLATE', 'Materials.MACHINE_PLATE.item()')
c = c.replace('SlimefunItems.BLISTERING_INGOT_3', 'SlimefunItems.BLISTERING_INGOT_3.item()')
c = c.replace('.item().item()', '.item()')
open(p, 'w', encoding='utf-8').write(c)

# 8. Researches.java
p = os.path.join(base, 'items', 'Researches.java')
c = open(p, 'r', encoding='utf-8').read()
c = c.replace('.addItems(items)', '.addItems(java.util.Arrays.stream(items).map(io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack::item).toArray(org.bukkit.inventory.ItemStack[]::new))')
open(p, 'w', encoding='utf-8').write(c)

# 9. SlimefunExtension.java
p = os.path.join(base, 'items', 'SlimefunExtension.java')
c = open(p, 'r', encoding='utf-8').read()
c = c.replace('SlimefunExtension.ADVANCED_GEO_ENERGY.item()', 'SlimefunExtension.ADVANCED_GEO_ENERGY')
c = c.replace('SlimefunExtension.ADVANCED_CHARGER_ENERGY.item()', 'SlimefunExtension.ADVANCED_CHARGER_ENERGY')
c = c.replace('SlimefunExtension.INFINITY_CHARGER_ENERGY.item()', 'SlimefunExtension.INFINITY_CHARGER_ENERGY')
c = c.replace('SlimefunExtension.STAR_ENERGY.item()', 'SlimefunExtension.STAR_ENERGY')
c = c.replace('SlimefunExtension.ADVANCED_EN_ENERGY.item()', 'SlimefunExtension.ADVANCED_EN_ENERGY')
c = c.replace('SlimefunExtension.ADVANCED_DIS_ENERGY.item()', 'SlimefunExtension.ADVANCED_DIS_ENERGY')
c = c.replace('SlimefunExtension.INFINITY_EN_ENERGY.item()', 'SlimefunExtension.INFINITY_EN_ENERGY')
c = c.replace('SlimefunExtension.INFINITY_DIS_ENERGY.item()', 'SlimefunExtension.INFINITY_DIS_ENERGY')
open(p, 'w', encoding='utf-8').write(c)

# 10. StorageUnit.java
p = os.path.join(base, 'items', 'storage', 'StorageUnit.java')
c = open(p, 'r', encoding='utf-8').read()
c = c.replace('cache.amount(data.getSecondValue());', 'cache.setAmount(data.getSecondValue());')
open(p, 'w', encoding='utf-8').write(c)

# 11. Materials.java
p = os.path.join(base, 'items', 'materials', 'Materials.java')
c = open(p, 'r', encoding='utf-8').read()
c = re.sub(r'new Singularity\(([A-Z_]+),\s*(SlimefunItems\.[A-Z_]+)\.item\(\),\s*([0-9]+)\)', r'new Singularity(\1, \2, \3)', c)
open(p, 'w', encoding='utf-8').write(c)

print("Final replace done")
