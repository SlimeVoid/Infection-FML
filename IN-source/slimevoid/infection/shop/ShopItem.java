package slimevoid.infection.shop;

import net.minecraft.item.Item;
import net.minecraft.block.Block;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;

public enum ShopItem {
	SWORD_WOOD(			new ItemStack(Item.swordWood)		, "Wood Sword"		, 2),
	SWORD_IRON(			new ItemStack(Item.swordSteel)		, "Iron Sword"		, 8),
	SWORD_DIAMOND(		new ItemStack(Item.swordDiamond)	, "Diam Sword"		, 15),
	BOW(				new ItemStack(Item.bow)				, "Bow"				, 10),
	ARROW(				new ItemStack(Item.arrow, 16)		, "Arrows"			, 4),
	
	BOOTS_LEATHER(		new ItemStack(Item.bootsLeather)	, "Boots"			, 2),
	LEGS_LEATHER(		new ItemStack(Item.legsLeather)		, "Pants"			, 3),
	CHEST_LEATHER(		new ItemStack(Item.plateLeather)	, "Shirt"			, 6),
	HELMET_LEATHER(		new ItemStack(Item.helmetLeather)	, "Hat"				, 4),
	BOOTS_IRON(			new ItemStack(Item.bootsSteel)		, "Iron Boots"		, 6),
	LEGS_IRON(			new ItemStack(Item.legsSteel)		, "Iron Legs"		, 9),
	CHEST_IRON(			new ItemStack(Item.plateSteel)		, "Iron Chest"		, 18),
	HELMET_IRON(		new ItemStack(Item.helmetSteel)		, "Iron Helmet"		, 12),
	BOOTS_DIAMOND(		new ItemStack(Item.bootsDiamond)	, "Diam Boots"		, 18),
	LEGS_DIAMOND(		new ItemStack(Item.legsDiamond)		, "Diam Legs"		, 27),
	CHEST_DIAMOND(		new ItemStack(Item.plateDiamond)	, "Diam Chest"		, 54),
	HELMET_DIAMOND(		new ItemStack(Item.helmetDiamond)	, "Diam Helmet"		, 36),
	
	PICKAXE(			new ItemStack(Item.pickaxeSteel)	, "Pickaxe"			, 5),
	AXE(				new ItemStack(Item.axeSteel)		, "Axe"				, 5),
	SHOVEL(				new ItemStack(Item.shovelSteel)		, "Shovel"			, 5),
	MAP(				new ItemStack(Item.map)				, "Map"				, 25),
	COMPASS(			new ItemStack(Item.compass)			, "Compass"			, 15),
	LIGHTER(			new ItemStack(Item.flintAndSteel)	, "Lighter"			, 40),
	ROD(				new ItemStack(Item.fishingRod)		, "Fishing rod"		, 10),
	
