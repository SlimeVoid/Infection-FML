package slimevoid.infection.mobs;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;

public class EntityInfectedWormRenderer extends RenderLiving {

	public EntityInfectedWormRenderer(){
		this(new ModelInfectedWorm(), 0.25f);
	}
	
	public EntityInfectedWormRenderer(ModelBase modelBase, float shadowSize) {
		super(modelBase, shadowSize);
		
	}

}
