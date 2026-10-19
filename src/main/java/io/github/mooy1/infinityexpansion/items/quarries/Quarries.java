package io.github.mooy1.infinityexpansion.items.quarries;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import io.github.mooy1.infinityexpansion.InfinityExpansion;
import io.github.mooy1.infinityexpansion.categories.Groups;
import io.github.mooy1.infinityexpansion.items.SlimefunExtension;
import io.github.mooy1.infinityexpansion.items.blocks.InfinityWorkbench;
import io.github.mooy1.infinityexpansion.items.gear.Gear;
import io.github.mooy1.infinityexpansion.items.materials.Materials;
import io.github.mooy1.infinitylib.machines.MachineLore;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun5.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun5.implementation.SlimefunItems;
import io.github.mooy1.infinityexpansion.MaterialCompat;
import io.github.thebusybiscuit.slimefun5.libraries.xseries.XMaterial;

public final class Quarries {

    private Quarries() {}

    public static final SlimefunItemStack BASIC_QUARRY = new SlimefunItemStack("BASIC_QUARRY", MaterialCompat.safe(XMaterial.CHISELED_SANDSTONE));
    public static final SlimefunItemStack ADVANCED_QUARRY = new SlimefunItemStack("ADVANCED_QUARRY", MaterialCompat.safe(XMaterial.CHISELED_RED_SANDSTONE));
    public static final SlimefunItemStack VOID_QUARRY = new SlimefunItemStack("VOID_QUARRY", MaterialCompat.safe(XMaterial.CHISELED_NETHER_BRICKS));
    public static final SlimefunItemStack INFINITY_QUARRY = new SlimefunItemStack("INFINITY_QUARRY", MaterialCompat.safe(XMaterial.CHISELED_POLISHED_BLACKSTONE));
    public static final double DIAMOND_CHANCE = getOscillatorChance("diamond");
    public static final double REDSTONE_CHANCE = getOscillatorChance("redstone");
    public static final double LAPIS_CHANCE = getOscillatorChance("lapis");
    public static final double EMERALD_CHANCE = getOscillatorChance("emerald");
    public static final double QUARTZ_CHANCE = getOscillatorChance("quartz");
    public static final SlimefunItemStack DIAMOND_OSCILLATOR = Oscillator.create(XMaterial.DIAMOND, DIAMOND_CHANCE);
    public static final SlimefunItemStack REDSTONE_OSCILLATOR = Oscillator.create(XMaterial.REDSTONE, REDSTONE_CHANCE);
    public static final SlimefunItemStack LAPIS_OSCILLATOR = Oscillator.create(XMaterial.LAPIS_LAZULI, LAPIS_CHANCE);
    public static final SlimefunItemStack QUARTZ_OSCILLATOR = Oscillator.create(XMaterial.QUARTZ, QUARTZ_CHANCE);
    public static final SlimefunItemStack EMERALD_OSCILLATOR = Oscillator.create(XMaterial.EMERALD, EMERALD_CHANCE);

    private static double getOscillatorChance(String type) {
        return InfinityExpansion.config().getDouble("quarry-options.oscillators." + type, 0, 1);
    }

    public static void setup(InfinityExpansion plugin) {
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("quarry-options.resources");
        Objects.requireNonNull(section);
        List<XMaterial> outputs = new ArrayList<>();

        boolean coal = section.getBoolean("coal");

        if (coal) {
            outputs.add(XMaterial.COAL);
            outputs.add(XMaterial.COAL);
        }

        if (section.getBoolean("iron")) {
            outputs.add(XMaterial.IRON_INGOT);
        }

        if (section.getBoolean("gold")) {
            outputs.add(XMaterial.GOLD_INGOT);
        }

        if (section.getBoolean("copper")) {
            if (XMaterial.COPPER_INGOT.isSupported()) {
                outputs.add(XMaterial.COPPER_INGOT);
                outputs.add(XMaterial.COPPER_INGOT);
            }
        }

        if (section.getBoolean("redstone")) {
            new Oscillator(REDSTONE_OSCILLATOR, REDSTONE_CHANCE).register(plugin);
            outputs.add(XMaterial.REDSTONE);
        }

        if (section.getBoolean("lapis")) {
            new Oscillator(LAPIS_OSCILLATOR, LAPIS_CHANCE).register(plugin);
            outputs.add(XMaterial.LAPIS_LAZULI);
        }

        if (section.getBoolean("emerald")) {
            new Oscillator(EMERALD_OSCILLATOR, EMERALD_CHANCE).register(plugin);
            outputs.add(XMaterial.EMERALD);
        }

        if (section.getBoolean("diamond")) {
            new Oscillator(DIAMOND_OSCILLATOR, DIAMOND_CHANCE).register(plugin);
            outputs.add(XMaterial.DIAMOND);
        }

        new Quarry(Groups.ADVANCED_MACHINES, BASIC_QUARRY, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                Materials.MAGSTEEL_PLATE.item(), SlimefunItems.CARBONADO_EDGED_CAPACITOR.item(), Materials.MAGSTEEL_PLATE.item(),
                new ItemStack(MaterialCompat.safe(XMaterial.IRON_PICKAXE)), SlimefunItems.GEO_MINER.item(), new ItemStack(MaterialCompat.safe(XMaterial.IRON_PICKAXE)),
                Materials.MACHINE_CIRCUIT.item(), Materials.MACHINE_CORE.item(), Materials.MACHINE_CIRCUIT.item()
        }, 1, 6, outputs.toArray(new XMaterial[0])).energyPerTick(300).register(plugin);

