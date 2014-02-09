package com.adamki11s.spellcraft.spells;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import com.adamki11s.spellcraft.SpellCraft;
import com.adamki11s.spellcraft.spelldata.Spell;
import com.adamki11s.spellcraft.spelldata.SpellData;

public class ExplosiveArrowSpell extends SpellData implements Spell, Listener {

	private final ItemStack icon;

	private static Set<Integer> ids;

	public ExplosiveArrowSpell(int cooldownSeconds, int manaCost) {
		super(cooldownSeconds, manaCost);
		icon = new ItemStack(Material.ARROW, 1);
		ItemMeta met = icon.getItemMeta();
		met.setDisplayName(ChatColor.RED + "Explosive" + ChatColor.RESET + "Arrow");
		met.setLore(new ArrayList<String>(Arrays.asList("Fires an explosive arrow.")));
		icon.setItemMeta(met);
		Bukkit.getPluginManager().registerEvents(this, SpellCraft.p);
		ids = new HashSet<Integer>();
	}

	@Override
	public void cast(Player p) {
		if (super.attemptCast(p)) {
			Vector eye = p.getEyeLocation().getDirection();
			Projectile proj = p.launchProjectile(Arrow.class);
			ids.add(proj.getEntityId());
			proj.setVelocity(eye.multiply(1.24).add(new Vector(0, 0.2, 0)));
			proj.setFireTicks(200);
		}
	}

	@EventHandler
	private void hit(ProjectileHitEvent evt) {
		if (evt.getEntity() instanceof Arrow && ids.contains(evt.getEntity().getEntityId())) {
			ids.remove(evt.getEntity().getEntityId());
			Location l = evt.getEntity().getLocation();
			try {
				SpellCraft.fep.playFirework(l.getWorld(), l,
						FireworkEffect.builder().withColor(Color.RED, Color.ORANGE, Color.YELLOW).build());
				evt.getEntity().getLocation().getWorld().createExplosion(l.getBlockX(),  l.getBlockY(), l.getBlockZ(), 1.8F, false, false);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			evt.getEntity().remove();
		}
	}

	@Override
	public ItemStack getIcon() {
		return icon;
	}

}
