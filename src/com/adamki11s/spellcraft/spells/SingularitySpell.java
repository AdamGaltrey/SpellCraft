package com.adamki11s.spellcraft.spells;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import com.adamki11s.spellcraft.SpellCraft;
import com.adamki11s.spellcraft.spelldata.BlockData;
import com.adamki11s.spellcraft.spelldata.RegenCycle;
import com.adamki11s.spellcraft.spelldata.Spell;
import com.adamki11s.spellcraft.spelldata.SpellData;

public class SingularitySpell extends SpellData implements Spell, Listener, Runnable {

	/*
	 * SINGULARITY PROCEDURE --------------------- 1) Define centre point 2)
	 * Collect blocks within radius of (x) blocks 3) Clear blocks within a
	 * radius of (x + 1) blocks around for clearance 4) Spawn falling entities
	 * and apply velocity to centre point every 2 ticks 5) After t ticks have
	 * elasped send all blocks away from centre point as explosion (Must affect
	 * players also) ---------------------
	 */

	private final ItemStack icon;

	public SingularitySpell(int cooldownSeconds, int manaCost) {
		super(cooldownSeconds, manaCost);
		icon = new ItemStack(Material.SNOW_BALL, 1);
		ItemMeta met = icon.getItemMeta();
		met.setDisplayName(ChatColor.MAGIC + "|" + ChatColor.RESET + "" + ChatColor.YELLOW + "Singularity" + ChatColor.RESET + ""
				+ ChatColor.MAGIC + "|");
		met.setLore(new ArrayList<String>(Arrays.asList("Fires an explosive arrow.")));
		icon.setItemMeta(met);
		Bukkit.getPluginManager().registerEvents(this, SpellCraft.p);
	}

	@EventHandler
	private static void entityChange(EntityChangeBlockEvent evt) {
		evt.setCancelled(true);
	}

	private Location centre;

	private int cycles = 0;
	BukkitTask id;

	Random r;

	private List<FallingBlock> entities = new ArrayList<FallingBlock>();

	List<Integer> ids;
	List<Byte> data;

	private final int radius = 2;

	@Override
	public void run() {
		cycles++;
		if (cycles < 12) {
			// just under 2s
			World w = null;
			for (FallingBlock fb : entities) {
				if (w == null) {
					w = fb.getWorld();
				}
				if (fb.getWorld().getName().equalsIgnoreCase(centre.getWorld().getName())
						&& fb.getLocation().distance(centre) <= ((double) radius / 3D)) {
					// spit out
					fb.setVelocity(new Vector(r.nextFloat() / 6F * (r.nextBoolean() ? -1 : 1), r.nextFloat() / 6F
							* (r.nextBoolean() ? -1 : 1), r.nextFloat() / 6F * (r.nextBoolean() ? -1 : 1)));
				} else {
					// suck in
					double dx = centre.getX() - fb.getLocation().getX(), dy = centre.getY() - fb.getLocation().getY(), dz = centre.getZ()
							- fb.getLocation().getZ();
					fb.setVelocity(new Vector(dx, dy, dz).normalize().multiply(0.5));
				}
			}

			for (LivingEntity le : w.getLivingEntities()) {
				if (le.getLocation().distance(centre) <= ((double) radius / 1.5D)) {
					// spit out
					le.setVelocity(new Vector(r.nextFloat() / 6F * (r.nextBoolean() ? -1 : 1), r.nextFloat() / 6F
							* (r.nextBoolean() ? -1 : 1), r.nextFloat() / 6F * (r.nextBoolean() ? -1 : 1)).multiply(1.2));
				} else if (le.getLocation().distance(centre) <= radius * 2) {
					// suck in
					double dx = centre.getX() - le.getLocation().getX(), dy = centre.getY() - le.getLocation().getY(), dz = centre.getZ()
							- le.getLocation().getZ();
					le.setVelocity(new Vector(dx, dy, dz).normalize());
				}
			}

			if (cycles < 10) {
				w.playSound(centre, Sound.WITHER_IDLE, (float) cycles / 5F, (float) cycles / 5F);
			}
		} else if (cycles == 12) {
			// explode
			World w = null;
			for (FallingBlock fb : entities) {
				if (w == null) {
					w = fb.getWorld();
				}
				fb.setVelocity(new Vector(r.nextFloat() / 6F * (r.nextBoolean() ? -1 : 1), r.nextFloat() / 6F * (r.nextBoolean() ? -1 : 1),
						r.nextFloat() / 6F * (r.nextBoolean() ? -1 : 1)).multiply(12));
			}
			if (w != null) {
				for (LivingEntity le : w.getLivingEntities()) {
					if (le == null || w == null) {
						continue;
					}
					if (le.getWorld().getName().equalsIgnoreCase(centre.getWorld().getName())
							&& le.getLocation().distance(centre) <= radius * 2) {
						le.setVelocity(new Vector(r.nextFloat() / 6F * (r.nextBoolean() ? -1 : 1), r.nextFloat() / 6F * 14, r.nextFloat()
								/ 6F * (r.nextBoolean() ? -1 : 1)));
					}
				}

				w.createExplosion(centre.getX(), centre.getY(), centre.getZ(), 2.0F, false, false);
				w.playEffect(centre, Effect.ENDER_SIGNAL, 0);
				w.playEffect(centre, Effect.ENDER_SIGNAL, 0);
				w.playEffect(centre, Effect.ENDER_SIGNAL, 0);
				w.playSound(centre, Sound.ANVIL_BREAK, 2, 2);

				for (Item e : w.getEntitiesByClass(Item.class)) {
					if (e.getWorld().getName().equalsIgnoreCase(centre.getWorld().getName()) && e.getLocation().distance(centre) <= 40) {
						e.remove();
					}
				}
			}

		} else {
			id.cancel();
			cycles = 0;
		}
	}

