package slimevoid.infection.core.cutscene;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_LEQUAL;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_ONE;
import static org.lwjgl.opengl.GL11.GL_POINTS;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_COLOR;
import static org.lwjgl.opengl.GL11.GL_TEXTURE;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glPointSize;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glScaled;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTexCoord2d;
import static org.lwjgl.opengl.GL11.glTranslated;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex3d;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.World;

import org.lwjgl.util.vector.Vector3f;


public class SceneMeteor extends Scene3D {
	
	public SceneMeteor(double x, double y, double z) {
	}
	
	boolean metorOnGround;
	private double rX, rY, rZ;
	private double prevrX, prevrY, prevrZ;
	private List<Vector3f> motionPos;
	private List<Vector3f> trailPos;
	private final int MAX_TRAILS = 25;

	@Override
	protected void init() {
		motionPos = new ArrayList<Vector3f>();
		trailPos = new ArrayList<Vector3f>();
		for (int i = 0; i < MAX_TRAILS ; i++){
			float sx = rand.nextFloat();
			float sy = rand.nextFloat();
			float sz = rand.nextFloat();
			
			trailPos.add(new Vector3f(sx, sy, sz));
		}
		
		pX = mc.thePlayer.posX;
		pY = mc.thePlayer.posY;
		pZ = mc.thePlayer.posZ;
		
		this.pX = pX + .5;
		this.pY = pY - 1;
		this.pZ = pZ + .5;
		
		this.sX = pX - 30 * 3;
		this.sY = pY + 55 * 3;
		this.sZ = pZ - 10 * 3;
		
		super.init();
	}

	@Override 
	protected void initWaypoints() {
		float yRot = 20;
		addWaypoint(new Waypoint(sX + 20, sY - 25, sZ - 15, yRot, -40, new Fonction() {@Override
		public float process(float f, int axis) {
			return axis == 1 ? (f * f * f * .75F + f * .25F) : f;
		} }), 0L);
		addWaypoint(new Waypoint(pX, pY + 1, pZ - 5, yRot - 20, -20), 7000L);
		double yaw = toRadians(360 - yRot);
		addWaypoint(new Waypoint(pX - sin(yaw) * 5, pY + 2, pZ - cos(yaw) * 5, yRot, 10), 1000L);
		addWaypoint(new Waypoint(pX - sin(yaw) * 5, pY + 2, pZ - cos(yaw) * 5, yRot, 15), 1000L);
	}

	@Override
	protected void initEffects() {
	}

