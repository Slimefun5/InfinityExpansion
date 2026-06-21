package io.github.mooy1.infinityexpansion;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;

import io.github.mooy1.infinityexpansion.categories.Groups;
import io.github.mooy1.infinityexpansion.commands.GiveRecipe;
import io.github.mooy1.infinityexpansion.commands.PrintItem;
import io.github.mooy1.infinityexpansion.commands.SetData;
import io.github.mooy1.infinityexpansion.items.Researches;
import io.github.mooy1.infinityexpansion.items.SlimefunExtension;
import io.github.mooy1.infinityexpansion.items.blocks.Blocks;
import io.github.mooy1.infinityexpansion.items.gear.Gear;
import io.github.mooy1.infinityexpansion.items.generators.Generators;
import io.github.mooy1.infinityexpansion.items.machines.Machines;
import io.github.mooy1.infinityexpansion.items.materials.Materials;
import io.github.mooy1.infinityexpansion.items.mobdata.MobData;
import io.github.mooy1.infinityexpansion.items.quarries.Quarries;
import io.github.mooy1.infinityexpansion.items.storage.Storage;
import io.github.mooy1.infinityexpansion.items.storage.StorageSaveFix;
import io.github.mooy1.infinitylib.common.Scheduler;
import io.github.mooy1.infinitylib.core.AbstractAddon;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.core.guide.wiki.WikiText;
import io.github.thebusybiscuit.slimefun5.core.guide.wiki.WikiTopic;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.libraries.keys.NamespacedKey;
import io.github.thebusybiscuit.slimefun5.libraries.xseries.XMaterial;

public final class InfinityExpansion extends AbstractAddon {

    public InfinityExpansion(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file,
                "Mooy1", "InfinityExpansion", "master", "auto-update");
    }

    public InfinityExpansion() {
        super("Mooy1", "InfinityExpansion", "master", "auto-update");
        StorageSaveFix.fixStuff(getLogger());
    }

    /**
     * Creates a version-safe {@link NamespacedKey} shim for PDC boundaries. The real
     * {@code org.bukkit.NamespacedKey} is only materialised reflectively at access time
     * (see {@code CompatUtils}), so this stays loadable on legacy servers without PDC.
     */
    public static NamespacedKey pdcKey(String key) {
        return createKey(key);
    }

    @Override
    protected void enable() {
        Plugin lx = getServer().getPluginManager().getPlugin("LiteXpansion");
        if (lx != null && lx.getConfig().getBoolean("options.nerf-other-addons")) {
            Scheduler.run(() -> log(Level.WARNING,
                    "########################################################",
                    "LiteXpansion nerfs energy generation in this addon.",
                    "You can disable these nerfs in the LiteXpansion config.",
                    "Under 'options:' add 'nerf-other-addons: false'",
                    "########################################################"
            ));
        }

        getAddonCommand()
                .addSub(new GiveRecipe())
                .addSub(new SetData())
                .addSub(new PrintItem());

        Groups.setup(this);
        MobData.setup(this);
        Materials.setup(this);
        Machines.setup(this);
        Quarries.setup(this);
        Gear.setup(this);
        Blocks.setup(this);
        Storage.setup(this);
        Generators.setup(this);
        SlimefunExtension.setup(this);

        if (getConfig().getBoolean("balance-options.enable-researches")) {
            Researches.setup();
        }

        // Contribute this addon's per-language item translations (languages/<lang>/items.yml).
        Slimefun.getItemTranslationService().registerTranslations(this);

        // Register this addon's own in-game wiki page (core does not auto-generate addon wikis).
        registerWiki();
    }

    private void registerWiki() {
        WikiText wiki = Slimefun.getWikiText();
        String topicId = "addon_infinityexpansion";

        wiki.registerTopic(new WikiTopic(topicId, "Infinity Expansion", XMaterial.NETHER_STAR, "&7End-game tech and storage"));
        wiki.setMechanic(topicId, Arrays.asList(
            "&7End-game tech and storage.", "",
            "&7Infinity-tier machines, huge storage", "&7units, mob & material chambers and", "&7infinite resource generation.", "",
            "&7Click an item below for its recipe."));

        // Collect this addon's own items dynamically - never hardcode item lists.
        List<String> items = new ArrayList<>();

        for (SlimefunItem item : Slimefun.getRegistry().getEnabledSlimefunItems()) {
            try {
                if (item.getAddon() == this) {
                    items.add(item.getId());
                }
            } catch (Exception | LinkageError ignored) {
                // A broken item should not break wiki registration.
            }
        }

        wiki.setTopicItems(topicId, items);
    }

    @Override
    public void disable() {

    }

}