	@Override
	public void cast(Player p) {
		if (super.attemptCast(p)) {
			centre = p.getTargetBlock(null, 35).getLocation();

			ids = new ArrayList<Integer>();
			data = new ArrayList<Byte>();

			Block base = centre.getBlock();

			final List<BlockData> bList = new ArrayList<BlockData>();

			for (int x = -radius - 3; x <= radius + 3; x++) {
				for (int y = -radius - 3; y <= radius + 3; y++) {
					for (int z = -radius - 3; z <= radius + 3; z++) {
						Block get = base.getRelative(x, y, z);
						if (get.getType() == Material.WEB) {
							get.setTypeId(0);
						}
					}
				}
			}

			for (int x = -radius - 1; x <= radius + 1; x++) {
				for (int y = -radius - 1; y <= radius + 1; y++) {
					for (int z = -radius - 1; z <= radius + 1; z++) {
						Block get = base.getRelative(x, y, z);
						if (get.getState() instanceof Sign || get.getTypeId() == 0) {
							continue;
						}
						if (get.getType() == Material.COAL || get.getType() == Material.IRON_ORE || get.getType() == Material.GOLD_ORE
								|| get.getType() == Material.DIAMOND_ORE || get.getType() == Material.EMERALD_ORE
								|| get.getType() == Material.EMERALD_ORE || get.getType() == Material.REDSTONE_ORE
								|| get.getType() == Material.IRON_BLOCK || get.getType() == Material.GOLD_BLOCK
								|| get.getType() == Material.DIAMOND_BLOCK || get.getType() == Material.EMERALD_BLOCK
								|| get.getType() == Material.REDSTONE_BLOCK) {
							continue;
						}
						if (get.getType() == Material.SNOW || get.getType() == Material.ENDER_PORTAL_FRAME
								|| get.getType() == Material.ENDER_PORTAL || get.getType() == Material.PORTAL
								|| get.getType() == Material.OBSIDIAN || get.getType() == Material.BEDROCK
								|| get.getState() instanceof Chest) {
							continue;
						}
						if (Math.sqrt((x * x) + (y * y) + (z * z)) <= radius + 0.5) {
							if (get.getRelative(1, 0, 0).getState() instanceof Sign || get.getRelative(-1, 0, 0).getState() instanceof Sign
									|| get.getRelative(0, 0, 1).getState() instanceof Sign
									|| get.getRelative(0, 0, -1).getState() instanceof Sign
									|| get.getRelative(0, 1, 0).getState() instanceof Sign
									|| get.getRelative(0, -1, 0).getState() instanceof Sign) {
								continue;
							} else {
								bList.add(new BlockData(get));
								ids.add(get.getTypeId());
								data.add(get.getData());
								get.setTypeId(0);
							}
						}
					}
				}
			}

			Bukkit.getScheduler().runTaskLater(SpellCraft.p, new Runnable() {
				@Override
				public void run() {
					for (BlockData bd : bList) {
						RegenCycle.addRegenData(bd.w, bd.x, bd.y, bd.z, bd.id, bd.data);
					}
					bList.clear();
					for (Item e : centre.getWorld().getEntitiesByClass(Item.class)) {
						if (e.getWorld().getName().equalsIgnoreCase(centre.getWorld().getName()) && e.getLocation().distance(centre) <= 40) {
							e.remove();
						}
					}
				}
			}, 100L);

			r = new Random(centre.getWorld().getSeed());

			for (int x = -radius - 1; x <= radius + 1; x++) {
				for (int y = -radius - 1; y <= radius + 1; y++) {
					for (int z = -radius - 1; z <= radius + 1; z++) {
						if (ids.isEmpty()) {
							continue;
						}
						Block get = base.getRelative(x, y, z);
						if (Math.sqrt((x * x) + (y * y) + (z * z)) <= radius + 0.5) {
							int id = ids.remove(0);
							byte d = data.remove(0);
							if (id == Material.GRASS.getId()) {
								id = Material.DIRT.getId();
							}
							FallingBlock fb = get.getWorld().spawnFallingBlock(centre, id, d);
							fb.setVelocity(new Vector(r.nextFloat() / 4F * (r.nextBoolean() ? -1 : 1), r.nextFloat() / 4F
									* (r.nextBoolean() ? -1 : 1), r.nextFloat() / 4F * (r.nextBoolean() ? -1 : 1)));
							entities.add(fb);
							get.getWorld().playEffect(get.getLocation(), Effect.STEP_SOUND, id);
						}
					}
				}
			}

			id = Bukkit.getScheduler().runTaskTimer(SpellCraft.p, this, 2, 3);
		}
	}

	@Override
	public ItemStack getIcon() {
		return icon;
	}

}
