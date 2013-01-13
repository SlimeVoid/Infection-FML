package net.minecraft.slimevoid.cutscene.effect;

import org.lwjgl.opengl.GL11;

public class Effect2DColor extends Effect2D {

	public Effect2DColor(long duration, int color) {
		super(duration);
		this.r = (color >> 16 & 0xFF) / (float)0xFF;
		this.g = (color >> 8  & 0xFF) / (float)0xFF;
		this.b = (color >> 0  & 0xFF) / (float)0xFF;
	}

	@Override
	public void render(float progress, float frameDelta) {
		GL11.glPushMatrix();
		
		GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(770, 771);
		GL11.glColor4f(r, g, b, 1);
		GL11.glBegin(GL11.GL_QUADS);
		
		GL11.glVertex2d(mc.displayWidth	, 0);
		GL11.glVertex2d(0				, 0);
		GL11.glVertex2d(0				, mc.displayHeight);
		GL11.glVertex2d(mc.displayWidth	, mc.displayHeight);
		
		GL11.glEnd();
		
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
		GL11.glColor4f(1, 1, 1, 1);
		GL11.glPopMatrix();
	}
	
	private final float r, g, b;
}
