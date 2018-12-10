package org.venompvp.venom.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GuiListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getCurrentItem() != null && event.getClickedInventory() != null && event.getWhoClicked() instanceof Player && event.getClickedInventory().getHolder() instanceof Gui) {
            Gui gui = (Gui) event.getClickedInventory().getHolder();
            gui.getClickEvent(event.getSlot()).onClick(event);
        }
    }
}
