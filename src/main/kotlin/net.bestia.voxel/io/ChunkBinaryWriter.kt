package net.bestia.voxel.io

import net.bestia.voxel.Chunk
import net.bestia.voxel.DEFAULT_CHUNK_SIZE
import net.bestia.voxel.Vector3
import net.bestia.voxel.Voxel
import java.nio.ByteBuffer
import kotlin.experimental.and
import kotlin.experimental.or

internal class ChunkBinaryWriter(
  private val chunkSize: Int = DEFAULT_CHUNK_SIZE
) : ChunkWriter {
  private val buffer: ByteBuffer

  init {
    val chunkByteSize = chunkSize * chunkSize * chunkSize * 2
    buffer = ByteBuffer.allocate(chunkByteSize)
  }

  @ExperimentalUnsignedTypes
  override fun write(chunk: Chunk): ByteArray {
    buffer.clear()

    var count = 0.toUByte()
    var currentVoxelData = chunk.getVoxel(Vector3(0, 0, 0))

    for (x in 0 until chunkSize) {
      for (y in 0 until chunkSize) {
        for (z in 0 until chunkSize) {
          if (count == 0.toUByte()) {
            currentVoxelData = chunk.getVoxel(x, y, z)
            count++
          } else {
            val nextVoxel = chunk.getVoxel(Vector3(x, y, z))
            if (nextVoxel == currentVoxelData && count < 255.toUByte()) {
              count++
            } else {
              writeRLEData(currentVoxelData, count)
              count = 0.toUByte()
            }
          }
        }
      }
    }

    buffer.flip()
    val result = ByteArray(buffer.remaining())
    buffer.get(result)

    return result
  }

  @ExperimentalUnsignedTypes
  private fun writeRLEData(voxel: Voxel, count: UByte) {
    val hasCount = count > 1.toUByte()

    val b1 = when (hasCount) {
      true -> 0b10000000.toByte()
      false -> 0b00000000.toByte()
    } or (voxel.material shr 4).toByte()
    val b2 = (voxel.material shl 4).toByte() or (voxel.occupancy and OCCUPANCY_MASK)

    buffer.put(b1)
    buffer.put(b2)
    if (hasCount) {
      buffer.put(count.toByte())
    }
  }

  companion object {
    private const val OCCUPANCY_MASK = 0b00001111.toByte()
  }
}