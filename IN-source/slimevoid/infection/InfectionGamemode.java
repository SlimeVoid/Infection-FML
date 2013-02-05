package slimevoid.infection;

import static java.lang.Math.abs;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import slimevoid.infection.actions.ActionEntity;
import slimevoid.infection.core.InfectionMod;
import slimevoid.infection.core.game.Gamemode;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.packet.Packet3Chat;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldType;
import net.minecraftforge.common.DimensionManager;

public class InfectionGamemode extends Gamemode {
	
	public Random rand = new Random();
	public final int MAX_INFECTIONS = 3;
	public static Infection infection;
	private ChunkCoordinates spawnBlock;
	public static final int SPAWN_WIDTH = 14;
	public static final int SPAWN_HEIGHT = 8;
	public static final int SPAWN_UNDERGROUND = 3;

	public InfectionGamemode(String name, int id) {
		super("Infection", 4);
	}

	@Override
	public WorldProvider getWorldProvider() {
		return null;
	}

	@Override
	public WorldType getWorldType() {
		return null;
	}
	
	
	@Override
	public void onEnable(World world, MinecraftServer server) {
		super.onEnable(world, server);
		// TODO :: Needed? mod_Infection.initModLoaderRegistrations();
		infection = new Infection();
	}
	
	@Override
	public boolean doRegenWorlAtStart() {
		return true;
	}
	
	public boolean expandInfection(InfectionMapEntry entry) {
		int x = entry.coord.posX;
		int y = entry.coord.posY;
		int z = entry.coord.posZ;
		World world = DimensionManager.getWorld(0);
		int infector = world.getBlockId(x, y, z), blockToInfect = 0;
		int availableAir = 0;
		List<ChunkCoordinates> possibleInfections = new ArrayList<ChunkCoordinates>();
		
		// Can Infect Around?
		for(int i = x - 1; i <= x + 1; i++) {
			for(int j = y - 1; j <= y + 1; j++) {
				for(int k = z - 1; k <= z + 1; k++) {
					blockToInfect = world.getBlockId(i, j, k);
					if (blockToInfect == 0) {
						availableAir ++;
					}
					ChunkCoordinates cc2 = new ChunkCoordinates(i, j, k);
					if(infection.canInfect(cc2, blockToInfect)) {
						possibleInfections.add(cc2);
					}
				}
			}
		}
		
		if (possibleInfections.size() > 0) {
			ChunkCoordinates cc2 = possibleInfections.get(rand.nextInt(possibleInfections.size()));
			blockToInfect = world.getBlockId(cc2.posX, cc2.posY, cc2.posZ);
			return infect(cc2.posX, cc2.posY, cc2.posZ, infector, blockToInfect, availableAir);
		}
		infection.setInactiveInfectedBlock(entry);
		return false;
	}
	
	public boolean infect(int x, int y, int z, int infector, int infected, int availableAir) {
		World world = DimensionManager.getWorld(0);
		
		ChunkCoordinates cc = new ChunkCoordinates(x, y, z);
		boolean ret = false;
		InfectionMapEntry entry = infection.getInfectedBlock(x, y, z);
		if(entry == null) {
			int resistance = infection.getBlockResistance(infected) * ((26 - availableAir)) / 13 - 1;
			entry = new InfectionMapEntry(cc, infected, resistance);
			infection.addInfectedBlock(entry);
		}
		if (entry.resistanceLeft <= 0) {
			if (infection.getBlockResistance(infected) != 0) {
				infection.addEntitySpawn(entry.coord);
				infection.setActiveInfectedBlock(entry);
				world.setBlockWithNotify(x, y, z, infection.getBlockInfector(infected));
			} else {
				world.setBlockWithNotify(x, y, z, 0);
			}
			
			ret = true;
		} else {
			entry.resistanceLeft --;
		}
		
		
		if (world.getTopSolidOrLiquidBlock(x, z) == y + 1) {
			for(int i = x - 1; i <= x + 1; i++) {
				for(int k = z - 1; k <= z + 1; k++) {
					world.setBlockWithNotify(i, 128, k, InfectionMod.INFECTED_SKY.blockID);
				}
			}
			world.setBlockWithNotify(x + 2, 128, z, InfectionMod.INFECTED_SKY.blockID);
			world.setBlockWithNotify(x - 2, 128, z, InfectionMod.INFECTED_SKY.blockID);
			world.setBlockWithNotify(x, 128, z + 2, InfectionMod.INFECTED_SKY.blockID);
			world.setBlockWithNotify(x, 128, z - 2, InfectionMod.INFECTED_SKY.blockID);
		}
		
		return ret;
	}
	
