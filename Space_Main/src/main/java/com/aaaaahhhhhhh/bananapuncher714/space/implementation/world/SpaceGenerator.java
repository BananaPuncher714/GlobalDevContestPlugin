package com.aaaaahhhhhhh.bananapuncher714.space.implementation.world;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.noise.SimplexNoiseGenerator;

import com.aaaaahhhhhhh.bananapuncher714.space.core.api.Pair;
import com.aaaaahhhhhhh.bananapuncher714.space.core.api.world.VoronoiNoiseGenerator;
import com.aaaaahhhhhhh.bananapuncher714.space.core.util.Util;

public class SpaceGenerator extends ChunkGenerator {
	private static final Map< Material, Integer > MATERIALS;
	private static final Map< Material, Integer > SLABS;
	private static final Map< Material, Integer > CRATER_MATERIALS;
	private static final Map< Material, Integer > CRATER_WALL_MATERIALS;
	
	static {
		MATERIALS = new HashMap< Material, Integer >();
		MATERIALS.put( Material.ANDESITE, 4 );
		MATERIALS.put( Material.STONE, 3 );
		MATERIALS.put( Material.CYAN_TERRACOTTA, 3 );
		
		SLABS = new HashMap< Material, Integer >();
		SLABS.put( Material.ANDESITE_SLAB, 1 );
		SLABS.put( Material.STONE_SLAB, 1 );
		
		CRATER_MATERIALS = new HashMap< Material, Integer >();
		CRATER_MATERIALS.put( Material.GRASS, 14 );
		CRATER_MATERIALS.put( Material.AIR, 25 );
		CRATER_MATERIALS.put( Material.TALL_GRASS, 7 );
		CRATER_MATERIALS.put( Material.POPPY, 1 );
		CRATER_MATERIALS.put( Material.DANDELION, 1 );
		CRATER_MATERIALS.put( Material.ORANGE_TULIP, 1 );
		CRATER_MATERIALS.put( Material.RED_TULIP, 1 );
		CRATER_MATERIALS.put( Material.WHITE_TULIP, 1 );
		CRATER_MATERIALS.put( Material.PINK_TULIP, 1 );
		CRATER_MATERIALS.put( Material.BLUE_ORCHID, 1 );
		CRATER_MATERIALS.put( Material.ALLIUM, 1 );
		CRATER_MATERIALS.put( Material.LILY_OF_THE_VALLEY, 1 );
		
		CRATER_WALL_MATERIALS = new HashMap< Material, Integer >();
		CRATER_WALL_MATERIALS.put( Material.STONE, 5000 );
		CRATER_WALL_MATERIALS.put( Material.CYAN_TERRACOTTA, 2000 );
		CRATER_WALL_MATERIALS.put( Material.OBSIDIAN, 1000 );
		CRATER_WALL_MATERIALS.put( Material.BLACK_TERRACOTTA, 2000 );
		CRATER_WALL_MATERIALS.put( Material.GRAY_TERRACOTTA, 2000 );
		CRATER_WALL_MATERIALS.put( Material.ICE, 1 );
	}
	
	SimplexNoiseGenerator generator;
	SimplexNoiseGenerator generator2;
	SimplexNoiseGenerator generator3;
	
	SimplexNoiseGenerator generator4;
	SimplexNoiseGenerator generator5;
	
	SimplexNoiseGenerator generator6;
	
	VoronoiNoiseGenerator vGenerator;
	VoronoiNoiseGenerator vGenerator2;
	
	private int craterRadius = 100;
	private int craterSlopeRadius = 150;
	private int craterWallRadius = 200;
	
	private int craterRadiusCenterSquared = craterRadius * craterRadius;
	private int innerCraterRadiusTotal = craterRadius + craterSlopeRadius;
	private int innerCraterRadiusSquared = innerCraterRadiusTotal * innerCraterRadiusTotal;
	
	private int craterRadiusTotal = craterRadius + craterSlopeRadius + craterWallRadius;
	private int craterRadiusSquared = craterRadiusTotal * craterRadiusTotal;
	
	public SpaceGenerator( long seed ) {
		generator = new SimplexNoiseGenerator( seed * 8443 );
		generator2 = new SimplexNoiseGenerator( seed * 42 );
		generator3 = new SimplexNoiseGenerator( seed * 137 );
		
		generator4 = new SimplexNoiseGenerator( seed * 66432 );
		generator5 = new SimplexNoiseGenerator( seed * 101325 );
		
		generator6 = new SimplexNoiseGenerator( seed * 3331 );
		
		vGenerator = new VoronoiNoiseGenerator( seed * 8443, false );
		vGenerator2 = new VoronoiNoiseGenerator( seed * 66432, false );
	}
	
	public Pair< Double, Double > getSinkholeCoords( int x, int z ) {
		double[] sinkhole = getSinkholeLocation( x, z );
		return new Pair< Double, Double >( sinkhole[ 0 ], sinkhole[ 1 ] );
	}
	
