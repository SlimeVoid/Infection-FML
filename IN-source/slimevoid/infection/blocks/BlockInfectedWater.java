package slimevoid.infection.blocks;

import java.util.Random;

import slimevoid.infection.InfectionGamemode;
import slimevoid.infection.core.InfectionMod;
import slimevoid.infection.fx.EntityInfectedWaterFX;
import slimevoid.infection.mobs.EntityInfectedSkeleton;
import slimevoid.infection.mobs.EntityInfectedSpider;
import slimevoid.infection.mobs.EntityInfectedZombie;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFluid;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.src.ModLoader;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class BlockInfectedWater extends BlockFluid {

	public BlockInfectedWater(int id) {
		super(id, Material.water);
		this.setCreativeTab(CreativeTabs.tabMisc);
		blockIndexInTexture = 10 * 16 + 9;
	}
	
	@Override
	public int getBlockTextureFromSide(int par1) {
        return blockIndexInTexture;
    }
	
	@Override
	public void randomDisplayTick(World world, int x, int y, int z, Random random) {
		if(Block.blocksList[world.getBlockId(x, y + 1, z)] == null || !Block.blocksList[world.getBlockId(x, y + 1, z)].isOpaqueCube()) {
			if(world.rand.nextInt(2) == 0) world.spawnParticle("reddust", x + world.rand.nextDouble(), y + 1, z + world.rand.nextDouble(), -0.9D, 0.5D, 0.25D);
		}
		
		if (world.rand.nextInt(100) == 0)
        {
            double d = x + world.rand.nextFloat();
            double d2 = y + maxY;
            double d4 = z + world.rand.nextFloat();
            //world.spawnParticle("lava", d, d2, d4, 10.0D, 50.0D, 100.0D);
            if (world.isRemote)
            	ModLoader.getMinecraftInstance().effectRenderer.addEffect((new EntityInfectedWaterFX(world, d, d2, d4)));
        }

        if (world.rand.nextInt(200) == 0)
        {
//        	world.playSoundEffect(x, y, z, "liquid.lava", 0.2F + world.rand.nextFloat() * 0.2F, 0.9F + world.rand.nextFloat() * 0.15F);
        }
	}

	@Override
	public void onBlockAdded(World world, int i, int j, int k) {
		super.onBlockAdded(world, i, j, k);
		InfectionGamemode.infection.activateInfection(i, j, k);
		world.setBlockMetadata(i, j, k, 0);
	}
	
	@Override
	public void breakBlock(World world, int i, int j, int k, int side, int metadata) {
		super.breakBlock(world, i, j, k, side, metadata);
		InfectionMod.instance.gamemode.desinfect(i, j, k);
	}
	
	@Override
	public void onNeighborBlockChange(World world, int i, int j, int k, int l) {
		super.onNeighborBlockChange(world, i, j, k, l);
		InfectionGamemode.infection.activateInfection(i, j, k);
	}
	
	@Override
	public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity) {
		super.onEntityCollidedWithBlock(world, i, j, k, entity);
		if (!(entity instanceof EntityMooshroom) &&!(entity instanceof EntityInfectedSpider) && !(entity instanceof EntityInfectedSkeleton) && !(entity instanceof EntityInfectedZombie)) {
			entity.attackEntityFrom(DamageSource.inFire, 1);
		}
		InfectionMod.instance.gamemode.infectEntity(world, i, j, k, entity);
	}
}
