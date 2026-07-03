package io.github.mooy1.infinityexpansion.items.blocks;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.mooy1.infinityexpansion.InfinityExpansion;
import io.github.mooy1.infinityexpansion.categories.Groups;
import io.github.mooy1.infinityexpansion.items.materials.Materials;
import io.github.mooy1.infinitylib.machines.MachineLore;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun5.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun5.implementation.SlimefunItems;
import io.github.mooy1.infinityexpansion.MaterialCompat;
import io.github.thebusybiscuit.slimefun5.libraries.xseries.XMaterial;

public final class Blocks {

    private Blocks() {}

    public static final SlimefunItemStack STRAINER_BASE = new SlimefunItemStack("STRAINER_BASE", MaterialCompat.safe(XMaterial.SANDSTONE_WALL));
    public static final SlimefunItemStack ADVANCED_ANVIL = new SlimefunItemStack("ADVANCED_ANVIL", MaterialCompat.safe(XMaterial.SMITHING_TABLE));
    public static final SlimefunItemStack INFINITY_FORGE = new SlimefunItemStack("INFINITY_FORGE", MaterialCompat.safe(XMaterial.RESPAWN_ANCHOR));

    public static void setup(InfinityExpansion plugin) {
        new StrainerBase(Groups.BASIC_MACHINES, STRAINER_BASE, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                new ItemStack(MaterialCompat.safe(XMaterial.STICK)), new ItemStack(MaterialCompat.safe(XMaterial.STRING)), new ItemStack(MaterialCompat.safe(XMaterial.STICK)),
                new ItemStack(MaterialCompat.safe(XMaterial.STICK)), new ItemStack(MaterialCompat.safe(XMaterial.STRING)), new ItemStack(MaterialCompat.safe(XMaterial.STICK)),
                Materials.MAGSTEEL.item(), Materials.MAGSTEEL.item(), Materials.MAGSTEEL.item(),
        }, 48).register(plugin);
        new AdvancedAnvil(Groups.MAIN_MATERIALS, ADVANCED_ANVIL, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                Materials.MACHINE_PLATE.item(), Materials.MACHINE_PLATE.item(), Materials.MACHINE_PLATE.item(),
                Materials.MACHINE_PLATE.item(), new ItemStack(MaterialCompat.safe(XMaterial.ANVIL)), Materials.MACHINE_PLATE.item(),
                Materials.MACHINE_CIRCUIT.item(), Materials.MACHINE_CORE.item(), Materials.MACHINE_CIRCUIT.item()
        }, 100000).register(plugin);
        new InfinityWorkbench(Groups.MAIN_MATERIALS, INFINITY_FORGE, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                Materials.VOID_INGOT.item(), Materials.MACHINE_PLATE.item(), Materials.VOID_INGOT.item(),
                SlimefunItems.ENERGIZED_CAPACITOR.item(), new ItemStack(MaterialCompat.safe(XMaterial.CRAFTING_TABLE)), SlimefunItems.ENERGIZED_CAPACITOR.item(),
                Materials.VOID_INGOT.item(), Materials.MACHINE_PLATE.item(), Materials.VOID_INGOT.item()
        }, 10000000).register(plugin);
    }

}