	@Override
    public ChunkData generateChunkData( World world, Random random, int cx, int cz, BiomeGrid grid ) {
		ChunkData data = createChunkData( world );
		
		int finX = cx << 4;
		int finZ = cz << 4;

		for ( int x = 0; x < 16; x++ ) {
			int absX = finX + x;
			for ( int z = 0; z < 16; z++ ) {
				int absZ = finZ + z;
				double[] sinkhole = getSinkholeLocation( absX, absZ );
				
				double distSq = distSq( sinkhole[ 0 ], sinkhole[ 1 ], absX, absZ );

				Biome biome = Biome.SNOWY_TUNDRA;
				
				double height = getSpaceTerrainHeight( absX, absZ );
				if ( distSq > craterRadiusSquared ) {
					// Get a moon-looking surface
					
					int blockHeight = ( int ) height;
					for ( int i = 0; i < blockHeight; i++ ) {
						if ( i == 0 ) {
							data.setBlock( x, i, z, Material.BEDROCK );
						} else if ( blockHeight - i < 4 ) {
							data.setBlock( x, i, z, Util.getRandom( MATERIALS ) );
						} else {
							data.setBlock( x, i, z, Material.SPONGE );
						}
					}
					
					int roundedHeight = ( int ) ( height + .5 );
					if ( roundedHeight > blockHeight ) {
						data.setBlock( x, blockHeight, z, Util.getRandom( SLABS ) );
					}
				} else if ( distSq > innerCraterRadiusSquared ) {
					double dist = Math.sqrt( distSq );
					double distFromInnerEdge = dist - innerCraterRadiusTotal;
					double percentHeight = 1 - ( distFromInnerEdge / craterWallRadius );
					percentHeight *= Math.pow( percentHeight, 2.6 );
					double wallHeightTop = 225;
					double wallHeightTopDev = 12;
					double wallHeightBottom = 45;
					double craterWall = getCraterWallHeight( absX, absZ );
					
					double craterHeightDev = craterWall * wallHeightTopDev;
					double craterFinHeight = wallHeightBottom + percentHeight * ( wallHeightTop - wallHeightBottom ) + craterHeightDev;
					
					int deviation = random.nextInt( 5 ) + 15;
					int blockHeight = ( int ) height;
					int roundedHeight = ( int ) ( height + .5 );
					for ( int i = 0; i < Math.max( height, craterFinHeight ); i++ ) {
						if ( i == 0 ) {
							data.setBlock( x, i, z, Material.BEDROCK );
						} else if ( i > craterFinHeight ) {
							if ( i == blockHeight ) {
								if ( roundedHeight > blockHeight ) {
									data.setBlock( x, blockHeight, z, Util.getRandom( SLABS ) );
								}
							} else {
								if ( blockHeight - i < 4 ) {
									data.setBlock( x, i, z, Util.getRandom( MATERIALS ) );
								} else {
									data.setBlock( x, i, z, Material.SPONGE );
								}
							}
						} else {
							double percentDiff = generator6.noise( absX / 32.0, absZ / 32.0 ) * .07;
							
							if ( i > craterFinHeight - deviation && percentHeight + percentDiff > .69 ) {
								data.setBlock( x, i, z, Material.BLACK_CONCRETE );
							} else {
								data.setBlock( x, i, z, Util.getRandom( CRATER_WALL_MATERIALS ) );
							}
						}
					}
					biome = Biome.NETHER;
				} else {
					int[] heights = getHeight( absX, absZ, sinkhole[ 0 ], sinkhole[ 1 ], craterRadius, craterSlopeRadius );
					int newHeight = ( int ) ( heights[ 0 ] * .85 );
					
					int lower = random.nextInt( 3 );
					if ( newHeight > lower ) {
						// Start the descent, from grass to abyss
						for ( int i = 0; i < newHeight; i++ ) {
							int blockHeight = newHeight - i;
							if ( blockHeight == newHeight ) {
								data.setBlock( x, blockHeight, z, Material.GRASS_BLOCK );
								data.setBlock( x, blockHeight + 1, z, Util.getRandom( CRATER_MATERIALS ) );
							} else if ( blockHeight > newHeight - ( lower + 1 ) ) {
								data.setBlock( x, blockHeight, z, Material.DIRT );
							} else {
								double noise = generator3.noise( absX / 8.0, i / 8.0, absZ / 8.0 );
								if ( noise < .3 ) {
									data.setBlock( x, blockHeight, z, Material.STONE );
								} else {
									data.setBlock( x, blockHeight, z, Material.CYAN_TERRACOTTA );
								}
							}
						}
					}
					if ( distSq > craterRadiusCenterSquared ) {
						biome = Biome.FLOWER_FOREST;
					} else {
						biome = Biome.MUSHROOM_FIELDS;
					}
				}
				
				for ( int i = 0; i < 256; i++ ) {
					grid.setBiome( x, i, z, biome );
				}
			}
		}
		
		return data;
	}
	
