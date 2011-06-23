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
  import scala.collection.mutable.HashMap

  val SIZE = 16
  val path = "src/main/resources/"

  private val transparent = List(//XXX water
    0,    //    AIR
    6,    //    SAPLING
    26,   //    BED
    27,   //    POWERED_RAIL
    28,   //    DETECTOR_RAIL
    30,   //    WEB
    31,   //    TALL_GRASS
    32,   //    DEAD_SHRUB
    37,   //    YELLOW_FLOWER
    38,   //    RED_ROS
    39,   //    BROWN_MUSHROOM
    40,   //    RED_MUSHROOM
    44,   //    SLAB
    50,   //    TORCH
    51,   //    FIRE
    53,   //    WOODEN_STAIRS
    55,   //    REDSTONE_WIRE
    59,   //    CROPS
    63,   //    SIGNPOST
    64,   //    WOODEN_DOOR
    65,   //    LADDER
    66,   //    MINECART_TRACKS
    67,   //    COBBLESTONE_STAIRS
    68,   //    WALL_SIGN
    69,   //    LEVER
    70,   //    STONE_PRESSURE_PLATE
    71,   //    IRON_DOOR
    72,   //    WOODEN_PRESSURE_PLATE
    75,   //    REDSTONE_TORCH_OFF
    76,   //    REDSTONE_TORCH_ON
    77,   //    STONE_BUTTON
    85,   //    FENCE
    92,   //    CAKE
    93,   //    REDSTONE_REPEATER_OFF
    94,   //    REDSTONE_REPEATER_ON
    96,   //    TRAPDOOR

    20,   //    GLASS
    8,    //    WATER
    52,   //    SPAWNER
    83)   //    SUGZRCANE

  private val textures = Map(
    1  -> tool.loadImage(path+"stone"+".png"),
    2  -> tool.loadImage(path+"grass"+".png"),
    3  -> tool.loadImage(path+"dirt"+".png"),
    4  -> tool.loadImage(path+"cobblestone"+".png"),
    5  -> tool.loadImage(path+"plank"+".png"),
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
    35 -> tool.loadImage(path+"wool"+".png"),
    41 -> tool.loadImage(path+"gold_block"+".png"),
    42 -> tool.loadImage(path+"iron_block"+".png"),
    43 -> tool.loadImage(path+"double_slab"+".png"),
    45 -> tool.loadImage(path+"brick"+".png"),
    46 -> tool.loadImage(path+"tnt"+".png"),
    47 -> tool.loadImage(path+"bookshelf"+".png"),
    48 -> tool.loadImage(path+"mossy_cobblestone"+".png"),
    49 -> tool.loadImage(path+"obsidian"+".png"),
    52 -> tool.loadImage(path+"spawner"+".png"),
    54 -> tool.loadImage(path+"chest"+".png"),
    56 -> tool.loadImage(path+"diamond_ore"+".png"),
    57 -> tool.loadImage(path+"diamond_block"+".png"),
    58 -> tool.loadImage(path+"bench"+".png"),
    60 -> tool.loadImage(path+"farmland"+".png"),
    61 -> tool.loadImage(path+"furnace"+".png"),
    62 -> tool.loadImage(path+"burning_furnace"+".png"),
    73 -> tool.loadImage(path+"redstone_ore"+".png"),
    74 -> tool.loadImage(path+"redstone_ore"+".png"),
    78 -> tool.loadImage(path+"snow"+".png"),
    79 -> tool.loadImage(path+"ice"+".png"),
    80 -> tool.loadImage(path+"snow"+".png"),
    81 -> tool.loadImage(path+"cactus"+".png"),
    82 -> tool.loadImage(path+"clay"+".png"),
    83 -> tool.loadImage(path+"sugarcane"+".png"),
    84 -> tool.loadImage(path+"jukebox"+".png"),
    86 -> tool.loadImage(path+"pumpkin"+".png"),
    87 -> tool.loadImage(path+"netherrack"+".png"),
    88 -> tool.loadImage(path+"soul_sand"+".png"),
    89 -> tool.loadImage(path+"glowstone"+".png"),
    90 -> tool.loadImage(path+"portal"+".png"),
    91 -> tool.loadImage(path+"jack_o_lantern"+".png"))

  def isTransparent(id:Int) =
    transparent exists(_==id)

  def draw(id:Int, parent: PApplet) =
    textures.get(id) match{
      case None => () //println("Texture block "+id+" not found")
      case Some(tex) => TexturedCube.draw(tex, parent)
    }
}
