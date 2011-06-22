import scala.collection.mutable.HashMap
import processing.core._

class Block(id:Int){
  val color = Block.colors.getOrElse(id, Block.color(255,255,255) )

  def draw(parent: PApplet) = {
    parent.fill(color)
    parent.box(Block.SIZE)
  }
}

object tool extends PApplet

object Block{
  val SIZE = 20

  def color(r: Int, g: Int, b: Int) = tool.color(r,g,b)

  val colors = Map(
    1 -> color(109,103,95),		// STONE
    2 -> color(11,109,9), // GRASS
    3 -> color(110,55,35)		// DIRT
		// COBBLESTONE
		// PLANK
		// SAPLING
		// BEDROCK
		// WATER
		// STATIONARY_WATER
		// LAVA
		// STATIONARY_LAVA
		// SAND
		// GRAVEL
		// GOLD_ORE
		// IRON_ORE
		// COAL_ORE
		// WOOD
		// LEAVES
		// SPONGE (19, "Sponge"),
		// GLASS (20, "Glass"),
		// LAPIS_LAZULI_ORE (21, "Lapis"),
		// LAPIS_LAZULI_BLOCK (22, "Lapis"),
		// DISPENSER (23, "Dispenser"),
		// SANDSTONE (24, "Sandstone"),
		// NOTE_BLOCK (25, "Note"),
		// BED (26, "Bed"),
		// POWERED_RAIL (27, "Power Rail"),
		// DETECTOR_RAIL (28, "Detector"),
		// WEB (30, "Web"),
		// TALL_GRASS (31, "Tall Grass"),
		// DEAD_SHRUB (32, "Dead Shrub"),
		// WOOL (35, "Wool"),
		// YELLOW_FLOWER (37, "Flower"),
		// RED_ROSE (38, "Rose"),
		// BROWN_MUSHROOM (39, "Mushroom"),
		// RED_MUSHROOM (40, "Mushroom"),
		// GOLD_BLOCK (41, "Gold"),
		// IRON_BLOCK (42, "Iron"),
		// DOUBLE_SLAB (43, "Slab"),
		// SLAB (44, "Slab"),
		// BRICK (45, "Brick"),
		// TNT (46, "TNT"),
		// BOOKSHELF (47, "Bookshelf"),
		// MOSSY_COBBLESTONE (48, "Moss"),
		// OBSIDIAN (49, "Obsidian"),
		// TORCH (50, "Torch"),
		// FIRE (51, "Fire"),
		// MOB_SPAWNER (52, "Spawner"),
		// WOODEN_STAIRS (53, "Stairs"),
		// CHEST (54, "Chest"),
		// REDSTONE_WIRE (55, "Wire"),
		// DIAMOND_ORE (56, "Diamond"),
		// DIAMOND_BLOCK (57, "Diamond"),
		// WORKBENCH (58, "Bench"),
		// CROPS (59, "Crops"),
		// FARMLAND (60, "Farmland"),
		// FURNACE (61, "Furnace"),
		// BURNING_FURNACE (62, "Furnace"),
		// SIGNPOST (63, "Sign"),
		// WOODEN_DOOR (64, "Door"),
		// LADDER (65, "Ladder"),
		// MINECART_TRACKS (66, "Tracks"),
		// COBBLESTONE_STAIRS (67, "Stairs"),
		// WALL_SIGN (68, "Sign"),
		// LEVER (69, "Lever"),
		// STONE_PRESSURE_PLATE (70, "Plate"),
		// IRON_DOOR (71, "Door"),
		// WOODEN_PRESSURE_PLATE (72, "Plate"),
		// REDSTONE_ORE (73, "Redstone"),
		// GLOWING_REDSTONE_ORE (74, "Redstone"),
		// REDSTONE_TORCH_OFF (75, "Torch"),
		// REDSTONE_TORCH_ON (76, "Torch"),
		// STONE_BUTTON (77, "Button"),
		// SNOW (78, "Snow"),
		// ICE (79, "Ice"),
		// SNOW_BLOCK (80, "Snow"),
		// CACTUS (81, "Cactus"),
		// CLAY (82, "Clay"),
		// SUGARCANE (83, "Sugarcane"),
		// JUKEBOX (84, "Jukebox"),
		// FENCE (85, "Fence"),
		// PUMPKIN (86, "Pumpkin"),
		// NETHERRACK (87, "Netherrack"),
		// SOUL_SAND (88, "Soul Sand"),
		// GLOWSTONE (89, "Glowstone"),
		// PORTAL (90, "Portal"),
		// JACK_O_LANTERN (91, "Jack"),
		// CAKE (92, "Cake"),
		// REDSTONE_REPEATER_OFF (93, "Redstone Repeater (off)"),
		// REDSTONE_REPEATER_ON (94, "Redstone Repeater (on)"),
		// TRAPDOOR (96, "Trapdoor")
    )
 // color

  def apply(id:Int):Block = new Block(id)
}

