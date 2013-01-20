package slimevoid.infection.gui;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSmallButton;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;

import org.lwjgl.opengl.GL12;

import slimevoid.infection.shop.ShopItem;
import slimevoid.infection.shop.ShopManager;
import slimevoid.infection.shop.ShopItem.ItemType;

public class GuiShop extends GuiScreen {
	
	public GuiShop() {
		displayedItems = new ArrayList<ShopItem>();
	}
	
	@Override
	public void drawScreen(int mX, int mY, float frameDelta) {
		drawDefaultBackground();
		glEnable(GL_BLEND);
		drawFrameBackground();
		mc.renderEngine.bindTexture(mc.renderEngine.getTexture("/achievement/bg.png"));
		drawTexturedModalRect(frameSx, frameSy, 0, 0, FRAME_W, FRAME_H);
		int x = frameISx + 4;
		int y = frameISy + 4;
		ShopItem hooveredItem = getShopItemAt(mX, mY);
		for(int i = scroll * 2; i < displayedItems.size(); i ++) {
			if(i < 0) {
				continue;
			}
			ShopItem shopItem = displayedItems.get(i);
			drawShopItem(shopItem, x, y, selectedItem == shopItem, hooveredItem == shopItem);
			if(x + 108 * 2 < frameISx + FRAME_IW) {
				x += 108;
			} else if(y + 24 + 22 < frameISy + FRAME_IH) {
				y += 24;
				x = frameISx + 4;
			} else {
				break;
			}
		}
		drawFilters();
		super.drawScreen(mX, mY, frameDelta);
	}
	
	public void drawFrameBackground() {
		mc.renderEngine.bindTexture(mc.renderEngine.getTexture("/terrain.png"));
		int tex = Block.planks.getBlockTextureFromSideAndMetadata(0, 2);
		int u = (tex % 16) * 16;
		int v = (tex / 16) * 16;
		for(int x = 0; x < 14; x++) {
			for(int y = 0; y < 10; y++) {
				drawTexturedModalRect(frameISx + x * 16, frameISy + y * 16, u, v, 16, 16);
			}
		}
	}
	
	@Override
	protected void mouseClicked(int mX, int mY, int bouton) {
		super.mouseClicked(mX, mY, bouton);
		ShopItem item = getShopItemAt(mX, mY);
		ItemType type = getItemTypeAt(mX, mY);
		if(item != null) {
			selectedItem = item;
			buttonBuy.enabled = ShopManager.canAfford(mc.thePlayer, selectedItem);
		}
		if(type != null) {
			if(type != selectedType) {
				selectedType = type;
				if(selectedItem != null && selectedItem.getType() != type) {
					selectedItem = null;
					buttonBuy.enabled = false;
				} 
				displayedItems.clear();
				for(ShopItem i : ShopItem.values()) {
					if(i.getType() == type) {
						displayedItems.add(i);
					}
				}
				scroll = 0;
			}
		}
		updateButtonsState();
	}
	
