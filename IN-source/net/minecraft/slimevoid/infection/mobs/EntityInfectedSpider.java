package net.minecraft.slimevoid.infection.mobs;

import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.world.World;

public class EntityInfectedSpider extends EntitySpider {

	public EntityInfectedSpider(World world) {
		super(world);
		texture = "/infected_spider.png";
        moveSpeed = 2.8F;
	}
	
	@Override
	public int getMaxHealth()
    {
        return 32;
    }
}
