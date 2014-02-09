package com.adamki11s.spellcraft.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Mana implements Runnable {

	private static final int manaCap = 100;

	@Override
	public void run() {
		// regen 1 mana point per second
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (p.getLevel() < manaCap) {
				p.setLevel(p.getLevel() + 1);
			}
		}
	}

	public static void drainMana(Player p, int mana) {
		p.setLevel(p.getLevel() - mana);
	}

	public static void addMana(Player p, int mana) {
		p.setLevel(p.getLevel() + mana > 100 ? 100 : p.getLevel() + mana);
	}

	public static boolean hasMana(Player p, int mana) {
		return p.getLevel() >= mana;
	}

}
