package slimevoid.infection.core.cutscene;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import slimevoid.infection.core.cutscene.effect.Effect2D;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.src.ModLoader;

public abstract class Scene {
	public Scene(long duration) {
		this.duration = duration;
		this.effects2d = new ArrayList<Effect2D>();
		initEffects();
	}
	
	public void start() {
		this.playerX = player.posX;
		this.playerY = player.posY;
		this.playerZ = player.posZ;
		
		this.playerYaw = player.cameraYaw;
		this.playerPitch = player.cameraPitch;
	}
	
	public void render2D(float frameDelta, long time) {
		for(Effect2D effect : effects2d) {
			if(time >= effect.getStartTime() && time < effect.getStartTime() + effect.getDuration()) {
				effect.render((time - effect.getStartTime()) / (float)effect.getDuration(), frameDelta);
			}
		}
	}
	
	public void render3D(float frameDelta, long time) {
	}
	
	public void setupCamera(float frameDelta, long time) {
	}
	
	public abstract void onTick(long time);
	protected abstract void initEffects();
	
	protected void addEffect2D(Effect2D effect, long startTime) {
		effect.setStartTime(startTime);
		effects2d.add(effect);
	}
	
	public void end() {
		player.fallDistance = 0;
		player.setPositionAndRotation(playerX, playerY, playerZ, playerYaw, playerPitch);
	}
	
	public long getDuration() {
		return duration;
	}
	
	public double getPlayerX() {
		return playerX;
	}
	
	public double getPlayerY() {
		return playerY;
	}
	
	public double getPlayerZ() {
		return playerZ;
	}
	
	public float getPlayerYaw() {
		return playerYaw;
	}
	
	public float getPlayerPitch() {
		return playerPitch;
	}
	
	private final List<Effect2D> effects2d;
	private final long duration;
	protected static final Minecraft mc = ModLoader.getMinecraftInstance();
	protected static final FontRenderer fontRenderer = mc.fontRenderer;
	protected static final Random rand = new Random();
	protected final EntityPlayer player = mc.thePlayer;
	protected double playerX, playerY, playerZ;
	protected float playerYaw, playerPitch;
}
