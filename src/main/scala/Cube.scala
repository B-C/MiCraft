import scala.collection.mutable.HashMap
import processing.core._

object TexturedCube{

  def draw(tex: PImage, parent: PApplet) {
    parent.beginShape(PConstants.QUADS)
    parent.texture(tex)

    parent.vertex(-1, -1,  1, 0, 0)
    parent.vertex( 1, -1,  1, 1, 0)
    parent.vertex( 1,  1,  1, 1, 1)
    parent.vertex(-1,  1,  1, 0, 1)

    parent.vertex( 1, -1, -1, 0, 0)
    parent.vertex(-1, -1, -1, 1, 0)
    parent.vertex(-1,  1, -1, 1, 1)
    parent.vertex( 1,  1, -1, 0, 1)

    parent.vertex(-1,  1,  1, 0, 0)
    parent.vertex( 1,  1,  1, 1, 0)
    parent.vertex( 1,  1, -1, 1, 1)
    parent.vertex(-1,  1, -1, 0, 1)

    parent.vertex(-1, -1, -1, 0, 0)
    parent.vertex( 1, -1, -1, 1, 0)
    parent.vertex( 1, -1,  1, 1, 1)
    parent.vertex(-1, -1,  1, 0, 1)

    parent.vertex( 1, -1,  1, 0, 0)
    parent.vertex( 1, -1, -1, 1, 0)
    parent.vertex( 1,  1, -1, 1, 1)
    parent.vertex( 1,  1,  1, 0, 1)

    parent.vertex(-1, -1, -1, 0, 0)
    parent.vertex(-1, -1,  1, 1, 0)
    parent.vertex(-1,  1,  1, 1, 1)
    parent.vertex(-1,  1, -1, 0, 1)

    parent.endShape
  }
}

object tool extends PApplet

object Block{
  val SIZE = 16
  val path = "src/main/resources/"

  val texNotFound = tool.loadImage(path+"error"+".png")
  val missing = tool.loadImage(path+"missing"+".png")

  // val dbg = tool.loadImage(path+"stone"+".png")

