package net.minecraft.slimevoid.infection;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.slimevoid.GameSession;
import net.minecraft.slimevoid.infection.actions.ActionEntity;
import net.minecraft.src.mod_Infection;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.EnumGameType;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class InfectionGameSession extends GameSession {
	
	public InfectionGameSession(InfectionGamemode gamemode) {
		super("Infection Session", -1);
		this.gamemode = gamemode;
		preStart = 1;
		playersReadyness = new HashMap<EntityPlayer, Boolean>();
		restartOnStart = false;
		playerFollowings = new HashMap<EntityPlayerMP, EntityPlayerMP>();
	}
	
	@Override
	public void onPreStart() {
		boolean found = false;
		while(!found) {
			found = InfectionGamemode.infection.findAndStartInfection();
		}
		genereateDiamonds();
		sessionStartTime = -1;
		sessionEndTime = -1;
	}
	
	@Override
	public boolean canStart() {
		if(sessionStartTime == -1) {
			/*for(EntityPlayer player : mod_SlimeVoid.getPlayersList()) {
				if(isPlayerReady(player)) {
					sessionStartTime = System.currentTimeMillis() + 60000L;
					updatePlayersReadyness();
				}
			}*/
			return false;
		}
		return sessionStartTime < System.currentTimeMillis();
	}
	
	@Override
	public void onStart() {
		super.onStart();
		/*Packet230ModLoader packet = new Packet230ModLoader();
		packet.dataInt = new int[] {4, 1};
		ModLoaderMp.sendPacketToAll(mod_Infection.instance, packet);
		for(EntityPlayerMP player : mod_SlimeVoid.getPlayersList()) {
			if(isPlayerReady(player)) {
				player.inventory.addItemStackToInventory(new ItemStack(mod_Infection.SPAWN));
				break;
			}
		}*/
	}
	
	@Override
	public void onTick() {
		super.onTick();
		if(isStarted) {
			for(Entry<EntityPlayer, Boolean> e : playersReadyness.entrySet()) {
				EntityPlayerMP player = (EntityPlayerMP) e.getKey();
				if(e.getValue()) {
					if(player.isDead) {
						setPlayerReady(player, false);
						if(playersReadyness.size() <= 0) {
							sessionEndTime = System.currentTimeMillis();
						}
					} else if(player.capabilities.allowFlying) {
						player.theItemInWorldManager.setGameType(EnumGameType.CREATIVE);
					}
				}
			}
			World world = DimensionManager.getWorld(0);
			world.getWorldInfo().setRaining(false); 
			
			/* Temp reset */
			boolean reset = false;
			if (reset) {
				InfectionGamemode.infection.init();
			}
			
			/* Do infections */
			for(int i = 0; i < gamemode.MAX_INFECTIONS; i ++) {
				InfectionMapEntry entry = InfectionGamemode.infection.getRandomActiveInfectedBlock();
				if (entry != null) {
					if (!gamemode.expandInfection(entry)) {
						i --;
					}
				} else {
					break;
				}
			}
	
			
			/* Randomly Spawn Infected Entity*/
			ChunkCoordinates coord = InfectionGamemode.infection.getRandomEntitySpawn();	
			if (InfectionGamemode.infection.getInfectionSize() >= 1 && coord != null) {
				maxInfectedEntity = (int) (InfectionGamemode.infection.getPossibleSpawnSize() * 0.025f);
				for (int iEntity = 0; iEntity < InfectionGamemode.infection.getEntityListSize(); iEntity ++) {
					Entity entity = InfectionGamemode.infection.getEntity(iEntity);
					if (entity.isDead) {
						InfectionGamemode.infection.removeEntity(entity);
						iEntity --;
					}
				}
				currentInfectedEntity = InfectionGamemode.infection.getEntityListSize();
				if (currentInfectedEntity < maxInfectedEntity) {
					ActionEntity aEntity = InfectionGamemode.infection.getRandomActionEntity();
					Class<? extends Entity> entityType = aEntity.getLinkedEntityType();
					if (aEntity.canSpawnOnInfection() && rand.nextFloat() <= aEntity.getSpawnChance()) {
						InfectionGamemode.infection.spawnEntity(world, ActionEntity.createEntity(world, entityType), coord.posX + 0.5f, coord.posY + 1.0, coord.posZ + 0.5f, 0, 0);
					}
				}
			}
			// TODO : WTF?
/*			for(EntityPlayerMP player : mod_SlimeVoid.getPlayersList()) {
				if(!isPlayerReady(player) && player != null) {
					if(player.theItemInWorldManager != null && player.theItemInWorldManager.getGameType() != 1) {
						player.theItemInWorldManager.setGameType(1);
					}
					
					if(!playerFollowings.containsKey(player)) {
						EntityPlayerMP followed = playerFollowings.get(player);
						if(followed != null) {
							player.setPosition(followed.posX, followed.posY - 10, followed.posZ);
						}
					}
				}
			}*/
		}
	}
	
	private void genereateDiamonds() {
		World world = DimensionManager.getWorld(0);
		for(int i = 0; i < 120;) {
			ChunkCoordinates spawn = world.getSpawnPoint();
			double dist = rand.nextDouble() * 200; // dist € [0;200]
			double rad = rand.nextFloat() * PI * 2;
			int x = (int) (Math.floor(cos(rad) * dist) + spawn.posX);
			int z = (int) (Math.floor(sin(rad) * dist) + spawn.posZ);
			int y = world.getTopSolidOrLiquidBlock(x, z);
			if (world.getBlockMaterial(x, y, z) != Material.water && world.getBlockMaterial(x, y + 1, z) != Material.water && rand.nextFloat() < dist / 200F) {
				i ++;
				world.setBlock(x, y, z, mod_Infection.GROUND_DIAMOND.blockID);
			}
		}
	}

	public void setPlayerReady(EntityPlayer player, boolean ready) {
		playersReadyness.put(player, ready);
		updatePlayersReadyness();
	}
	
	/**
	 * It also means that the player is "in game" ; "not a spec"
	 */
	public boolean isPlayerReady(EntityPlayer player) {
		if(!playersReadyness.containsKey(player)) {
			return false;
		}
		return playersReadyness.get(player);
	}
	
	public void updatePlayersReadyness() {
		updatePlayersReadyness(null);
	}
	
	public void updatePlayersReadyness(EntityPlayerMP client) {
		// TODO : Moar and Moar Packet shizzle
/*		Packet230ModLoader packet = new Packet230ModLoader();
		List<EntityPlayerMP> players = mod_SlimeVoid.getPlayersList();
		packet.dataInt = new int[2 + players.size()];
		packet.dataString = new String[players.size()];
		packet.dataInt[0] = 3;
		packet.dataInt[1] = sessionStartTime < 0 ? -1 : (int)((sessionStartTime - System.currentTimeMillis()) / 100);
		for(int i = 0; i < players.size(); i++) {
			packet.dataString[i] = players.get(i).username;
			packet.dataInt[i + 2] = isPlayerReady(players.get(i)) ? 1 : 0;
		}
		if(client != null) {
			ModLoaderMp.sendPacketTo(mod_Infection.instance, client, packet);
		} else {
			ModLoaderMp.sendPacketToAll(mod_Infection.instance, packet);
		}*/
	}
	
	public void setPlayerFollowing(EntityPlayerMP spec, EntityPlayerMP followed) {
		playerFollowings.put(spec, followed);
	}

	private InfectionGamemode gamemode;
	public int maxInfectedEntity = 0;
	public int currentInfectedEntity = 0;
	public Random rand = new Random();
	
	/**
	 * Timestamps of when the session will start / has started
	 */
	private long sessionStartTime;
	private long sessionEndTime;
	private Map<EntityPlayer, Boolean> playersReadyness;
	private Map<EntityPlayerMP, EntityPlayerMP> playerFollowings;
}
