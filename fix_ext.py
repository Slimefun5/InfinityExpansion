import os
import re

path = r'D:\Documents\GitHub\slimefun\InfinityExpansion\src\main\java\io\github\mooy1\infinityexpansion\items\SlimefunExtension.java'
c = open(path, 'r', encoding='utf-8').read()

# First replace all .item() to start clean
c = c.replace('.item()', '')

# List of materials used in SlimefunExtension recipes
materials = [
    'Materials.INFINITE_INGOT', 'Materials.VOID_INGOT', 'Materials.INFINITE_CIRCUIT',
    'SlimefunItems.ENERGIZED_CAPACITOR', 'Materials.REDSTONE_SINGULARITY',
    'Materials.MAGSTEEL', 'Materials.MAGSTEEL_PLATE', 'SlimefunItems.AUTO_ENCHANTER',
    'Materials.MACHINE_CIRCUIT', 'Materials.MACHINE_CORE', 'SlimefunItems.AUTO_DISENCHANTER',
    'ADVANCED_ENCHANTER'
]

for m in materials:
    c = c.replace(m + ',', m + '.item(),')
    c = c.replace(m + '\n', m + '.item()\n')
    c = c.replace(m + '\r', m + '.item()\r')
    c = c.replace(m + ')', m + '.item())')

for m in materials:
    c = c.replace(f'SlimefunItemStack {m}.item()', f'SlimefunItemStack {m}')

c = c.replace('public static final SlimefunItemStack ADVANCED_ENCHANTER.item()', 'public static final SlimefunItemStack ADVANCED_ENCHANTER')

open(path, 'w', encoding='utf-8').write(c)
print("SlimefunExtension done")
