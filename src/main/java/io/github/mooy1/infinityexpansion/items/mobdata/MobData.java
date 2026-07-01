package io.github.mooy1.infinityexpansion.items.mobdata;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.mooy1.infinityexpansion.InfinityExpansion;
import io.github.mooy1.infinityexpansion.categories.Groups;
import io.github.mooy1.infinityexpansion.items.materials.Materials;
import io.github.mooy1.infinitylib.core.Environment;
import io.github.mooy1.infinitylib.machines.MachineLore;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun5.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun5.implementation.SlimefunItems;
import io.github.mooy1.infinityexpansion.MaterialCompat;
import io.github.thebusybiscuit.slimefun5.libraries.xseries.XMaterial;

public final class MobData {

    private MobData() {}

    private static final int CHAMBER_INTERVAL =
            InfinityExpansion.config().getInt("mob-simulation-options.ticks-per-output", 1, 1000);
    private static final int CHAMBER_BUFFER = 15000;
    private static final int CHAMBER_ENERGY = 150;
    private static final int INFUSER_ENERGY = 20000;

    public static final SlimefunItemStack EMPTY_DATA_CARD = new SlimefunItemStack(
            "EMPTY_DATA_CARD",
            MaterialCompat.safe(XMaterial.CHAINMAIL_CHESTPLATE),
            "&8Empty Data Card",
            "&7Infuse with a mob's items to fill"
    );
    public static final SlimefunItemStack INFUSER = new SlimefunItemStack(
            "DATA_INFUSER",
            MaterialCompat.safe(XMaterial.LODESTONE),
            "&8Mob Data Infuser",
            "&7Infused empty data cards with mob items",
            "",
            MachineLore.energy(INFUSER_ENERGY) + "per use"
    );
    public static final SlimefunItemStack CHAMBER = new SlimefunItemStack(
            "MOB_SIMULATION_CHAMBER",
            MaterialCompat.safe(XMaterial.GILDED_BLACKSTONE),
            "&8Mob Simulation Chamber",
            "&7Use mob data cards to activate",
            "",
            MachineLore.energyBuffer(CHAMBER_BUFFER),
            MachineLore.energyPerSecond(CHAMBER_ENERGY)
    );

    public static final SlimefunItemStack COW = MobDataCard.create("Cow", MobDataTier.PASSIVE);
    public static final SlimefunItemStack SHEEP = MobDataCard.create("Sheep", MobDataTier.PASSIVE);
    public static final SlimefunItemStack CHICKEN = MobDataCard.create("Chicken", MobDataTier.PASSIVE);
    public static final SlimefunItemStack VILLAGER = MobDataCard.create("Villager", MobDataTier.NEUTRAL);

    public static final SlimefunItemStack BEE = MobDataCard.create("Bee", MobDataTier.NEUTRAL);
    public static final SlimefunItemStack SLIME = MobDataCard.create("Slime", MobDataTier.NEUTRAL);
    public static final SlimefunItemStack MAGMA_CUBE = MobDataCard.create("Magma Cube", MobDataTier.NEUTRAL);

    public static final SlimefunItemStack WITCH = MobDataCard.create("Witch", MobDataTier.ADVANCED);
    public static final SlimefunItemStack ZOMBIE = MobDataCard.create("Zombie", MobDataTier.HOSTILE);
    public static final SlimefunItemStack SPIDER = MobDataCard.create("Spider", MobDataTier.HOSTILE);
    public static final SlimefunItemStack SKELETON = MobDataCard.create("Skeleton", MobDataTier.HOSTILE);
    public static final SlimefunItemStack CREEPER = MobDataCard.create("Creeper", MobDataTier.HOSTILE);

