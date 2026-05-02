import os
import subprocess
import re

os.chdir(r'D:\Documents\GitHub\slimefun\InfinityExpansion')
subprocess.run(['git', 'checkout', 'src/main/java/io/github/mooy1/infinityexpansion/items/materials/Materials.java'])

path = 'src/main/java/io/github/mooy1/infinityexpansion/items/materials/Materials.java'
c = open(path, 'r', encoding='utf-8').read()

# Fix Singularity constructors
c = re.sub(r'new Singularity\(([A-Z_]+),\s*(SlimefunItems\.[A-Z_]+),\s*([0-9]+)\)', r'new Singularity(\1, \2.item(), \3)', c)

# Fix registerSmeltery to take .item() on arguments after the first
def fix_smeltery(match):
    prefix = match.group(1)
    args_str = match.group(2)
    args = [arg.strip() for arg in args_str.split(',')]
    new_args = [args[0]]
    for arg in args[1:]:
        if arg.startswith('SlimefunItems.') or (arg.isupper() and 'Material.' not in arg):
            arg += '.item()'
        new_args.append(arg)
    return prefix + ', '.join(new_args) + ');'

c = re.sub(r'(registerSmeltery\()([^;]+)\);', fix_smeltery, c)

# Recipes array
c = c.replace('COBBLE_1, COBBLE_1, COBBLE_1', 'COBBLE_1.item(), COBBLE_1.item(), COBBLE_1.item()')
c = c.replace('COBBLE_2, COBBLE_2, COBBLE_2', 'COBBLE_2.item(), COBBLE_2.item(), COBBLE_2.item()')
c = c.replace('COBBLE_3, COBBLE_3, COBBLE_3', 'COBBLE_3.item(), COBBLE_3.item(), COBBLE_3.item()')
c = c.replace('COBBLE_4, COBBLE_4, COBBLE_4', 'COBBLE_4.item(), COBBLE_4.item(), COBBLE_4.item()')
c = c.replace('VOID_BIT, VOID_BIT, VOID_BIT', 'VOID_BIT.item(), VOID_BIT.item(), VOID_BIT.item()')
c = c.replace('VOID_DUST, VOID_DUST, VOID_DUST', 'VOID_DUST.item(), VOID_DUST.item(), VOID_DUST.item()')
c = c.replace('MAGSTEEL, MAGSTEEL, MAGSTEEL', 'MAGSTEEL.item(), MAGSTEEL.item(), MAGSTEEL.item()')
c = c.replace('MAGSTEEL, SlimefunItems.HARDENED_METAL_INGOT, MAGSTEEL', 'MAGSTEEL.item(), SlimefunItems.HARDENED_METAL_INGOT.item(), MAGSTEEL.item()')
c = c.replace('SlimefunItems.COPPER_INGOT, SlimefunItems.ELECTRO_MAGNET, SlimefunItems.COPPER_INGOT', 'SlimefunItems.COPPER_INGOT.item(), SlimefunItems.ELECTRO_MAGNET.item(), SlimefunItems.COPPER_INGOT.item()')
c = c.replace('SlimefunItems.COPPER_INGOT, SlimefunItems.SILICON, SlimefunItems.COPPER_INGOT', 'SlimefunItems.COPPER_INGOT.item(), SlimefunItems.SILICON.item(), SlimefunItems.COPPER_INGOT.item()')
c = c.replace('Materials.MAGSTEEL, new ItemStack(Material.STRING), Materials.MAGSTEEL', 'Materials.MAGSTEEL.item(), new ItemStack(Material.STRING), Materials.MAGSTEEL.item()')
c = c.replace('new ItemStack(Material.STRING), BASIC_STRAINER, new ItemStack(Material.STRING)', 'new ItemStack(Material.STRING), BASIC_STRAINER.item(), new ItemStack(Material.STRING)')

open(path, 'w', encoding='utf-8').write(c)
print("Finished!")