	@Override
	protected void actionPerformed(GuiButton guibutton) {
		super.actionPerformed(guibutton);
		if(guibutton.enabled) {
			switch(guibutton.id) {
			case 0: // TODO : BUY packet
/*				Packet230ModLoader packet = new Packet230ModLoader();
				packet.dataInt = new int[]{1, selectedItem.ordinal()};
				ModLoaderMp.sendPacket(mod_Infection.instance, packet);*/
				break;
				
			case 1: // DONE
				mc.displayGuiScreen(null);
				break;
				
			case 2: // UP
				scroll --; 
				break;
				
			case 3: // DOWN 
				scroll ++;
				break;
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		super.initGui();
		frameSx = width / 2 - FRAME_W / 2;
		frameSy = height / 2 - FRAME_H / 2;
		frameISx = frameSx + 16;
		frameISy = frameSy + 17;
		buttonBuy = new GuiSmallButton(0, width / 2 + FRAME_IW / 2 - 50 - 50 - 4, height / 2 + FRAME_IH / 2 - 2, 50, 20, "Buy");
		buttonBuy.enabled = selectedItem != null ? ShopManager.canAfford(mc.thePlayer, selectedItem) : false;
		buttonDone = new GuiSmallButton(1, width / 2 + FRAME_IW / 2 - 50, height / 2 + FRAME_IH / 2 - 2, 50, 20, "Done");
		buttonUp   = new GuiButton(2, frameISx + FRAME_IW - 2, frameISy + 2, 20, 20, "/\\");
		buttonDown = new GuiButton(3, frameISx + FRAME_IW - 2, frameISy + FRAME_IH - 20 - 2, 20, 20, "\\/");
		controlList.add(0, buttonBuy);
		controlList.add(1, buttonDone);
		controlList.add(2, buttonUp);
		controlList.add(3, buttonDown);
		updateButtonsState();
	}
	
	/**
	 * height: 22
	 * width : 104
	 */
	public void drawShopItem(ShopItem shopItem, int x, int y, boolean isSelected, boolean isHoovered) {
		glDisable(GL_LIGHTING);
//		glEnable(GL_rES)
		boolean canAfford = ShopManager.canAfford(mc.thePlayer, shopItem);
		float light = canAfford ? 1 : .25F;
		glColor4f(light, light, light, 1);
		mc.renderEngine.bindTexture(mc.renderEngine.getTexture("/achievement/bg.png"));
		if(isSelected) {
			drawTexturedModalRect(x - 2, y - 2, 26, 202, 26, 26);
		} else {
			drawTexturedModalRect(x, y, 2, 204, 22, 22);
		}
		
		int textWidth = 82;
		drawRect(x + 22, y + 2, x + 22 + textWidth, y + 20, isSelected ? 0x60505050 : (isHoovered ? 0x60202020 : 0x60000000));
		fontRenderer.drawStringWithShadow(shopItem.getName(), x + 24, y + 6, canAfford ? 0xFFD060 : 0xD0D0D0);
		fontRenderer.drawStringWithShadow(""+shopItem.getCost(), x + 20 + textWidth - fontRenderer.getStringWidth(""+shopItem.getCost()), y + 6, canAfford ? 0x7EFF00 : 0xB7113C);
		RenderHelper.enableGUIStandardItemLighting();
		glColor4f(light, light, light, 1);
        glEnable(GL12.GL_RESCALE_NORMAL);
		itemRenderer.field_77024_a = false;
		itemRenderer.renderItemIntoGUI(fontRenderer, mc.renderEngine, shopItem.getItem(), x+3, y+3);
		itemRenderer.renderItemOverlayIntoGUI(fontRenderer, mc.renderEngine, shopItem.getItem(), x+3, y+3);
	}
	
	public void drawFilters() {
		int i = 0;
		for(ItemType type : ItemType.values()) {
			drawFilter(frameISx + i * 24, frameISy + FRAME_IH + 4, type);
			i ++;
		}
	}
	
	public void drawFilter(int x, int y, ItemType type) {
		boolean isSelected = type == selectedType;
		glDisable(GL_LIGHTING);
		boolean canAfford = isSelected;
		float light = canAfford ? 1 : .25F;
		glColor4f(light, light, light, 1);
		mc.renderEngine.bindTexture(mc.renderEngine.getTexture("/achievement/bg.png"));
		
		if(isSelected) {
			drawTexturedModalRect(x - 2, y - 2, 26, 202, 26, 26);
		} else {
			drawTexturedModalRect(x, y, 2, 204, 22, 22);
		}
		
		RenderHelper.enableGUIStandardItemLighting();
		glColor4f(light, light, light, 1);
		itemRenderer.field_77024_a = false;
		itemRenderer.renderItemIntoGUI(fontRenderer, mc.renderEngine, type.getIcon(), x+3, y+3);
	}
	
	private ShopItem getShopItemAt(int x, int y) { 
		if(x < frameISx + 4 || x > frameISx + 104 + 104 || y < frameISy + 4 || y > frameISy + FRAME_IH) {
			return null;
		}
		int index = (y - (frameISy + 4)) / 24 * 2 + ((x - (frameISx + 4)) / 104) + scroll * 2;
		if(index < 0 || index >= displayedItems.size()) {
			return null;
		}
		return displayedItems.get(index);
	}
	
	private ItemType getItemTypeAt(int x, int y) { 
		if(x < frameISx || x > frameISx + 24 * ItemType.values().length || y < frameISy + FRAME_IH + 4 || y > frameISy + FRAME_IH + 4 + 24) {
			return null;
		}
		int index = (x - frameISx + 2) / 24;
		if(index < 0 || index >= ItemType.values().length) {
			return null;
		}
		return ItemType.values()[index];
	}
	
	private void updateButtonsState() {
		buttonUp.enabled = scroll > 0;
		buttonDown.enabled = scroll + 6 < displayedItems.size() / 2 + 1;
	}
	
	private final int FRAME_W = 256;
	private final int FRAME_H = 202;
	private final int FRAME_IW = 224;
	private final int FRAME_IH = 153;
	private int frameSx, frameSy;
	private int frameISx, frameISy;
	
	private int scroll;
	
	private GuiButton buttonBuy;
	private GuiButton buttonDone;
	private GuiButton buttonUp;
	private GuiButton buttonDown;
	private ShopItem selectedItem;
	private ItemType selectedType;
	
	private List<ShopItem> displayedItems;
	
	private static RenderItem itemRenderer = new RenderItem();
}
