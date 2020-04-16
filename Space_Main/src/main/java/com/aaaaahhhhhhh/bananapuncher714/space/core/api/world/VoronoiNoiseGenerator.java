/*
 * Copyright (C) 2003, 2004 Jason Bevins (original libnoise code) Copyright �
 * 2010 Thomas J. Hodge (java port of
 * libnoise)
 *
 * This file was part of libnoiseforjava.
 *
 * libnoiseforjava is a Java port of the C++ library libnoise, which may be
 * found at http://libnoise.sourceforge.net/.
 * libnoise was developed by Jason Bevins, who may be contacted at
 * jlbezigvins@gmzigail.com (for great email, take off
 * every 'zig'). Porting to Java was done by Thomas Hodge, who may be contacted
 * at libnoisezagforjava@gzagmail.com
 * (remove every 'zag').
 *
 * libnoiseforjava is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later
 * version.
 *
 * libnoiseforjava is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * libnoiseforjava. If not, see
 * <http://www.gnu.org/licenses/>.
 *
 */
package com.aaaaahhhhhhh.bananapuncher714.space.core.api.world;

import java.util.Random;

import org.bukkit.util.noise.NoiseGenerator;

/**
 * This is a Voronoi noise generator, originally from
 * https://github.com/TJHJava/libnoiseforjava It was modified to work in a
 * similar way to the bukkit noise generators, and to support octaves and 2d
 * noise, by mncat77 and jtjj222.
 *
 * To use octaves, use the VoronoiOctaveGenerator class.
 *
 * Source:
 * https://github.com/justinmichaud/bukkit-terrain-tutorials/blob/master/7.5%
 * 20Cell%20Noise/src/main/java/me/jtjj222/ biomegen/VoronoiNoiseGenerator.java
 */
public class VoronoiNoiseGenerator extends NoiseGenerator {
	
	/// Noise module that outputs Voronoi cells.
	///
	/// In mathematics, a <i>Voronoi cell</i> is a region containing all the
	/// points that are closer to a specific <i>seed point</i> than to any
	/// other seed point. These cells mesh with one another, producing
	/// polygon-like formations.
	///
	/// By default, this noise module randomly places a seed point within
	/// each unit cube. By modifying the <i>frequency</i> of the seed points,
	/// an application can change the distance between seed points. The
	/// higher the frequency, the closer together this noise module places
	/// the seed points, which reduces the size of the cells. To specify the
	/// frequency of the cells, call the setFrequency() method.
	///
	/// This noise module assigns each Voronoi cell with a random constant
	/// value from a coherent-noise function. The <i>displacement value</i>
	/// controls the range of random values to assign to each cell. The
	/// range of random values is +/- the displacement value. Call the
	/// setDisplacement() method to specify the displacement value.
	///
	/// To modify the random positions of the seed points, call the SetSeed()
	/// method.
	///
	/// This noise module can optionally add the distance from the nearest
	/// seed to the output value. To enable this feature, call the
	/// enableDistance() method. This causes the points in the Voronoi cells
	/// to increase in value the further away that point is from the nearest
	/// seed point.
	
	// for speed, we can approximate the sqrt term in the distance functions
	// private static final float SQRT_2 = 1.4142135623730950488;
	private static final float ONE_DIV_SQRT_3 = (float) (1.0D / Math.sqrt(3.0D));
	
	// You can either use the feature point height (for biomes or solid
	// pillars), or the distance to the feature point
	private final boolean useDistance;
	private final Random random;
	
	private long seed;
	
	public VoronoiNoiseGenerator(long seed, boolean useDistance) {
		this(new Random(seed), useDistance);
	}
	
	public VoronoiNoiseGenerator(Random random, boolean useDistance) {
		this.seed = random.nextLong();
		this.random = random;
		this.useDistance = useDistance;
	}
	
	public boolean isUseDistance() {
		return useDistance;
	}
	
	public long getSeed() {
		return seed;
	}
	
	public void setSeed(long seed) {
		this.seed = seed;
	}
	
	@Override
	public double noise(double x, double z, double frequency) {
		x *= frequency;
		z *= frequency;
		
		int xInt = (x > .0 ? (int) x : (int) x - 1);
		int zInt = (z > .0 ? (int) z : (int) z - 1);
		
		double minDist = 32000000;
		
		float xCandidate = 0;
		float zCandidate = 0;
		
		random.setSeed(this.seed);
		long l = random.nextLong();
		
		for (int zCur = zInt - 2; zCur <= zInt + 2; zCur++) {
			for (int xCur = xInt - 2; xCur <= xInt + 2; xCur++) {
				
				float xPos = xCur + valueNoise2D(xCur, zCur, l);
				float zPos = zCur + valueNoise2D(xCur, zCur, seed);
				double xDist = xPos - x;
				double zDist = zPos - z;
				double dist = xDist * xDist + zDist * zDist;
				
				if (dist < minDist) {
					minDist = dist;
					xCandidate = xPos;
					zCandidate = zPos;
				}
			}
		}
		
		if (useDistance) {
			double xDist = xCandidate - x;
			double zDist = zCandidate - z;
			double squared = (xDist * xDist + zDist * zDist);
			double distance = Math.sqrt(squared) * ONE_DIV_SQRT_3;
			return distance;
		}
		
		else
			return (VoronoiNoiseGenerator.valueNoise2D((int) (Math.floor(xCandidate)), (int) (Math.floor(zCandidate)),
					seed));
	}
	
