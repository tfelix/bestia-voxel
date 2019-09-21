package net.bestia.voxel.io

import net.bestia.voxel.Chunk

class ChunkBinaryReader : ChunkReader {
  override fun read(data: ByteArray): Chunk {
    return Chunk.makeEmpy()
  }
}