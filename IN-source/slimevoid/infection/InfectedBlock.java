package slimevoid.infection;

import slimevoid.infection.core.InfectionMod;

public class InfectedBlock {
		public int infector;
		public int resistance;
		
		public InfectedBlock(int infector, int resistance) {
			this.infector = infector;
			this.resistance = resistance;
		}
		
		public InfectedBlock(int resistance) {
			this.infector = InfectionMod.INFECTED.blockID;
			this.resistance = resistance;
		}
	}
	
