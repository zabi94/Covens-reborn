package com.covens.client.fx;

import com.covens.client.ResourceLocations;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
class ParticleBat extends Particle {

	private final double decay;
	private final Vec3d acc;

	private ParticleBat(World worldIn, double posXIn, double posYIn, double posZIn, boolean endAnimation) {
		super(worldIn, posXIn, posYIn, posZIn);
		this.particleScale = (float) (1 + (this.rand.nextDouble() * 0.3));
		this.particleMaxAge = endAnimation ? 40 : 10;
		this.acc = new Vec3d(this.rand.nextGaussian(), this.rand.nextGaussian(), this.rand.nextGaussian());
		this.decay = 1d;
		this.motionX = this.rand.nextGaussian() * 0.1;
		this.motionY = this.rand.nextDouble() * (endAnimation ? 0.5 : 0.1);
		this.motionZ = this.rand.nextGaussian() * 0.1;
		this.prevParticleAngle = (float) this.rand.nextDouble();
		this.particleAngle = this.prevParticleAngle;
		final TextureAtlasSprite atlasSprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(ResourceLocations.BAT.toString());
		this.setParticleTexture(atlasSprite);
	}

	@Override
	public void onUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;

		Vec3d movement = new Vec3d(this.motionX, this.motionY, this.motionZ);
		double size = movement.length();
		movement = movement.add(this.acc).normalize().scale(size * this.decay);
		this.motionX = movement.x;
		this.motionY = movement.y;
		this.motionZ = movement.z;

		this.move(this.motionX, this.motionY, this.motionZ);

		this.setAlphaF(1f - ((float) this.particleAge / this.particleMaxAge));

		if (this.particleAge++ > this.particleMaxAge) {
			this.setExpired();
		}
	}

	@Override
	public int getFXLayer() {
		return 1;
	}

	@SideOnly(Side.CLIENT)
	static class Factory implements IParticleF {
		@Override
		public Particle createParticle(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... args) {
			return new ParticleBat(worldIn, xCoordIn, yCoordIn, zCoordIn, args.length > 0);
		}
	}
}