	public double[][] getClosestNeighbor( double x, double z, double frequency ) {
		x *= frequency;
		z *= frequency;
		
		int xInt = (x > .0 ? (int) x : (int) x - 1);
		int zInt = (z > .0 ? (int) z : (int) z - 1);
		
		double[][] neighbors = new double[ 5 ][ 3 ];
		
		for ( double[] neighbor : neighbors ) {
			neighbor[ 0 ] = 32000000;
		}
		
		random.setSeed(this.seed);
		long l = random.nextLong();
		
		for (int zCur = zInt - 2; zCur <= zInt + 2; zCur++) {
			for (int xCur = xInt - 2; xCur <= xInt + 2; xCur++) {
				
				float xPos = xCur + valueNoise2D(xCur, zCur, l);
				float zPos = zCur + valueNoise2D(xCur, zCur, seed);
				double xDist = xPos - x;
				double zDist = zPos - z;
				double dist = xDist * xDist + zDist * zDist;
				
				double newV = dist;
				double newX = xPos;
				double newZ = zPos;
				for ( double[] neighbor : neighbors ) {
					if ( newV < neighbor[ 0 ] ) {
						double oldV = neighbor[ 0 ];
						double oldX = neighbor[ 1 ];
						double oldZ = neighbor[ 2 ];
						
						neighbor[ 0 ] = newV;
						neighbor[ 1 ] = newX;
						neighbor[ 2 ] = newZ;
						newV = oldV;
						newX = oldX;
						newZ = oldZ;
					}
				}
			}
		}
		
		double[][] values = new double[ neighbors.length ][];
		int index = 0;
		for ( double[] neighbor : neighbors ) {
			double value = VoronoiNoiseGenerator.valueNoise2D((int) (Math.floor(neighbor[ 1 ])), (int) (Math.floor(neighbor[ 2 ])), seed);
			double xDist = x - neighbor[ 1 ];
			double zDist = z - neighbor[ 2 ];
			// The first number is the height, and should range between 0 and 1
			// The second number should be the distance in the same scale as the parameters from the center of that cell
			values[ index++ ] = new double[] { ( value + 1 ) / 2.0, Math.sqrt( xDist * xDist + zDist * zDist ) / frequency, neighbor[ 1 ] / frequency, neighbor[ 2 ] / frequency };
		}
		return values;
	}
	
	public float noise(float x, float y, float z, float frequency) {
		// Inside each unit cube, there is a seed point at a random position. Go
		// through each of the nearby cubes until we find a cube with a seed
		// point
		// that is closest to the specified position.
		x *= frequency;
		y *= frequency;
		z *= frequency;
		
		int xInt = (x > .0 ? (int) x : (int) x - 1);
		int yInt = (y > .0 ? (int) y : (int) y - 1);
		int zInt = (z > .0 ? (int) z : (int) z - 1);
		
		float minDist = 32000000.0F;
		
		float xCandidate = 0;
		float yCandidate = 0;
		float zCandidate = 0;
		
		random.setSeed(seed);
		
		for (int zCur = zInt - 2; zCur <= zInt + 2; zCur++) {
			for (int yCur = yInt - 2; yCur <= yInt + 2; yCur++) {
				for (int xCur = xInt - 2; xCur <= xInt + 2; xCur++) {
					// Calculate the position and distance to the seed point
					// inside of
					// this unit cube.
					
					float xPos = xCur + valueNoise3D(xCur, yCur, zCur, seed);
					float yPos = yCur + valueNoise3D(xCur, yCur, zCur, random.nextLong());
					float zPos = zCur + valueNoise3D(xCur, yCur, zCur, random.nextLong());
					float xDist = xPos - x;
					float yDist = yPos - y;
					float zDist = zPos - z;
					float dist = xDist * xDist + yDist * yDist + zDist * zDist;
					
					if (dist < minDist) {
						// This seed point is closer to any others found so far,
						// so record
						// this seed point.
						minDist = dist;
						xCandidate = xPos;
						yCandidate = yPos;
						zCandidate = zPos;
					}
				}
			}
		}
		if (useDistance) {
			float xDist = xCandidate - x;
			float yDist = yCandidate - y;
			float zDist = zCandidate - z;
			
			return (xDist * xDist + yDist * yDist + zDist * zDist);
		}
		return VoronoiNoiseGenerator.valueNoise3D(( int ) Math.floor(xCandidate), ( int ) Math.floor(yCandidate),
				( int ) Math.floor(zCandidate), seed);
	}
	
	/**
	 * To avoid having to store the feature points, we use a hash function of
	 * the coordinates and the seed instead. Those big scary numbers are
	 * arbitrary primes.
	 */
	public static float valueNoise2D(int x, int z, long seed) {
		long n = (1619 * x + 6971 * z + 1013 * seed) & 0x7fffffff;
		n = (n >> 13) ^ n;
		return 1.0f - (((n * (n * n * 60493 + 19990303) + 1376312589) & 0x7fffffff) / 1073741824.0f);
	}
	
	public static float valueNoise3D(int x, int y, int z, long seed) {
		long n = (1619 * x + 31337 * y + 6971 * z + 1013 * seed) & 0x7fffffff;
		n = (n >> 13) ^ n;
		return 1.0f - (((n * (n * n * 60493 + 19990303) + 1376312589) & 0x7fffffff) / 1073741824.0f);
	}
}
