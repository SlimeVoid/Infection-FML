package slimevoid.infection.mobs.ai;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFluid;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class EntityAIBreakWall extends EntityAIBase {
	
	public EntityAIBreakWall(EntityLiving mob) {
		this.mob = mob;
		isBreakingBlock = false;
	}
	
	@Override
	public boolean shouldExecute() {
		return mob.getNavigator().noPath() && mob.getAttackTarget() != null;
	}
	
	@Override
	public boolean continueExecuting() {
		return (mob.getNavigator().noPath() && mob.getAttackTarget() != null) || isBreakingBlock;
	}
	
	@Override
	public void startExecuting() {
		super.startExecuting();
	}
	
	@Override
	public void updateTask() {
		super.updateTask();
		World world = DimensionManager.getWorld(0);
		if(isBreakingBlock) {
			breakingProgress++;
			if(breakingProgress > 40) {
				breakBlock(world);
			}
		} else {
			EntityLiving target = mob.getAttackTarget();
			
			Vec3 diff = Vec3.createVectorHelper(target.posX - mob.posX, 0, target.posZ - mob.posZ).normalize();
			int x = (int) Math.floor(Math.floor(mob.posX) + .5 + diff.xCoord);
			int y = (int) Math.floor(Math.floor(mob.posY) + .5 + diff.yCoord);
			int z = (int) Math.floor(Math.floor(mob.posZ) + .5 + diff.zCoord);
			if(startBreakingBlock(x, y + 1, z, world)) {
				return;
			} else {
				startBreakingBlock(x, diff.yCoord > .5 ? y + 2 : y, z, world);
			}
		}
	}
	
	private boolean startBreakingBlock(int x, int y, int z, World world) {
		Block block = Block.blocksList[world.getBlockId(x, y, z)];
		if(block == null || block instanceof BlockFluid) {
			return false;
		}
		bX = x;
		bY = y;
		bZ = z;
		isBreakingBlock = true;
		breakingProgress = 0;
		return true;
	}
	
	private void breakBlock(World world) {
		isBreakingBlock = false;
		breakingProgress = 0;
		world.setBlockWithNotify(bX, bY, bZ, 0);
	}
	
	private int breakingProgress;
	private boolean isBreakingBlock;
	private int bX, bY, bZ;
	private EntityLiving mob;
}
