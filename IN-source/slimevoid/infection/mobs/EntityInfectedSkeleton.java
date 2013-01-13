package slimevoid.infection.mobs;

import slimevoid.infection.mobs.ai.EntityAIInfectedArrowAttack;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.world.World;

public class EntityInfectedSkeleton extends EntitySkeleton {

	public EntityInfectedSkeleton(World world) {
		super(world);
		texture = "/infected_skeleton.png";
		int taskCount = this.tasks.taskEntries.size();
        tasks.addTask(taskCount++, new EntityAIInfectedArrowAttack(this, moveSpeed, 1, 60));
	}

}
