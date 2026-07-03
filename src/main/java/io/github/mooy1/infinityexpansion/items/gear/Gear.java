package io.github.mooy1.infinityexpansion.items.gear;

import io.github.mooy1.infinityexpansion.utils.CompatUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import io.github.mooy1.infinityexpansion.InfinityExpansion;
import io.github.mooy1.infinityexpansion.categories.Groups;
import io.github.mooy1.infinityexpansion.items.blocks.InfinityWorkbench;
import io.github.mooy1.infinityexpansion.items.materials.Materials;
import io.github.mooy1.infinityexpansion.utils.Util;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun5.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun5.implementation.SlimefunItems;
import io.github.mooy1.infinityexpansion.MaterialCompat;
import io.github.thebusybiscuit.slimefun5.libraries.xseries.XMaterial;

public final class Gear {

    private Gear() {}

    public static final SlimefunItemStack ENDER_FLAME = new SlimefunItemStack("ENDER_FLAME", MaterialCompat.safe(XMaterial.ENCHANTED_BOOK));
    public static final SlimefunItemStack CROWN = new SlimefunItemStack("INFINITY_CROWN", MaterialCompat.safe(XMaterial.NETHERITE_HELMET));
    public static final SlimefunItemStack CHESTPLATE = new SlimefunItemStack("INFINITY_CHESTPLATE", MaterialCompat.safe(XMaterial.NETHERITE_CHESTPLATE));
    public static final SlimefunItemStack LEGGINGS = new SlimefunItemStack("INFINITY_LEGGINGS", MaterialCompat.safe(XMaterial.NETHERITE_LEGGINGS));
    public static final SlimefunItemStack BOOTS = new SlimefunItemStack("INFINITY_BOOTS", MaterialCompat.safe(XMaterial.NETHERITE_BOOTS));
    public static final SlimefunItemStack INFINITY_MATRIX = new SlimefunItemStack("INFINITY_MATRIX", MaterialCompat.safe(XMaterial.NETHER_STAR));
    public static final SlimefunItemStack SHIELD = new SlimefunItemStack("INFINITY_SHIELD", MaterialCompat.safe(XMaterial.SHIELD));
    public static final SlimefunItemStack BLADE = new SlimefunItemStack("INFINITY_BLADE", MaterialCompat.safe(XMaterial.NETHERITE_SWORD));
    public static final SlimefunItemStack PICKAXE = new SlimefunItemStack("INFINITY_PICKAXE", MaterialCompat.safe(XMaterial.NETHERITE_PICKAXE));
    public static final SlimefunItemStack AXE = new SlimefunItemStack("INFINITY_AXE", MaterialCompat.safe(XMaterial.NETHERITE_AXE));
    public static final SlimefunItemStack SHOVEL = new SlimefunItemStack("INFINITY_SHOVEL", MaterialCompat.safe(XMaterial.NETHERITE_SHOVEL));
    public static final SlimefunItemStack BOW = new SlimefunItemStack("INFINITY_BOW", MaterialCompat.safe(XMaterial.BOW));
    public static final SlimefunItemStack VEIN_MINER_RUNE = new SlimefunItemStack("VEIN_MINER_RUNE", MaterialCompat.safe(XMaterial.DIAMOND));

