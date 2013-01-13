package net.minecraft.slimevoid.infection.blocks;

import static java.lang.Math.pow;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.slimevoid.infection.fx.EntityInfectedLeavesFX;
import net.minecraft.src.ModLoader;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockInfectedLeaves extends Block {
	
	public BlockInfectedLeaves(int id) {
		super(id, Material.clay);
		blockIndexInTexture = (10 * 16 + 11) + 1;
	}
	
	@Override
	public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity) {
		if(entity instanceof EntityPlayer) {
			entity.attackEntityFrom(DamageSource.cactus, 1);
		}
	}
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving entityliving) {
		super.onBlockPlacedBy(world, x, y, z, entityliving);
		if(pow(x - entityliving.posX, 2) + pow(y - entityliving.posY, 2) + pow(z - entityliving.posZ, 2) < 64 && System.currentTimeMillis() - lastSnd > 200) {
			lastSnd = System.currentTimeMillis();
			world.playSoundEffect(x + .5, y + .5, z + .5, "mob.slime", 1, 1);
		}
		for(int i = 0; i < 15; i ++) {
			world.spawnParticle("largesmoke", x + world.rand.nextDouble(), y + 0.5 + world.rand.nextDouble() * 0.6, z + world.rand.nextDouble(), 0, 0, 0);
		}
	}
	
	@Override
	public void randomDisplayTick(World world, int x, int y, int z, Random random) {
		if(Block.blocksList[world.getBlockId(x, y + 1, z)] == null || !Block.blocksList[world.getBlockId(x, y + 1, z)].isOpaqueCube()) {
			if(world.rand.nextInt(20) == 0) { 
				world.spawnParticle("reddust", x + world.rand.nextDouble(), y + world.rand.nextDouble(), z + world.rand.nextDouble(), 0.9D, 0.9D, 0.9D);
				double d = (float)x + world.rand.nextFloat();
	            double d2 = (double)y + maxY;
	            double d4 = (float)z + world.rand.nextFloat();
	            if (world.isRemote)
	            	ModLoader.getMinecraftInstance().effectRenderer.addEffect(((EntityFX)(new EntityInfectedLeavesFX(world, d, d2, d4))));
			}
		}
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
		return null;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
	
	@Override
	public int getRenderBlockPass() {
        return 1;
    }
	
	@Override
	public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
		if(world.getBlockId(x, y, z) == 0) {
			return true;
		}
		if(world.getBlockId(x, y, z) == this.blockID && (side == 0 || side == 2 || side == 4)) {
			return true;
		}
		return false;
	}
	
	
	
	private long lastSnd = 0;
}