	DIRT(				new ItemStack(Block.dirt, 8)		, "Dirt"			, 1),
	COBBLE(				new ItemStack(Block.cobblestone, 8)	, "Cobblestone"		, 1),
	STONE_BRICKS(		new ItemStack(Block.stoneBrick, 8)	, "Stone Bricks"	, 2),
	BRICKS(				new ItemStack(Block.brick, 8)		, "Bricks"			, 3),
	SANDSTONE(			new ItemStack(Block.sandStone, 8)	, "Sandstone"		, 2),
	PLANKS0(			new ItemStack(Block.planks, 8)		, "Planks"			, 1),
	PLANKS1(			new ItemStack(Block.planks, 8, 1)	, "Planks"			, 1),
	PLANKS2(			new ItemStack(Block.planks, 8, 2)	, "Planks"			, 1),
	PLANKS3(			new ItemStack(Block.planks, 8, 3)	, "Planks"			, 1),
	LOG0(				new ItemStack(Block.wood, 8)		, "Log"				, 2),
	LOG1(				new ItemStack(Block.wood, 8, 1)		, "Log"				, 2),
	LOG2(				new ItemStack(Block.wood, 8, 2)		, "Log"				, 2),
	LOG3(				new ItemStack(Block.wood, 8, 3)		, "Log"				, 2),
	FENCE(				new ItemStack(Block.fence, 8)		, "Fence"			, 2),
	FENCE_GATE(			new ItemStack(Block.fenceGate)		, "Fence Gate"		, 1),
	SLAB0(				new ItemStack(Block.stoneSingleSlab, 8)	, "Stone Slab"	, 1),
	SLAB1(				new ItemStack(Block.stoneSingleSlab, 8, 1), "Sand Slab"	, 1),
	SLAB2(				new ItemStack(Block.woodSingleSlab, 8), "Oak Slab"		, 1),
	SLAB3(				new ItemStack(Block.woodSingleSlab, 8, 1), "Spruce Slab", 1),
	SLAB4(				new ItemStack(Block.woodSingleSlab, 8, 2), "Birch Slab",  1),
	SLAB5(				new ItemStack(Block.woodSingleSlab, 8, 3), "Jungle Slab", 1),
	STAIRS0(			new ItemStack(Block.stairCompactPlanks, 4), "Wood Stairs"	, 2),
	STAIRS1(			new ItemStack(Block.stairsStoneBrickSmooth, 4), "Stone Stairs", 2),
	DOOR_WOOD(			new ItemStack(Item.doorWood)			, "Wood Door"	, 2),
	DOOR_IRON(			new ItemStack(Item.doorSteel)			, "Iron Door"	, 2),
	TORCHES(			new ItemStack(Block.torchWood, 16)		, "Torches"		, 5),
	CHEST(				new ItemStack(Block.chest)				, "Chest"		, 10),	
	FURNACE(			new ItemStack(Block.stoneOvenIdle)		, "Furnace"		, 20),
	ENCHANT_TABLE(		new ItemStack(Block.enchantmentTable)	, "Enchant Table", 35),
	
	BOAT(				new ItemStack(Item.boat)			, "Boat"			, 6),
	COAL(				new ItemStack(Item.coal, 4)			, "Coal"			, 10),
	;
	
	private ShopItem(ItemStack item, String name, int cost) {
		this.item = item;
		this.name = name;
		this.cost = cost;
		Item itemType = item.getItem();
		if(itemType instanceof ItemSword || itemType instanceof ItemBow || itemType == Item.arrow) {
			this.type = ItemType.WEAPONS;
		} else if(itemType instanceof ItemArmor) {
			this.type = ItemType.ARMOR;
		} else if(itemType instanceof ItemTool || itemType instanceof ItemMap || itemType == Item.compass || itemType == Item.flintAndSteel || itemType == Item.fishingRod) {
			this.type = ItemType.TOOLS;
		} else if(itemType instanceof ItemBlock || itemType instanceof ItemDoor) {
			this.type = ItemType.CONSTRUCTION;
		} else {
			this.type = ItemType.OTHERS;
		}
	}
	
	public ItemStack getItem() {
		return item;
	}
	
	public String getName() {
		return name;
	}
	
	public int getCost() {
		return cost;
	}
	
	public ItemType getType() {
		return type;
	}
	
	private final ItemStack item;
	private final String name;
	private final int cost;
	private final ItemType type;
	
	public enum ItemType {
		WEAPONS(new ItemStack		(Item.swordSteel)),
		ARMOR(new ItemStack			(Item.helmetGold)),
		TOOLS(new ItemStack			(Item.pickaxeSteel)),
		CONSTRUCTION(new ItemStack	(Block.planks)),
		OTHERS(new ItemStack		(Item.redstone));
		
		private ItemType(ItemStack icon) {
			this.icon = icon;
		}
		
		public ItemStack getIcon() {
			return icon;
		}
		
		private final ItemStack icon;
	}
}
