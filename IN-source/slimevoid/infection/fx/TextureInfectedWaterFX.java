package slimevoid.infection.fx;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import slimevoid.infection.core.InfectionMod;

import net.minecraft.client.renderer.texturefx.TextureFX;

public class TextureInfectedWaterFX extends TextureFX {
	protected float r[];
    protected float g[];
    protected float b[];
    protected float a[];
    protected float l[];
    protected boolean dirs[];

    public TextureInfectedWaterFX() {
        super(InfectionMod.INFECTED_WATER.blockIndexInTexture);
        r = new float[256];
        g = new float[256];
        b = new float[256];
        a = new float[256];
        l = new float[256];
        dirs = new boolean[256];
        
        try {
			BufferedImage tex = ImageIO.read(TextureInfectedWaterFX.class.getResource("/infectedwater.png"));
			for (int i = 0; i < 256; i++) {
				int color = tex.getRGB(i % 16, i / 16);
	        	r[i] = ((color & 0x000000FF) >>  0) / 255F;
	        	g[i] = ((color & 0x0000FF00) >>  8) / 255F;
	        	b[i] = ((color & 0x00FF0000) >> 16) / 255F;
	        	a[i] = ((color & 0xFF000000) >> 24) / 255F;
	        	l[i] = 0.9F + rand.nextFloat() * .2F;
	        	dirs[i] = rand.nextBoolean();
	        }
		} catch (IOException e) {
			for (int i = 0; i < 256; i++) {
	        	r[i] = rand.nextFloat();
	        	g[i] = rand.nextFloat();
	        	b[i] = rand.nextFloat();
	        	a[i] = rand.nextFloat();
	        	l[i] = 0.9F + rand.nextFloat() * .2F;
	        	dirs[i] = rand.nextBoolean();
	        }
		}
    }

    public void onTick() { 
        for (int i = 0; i < 256; i++) {
        	if(dirs[i]) {
        		l[i] += rand.nextFloat() * .02F;
        	} else {
        		l[i] -= rand.nextFloat() * .02F;
        	}
        	if(l[i] > 1.2F) {
        		dirs[i] = false;
        	}
        	if(l[i] < 0.8F) {
        		dirs[i] = true;
        	}
        	
            imageData[i * 4 + 0] = (byte)(mul(r[i], l[i]) * 255);
            imageData[i * 4 + 1] = (byte)(mul(g[i], l[i]) * 255);
            imageData[i * 4 + 2] = (byte)(mul(b[i], l[i]) * 255);
            imageData[i * 4 + 3] = (byte)(mul(a[i],   1 ) * 255);
        }
    }
    
    private float mul(float f1, float f2) {
    	float m = f1 * f2;
    	return m > 1 ? 1 : m;
    }
    
    private static Random rand = new Random();
    
}
