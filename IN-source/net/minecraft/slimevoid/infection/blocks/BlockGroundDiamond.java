package net.minecraft.slimevoid.infection.blocks;

import java.util.Random;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class BlockGroundDiamond extends BlockContainer {

	public BlockGroundDiamond(int id) {
		super(id, Material.rock);
	}
	
	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
		if(entity instanceof EntityPlayer) {
			world.setBlockWithNotify(x, y, z, 0);
			EntityItem eItem = new EntityItem(world, x + .5, y + .5, z + .5, new ItemStack(Item.diamond));
			Random rand = world.rand;
			eItem.motionX = rand.nextDouble() - .5;
			eItem.motionY = rand.nextDouble() - .5;
			eItem.motionZ = rand.nextDouble() - .5;
			if (!world.isRemote) {
				world.spawnEntityInWorld(eItem);
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
	public int getRenderType() {
		return -1;
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityDiamond();
	}
}
