package org.venompvp.venom.objs;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CachedInventory {

    private ItemStack[] inventoryContents;
    private ItemStack[] armorContents;

    public CachedInventory(Player player) {
        this.inventoryContents = player.getInventory().getContents();
        this.armorContents = player.getInventory().getArmorContents();
    }

    public CachedInventory(ItemStack[] inventoryContents, ItemStack[] armorContents) {
        this.inventoryContents = inventoryContents;
        this.armorContents = armorContents;
    }

    public ItemStack[] getInventoryContents() {
        return inventoryContents;
    }

    public void setInventoryContents(ItemStack[] inventoryContents) {
        this.inventoryContents = inventoryContents;
    }

    public ItemStack[] getArmorContents() {
        return armorContents;
    }

    public void setArmorContents(ItemStack[] armorContents) {
        this.armorContents = armorContents;
    }

    public void restoreInventory(Player player) {
        player.getInventory().clear();
        player.getInventory().setContents(inventoryContents);
        player.getInventory().setArmorContents(armorContents);
        player.updateInventory();
    }
}
