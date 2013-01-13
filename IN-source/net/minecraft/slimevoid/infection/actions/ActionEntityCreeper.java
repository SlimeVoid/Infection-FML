package net.minecraft.slimevoid.infection.actions;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.world.World;

public class ActionEntityCreeper extends ActionEntity {

	public ActionEntityCreeper(float spawnRate) {
		super(spawnRate);
	}
	
	
	/* turn noob creeper into PGM creeper */
	@Override
	public void onInfect(World world, Entity entity) {
		if (!((EntityCreeper) entity).getPowered()) {
			entity.onStruckByLightning(null);
		}
	}

}
