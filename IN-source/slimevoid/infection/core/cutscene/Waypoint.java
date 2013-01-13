package slimevoid.infection.core.cutscene;

public class Waypoint {
	public Waypoint(double x, double y, double z, float yaw, float pitch) {
		this(x, y, z, yaw, pitch, new Fonction());
	}
	
	public Waypoint(double x, double y, double z, float yaw, float pitch, Fonction f) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
		this.f = f;
	}
	
	public Waypoint(double x, double y, double z) {
		this(x, y, z, 0, 0);
	}
	
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	public double getZ() {
		return z;
	}
	public float getYaw() {
		return yaw;
	}
	public float getPitch() {
		return pitch;
	}

	public static Waypoint calcInterCoords(Waypoint start, Waypoint end, float coef) {
		double x = start.getX() + (end.getX() - start.getX()) * start.f.process(coef, 0);
		double y = start.getY() + (end.getY() - start.getY()) * start.f.process(coef, 1);
		double z = start.getZ() + (end.getZ() - start.getZ()) * start.f.process(coef, 2);
		float yaw = start.getYaw() + (end.getYaw() - start.getYaw()) * coef;
		float pitch = start.getPitch() + (end.getPitch() - start.getPitch()) * coef;
		
		return new Waypoint(x, y, z, yaw, pitch);
	}
	
	@Override
	public int hashCode() {
		return (x+" "+y+" "+z+" "+yaw+" "+pitch).hashCode();
	}
	
	private final double x, y, z;
	private final float yaw, pitch;
	private final Fonction f;
}