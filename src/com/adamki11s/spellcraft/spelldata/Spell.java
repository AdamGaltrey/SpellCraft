package com.adamki11s.spellcraft.spelldata;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface Spell {
	
	public void cast(Player p);
	
	public ItemStack getIcon();

}
