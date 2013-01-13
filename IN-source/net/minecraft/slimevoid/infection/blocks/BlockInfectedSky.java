package net.minecraft.slimevoid.infection.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockInfectedSky extends Block {

	public BlockInfectedSky(int id) {
		super(id, Material.iron);
		blockIndexInTexture = 10 * 16 + 8;
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
}