    public static void setup(InfinityExpansion plugin) {
        addInfinityEnchants(plugin,
                CROWN, CHESTPLATE, LEGGINGS, BOOTS,
                AXE, BLADE, PICKAXE,
                SHIELD, SHOVEL, BOW
        );
        EnchantmentStorageMeta storageMeta = (EnchantmentStorageMeta) ENDER_FLAME.getItemMeta();
        Objects.requireNonNull(storageMeta).addStoredEnchant(Enchantment.FIRE_ASPECT, 10, true);
        ENDER_FLAME.setItemMeta(storageMeta);
        new SlimefunItem(Groups.MAIN_MATERIALS, ENDER_FLAME, RecipeType.MAGIC_WORKBENCH, new ItemStack[] {
                Materials.ENDER_ESSENCE.item(), Materials.ENDER_ESSENCE.item(), Materials.ENDER_ESSENCE.item(),
                Materials.ENDER_ESSENCE.item(), new ItemStack(MaterialCompat.safe(XMaterial.BOOK)), Materials.ENDER_ESSENCE.item(),
                Materials.ENDER_ESSENCE.item(), Materials.ENDER_ESSENCE.item(), Materials.ENDER_ESSENCE.item()
        }).register(plugin);
        new InfinityArmor(CROWN, CompatUtils.potionEffects(
                CompatUtils.potionEffect("NIGHT_VISION", 600, 0, false, false, false),
                CompatUtils.potionEffect("CONDUIT_POWER", 600, 0, false, false, false)
        ), new ItemStack[] {
                null, Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(), null,
                Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(),
                Materials.INFINITE_INGOT.item(), Materials.VOID_INGOT.item(), Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(), Materials.VOID_INGOT.item(), Materials.INFINITE_INGOT.item(),
                null, Materials.INFINITE_INGOT.item(), null, null, Materials.INFINITE_INGOT.item(), null,
                null, null, null, null, null, null,
                null, null, null, null, null, null
        }).register(plugin);
        new InfinityArmor(CHESTPLATE, CompatUtils.potionEffects(
                CompatUtils.potionEffect("DAMAGE_RESISTANCE", 600, 0, false, false, false),
                CompatUtils.potionEffect("INCREASE_DAMAGE", 600, 1, false, false, false),
                CompatUtils.potionEffect("FIRE_RESISTANCE", 600, 0, false, false, false)
        ), new ItemStack[] {
                null, Materials.INFINITE_INGOT.item(), null, null, Materials.INFINITE_INGOT.item(), null,
                Materials.INFINITE_INGOT.item(), Materials.VOID_INGOT.item(), Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(), Materials.VOID_INGOT.item(), Materials.INFINITE_INGOT.item(),
                Materials.VOID_INGOT.item(), Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(), Materials.VOID_INGOT.item(),
                Materials.VOID_INGOT.item(), Materials.INFINITE_INGOT.item(), Materials.VOID_INGOT.item(), Materials.VOID_INGOT.item(), Materials.INFINITE_INGOT.item(), Materials.VOID_INGOT.item(),
                null, Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(), null,
                null, Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(), null
        }).register(plugin);
        new InfinityArmor(LEGGINGS, CompatUtils.potionEffects(
                CompatUtils.potionEffect("FAST_DIGGING", 600, 2, false, false, false),
                CompatUtils.potionEffect("REGENERATION", 600, 0, false, false, false),
                CompatUtils.potionEffect("SATURATION", 600, 0, false, false, false)
        ), new ItemStack[] {
                null, Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(), null,
                Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(),
                Materials.VOID_INGOT.item(), Materials.INFINITE_INGOT.item(), null, null, Materials.INFINITE_INGOT.item(), Materials.VOID_INGOT.item(),
                Materials.VOID_INGOT.item(), Materials.INFINITE_INGOT.item(), null, null, Materials.INFINITE_INGOT.item(), Materials.VOID_INGOT.item(),
                Materials.VOID_INGOT.item(), Materials.INFINITE_INGOT.item(), null, null, Materials.INFINITE_INGOT.item(), Materials.VOID_INGOT.item(),
                null, Materials.INFINITE_INGOT.item(), null, null, Materials.INFINITE_INGOT.item(), null
        }).register(plugin);
        new InfinityArmor(BOOTS, CompatUtils.potionEffects(
                CompatUtils.potionEffect("SPEED", 600, 2, false, false, false),
                CompatUtils.potionEffect("DOLPHINS_GRACE", 600, 0, false, false, false)
        ), new ItemStack[] {
                null, null, null, null, null, null,
                Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(), null, null, Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(),
                Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(), null, null, Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(),
                Materials.VOID_INGOT.item(), Materials.VOID_INGOT.item(), null, null, Materials.VOID_INGOT.item(), Materials.VOID_INGOT.item(),
                Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(), null, null, Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(),
                Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(), null, null, Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item()
        }).register(plugin);
        new InfinityTool(SHIELD, new ItemStack[] {
                Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(), null, null, Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(),
                Materials.INFINITE_INGOT.item(), Materials.VOID_INGOT.item(), Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(), Materials.VOID_INGOT.item(), Materials.INFINITE_INGOT.item(),
                Materials.INFINITE_INGOT.item(), Materials.VOID_INGOT.item(), Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(), Materials.VOID_INGOT.item(), Materials.INFINITE_INGOT.item(),
                Materials.INFINITE_INGOT.item(), Materials.VOID_INGOT.item(), Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(), Materials.VOID_INGOT.item(), Materials.INFINITE_INGOT.item(),
                null, Materials.INFINITE_INGOT.item(), Materials.VOID_INGOT.item(), Materials.VOID_INGOT.item(), Materials.INFINITE_INGOT.item(), null,
                null, Materials.INFINITE_INGOT.item(), Materials.VOID_INGOT.item(), Materials.VOID_INGOT.item(), Materials.INFINITE_INGOT.item(), null
        }).register(plugin);
        new InfinityBow(Groups.INFINITY_CHEAT, BOW, InfinityWorkbench.TYPE, new ItemStack[] {
                null, Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(), Materials.VOID_INGOT.item(), null, null,
                Materials.INFINITE_INGOT.item(), null, Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(), Materials.VOID_INGOT.item(), null,
                Materials.VOID_INGOT.item(), null, null, ENDER_FLAME.item(), Materials.INFINITE_INGOT.item(), Materials.VOID_INGOT.item(),
                null, Materials.VOID_INGOT.item(), null, null, Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(),
                null, null, Materials.VOID_INGOT.item(), null, null, Materials.INFINITE_INGOT.item(),
                null, null, null, Materials.VOID_INGOT.item(), Materials.INFINITE_INGOT.item(), null
        }).register(plugin);
        new InfinityTool(AXE, new ItemStack[] {
                null, Materials.VOID_INGOT.item(), Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(), null, null,
                Materials.VOID_INGOT.item(), Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(), Materials.VOID_INGOT.item(), null,
                null, Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(), Materials.VOID_INGOT.item(), Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(),
                null, null, Materials.VOID_INGOT.item(), Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(),
                null, Materials.VOID_INGOT.item(), null, Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(), Materials.VOID_INGOT.item(),
                Materials.VOID_INGOT.item(), null, null, null, Materials.VOID_INGOT.item(), null
        }).register(plugin);
        new InfinityTool(BLADE, new ItemStack[] {
                null, null, null, null, Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(),
                null, null, null, Materials.INFINITE_INGOT.item(), Materials.VOID_INGOT.item(), Materials.INFINITE_INGOT.item(),
                null, null, Materials.INFINITE_INGOT.item(), Materials.VOID_INGOT.item(), Materials.INFINITE_INGOT.item(), null,
                Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(), Materials.VOID_INGOT.item(), Materials.INFINITE_INGOT.item(), null, null,
                null, Materials.VOID_INGOT.item(), Materials.INFINITE_INGOT.item(), null, null, null,
                Materials.VOID_INGOT.item(), null, Materials.INFINITE_INGOT.item(), null, null, null
        }).register(plugin);
        new InfinityTool(SHOVEL, new ItemStack[] {
                null, null, null, Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(),
                null, null, Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(),
                null, null, Materials.INFINITE_INGOT.item(), Materials.VOID_INGOT.item(), Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(),
                null, null, Materials.VOID_INGOT.item(), Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(), null,
                null, Materials.VOID_INGOT.item(), null, null, null, null,
                Materials.VOID_INGOT.item(), null, null, null, null, null
        }).register(plugin);
        new InfinityTool(PICKAXE, new ItemStack[] {
                null, Materials.VOID_INGOT.item(), Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(), null,
                null, null, null, Materials.INFINITE_INGOT.item(), Materials.VOID_INGOT.item(), Materials.INFINITE_INGOT.item(),
                null, null, null, Materials.VOID_INGOT.item(), Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(),
                null, null, Materials.VOID_INGOT.item(), null, null, Materials.INFINITE_INGOT.item(),
                null, Materials.VOID_INGOT.item(), null, null, null, Materials.VOID_INGOT.item(),
                Materials.VOID_INGOT.item(), null, null, null, null, null
        }).register(plugin);
        new InfinityMatrix(Groups.INFINITY_CHEAT, INFINITY_MATRIX, InfinityWorkbench.TYPE, new ItemStack[] {
                Materials.INFINITE_INGOT.item(), null, Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(), null, Materials.INFINITE_INGOT.item(),
                Materials.INFINITE_INGOT.item(), Materials.VOID_INGOT.item(), Materials.VOID_INGOT.item(), Materials.VOID_INGOT.item(), Materials.VOID_INGOT.item(), Materials.INFINITE_INGOT.item(),
                Materials.VOID_INGOT.item(), Materials.VOID_INGOT.item(), new ItemStack(MaterialCompat.safe(XMaterial.ELYTRA)), new ItemStack(MaterialCompat.safe(XMaterial.ELYTRA)), Materials.VOID_INGOT.item(), Materials.VOID_INGOT.item(),
                Materials.VOID_INGOT.item(), Materials.VOID_INGOT.item(), Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(), Materials.VOID_INGOT.item(), Materials.VOID_INGOT.item(),
                Materials.INFINITE_INGOT.item(), Materials.VOID_INGOT.item(), Materials.VOID_INGOT.item(), Materials.VOID_INGOT.item(), Materials.VOID_INGOT.item(), Materials.INFINITE_INGOT.item(),
                Materials.INFINITE_INGOT.item(), null, Materials.INFINITE_INGOT.item(), Materials.INFINITE_INGOT.item(), null, Materials.INFINITE_INGOT.item()
        }).register(plugin);
        new VeinMinerRune(Groups.MAIN_MATERIALS, VEIN_MINER_RUNE, RecipeType.MAGIC_WORKBENCH, new ItemStack[] {
                Materials.MAGSTEEL_PLATE.item(), SlimefunItems.PICKAXE_OF_VEIN_MINING.item(), Materials.MAGSTEEL_PLATE.item(),
                Materials.ENDER_ESSENCE.item(), SlimefunItems.BLANK_RUNE.item(), Materials.ENDER_ESSENCE.item(),
                Materials.MAGSTEEL_PLATE.item(), SlimefunItems.PICKAXE_OF_VEIN_MINING.item(), Materials.MAGSTEEL_PLATE.item(),
        }).register(plugin);
    }

