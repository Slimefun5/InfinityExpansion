package io.github.mooy1.infinityexpansion.items.mobdata;

import org.bukkit.Material;
import io.github.mooy1.infinityexpansion.MaterialCompat;
import io.github.thebusybiscuit.slimefun5.libraries.xseries.XMaterial;

public enum MobDataTier {

    // ex: chicken
    PASSIVE(1, 75, MaterialCompat.safe(XMaterial.IRON_CHESTPLATE)),

    // ex: slime
    NEUTRAL(1, 150, MaterialCompat.safe(XMaterial.IRON_CHESTPLATE)),

    // ex: zombie
    HOSTILE(2, 300, MaterialCompat.safe(XMaterial.DIAMOND_CHESTPLATE)),

    // ex: endermen
    ADVANCED(4, 600, MaterialCompat.safe(XMaterial.DIAMOND_CHESTPLATE)),

    // ex: wither
    MINI_BOSS(32, 4500, MaterialCompat.safe(XMaterial.NETHERITE_CHESTPLATE)),

    // ex: ender dragon
    BOSS(96, 9000, MaterialCompat.safe(XMaterial.NETHERITE_CHESTPLATE));

    final int xp;
    final int energy;
    final Material material;

    MobDataTier(int xp, int energy, Material material) {
        this.xp = (int) (xp * MobSimulationChamber.XP_MULTIPLIER);
        this.energy = energy;
        this.material = material;
    }

}
