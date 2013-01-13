package slimevoid.infection.shop;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ShopManager {
	public static boolean canAfford(EntityPlayer player, ShopItem item) {
		return countCredits(player) >= item.getCost();
	}
	
	public static int countCredits(EntityPlayer player) {
		return countItem(player, Item.diamond.itemID);
	}
	
	public static int countItem(EntityPlayer player, int id) {
		int amount = 0;
		for(ItemStack stack : player.inventory.mainInventory) {
			if(stack != null && stack.itemID == id) {
				amount += stack.stackSize;
			}
		}
		return amount;
	}
}
