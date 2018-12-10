package org.venompvp.venom.data;

import com.google.gson.annotations.SerializedName;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ItemSlot {

    @SerializedName("t")
    public final String itemType;
    @SerializedName("d")
    public final short damage;
    @SerializedName("a")
    public final int amount;
    @SerializedName("m")
    public ItemMetaSerializable meta;

    public ItemSlot(ItemStack itemStack) {
        this.itemType = itemStack.getType().name();
        this.damage = itemStack.getDurability();
        this.amount = itemStack.getAmount();
        this.meta = new ItemMetaSerializable(itemStack);
    }

    public class PotionEffectTypeSerializable {

        @SerializedName("t")
        public String type;
        @SerializedName("d")
        public int duration;
        @SerializedName("a")
        public int amplifier;

        public PotionEffectTypeSerializable(String type, int duration, int amplifier) {
            this.type = type;
            this.duration = duration;
            this.amplifier = amplifier;
        }
    }

    public class EnchantType {

        @SerializedName("t")
        public String type;
        @SerializedName("l")
        public int level;

        public EnchantType(String type, int level) {
            this.type = type;
            this.level = level;
        }
    }

    public class ItemMetaSerializable {

        @SerializedName("d")
        public String displayName;
        @SerializedName("l")
        public List<String> lore;
        @SerializedName("e")
        public List<EnchantType> enchantments;
        @SerializedName("c")
        public Integer leatherColor;
        @SerializedName("p")
        public List<PotionEffectTypeSerializable> potionEffects;

        public ItemMetaSerializable(ItemStack itemStack) {
            ItemMeta itemStackMeta;
            if (itemStack.hasItemMeta()) {
                itemStackMeta = itemStack.getItemMeta();
                if (itemStackMeta.hasDisplayName()) {
                    displayName = itemStackMeta.getDisplayName();
                }

                if (itemStackMeta.hasLore()) {
                    lore = itemStackMeta.getLore();
                }

                if (itemStackMeta instanceof EnchantmentStorageMeta) {
                    EnchantmentStorageMeta esm = (EnchantmentStorageMeta) itemStackMeta;
                    if (esm.hasStoredEnchants()) {
                        enchantments = esm.getStoredEnchants().entrySet().stream().map(entry -> new EnchantType(entry.getKey().getName(), entry.getValue())).collect(Collectors.toList());
                    }
                }

                if (itemStackMeta instanceof LeatherArmorMeta) {
                    leatherColor = ((LeatherArmorMeta) itemStackMeta).getColor().asRGB();
                }

                if (itemStackMeta instanceof PotionMeta) {
                    PotionMeta potionMeta = (PotionMeta) itemStackMeta;
                    if (potionMeta.hasCustomEffects()) {
                        this.potionEffects = potionMeta.getCustomEffects().stream().map(effect -> new PotionEffectTypeSerializable(effect.getType().getName(), effect.getDuration(), effect.getAmplifier())).collect(Collectors.toList());
                    }
                }
            }
            if (enchantments == null) {
                enchantments = new ArrayList<>();
                itemStack.getEnchantments().forEach((key, value) -> enchantments.add(new EnchantType(key.getName(), value)));
            }
        }
    }
}
