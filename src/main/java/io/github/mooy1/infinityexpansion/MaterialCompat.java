package io.github.mooy1.infinityexpansion;

import javax.annotation.Nonnull;

import org.bukkit.Material;

import io.github.thebusybiscuit.slimefun5.libraries.xseries.XMaterial;

/**
 * Resolves {@link XMaterial} constants to a {@link Material} that exists on the
 * running server. Keeps InfinityExpansion loadable on legacy versions (e.g. 1.8)
 * where modern constants like {@code NETHERITE_SCRAP} or {@code SMOOTH_STONE} are absent.
 *
 * @author Mooy1
 */
public final class MaterialCompat {

    private MaterialCompat() {}

    @Nonnull
    public static Material safe(@Nonnull XMaterial material) {
        Material resolved = material.parseMaterial();
        return resolved != null ? resolved : Material.STONE;
    }

    /**
     * Resolves an {@link XMaterial} to an {@link ItemStack}, preserving the legacy data value
     * (e.g. SKULL_ITEM:1 = wither skull) that {@link #safe(XMaterial)} drops on 1.8-1.12.
     */
    @javax.annotation.Nonnull
    public static org.bukkit.inventory.ItemStack stack(@javax.annotation.Nonnull XMaterial material) {
        org.bukkit.inventory.ItemStack item = material.parseItem();
        return item != null ? item : new org.bukkit.inventory.ItemStack(safe(material));
    }

    /** Amount-aware variant of {@link #stack(XMaterial)}. */
    @javax.annotation.Nonnull
    public static org.bukkit.inventory.ItemStack stack(@javax.annotation.Nonnull XMaterial material, int amount) {
        org.bukkit.inventory.ItemStack item = stack(material);
        item.setAmount(amount);
        return item;
    }
}
