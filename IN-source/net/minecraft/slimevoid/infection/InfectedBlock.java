package net.minecraft.slimevoid.infection;

import net.minecraft.src.mod_Infection;

public class InfectedBlock {
		public int infector;
		public int resistance;
		
		public InfectedBlock(int infector, int resistance) {
			this.infector = infector;
			this.resistance = resistance;
		}
		
		public InfectedBlock(int resistance) {
			this.infector = mod_Infection.INFECTED.blockID;
			this.resistance = resistance;
		}
	}
	
