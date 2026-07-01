package io.github.mooy1.infinityexpansion.items.materials;

import javax.annotation.Nullable;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.thebusybiscuit.slimefun5.libraries.keys.NamespacedKey;

import io.github.mooy1.infinityexpansion.InfinityExpansion;
import io.github.mooy1.infinityexpansion.categories.Groups;
import io.github.mooy1.infinityexpansion.utils.CompatUtils;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun5.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun5.core.attributes.NotPlaceable;

/**
 * Items to be used in the Strainer Base
 *
 * @author Mooy1
 */
public final class Strainer extends SlimefunItem implements NotPlaceable {

    private static final NamespacedKey KEY = InfinityExpansion.pdcKey("strainer_speed");

    public Strainer(SlimefunItemStack item, ItemStack[] recipe, int speed) {
        super(Groups.BASIC_MACHINES, item, RecipeType.ENHANCED_CRAFTING_TABLE, recipe);
        ItemMeta meta = item.getItemMeta();
        CompatUtils.setPdcInt(meta, KEY, speed);
        item.setItemMeta(meta);
    }

    /**
     * This method gets the speed of strainer from an item
     *
     * @return speed
     */
    public static int getStrainer(@Nullable ItemStack item) {
        if (item != null && item.hasItemMeta()) {
            return CompatUtils.getPdcInt(item.getItemMeta(), Strainer.KEY, 0);
        }
        return 0;
    }

}
