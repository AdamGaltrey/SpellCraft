package com.adamki11s.spellcraft.spells;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.adamki11s.spellcraft.SpellCraft;
import com.adamki11s.spellcraft.spelldata.Spell;
import com.adamki11s.spellcraft.spelldata.SpellData;

public class FlingSpell extends SpellData implements Spell, Listener {

	private final ItemStack icon;
	
	private static Set<String> exempt = new HashSet<String>();

	public FlingSpell(int cooldownSeconds, int manaCost) {
		super(cooldownSeconds, manaCost);
		icon = new ItemStack(Material.ARROW, 1);
		ItemMeta met = icon.getItemMeta();
		met.setDisplayName(ChatColor.GREEN + "Fling");
		met.setLore(new ArrayList<String>(Arrays.asList("Fling yourself through the air.")));
		icon.setItemMeta(met);
		Bukkit.getPluginManager().registerEvents(this, SpellCraft.p);
	}

	@EventHandler
	private void ede(EntityDamageEvent evt) {
		if (evt.getEntity() instanceof Player && evt.getCause() == DamageCause.FALL) {
			Player p = (Player) evt.getEntity();
			if (exempt.contains(p.getName())) {
				exempt.remove(p.getName());
				evt.setCancelled(true);
			}
		}
	}

	@Override
	public void cast(Player p) {
		if (super.attemptCast(p)) {
			p.setVelocity(p.getLocation().getDirection().multiply(3.45).setY(1.35));
			p.setFallDistance(0);
			exempt.add(p.getName());
		}
	}

	@Override
	public ItemStack getIcon() {
		return icon;
	}

}