        if (section.getBoolean("quartz")) {
            new Oscillator(QUARTZ_OSCILLATOR, QUARTZ_CHANCE).register(plugin);

            outputs.add(XMaterial.QUARTZ);
        }

        if (section.getBoolean("netherite")) {
            outputs.add(XMaterial.NETHERITE_INGOT);
        }

        if (section.getBoolean("netherrack")) {
            outputs.add(XMaterial.NETHERRACK);
            outputs.add(XMaterial.NETHERRACK);
        }

        new Quarry(Groups.ADVANCED_MACHINES, ADVANCED_QUARRY, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                Materials.MACHINE_PLATE.item(), SlimefunItems.ENERGIZED_CAPACITOR.item(), Materials.MACHINE_PLATE.item(),
                new ItemStack(MaterialCompat.safe(XMaterial.DIAMOND_PICKAXE)), BASIC_QUARRY.item(), new ItemStack(MaterialCompat.safe(XMaterial.DIAMOND_PICKAXE)),
                Materials.MACHINE_CIRCUIT.item(), Materials.MACHINE_CORE.item(), Materials.MACHINE_CIRCUIT.item()
        }, 2, 4, outputs.toArray(new XMaterial[0])).energyPerTick(900).register(plugin);

        if (coal) {
            outputs.add(XMaterial.COAL);
        }

        new Quarry(Groups.ADVANCED_MACHINES, VOID_QUARRY, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                Materials.VOID_INGOT.item(), SlimefunExtension.VOID_CAPACITOR.item(), Materials.VOID_INGOT.item(),
                new ItemStack(MaterialCompat.safe(XMaterial.NETHERITE_PICKAXE)), ADVANCED_QUARRY.item(), new ItemStack(MaterialCompat.safe(XMaterial.NETHERITE_PICKAXE)),
                Materials.MACHINE_CIRCUIT.item(), Materials.MACHINE_CORE.item(), Materials.MACHINE_CIRCUIT.item()
        }, 6, 2, outputs.toArray(new XMaterial[0])).energyPerTick(3600).register(plugin);

        if (coal) {
            outputs.add(XMaterial.COAL);
        }

        new Quarry(Groups.INFINITY_CHEAT, INFINITY_QUARRY, InfinityWorkbench.TYPE, new ItemStack[] {
                null, Materials.MACHINE_PLATE.item(), Materials.MACHINE_PLATE.item(), Materials.MACHINE_PLATE.item(), Materials.MACHINE_PLATE.item(), null,
                Materials.MACHINE_PLATE.item(), Gear.PICKAXE.item(), Materials.INFINITE_CIRCUIT.item(), Materials.INFINITE_CIRCUIT.item(), Gear.PICKAXE.item(), Materials.MACHINE_PLATE.item(),
                Materials.MACHINE_PLATE.item(), VOID_QUARRY.item(), Materials.INFINITE_CORE.item(), Materials.INFINITE_CORE.item(), VOID_QUARRY.item(), Materials.MACHINE_PLATE.item(),
                Materials.VOID_INGOT.item(), null, Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(), null, Materials.VOID_INGOT.item(),
                Materials.VOID_INGOT.item(), null, Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(), null, Materials.VOID_INGOT.item(),
                Materials.VOID_INGOT.item(), null, Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(), null, Materials.VOID_INGOT.item()
        }, 64, 1, outputs.toArray(new XMaterial[0])).energyPerTick(36000).register(plugin);
    }

}
