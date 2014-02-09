package com.adamki11s.spellcraft;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.adamki11s.spellcraft.spelldata.Spell;
import com.adamki11s.spellcraft.spells.ExplosiveArrowSpell;
import com.adamki11s.spellcraft.spells.FlingSpell;
import com.adamki11s.spellcraft.spells.SingularitySpell;

public class SpellMenu {

	public static final ExplosiveArrowSpell triArrow = new ExplosiveArrowSpell(1, 5);
	public static final SingularitySpell singularity = new SingularitySpell(15, 30);
	public static final FlingSpell fling = new FlingSpell(5, 15);

	public static Map<String, Spell> chosen = new HashMap<String, Spell>();

	public static IconMenu menu;

	public static void init() {
		menu = new IconMenu("Spell Menu", 9, new IconMenu.OptionClickEventHandler() {
			@Override
			public void onOptionClick(IconMenu.OptionClickEvent evt) {
				Player p = evt.getPlayer();

				ItemMeta met = p.getItemInHand().getItemMeta();

				Spell s = null;
				switch (evt.getPosition()) {
				case 0:
					p.sendMessage(ChatColor.GREEN + "Active spell disabled.");
					met.setDisplayName(ChatColor.YELLOW + "Wand" + ChatColor.RESET + " - " + ChatColor.RED + "No Spell");
					break;
				case 2:
					s = triArrow;
					p.sendMessage(ChatColor.GREEN + "ExplosiveArrow " + ChatColor.RESET + "spell chosen. " + ChatColor.BLUE
							+ triArrow.getManaCost() + ChatColor.RESET + " mana cost.");
					met.setDisplayName(ChatColor.YELLOW + "Wand" + ChatColor.RESET + " - " + ChatColor.GREEN + "ExplosiveArrow"
							+ ChatColor.RESET + " - " + ChatColor.BLUE + triArrow.getManaCost());
					break;
				case 4:
					s = new SingularitySpell(15, 30);
					p.sendMessage(ChatColor.GREEN + "Singularity " + ChatColor.RESET + "spell chosen. " + ChatColor.BLUE
							+ singularity.getManaCost() + ChatColor.RESET + " mana cost.");
					met.setDisplayName(ChatColor.YELLOW + "Wand" + ChatColor.RESET + " - " + ChatColor.GREEN + "Singularity"
							+ ChatColor.RESET + " - " + ChatColor.BLUE + singularity.getManaCost());
					break;
				case 6:
					s = fling;
					p.sendMessage(ChatColor.GREEN + "Fling " + ChatColor.RESET + "spell chosen. " + ChatColor.BLUE + fling.getManaCost()
							+ ChatColor.RESET + " mana cost.");
					met.setDisplayName(ChatColor.YELLOW + "Wand" + ChatColor.RESET + " - " + ChatColor.GREEN + "Fling" + ChatColor.RESET
							+ " - " + ChatColor.BLUE + fling.getManaCost());
					break;
				}
				evt.setWillClose(true);
				if (s != null) {
					chosen.put(evt.getPlayer().getName(), s);
				} else if (chosen.containsKey(evt.getPlayer().getName())) {
					chosen.remove(evt.getPlayer().getName());
				}
				p.getItemInHand().setItemMeta(met);
			}
		}, SpellCraft.p).setOption(0, new ItemStack(Material.GLASS, 1), ChatColor.RED + "No Spell", ChatColor.RESET + "Disable any active spells.")
				.setOption(2, new ItemStack(Material.ARROW, 1), ChatColor.GREEN + "ExplosiveArrow", ChatColor.RESET + "Fires an explosive arrow.")
				.setOption(4, new ItemStack(Material.ENDER_PEARL, 1), ChatColor.GREEN + "Singularity", ChatColor.RESET + "Creates a singularity", ChatColor.RESET + "at your target position.")
				.setOption(6, new ItemStack(Material.RED_MUSHROOM, 1), ChatColor.GREEN + "Fling", ChatColor.RESET + "Fling yourself through the air.");
	}
}
 