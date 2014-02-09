package com.adamki11s.spellcraft.spelldata;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.adamki11s.spellcraft.utils.Mana;

public abstract class SpellData {

	private Map<String, Long> cast = new HashMap<String, Long>();

	private final int cooldownMS, manaCost;

	public SpellData(int cooldownSeconds, int manaCost) {
		this.cooldownMS = cooldownSeconds * 1000;
		this.manaCost = manaCost;
	}

	/**
	 * Return whether or not the spell can be cast.
	 * 
	 * @param p
	 * @return
	 */
	public boolean attemptCast(Player p) {
		if (!Mana.hasMana(p, manaCost)) {
			p.sendMessage(ChatColor.RED + "You do not have enough mana to cast this spell.");
			return false;
		} else {
			String name = p.getName();
			long t = System.currentTimeMillis();
			if (!cast.containsKey(name)) {
				cast.put(name, t);
				Mana.drainMana(p, manaCost);
				return true;
			} else {
				long c = cast.get(name);
				// if time elapsed since last cast is greater than cooldown
				// allow
				if (t - c > cooldownMS) {
					cast.put(name, t);
					Mana.drainMana(p, manaCost);
					return true;
				} else {
					p.sendMessage(ChatColor.RED + "You must wait " + (cooldownMS / 1000) + " seconds between casts.");
					return false;
				}
			}
		}
	}

	public int getManaCost() {
		return manaCost;
	}
}