  val textures = Map(
    1  -> tool.loadImage(path+"stone"+".png"),
    2  -> tool.loadImage(path+"grass"+".png"),
    3  -> tool.loadImage(path+"dirt"+".png"),
    4  -> tool.loadImage(path+"cobblestone"+".png"),
    5  -> tool.loadImage(path+"plank"+".png"),
    6  -> missing,    // SAPLING
    7  -> tool.loadImage(path+"bedrock"+".png"),
    8  -> tool.loadImage(path+"water"+".png"),
    9  -> tool.loadImage(path+"stationary_water"+".png"),
    10 -> tool.loadImage(path+"lava"+".png"),
    11 -> tool.loadImage(path+"stationary_lava"+".png"),
    12 -> tool.loadImage(path+"sand"+".png"),
    13 -> tool.loadImage(path+"gravel"+".png"),
    14 -> tool.loadImage(path+"gold_ore"+".png"),
    15 -> tool.loadImage(path+"iron_ore"+".png"),
    16 -> tool.loadImage(path+"coal_ore"+".png"),
    17 -> tool.loadImage(path+"wood"+".png"),
    18 -> tool.loadImage(path+"leaves"+".png"),
    19 -> tool.loadImage(path+"sponge"+".png"),
    20 -> tool.loadImage(path+"glass"+".png"),
    21 -> tool.loadImage(path+"lapis_lazuli_ore"+".png"),
    22 -> tool.loadImage(path+"lapis_lazuli_block"+".png"),
    23 -> tool.loadImage(path+"dispenser"+".png"),
    24 -> tool.loadImage(path+"sandstone"+".png"),
    25 -> tool.loadImage(path+"note_block"+".png"),
    26 -> missing,    // BED
    27 -> missing,    // POWERED_RAIL
    28 -> missing,    // DETECTOR_RAIL
    30 -> missing,    // WEB
    31 -> missing,    // TALL_GRASS
    32 -> missing,    // DEAD_SHRUB
    35 -> tool.loadImage(path+"wool"+".png"),
    37 -> missing,    // YELLOW_FLOWER
    38 -> missing,    // RED_ROS
    39 -> missing,    // BROWN_MUSHROOM
    40 -> missing,    // RED_MUSHROOM
    41 -> tool.loadImage(path+"gold_block"+".png"),
    42 -> tool.loadImage(path+"iron_block"+".png"),
    43 -> tool.loadImage(path+"double_slab"+".png"),
    44 -> missing,     // SLAB
    45 -> tool.loadImage(path+"brick"+".png"),
    46 -> tool.loadImage(path+"tnt"+".png"),
    47 -> tool.loadImage(path+"bookshelf"+".png"),
    48 -> tool.loadImage(path+"mossy_cobblestone"+".png"),
    49 -> tool.loadImage(path+"obsidian"+".png"),
    50 -> missing,    // TORCH
    51 -> missing,    // FIRE
    52 -> tool.loadImage(path+"spawner"+".png"),
    53 -> missing,    // WOODEN_STAIRS
    54 -> tool.loadImage(path+"chest"+".png"),
    55 -> missing,    // REDSTONE_WIRE
    56 -> tool.loadImage(path+"diamond_ore"+".png"),
    57 -> tool.loadImage(path+"diamond_block"+".png"),
    58 -> tool.loadImage(path+"bench"+".png"),
    59 -> missing,    // CROPS
    60 -> tool.loadImage(path+"farmland"+".png"),
    61 -> tool.loadImage(path+"furnace"+".png"),
    62 -> tool.loadImage(path+"burning_furnace"+".png"),
    63 -> missing,    // SIGNPOST
    64 -> missing,    // WOODEN_DOOR
    65 -> missing,    // LADDER
    66 -> missing,    // MINECART_TRACKS
    67 -> missing,    // COBBLESTONE_STAIRS
    68 -> missing,    // WALL_SIGN
    69 -> missing,    // LEVER
    70 -> missing,    // STONE_PRESSURE_PLATE
    71 -> missing,    // IRON_DOOR
    72 -> missing,    // WOODEN_PRESSURE_PLATE
    73 -> tool.loadImage(path+"redstone_ore"+".png"),
    74 -> tool.loadImage(path+"redstone_ore"+".png"),
    75 -> missing,    // REDSTONE_TORCH_OFF
    76 -> missing,    // REDSTONE_TORCH_ON
    77 -> missing,    // STONE_BUTTON
    78 -> tool.loadImage(path+"snow"+".png"),
    79 -> tool.loadImage(path+"ice"+".png"),
    80 -> tool.loadImage(path+"snow"+".png"),
    81 -> tool.loadImage(path+"cactus"+".png"),
    82 -> tool.loadImage(path+"clay"+".png"),
    83 -> tool.loadImage(path+"sugarcane"+".png"),
    84 -> tool.loadImage(path+"jukebox"+".png"),
    85 -> missing,    // FENCE
    86 -> tool.loadImage(path+"pumpkin"+".png"),
    87 -> tool.loadImage(path+"netherrack"+".png"),
    88 -> tool.loadImage(path+"soul_sand"+".png"),
    89 -> tool.loadImage(path+"glowstone"+".png"),
    90 -> tool.loadImage(path+"portal"+".png"),
    91 -> tool.loadImage(path+"jack_o_lantern"+".png"),
    92 -> missing,    // CAKE
    93 -> missing,    // REDSTONE_REPEATER_OFF
    94 -> missing,    // REDSTONE_REPEATER_ON
    96 -> missing)    // TRAPDOOR

  def draw(id:Int, parent: PApplet) =
    TexturedCube.draw(textures.getOrElse(id, texNotFound), parent)
}
