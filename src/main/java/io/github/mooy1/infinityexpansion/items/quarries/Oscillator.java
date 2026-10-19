package io.github.mooy1.infinityexpansion.items.quarries;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.inventory.ItemStack;

import io.github.mooy1.infinityexpansion.MaterialCompat;
import io.github.mooy1.infinityexpansion.categories.Groups;
import io.github.mooy1.infinityexpansion.items.materials.Materials;
import io.github.mooy1.infinitylib.common.StackUtils;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun5.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun5.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun5.libraries.dough.items.ItemUtils;
import io.github.thebusybiscuit.slimefun5.libraries.xseries.XMaterial;

public final class Oscillator extends SlimefunItem {

    private static final Map<String, Oscillator> OSCILLATORS = new HashMap<>();

    public final XMaterial material;

    public final double chance;

    @Nullable
    public static Oscillator getOscillator(@Nullable ItemStack item) {
        if (item == null) {
            return null;
        }
        return OSCILLATORS.get(StackUtils.getId(item));
    }

    @Nonnull
    /**
     * @implNote The id comes from the {@link XMaterial} name because the runtime {@code Material} differs on 1.8,
     *           where lapis is {@code INK_SACK} with data value 4.
     */
    public static SlimefunItemStack create(XMaterial material, double chance) {
        return new SlimefunItemStack(
                "QUARRY_OSCILLATOR_" + material.name(),
                MaterialCompat.stack(material)
        );
    }

    public Oscillator(SlimefunItemStack item, double chance) {
        this(item, XMaterial.matchXMaterial(item.item()), chance);
    }

    private Oscillator(SlimefunItemStack item, XMaterial material, double chance) {
        super(Groups.MAIN_MATERIALS, item, RecipeType.ENHANCED_CRAFTING_TABLE, new ItemStack[] {
                Materials.MACHINE_PLATE.item(), SlimefunItems.BLISTERING_INGOT_3.item(), Materials.MACHINE_PLATE.item(),
                SlimefunItems.BLISTERING_INGOT_3.item(), MaterialCompat.stack(material), SlimefunItems.BLISTERING_INGOT_3.item(),
                Materials.MACHINE_PLATE.item(), SlimefunItems.BLISTERING_INGOT_3.item(), Materials.MACHINE_PLATE.item()
        });
        OSCILLATORS.put(getId(), this);
        this.material = material;
        this.chance = chance;
    }

}
