package io.github.mooy1.infinityexpansion.commands;

import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.ChatColor;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.mooy1.infinityexpansion.MaterialCompat;
import io.github.thebusybiscuit.slimefun5.libraries.xseries.XMaterial;
import io.github.mooy1.infinityexpansion.items.storage.StorageUnit;
import io.github.mooy1.infinityexpansion.utils.Messages;
import io.github.mooy1.infinitylib.commands.SubCommand;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

public final class SetData extends SubCommand {

    public SetData() {
        super("setdata", "Set slimefun block data of the block you are looking at", "infinityexpansion.setdata");
    }

    @Override
    protected void execute(@Nonnull CommandSender commandSender, @Nonnull String[] strings) {
        if (!(commandSender instanceof Player)) {
            Messages.send(commandSender, "command.players-only", "Only players can use this!");
            return;
        }

        if (strings.length != 2) {
            Messages.send(commandSender, ChatColor.RED, "command.setdata-usage", "You must specify a key and value to set!");
            return;
        }

        Player p = (Player) commandSender;

        Block target = p.getTargetBlockExact(8, FluidCollisionMode.NEVER);

        if (target == null || target.getType() == MaterialCompat.safe(XMaterial.AIR)) {
            Messages.send(p, ChatColor.RED, "command.target-block", "You need to target a block to use this command!");
            return;
        }

        String id = BlockStorage.getLocationInfo(target.getLocation(), "id");

        if (id == null) {
            Messages.send(p, ChatColor.RED, "command.target-slimefun-block", "You need to target a slimefun block to use this command!");
            return;
        }

        if (strings[0].equals("id")) {
            Messages.send(p, ChatColor.RED, "command.id-immutable", "You cannot change the id of this block, it could cause internal issues!");
            return;
        }

        if (strings[1].equals("\\remove")) {
            Messages.send(p, ChatColor.GREEN, "command.data-removed", "Successfully removed value of key '%key%' in %id%",
                    line -> line.replace("%key%", strings[0]).replace("%id%", id));
            BlockStorage.addBlockInfo(target, strings[0], null);
        }
        else {
            Messages.send(p, ChatColor.GREEN, "command.data-set", "Successfully set key '%key%' to value '%value%' in %id%",
                    line -> line.replace("%key%", strings[0]).replace("%value%", strings[1]).replace("%id%", id));
            BlockStorage.addBlockInfo(target, strings[0], strings[1]);
        }

        SlimefunItem unit = SlimefunItem.getById(id);
        if (unit instanceof StorageUnit) {
            ((StorageUnit) unit).reloadCache(target);
        }
    }

    @Override
    protected void complete(@Nonnull CommandSender commandSender, @Nonnull String[] strings, @Nonnull List<String> list) {
        if (!(commandSender instanceof Player)) {
            return;
        }

        Player p = (Player) commandSender;

        Block target = p.getTargetBlockExact(8, FluidCollisionMode.NEVER);

        if (target == null || target.getType() == MaterialCompat.safe(XMaterial.AIR)) {
            return;
        }

        if (strings.length == 1) {
            if (BlockStorage.hasBlockInfo(target)) {
                list.addAll(BlockStorage.getLocationInfo(target.getLocation()).getKeys());
                list.remove("id");
            }
        }
        else if (strings.length == 2 && !strings[1].equals("id")) {
            String current = BlockStorage.getLocationInfo(target.getLocation(), strings[1]);
            if (current != null) {
                list.add(current);
                list.add("\\remove");
            }
        }
    }

}