    public static final SlimefunItemStack WITHER_SKELETON = MobDataCard.create("Wither Skeleton", MobDataTier.ADVANCED);
    public static final SlimefunItemStack ENDERMEN = MobDataCard.create("Endermen", MobDataTier.ADVANCED);
    public static final SlimefunItemStack GUARDIAN = MobDataCard.create("Guardian", MobDataTier.HOSTILE);
    public static final SlimefunItemStack IRON_GOLEM = MobDataCard.create("Iron Golem", MobDataTier.ADVANCED);
    public static final SlimefunItemStack BLAZE = MobDataCard.create("Blaze", MobDataTier.ADVANCED);

    public static final SlimefunItemStack WITHER = MobDataCard.create("Wither", MobDataTier.MINI_BOSS);
    public static final SlimefunItemStack ENDER_DRAGON = MobDataCard.create("Ender Dragon", MobDataTier.BOSS);

    public static void setup(InfinityExpansion plugin) {

        new MobSimulationChamber(Groups.MOB_SIMULATION, CHAMBER, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                Materials.MAGSTEEL_PLATE.item(), Materials.MACHINE_PLATE.item(), Materials.MAGSTEEL_PLATE.item(),
                Materials.MACHINE_CIRCUIT.item(), SlimefunItems.PROGRAMMABLE_ANDROID_BUTCHER.item(), Materials.MACHINE_CIRCUIT.item(),
                Materials.MAGSTEEL_PLATE.item(), Materials.MACHINE_PLATE.item(), Materials.MAGSTEEL_PLATE.item(),
        }, CHAMBER_ENERGY, CHAMBER_INTERVAL).register(plugin);

