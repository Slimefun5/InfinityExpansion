package io.github.mooy1.infinityexpansion.categories;

import io.github.mooy1.infinityexpansion.InfinityExpansion;
import io.github.mooy1.infinityexpansion.MaterialCompat;
import io.github.thebusybiscuit.slimefun5.libraries.xseries.XMaterial;
import io.github.mooy1.infinitylib.groups.SubGroup;
import io.github.thebusybiscuit.slimefun5.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun5.libraries.dough.items.CustomItemStack;

/**
 * Categories for this addon
 *
 * @author Mooy1
 */
public final class Groups {

    // Kept for InfinityGroup's own references; no longer registered (custom guide UIs are deprecated).
    public static final ItemGroup INFINITY = new InfinityGroup(InfinityExpansion.createKey("infinity_recipes"),
            CustomItemStack.create(MaterialCompat.safe(XMaterial.RESPAWN_ANCHOR), "&bInfinity &7Recipes"), 3).setTheme("machines");
    public static final ItemGroup MAIN_MATERIALS = new ItemGroup(InfinityExpansion.createKey("main_materials"),
            CustomItemStack.create(MaterialCompat.safe(XMaterial.NETHER_STAR), "&bInfinity &7Materials"));
    public static final ItemGroup BASIC_MACHINES = new ItemGroup(InfinityExpansion.createKey("basic_machines"),
            CustomItemStack.create(MaterialCompat.safe(XMaterial.LOOM), "&9Basic &7Machines"));
    public static final ItemGroup ADVANCED_MACHINES = new ItemGroup(InfinityExpansion.createKey("advanced_machines"),
            CustomItemStack.create(MaterialCompat.safe(XMaterial.BLAST_FURNACE), "&cAdvanced &7Machines"));
    public static final ItemGroup STORAGE = new ItemGroup(InfinityExpansion.createKey("storage"),
            CustomItemStack.create(MaterialCompat.safe(XMaterial.BEEHIVE), "&6Storage"));
    public static final ItemGroup MOB_SIMULATION = new ItemGroup(InfinityExpansion.createKey("mob_simulation"),
            CustomItemStack.create(MaterialCompat.safe(XMaterial.BEACON), "&bMob Simulation"));
    public static final ItemGroup INFINITY_MATERIALS = new ItemGroup(InfinityExpansion.createKey("infinity_materials"),
            CustomItemStack.create(MaterialCompat.safe(XMaterial.NETHERITE_BLOCK), "&bInfinity &aMaterials"));
    // Hidden: holds intentionally-incorrect infinity recipes.
    public static final ItemGroup INFINITY_CHEAT = new SubGroup("infinity_cheat",
            CustomItemStack.create(MaterialCompat.safe(XMaterial.RESPAWN_ANCHOR), "&bInfinity &7Recipes &c- INCORRECT RECIPES")).setTheme("machines");

    public static void setup(InfinityExpansion inst) {
        MAIN_MATERIALS.register(inst);
        BASIC_MACHINES.register(inst);
        ADVANCED_MACHINES.register(inst);
        STORAGE.register(inst);
        MOB_SIMULATION.register(inst);
        INFINITY_MATERIALS.register(inst);
        MOB_SIMULATION.setCrossAddonItemGroup(true);
        INFINITY_CHEAT.register(inst);
    }

}