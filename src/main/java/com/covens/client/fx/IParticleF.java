package com.covens.client.fx;

import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;


interface IParticleF {

	Particle createParticle(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... args);
}
