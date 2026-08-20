package io.github.mooy1.infinityexpansion.utils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import io.github.thebusybiscuit.slimefun5.libraries.keys.NamespacedKey;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.BukkitKeys;

/**
 * Java-8 universal port: reflective guards around Bukkit API members that only exist on MC 1.13+/1.14+
 * (the persistent-data container and {@code Block#getBlockData()}). On servers without them
 * (1.8&ndash;1.12) these helpers degrade gracefully instead of throwing
 * {@code NoSuchMethodError}/{@code NoClassDefFoundError}, while behaving identically on modern versions.
 *
 * <p>All persistent-data access is routed through reflection on the real {@code org.bukkit.NamespacedKey}
 * (obtained from the bundled {@link NamespacedKey} shim via {@link BukkitKeys#toBukkit(NamespacedKey)}),
 * so neither {@code PersistentDataType} nor {@code PersistentDataContainer} is referenced in this addon's
 * bytecode.
 */
public final class CompatUtils {

    private static final boolean BLOCK_DATA_AVAILABLE = classExists("org.bukkit.block.data.BlockData");

    private CompatUtils() {}

    /**
     * Builds a {@link PotionEffect} version-safely: the 6-arg (icon) ctor is 1.13+ and the 5-arg
     * (particles) is 1.9+, so fall back to the 4-arg ctor (present since 1.8) on legacy servers.
     */
    public static PotionEffect potionEffect(String typeName, int duration, int amplifier, boolean ambient, boolean particles, boolean icon) {
        // Resolve by name so a 1.13+ type (e.g. CONDUIT_POWER) is just skipped on legacy MC instead of
        // a NoSuchFieldError at the constant reference.
        PotionEffectType type = PotionEffectType.getByName(typeName);
        if (type == null) {
            return null;
        }
        try {
            return PotionEffect.class.getConstructor(PotionEffectType.class, int.class, int.class, boolean.class, boolean.class, boolean.class)
                    .newInstance(type, duration, amplifier, ambient, particles, icon);
        } catch (ReflectiveOperationException e6) {
            try {
                return PotionEffect.class.getConstructor(PotionEffectType.class, int.class, int.class, boolean.class, boolean.class)
                        .newInstance(type, duration, amplifier, ambient, particles);
            } catch (ReflectiveOperationException e5) {
                return new PotionEffect(type, duration, amplifier, ambient);
            }
        }
    }

    /** Returns the non-null effects (a type absent on this MC version yields a null from {@link #potionEffect}). */
    public static PotionEffect[] potionEffects(PotionEffect... effects) {
        java.util.List<PotionEffect> list = new java.util.ArrayList<>();
        for (PotionEffect e : effects) {
            if (e != null) {
                list.add(e);
            }
        }
        return list.toArray(new PotionEffect[0]);
    }

    public static boolean isBlockDataAvailable() {
        return BLOCK_DATA_AVAILABLE;
    }

    /**
     * Reflectively invokes {@code Block#getBlockData()}, returning the result typed as {@link Object}
     * so {@code BlockData} is never referenced in bytecode. Returns {@code null} on versions without it.
     */
    @Nullable
    public static Object getBlockData(@Nonnull Block block) {
        if (!BLOCK_DATA_AVAILABLE) {
            return null;
        }

        try {
            return Block.class.getMethod("getBlockData").invoke(block);
        } catch (ReflectiveOperationException e) {
            return null;
        }
    }

    /**
     * Returns the facing of a directional block (e.g. a wall sign) via {@code Directional#getFacing()},
     * resolved reflectively off the block's {@code BlockData}. Returns {@code null} on versions without
     * {@code BlockData} or for non-directional blocks.
     */
    @Nullable
    public static BlockFace getDirectionalFacing(@Nonnull Block block) {
        Object blockData = getBlockData(block);
        if (blockData == null) {
            return null;
        }

        try {
            Class<?> directional = Class.forName("org.bukkit.block.data.Directional");
            if (!directional.isInstance(blockData)) {
                return null;
            }
            Object facing = directional.getMethod("getFacing").invoke(blockData);
            return facing instanceof BlockFace ? (BlockFace) facing : null;
        } catch (ReflectiveOperationException e) {
            return null;
        }
    }

