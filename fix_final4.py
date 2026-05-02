import os
import subprocess

p = r'D:\Documents\GitHub\slimefun\InfinityExpansion\src\main\java\io\github\mooy1\infinityexpansion\categories\InfinityGroup.java'
c = open(p, 'r', encoding='utf-8').read()
c = c.replace('SlimefunItemStack sfStack = (SlimefunItemStack) io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem.getByItem(output).getItem();', 
              'io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem sfItem = io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem.getByItem(output);\n            SlimefunItemStack sfStack = new SlimefunItemStack(sfItem.getId(), output);')

c = c.replace('menu.addItem(i, item.getFirstValue(),', 'menu.addItem(i, item.getFirstValue().item(),')
c = c.replace('menu.addItem(INFINITY_OUTPUT, pair.getFirstValue(),', 'menu.addItem(INFINITY_OUTPUT, pair.getFirstValue().item(),')

open(p, 'w', encoding='utf-8').write(c)

p2 = r'D:\Documents\GitHub\slimefun\InfinityExpansion\src\main\java\io\github\mooy1\infinityexpansion\items\machines\SingularityConstructor.java'
c2 = open(p2, 'r', encoding='utf-8').read()
c2 = c2.replace('new Recipe((SlimefunItemStack) io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem.getByItem(itemStack).getItem(), stacks[0], id, amt);',
                'new Recipe(new SlimefunItemStack(io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem.getByItem(itemStack).getId(), itemStack), stacks[0], id, amt);')

open(p2, 'w', encoding='utf-8').write(c2)
print("Done")
