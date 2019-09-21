package net.bestia.voxel

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ChunkTest {

  @Test
  fun `setVoxel and getVoxel return the right value`() {
    val c = Chunk.makeEmpy()
    val v = Voxel.of(1, 0.5f)
    val p = Vector3(6, 7, 9)
    c.setVoxel(p, v)

    Assertions.assertEquals(v, c.getVoxel(p))
  }

  @Test
  fun `fill() fills up the entire chunk with voxel`() {
    val c = Chunk.makeEmpy()
    val v = Voxel.of(1, 0.5f)
    c.fill(v)

    Assertions.assertEquals(v, c.getVoxel(Vector3(0, 1, 2)))
  }

  @Test
  fun `makeEmpty returns an empty chunk`() {
    val c = Chunk.makeEmpy()

    for (x in 0 until DEFAULT_CHUNK_SIZE) {
      for (y in 0 until DEFAULT_CHUNK_SIZE) {
        for (z in 0 until DEFAULT_CHUNK_SIZE) {
          Assertions.assertEquals(Voxel.EMPTY, c.getVoxel(Vector3(x, y, z)))
        }
      }
    }
  }
}