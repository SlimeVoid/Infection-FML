package net.minecraft.src;

/* TODO:
 * V	- Interdire la pose de blocks
 * 		- Regenerer la map a chaque sessions
 * 		- Retour au lobby quand la session est terminée
 * 		- Finir le mode Spec
 *      - 
 */

import static java.lang.Math.sqrt;
import net.minecraft.slimevoid.infection.InfectionGameSession;
import net.minecraft.slimevoid.infection.InfectionGamemode;
import net.minecraft.slimevoid.infection.blocks.BlockGroundDiamond;
import net.minecraft.slimevoid.infection.blocks.BlockInfected;
import net.minecraft.slimevoid.infection.blocks.BlockInfectedLeaves;
import net.minecraft.slimevoid.infection.blocks.BlockInfectedSky;
import net.minecraft.slimevoid.infection.blocks.BlockInfectedWater;
import net.minecraft.slimevoid.infection.blocks.BlockInfectedWood;
import net.minecraft.slimevoid.infection.blocks.BlockSpawn;
import net.minecraft.slimevoid.infection.entities.EntityInfectedArrow;
import net.minecraft.slimevoid.infection.mobs.EntityInfectedSkeleton;
import net.minecraft.slimevoid.infection.mobs.EntityInfectedSpider;
import net.minecraft.slimevoid.infection.mobs.EntityInfectedWorm;
import net.minecraft.slimevoid.infection.mobs.EntityInfectedZombie;
import net.minecraft.slimevoid.infection.shop.ShopItem;
import net.minecraft.slimevoid.infection.shop.ShopManager;


public class mod_Infection extends mod_SlimeVoid {
	
	// SERVER
	
	public mod_Infection() {
		gamemode = new InfectionGamemode("Infection", 4);
		instance = this;
		
		ModLoader.setInGameHook(this, true, true);
		
		gameSession = new InfectionGameSession(gamemode);
	}
	
	public static void initModLoaderRegistrations() {
		INFECTED = new BlockInfected(131).setHardness(100F);
		INFECTED_SKY = new BlockInfectedSky(132).setHardness(100F);
		INFECTED_WOOD = new BlockInfectedWood(133).setHardness(100F);
		INFECTED_WATER = new BlockInfectedWater(134).setHardness(100F);
		INFECTED_LEAVES = new BlockInfectedLeaves(136).setHardness(.2F);
		SPAWN = new BlockSpawn(137).setHardness(-1);
		GROUND_DIAMOND = new BlockGroundDiamond(138).setHardness(-1);
		ModLoader.registerBlock(INFECTED);
		ModLoader.registerBlock(INFECTED_SKY);
		ModLoader.registerBlock(INFECTED_WOOD);
		ModLoader.registerBlock(INFECTED_WATER);
		ModLoader.registerBlock(INFECTED_LEAVES);
		ModLoader.registerBlock(SPAWN);
		ModLoader.registerBlock(GROUND_DIAMOND);
		
		ModLoader.registerEntityID(EntityInfectedSpider.class, "Infected Spider", 154);
		ModLoader.registerEntityID(EntityInfectedZombie.class, "Infected Zombie", 155);
		ModLoader.registerEntityID(EntityInfectedSkeleton.class, "Infected Skeleton", 156);
		ModLoader.registerEntityID(EntityInfectedWorm.class, "Infected Worm", 157); 
		ModLoader.registerEntityID(EntityInfectedArrow.class, "Infected Arrow", 158);
		ModLoaderMp.registerEntityTracker(EntityInfectedArrow.class, 2048, 110);
		ModLoaderMp.registerEntityTrackerEntry(EntityInfectedArrow.class, 110);
		
		CraftingManager.getInstance().getRecipeList().clear();
	}
	
	@Override
	public void onPlayerJoined(EntityPlayer player) {
		if(gameSession.isStarted()) {
			gameSession.setPlayerReady(player, false);
		}
		if (InfectionGamemode.infection.infectionPos != null) {
			Packet230ModLoader packet = new Packet230ModLoader();
			ChunkCoordinates pos = InfectionGamemode.infection.infectionPos;
			packet.dataInt = new int[] {0, pos.posX, pos.posY, pos.posZ};
			ModLoaderMp.sendPacketTo(this, (EntityPlayerMP)player, packet);
		}
		gameSession.updatePlayersReadyness((EntityPlayerMP) player);
		Packet230ModLoader packet = new Packet230ModLoader();
		packet.dataInt = new int[] {4, gameSession.isStarted() ? 1 : 0};
		ModLoaderMp.sendPacketTo(mod_Infection.instance, (EntityPlayerMP) player, packet);
	}
	
	@Override
	public void handlePacket(Packet230ModLoader packet, EntityPlayerMP player) {
		int[] d = packet.dataInt;
		World world = ModLoader.getMinecraftServerInstance().getWorldManager(0);
		switch(d[0]) {
		// 0 : INFECTION START POINT
		case 1: // BUY
			ChunkCoordinates spawnBlockPos = gamemode.getSpawnBlock();
			if(spawnBlockPos != null && player.getDistanceSq(spawnBlockPos.posX, spawnBlockPos.posY, spawnBlockPos.posZ) < 64 && d[1] >= 0 && d[1] < ShopItem.values().length) {
				ShopItem requestedItem = ShopItem.values()[d[1]];
				if(ShopManager.canAfford(player, requestedItem)) {
					int paid = 0;
					while(paid < requestedItem.getCost()) {
						if(!player.inventory.consumeInventoryItem(Item.diamond.shiftedIndex)) {
							System.out.println("Dafuq did just happend?");
							break;
						}
						paid ++;
					}
					ItemStack is = new ItemStack(requestedItem.getItem().itemID, requestedItem.getItem().stackSize, 0);
					if(!player.inventory.addItemStackToInventory(is)) {
						System.out.println("Invetory full");
					}
				}
			}
			break;
			
		case 2: // LOBBY READY
			gameSession.setPlayerReady(player, d[1] == 1 ? true : false);
			break;
			
			// 3 : PLAYER READYNESS
			// 4 : SESSION STATUS
			
		case 5: // PLAYER FOLLOWING
			gameSession.setPlayerFollowing(player, (EntityPlayerMP) world.getPlayerEntityByName(packet.dataString[0]));
			break;
		}
	}
	
	public static Block INFECTED ;
	public static Block INFECTED_SKY;
	public static Block INFECTED_WOOD;
	public static Block INFECTED_WATER;
	public static Block INFECTED_LAVA;
	public static Block INFECTED_LEAVES;
	public static Block SPAWN;
	public static Block GROUND_DIAMOND;

	@Override
	public String getVersion() {
		return "1.0";
	}

	public static mod_Infection instance;
	public final InfectionGamemode gamemode;
	public static InfectionGameSession gameSession;
	
}
