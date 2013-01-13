package slimevoid.infection.actions;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class ActionEntityChicken extends ActionEntity {

	public ActionEntityChicken() {
		super(0);
	}
	
	/* Explode chicken and learn him to fly (up to 2 explosion before he join god) */
	@Override
	public void onInfect(World world, Entity entity) {
		world.createExplosion(entity, entity.posX, entity.posY, entity.posZ, 2, false);
		entity.attackEntityFrom(DamageSource.explosion, 2);
		entity.addVelocity(world.rand.nextInt(3)-1, world.rand.nextInt(3)+1, world.rand.nextInt(3)-1);
	}

}
