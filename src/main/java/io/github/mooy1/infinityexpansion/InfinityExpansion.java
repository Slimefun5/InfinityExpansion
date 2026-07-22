package io.github.mooy1.infinityexpansion;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;

import io.github.mooy1.infinityexpansion.categories.Groups;
import io.github.mooy1.infinityexpansion.commands.GiveRecipe;
import io.github.mooy1.infinityexpansion.commands.PrintItem;
import io.github.mooy1.infinityexpansion.commands.SetData;
import io.github.mooy1.infinityexpansion.items.Researches;
import io.github.mooy1.infinityexpansion.items.SlimefunExtension;
import io.github.mooy1.infinityexpansion.items.blocks.Blocks;
import io.github.mooy1.infinityexpansion.items.gear.Gear;
import io.github.mooy1.infinityexpansion.items.generators.Generators;
import io.github.mooy1.infinityexpansion.items.machines.Machines;
import io.github.mooy1.infinityexpansion.items.materials.Materials;
import io.github.mooy1.infinityexpansion.items.mobdata.MobData;
import io.github.mooy1.infinityexpansion.items.quarries.Quarries;
import io.github.mooy1.infinityexpansion.items.storage.Storage;
import io.github.mooy1.infinityexpansion.items.storage.StorageSaveFix;
import io.github.mooy1.infinitylib.common.Scheduler;
import io.github.mooy1.infinitylib.core.AbstractAddon;
import io.github.thebusybiscuit.slimefun5.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun5.core.guide.wiki.WikiText;
import io.github.thebusybiscuit.slimefun5.core.guide.wiki.WikiTopic;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.libraries.keys.NamespacedKey;
import io.github.thebusybiscuit.slimefun5.libraries.xseries.XMaterial;

public final class InfinityExpansion extends AbstractAddon {

