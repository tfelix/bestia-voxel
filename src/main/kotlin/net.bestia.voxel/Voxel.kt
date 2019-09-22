package net.bestia.voxel

import kotlin.experimental.and

typealias MaterialRef = Int

data class Voxel private constructor(
    val material: MaterialRef,
    val occupancy: Byte
) {
    // Make sure empty material is always 0
    constructor() : this(0, 0)

    val occupancyPercent: Float
        get() = occupancy / 32f

    companion object {
        private const val UPPER_BOUND_MASK = 0b00111111.toByte()
        val EMPTY = Voxel(0, 0)

        fun of(material: MaterialRef, occupancy: Float): Voxel {
            val clippedOccupancy = when {
                occupancy < 0 -> 0f
                occupancy > 1f -> 1f
                material == 0 -> 0f
                else -> occupancy
            }

            val quantized = (32 * clippedOccupancy).toByte()

            return Voxel(material, quantized and UPPER_BOUND_MASK)
        }

        fun of(material: MaterialRef, occupancy: Byte): Voxel {
            val clippedOccupancy = when {
                occupancy < 0 -> 0
                occupancy > 32 -> 32
                else -> occupancy
            }

            return Voxel(material, clippedOccupancy and UPPER_BOUND_MASK)
        }
    }
}