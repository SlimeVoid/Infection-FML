package slimevoid.infection.blocks;

import net.minecraft.creativetab.CreativeTabs;


public class BlockInfectedWood extends BlockInfected {

	public BlockInfectedWood(int id) {
		super(id);
		this.setCreativeTab(CreativeTabs.tabBlock);
		blockIndexInTexture = 10 * 16 + 11;
	}
	
}
