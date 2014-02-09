package com.adamki11s.spellcraft;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class SpellListener implements Listener {

	public SpellListener(Plugin p) {
		Bukkit.getPluginManager().registerEvents(this, p);
	}

	
	@EventHandler
	private void onInteract(PlayerInteractEvent evt) {
		if (evt.getPlayer().getItemInHand() != null && evt.getPlayer().getItemInHand().getType() == Material.STICK) {
			ItemStack is = evt.getPlayer().getItemInHand();
			if (is.hasItemMeta() && is.getItemMeta().hasDisplayName()
					&& ChatColor.stripColor(is.getItemMeta().getDisplayName()).startsWith("Wand")) {
				Player p = evt.getPlayer();
				if (evt.getAction() == Action.RIGHT_CLICK_AIR || evt.getAction() == Action.RIGHT_CLICK_BLOCK) {
					//right click to open menu
					SpellMenu.menu.open(p);
				} else {
					//left click to cast
					if (SpellMenu.chosen.containsKey(p.getName())) {
						SpellMenu.chosen.get(p.getName()).cast(p);
					}
				}
			}
		}
	}

}
