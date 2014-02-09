package com.adamki11s.spellcraft;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.adamki11s.spellcraft.spelldata.RegenCycle;
import com.adamki11s.spellcraft.utils.FireworkEffectPlayer;
import com.adamki11s.spellcraft.utils.Mana;

public class SpellCraft extends JavaPlugin {
	
	public static Plugin p;
	
	public static FireworkEffectPlayer fep = new FireworkEffectPlayer();
	
	@Override
	public void onEnable(){
		p = this;
		SpellMenu.init();
		Bukkit.getScheduler().runTaskTimer(this, new Mana(), 0, 20);
		Bukkit.getScheduler().runTaskTimer(this, new RegenCycle(), 0, 5);
		new SpellListener(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(label.equalsIgnoreCase("spellcraft")){
			if(sender instanceof Player){
				Player p = (Player) sender;
				ItemStack i = new ItemStack(Material.STICK, 1);
				ItemMeta met = i.getItemMeta();
				met.setDisplayName(ChatColor.YELLOW + "Wand");
				i.setItemMeta(met);
				p.getInventory().addItem(i);
				return true;
			}
		}
		return false;
	}

}
