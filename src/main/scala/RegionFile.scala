
/*
 *  ToDo: handle errors
 */

object RegionFile {
  import com.mojang.RegionFileCache
  import java.io.File
  import java.io.DataInputStream

  def apply(path: String, x: Int, z: Int): java.io.DataInputStream  =
    RegionFileCache.getRegionFile(new File(path), x, z).getChunkDataInputStream(x & 31 , x & 31 )
  }


