package me.nathanfallet.ensilan.deacoudre.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

public class DACGenerator extends ChunkGenerator {

	@Override
	public List<BlockPopulator> getDefaultPopulators(World world) {
		return Arrays.asList();
	}

	@Override
	public boolean canSpawn(World world, int x, int z) {
		return true;
	}

	public int xyzToByte(int x, int y, int z) {
		return (x * 16 + z) * 128 + y;
	}

	@Override
	public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome) {
		ChunkData chunk = createChunkData(world);
		if (chunkZ == 0 && chunkX >= 0 && chunkX % 2 == 0) {
            for (int x = 2; x < 14; x++) {
                for (int z = 5; z < 16; z++) {
                    chunk.setBlock(x, 0, z, Material.STONE_BRICKS);
                    if (x > 2 && x < 13 && z > 5 && z < 15) {
                        chunk.setBlock(x, 1, z, Material.WATER);
                    } else {
                        chunk.setBlock(x, 1, z, Material.STONE_BRICKS);
                    }
                }
            }
            for (int x = 3; x < 13; x++) {
                for (int y = 2; y < 41; y++) {
                    chunk.setBlock(x, y, 5, Material.STONE_BRICKS);
                    chunk.setBlock(x, y, 15, Material.STONE_BRICKS);
                }
                for (int y = 41; y < 45; y++) {
                    chunk.setBlock(x, y, 0, Material.STONE_BRICKS);
                    chunk.setBlock(x, y, 15, Material.STONE_BRICKS);
                }
                for (int z = 1; z < 6; z++) {
                    chunk.setBlock(x, 41, z, Material.STONE_BRICKS);
                }
            }
            for (int z = 0; z < 16; z++) {
                for (int y = 2; y < 45; y++) {
                    chunk.setBlock(2, y, z, Material.STONE_BRICKS);
                    chunk.setBlock(13, y, z, Material.STONE_BRICKS);
                }
            }
		}
		return chunk;
	}

}
