package io.github.mooy1.infinityexpansion.utils;

import java.util.function.UnaryOperator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.mooy1.infinityexpansion.InfinityExpansion;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;

/**
 * Resolves this addon's chat text for one viewer, from {@code languages/<lang>/messages.yml}.
 *
 * @implNote The {@link ChatColor} deliberately stays at the call site rather than moving into the
 *           translated value: a translator never edits a colour code they cannot see the effect of.
 *           A console recipient is handled by the service, which applies the same substitutions to
 *           the English literal and strips the colour.
 */
public final class Messages {

    private Messages() {}

    @Nonnull
    public static String get(@Nullable Player player, @Nonnull String key, @Nonnull String english) {
        return Slimefun.getAddonMessageService().getMessage(player, InfinityExpansion.instance(), key, english);
    }

    public static void send(@Nonnull CommandSender recipient, @Nonnull String key, @Nonnull String english) {
        Slimefun.getAddonMessageService().sendMessage(recipient, InfinityExpansion.instance(), key, english);
    }

    public static void send(@Nonnull CommandSender recipient, @Nonnull ChatColor colour, @Nonnull String key, @Nonnull String english) {
        send(recipient, colour, key, english, UnaryOperator.identity());
    }

    public static void send(@Nonnull CommandSender recipient, @Nonnull ChatColor colour, @Nonnull String key,
                            @Nonnull String english, @Nonnull UnaryOperator<String> placeholders) {
        Slimefun.getAddonMessageService().sendMessage(recipient, InfinityExpansion.instance(), key, english,
                line -> colour + placeholders.apply(line));
    }

    /**
     * The two-line refusal a machine gives when it cannot pay for a craft: the reason, then how far its
     * buffer has actually filled.
     *
     * @implNote The charge itself is wrapped in its own colour codes rather than coloured by the line,
     *           because the highlight sits mid-sentence and a translator may move it.
     */
    public static void sendNotEnoughEnergy(@Nonnull Player player, int charge, int capacity) {
        send(player, ChatColor.RED, "machine.no-energy", "Not enough energy!");
        send(player, ChatColor.GREEN, "machine.charge", "Charge: %charge%/%capacity% J",
                line -> line.replace("%charge%", ChatColor.RED + Integer.toString(charge) + ChatColor.GREEN)
                        .replace("%capacity%", Integer.toString(capacity)));
    }
}
