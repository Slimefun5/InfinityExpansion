package io.github.mooy1.infinityexpansion.utils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Sound;

import io.github.thebusybiscuit.slimefun5.libraries.xseries.XSound;

/**
 * Version-safe {@link Sound} resolution. Modern enum names (1.9+) like {@code BLOCK_ANVIL_USE} or
 * {@code ITEM_BOOK_PAGE_TURN} do not exist on 1.8 (which uses {@code ANVIL_USE} etc.) and would throw
 * {@code NoSuchFieldError} if referenced as constants at class load. Resolving by name through
 * {@link XSound} maps each to the constant present on the running server, returning {@code null} when
 * no equivalent exists so callers can no-op instead of crashing.
 */
public final class SoundCompat {

    private SoundCompat() {}

    /**
     * Resolves a modern {@link Sound} name to the {@link Sound} present on the running server, or
     * {@code null} if it cannot be resolved.
     */
    @Nullable
    public static Sound resolve(@Nonnull String soundName) {
        return XSound.matchXSound(soundName).map(XSound::parseSound).orElse(null);
    }
}