    // --- Item durability (Damageable is 1.13+) routed through reflection, never referenced in bytecode ---

    /**
     * Returns whether the item supports a damage value. On 1.13+ this checks the meta reflectively;
     * on older versions it falls back to whether the item type has a max durability.
     */
    public static boolean isDamageable(@Nonnull ItemStack stack) {
        ItemMeta meta = stack.getItemMeta();
        if (meta != null) {
            try {
                Class<?> damageable = Class.forName("org.bukkit.inventory.meta.Damageable");
                return damageable.isInstance(meta);
            } catch (ReflectiveOperationException ignored) {
                // fall through to the legacy check
            }
        }
        return stack.getType().getMaxDurability() > 0;
    }

    /**
     * Reads the item's damage via {@code Damageable#getDamage()} reflectively on 1.13+,
     * falling back to {@link ItemStack#getDurability()} on older versions.
     */
    public static int getDamage(@Nonnull ItemStack stack) {
        ItemMeta meta = stack.getItemMeta();
        if (meta != null) {
            try {
                Method getDamage = meta.getClass().getMethod("getDamage");
                Object result = getDamage.invoke(meta);
                if (result instanceof Integer) {
                    return (Integer) result;
                }
            } catch (ReflectiveOperationException ignored) {
                // fall through to the legacy durability
            }
        }
        return stack.getDurability();
    }

    /**
     * Writes the item's damage via {@code Damageable#setDamage(int)} + {@code setItemMeta} reflectively
     * on 1.13+, falling back to {@link ItemStack#setDurability(short)} on older versions.
     */
    public static void setDamage(@Nonnull ItemStack stack, int damage) {
        ItemMeta meta = stack.getItemMeta();
        if (meta != null) {
            try {
                Method setDamage = meta.getClass().getMethod("setDamage", int.class);
                setDamage.invoke(meta, damage);
                stack.setItemMeta(meta);
                return;
            } catch (ReflectiveOperationException ignored) {
                // fall through to the legacy durability
            }
        }
        stack.setDurability((short) damage);
    }

    // --- Persistent data container (1.14+) routed through reflection on the real NamespacedKey ---

    /**
     * Reflectively resolves InfinityLib's {@code PersistentType.ITEM_STACK_OLD} custom data type
     * (typed as {@link Object} so {@code PersistentDataType} is never referenced here). Returns
     * {@code null} on versions where the type cannot load.
     */
    @Nullable
    public static Object itemStackDataType() {
        try {
            return Class.forName("io.github.mooy1.infinitylib.common.PersistentType")
                    .getField("ITEM_STACK_OLD").get(null);
        } catch (ReflectiveOperationException | LinkageError e) {
            return null;
        }
    }

    public static void setPdcByte(@Nonnull ItemMeta meta, @Nonnull NamespacedKey key, byte value) {
        pdcSet(meta, key, "BYTE", value);
    }

    public static void setPdcInt(@Nonnull ItemMeta meta, @Nonnull NamespacedKey key, int value) {
        pdcSet(meta, key, "INTEGER", value);
    }

    public static boolean hasPdc(@Nonnull ItemMeta meta, @Nonnull NamespacedKey key, @Nonnull String typeName) {
        Object container = container(meta);
        Object bukkitKey = BukkitKeys.toBukkit(key);
        Object type = dataType(typeName);

        if (container == null || bukkitKey == null || type == null) {
            return false;
        }

        try {
            Class<?> keyClass = Class.forName("org.bukkit.NamespacedKey");
            Class<?> typeClass = Class.forName("org.bukkit.persistence.PersistentDataType");
            Method has = findMethod(container.getClass(), "has", keyClass, typeClass);
            return has != null && Boolean.TRUE.equals(has.invoke(container, bukkitKey, type));
        } catch (ReflectiveOperationException e) {
            return false;
        }
    }

