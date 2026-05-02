import os
import subprocess

os.chdir(r'D:\Documents\GitHub\slimefun\InfinityExpansion')
subprocess.run(['git', 'checkout', 'src/main/java/io/github/mooy1/infinityexpansion/items/materials/Materials.java'])
subprocess.run(['git', 'checkout', 'src/main/java/io/github/mooy1/infinityexpansion/items/SlimefunExtension.java'])

# Then run Gradle compile and capture output
out = subprocess.run(['gradlew.bat', 'compileJava', '-i'], capture_output=True, text=True)

import re

# Parse errors from gradle output
# Format:
# D:\...\Materials.java:323: error: cannot find symbol
#         registerSmeltery(MAGSTEEL, SlimefunItems.MAGNESIUM_INGOT.item(), ...
#                                                                       ^
errors = []
lines = out.stdout.split('\n')
for i, line in enumerate(lines):
    if 'error: incompatible types: SlimefunItemStack cannot be converted to ItemStack' in line or 'error: method registerSmeltery in class Materials cannot be applied to given types' in line or 'no suitable constructor found for Singularity' in line or 'error: incompatible types: ItemStack cannot be converted to SlimefunItemStack' in line or 'error: no suitable constructor found for RecipeType' in line:
        file_line = line.split(': error:')[0]
        if ':' in file_line:
            parts = file_line.rsplit(':', 1)
            filepath = parts[0].strip()
            linenum = int(parts[1].strip()) - 1
            errors.append((filepath, linenum, line))

# Wait, if I just replace all `SlimefunItems.XYZ` with `SlimefunItems.XYZ.item()` ONLY inside `new ItemStack[] { ... }` 
# I can do that easily in python.

def apply_item_to_array(c):
    # match `new ItemStack[] { ... }`
    def replacer(match):
        inner = match.group(1)
        # split by comma, and for each item, if it matches uppercase or SlimefunItems, append .item() if not there
        parts = inner.split(',')
        new_parts = []
        for p in parts:
            p_strip = p.strip()
            if p_strip and p_strip != 'null':
                if 'Material.' not in p_strip and '.item()' not in p_strip and ('SlimefunItems.' in p_strip or p_strip.isupper() or p_strip.endswith('_STRAINER') or p_strip.endswith('_ENCHANTER') or p_strip.endswith('_DISENCHANTER') or 'Materials.' in p_strip):
                    # Replace the stripped part with stripped + .item()
                    p = p.replace(p_strip, p_strip + '.item()')
            new_parts.append(p)
        return 'new ItemStack[] {' + ','.join(new_parts) + '}'
    
    return re.sub(r'new ItemStack\[\]\s*\{([^}]+)\}', replacer, c)

def apply_item_to_varargs(c):
    # registerSmeltery(a, b, c...)
    def replacer(match):
        inner = match.group(1)
        parts = inner.split(',')
        new_parts = [parts[0]] # first arg is SlimefunItemStack
        for p in parts[1:]:
            p_strip = p.strip()
            if p_strip and p_strip != 'null':
                if 'Material.' not in p_strip and '.item()' not in p_strip and ('SlimefunItems.' in p_strip or p_strip.isupper() or 'Materials.' in p_strip):
                    p = p.replace(p_strip, p_strip + '.item()')
            new_parts.append(p)
        return 'registerSmeltery(' + ','.join(new_parts) + ');'
    
    return re.sub(r'registerSmeltery\(([^;]+)\);', replacer, c)

def fix_materials():
    p = 'src/main/java/io/github/mooy1/infinityexpansion/items/materials/Materials.java'
    c = open(p, 'r', encoding='utf-8').read()
    c = apply_item_to_array(c)
    c = apply_item_to_varargs(c)
    c = re.sub(r'new Singularity\(([A-Z_0-9]+),\s*(SlimefunItems\.[A-Z_]+),\s*([0-9]+)\)', r'new Singularity(\1, \2.item(), \3)', c)
    open(p, 'w', encoding='utf-8').write(c)

def fix_extension():
    p = 'src/main/java/io/github/mooy1/infinityexpansion/items/SlimefunExtension.java'
    c = open(p, 'r', encoding='utf-8').read()
    c = apply_item_to_array(c)
    open(p, 'w', encoding='utf-8').write(c)

fix_materials()
fix_extension()

print("Smart replace done")
