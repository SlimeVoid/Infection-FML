package slimevoid.infection.mobs;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glEnable;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

import org.lwjgl.opengl.GL11;

public class ModelInfectedWorm extends ModelBase {
	
	public ModelInfectedWorm() {
		base = new ModelRenderer(this);
		base.addBox(0, 0, 0, 1, 4, 1);
		
		for (int i = 0; i < 5; i ++) {
			parts[i] = new ModelRenderer(this);
			parts[i].addBox(0, -1 - i, 0, 1, 1, 1);
		}
	}
	
	 /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
    	super.render(entity, f, f1, f2, f3, f4, f5);
    	EntityInfectedWorm worm = (EntityInfectedWorm) entity;
    	
    	glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		glColor4f(1f, 1f, 1f, 0.75f);
    	for (int j = 0; j < 5; j ++) {
    		GL11.glRotatef((float)worm.randPos[j].x * 180, 0, 1, 0);
			GL11.glPushMatrix();
			GL11.glTranslated(worm.randPos[j].x, 1.0 - worm.animY - (worm.randPos[j].z / 5f), worm.randPos[j].z);
			
	    	base.render(f5);
	    	for (int i = 0; i < 5; i ++) {
	    		GL11.glRotatef((float)worm.randPos[j].z * 30 + worm.animPitch * 16, 1, 0, 0);
				parts[i].render(f5);
			}
	    	
	    	GL11.glPopMatrix();
    	}
    	
    }
    
    
	public ModelRenderer base;
	public ModelRenderer[] parts = new ModelRenderer[5];
	
	

}