	public void desinfect(int x, int y, int z) {
		desinfect(x, y, z, false);
	}
	
	public void desinfect(int x, int y, int z, boolean createMatter) {
		
		World world = DimensionManager.getWorld(0);
		InfectionMapEntry entry = infection.getInfectedBlock(x, y, z);
		if(entry != null) {
			infection.setInactiveInfectedBlock(entry);
			infection.removeInfectedBlock(entry);
			infection.removeEntitySpawn(entry.coord);
			infection.removeEntitySpawn(infection.getKeyForPos(x, y, z));
			if(createMatter) {
				world.setBlockWithNotify(x, y, z, Block.dirt.blockID);
			}
		}
	}
	
	public void infectEntity(World world, int i, int j, int k, Entity entity) {
		if(entity instanceof EntityLiving && !(entity instanceof EntityPlayer)) {
			if(infection.entityAction.containsKey(entity.getClass())) {
				ActionEntity aEntity = infection.entityAction.get(entity.getClass());
				aEntity.onInfect(world, entity);
			}
//		} else if (!(entity instanceof EntityPlayer)) {
//			entity.setDead(); 
		}
		if ((entity instanceof EntityItem)) {
			int age = ((EntityItem) entity).age;
			if (age < 100) {
				entity.motionY = 0.25F - 0.25F * ((float) age / 50F);
				entity.onGround = false;
				entity.addVelocity(0, 0.25F - 0.25F * ((float) age / 50F), 0);
			}
			
		}
	}
	
	public void setSpawnBlock(int x, int y, int z) {
		spawnBlock = new ChunkCoordinates(x, y, z);
	}
	
	public ChunkCoordinates getSpawnBlock() {
		return spawnBlock;
	}
	
	@Override
	public boolean canPlayerEdit(EntityPlayer player, int x, int y, int z, int id, boolean mine) {
		EntityPlayerMP p = (EntityPlayerMP) player;
		if(!mine) {
			if(id == Block.torchWood.blockID) {
				return true;
			}
			if(id == InfectionMod.SPAWN.blockID) {
				boolean flag = spawnBlock == null;
				if(!flag) {
					p.playerNetServerHandler.sendPacketToPlayer(new Packet3Chat("You can't place the spawn block here")) ;
				}
				return flag;
			}
		} else {
			if(id == Block.leaves.blockID || id == InfectionMod.INFECTED_LEAVES.blockID) {
				return true;
			}
		}
		if(spawnBlock == null) {
			p.playerNetServerHandler.sendPacketToPlayer(new Packet3Chat("You need to place the spawn block first")) ;
			return false;
		}
		int diffX = abs(spawnBlock.posX - x);
		int diffY = y - spawnBlock.posY;
		int diffZ = abs(spawnBlock.posZ - z);
		boolean flag = diffX < SPAWN_WIDTH / 2 && diffY > -SPAWN_UNDERGROUND && diffY < SPAWN_HEIGHT && diffZ < SPAWN_WIDTH / 2;
		if(!flag) {
			p.playerNetServerHandler.sendPacketToPlayer(new Packet3Chat("You can't edit here because the spawn block is too far away")) ;
		}
		return flag;
	}
}
