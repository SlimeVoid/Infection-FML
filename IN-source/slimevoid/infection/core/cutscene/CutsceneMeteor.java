package slimevoid.infection.core.cutscene;


public class CutsceneMeteor extends Cutscene {
	
	
	public CutsceneMeteor(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	protected void initScenes() {
		addScene(new SceneMeteor(x, y, z)); 
	}
	
	private double x, y, z;
}
