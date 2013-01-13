package net.minecraft.slimevoid.infection.blocks;

import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTranslated;
import static org.lwjgl.opengl.GL11.glTranslatef;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.src.ModLoader;
import net.minecraft.tileentity.TileEntity;

public class RenderDiamond extends TileEntitySpecialRenderer {

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float frameDelta) {
		glPushMatrix();
		glTranslated(x, y, z);
		glTranslated(.5, 0, .5);
		glRotatef(360 * (System.currentTimeMillis() % 2000) / 2000F, 0, 1, 0);
		glTranslated(-.5 + .0625, 0, .0625 * .5);
		float f4 = 0.0F;
        float f5 = -0.3F;
		float f6 = 1 / 1.5F;
        glTranslatef(0.9375F, 0.0625F, 0.0F);
        glRotatef(-335F, 0.0F, 0.0F, 1.0F);
        glRotatef(-50F, 0.0F, 1.0F, 0.0F);
        glScalef(f6, f6, f6);
        glTranslatef(-f4, -f5, 0.0F);
        
		RenderManager.instance.itemRenderer.renderItem(ModLoader.getMinecraftInstance().thePlayer, new ItemStack(Item.diamond), 0);
		glPopMatrix();
	}
}
