package slimevoid.infection.mobs;

import slimevoid.infection.mobs.ai.EntityAIBreakWall;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.world.World;

public class EntityInfectedZombie extends EntityZombie {
	public EntityInfectedZombie(World world) {
		super(world);
		setAttackTarget(world.getClosestPlayerToEntity(this, -1));
		isImmuneToFire = true;
		tasks.addTask(1, new EntityAIBreakWall(this));
		texture = "/infected_zombie.png";
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		if(getAttackTarget() == null) {
			setAttackTarget(worldObj.getClosestPlayerToEntity(this, -1));
		}
	}
}
