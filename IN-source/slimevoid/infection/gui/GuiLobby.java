package slimevoid.infection.gui;

import static java.lang.Math.PI;
import static java.lang.Math.sin;
import static org.lwjgl.opengl.GL11.GL_ALPHA_TEST;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glColorMask;
import static org.lwjgl.opengl.GL11.glCopyTexSubImage2D;
import static org.lwjgl.opengl.GL11.glDepthMask;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glScaled;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glTranslated;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glViewport;

import java.util.List;

import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiPlayerInfo;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.multiplayer.NetClientHandler;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.src.ModLoader;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.glu.GLU;

import slimevoid.infection.core.InfectionMod;

public class GuiLobby extends GuiScreen {
	
	public GuiLobby() {
		tickCounter = 0 ;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		viewportTexture = mc.renderEngine.allocateAndSetupTexture(new java.awt.image.BufferedImage(256, 256, 2));
		Keyboard.enableRepeatEvents(true);
		chatInput = new GuiTextField(fontRenderer, 20 + 150, height - 40, width - 40 - 150, 20);
		chatInput.setFocused(true);
		buttonReady = new GuiButton(0, 20, height - 40, 40, 20, "Ready");
		buttonKick = new GuiButton(1, 20 + 90, height - 40, 40, 20, "Kick");
		buttonKick.enabled = false;
		controlList.add(buttonReady);
		controlList.add(buttonKick);
	}
	
	@Override
	protected void actionPerformed(GuiButton guibutton) {
		switch(guibutton.id) {
		case 0:
			// TODO : Gui Packet Shizzle
			/**
			Packet230ModLoader packet = new Packet230ModLoader();
			packet.dataInt = new int[] {2, mod_Infection.instance.isPlayerReady(mc.thePlayer.username) ? 0 : 1};
			ModLoaderMp.sendPacket(mod_Infection.instance, packet);
			**/
			break;
		}
		super.actionPerformed(guibutton);
	}
	
	@Override
	protected void keyTyped(char c, int i) {
		switch(i) {
		case Keyboard.KEY_RETURN:
			String msg = chatInput.getText().trim();
			if(!msg.isEmpty()) {
				mc.thePlayer.sendChatMessage(msg);
				chatInput.setText("");
			}
			break;
		
		default:
			chatInput.textboxKeyTyped(c, i);
			break;
		}
		super.keyTyped(c, i);
	}
	
	@Override
	public void updateScreen() {
		super.updateScreen();
		tickCounter ++;
	}
	
	@Override
	public void drawScreen(int mX, int mY, float frameDelta) {
		renderSkybox(mX, mY, frameDelta);
		int playersWidth = 150;
		drawPlayerList(playersWidth);
		drawChat(playersWidth);
		drawTimer();
		super.drawScreen(mX, mY, frameDelta);
	}
	
	private void drawTimer() {
		drawRect(20 + 40 + 2, height - 40, 20 + 40 + 46, height - 20, 0xA0000000);
		String str;
		long timer = InfectionMod.instance.getSessionStartTime();
		boolean isCloseToEnd = timer - System.currentTimeMillis() < 10000 && timer > 0;
		if(timer > System.currentTimeMillis()) {
			str = ""+((timer - System.currentTimeMillis()) / 1000);
		} else {
			str = isCloseToEnd ? "\\o/" : "60";
		}
		glPushMatrix(); 
		glTranslated(20 + 40 + 2 + 44 / 2, height - 33 + 3, 0);
		double f = isCloseToEnd ? 2 : 1;
		glScaled(f, f, 1);
		int color = 0xFFFFFF ;
		if(isCloseToEnd) {
			double d = sin(((System.currentTimeMillis() - timer + 500) % 1000) / 1000D * PI); 
			int r = 0xBD + (int)(d * (0xF8 - 0xBD));
			int g = 0x74 + (int)(d * (0xC5 - 0x75));
			int b = 0x34 + (int)(d * (0x28 - 0x34));
			color = (r << 16) + (g << 8) + (b); // bd7434 => f8c528
 		}
		fontRenderer.drawString(str, -fontRenderer.getStringWidth(str) / 2, -4, color);
		glPopMatrix();
	}

