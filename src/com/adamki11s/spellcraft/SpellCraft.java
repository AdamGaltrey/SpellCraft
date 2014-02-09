package com.adamki11s.spellcraft;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.adamki11s.spellcraft.utils.FireworkEffectPlayer;
import com.adamki11s.spellcraft.utils.Mana;

public class SpellCraft extends JavaPlugin {
	
	public static Plugin p;
	
	public static FireworkEffectPlayer fep = new FireworkEffectPlayer();
	
	@Override
	public void onEnable(){
		p = this;
		Bukkit.getScheduler().runTaskTimer(this, new Mana(), 0, 20);
		new SpellListener(this);
	}

}