    private static void addInfinityEnchants(InfinityExpansion plugin, SlimefunItemStack... items) {
        ConfigurationSection typeSection = plugin.getConfig().getConfigurationSection("infinity-enchant-levels");

        if (typeSection == null) {
            InfinityExpansion.log(Level.SEVERE, "Config section \"infinity-enchant-levels\" missing, Check your config and report this!");
            return;
        }

        for (SlimefunItemStack item : items) {
            ItemMeta meta = item.getItemMeta();

            // lore
            List<String> lore;
            if (meta.hasLore()) {
                lore = meta.getLore();
            }
            else {
                lore = new ArrayList<>();
            }
            lore.add(ChatColor.AQUA + "Soulbound");
            meta.setLore(lore);

            // find path
            String itemPath = item.getItemId().replace("INFINITY_", "").toLowerCase();
            ConfigurationSection itemSection = typeSection.getConfigurationSection(itemPath);

            // unbreakable and enchants
            boolean unbreakable = Objects.requireNonNull(itemSection).getBoolean("unbreakable");
            try {
                meta.getClass().getMethod("setUnbreakable", boolean.class).invoke(meta, unbreakable);
            } catch (ReflectiveOperationException ignored) {
                // setUnbreakable is 1.11+, no-op on older versions
            }
            for (Map.Entry<Enchantment, Integer> entry : Util.getEnchants(itemSection).entrySet()) {
                meta.addEnchant(entry.getKey(), entry.getValue(), true);
            }

            item.setItemMeta(meta);
        }
    }

}
