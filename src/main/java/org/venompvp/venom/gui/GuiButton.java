package org.venompvp.venom.gui;

import org.bukkit.inventory.ItemStack;

public class GuiButton {

    private ItemStack itemStack;
    private ClickEvent clickEvent;

    public GuiButton(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public ClickEvent getClickEvent() {
        return clickEvent;
    }

    public GuiButton setClickEvent(ClickEvent clickEvent) {
        this.clickEvent = clickEvent;
        return this;
    }
}