	@Override
	public void onTick(long time) {
		super.onTick(time);
		World world = mc.theWorld;
		float f = time / (float)7000;
		if(f > 1) {
			f = 1;
			if(!metorOnGround) {
				metorOnGround = true;
				for(int i = 0; i < 250; i ++) {
					String particleName = "";
					switch(rand.nextInt(2)) {
					case 0:
						particleName = "smoke";
						break;
						
					case 1:
						particleName = "explode";
						break;
					}
					float metorMotion = .8F;
					float randomMotion = 1.5F;
					double mX = (rX - prevrX) * metorMotion + (rand.nextDouble() - .5) * randomMotion;
					double mY = (rY - prevrY) * metorMotion + (rand.nextDouble() - .5) * randomMotion;
					double mZ = (rZ - prevrZ) * metorMotion + (rand.nextDouble() - .5) * randomMotion;
					world.spawnParticle(particleName, prevrX + rand.nextDouble() - .5, prevrY + rand.nextDouble() - .5, prevrZ + rand.nextDouble() - .5, mX, mY, mZ);
				}
			}
		}
		
		prevrX = rX;
		prevrY = rY;
		prevrZ = rZ;
		rX = sX + (pX - sX) * f;
		rY = sY + (pY - sY) * (f * f * f * .75 + f * .25);
		rZ = sZ + (pZ - sZ) * f;
		
		motionPos.add(new Vector3f((float)prevrX, (float)prevrY, (float)prevrZ));
		
		for(int i = 0; i < rand.nextInt(f > .9F ? 200 : 20); i ++) {
			String particleName = "";
			switch(rand.nextInt(5)) {
			case 0:
				particleName = "smoke";
				break;
				
			case 1:
				if(rand.nextFloat() < .2F) {
					particleName = "flame";
				}
				break;
				
			case 2:
				particleName = "largesmoke";
				break;
				
			case 3:
				particleName = "explode";
				break;
				
			case 4: 
				particleName = "lava";
				break;
			}
			float metorMotion = f > .95F ? 2 : 1;
			float randomMotion = .3F;
			double mX = (rX - prevrX) * metorMotion + (rand.nextDouble() - .5) * randomMotion;
			double mY = (rY - prevrY) * metorMotion + (rand.nextDouble() - .5) * randomMotion;
			double mZ = (rZ - prevrZ) * metorMotion + (rand.nextDouble() - .5) * randomMotion;
			if(f != 1) {
				world.spawnParticle(particleName, prevrX + rand.nextDouble() - .5, prevrY + rand.nextDouble() - .5, prevrZ + rand.nextDouble() - .5, mX, mY, mZ);
			}
		}
		if(f != 1) {
			for(int i = 0; i < rand.nextInt(20); i ++) {
				world.spawnParticle("explode", rX + (rand.nextDouble() - .5) * 1.5, rY + (rand.nextDouble() - .5) * 1.5, rZ + (rand.nextDouble() - .5) * 1.5, 0, 0, 0);
			}
		}
	}

	@Override
	public void render3D(float frameDelta, long time) { 
		glDisable(GL_CULL_FACE);
		glEnable(GL_TEXTURE_2D);
		mc.renderEngine.bindTexture(mc.renderEngine.getTexture("/meteoralt.png"));
		
		drawMeteor(frameDelta);
		drawHalo(frameDelta);
		glDisable(GL_TEXTURE_2D);
		drawTrails(frameDelta);
		glEnable(GL_CULL_FACE);
	}

	private void drawHalo(float frameDelta) {
	    mc.renderEngine.bindTexture(mc.renderEngine.getTexture("%blur%/misc/glint.png"));
	    glEnable(GL_BLEND);
	    glBlendFunc(GL_SRC_COLOR, GL_ONE);
	    float light = .8F;
	    glColor4f(1F * light, 0.25F * light, 0.25F * light, .4F);
	    glMatrixMode(GL_TEXTURE);
	    glPushMatrix();
	    float scale = .1F;
	    glScalef(scale, scale, scale);
	    float f9 = ((float)(System.currentTimeMillis() % 3000L) / 3000F) * 8F;
	    glTranslatef(f9, 0.0F, 0.0F);
	    glRotatef(-50F, 0.0F, 0.0F, 1.0F);
	    glMatrixMode(GL_MODELVIEW);
	    drawMeteor(frameDelta, true);
	    glMatrixMode(GL_TEXTURE);
	    glPopMatrix();
	    glPushMatrix();
	    glScalef(scale, scale, scale);
	    f9 = ((float)(System.currentTimeMillis() % 4873L) / 4873F) * 8F;
	    glTranslatef(-f9, 0.0F, 0.0F);
	    glRotatef(10F, 0.0F, 0.0F, 1.0F);
	    glMatrixMode(GL_MODELVIEW);
	    drawMeteor(frameDelta, true);
	    glMatrixMode(GL_TEXTURE);
	    glPopMatrix();
	    glMatrixMode(GL_MODELVIEW);
	    glDisable(GL_BLEND);
	    glDepthFunc(GL_LEQUAL);
	}

	public void drawMeteor(float frameDelta) {
		drawMeteor(frameDelta, false);
	}

