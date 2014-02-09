package com.adamki11s.spellcraft.spelldata;

import org.bukkit.World;

public class RegenData {
	
	private final World w;
	private final int x, y, z, id;
	private final byte data;
	
	public RegenData(World w, int x, int y, int z, int id, byte data) {
		this.w = w;
		this.x = x;
		this.y = y;
		this.z = z;
		this.id = id;
		this.data = data;
	}

	public World getW() {
		return w;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public int getId() {
		return id;
	}

	public byte getData() {
		return data;
	}

}
