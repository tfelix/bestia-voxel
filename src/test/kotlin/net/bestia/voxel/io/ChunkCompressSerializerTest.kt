package net.bestia.voxel.io

import net.bestia.voxel.Chunk
import org.junit.jupiter.api.Test

internal class ChunkCompressSerializerTest {

  private val writer = ChunkCompressWriter()
  private val reader = ChunkCompressReader()

  @Test
  fun `serializing an chunk of empty voxel works`() {
    val emptyChunk = Chunk.makeEmpy()
    val data = writer.write(emptyChunk)

    // FIXME
    //Assert.fail()

    reader.read(data)
  }
}