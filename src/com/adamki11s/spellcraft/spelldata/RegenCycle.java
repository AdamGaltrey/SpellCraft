package com.adamki11s.spellcraft.spelldata;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

public class RegenCycle implements Runnable {

	static List<RegenData> data = new ArrayList<RegenData>();

	private static Object lock = new Object();

	private static final Random r = new Random();

	public static void addRegenData(World w, int x, int y, int z, int id, byte d) {
		synchronized (lock) {
			data.add(new RegenData(w, x, y, z, id, d));
		}
	}

	// run every 2 ticks
	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		for (int i = 0; i <= 1; i++) {
			if (!data.isEmpty()) {
				int index = r.nextInt(data.size());
				RegenData rd;
				synchronized (lock) {
					rd = data.remove(index);
				}

				Block b = rd.getW().getBlockAt(rd.getX(), rd.getY(), rd.getZ());
				int effectID = rd.getId() == 0 ? b.getTypeId() : rd.getId();
				b.setTypeIdAndData(rd.getId(), rd.getData(), true);
				rd.getW().playEffect(new Location(rd.getW(), rd.getX(), rd.getY(), rd.getZ()), Effect.STEP_SOUND, effectID);
				rd = null;
			}
		}
	}

}
