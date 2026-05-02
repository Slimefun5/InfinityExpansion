import os
import subprocess
import re

os.chdir(r'D:\Documents\GitHub\slimefun\InfinityExpansion')
subprocess.run(['git', 'checkout', 'src/main/java/io/github/mooy1/infinityexpansion/items/SlimefunExtension.java'])

path = 'src/main/java/io/github/mooy1/infinityexpansion/items/SlimefunExtension.java'
c = open(path, 'r', encoding='utf-8').read()

c = c.replace('null, Materials.INFINITE_INGOT, Materials.VOID_INGOT, Materials.VOID_INGOT, Materials.INFINITE_INGOT, null',
              'null, Materials.INFINITE_INGOT.item(), Materials.VOID_INGOT.item(), Materials.VOID_INGOT.item(), Materials.INFINITE_INGOT.item(), null')
c = c.replace('null, Materials.INFINITE_INGOT, Materials.INFINITE_CIRCUIT, Materials.INFINITE_CIRCUIT, Materials.INFINITE_INGOT, null',
              'null, Materials.INFINITE_INGOT.item(), Materials.INFINITE_CIRCUIT.item(), Materials.INFINITE_CIRCUIT.item(), Materials.INFINITE_INGOT.item(), null')
c = c.replace('null, Materials.INFINITE_INGOT, SlimefunItems.ENERGIZED_CAPACITOR, SlimefunItems.ENERGIZED_CAPACITOR, Materials.INFINITE_INGOT, null',
              'null, Materials.INFINITE_INGOT.item(), SlimefunItems.ENERGIZED_CAPACITOR.item(), SlimefunItems.ENERGIZED_CAPACITOR.item(), Materials.INFINITE_INGOT.item(), null')
c = c.replace('Materials.VOID_INGOT, Materials.REDSTONE_SINGULARITY, Materials.VOID_INGOT',
              'Materials.VOID_INGOT.item(), Materials.REDSTONE_SINGULARITY.item(), Materials.VOID_INGOT.item()')
c = c.replace('Materials.VOID_INGOT, SlimefunItems.ENERGIZED_CAPACITOR, Materials.VOID_INGOT',
              'Materials.VOID_INGOT.item(), SlimefunItems.ENERGIZED_CAPACITOR.item(), Materials.VOID_INGOT.item()')
c = c.replace('Materials.MAGSTEEL, Materials.MAGSTEEL, Materials.MAGSTEEL',
              'Materials.MAGSTEEL.item(), Materials.MAGSTEEL.item(), Materials.MAGSTEEL.item()')
c = c.replace('Materials.MAGSTEEL_PLATE, SlimefunItems.AUTO_ENCHANTER, Materials.MAGSTEEL_PLATE',
              'Materials.MAGSTEEL_PLATE.item(), SlimefunItems.AUTO_ENCHANTER.item(), Materials.MAGSTEEL_PLATE.item()')
c = c.replace('Materials.MACHINE_CIRCUIT, Materials.MACHINE_CORE, Materials.MACHINE_CIRCUIT',
              'Materials.MACHINE_CIRCUIT.item(), Materials.MACHINE_CORE.item(), Materials.MACHINE_CIRCUIT.item()')
c = c.replace('Materials.MAGSTEEL_PLATE, SlimefunItems.AUTO_DISENCHANTER, Materials.MAGSTEEL_PLATE',
              'Materials.MAGSTEEL_PLATE.item(), SlimefunItems.AUTO_DISENCHANTER.item(), Materials.MAGSTEEL_PLATE.item()')
c = c.replace('Materials.VOID_INGOT, null, null, null, null, Materials.VOID_INGOT',
              'Materials.VOID_INGOT.item(), null, null, null, null, Materials.VOID_INGOT.item()')
c = c.replace('Materials.VOID_INGOT, Materials.VOID_INGOT, ADVANCED_ENCHANTER, ADVANCED_ENCHANTER, Materials.VOID_INGOT, Materials.VOID_INGOT',
              'Materials.VOID_INGOT.item(), Materials.VOID_INGOT.item(), ADVANCED_ENCHANTER.item(), ADVANCED_ENCHANTER.item(), Materials.VOID_INGOT.item(), Materials.VOID_INGOT.item()')

open(path, 'w', encoding='utf-8').write(c)
print("Finished!")
