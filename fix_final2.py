import os

# InfinityGroup.java
p = r'D:\Documents\GitHub\slimefun\InfinityExpansion\src\main\java\io\github\mooy1\infinityexpansion\categories\InfinityGroup.java'
c = open(p, 'r', encoding='utf-8').read()
c = c.replace('Pair<SlimefunItemStack, ItemStack[]>', 'Pair<ItemStack, ItemStack[]>')
open(p, 'w', encoding='utf-8').write(c)

# SingularityConstructor.java
p2 = r'D:\Documents\GitHub\slimefun\InfinityExpansion\src\main\java\io\github\mooy1\infinityexpansion\items\machines\SingularityConstructor.java'
c2 = open(p2, 'r', encoding='utf-8').read()
c2 = c2.replace('Machines.SINGULARITY_CONSTRUCTOR, (stacks, itemStack) -> {', 'Machines.SINGULARITY_CONSTRUCTOR.item(), (stacks, itemStack) -> {')
open(p2, 'w', encoding='utf-8').write(c2)
