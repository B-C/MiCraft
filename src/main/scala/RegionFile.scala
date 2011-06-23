
object RegionFile {
  import com.mojang.RegionFileCache
  import java.io.File
  import java.io.DataInputStream

  def apply(path: String, x: Int, z: Int): Option[java.io.DataInputStream] =
    RegionFileCache.getRegionFile(new File(path), x, z).getChunkDataInputStream(x&31, z&31) match{
      case null => None
      case stream => Some(stream)
    }
}


