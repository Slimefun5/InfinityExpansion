package io.github.mooy1.infinityexpansion.utils;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Sound;

import io.github.thebusybiscuit.slimefun5.libraries.xseries.XSound;

/**
 * Version-safe {@link Sound} resolution. Modern enum names (1.9+) like {@code BLOCK_ANVIL_USE} or
 * {@code ITEM_BOOK_PAGE_TURN} do not exist on 1.8 (which uses {@code ANVIL_USE} etc.) and would throw
 * {@code NoSuchFieldError} if referenced as constants at class load. Resolving by name through
 * {@link XSound} maps each to the constant present on the running server, returning {@code null} when
 * no equivalent exists so callers can no-op instead of crashing. On 1.21.3+/26.x, where {@code Sound}
 * became a registry-backed interface and the shaded XSeries 9.10.0 can no longer initialise, resolution
 * falls back to reading the constant straight off {@code org.bukkit.Sound}.
 */
public final class SoundCompat {

    // Flipped off permanently the first time XSeries fails to initialise (1.21.3+ / 26.x).
    private static volatile boolean xSeriesUsable = true;

    private SoundCompat() {}

    /**
     * Resolves a modern {@link Sound} name to the {@link Sound} present on the running server, or
     * {@code null} if it cannot be resolved.
     */
    @Nullable
    public static Sound resolve(@Nonnull String soundName) {
        if (xSeriesUsable) {
            try {
                Sound viaXSeries = XSound.matchXSound(soundName).map(XSound::parseSound).orElse(null);
                if (viaXSeries != null) {
                    return viaXSeries;
                }
            } catch (Throwable x) {
                xSeriesUsable = false;
            }
        }

        return resolveByField(soundName);
    }

    @Nullable
    private static Sound resolveByField(@Nonnull String soundName) {
        String name = soundName.toUpperCase(Locale.ROOT).replace('.', '_').replace(' ', '_').replace('-', '_');

        try {
            Object value = Sound.class.getField(name).get(null);
            return value instanceof Sound ? (Sound) value : null;
        } catch (ReflectiveOperationException | RuntimeException x) {
            return null;
        }
    }
}
