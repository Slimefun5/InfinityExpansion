import os
import subprocess

os.chdir(r'D:\Documents\GitHub\slimefun\InfinityExpansion')
subprocess.run(['git', 'checkout', 'src/main/java/io/github/mooy1/infinityexpansion/categories/InfinityGroup.java'])

p = r'D:\Documents\GitHub\slimefun\InfinityExpansion\src\main\java\io\github\mooy1\infinityexpansion\categories\InfinityGroup.java'
c = open(p, 'r', encoding='utf-8').read()

c = c.replace('CustomItemStack.create(ChestMenuUtils.getBackButton(', 'ChestMenuUtils.getBackButton(')
c = c.replace('guide.back.guide"))));', 'guide.back.guide")));')

# the original code was:
#        InfinityWorkbench.TYPE.sendRecipesTo((input, output) -> {
#            SlimefunItemStack sfStack = (SlimefunItemStack) output;
#            IDS.add(sfStack.getItemId());
#            ITEMS.put(sfStack.getItemId(), new Pair<>(sfStack, input));
#        });

c = c.replace('SlimefunItemStack sfStack = (SlimefunItemStack) output;', 'SlimefunItemStack sfStack = (SlimefunItemStack) io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem.getByItem(output).getItem();')

# We also need to fix .item() that we might have added earlier but we just did a git checkout, so InfinityGroup is fresh!
open(p, 'w', encoding='utf-8').write(c)

p2 = r'D:\Documents\GitHub\slimefun\InfinityExpansion\src\main\java\io\github\mooy1\infinityexpansion\items\machines\SingularityConstructor.java'
c2 = open(p2, 'r', encoding='utf-8').read()
c2 = c2.replace('new Recipe((SlimefunItemStack) itemStack, stacks[0], id, amt);', 'new Recipe((SlimefunItemStack) io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem.getByItem(itemStack).getItem(), stacks[0], id, amt);')
open(p2, 'w', encoding='utf-8').write(c2)

print("Done")
