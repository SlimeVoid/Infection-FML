package slimevoid.infection.fx;

import static java.lang.Math.pow;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;

public class EntityInfectedArrowFX extends EntityFX {
    private float portalParticleScale;
    private double sX;
    private double sY;
    private double sZ;
    

    public EntityInfectedArrowFX(World par1World, double sX, double sY, double sZ, double mX, double mY, double mZ) {
        super(par1World, sX, sY, sZ, mX, mY, mZ);
        motionX = mX;
        motionY = mY;
        motionZ = mZ;
        this.sX = posX = sX;
        this.sY = posY = sY;
        this.sZ = posZ = sZ;

        float f = rand.nextFloat() * 0.6F + 0.4F;
        portalParticleScale = particleScale = rand.nextFloat() * 0.2F + 0.5F;
        particleRed = particleGreen = particleBlue = 1.0F * f;
        particleGreen *= 0.3F;
        particleRed *= 0.9F;
        particleMaxAge = (int)(Math.random() * 10D) + 40;
        noClip = true;
        setParticleTextureIndex((int)(Math.random() * 8D));
    }

    public void renderParticle(Tessellator par1Tessellator, float par2, float par3, float par4, float par5, float par6, float par7) {
        float f = (particleAge + par2) / particleMaxAge;
        f = 1.0F - f;
        f *= f * 2 + .2F;
        particleScale = portalParticleScale * f;
        super.renderParticle(par1Tessellator, par2, par3, par4, par5, par6, par7);
    }

    public int getBrightnessForRender(float par1) {
        int i = super.getBrightnessForRender(par1);
        float f = (float)particleAge / (float)particleMaxAge;
        f *= f;
        f *= f;
        int j = i & 0xff;
        int k = i >> 16 & 0xff;
        k += (int)(f * 15F * 16F);

        if (k > 240)
        {
            k = 240;
        }

        return j | k << 16;
    }

    /**
     * Gets how bright this entity is.
     */
    public float getBrightness(float par1) {
        float f = super.getBrightness(par1);
        float f1 = (float)particleAge / (float)particleMaxAge;
        f1 = f1 * f1 * f1 * f1;
        return f * (1.0F - f1) + f1;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate() {
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        float f = (float)particleAge / (float)particleMaxAge;
        f = (float) (1 - (pow(1 - f, 2)));
        posX = sX + motionX * f;
        posY = sY + motionY * f;
        posZ = sZ + motionZ * f;

        if (particleAge++ >= particleMaxAge)
        {
            setDead();
        }
    }
}
