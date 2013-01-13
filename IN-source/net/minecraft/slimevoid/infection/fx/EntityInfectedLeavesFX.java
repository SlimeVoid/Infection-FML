package net.minecraft.slimevoid.infection.fx;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;

public class EntityInfectedLeavesFX extends EntityFX
{
    private float lavaParticleScale;

    public EntityInfectedLeavesFX(World par1World, double par2, double par4, double par6)
    {
        super(par1World, par2, par4, par6, 0.0D, 0.0D, 0.0D);
        motionX *= 0.80000001192092896D;
        motionY *= 0.80000001192092896D;
        motionZ *= 0.80000001192092896D;
        motionY = rand.nextFloat() * -0.4F - 0.05F;
        particleRed = particleGreen = particleBlue = 1.0F;
        particleScale *= rand.nextFloat() * 2.0F + 0.2F;
        lavaParticleScale = particleScale;
        particleMaxAge = (int)(16D / (Math.random() * 0.80000000000000004D + 0.20000000000000001D));
        noClip = false;
        setParticleTextureIndex(96);
    }

    public int getBrightnessForRender(float par1)
    {
        return 34;
    }

    /**
     * Gets how bright this entity is.
     */
    public float getBrightness(float par1)
    {
        return 0.2F;
    }

    public void renderParticle(Tessellator par1Tessellator, float par2, float par3, float par4, float par5, float par6, float par7)
    {
        particleScale = lavaParticleScale * (1.25F);
        super.renderParticle(par1Tessellator, par2, par3, par4, par5, par6, par7);
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;

        if (particleAge++ >= particleMaxAge)
        {
            setDead();
        }

        motionY -= 0.029999999999999999D;
        moveEntity(motionX, motionY, motionZ);
        motionX *= 0.99900001287460327D;
        motionY *= 0.99900001287460327D;
        motionZ *= 0.99900001287460327D;

        if (onGround)
        {
            motionX *= 0.69999998807907104D;
            motionZ *= 0.69999998807907104D;
            worldObj.spawnParticle("smoke", posX, posY, posZ, motionX, motionY, motionZ);
        }
    }
}