	private double getVNoise( double i, double z, double frequency ) {
		// Get the original noise value and set the range between 0 and 1
		double noise = ( vGenerator.noise( i, z, frequency ) + 1 ) / 2.0;
		// Get the list of neighboring cells, their height, their distance, and x and z
		double[][] values = vGenerator.getClosestNeighbor( i, z, frequency );
		// Get the original values for the first, or the closest cell
		double origVal = values[ 0 ][ 0 ];
		double origDist = values[ 0 ][ 1 ];
		double avg = 0;
		double amount = 0;
		// Loop over the other neighboring cells
		for ( int idx = 1; idx < values.length; idx++ ) {
			// Get the distance and then find the halfway point between the origin and neighbor cell heights
			double distance = values[ idx ][ 0 ];
			double halfDist = ( distance - origVal ) / 2.0 + origVal;
			
			double twoDist = values[ idx ][ 1 ];
			
			// Get the distance from the center of the origin cell to its neighbor, and apply
			// a sqrt function so that it forms rounder edges when calculating the height
			// divDist should range between 0 and 1
			double divDist = Math.sqrt( origDist / twoDist );

			// Get the overall distance
			double dist = Math.abs( distance - origVal );
			dist = Math.sqrt( dist );
			if ( dist < .5 ) {
				// Add the value of the neighboring cell and the current cell value to the average
				avg += origVal * ( 1 - divDist ) + halfDist * divDist;
				amount++;
			}
		}
		if ( amount > 0 ) {
			// Average out the noise to a proper value between 0 and 1
			noise = avg / amount;
		}
		
		return noise;
	}
	
	private double getCraterWallHeight( int x, int z ) {
		double h1 = ( generator4.noise( x / 32.0, z / 32.0 ) + 1 ) / 2.0;
		double h2 = ( generator5.noise( x / 32.0, z / 32.0 ) + 1 ) / 2.0;
		
		return Math.max( h1 * h1 * h1, h2 * h2 * h2 );
	}
	
	private double getSpaceTerrainHeight( int x, int z ) {
		// Returns -1 to 1
		double h1 = generator.noise( x / 128.0, z / 128.0 );
		double h2 = generator2.noise( x / 72.0, z / 72.0 );
		double h3 = generator3.noise( x / 128.0, z / 128.0 );
		
		// We want the overall surface to be somewhat smooth
		// Center around 64 with a range of 10 in both directions
		double surface = Math.min( h1, h3 ) * 20 + 64;
		
		// Add in the small curves in the event that the value produced is lower
		surface = Math.min( h2 * 18 + 69, surface );
		
		return surface;
	}
	
	private double[] getSinkholeLocation( int x, int z ) {
		// Gives it in height, distance, x, and z
		double[][] neighbors = vGenerator.getClosestNeighbor( x, z, 1 / 1024.0 );
		double noise = vGenerator.noise( x, z, 1 / 1024.0 );
		
		return new double[] { neighbors[ 0 ][ 2 ], neighbors[ 0 ][ 3 ], noise };
	}
	
	private int[] getHeight( int i, int z, double centerX, double centerY, int craterRad, double craterOuterRad ) {
		// Get the distance from the center of the abyss
		double dist = dist( i, z, centerX, centerY );
		// Get the depth after applying inner and outer limits of abyss
		double shade = Math.max( 0, ( dist - craterRad ) / craterOuterRad );
		shade = Math.sqrt( shade );
		
		if ( shade > 1 ) {
			return new int[] { 255 };
		}
		
		// Inverse the shade
		double distance = 1 - shade;
		
		// Get 3 depth samples and find the lowest
		double n1 = generator.noise( i / 64.0 , z / 64.0 );
		double n2 = generator2.noise( i / 32.0 , z / 32.0 );
		double n3 = generator3.noise( i / 64.0 , z / 64.0 );
		double noise = Math.max( n1, Math.max( n2, n3 ) );
		double noise2 = Math.min( n1, Math.min( n2, n3 ) );
		
		// Calculate the smooth depth
		double val = ( ( noise + 1 ) / 2.0 ) * distance + shade;
		int rgb = ( int ) ( 255 * val * shade );
		
		// Calculate the smooth depth
		double val2 = ( ( noise2 + 1 ) / 2.0 ) * distance + shade;
		int rgb2 = ( int ) ( 255 * val2 * shade );
		
		// Softly terrace the land
		int terraced = rgb / 35 * 35;
		int terraced2 = rgb2 / 35 * 35;
//		if ( terraced2 != terraced ) {
//			terraced = terraced + ( terraced - terraced2 ) / 2;
//		}
		if ( ( rgb ) % 35 < 29 ) {
			rgb = terraced;
		}
		
		int gapBottom = Math.max( 0, rgb - 35 );
		if ( rgb > 0 ) {
			double rgbDist = ( gapBottom / ( double ) rgb );
			double vnoise = getVNoise( i, z, 1 / 16.0 ) * ( 1 - rgbDist ) + rgbDist;
			rgb *= vnoise;
		}
		
		if ( rgb < shade * 255 * .1 ) {
			return new int[] { 0 };
		}
		
		return new int[] { rgb };
	}
	
	private double distSq( double x, double y, double x2, double y2 ) {
		return ( x - x2 ) * ( x - x2 ) + ( y - y2 ) * ( y - y2 );
	}
	
	private double dist( double x, double y, double x2, double y2 ) {
		return Math.sqrt( distSq( x, y, x2, y2 ) );
	}
}
