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
}
