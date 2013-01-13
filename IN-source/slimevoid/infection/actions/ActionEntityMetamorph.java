package slimevoid.infection.actions;

import slimevoid.infection.InfectionGamemode;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class ActionEntityMetamorph extends ActionEntity {

	public ActionEntityMetamorph(float spawnChance, Class<? extends Entity> entityType) {
		super(spawnChance);
		this.entityType = entityType;
	}
	
	@Override
	public void onInfect(World world, Entity entity) {
		super.onInfect(world, entity);
		InfectionGamemode.infection.spawnEntity(world, createEntity(world), entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch);
		entity.setDead();
		entity.setPosition(9999, 9999, 9999);
	}
	
	private Entity createEntity(World world) {
		return createEntity(world, getEntityType());
	}

	public Class<? extends Entity> getEntityType() {
		return entityType;
	}
	
	private final Class<? extends Entity> entityType;
}
