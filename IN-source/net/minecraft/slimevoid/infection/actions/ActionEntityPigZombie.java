package net.minecraft.slimevoid.infection.actions;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class ActionEntityPigZombie extends ActionEntity {

	public ActionEntityPigZombie() {
		super(0);
	}
	
	/* turn gentil pigzombie into mechant pigzombie */
	@Override
	public void onInfect(World world, Entity entity) {
		EntityPigZombie pigZombie = (EntityPigZombie)entity;
		if (pigZombie.getAttackTarget() == null) {
			EntityPlayer closestPlayer = world.getClosestPlayerToEntity(entity, -1);
			pigZombie.setAttackTarget(closestPlayer);
		}
	}
}
