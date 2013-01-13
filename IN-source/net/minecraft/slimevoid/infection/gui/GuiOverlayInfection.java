package net.minecraft.slimevoid.infection.gui;

import static org.lwjgl.opengl.GL11.GL_ALPHA_TEST;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glEnable;
import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.item.Item;
import net.minecraft.slimevoid.infection.shop.ShopManager;
import net.minecraft.src.mod_Infection;

public class GuiOverlayInfection extends GuiScreen {
	public GuiOverlayInfection() {
		mc = FMLClientHandler.instance().getClient();
		fontRenderer = mc.fontRenderer;
	}
	
	@Override
	public void drawScreen(int i, int j, float f) {
		super.drawScreen(i, j, f);
		
		ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        width = scaledresolution.getScaledWidth();
        height = scaledresolution.getScaledHeight();
		drawRect(4, height - 38, 54, height - 16, 0x50000000);
		drawRect(58, height - 38, 108, height - 16, 0x50000000);
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_ALPHA_TEST);
        glColor4f(1, 1, 1, 1);
		mc.renderEngine.bindTexture(mc.renderEngine.getTexture("/gui/items.png"));
		int amount = ShopManager.countCredits(mc.thePlayer);
		int tex = Item.diamond.getIconFromDamage(0);
		drawTexturedModalRect(4, height - 36, (tex % 16) * 16, (tex / 16) * 16, 16, 16);
		fontRenderer.drawStringWithShadow(""+amount, 4 + 18, height - 31, 0xFFFFFFFF);
		int chrono = (int) (System.currentTimeMillis() - mod_Infection.instance.getSessionStartTime()) / 1000;
		String str =( chrono / 60)+":"+(chrono % 60 < 10 ? "0"+(chrono % 60) : chrono % 60);
		fontRenderer.drawStringWithShadow(str, 58 + 25 - fontRenderer.getStringWidth(str) / 2, height - 31, 0xFFFFFFFF);
	}
}
