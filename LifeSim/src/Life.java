import java.awt.Color;

public abstract class Life {
	
	protected double size;
	protected Vector location;
	protected World world;
	protected Color color;
	
	public Life(double s, Vector v, World w, Color c){
		size = s;
		location = v;
		world = w;
		color = c;
	}
	
	public abstract void update();
	protected abstract void die();
	protected abstract void reproduce();
	
	public double getSize(){ return size; }
	public Vector getLocation(){ return location; }
	public Color getColor(){ return color; }

}
