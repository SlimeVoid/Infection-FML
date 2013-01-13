package net.minecraft.slimevoid.infection;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.slimevoid.infection.actions.ActionEntity;
import net.minecraft.slimevoid.infection.actions.ActionEntityChicken;
import net.minecraft.slimevoid.infection.actions.ActionEntityCreeper;
import net.minecraft.slimevoid.infection.actions.ActionEntityMetamorph;
import net.minecraft.slimevoid.infection.actions.ActionEntityPigZombie;
import net.minecraft.slimevoid.infection.actions.ActionEntityWolf;
import net.minecraft.slimevoid.infection.mobs.EntityInfectedSkeleton;
import net.minecraft.slimevoid.infection.mobs.EntityInfectedSpider;
import net.minecraft.slimevoid.infection.mobs.EntityInfectedWorm;
import net.minecraft.slimevoid.infection.mobs.EntityInfectedZombie;
import net.minecraft.src.mod_Infection;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;


public class Infection {

	public Infection(){
		init();
	}
	
	public void init() {
		/* Précise en quoi se tranforme un block infecté ainsi que sa resistance (si = -1, non infectable)*/ 
		infectedBlock = new InfectedBlock[255];
		for (int i = 0; i < 255; i++) {
			infectedBlock[i] = new InfectedBlock(mod_Infection.INFECTED.blockID, 2);
		}
		infectedBlock[mod_Infection.INFECTED.blockID] = new InfectedBlock(-1);
		infectedBlock[mod_Infection.INFECTED_SKY.blockID] = new InfectedBlock(-1);
		infectedBlock[mod_Infection.INFECTED_WOOD.blockID] = new InfectedBlock(-1);
		infectedBlock[mod_Infection.INFECTED_WATER.blockID] = new InfectedBlock(-1);
		infectedBlock[mod_Infection.INFECTED_LEAVES.blockID] = new InfectedBlock(-1);
		infectedBlock[0] = new InfectedBlock(-1);
		infectedBlock[Block.blockClay.blockID] = new InfectedBlock(-1);
		infectedBlock[Block.bedrock.blockID] = new InfectedBlock(-1);
		infectedBlock[Block.leaves.blockID] = new InfectedBlock(mod_Infection.INFECTED_LEAVES.blockID, 1);
		infectedBlock[Block.wood.blockID] = new InfectedBlock(mod_Infection.INFECTED_WOOD.blockID, 4);
		infectedBlock[Block.dirt.blockID] = new InfectedBlock(2);
		infectedBlock[Block.grass.blockID] = new InfectedBlock(2);
		infectedBlock[Block.waterMoving.blockID] = new InfectedBlock(mod_Infection.INFECTED_WATER.blockID, 1);
		infectedBlock[Block.waterStill.blockID] = new InfectedBlock(mod_Infection.INFECTED_WATER.blockID, 1);
		infectedBlock[Block.lavaMoving.blockID] = new InfectedBlock(-1);
		infectedBlock[Block.lavaStill.blockID] = new InfectedBlock(-1);
		infectedBlock[Block.vine.blockID] = new InfectedBlock(0, 0);
		infectedBlock[mod_Infection.GROUND_DIAMOND.blockID] = new InfectedBlock(-1);
		
		/* Définit les réaction des entités au contact de l'infection et les rates de spawn */
		entityAction = new HashMap<Class<? extends Entity>, ActionEntity>();
		registerActionEntity(EntitySpider.class			, new ActionEntityMetamorph(0, EntityInfectedSpider.class));
		registerActionEntity(EntityZombie.class			, new ActionEntityMetamorph(0, EntityInfectedZombie.class));
		registerActionEntity(EntitySkeleton.class		, new ActionEntityMetamorph(0, EntityInfectedSkeleton.class));
		registerActionEntity(EntityCow.class			, new ActionEntityMetamorph(0, EntityMooshroom.class));
		registerActionEntity(EntityOcelot.class			, new ActionEntityMetamorph(0, EntityWolf.class));
		registerActionEntity(EntityPig.class			, new ActionEntityMetamorph(0, EntityPigZombie.class));
		registerActionEntity(EntityChicken.class		, new ActionEntityChicken());
		registerActionEntity(EntityWolf.class			, new ActionEntityWolf());
		registerActionEntity(EntityCreeper.class		, new ActionEntityCreeper(.25F));
		registerActionEntity(EntityPigZombie.class		, new ActionEntityPigZombie());
		registerActionEntity(EntityInfectedWorm.class	, new ActionEntity(1));
		registerActionEntity(EntityInfectedZombie.class	, new ActionEntity(1));
		registerActionEntity(EntityInfectedSpider.class	, new ActionEntity(.5F));
		registerActionEntity(EntityInfectedSkeleton.class, new ActionEntity(.5F));
		
		/* List des infections active/passives suivant leur chunkcoord */
		infectionMap = new HashMap<ChunkCoordinates, InfectionMapEntry>();
		activeInfection = new ArrayList<InfectionMapEntry>();
		entitySpawn = new ArrayList<ChunkCoordinates>();
		entityList = new ArrayList<Entity>();
	}
	