	public void drawMeteor(float frameDelta, boolean halo) {
		double x = prevrX + (rX - prevrX) * frameDelta;
		double y = prevrY + (rY - prevrY) * frameDelta;
		double z = prevrZ + (rZ - prevrZ) * frameDelta;
		
		glPushMatrix();
			glTranslated(x, y, z);
			if(halo) {
				glScaled(1.1, 1.1, 1.1);
			}
				glBegin(GL_QUADS);
					glTexCoord2d(.75, .25); glVertex3d(-.5, -.5, -.5);
					glTexCoord2d(.50, .25); glVertex3d(+.5, -.5, -.5);
					glTexCoord2d(.50, .50); glVertex3d(+.5, +.5, -.5);
					glTexCoord2d(.75, .50); glVertex3d(-.5, +.5, -.5);
					
					glTexCoord2d(.25, .25); glVertex3d(-.5, -.5, +.5);
					glTexCoord2d(.00, .25); glVertex3d(+.5, -.5, +.5);
					glTexCoord2d(.00, .50); glVertex3d(+.5, +.5, +.5);
					glTexCoord2d(.25, .50); glVertex3d(-.5, +.5, +.5);
					
					glTexCoord2d(.75, .25); glVertex3d(-.5, -.5, -.5);
					glTexCoord2d(.50, .25); glVertex3d(+.5, -.5, -.5);
					glTexCoord2d(.50, .50); glVertex3d(+.5, -.5, +.5);
					glTexCoord2d(.75, .50); glVertex3d(-.5, -.5, +.5);
					
					glTexCoord2d(.75, .50); glVertex3d(-.5, +.5, -.5);
					glTexCoord2d(.50, .50); glVertex3d(+.5, +.5, -.5);
					glTexCoord2d(.50, .75); glVertex3d(+.5, +.5, +.5);
					glTexCoord2d(.75, .75); glVertex3d(-.5, +.5, +.5);
					
					glTexCoord2d(.75, .25); glVertex3d(-.5, -.5, -.5);
					glTexCoord2d(1.0, .25); glVertex3d(-.5, -.5, +.5);
					glTexCoord2d(1.0, .50); glVertex3d(-.5, +.5, +.5);
					glTexCoord2d(.75, .50); glVertex3d(-.5, +.5, -.5);
					
					glTexCoord2d(.50, .25); glVertex3d(+.5, -.5, -.5);
					glTexCoord2d(.25, .25); glVertex3d(+.5, -.5, +.5);
					glTexCoord2d(.25, .50); glVertex3d(+.5, +.5, +.5);
					glTexCoord2d(.50, .50); glVertex3d(+.5, +.5, -.5);
				glEnd();
//					} else {
//						mc.renderEngine.bindTexture(mc.renderEngine.getTexture("/meteor.png"));	
//						glColor4f(0.6f, 0.6f, 0.6f, 1.0f);
//						glBegin(GL_QUADS);
//							glTexCoord2d(0, 0); glVertex3d(-.5, -.5, -.5);
//							glTexCoord2d(1, 0); glVertex3d(+.5, -.5, -.5);
//							glTexCoord2d(1, 1); glVertex3d(+.5, +.5, -.5);
//							glTexCoord2d(0, 1); glVertex3d(-.5, +.5, -.5);
//							
//							glTexCoord2d(0, 0); glVertex3d(-.5, -.5, +.5);
//							glTexCoord2d(1, 0); glVertex3d(+.5, -.5, +.5);
//							glTexCoord2d(1, 1); glVertex3d(+.5, +.5, +.5);
//							glTexCoord2d(0, 1); glVertex3d(-.5, +.5, +.5);
//							
//							glTexCoord2d(0, 0); glVertex3d(-.5, -.5, -.5);
//							glTexCoord2d(1, 0); glVertex3d(+.5, -.5, -.5);
//							glTexCoord2d(1, 1); glVertex3d(+.5, -.5, +.5);
//							glTexCoord2d(0, 1); glVertex3d(-.5, -.5, +.5);
//							
//							glTexCoord2d(0, 0); glVertex3d(-.5, +.5, -.5);
//							glTexCoord2d(1, 0); glVertex3d(+.5, +.5, -.5);
//							glTexCoord2d(1, 1); glVertex3d(+.5, +.5, +.5);
//							glTexCoord2d(0, 1); glVertex3d(-.5, +.5, +.5);
//							
//							glTexCoord2d(0, 0); glVertex3d(-.5, -.5, -.5);
//							glTexCoord2d(1, 0); glVertex3d(-.5, -.5, +.5);
//							glTexCoord2d(1, 1); glVertex3d(-.5, +.5, +.5);
//							glTexCoord2d(0, 1); glVertex3d(-.5, +.5, -.5);
//							
//							glTexCoord2d(0, 0); glVertex3d(+.5, -.5, -.5);
//							glTexCoord2d(1, 0); glVertex3d(+.5, -.5, +.5);
//							glTexCoord2d(1, 1); glVertex3d(+.5, +.5, +.5);
//							glTexCoord2d(0, 1); glVertex3d(+.5, +.5, -.5);
//						glEnd(); 
//					}
		glPopMatrix();
	}

