import os
import re

p = r'D:\Documents\GitHub\slimefun\InfinityExpansion\src\main\java\io\github\mooy1\infinityexpansion\categories\InfinityGroup.java'
c = open(p, 'r', encoding='utf-8').read()

# Fix CustomItemStack.create
def fix_back(m):
    return m.group(1)

c = re.sub(r'CustomItemStack\.create\((ChestMenuUtils\.getBackButton\([^)]+\)))\)', r'\1', c)
# The above regex: ChestMenuUtils.getBackButton( ... ) is group 1. But wait, `Slimefun.getLocalization().getMessage(player, "guide.back.guide")` has parentheses!
# Let's just do literal string replacement.
old = '''CustomItemStack.create(ChestMenuUtils.getBackButton(
                player, "", ChatColor.GRAY + Slimefun.getLocalization().getMessage(player, "guide.back.guide")))'''
new = '''ChestMenuUtils.getBackButton(
                player, "", ChatColor.GRAY + Slimefun.getLocalization().getMessage(player, "guide.back.guide"))'''
c = c.replace(old, new)
# wait, there's `player, ""` on the second line. In the powershell output it was:
#        menu.addItem(1, CustomItemStack.create(ChestMenuUtils.getBackButton(
#                player, "", ChatColor.GRAY + Slimefun.getLocalization().getMessage(player, "guide.back.guide"))));
c = c.replace('CustomItemStack.create(ChestMenuUtils.getBackButton(', 'ChestMenuUtils.getBackButton(')

# But wait, we need to remove the closing parenthesis of CustomItemStack.create. 
# `guide.back.guide"))));` -> `guide.back.guide")));`
c = c.replace('guide.back.guide"))));', 'guide.back.guide")));')
c = c.replace('guide.back.guide")));', 'guide.back.guide")));') # safe

open(p, 'w', encoding='utf-8').write(c)
