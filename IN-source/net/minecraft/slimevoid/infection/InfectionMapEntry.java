package net.minecraft.slimevoid.infection;

import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.common.DimensionManager;


public class InfectionMapEntry {
		public ChunkCoordinates coord;
		public int blockID;
		public int resistanceLeft;
		
		InfectionMapEntry(ChunkCoordinates coord, int blockID, int resistance) {
			this.coord = coord;
			this.blockID = blockID;
			this.resistanceLeft = resistance;
		}
		
		InfectionMapEntry(ChunkCoordinates coord) {
			this.coord = coord;
			this.blockID = DimensionManager.getWorld(0).getBlockId(coord.posX, coord.posY, coord.posZ);
			this.resistanceLeft = InfectionGamemode.infection.getBlockResistance(this.blockID);
		}
	}