    public static int getPdcInt(@Nonnull ItemMeta meta, @Nonnull NamespacedKey key, int defaultValue) {
        Object value = pdcGet(meta, key, "INTEGER", defaultValue);
        return value instanceof Integer ? (Integer) value : defaultValue;
    }

    public static void removePdc(@Nonnull ItemMeta meta, @Nonnull NamespacedKey key) {
        Object container = container(meta);
        Object bukkitKey = BukkitKeys.toBukkit(key);

        if (container == null || bukkitKey == null) {
            return;
        }

        try {
            Class<?> keyClass = Class.forName("org.bukkit.NamespacedKey");
            Method remove = findMethod(container.getClass(), "remove", keyClass);
            if (remove != null) {
                remove.invoke(container, bukkitKey);
            }
        } catch (ReflectiveOperationException ignored) {
            // not supported on this version
        }
    }

    /**
     * Stores an {@link ItemStack} under the given key using the supplied custom {@code PersistentDataType}
     * (typed as {@link Object} so the type is never referenced in this addon's bytecode).
     */
    public static void setPdcItemStack(@Nonnull ItemMeta meta, @Nonnull NamespacedKey key, @Nonnull Object type, @Nonnull ItemStack value) {
        Object container = container(meta);
        Object bukkitKey = BukkitKeys.toBukkit(key);

        if (container == null || bukkitKey == null) {
            return;
        }

        try {
            Class<?> keyClass = Class.forName("org.bukkit.NamespacedKey");
            Class<?> typeClass = Class.forName("org.bukkit.persistence.PersistentDataType");
            Method set = findMethod(container.getClass(), "set", keyClass, typeClass, Object.class);
            if (set != null) {
                set.invoke(container, bukkitKey, type, value);
            }
        } catch (ReflectiveOperationException ignored) {
            // not supported on this version
        }
    }

    /**
     * Reads an {@link ItemStack} stored under the given key using the supplied custom
     * {@code PersistentDataType}. Returns {@code null} when absent or unsupported.
     */
    @Nullable
    public static ItemStack getPdcItemStack(@Nonnull ItemMeta meta, @Nonnull NamespacedKey key, @Nonnull Object type) {
        Object container = container(meta);
        Object bukkitKey = BukkitKeys.toBukkit(key);

        if (container == null || bukkitKey == null) {
            return null;
        }

        try {
            Class<?> keyClass = Class.forName("org.bukkit.NamespacedKey");
            Class<?> typeClass = Class.forName("org.bukkit.persistence.PersistentDataType");
            Method get = findMethod(container.getClass(), "get", keyClass, typeClass);
            Object result = get == null ? null : get.invoke(container, bukkitKey, type);
            return result instanceof ItemStack ? (ItemStack) result : null;
        } catch (ReflectiveOperationException e) {
            return null;
        }
    }

    /**
     * Compares the persistent-data containers of two {@link ItemMeta}. Falls back to comparing the metas
     * themselves on versions without a persistent-data container.
     */
    public static boolean pdcEquals(@Nonnull ItemMeta a, @Nonnull ItemMeta b) {
        Object containerA = container(a);
        Object containerB = container(b);

        if (containerA == null || containerB == null) {
            return a.equals(b);
        }

        return containerA.equals(containerB);
    }

    private static void pdcSet(@Nonnull ItemMeta meta, @Nonnull NamespacedKey key, @Nonnull String typeName, @Nonnull Object value) {
        Object container = container(meta);
        Object bukkitKey = BukkitKeys.toBukkit(key);
        Object type = dataType(typeName);

        if (container == null || bukkitKey == null || type == null) {
            return;
        }

        try {
            Class<?> keyClass = Class.forName("org.bukkit.NamespacedKey");
            Class<?> typeClass = Class.forName("org.bukkit.persistence.PersistentDataType");
            Method set = findMethod(container.getClass(), "set", keyClass, typeClass, Object.class);
            if (set != null) {
                set.invoke(container, bukkitKey, type, value);
            }
        } catch (ReflectiveOperationException ignored) {
            // not supported on this version
        }
    }

