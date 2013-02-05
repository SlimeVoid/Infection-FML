package slimevoid.infection.blocks;

import static java.lang.Math.pow;

import java.util.Random;

import slimevoid.infection.core.InfectionMod;
import slimevoid.infection.mobs.EntityInfectedSpider;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class BlockInfected extends Block {

	public BlockInfected(int id) {
		super(id, Material.clay);
		blockIndexInTexture = 10 * 16 + 10;
		this.setCreativeTab(CreativeTabs.tabBlock);
	}
	
	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		// TODO :: InfectionGamemode.infection.activateInfection(x, y, z);
	}
	
	@Override
	public void breakBlock(World world, int x, int y, int z, int side, int metadata) {
		super.breakBlock(world, x, y, z, side, metadata);
		InfectionMod.instance.gamemode.desinfect(x, y, z);
//		EntityInfectedWorm worm = new EntityInfectedWorm(world);
//		worm.setPosition(i+0.5, j, k+0.5);
//		world.spawnEntityInWorld(worm);
	}
	
	@Override
	public void onBlockClicked(World world, int i, int j, int k, EntityPlayer entityplayer) {
//		ModLoader.getMinecraftInstance().displayGuiScreen(new GuiCutscene(new CutsceneMeteor(i, j, k)));
		super.onBlockClicked(world, i, j, k, entityplayer);
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
			if(world.rand.nextInt(20) == 0) world.spawnParticle("reddust", x + world.rand.nextDouble(), y + 1, z + world.rand.nextDouble(), 0.5D, 0.3D, 0.6D);
			world.spawnParticle("depthsuspend", x + world.rand.nextDouble(), y + 1.2, z + world.rand.nextDouble(), 0D, 0D, 0D);
		}
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
        float f = 0.125F;
        return AxisAlignedBB.getBoundingBox(par2, par3, par4, par2 + 1, (par3 + 1) - f, par4 + 1);
    }
	
	@Override
	public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity) {
		InfectionMod.instance.gamemode.infectEntity(world, i, j, k, entity);
		if (entity instanceof EntityInfectedSpider) {
			double d = 1.025;
			entity.motionX *= d;
			entity.motionZ *= d;
		} else {
			double d = .5;
			entity.motionX *= d;
			entity.motionZ *= d;
		}
	}
	
	@Override
	public void onNeighborBlockChange(World world, int i, int j, int k, int l) {
		// TODO :: InfectionGamemode.infection.activateInfection(i, j, k);
	}
	
	private long lastSnd = 0;
}
