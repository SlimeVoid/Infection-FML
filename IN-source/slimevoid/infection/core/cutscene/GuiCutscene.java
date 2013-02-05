package slimevoid.infection.core.cutscene;

import static org.lwjgl.opengl.GL11.GL_ALPHA_TEST;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTranslatef;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiScreen;

import org.lwjgl.input.Keyboard;

public class GuiCutscene extends GuiScreen {
	
	public GuiCutscene(Cutscene cutscene) {
		super();
		this.cutscene = cutscene;
		this.allowUserInput = false;
		this.modal = cutscene.isModal();
	}
	
	@Override
	public void initGui() {
		super.initGui();
		cutscene.init();
		mc.gameSettings.hideGUI = true;
	}
	
	@Override
	protected void keyTyped(char c, int i) {
		if(i == 1) {
			if(!(subScreen instanceof GuiIngameMenu)) {
				mc.displayGuiScreen(new GuiIngameMenu());
				checkScreen();
			}
		}
		if(i == Keyboard.KEY_T) {
			if(!(subScreen instanceof GuiChat)) {
				mc.displayGuiScreen(new GuiChat());
				checkScreen();
			}
		}
		if(i != 1) { // Disable ESC => Quit Gui
			super.keyTyped(c, i);
		}
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	
	@Override
	public void onGuiClosed() {
		mc.gameSettings.hideGUI = false;
		super.onGuiClosed();
	}
	
	public Cutscene getCutscene() {
		return cutscene;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float frameDelta) {
		cutscene.render2D(frameDelta);
		
		glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDisable(GL_ALPHA_TEST);
        glPushMatrix();
        glTranslatef(0, height - 48, 0);
        // TODO :: WTF? mc.ingameGUI.func_50010_a(fontRenderer);
        glPopMatrix();
		
		super.drawScreen(mouseX, mouseY, frameDelta);
		if(!modal && subScreen != null) {
			subScreen.drawScreen(mouseX, mouseY, frameDelta);
			checkScreen();
		}
	}
	
	public void render3D(float frameDelta) {
		cutscene.render3D(frameDelta);
	}
	
	public void setupCamera(float frameDelta) {
		cutscene.setupCamera(frameDelta);
	}
	
	@Override
	public void updateScreen() {
		super.updateScreen();
		
		cutscene.onTick();
		
		if(!modal && subScreen != null) {
			subScreen.updateScreen();
			checkScreen();
		}
		
		if(cutscene.isOver()) {
			mc.displayGuiScreen(null);
			mc.setIngameFocus();
		}
	}
	
	@Override
	public void setWorldAndResolution(Minecraft minecraft, int w, int h) {
		super.setWorldAndResolution(minecraft, w, h);
		if(!modal && subScreen != null) {
			subScreen.setWorldAndResolution(minecraft, w, h);
			checkScreen();
		}
	}
	
	@Override
	public void handleInput() {
		super.handleInput();
		if(!modal && subScreen != null) {
			subScreen.handleInput();
			checkScreen();
		}
	}
	
	@Override
	public void handleMouseInput() {
		super.handleMouseInput();
		if(!modal && subScreen != null) {
			subScreen.handleMouseInput();
			checkScreen();
		}
	}
	
	@Override
	public void handleKeyboardInput() {
		if(!modal && subScreen != null) {
			subScreen.handleKeyboardInput();
			checkScreen();
		} else {
			super.handleKeyboardInput();
		}
	}
	
	private void checkScreen() {
		if(mc.currentScreen != this && mc.theWorld != null) {
			GuiScreen prevSub = subScreen;
			subScreen = mc.currentScreen;
			mc.displayGuiScreen(this);
			if(prevSub == null && subScreen != null) {
				cutscene.focusLost();
			} else if(prevSub != null && subScreen	 == null) {
				cutscene.focusGained();
			}
			mc.gameSettings.hideGUI = true;
		} 
	}
	
	public boolean hasFocus() {
		return subScreen == null;
	}
	
	public GuiScreen getSubScreen() {
		return subScreen;
	}
	
	private GuiScreen subScreen;
	
	/**
	 * If false, allow user to open chat gui and main menu
	 */
	private boolean modal;
	
	private final Cutscene cutscene;
}