	public void addEntitySpawn(ChunkCoordinates coord) {
		int b1 = DimensionManager.getWorld(0).getBlockId(coord.posX, coord.posY, coord.posZ);
		int b2 = DimensionManager.getWorld(0).getBlockId(coord.posX, coord.posY + 1, coord.posZ);
		if (!entitySpawn.contains(coord) && b1 == mod_Infection.INFECTED.blockID && b2 == 0) {
			entitySpawn.add(coord);
		}
	}
	
	public void removeEntitySpawn(ChunkCoordinates coord) {
		if (entitySpawn.contains(coord)) {
			entitySpawn.remove(coord);
		}
	}
	
	public ChunkCoordinates getRandomEntitySpawn() {
		world = DimensionManager.getWorld(0);
		if (entitySpawn.size() >= 1) {
			int j = rand.nextInt(entitySpawn.size());
			ChunkCoordinates coord = entitySpawn.get(j);
			
			// && !isPopulated(coord)
			if (world.getBlockId(coord.posX, coord.posY + 1, coord.posZ) == 0 && isInfected(coord)) {
				return coord;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
	
	public InfectionMapEntry getInfectedBlock(int x, int y, int z) {
		if (infectionMap.containsKey(getKeyForPos(x, y, z))) {
			return infectionMap.get(getKeyForPos(x, y, z));
		} else {
			return null;
		}
	}
	
	public void addInfectedBlock(InfectionMapEntry entry) {
		infectionMap.put(getKeyForPos(entry.coord), entry);
	}
	
	public void removeInfectedBlock(InfectionMapEntry entry) {
		infectionMap.remove(entry.coord);
	}
	
	public boolean canInfect(int x, int y, int z) {
		return canInfect(new ChunkCoordinates(x, y, z));
	}
	
	public boolean canInfect(ChunkCoordinates coord) {
		world = DimensionManager.getWorld(0);
		return canInfect(coord, world.getBlockId(coord.posX, coord.posY, coord.posZ));
	}
	
	public boolean canInfect(ChunkCoordinates coord, int blockToInfect) {
		return (infectedBlock[blockToInfect].resistance >= 0 && !isInfected(coord) && coord.posY > 60);
	}
	
	public boolean isInfected(ChunkCoordinates coord) {
		return isInfected(coord.posX, coord.posY, coord.posZ);
	}
	
	public boolean isInfected(int x, int y, int z) {
		return infectionMap.containsKey(getKeyForPos(x, y, z)) && infectionMap.get(getKeyForPos(x, y, z)).resistanceLeft < 0;
	}
	
	public int getBlockResistance(int blockID) {
		return infectedBlock[blockID].resistance;
	}
	
	public int getBlockInfector(int blockID) {
		return infectedBlock[blockID].infector;
	}
	
	public void setActiveInfectedBlock(InfectionMapEntry entry) {
		activeInfection.add(entry);
	}
	
	public void setInactiveInfectedBlock(InfectionMapEntry entry) {
		activeInfection.remove(entry);
	}
	
	public int getActiveInfectionSize() {
		return activeInfection.size();
	}
	
	public int getInfectionSize() {
		return infectionMap.size();
	}
	
	public int getEntityListSize() {
		return entityList.size();
	}
	
	public Entity getEntity(int id) {
		return entityList.get(id);
	}
	
	public void removeEntity(Entity entity) {
		entity.entityDropItem(new ItemStack(Item.diamond, rand.nextInt(3) + 1), 0);
		entityList.remove(entity); 
	}
	
	public void spawnEntity(World world, Entity entity, double x, double y, double z, float yaw, float pitch) {
		if (entity != null) {
			ActionEntity aEntity = getActionEntity(entity);
			entity.setLocationAndAngles(x, y, z, yaw, pitch);
			world.spawnEntityInWorld(entity);
			if(aEntity != null) {
				aEntity.onSpawn(world, entity);
			}
			InfectionGamemode.infection.entityList.add(entity);
		}
	}
	
	public InfectionMapEntry getRandomActiveInfectedBlock() {
		if (getActiveInfectionSize() >= 1) {
			int j = rand.nextInt(getActiveInfectionSize());
			return activeInfection.get(j);
		} else {
			return null;
		}
	}
	
	public int getPossibleSpawnSize() {
		return entitySpawn.size();
	}
	
	public ActionEntity getRandomActionEntity() {
		Object[] aes = entityAction.values().toArray();
		return (ActionEntity) aes[rand.nextInt(aes.length)];
	}
	
	public ActionEntity getActionEntity(Entity entity) {
		return entityAction.get(entity.getClass());
	}
	
	public ActionEntity getActionEntity(Class<? extends Entity> entity) {
		return entityAction.get(entity);
	}
	
	private void registerActionEntity(Class<? extends Entity> entityType, ActionEntity aEntity) {
		aEntity.setLinkedEntity(entityType);
		entityAction.put(entityType, aEntity);
	}
	
	public ChunkCoordinates getKeyForPos(ChunkCoordinates cc) {
		return cc;
	}
	
	public ChunkCoordinates getKeyForPos(int x, int y, int z) {
		return new ChunkCoordinates(x, y, z);
	}
	
	public void activateInfection(int x, int y, int z) {
		ChunkCoordinates cc = new ChunkCoordinates(x, y, z);
		InfectionMapEntry entry = new InfectionMapEntry(new ChunkCoordinates(x, y, z));
		addInfectedBlock(entry);
		setActiveInfectedBlock(entry);
		addEntitySpawn(cc);
	}
	
	public boolean findAndStartInfection() {
		World world = DimensionManager.getWorld(0);
		if (world != null) {
			ChunkCoordinates spawn = world.getSpawnPoint();
			boolean found = false;
			int it = 0;
			while (!found && it < 200) {
				float angle = (float) (rand.nextFloat() * (2 * PI));
				float dist = rand.nextInt(20) + 100;
				int x = (int)(dist * cos(angle) + spawn.posX);				
				int z = (int)(dist * sin(angle) + spawn.posZ);
				int y = world.getTopSolidOrLiquidBlock(x, z);
				if (world.getBlockMaterial(x, y, z) != Material.water && world.getBlockMaterial(x, y + 1, z) != Material.water) {
					activateInfection(x, y, z);
					infectionPos = new ChunkCoordinates(x, y, z);
					return true;
				}
			}
			if (!found) {
				float angle = (float) (rand.nextFloat() * (2 * PI));
				float dist = rand.nextInt(20) + 100;
				int x = (int)(dist * cos(angle) + spawn.posX);				
				int z = (int)(dist * sin(angle) + spawn.posZ);
				int y = world.getTopSolidOrLiquidBlock(x, z);
				activateInfection(x, y, z);
				infectionPos = new ChunkCoordinates(x, y, z);
				return true;
			}
		}
		
		return false;
	}
	
	public ChunkCoordinates getInfectionPos() {
		return infectionPos;
	}
	
	public boolean isInGameArea(int x, int y, int z) {
		double dx = infectionPos.posX - x;
		double dy = infectionPos.posY - y;
		double dz = infectionPos.posZ - z;
		
		double dist = sqrt(dx * dx + dy * dy + dz * dz);
		if (dist > 50 && dist < 150) {
			return true;
		}
		return false;
	}
	
	private InfectedBlock[] infectedBlock;
	public Map<Class<? extends Entity>, ActionEntity> entityAction;
	private Map<ChunkCoordinates, InfectionMapEntry> infectionMap;
	private List<InfectionMapEntry> activeInfection;
	private List<ChunkCoordinates> entitySpawn;
	private List<Entity> entityList;
	
	public ChunkCoordinates infectionPos = new ChunkCoordinates();
	private Random rand = new Random();
	private World world;
}
