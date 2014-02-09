package com.adamki11s.spellcraft;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

import com.adamki11s.spellcraft.spells.ExplosiveArrowSpell;

public class SpellListener implements Listener {
	
	private final ExplosiveArrowSpell triArrow = new ExplosiveArrowSpell(1, 5);
	
	public SpellListener(Plugin p){
		Bukkit.getPluginManager().registerEvents(this, p);
	}
	
	@EventHandler
	private void onInteract(PlayerInteractEvent evt){
		Player p = evt.getPlayer();
		triArrow.cast(p);
	}

}
