package io.github.mooy1.infinityexpansion.commands;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.mooy1.infinityexpansion.utils.Messages;
import io.github.mooy1.infinitylib.commands.SubCommand;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun5.core.multiblocks.MultiBlockMachine;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;

public final class GiveRecipe extends SubCommand {

    public GiveRecipe() {
        super("giverecipe", "gives all the items in a Slimefun item recipe", "infinityexpansion.giverecipe");
    }

    @Override
    protected void execute(@Nonnull CommandSender sender, @Nonnull String[] args) {
        if (!(sender instanceof Player)) {
            Messages.send(sender, "command.players-only", "Only players can use this!");
            return;
        }

        if (args.length != 1) {
            Messages.send(sender, "command.giverecipe-usage", "Usage: /ie giverecipe <ID>");
            return;
        }

        SlimefunItem sfItem = SlimefunItem.getById(args[0].toUpperCase());

        if (sfItem == null || sfItem instanceof MultiBlockMachine || sfItem.getRecipeType() == RecipeType.GEO_MINER) {
            Messages.send(sender, ChatColor.RED, "command.invalid-item", "Invalid Slimefun item!");
            return;
        }

        Player p = (Player) sender;

        Messages.send(p, ChatColor.GREEN, "command.gave-recipe", "Gave recipe for %item%",
                line -> line.replace("%item%", Slimefun.getItemTranslationService().getName(p, sfItem)));

        List<ItemStack> recipe = new ArrayList<>();

        for (ItemStack e : sfItem.getRecipe()) {
            if (e != null) {
                recipe.add(e);
            }
        }

        p.getInventory().addItem(recipe.toArray(new ItemStack[0]));
    }

    @Override
    protected void complete(@Nonnull CommandSender sender, @Nonnull String[] args, @Nonnull List<String> tabs) {
        if (args.length == 1) {
            for (SlimefunItem item : Slimefun.getRegistry().getEnabledSlimefunItems()) {
                tabs.add(item.getId());
            }
        }
    }

}