	private void drawTrails(float frameDelta) {
		glEnable(GL_BLEND);
		//glBlendFunc(GL_ONE, GL_ONE);
		
		for (int i = 0; i < MAX_TRAILS; i++) {
			if (trailPos.get(i).y > 0.5f) {
				glBlendFunc(GL_ONE, GL_ONE);
			} else {
				glBlendFunc(GL_SRC_ALPHA, GL_ONE);
			}
			glPushMatrix();
				glTranslated(trailPos.get(i).x - 0.5f, trailPos.get(i).y - 0.5f, trailPos.get(i).z - 0.5f);
				renderTrail(i, frameDelta);
			glPopMatrix();
		}
		glDisable(GL_BLEND);
	}

	public void renderTrail(int i, float frameDelta) {
		
		float maxSize = 12.0f * trailPos.get(i).x + 4.0f;
		
		double x = prevrX + (rX - prevrX) * frameDelta;
		double y = prevrY + (rY - prevrY) * frameDelta;
		double z = prevrZ + (rZ - prevrZ) * frameDelta;
		
		float oldX, oldY, oldZ;
		float newX = (float)x, newY = (float)y, newZ = (float)z;
		int maxPos = motionPos.size();
		if (maxPos > 12) {
			maxPos = 12;
		}
		for (int b = 0; b < maxPos; b ++) {
			Vector3f pos = motionPos.get(motionPos.size() - b - 1);
			oldX = newX;
			oldY = newY;
			oldZ = newZ;
			newX = pos.x;
			newY = pos.y;
			newZ = pos.z;
			if (oldX != newX && oldY != newY && oldZ != newZ && oldX != 0 && oldY != 0 && oldZ != 0){
				float factor = (float)((float)b / (float)maxPos);
				if (factor < 0.1f) {
					factor = 0.1f;
				}
				glColor4f(0.3f - .3F * factor, 0.1f - .1F * factor, 0.15f - .15F * factor, 0.8f - 0.8f * factor);
				glLineWidth(maxSize - maxSize * (float)((float)b / (float)maxPos) + 1);
				glBegin(GL_LINES);
					glVertex3d(newX, newY, newZ);
					glVertex3d(oldX, oldY, oldZ);
				glEnd();
				
				for (int tP = 0; tP < 20; tP++) {
					float size = rand.nextFloat() * 2;
					glPointSize(size != 0 ? size : 0.001F);
					glBegin(GL_POINTS);
					glColor4f(0.3f - .3F * factor, 0.3f - .3F * factor, 0.3f - .3F * factor, 0.6f - 0.6f * factor);
						glVertex3d(oldX + rand.nextFloat() - 0.5, oldY + rand.nextFloat() - 0.5, oldZ + rand.nextFloat() - 0.5);
					glEnd();
				}
				
			}
		}
	}
	
	private double pX, pY, pZ;
	private double sX, sY, sZ;
}