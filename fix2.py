import os
import re

base = r'D:\Documents\GitHub\slimefun\InfinityExpansion\src\main\java\io\github\mooy1\infinityexpansion'

def process(filepath, is_materials, is_extension):
    c = open(filepath, 'r', encoding='utf-8').read()
    # Strip all .item() entirely
    c = c.replace('.item()', '')

    if is_materials:
        # Singularity constructors
        c = re.sub(r'new Singularity\(([A-Z_]+),\s*(SlimefunItems\.[A-Z_]+),\s*([0-9]+)\)', r'new Singularity(\1, \2.item(), \3)', c)
        
        # fix registerSmeltery
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
        
        # known recipes
        recipes = [
            'COBBLE_1', 'COBBLE_2', 'COBBLE_3', 'COBBLE_4', 'VOID_BIT', 'VOID_DUST', 'MAGSTEEL',
            'Materials.MAGSTEEL', 'SlimefunItems.HARDENED_METAL_INGOT', 'SlimefunItems.COPPER_INGOT', 
            'SlimefunItems.ELECTRO_MAGNET', 'SlimefunItems.SILICON', 'BASIC_STRAINER'
        ]
        # Regex to find these items when followed by comma or brace, to avoid breaking other things
        # But honestly string replace on the recipe patterns is safer
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
        
    if is_extension:
        # In SlimefunExtension, any array creation for recipes needs .item()
        # Like Materials.INFINITE_INGOT, Materials.VOID_INGOT, etc.
        # We can find all RecipeType usages maybe?
        
        # Let's just do known lists since it's 100 errors.
        reps = [
            'Materials.INFINITE_INGOT', 'Materials.VOID_INGOT', 'Materials.INFINITE_CIRCUIT',
            'SlimefunItems.ENERGIZED_CAPACITOR', 'Materials.REDSTONE_SINGULARITY',
            'Materials.MAGSTEEL', 'Materials.MAGSTEEL_PLATE', 'SlimefunItems.AUTO_ENCHANTER',
            'Materials.MACHINE_CIRCUIT', 'Materials.MACHINE_CORE', 'SlimefunItems.AUTO_DISENCHANTER',
            'ADVANCED_ENCHANTER'
        ]
        for item in reps:
            # Only replace if followed by comma or \n
            # Actually just `item + ','` -> `item + '.item(),'`
            c = c.replace(item + ',', item + '.item(),')
            c = c.replace(item + ' ', item + '.item() ') # for the end of arrays or line breaks before commas
            # Wait, `item + ' '` might match `ADVANCED_ENCHANTER = ...` so let's be careful.
            
        # Better: let's replace exact lines that are failing in SlimefunExtension
        c = c.replace('null, Materials.INFINITE_INGOT.item(), Materials.VOID_INGOT.item(), Materials.VOID_INGOT.item(), Materials.INFINITE_INGOT.item(), null,', 'null, Materials.INFINITE_INGOT.item(), Materials.VOID_INGOT.item(), Materials.VOID_INGOT.item(), Materials.INFINITE_INGOT.item(), null,')
        # Wait, if I just do .replace(item, item + '.item()') but restrict to certain blocks?
        # Actually I can just do:
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
                      
        # Clean up any double .item().item() just in case
        c = c.replace('.item().item()', '.item()')

    open(filepath, 'w', encoding='utf-8').write(c)

process(os.path.join(base, 'items', 'materials', 'Materials.java'), True, False)
process(os.path.join(base, 'items', 'SlimefunExtension.java'), False, True)
print("Done")
