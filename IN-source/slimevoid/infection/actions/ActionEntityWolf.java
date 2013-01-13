package slimevoid.infection.actions;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class ActionEntityWolf extends ActionEntity {

	public ActionEntityWolf() {
		super(0);
	}
	
	/* turn gentil wolf into mechant wolf */
	@Override
	public void onInfect(World world, Entity entity) {
		if (!((EntityWolf) entity).isAngry()) {
			((EntityWolf) entity).setAngry(true);
			EntityPlayer closestPlayer = world.getClosestPlayerToEntity(entity, -1);
			entity.attackEntityFrom(DamageSource.causeMobDamage(closestPlayer), 0);	
		}
	}
}
