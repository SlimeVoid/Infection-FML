package slimevoid.infection.mobs;

import java.util.Random;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.world.World;

public class EntityInfectedWorm extends EntityMob {

	public EntityInfectedWorm(World world, int posX, int posY, int posZ) {
		super(world);
		moveSpeed = 0;
		setSize(0.0625f, 0.375f);
		noClip = true;
		sPosX = posX;
		sPosY = posY;
		sPosZ = posZ;
		texture = "/infected_worm.png";
	}

	public EntityInfectedWorm(World world) {
		super(world);
		setSize(0.0625f, 0.375f);
		noClip = true;
		
		for (int i = 0; i < 5; i++) {
			randPos[i] = new randomPos();
		}
		texture = "/infected_worm.png";
	}

	@Override
	public int getMaxHealth() {
		return 1;
	}
	
	@Override
	/**
     * Will get destroyed next tick.
     */
    public void setDead() {
//		mod_Infection.gameSession.currentInfectedEntity --;
//		ChunkCoordinates coord = new ChunkCoordinates((int)sPosX, (int)sPosY, (int)sPosZ);
//		InfectionGamemode.infection.setInfectedBlockUnpopulated(coord);
        isDead = true;
    }

	@Override
	public void onKillEntity(EntityLiving entityliving) {
		super.onKillEntity(entityliving);
	}
	
	@Override
	public void onLivingUpdate() {
		//super.onLivingUpdate();
		
		if (firstUpdate == false) {
			firstUpdate = true;
			sPosX = posX;
			sPosY = posY;
			sPosZ = posZ;
		} else {
			setPosition(sPosX, sPosY, sPosZ);
		}
		
		if (animSwitch) {
			animY += 0.013f;
		} else {
			animY -= 0.013f;
		}
		
		if (animY >= -0.25f) {
			animSwitch = false;
		}
		
		animAngle += 0.02f;
		animPitch = (float) Math.sin(animAngle * 10);
	}
	
	@Override
	public boolean isEntityInsideOpaqueBlock() {
		return false;
	}
	
	public class randomPos {
		public double x, z;
    	public Random rand = new Random();
    	
    	public randomPos() {
    		x = (rand.nextInt(100) + 1 - 50) / 100f;
    		z = (rand.nextInt(100) + 1 - 50) / 100f;
    	}
    }

	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();
				
		wormAnim += 2;
		if (wormAnim > 200) {
			setDead();
		}
		
		if (firstUpdate == false) {
			firstUpdate = true;
			sPosX = posX;
			sPosY = posY;
			sPosZ = posZ;
		} else {
			setPosition(sPosX, sPosY, sPosZ);
		}
	}
	
	
	public randomPos[] randPos = new randomPos[5];
	public float animAngle;
	public float animPitch;
	public float animY = -0.8f;
	private boolean animSwitch = true;
	
	private int wormAnim;
	private boolean firstUpdate = false;
	private double sPosX, sPosY, sPosZ;
}