        new MobDataInfuser(Groups.MOB_SIMULATION, INFUSER, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                Materials.MACHINE_CIRCUIT.item(), SlimefunItems.REINFORCED_ALLOY_INGOT.item(), Materials.MACHINE_CIRCUIT.item(),
                SlimefunItems.REINFORCED_ALLOY_INGOT.item(), Materials.MACHINE_CORE.item(), SlimefunItems.REINFORCED_ALLOY_INGOT.item(),
                Materials.MACHINE_CIRCUIT.item(), SlimefunItems.REINFORCED_ALLOY_INGOT.item(), Materials.MACHINE_CIRCUIT.item()
        }, INFUSER_ENERGY).register(plugin);

        new SlimefunItem(Groups.MOB_SIMULATION, EMPTY_DATA_CARD, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                SlimefunItems.MAGNESIUM_INGOT.item(), Materials.MACHINE_CIRCUIT.item(), SlimefunItems.MAGNESIUM_INGOT.item(),
                SlimefunItems.SYNTHETIC_SAPPHIRE.item(), SlimefunItems.SYNTHETIC_DIAMOND.item(), SlimefunItems.SYNTHETIC_EMERALD.item(),
                SlimefunItems.MAGNESIUM_INGOT.item(), Materials.MACHINE_CIRCUIT.item(), SlimefunItems.MAGNESIUM_INGOT.item()
        }).register(plugin);

        if (InfinityExpansion.environment() == Environment.TESTING) {
            // There is some issues with player skull items in randomized sets when testing
            return;
        }

        new MobDataCard(ZOMBIE, MobDataTier.HOSTILE, new ItemStack[] {
                new ItemStack(MaterialCompat.safe(XMaterial.IRON_SWORD), 1), new ItemStack(MaterialCompat.safe(XMaterial.ROTTEN_FLESH), 16), new ItemStack(MaterialCompat.safe(XMaterial.IRON_SHOVEL), 1),
                new ItemStack(MaterialCompat.safe(XMaterial.IRON_INGOT), 64), EMPTY_DATA_CARD.item(), new ItemStack(MaterialCompat.safe(XMaterial.IRON_INGOT), 64),
                new ItemStack(MaterialCompat.safe(XMaterial.CARROT), 64), new ItemStack(MaterialCompat.safe(XMaterial.ROTTEN_FLESH), 16), new ItemStack(MaterialCompat.safe(XMaterial.POTATO), 64)
        }).addDrop(MaterialCompat.safe(XMaterial.ROTTEN_FLESH), 1).register(plugin);
        new MobDataCard(SLIME, MobDataTier.NEUTRAL, new ItemStack[] {
                new ItemStack(MaterialCompat.safe(XMaterial.SLIME_BLOCK), 16), new ItemStack(MaterialCompat.safe(XMaterial.LIME_DYE), 16), new ItemStack(MaterialCompat.safe(XMaterial.SLIME_BLOCK), 16),
                new ItemStack(MaterialCompat.safe(XMaterial.LIME_DYE), 16), EMPTY_DATA_CARD.item(), new ItemStack(MaterialCompat.safe(XMaterial.LIME_DYE), 16),
                new ItemStack(MaterialCompat.safe(XMaterial.SLIME_BLOCK), 16), new ItemStack(MaterialCompat.safe(XMaterial.LIME_DYE), 16), new ItemStack(MaterialCompat.safe(XMaterial.SLIME_BLOCK), 16)
        }).addDrop(MaterialCompat.safe(XMaterial.SLIME_BALL), 1).register(plugin);
        new MobDataCard(MAGMA_CUBE, MobDataTier.NEUTRAL, new ItemStack[] {
                new ItemStack(MaterialCompat.safe(XMaterial.MAGMA_BLOCK), 64), new ItemStack(MaterialCompat.safe(XMaterial.MAGMA_CREAM), 16), new ItemStack(MaterialCompat.safe(XMaterial.MAGMA_BLOCK), 64),
                new ItemStack(MaterialCompat.safe(XMaterial.SLIME_BLOCK), 16), EMPTY_DATA_CARD.item(), new ItemStack(MaterialCompat.safe(XMaterial.SLIME_BLOCK), 16),
                new ItemStack(MaterialCompat.safe(XMaterial.MAGMA_BLOCK), 64), new ItemStack(MaterialCompat.safe(XMaterial.MAGMA_CREAM), 16), new ItemStack(MaterialCompat.safe(XMaterial.MAGMA_BLOCK), 64)
        }).addDrop(MaterialCompat.safe(XMaterial.MAGMA_CREAM), 1).register(plugin);
        new MobDataCard(COW, MobDataTier.PASSIVE, new ItemStack[] {
                new ItemStack(MaterialCompat.safe(XMaterial.LEATHER), 64), new ItemStack(MaterialCompat.safe(XMaterial.BEEF), 64), new ItemStack(MaterialCompat.safe(XMaterial.LEATHER), 64),
                new ItemStack(MaterialCompat.safe(XMaterial.COOKED_BEEF), 64), EMPTY_DATA_CARD.item(), new ItemStack(MaterialCompat.safe(XMaterial.COOKED_BEEF), 64),
                new ItemStack(MaterialCompat.safe(XMaterial.LEATHER), 64), new ItemStack(MaterialCompat.safe(XMaterial.BEEF), 64), new ItemStack(MaterialCompat.safe(XMaterial.LEATHER), 64)
        }).addDrop(MaterialCompat.safe(XMaterial.LEATHER), 1).addDrop(MaterialCompat.safe(XMaterial.BEEF), 1).register(plugin);
        new MobDataCard(SHEEP, MobDataTier.PASSIVE, new ItemStack[] {
                new ItemStack(MaterialCompat.safe(XMaterial.WHITE_WOOL), 64), new ItemStack(MaterialCompat.safe(XMaterial.MUTTON), 64), new ItemStack(MaterialCompat.safe(XMaterial.WHITE_WOOL), 64),
                new ItemStack(MaterialCompat.safe(XMaterial.COOKED_MUTTON), 64), EMPTY_DATA_CARD.item(), new ItemStack(MaterialCompat.safe(XMaterial.COOKED_MUTTON), 64),
                new ItemStack(MaterialCompat.safe(XMaterial.WHITE_WOOL), 64), new ItemStack(MaterialCompat.safe(XMaterial.MUTTON), 64), new ItemStack(MaterialCompat.safe(XMaterial.WHITE_WOOL), 64)
        }).addDrop(MaterialCompat.safe(XMaterial.WHITE_WOOL), 1).addDrop(MaterialCompat.safe(XMaterial.MUTTON), 1).addDrop(MaterialCompat.safe(XMaterial.PINK_WOOL), 10000).register(plugin);
        new MobDataCard(SPIDER, MobDataTier.HOSTILE, new ItemStack[] {
                new ItemStack(MaterialCompat.safe(XMaterial.COBWEB), 8), new ItemStack(MaterialCompat.safe(XMaterial.STRING), 64), new ItemStack(MaterialCompat.safe(XMaterial.COBWEB), 8),
                new ItemStack(MaterialCompat.safe(XMaterial.SPIDER_EYE), 32), EMPTY_DATA_CARD.item(), new ItemStack(MaterialCompat.safe(XMaterial.SPIDER_EYE), 32),
                new ItemStack(MaterialCompat.safe(XMaterial.COBWEB), 8), new ItemStack(MaterialCompat.safe(XMaterial.STRING), 64), new ItemStack(MaterialCompat.safe(XMaterial.COBWEB), 8)
        }).addDrop(MaterialCompat.safe(XMaterial.STRING), 1).addDrop(MaterialCompat.safe(XMaterial.SPIDER_EYE), 2).register(plugin);
        new MobDataCard(SKELETON, MobDataTier.HOSTILE, new ItemStack[] {
                new ItemStack(MaterialCompat.safe(XMaterial.LEATHER_HELMET), 1), new ItemStack(MaterialCompat.safe(XMaterial.BONE), 64), new ItemStack(MaterialCompat.safe(XMaterial.LEATHER_HELMET), 1),
                new ItemStack(MaterialCompat.safe(XMaterial.ARROW), 64), EMPTY_DATA_CARD.item(), new ItemStack(MaterialCompat.safe(XMaterial.ARROW), 64),
                new ItemStack(MaterialCompat.safe(XMaterial.BOW), 1), new ItemStack(MaterialCompat.safe(XMaterial.BONE), 64), new ItemStack(MaterialCompat.safe(XMaterial.BOW), 1)
        }).addDrop(MaterialCompat.safe(XMaterial.BONE), 1).addDrop(MaterialCompat.safe(XMaterial.ARROW), 3).register(plugin);
        new MobDataCard(WITHER_SKELETON, MobDataTier.ADVANCED, new ItemStack[] {
                MaterialCompat.stack(XMaterial.WITHER_SKELETON_SKULL, 8), new ItemStack(MaterialCompat.safe(XMaterial.BONE), 64), MaterialCompat.stack(XMaterial.WITHER_SKELETON_SKULL, 8),
                new ItemStack(MaterialCompat.safe(XMaterial.COAL_BLOCK), 64), EMPTY_DATA_CARD.item(), new ItemStack(MaterialCompat.safe(XMaterial.COAL_BLOCK), 64),
                new ItemStack(MaterialCompat.safe(XMaterial.STONE_SWORD), 1), new ItemStack(MaterialCompat.safe(XMaterial.BONE), 64), new ItemStack(MaterialCompat.safe(XMaterial.STONE_SWORD), 1)
        }).addDrop(MaterialCompat.safe(XMaterial.COAL), 2, 1).addDrop(MaterialCompat.safe(XMaterial.BONE), 3).addDrop(MaterialCompat.safe(XMaterial.WITHER_SKELETON_SKULL), 15).register(plugin);
        new MobDataCard(ENDERMEN, MobDataTier.ADVANCED, new ItemStack[] {
                new ItemStack(MaterialCompat.safe(XMaterial.ENDER_EYE), 16), new ItemStack(MaterialCompat.safe(XMaterial.OBSIDIAN), 64), new ItemStack(MaterialCompat.safe(XMaterial.ENDER_EYE), 16),
                new ItemStack(MaterialCompat.safe(XMaterial.ENDER_PEARL), 16), EMPTY_DATA_CARD.item(), new ItemStack(MaterialCompat.safe(XMaterial.ENDER_PEARL), 16),
                new ItemStack(MaterialCompat.safe(XMaterial.ENDER_EYE), 16), new ItemStack(MaterialCompat.safe(XMaterial.OBSIDIAN), 64), new ItemStack(MaterialCompat.safe(XMaterial.ENDER_EYE), 16)
        }).addDrop(MaterialCompat.safe(XMaterial.ENDER_PEARL), 1).register(plugin);
        new MobDataCard(CREEPER, MobDataTier.HOSTILE, new ItemStack[] {
                new ItemStack(MaterialCompat.safe(XMaterial.TNT), 16), new ItemStack(MaterialCompat.safe(XMaterial.GREEN_DYE), 64), new ItemStack(MaterialCompat.safe(XMaterial.TNT), 16),
                new ItemStack(MaterialCompat.safe(XMaterial.GUNPOWDER), 16), EMPTY_DATA_CARD.item(), new ItemStack(MaterialCompat.safe(XMaterial.GUNPOWDER), 16),
                new ItemStack(MaterialCompat.safe(XMaterial.TNT), 16), new ItemStack(MaterialCompat.safe(XMaterial.GREEN_DYE), 64), new ItemStack(MaterialCompat.safe(XMaterial.TNT), 16)
        }).addDrop(MaterialCompat.safe(XMaterial.GUNPOWDER), 1).register(plugin);
        new MobDataCard(GUARDIAN, MobDataTier.HOSTILE, new ItemStack[] {
                new ItemStack(MaterialCompat.safe(XMaterial.COD), 16), new ItemStack(MaterialCompat.safe(XMaterial.PRISMARINE_SHARD), 64), new ItemStack(MaterialCompat.safe(XMaterial.PRISMARINE_CRYSTALS), 64),
                new ItemStack(MaterialCompat.safe(XMaterial.SPONGE), 4), EMPTY_DATA_CARD.item(), new ItemStack(MaterialCompat.safe(XMaterial.PUFFERFISH), 4),
                new ItemStack(MaterialCompat.safe(XMaterial.PRISMARINE_CRYSTALS), 64), new ItemStack(MaterialCompat.safe(XMaterial.PRISMARINE_SHARD), 64), new ItemStack(MaterialCompat.safe(XMaterial.COOKED_COD), 16)
        }).addDrop(MaterialCompat.safe(XMaterial.PRISMARINE_SHARD), 1).addDrop(MaterialCompat.safe(XMaterial.PRISMARINE_CRYSTALS), 2)
                .addDrop(MaterialCompat.safe(XMaterial.COD), 3).addDrop(MaterialCompat.safe(XMaterial.SPONGE), 40).register(plugin);
        new MobDataCard(CHICKEN, MobDataTier.PASSIVE, new ItemStack[] {
                new ItemStack(MaterialCompat.safe(XMaterial.CHICKEN), 64), new ItemStack(MaterialCompat.safe(XMaterial.FEATHER), 64), new ItemStack(MaterialCompat.safe(XMaterial.COOKED_CHICKEN), 64),
                new ItemStack(MaterialCompat.safe(XMaterial.EGG), 16), EMPTY_DATA_CARD.item(), new ItemStack(MaterialCompat.safe(XMaterial.EGG), 16),
                new ItemStack(MaterialCompat.safe(XMaterial.COOKED_CHICKEN), 64), new ItemStack(MaterialCompat.safe(XMaterial.FEATHER), 64), new ItemStack(MaterialCompat.safe(XMaterial.CHICKEN), 64)
        }).addDrop(MaterialCompat.safe(XMaterial.CHICKEN), 1).addDrop(MaterialCompat.safe(XMaterial.FEATHER), 2).register(plugin);
        new MobDataCard(IRON_GOLEM, MobDataTier.ADVANCED, new ItemStack[] {
                new ItemStack(MaterialCompat.safe(XMaterial.IRON_BLOCK), 64), new ItemStack(MaterialCompat.safe(XMaterial.PUMPKIN), 16), new ItemStack(MaterialCompat.safe(XMaterial.IRON_BLOCK), 64),
                new ItemStack(MaterialCompat.safe(XMaterial.POPPY), 16), EMPTY_DATA_CARD.item(), new ItemStack(MaterialCompat.safe(XMaterial.POPPY), 16),
                new ItemStack(MaterialCompat.safe(XMaterial.IRON_BLOCK), 64), new ItemStack(MaterialCompat.safe(XMaterial.PUMPKIN), 16), new ItemStack(MaterialCompat.safe(XMaterial.IRON_BLOCK), 64)
        }).addDrop(MaterialCompat.safe(XMaterial.IRON_INGOT), 2, 1).addDrop(MaterialCompat.safe(XMaterial.POPPY), 3).addDrop(SlimefunItems.BASIC_CIRCUIT_BOARD.item(), 3).register(plugin);
        new MobDataCard(BLAZE, MobDataTier.ADVANCED, new ItemStack[] {
                new ItemStack(MaterialCompat.safe(XMaterial.MAGMA_BLOCK), 64), new ItemStack(MaterialCompat.safe(XMaterial.BLAZE_ROD), 64), new ItemStack(MaterialCompat.safe(XMaterial.MAGMA_BLOCK), 64),
                new ItemStack(MaterialCompat.safe(XMaterial.BLAZE_ROD), 64), EMPTY_DATA_CARD.item(), new ItemStack(MaterialCompat.safe(XMaterial.BLAZE_ROD), 64),
                new ItemStack(MaterialCompat.safe(XMaterial.MAGMA_BLOCK), 64), new ItemStack(MaterialCompat.safe(XMaterial.BLAZE_ROD), 64), new ItemStack(MaterialCompat.safe(XMaterial.MAGMA_BLOCK), 64)
        }).addDrop(MaterialCompat.safe(XMaterial.BLAZE_ROD), 1).register(plugin);
        new MobDataCard(WITHER, MobDataTier.MINI_BOSS, new ItemStack[] {
                MaterialCompat.stack(XMaterial.WITHER_SKELETON_SKULL, 64), MaterialCompat.stack(XMaterial.WITHER_SKELETON_SKULL, 64), MaterialCompat.stack(XMaterial.WITHER_SKELETON_SKULL, 64),
                new SlimefunItemStack(SlimefunItems.WITHER_PROOF_OBSIDIAN, 64).item(), EMPTY_DATA_CARD.item(), new SlimefunItemStack(SlimefunItems.WITHER_PROOF_OBSIDIAN, 64).item(),
                new SlimefunItemStack(Materials.VOID_INGOT, 4).item(), new SlimefunItemStack(SlimefunItems.WITHER_ASSEMBLER, 4).item(), new SlimefunItemStack(Materials.VOID_INGOT, 4).item()
        }).addDrop(MaterialCompat.safe(XMaterial.NETHER_STAR), 1).addDrop(SlimefunItems.COMPRESSED_CARBON.item(), 8, 2).register(plugin);
        new MobDataCard(ENDER_DRAGON, MobDataTier.BOSS, new ItemStack[] {
                new ItemStack(MaterialCompat.safe(XMaterial.END_CRYSTAL), 64), new SlimefunItemStack(Materials.VOID_INGOT, 32).item(), new ItemStack(MaterialCompat.safe(XMaterial.CHORUS_FLOWER), 64),
                SlimefunItems.INFUSED_ELYTRA.item(), EMPTY_DATA_CARD.item(), MaterialCompat.stack(XMaterial.DRAGON_HEAD, 1),
                new SlimefunItemStack(SlimefunItems.ENDER_LUMP_3, 64).item(), new SlimefunItemStack(Materials.VOID_INGOT, 32).item(), new ItemStack(MaterialCompat.safe(XMaterial.DRAGON_BREATH), 64)
        }).addDrop(Materials.VOID_DUST.item(), 1).addDrop(Materials.ENDER_ESSENCE.item(), 4).addDrop(MaterialCompat.safe(XMaterial.DRAGON_EGG), 1_000_000).register(plugin);
        new MobDataCard(BEE, MobDataTier.NEUTRAL, new ItemStack[] {
                new ItemStack(MaterialCompat.safe(XMaterial.HONEYCOMB_BLOCK), 16), new ItemStack(MaterialCompat.safe(XMaterial.HONEY_BLOCK), 16), new ItemStack(MaterialCompat.safe(XMaterial.HONEYCOMB_BLOCK), 16),
                new ItemStack(MaterialCompat.safe(XMaterial.HONEY_BLOCK), 16), EMPTY_DATA_CARD.item(), new ItemStack(MaterialCompat.safe(XMaterial.HONEY_BLOCK), 16),
                new ItemStack(MaterialCompat.safe(XMaterial.HONEYCOMB_BLOCK), 16), new ItemStack(MaterialCompat.safe(XMaterial.HONEY_BLOCK), 16), new ItemStack(MaterialCompat.safe(XMaterial.HONEYCOMB_BLOCK), 16)
        }).addDrop(MaterialCompat.safe(XMaterial.HONEYCOMB), 1).register(plugin);
        new MobDataCard(VILLAGER, MobDataTier.NEUTRAL, new ItemStack[] {
                new ItemStack(MaterialCompat.safe(XMaterial.EMERALD), 64), new ItemStack(MaterialCompat.safe(XMaterial.POTATO), 64), new ItemStack(MaterialCompat.safe(XMaterial.EMERALD), 64),
                new ItemStack(MaterialCompat.safe(XMaterial.CARROT), 64), EMPTY_DATA_CARD.item(), new ItemStack(MaterialCompat.safe(XMaterial.WHEAT), 64),
                new ItemStack(MaterialCompat.safe(XMaterial.EMERALD), 64), new ItemStack(MaterialCompat.safe(XMaterial.PUMPKIN), 64), new ItemStack(MaterialCompat.safe(XMaterial.EMERALD), 64)
        }).addDrop(MaterialCompat.safe(XMaterial.EMERALD), 1).register(plugin);
        new MobDataCard(WITCH, MobDataTier.ADVANCED, new ItemStack[] {
                new ItemStack(MaterialCompat.safe(XMaterial.REDSTONE_BLOCK), 64), new ItemStack(MaterialCompat.safe(XMaterial.GLASS), 64), new ItemStack(MaterialCompat.safe(XMaterial.SUGAR), 64),
                new ItemStack(MaterialCompat.safe(XMaterial.GLOWSTONE), 64), EMPTY_DATA_CARD.item(), new ItemStack(MaterialCompat.safe(XMaterial.GLOWSTONE), 64),
                new ItemStack(MaterialCompat.safe(XMaterial.SUGAR), 64), new ItemStack(MaterialCompat.safe(XMaterial.GLASS), 64), new ItemStack(MaterialCompat.safe(XMaterial.REDSTONE_BLOCK), 64)
        }).addDrop(MaterialCompat.safe(XMaterial.SUGAR), 1).addDrop(MaterialCompat.safe(XMaterial.REDSTONE), 1)
                .addDrop(MaterialCompat.safe(XMaterial.GLASS_BOTTLE), 1).addDrop(MaterialCompat.safe(XMaterial.GLOWSTONE_DUST), 1).register(plugin);
    }

}
