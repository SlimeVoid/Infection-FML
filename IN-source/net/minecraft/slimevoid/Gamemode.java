package net.minecraft.slimevoid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cpw.mods.fml.server.FMLServerHandler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldType;


public abstract class Gamemode {
	/**
	 * Create new Gamemode
	 * @param name
	 * @param id
	 */
	public Gamemode(String name, int id) {
		gamemodes.put(id, this);
		
		this.name = name;
		this.id = id;
	}
	
	public abstract WorldProvider getWorldProvider();
	
	public void onEnable(World world, MinecraftServer server) {
		isEnabled = true;
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean doRegenWorlAtStart(){
		return false;
	}
	
	public abstract WorldType getWorldType();
	
	public boolean keepChunkUnloading() {
		return true;
	}
	
	public static Gamemode getGamemodeById(int id) {
		return gamemodes.get(id);
	}
	
	public static Gamemode getGamemodeByName(String name) {
		for(Entry<Integer, Gamemode> entry : gamemodes.entrySet()) {
			if(entry.getValue().name.equalsIgnoreCase(name)) {
				return entry.getValue();
			}
		}
		return null;
	}
	
	public void onTick() {
		List<GameSession> sessionsList = new ArrayList<GameSession>(GameSession.getSessionsList());
		for (GameSession session : sessionsList) {
				session.onTick();
		}
	}

	public boolean isEnabled() {
		return isEnabled;
	}
	
	public void setWorld(World world) {
		this.world = world;
	}
	
	public World getWorld() {
		return this.world;
	}
	
	public boolean canPlayerEdit(EntityPlayer player, int x, int y, int z, int id, boolean mine) {
		return true;
	}
	
	protected World world;
	protected WorldProvider worldProvider;
	private final int id;
	private final String name;
	private final static Map<Integer, Gamemode> gamemodes = new HashMap<Integer, Gamemode>();
	private boolean isEnabled = false;
	protected final static MinecraftServer server = FMLServerHandler.instance().getServer();
}