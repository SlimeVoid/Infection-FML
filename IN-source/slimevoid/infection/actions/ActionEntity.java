package slimevoid.infection.actions;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class ActionEntity {
	
	public ActionEntity(float spawnChance) {
		this.spawnChance = spawnChance;
	}
	
	/**
	 * @param world
	 * @param entity
	 * Called when [entity] is spawned in [world]
	 */
	public void onSpawn(World world, Entity entity) {
	}
	
	/**
	 * @param world
	 * @param entity
	 * Called when [entity] is in contact with the infection in [world]
	 */
	public void onInfect(World world, Entity entity) {
		
	}
	
	public float getSpawnChance() {
		return spawnChance;
	}
	
	public boolean canSpawnOnInfection() {
		return getSpawnChance() > 0;
	}
	
	public Class<? extends Entity> getLinkedEntityType() {
		return linkedEntity;
	}

	public void setLinkedEntity(Class<? extends Entity> linkedEntity) {
		this.linkedEntity = linkedEntity;
	}

	public static Entity createEntity(World world, Class<? extends Entity> entityType) {
		try {
			return entityType.getConstructor(World.class).newInstance(world);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private final float spawnChance;
	private Class<? extends Entity> linkedEntity;
}
