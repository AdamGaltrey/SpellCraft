package com.adamki11s.spellcraft.spelldata;

import org.bukkit.World;
import org.bukkit.block.Block;

public class BlockData {
	
	public final World w;
	public final int x,y,z,id;
	public final byte data;
	public BlockData(World w, int x, int y, int z, int id, byte data) {
		this.w = w;
		this.x = x;
		this.y = y;
		this.z = z;
		this.id = id;
		this.data = data;
	}
	public BlockData(Block b){
		this.w = b.getWorld();
		this.x = b.getX();
		this.y = b.getY();
		this.z = b.getZ();
		this.id = b.getTypeId();
		this.data = b.getData();
	}
	
	

}
