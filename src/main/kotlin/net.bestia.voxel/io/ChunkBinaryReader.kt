package net.bestia.voxel.io

import net.bestia.voxel.Chunk
import net.bestia.voxel.DEFAULT_CHUNK_SIZE
import net.bestia.voxel.Vector3
import net.bestia.voxel.Voxel
import java.nio.ByteBuffer
import kotlin.experimental.and
import sun.security.krb5.Confounder.bytes



class ChunkBinaryReader(
  private val chunkSize: Int = DEFAULT_CHUNK_SIZE
) : ChunkReader {

  override fun read(data: ByteArray): Chunk {
    val buffer = ByteBuffer.wrap(data)
    val totalCount = chunkSize * chunkSize * chunkSize
    val chunk = Chunk.makeEmpy(chunkSize)
    var pos = 0

    while (pos < totalCount) {
      val b1 = buffer.get().toUByte()
      val b2 = buffer.get().toUByte()

      // val t = ((b1 and 0xff.toUByte()).toInt() shl 8)or (b2 and 0xff.toUByte())
      val r = (b1 and 0xff.toUByte()).toInt() shl 8 or (b2 and 0xff.toUByte()).toInt()
      println(r)

      val material = (b1.toInt() shl 4) or (b2.toInt() shr 4)
      val occupancy = (b2 and OCCUPANCY_MASK).toByte()
      val hasCount = (b1.toInt() shr 7) == 1

      if (hasCount) {
        val count = buffer.get().toUByte().toInt()

        for (i in 0 until count) {
          val posVec = posToVec3(pos)
          chunk.setVoxel(posVec, Voxel.of(material, occupancy))
          pos++
        }

      } else {
        val posVec = posToVec3(pos)
        chunk.setVoxel(posVec, Voxel.of(material, occupancy))
        pos++
      }
    }

    return chunk
  }

  private fun posToVec3(pos: Int): Vector3 {
    val z = pos % chunkSize
    val y = pos / chunkSize
    val x = pos / (chunkSize * chunkSize)

    return Vector3(x, y, z)
  }

  companion object {
    private val OCCUPANCY_MASK = 0b00001111.toUByte()
  }
}