    @Nullable
    private static Object pdcGet(@Nonnull ItemMeta meta, @Nonnull NamespacedKey key, @Nonnull String typeName, @Nullable Object defaultValue) {
        Object container = container(meta);
        Object bukkitKey = BukkitKeys.toBukkit(key);
        Object type = dataType(typeName);

        if (container == null || bukkitKey == null || type == null) {
            return defaultValue;
        }

        try {
            Class<?> keyClass = Class.forName("org.bukkit.NamespacedKey");
            Class<?> typeClass = Class.forName("org.bukkit.persistence.PersistentDataType");
            Method get = findMethod(container.getClass(), "get", keyClass, typeClass);
            Object result = get == null ? null : get.invoke(container, bukkitKey, type);
            return result != null ? result : defaultValue;
        } catch (ReflectiveOperationException e) {
            return defaultValue;
        }
    }

    @Nullable
    private static Object container(@Nonnull ItemMeta meta) {
        try {
            return ItemMeta.class.getMethod("getPersistentDataContainer").invoke(meta);
        } catch (ReflectiveOperationException e) {
            return null;
        }
    }

    @Nullable
    private static Object dataType(@Nonnull String typeName) {
        try {
            return Class.forName("org.bukkit.persistence.PersistentDataType").getField(typeName).get(null);
        } catch (ReflectiveOperationException e) {
            return null;
        }
    }

    /**
     * Resolves {@code name(params)} on {@code owner} to a {@link Method} handle that can actually be invoked.
     *
     * @implNote The container is the non-public {@code CraftPersistentDataContainer}; invoking a method whose
     *           declaring class is non-public throws {@link IllegalAccessException} on module-restricted JVMs
     *           (26.x), which was silently swallowed and lost all persistent data (e.g. a Storage Unit's stored
     *           amount on break). When the resolved handle is non-public the same signature is re-resolved on a
     *           public supertype/interface so it stays invocable.
     */
    @Nullable
    private static Method findMethod(@Nonnull Class<?> owner, @Nonnull String name, @Nonnull Class<?>... params) {
        Method resolved = null;

        try {
            resolved = owner.getMethod(name, params);
        } catch (NoSuchMethodException e) {
            for (Method m : owner.getMethods()) {
                if (m.getName().equals(name) && m.getParameterCount() == params.length) {
                    resolved = m;
                    break;
                }
            }
        }

        if (resolved == null) {
            return null;
        }

        if (!Modifier.isPublic(resolved.getDeclaringClass().getModifiers())) {
            Method publicMethod = searchPublic(owner, resolved.getName(), resolved.getParameterTypes());

            if (publicMethod != null) {
                return publicMethod;
            }

            try {
                resolved.setAccessible(true);
            } catch (Throwable ignored) {
                // Strong encapsulation may forbid this; invocation then fails and the caller falls back.
            }
        }

        return resolved;
    }

    @Nullable
    private static Method searchPublic(@Nonnull Class<?> type, @Nonnull String name, @Nonnull Class<?>[] paramTypes) {
        for (Class<?> current = type; current != null; current = current.getSuperclass()) {
            if (Modifier.isPublic(current.getModifiers())) {
                try {
                    Method candidate = current.getMethod(name, paramTypes);

                    if (Modifier.isPublic(candidate.getDeclaringClass().getModifiers())) {
                        return candidate;
                    }
                } catch (NoSuchMethodException ignored) {
                    // Not declared here - keep walking.
                }
            }

            for (Class<?> iface : current.getInterfaces()) {
                Method candidate = searchPublic(iface, name, paramTypes);

                if (candidate != null) {
                    return candidate;
                }
            }
        }

        return null;
    }

    private static boolean classExists(@Nonnull String name) {
        try {
            Class.forName(name);
            return true;
        } catch (Throwable ignored) {
            return false;
        }
    }
}
