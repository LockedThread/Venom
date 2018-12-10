package org.venompvp.venom.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Gui implements InventoryHolder {

    private String name;
    private int size;
    private ConcurrentHashMap<Integer, GuiButton> buttons;

    public Gui(String name, int size) {
        this.name = name;
        this.size = size;
        this.buttons = new ConcurrentHashMap<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Optional<GuiButton> getButtonAt(int slot) {
        return buttons.entrySet().stream().filter(entry -> entry.getKey() == slot).map(Map.Entry::getValue).findFirst();
    }

    public ClickEvent getClickEvent(int slot) {
        return getButtonAt(slot).map(GuiButton::getClickEvent).orElse(null);
    }

    public void setButtonAt(int slot, GuiButton guiButton) {
        buttons.put(slot, guiButton);
    }

    public abstract void openInventory(Player player);

    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, size, ChatColor.translateAlternateColorCodes('&', name));
        buttons.forEach((key, value) -> inventory.setItem(key, value.getItemStack()));
        return inventory;
    }
}
