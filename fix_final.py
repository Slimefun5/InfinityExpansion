import os
import re

print("Starting fix_final")

# StorageForge.java
p3 = r'D:\Documents\GitHub\slimefun\InfinityExpansion\src\main\java\io\github\mooy1\infinityexpansion\items\storage\StorageForge.java'
c3 = open(p3, 'r', encoding='utf-8').read()
old = 'new MachineRecipeType("storage_forge", Storage.STORAGE_FORGE);'
new = 'new MachineRecipeType("storage_forge", Storage.STORAGE_FORGE.item());'
if old in c3:
    c3 = c3.replace(old, new)
    open(p3, 'w', encoding='utf-8').write(c3)
    print("Fixed StorageForge")
else:
    print("StorageForge target not found")

# SingularityConstructor.java
p2 = r'D:\Documents\GitHub\slimefun\InfinityExpansion\src\main\java\io\github\mooy1\infinityexpansion\items\machines\SingularityConstructor.java'
c2 = open(p2, 'r', encoding='utf-8').read()
c2 = re.sub(r'(public static final RecipeType TYPE = new RecipeType\([^,]+,\s*)(SlimefunItems\.ENERGIZED_GALACTIC_ALLOY)', r'\1\2.item()', c2)
open(p2, 'w', encoding='utf-8').write(c2)
print("Fixed SingularityConstructor")

# InfinityGroup.java
p = r'D:\Documents\GitHub\slimefun\InfinityExpansion\src\main\java\io\github\mooy1\infinityexpansion\categories\InfinityGroup.java'
c = open(p, 'r', encoding='utf-8').read()
c = c.replace('IDS.add(sfStack.getItemId());', 'IDS.add(io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem.getByItem(sfStack).getId());')
c = c.replace('ITEMS.put(sfStack.getItemId(),', 'ITEMS.put(io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem.getByItem(sfStack).getId(),')

# and the ChestMenuUtils.getBackButton error
c = c.replace('CustomItemStack.create(ChestMenuUtils.getBackButton(', 'ChestMenuUtils.getBackButton(')
c = c.replace('guide.back.guide"))));', 'guide.back.guide")));')
open(p, 'w', encoding='utf-8').write(c)
print("Fixed InfinityGroup")