	private void drawPlayerList(int playersWidth) {
		int margin = 20;
		drawRect(margin, margin, playersWidth, height - margin * 3, 0xA0000000);
		drawRect(margin, margin, playersWidth, margin + 1, 0xA0FFFFFF);
		drawRect(margin, height - margin * 3 - 1, playersWidth, height - margin * 3, 0xA0FFFFFF);
		drawRect(margin, margin, margin + 1, height - margin * 3, 0xA0FFFFFF);
		drawRect(playersWidth - 1, margin, playersWidth, height - margin * 3, 0xA0FFFFFF);
		NetClientHandler netclienthandler = ((EntityClientPlayerMP)mc.thePlayer).sendQueue;
        @SuppressWarnings("unchecked")
		List<GuiPlayerInfo> list = netclienthandler.playerInfoList;
        for(int i = 0; i < list.size(); i ++) {
        	String str = list.get(i).name;
        	int hPerName = 15;
        	fontRenderer.drawStringWithShadow(str, margin + 4, margin + 4 + i * hPerName, InfectionMod.instance.isPlayerReady(str) ? 0xFFFFFF : 0xFF2020);
        	drawRect(margin, margin + (i+1) * hPerName, playersWidth, margin + (i+1) * hPerName + 1, 0x40FFFFFF);
        }
	}
	
	@SuppressWarnings("unchecked")
	private void drawChat(int playersWidth) {
		int margin = 20;
		drawRect(playersWidth + margin, margin * 2 + 10, width - margin, height - margin * 3, 0xA0000000);
		drawRect(playersWidth + margin, margin * 2 + 10, width - margin, margin * 2 + 10 + 1, 0xA0FFFFFF);
		drawRect(playersWidth + margin, height - margin * 3, width - margin, height - margin * 3 - 1, 0xA0FFFFFF);
		drawRect(playersWidth + margin, margin * 2 + 10, playersWidth + margin + 1, height - margin * 3, 0xA0FFFFFF);
		drawRect(width - margin, margin * 2 + 10, width - margin - 1, height - margin * 3, 0xA0FFFFFF);
		int j = 0;
		int hPerName = 15;
		List<ChatLine> chat;
		try {
			chat = (List<ChatLine>) ModLoader.getPrivateValue(GuiIngame.class, mc.ingameGUI, 1);
			int maxMsgs = ((height - margin * 3) - (margin * 2 + 10)) / hPerName - 1;
			for(int i = chat.size() > maxMsgs ? maxMsgs : chat.size() - 1; i >= 0; i --) {
	        	String str = chat.get(i).getChatLineString();
	        	fontRenderer.drawStringWithShadow(str, margin + 4 + playersWidth, margin * 2 + 10 + 4 + j * hPerName, 0xFFFFFF);
	        	drawRect(margin + playersWidth, margin * 2 + 10 + (j+1) * hPerName, width - margin, margin * 2 + 10 + (j+1) * hPerName + 1, 0x40FFFFFF);
	        	j ++;
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
		chatInput.drawTextBox();
	}
	
	private void renderSkybox(int mX, int mY, float frameDelta) {
        glViewport(0, 0, 256, 256);
        drawPanorama(mX, mY, frameDelta);
        glDisable(GL_TEXTURE_2D);
        glEnable(GL_TEXTURE_2D);
        rotateAndBlurSkybox(frameDelta);
        rotateAndBlurSkybox(frameDelta);
        rotateAndBlurSkybox(frameDelta);
        rotateAndBlurSkybox(frameDelta);
        rotateAndBlurSkybox(frameDelta);
        rotateAndBlurSkybox(frameDelta);
        rotateAndBlurSkybox(frameDelta);
        rotateAndBlurSkybox(frameDelta);
        glViewport(0, 0, mc.displayWidth, mc.displayHeight);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        float f = width <= height ? 120F / (float)height : 120F / (float)width;
        float u = ((float)height * f) / 256F;
        float v = ((float)width * f) / 256F;
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR); 
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        tessellator.setColorRGBA_F(1, 1, 1, 1);
        int w = width;
        int h = height;
        tessellator.addVertexWithUV(0, h, zLevel, .5F - u, .5F + v);
        tessellator.addVertexWithUV(w, h, zLevel, .5F - u, .5F - v);
        tessellator.addVertexWithUV(w, 0, zLevel, .5F + u, .5F - v);
        tessellator.addVertexWithUV(0, 0, zLevel, .5F + u, .5F + v);
        tessellator.draw();
	}
	
	private void rotateAndBlurSkybox(float par1) {
		glBindTexture(GL_TEXTURE_2D, viewportTexture);
		glCopyTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, 0, 0, 256, 256);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glColorMask(true, true, true, false);
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		
		byte blur = 2;
		for (int i = 0; i < blur; i++) {
			tessellator.setColorRGBA_F(1, 1, 1, 1 / (float)(i + 1));
			int j = width;
			int k = height;
			float f = (float)(i - blur / 2) / 256F;
			tessellator.addVertexWithUV(j, k, zLevel, 0 + f, 0);
			tessellator.addVertexWithUV(j, 0, zLevel, 1 + f, 0);
			tessellator.addVertexWithUV(0, 0, zLevel, 1 + f, 1);
			tessellator.addVertexWithUV(0, k, zLevel, 0 + f, 1);
		}
		tessellator.draw();
		glColorMask(true, true, true, true);
	}
	
