package org.venompvp.venom.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.venompvp.venom.Venom;
import org.venompvp.venom.data.ItemSlot;

import java.io.IOException;

public class ItemStackAdapter extends TypeAdapter<ItemStack> {

    private Venom plugin;

    public ItemStackAdapter(Venom plugin) {
        this.plugin = plugin;
    }

    @Override
    public void write(JsonWriter jsonWriter, ItemStack itemStack) throws IOException {
        if (itemStack == null) {
            jsonWriter.nullValue();
        } else {
            ItemSlot itemSlot = new ItemSlot(itemStack);
            plugin.getGson().toJson(itemSlot, ItemSlot.class, jsonWriter);
        }
    }

    @Override
    public ItemStack read(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        }

        ItemSlot itemSlot = plugin.getGson().fromJson(jsonReader, ItemSlot.class);
        ItemStack item = new ItemStack(Material.matchMaterial(itemSlot.itemType));
        item.setAmount(itemSlot.amount);
        item.setDurability(itemSlot.damage);
        ItemMeta itemMeta = item.getItemMeta();
        if (itemSlot.meta != null) {
            if (itemSlot.meta.enchantments != null) {
                for (ItemSlot.EnchantType enchant : itemSlot.meta.enchantments) {
                    itemMeta.addEnchant(Enchantment.getByName(enchant.type), enchant.level, true);
                }
            }

            if (itemSlot.meta.displayName != null) {
                itemMeta.setDisplayName(itemSlot.meta.displayName);
            }

            if (itemSlot.meta.lore != null) {
                itemMeta.setLore(itemSlot.meta.lore);
            }

            if (itemMeta instanceof EnchantmentStorageMeta && itemSlot.meta.enchantments != null) {
                EnchantmentStorageMeta esm = (EnchantmentStorageMeta) itemMeta;
                for (ItemSlot.EnchantType enchant : itemSlot.meta.enchantments) {
                    esm.addStoredEnchant(Enchantment.getByName(enchant.type), enchant.level, true);
                }
            }

            if (itemMeta instanceof LeatherArmorMeta && itemSlot.meta.leatherColor != null) {
                ((LeatherArmorMeta) itemMeta).setColor(Color.fromRGB(itemSlot.meta.leatherColor));
            }

            if (itemMeta instanceof PotionMeta && itemSlot.meta.potionEffects != null) {
                PotionMeta potionMeta = (PotionMeta) itemMeta;
                for (ItemSlot.PotionEffectTypeSerializable effect : itemSlot.meta.potionEffects) {
                    org.bukkit.potion.PotionEffectType type = org.bukkit.potion.PotionEffectType.getByName(effect.type);
                    potionMeta.addCustomEffect(new PotionEffect(type, effect.duration, effect.amplifier), true);
                }
            }
        }
        item.setItemMeta(itemMeta);
        return item;
    }
}
