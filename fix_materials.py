import os
import re

path = r'D:\Documents\GitHub\slimefun\InfinityExpansion\src\main\java\io\github\mooy1\infinityexpansion\items\materials\Materials.java'
c = open(path, 'r', encoding='utf-8').read()

# First replace all .item() to start clean
c = c.replace('.item()', '')

# Singularity constructors
c = re.sub(r'new Singularity\(([A-Z_0-9]+),\s*(SlimefunItems\.[A-Z_]+),\s*([0-9]+)\)', r'new Singularity(\1, \2.item(), \3)', c)

# Recipes and array definitions
# There are 9-item recipes, 36-item recipes
# For any uppercase word that isn't preceded by `Material.` or `RecipeType.` or `MachineRecipeType.` and isn't followed by `=`, we could append `.item()`.
# A safer way: list all the materials explicitly used in recipes.
materials = [
    'COBBLE_1', 'COBBLE_2', 'COBBLE_3', 'COBBLE_4', 'VOID_BIT', 'VOID_DUST', 'MAGSTEEL',
    'TITANIUM', 'MACHINE_CIRCUIT', 'MACHINE_PLATE', 'MACHINE_CORE',
    'INFINITE_INGOT', 'VOID_INGOT', 'MAGSTEEL_PLATE', 'ADVANCED_STRAINER', 'BASIC_STRAINER',
    'EARTH_SINGULARITY', 'MYTHRIL', 'FORTUNE_SINGULARITY', 'MAGIC_SINGULARITY', 'METAL_SINGULARITY',
    'GOLD_SINGULARITY', 'DIAMOND_SINGULARITY', 'EMERALD_SINGULARITY', 'NETHERITE_SINGULARITY', 'ADAMANTITE',
    'REDSTONE_SINGULARITY', 'LAPIS_SINGULARITY', 'QUARTZ_SINGULARITY', 'MAGNESIUM_SINGULARITY', 'MAGNONIUM',
    'COAL_SINGULARITY', 'IRON_SINGULARITY', 'COPPER_SINGULARITY', 'LEAD_SINGULARITY',
    'SILVER_SINGULARITY', 'ALUMINUM_SINGULARITY', 'TIN_SINGULARITY', 'ZINC_SINGULARITY',
    'ENDER_ESSENCE'
]
# Add SlimefunItems ones
sf_items = [
    'MAGNESIUM_INGOT', 'STEEL_INGOT', 'MAGNESIUM_DUST', 'REINFORCED_ALLOY_INGOT',
    'DAMASCUS_STEEL_INGOT', 'HARDENED_METAL_INGOT', 'REDSTONE_ALLOY', 'COPPER_INGOT',
    'ELECTRO_MAGNET', 'SILICON', 'REINFORCED_PLATE'
]
materials.extend(['SlimefunItems.' + x for x in sf_items])

# But they should only have .item() if they are in an array `{ ... }` or in a method call (except the first arg of registerSmeltery).
# Easiest way: just replace them globally and then fix the declarations and first args!
for m in materials:
    # replace 'm,' with 'm.item(),'
    c = c.replace(m + ',', m + '.item(),')
    # replace 'm\n' with 'm.item()\n'
    c = c.replace(m + '\n', m + '.item()\n')
    # replace 'm\r' with 'm.item()\r'
    c = c.replace(m + '\r', m + '.item()\r')
    # replace 'm)' with 'm.item())'
    c = c.replace(m + ')', m + '.item())')

# Now fix the false positives:
# 1. Declarations: public static final SlimefunItemStack COBBLE_1.item() = ...
for m in materials:
    c = c.replace(f'SlimefunItemStack {m}.item()', f'SlimefunItemStack {m}')

# 2. registerSmeltery first argument
# The pattern is: registerSmeltery(SOME_MAT.item(),
c = re.sub(r'registerSmeltery\(([A-Z_]+)\.item\(\),', r'registerSmeltery(\1,', c)

open(path, 'w', encoding='utf-8').write(c)
print("Materials done")