    public InfinityExpansion(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file,
                "Slimefun5", "InfinityExpansion", "master", "auto-update");
    }

    public InfinityExpansion() {
        super("Slimefun5", "InfinityExpansion", "master", "auto-update");
        StorageSaveFix.fixStuff(getLogger());
    }

    /**
     * Creates a version-safe {@link NamespacedKey} shim for PDC boundaries. The real
     * {@code org.bukkit.NamespacedKey} is only materialised reflectively at access time
     * (see {@code CompatUtils}), so this stays loadable on legacy servers without PDC.
     */
    public static NamespacedKey pdcKey(String key) {
        return createKey(key);
    }

    @Override
    protected void enable() {
        Plugin lx = getServer().getPluginManager().getPlugin("LiteXpansion");
        if (lx != null && lx.getConfig().getBoolean("options.nerf-other-addons")) {
            Scheduler.run(() -> log(Level.WARNING,
                    "########################################################",
                    "LiteXpansion nerfs energy generation in this addon.",
                    "You can disable these nerfs in the LiteXpansion config.",
                    "Under 'options:' add 'nerf-other-addons: false'",
                    "########################################################"
            ));
        }

        getAddonCommand()
                .addSub(new GiveRecipe())
                .addSub(new SetData())
                .addSub(new PrintItem());

        Groups.setup(this);
        MobData.setup(this);
        Materials.setup(this);
        Machines.setup(this);
        Quarries.setup(this);
        Gear.setup(this);
        Blocks.setup(this);
        Storage.setup(this);
        Generators.setup(this);
        SlimefunExtension.setup(this);

        classifyGuideTypes();

        if (getConfig().getBoolean("balance-options.enable-researches")) {
            Researches.setup();
        }

        // Contribute this addon's per-language item translations (languages/<lang>/items.yml).
        Slimefun.getItemTranslationService().registerTranslations(this);

        // Register this addon's own in-game wiki page (core does not auto-generate addon wikis).
        registerWiki();
    }

    /**
     * Declares a categorized-guide type for this addon's items whose group the core heuristic
     * can't type on its own. EnergyNetComponent machines and real-material gear auto-classify,
     * so they are left untouched.
     */
    private void classifyGuideTypes() {
        for (SlimefunItem item : Slimefun.getRegistry().getEnabledSlimefunItems()) {
            try {
                if (item.getAddon() != this) {
                    continue;
                }
                String type = guideTypeFor(item);
                if (type != null) {
                    item.setGuideType(type);
                }
            } catch (Exception | LinkageError ignored) {
                // A broken item should not break classification.
            }
        }
    }

    private static String guideTypeFor(SlimefunItem item) {
        // These auto-classify via the core heuristic (machines/energy_tech), so leave them.
        if (item instanceof EnergyNetComponent) {
            return null;
        }
        ItemGroup group = item.getItemGroup();
        if (group == Groups.MAIN_MATERIALS || group == Groups.INFINITY_MATERIALS) {
            return "resources";
        }
        if (group == Groups.STORAGE) {
            return "logistics";
        }
        if (group == Groups.BASIC_MACHINES || group == Groups.ADVANCED_MACHINES || group == Groups.MOB_SIMULATION) {
            return "machines";
        }
        return null;
    }

    private void registerWiki() {
        WikiText wiki = Slimefun.getWikiText();

        // Bucket this addon's items by their ItemGroup dynamically - never hardcode item lists.
        Map<ItemGroup, List<String>> byGroup = new LinkedHashMap<>();

        for (SlimefunItem item : Slimefun.getRegistry().getEnabledSlimefunItems()) {
            try {
                if (item.getAddon() != this) {
                    continue;
                }
                ItemGroup group = item.getItemGroup();
                if (group == null) {
                    continue;
                }
                byGroup.computeIfAbsent(group, g -> new ArrayList<>()).add(item.getId());

                // Author this item's own wiki page if we have something to say about it.
                List<String> text = itemText(item.getId());
                if (text != null) {
                    wiki.set(item.getId(), text);
                }
            } catch (Exception | LinkageError ignored) {
                // A broken item should not break wiki registration.
            }
        }

        for (Map.Entry<ItemGroup, List<String>> entry : byGroup.entrySet()) {
            try {
                String groupKey = entry.getKey().getKey().getKey();
                String topicId = "addon_infinityexpansion_" + groupKey;

                wiki.registerTopic(new WikiTopic(topicId,
                        categoryTitle(groupKey), categoryIcon(groupKey), categoryTagline(groupKey)));
                wiki.setMechanic(topicId, categoryBlurb(groupKey));
                wiki.setTopicItems(topicId, entry.getValue());
            } catch (Exception | LinkageError ignored) {
                // A broken group should not break wiki registration.
            }
        }
    }

    private static String categoryTitle(String groupKey) {
        switch (groupKey) {
            case "main_materials": return "Infinity Expansion: Materials";
            case "basic_machines": return "Infinity Expansion: Basic Machines";
            case "advanced_machines": return "Infinity Expansion: Advanced Machines";
            case "storage": return "Infinity Expansion: Storage Units";
            case "mob_simulation": return "Infinity Expansion: Mob Simulation";
            case "infinity_materials": return "Infinity Expansion: Infinity Materials";
            case "infinity_cheat": return "Infinity Expansion: Infinity Tier";
            default: return "Infinity Expansion";
        }
    }

    private static XMaterial categoryIcon(String groupKey) {
        switch (groupKey) {
            case "main_materials": return XMaterial.NETHER_STAR;
            case "basic_machines": return XMaterial.LOOM;
            case "advanced_machines": return XMaterial.BLAST_FURNACE;
            case "storage": return XMaterial.BEEHIVE;
            case "mob_simulation": return XMaterial.BEACON;
            case "infinity_materials": return XMaterial.NETHERITE_BLOCK;
            case "infinity_cheat": return XMaterial.RESPAWN_ANCHOR;
            default: return XMaterial.NETHER_STAR;
        }
    }

    private static String categoryTagline(String groupKey) {
        switch (groupKey) {
            case "main_materials": return "&7Alloys, singularities & components";
            case "basic_machines": return "&7Your first power & automation";
            case "advanced_machines": return "&7Quarries, generators & processing";
            case "storage": return "&7Bottomless item storage units";
            case "mob_simulation": return "&7Farm mob drops without spawning mobs";
            case "infinity_materials": return "&7The cosmos, condensed";
            case "infinity_cheat": return "&7End-game infinity machinery";
            default: return "&7End-game tech and storage";
        }
    }

    private static List<String> categoryBlurb(String groupKey) {
        switch (groupKey) {
            case "main_materials": return Arrays.asList(
                    "&7The crafting backbone of the addon.", "",
                    "&7Smelt vanilla and Slimefun metals into",
                    "&7&lMagSteel&7, &lTitanium&7, &lMythril &7and &lAdamantite&7,",
                    "&7then plate and wire them into &fMachine Plates&7,",
                    "&fCircuits &7and &fCores &7that every machine needs.", "",
                    "&7Compress ores into &eSingularities&7, fuse them",
                    "&7into the four element singularities, and finally",
                    "&7forge the &bInfinity Ingot &7- the gateway to",
                    "&7every infinity-tier item.", "",
                    "&7Also home to the &6Infinity Workbench &7and",
                    "&cAdvanced Anvil&7. Click an item for its recipe.");
            case "basic_machines": return Arrays.asList(
                    "&7Entry-level machines to get you started.", "",
                    "&7These run on modest amounts of energy and",
                    "&7are crafted in the Enhanced Crafting Table.", "",
                    "&7Generate your first power with the &9Hydro",
                    "&7Generator &7or &9Basic Solar Panel&7, automate",
                    "&7crops and trees with the &aVirtual Farm &7and",
                    "&2Tree Grower&7, and mass-produce stone with the",
                    "&8Basic Cobble Generator&7.", "",
                    "&7Each tier upgrades into its Advanced and",
                    "&7Infinity counterpart later on.");
            case "advanced_machines": return Arrays.asList(
                    "&7The heart of mid-to-late game automation.", "",
                    "&7High-throughput processing, generation and",
                    "&7mining - all hungry for energy, so build out",
                    "&7your power grid first.", "",
                    "&7Includes &cQuarries &7that mine ores from thin",
                    "&7air, the &8Stoneworks Factory&7, &8Dust Extractor",
                    "&7and &8Ingot Former &7for ore processing, faster",
                    "&7Geo Miners, Smelteries, Enchanters and Chargers,",
                    "&7plus &cGeothermal&7, &cSolar &7and &8Void &7generators.", "",
                    "&7Feed singularities to the &6Resource Synthesizer",
                    "&7to print rare Slimefun resources in bulk.");
            case "storage": return Arrays.asList(
                    "&7Massive single-item storage units.", "",
                    "&7A Storage Unit holds one item type, but in",
                    "&7enormous quantities - from &e6,400 &7items at",
                    "&7&9Basic &7tier up to &e1.6 billion &7at &bInfinity&7.", "",
                    "&7Right-click the block to set the held item,",
                    "&7then hopper or insert items as normal. The",
                    "&7status display shows the live stored amount.", "",
                    "&7Outgrown a tier? The &6Storage Forge &7upgrades",
                    "&7a unit in place &awithout &7losing its contents.", "",
                    "&7Break a unit to recover everything inside.");
            case "mob_simulation": return Arrays.asList(
                    "&7Farm mob drops without ever spawning a mob.", "",
                    "&7Craft an &8Empty Data Card&7, then feed it and",
                    "&7that mob's signature items into the &8Mob Data",
                    "&7Infuser &7to imprint the creature onto the card.", "",
                    "&7Slot the filled card into a &8Mob Simulation",
                    "&7Chamber&7, supply power, and it will continuously",
                    "&7generate that mob's drops - safely and silently.", "",
                    "&7Cards range from &aPassive &7animals to &4Hostile",
                    "&7mobs and &5Boss &7tiers like the Wither and Ender",
                    "&7Dragon, each with rarer, more valuable yields.");
            case "infinity_materials": return Arrays.asList(
                    "&7The pinnacle crafting tier.", "",
                    "&7Built around the &bInfinity Ingot &7and &8Void",
                    "&7Ingot&7, condensed from singularities and Void",
                    "&7Bits harvested from nothing.", "",
                    "&7These materials feed every infinity machine,",
                    "&7tool and armor piece in the addon.");
            case "infinity_cheat": return Arrays.asList(
                    "&7The end-game infinity tier.", "",
                    "&7Every item here is crafted in the &6Infinity",
                    "&7Workbench &7- a 6x6 multiblock - using &bInfinity",
                    "&7and &8Void &7Ingots plus &bInfinite Circuits &7and",
                    "&bCores&7.", "",
                    "&7You'll find the &bInfinity Reactor&7, infinite",
                    "&7generators, the &bInfinity Quarry&7, instant",
                    "&7Enchanters and Chargers, the flight-granting",
                    "&fInfinity Matrix&7, and the full set of cosmic",
                    "&7armor, tools and weapons.", "",
                    "&cNote: &7recipes shown here are intentionally",
                    "&cscrambled &7- the real layout is in the workbench.");
            default: return Arrays.asList(
                    "&7End-game tech and storage.", "",
                    "&7Infinity-tier machines, huge storage units,",
                    "&7mob & material chambers and infinite resource",
                    "&7generation.", "",
                    "&7Click an item below for its recipe.");
        }
    }

    private static List<String> itemText(String id) {
        switch (id) {
            // --- Core infinity materials ---
            case "VOID_BIT": return Arrays.asList(
                    "&7The most fundamental Void material.",
                    "&7Harvested from nothing by the &8Void Harvester&7.",
                    "&7Nine combine into &8Void Dust&7.");
            case "VOID_DUST": return Arrays.asList(
                    "&7Nine &8Void Bits &7compressed together.",
                    "&7Nine of these form a single &8Void Ingot&7.");
            case "VOID_INGOT": return Arrays.asList(
                    "&7A staple end-game crafting material.",
                    "&7Made from 9 &8Void Dust &7in the Enhanced",
                    "&7Crafting Table. Used in nearly every infinity recipe.");
            case "INFINITE_INGOT": return Arrays.asList(
                    "&7The signature material of the addon.",
                    "&7Smelted in the &6Smeltery &7from the four element",
                    "&7singularities, Mythril and a Void Ingot.",
                    "&7Required for all infinity gear and machines.");
            case "INFINITY_SINGULARITY": return Arrays.asList(
                    "&7100 &bInfinity Ingots &7condensed into one.",
                    "&7The densest material the addon offers.");
            case "MAGSTEEL": return Arrays.asList(
                    "&7A basic alloy smelted from Magnesium and Steel.",
                    "&7The first custom metal you'll make; used widely",
                    "&7in basic machines and plates.");
            case "TITANIUM": return Arrays.asList(
                    "&7A tough alloy smelted from reinforced Slimefun metals.",
                    "&7Used in Machine Cores and advanced components.");
            case "MYTHRIL": return Arrays.asList(
                    "&7An alloy of Reinforced Alloy, Iron Singularity",
                    "&7and Hardened Metal. A key Infinity Ingot ingredient.");
            case "ADAMANTITE": return Arrays.asList(
                    "&7A dense alloy of Redstone Alloy, a Diamond",
                    "&7Singularity and MagSteel. Powers the Fortune Singularity.");
            case "MAGNONIUM": return Arrays.asList(
                    "&7A magical alloy of MagSteel, a Magnesium",
                    "&7Singularity and Ender Essence.");
            case "MACHINE_PLATE": return Arrays.asList(
                    "&fA core machine component.",
                    "&7Pressed from Reinforced Alloy, plates and Titanium.",
                    "&7Forms the frame of most advanced machines.");
            case "MACHINE_CIRCUIT": return Arrays.asList(
                    "&6A core machine component.",
                    "&7Wired from Copper, an Electro Magnet and Silicon.",
                    "&7Used to control almost every machine.");
            case "MACHINE_CORE": return Arrays.asList(
                    "&fThe central component of advanced machines.",
                    "&7Built from Titanium, Circuits and a Machine Plate.");
            case "INFINITE_MACHINE_CIRCUIT": return Arrays.asList(
                    "&7An upgraded circuit for infinity machines.",
                    "&7Crafted in the &6Infinity Workbench&7.");
            case "INFINITE_MACHINE_CORE": return Arrays.asList(
                    "&7An upgraded core for infinity machines.",
                    "&7Crafted in the &6Infinity Workbench&7.");
            case "ENDER_ESSENCE": return Arrays.asList(
                    "&5A magical drop from the End.",
                    "&7Obtained from the Ender Dragon mob card and used",
                    "&7in Magnonium, the Ender Flame and the Vein Miner Rune.");
            case "ENDER_FLAME": return Arrays.asList(
                    "&cAn enchanted book holding &lFire Aspect X&7.",
                    "&7Crafted from Ender Essence around a Book in the",
                    "&7Magic Workbench. Used to forge the &6Sky Piercer&7.");

            // --- Key blocks / workbench ---
            case "INFINITY_FORGE": return Arrays.asList(
                    "&6The central multiblock of the addon.",
                    "&7A 6x6 crafting grid used to build every",
                    "&7infinity-tier item. Place it and right-click",
                    "&7to open the oversized recipe grid.",
                    "&8Consumes 10,000,000 J per craft.");
            case "ADVANCED_ANVIL": return Arrays.asList(
                    "&cCombines the enchantments of two tools or",
                    "&7armor pieces and can even upgrade them.",
                    "&bWorks with Slimefun items too.",
                    "&8Consumes 100,000 J per use.");
            case "VEIN_MINER_RUNE": return Arrays.asList(
                    "&bDrop this rune onto a tool to upgrade it.",
                    "&7Grants the ability to vein-mine certain materials,",
                    "&7breaking whole ore veins at once.");

            // --- Machines ---
            case "VOID_HARVESTER": return Arrays.asList(
                    "&8Slowly harvests &8Void Bits &7from nothing.",
                    "&7The only source of Void material, and the start",
                    "&7of the entire Void crafting line.");
            case "STONEWORKS_FACTORY": return Arrays.asList(
                    "&8An all-in-one stone processor.",
                    "&7Generates cobblestone and refines it into",
                    "&7gravel, sand, flint, glass and more in one block.");
            case "SINGULARITY_CONSTRUCTOR": return Arrays.asList(
                    "&fCondenses large stacks of a resource into",
                    "&7a single &eSingularity&7.",
                    "&7The standard way to make every singularity.");
            case "RESOURCE_SYNTHESIZER": return Arrays.asList(
                    "&6Combines two &eSingularities &7to print rare",
                    "&7Slimefun resources - Carbonado, Reinforced",
                    "&7Alloy, Solar Panels and more - in bulk.",
                    "&8Consumes 1,000,000 J per use.");
            case "GEO_QUARRY": return Arrays.asList(
                    "&fHarvests GEO resources (oil, salt, nether ice)",
                    "&7directly from the void using power - no GEO",
                    "&7scan or specific biome required.");
            case "DUST_EXTRACTOR": return Arrays.asList(
                    "&8Grinds cobblestone and other stones into",
                    "&7random metal dusts.",
                    "&7Pairs with the Ingot Former to make free metal.");
            case "INGOT_FORMER": return Arrays.asList(
                    "&8Smelts metal dusts into their ingots.",
                    "&7The other half of the Dust Extractor pipeline.");
            case "URANIUM_EXTRACTOR": return Arrays.asList(
                    "&aExtracts Small Uranium from common stone types.",
                    "&7A steady source of nuclear reactor fuel.");
            case "DECOMPRESSOR": return Arrays.asList(
                    "&7Reverses crafting: turns blocks back into their",
                    "&7base items (e.g. a Diamond Block into 9 diamonds),",
                    "&7including Compressed Cobblestone tiers.");
            case "GEAR_TRANSFORMER": return Arrays.asList(
                    "&7Changes the material of vanilla tools and armor",
                    "&7while keeping their enchantments and durability.",
                    "&8Consumes 12,000 J per use.");
            case "COBBLE_PRESS": return Arrays.asList(
                    "&8Compresses cobblestone through all five",
                    "&7Compressed Cobblestone tiers efficiently.");
            case "EXTREME_FREEZER": return Arrays.asList(
                    "&bConverts ice into Reactor Coolant Cells and",
                    "&7magma blocks into Nether Ice Coolant Cells.");
            case "POWERED_BEDROCK": return Arrays.asList(
                    "&4While powered, turns into real bedrock.",
                    "&7Reverts to an item when unpowered or broken.",
                    "&8Consumes 10,000 J/s.");

            // --- Material generators & growers ---
            case "BASIC_COBBLE_GEN": case "ADVANCED_COBBLE_GEN": case "INFINITY_COBBLE_GEN": return Arrays.asList(
                    "&7Generates cobblestone automatically from power.",
                    "&7Higher tiers produce far more per tick.");
            case "BASIC_OBSIDIAN_GEN": return Arrays.asList(
                    "&8Generates obsidian automatically from power.",
                    "&7A renewable obsidian source for big builds.");
            case "BASIC_VIRTUAL_FARM": case "ADVANCED_VIRTUAL_FARM": case "INFINITY_VIRTUAL_FARM": return Arrays.asList(
                    "&aAutomatically grows, harvests and replants crops.",
                    "&7Place seeds inside; it outputs the grown crop.",
                    "&7Higher tiers run dramatically faster.");
            case "BASIC_TREE_GROWER": case "ADVANCED_TREE_GROWER": case "INFINITY_TREE_GROWER": return Arrays.asList(
                    "&2Automatically grows and harvests trees.",
                    "&7Feed it saplings to receive logs, leaves and",
                    "&7extras. Higher tiers run much faster.");

            // --- Storage ---
            case "STORAGE_FORGE": return Arrays.asList(
                    "&6Upgrades a Storage Unit to the next tier.",
                    "&7Place a unit inside; its stored items are kept",
                    "&7through the upgrade.");
            case "BASIC_STORAGE": case "ADVANCED_STORAGE": case "REINFORCED_STORAGE":
            case "VOID_STORAGE": case "INFINITY_STORAGE": return Arrays.asList(
                    "&7Stores a single item type in huge amounts.",
                    "&7Right-click to set the item, then insert freely.",
                    "&7Break it to recover everything stored inside.");

            // --- Mob simulation ---
            case "EMPTY_DATA_CARD": return Arrays.asList(
                    "&8A blank card waiting to be imprinted.",
                    "&7Infuse it with a mob's signature items in the",
                    "&8Mob Data Infuser &7to capture that creature.");
            case "DATA_INFUSER": return Arrays.asList(
                    "&8Imprints a mob onto an Empty Data Card.",
                    "&7Supply the card plus that mob's themed items.",
                    "&8Consumes 20,000 J per use.");
            case "MOB_SIMULATION_CHAMBER": return Arrays.asList(
                    "&8Generates a mob's drops from its data card.",
                    "&7Slot in a filled card and supply power - drops",
                    "&7appear continuously, no spawning required.");

            // --- Generators ---
            case "INFINITY_REACTOR": return Arrays.asList(
                    "&bThe addon's ultimate power source.",
                    "&7Burns &8Void &7and &bInfinity &7Ingots to output",
                    "&7120,000 J/s. Built in the Infinity Workbench.");
            case "HYDRO_GENERATOR": case "REINFORCED_HYDRO_GENERATOR": return Arrays.asList(
                    "&9Generates energy from flowing water around it.",
                    "&7Surround it with water for maximum output.");
            case "GEOTHERMAL_GENERATOR": case "REINFORCED_GEOTHERMAL_GENERATOR": return Arrays.asList(
                    "&cGenerates energy from the heat of lava nearby.",
                    "&7Place it next to lava for full output.");
            case "BASIC_PANEL": case "ADVANCED_PANEL": case "CELESTIAL_PANEL": return Arrays.asList(
                    "&eA solar panel that generates energy in daylight.",
                    "&7Needs a clear view of the sky; idle at night.");
            case "VOID_PANEL": return Arrays.asList(
                    "&8An inverted panel that generates energy in",
                    "&7darkness instead of daylight.");
            case "INFINITE_PANEL": return Arrays.asList(
                    "&bGenerates massive energy from the cosmos,",
                    "&7day or night. Crafted in the Infinity Workbench.");

            // --- Quarries ---
            case "BASIC_QUARRY": case "ADVANCED_QUARRY": case "VOID_QUARRY": case "INFINITY_QUARRY": return Arrays.asList(
                    "&7Automatically mines ores using only power.",
                    "&7No digging - place it and supply energy. Higher",
                    "&7tiers mine faster and reach nether ores.",
                    "&7Add an &bOscillator &7to bias output toward a gem.");
            case "DIAMOND_OSCILLATOR": case "REDSTONE_OSCILLATOR": case "LAPIS_OSCILLATOR":
            case "QUARTZ_OSCILLATOR": case "EMERALD_OSCILLATOR": return Arrays.asList(
                    "&bPlace inside a Quarry to greatly raise the",
                    "&7chance of mining this specific resource.");

            // --- Slimefun extension machines ---
            case "ADVANCED_GEO_MINER": return Arrays.asList(
                    "&cA faster GEO Miner.",
                    "&7Extracts GEO resources from the chunk below it.");
            case "ADVANCED_SMELTERY": return Arrays.asList(
                    "&cA much faster Electric Smeltery.",
                    "&7Runs Slimefun alloy recipes at high speed.");
            case "ADVANCED_CHARGER": case "INFINITY_CHARGER": return Arrays.asList(
                    "&7Charges energy-using items placed inside.",
                    "&7Higher tiers charge far faster - the Infinity",
                    "&7tier charges instantly.");
            case "ADVANCED_ENCHANTER": case "INFINITY_ENCHANTER": return Arrays.asList(
                    "&7Applies enchantments from a book to an item.",
                    "&7Faster than the vanilla Auto Enchanter.");
            case "ADVANCED_DISENCHANTER": case "INFINITY_DISENCHANTER": return Arrays.asList(
                    "&7Strips enchantments off an item onto a book.",
                    "&7Faster than the vanilla Auto Disenchanter.");
            case "ADVANCED_NETHER_STAR_REACTOR": return Arrays.asList(
                    "&cBurns Nether Stars for strong power output.",
                    "&bMust be surrounded by water &7and fed Nether Ice",
                    "&7Coolant Cells. &4Withers nearby unshielded entities.");
            case "INFINITY_CAPACITOR": return Arrays.asList(
                    "&bStores 2 billion J of energy.",
                    "&c&oUse only one per energy network.");
            case "VOID_CAPACITOR": return Arrays.asList(
                    "&8A high-capacity capacitor storing 16,000,000 J.",
                    "&7Used in the Void Quarry and large grids.");

            // --- Infinity gear ---
            case "INFINITY_CROWN": case "INFINITY_CHESTPLATE": case "INFINITY_LEGGINGS": case "INFINITY_BOOTS": return Arrays.asList(
                    "&bPart of the cosmic Infinity armor set.",
                    "&7Grants powerful permanent potion effects and",
                    "&7damage immunities while worn. &bSoulbound&7.",
                    "&7Forged from Infinity and Void Ingots.");
            case "INFINITY_MATRIX": return Arrays.asList(
                    "&6Grants unlimited creative-style flight.",
                    "&7Right-click to claim and toggle; crouch +",
                    "&7right-click to release ownership. &bSoulbound&7.");
            case "INFINITY_BLADE": return Arrays.asList(
                    "&bAn end-game sword dealing immense damage.",
                    "&7Forged in the Infinity Workbench.");
            case "INFINITY_PICKAXE": return Arrays.asList(
                    "&9A pickaxe that shatters anything instantly",
                    "&7and vein-mines. Forged in the Infinity Workbench.");
            case "INFINITY_AXE": return Arrays.asList(
                    "&4An end-game axe that fells trees and foes alike.",
                    "&7Forged in the Infinity Workbench.");
            case "INFINITY_SHOVEL": return Arrays.asList(
                    "&aAn end-game shovel that clears terrain rapidly.",
                    "&7Forged in the Infinity Workbench.");
            case "INFINITY_BOW": return Arrays.asList(
                    "&6A devastating bow infused with the Ender Flame.",
                    "&7Forged in the Infinity Workbench.");
            case "INFINITY_SHIELD": return Arrays.asList(
                    "&bAn unbreakable shield of cosmic alloy.",
                    "&7Forged from Infinity and Void Ingots.");

            default: return null;
        }
    }

    @Override
    public void disable() {

    }

}
