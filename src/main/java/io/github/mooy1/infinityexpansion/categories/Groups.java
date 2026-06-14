package io.github.mooy1.infinityexpansion.categories;

import org.bukkit.Material;

import io.github.mooy1.infinityexpansion.InfinityExpansion;
import io.github.mooy1.infinityexpansion.MaterialCompat;
import io.github.thebusybiscuit.slimefun5.libraries.xseries.XMaterial;
import io.github.mooy1.infinitylib.groups.MultiGroup;
import io.github.mooy1.infinitylib.groups.SubGroup;
import io.github.thebusybiscuit.slimefun5.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun5.libraries.dough.items.CustomItemStack;

/**
 * Categories for this addon
 *
 * @author Mooy1
 */
public final class Groups {

    public static final ItemGroup INFINITY = new InfinityGroup(InfinityExpansion.createKey("infinity_recipes"),
            CustomItemStack.create(MaterialCompat.safe(XMaterial.RESPAWN_ANCHOR), "&bInfinity &7Recipes"), 3);
    public static final ItemGroup MAIN_MATERIALS = new SubGroup("main_materials",
            CustomItemStack.create(MaterialCompat.safe(XMaterial.NETHER_STAR), "&bInfinity &7Materials"));
    public static final ItemGroup BASIC_MACHINES = new SubGroup("basic_machines",
            CustomItemStack.create(MaterialCompat.safe(XMaterial.LOOM), "&9Basic &7Machines"));
    public static final ItemGroup ADVANCED_MACHINES = new SubGroup("advanced_machines",
            CustomItemStack.create(MaterialCompat.safe(XMaterial.BLAST_FURNACE), "&cAdvanced &7Machines"));
    public static final ItemGroup STORAGE = new SubGroup("storage",
            CustomItemStack.create(MaterialCompat.safe(XMaterial.BEEHIVE), "&6Storage"));
    public static final ItemGroup MOB_SIMULATION = new SubGroup("mob_simulation",
            CustomItemStack.create(MaterialCompat.safe(XMaterial.BEACON), "&bMob Simulation"));
    public static final ItemGroup INFINITY_MATERIALS = new SubGroup("infinity_materials",
            CustomItemStack.create(MaterialCompat.safe(XMaterial.NETHERITE_BLOCK), "&bInfinity &aMaterials"));
    public static final ItemGroup MAIN_CATEGORY = new MultiGroup("main",
            CustomItemStack.create(MaterialCompat.safe(XMaterial.NETHER_STAR), "&bInfinity &7Expansion"), 3,
            MAIN_MATERIALS, BASIC_MACHINES, ADVANCED_MACHINES, STORAGE, MOB_SIMULATION, INFINITY_MATERIALS, INFINITY);
    public static final ItemGroup INFINITY_CHEAT = new SubGroup("infinity_cheat",
            CustomItemStack.create(MaterialCompat.safe(XMaterial.RESPAWN_ANCHOR), "&bInfinity &7Recipes &c- INCORRECT RECIPES"));

    public static void setup(InfinityExpansion inst) {
        INFINITY.register(inst);
        MAIN_CATEGORY.register(inst);
        MOB_SIMULATION.setCrossAddonItemGroup(true);
        INFINITY_CHEAT.register(inst);
    }

}