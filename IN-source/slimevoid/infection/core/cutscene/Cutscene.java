package slimevoid.infection.core.cutscene;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.src.ModLoader;

public abstract class Cutscene {
	public Cutscene() {
		over = true;
		running = false;
		modal = false;
		focus = true;
	}
	
	public void render2D(float frameDelta) {
		if(over || currentScene == null) {
			return;
		}
		currentScene.render2D(frameDelta, System.currentTimeMillis() - currentSceneStart);
	}
	
	public void render3D(float frameDelta) {
		if(over || currentScene == null) {
			return;
		}
		currentScene.render3D(frameDelta, System.currentTimeMillis() - currentSceneStart);
	}
	
	public void onTick() {
		if(over) {
			return;
		}
		if(System.currentTimeMillis() - currentSceneStart > currentScene.getDuration()) {
			nextScene();
		}
		if(currentScene == null) {
			return;
		}
		currentScene.onTick(System.currentTimeMillis() - currentSceneStart);
	}
	
	public void init(){
		if(!running) {
			running = true;
			over = false;
			
			initScenes();
			nextScene();
		}
	}
	
	private void nextScene() {
		if(over)  {
			return;
		}
		
		int currentIndex = currentScene == null ? -1 : scenes.indexOf(currentScene);
		if(currentScene != null) {
			currentScene.end();
		}
		if(currentIndex + 1 >= scenes.size()) {
			end();
		} else {
			currentScene = scenes.get(currentIndex + 1);
			currentSceneStart = System.currentTimeMillis();
			currentScene.start();
		}
	}
	
	private void end() {
		over = true;
		currentScene = null;
		currentSceneStart = -1;
	}
	
	protected abstract void initScenes();
	
	protected void addScene(Scene scene) {
		if(scene != null) {
			scenes.add(scene);
		}
	}
	
	public void setupCamera(float frameDelta) {
		if(over || currentScene == null) {
			return;
		}
		currentScene.setupCamera(frameDelta, System.currentTimeMillis() - currentSceneStart);
	}
	
	public void focusLost() {
		focus = false;
	}
	
	public void focusGained() {
		focus = true;
	}
	
	public Scene getCurrentScene() {
		return currentScene;
	}
	
	public boolean isOver() {
		return over;
	}
	
	/**
	 * If false, allow user to open chat gui and main menu
	 */
	public boolean isModal() {
		return modal;
	}
	
	public boolean hasFocus() {
		return focus;
	}
	
	private List<Scene> scenes = new ArrayList<Scene>();
	
	private boolean focus;
	/**
	 * If false, allow user to open chat gui and main menu
	 */
	private boolean modal;
	
	private long currentSceneStart = -1;
	private boolean over;
	private boolean running;
	private Scene currentScene;
	
	protected static Random rand = new Random();
	protected static Minecraft mc = ModLoader.getMinecraftInstance();
}
