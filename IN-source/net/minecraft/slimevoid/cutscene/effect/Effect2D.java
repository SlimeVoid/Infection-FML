package net.minecraft.slimevoid.cutscene.effect;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.src.ModLoader;

public abstract class Effect2D {
	public Effect2D(long duration) {
		this.duration = duration;
		ScaledResolution screen = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
		width = (int) screen.getScaledWidth_double();
		height = (int) screen.getScaledHeight_double();
	}
	
	public abstract void render(float progress, float frameDelta);
	
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public long getStartTime() {
		return startTime;
	}
	public long getDuration() {
		return duration;
	}
	
	protected int width ;
	protected int height;
	private final long duration;
	private long startTime;
	protected final Minecraft mc = ModLoader.getMinecraftInstance();
}