	private void drawPanorama(int par1, int par2, float par3) {
        Tessellator tessellator = Tessellator.instance;
        glMatrixMode(GL_PROJECTION);
        glPushMatrix();
        glLoadIdentity();
        GLU.gluPerspective(120, 1, .05F, 10);
        glMatrixMode(GL_MODELVIEW);
        glPushMatrix();
        glLoadIdentity();
        glColor4f(1, 1, 1, 1);
        glRotatef(180, 1, 0, 0);
        glEnable(GL_BLEND);
        glDisable(GL_ALPHA_TEST);
        glDisable(GL_CULL_FACE);
        glDepthMask(false);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        int i = 8;

        for (int j = 0; j < i * i; j++) {
            glPushMatrix();
            float f =  ((float)(j % i) / (float)i - .5F) / 64F;
            float f1 = ((float)(j / i) / (float)i - .5F) / 64F;
            float f2 = 0;
            glTranslatef(f, f1, f2);
            glRotatef(-((float)tickCounter + par3) * .1F, 0, 1, 0);

            for (int face = 0; face < 6; face++) {
                glPushMatrix();
                switch(face) {
                case 1:
                	glRotatef(90, 0, 1, 0);
                	break;
                case 2:
                	glRotatef(180, 0, 1, 0);
                	break;
                case 3:
                	glRotatef(-90, 0, 1, 0);
                	break;
                case 4:
                	glRotatef(90, 1, 0, 0);
                	break;
                case 5:
                	glRotatef(-90, 1, 0, 0);
                	break;
                }

                glBindTexture(GL_TEXTURE_2D, mc.renderEngine.getTexture((new StringBuilder()).append("/panorama/").append(face).append(".png").toString()));
                tessellator.startDrawingQuads();
                tessellator.setColorRGBA_I(0xffffff, 255 / (j + 1));
                float f3 = 0;
                tessellator.addVertexWithUV(-1, -1, 1, 0 + f3, 0 + f3);
                tessellator.addVertexWithUV( 1, -1, 1, 1 - f3, 0 + f3);
                tessellator.addVertexWithUV( 1,  1, 1, 1 - f3, 1 - f3);
                tessellator.addVertexWithUV(-1,  1, 1, 0 + f3, 1 - f3);
                tessellator.draw();
                glPopMatrix();
            }

            glPopMatrix();
            glColorMask(true, true, true, false);
        }

        tessellator.setTranslation(0, 0, 0);
        glColorMask(true, true, true, true);
        glMatrixMode(GL_PROJECTION);
        glPopMatrix();
        glMatrixMode(GL_MODELVIEW);
        glPopMatrix();
        glDepthMask(true);
        glEnable(GL_CULL_FACE);
        glEnable(GL_ALPHA_TEST);
        glEnable(GL_DEPTH_TEST);
    }
       
	private GuiTextField chatInput;
	private GuiButton buttonReady;
	private GuiButton buttonKick;
	
	private int viewportTexture;
	private int tickCounter;
}
