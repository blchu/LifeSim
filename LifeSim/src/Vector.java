
public class Vector {
	
	private double xComponent, yComponent; 
	private double magnitude, direction;
	
	public Vector(double x, double y){
		xComponent = x;
		yComponent = y;
		magnitude = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
		direction = Math.atan2(y, x);
	}
	
	public Vector(double theta){
		xComponent = Math.cos(theta);
		yComponent = Math.sin(theta);
		magnitude = 1;
		direction = theta;
	}
	
	public Vector normalize(){
		return new Vector(direction);
	}
	
	public Vector getInverse(){
		return new Vector(yComponent, xComponent);
	}
	
	public Vector add(Vector v){
		return new Vector(xComponent + v.getXComponent(), yComponent + v.getYComponent());
	}
	
	public Vector subtract(Vector v){
		return new Vector(xComponent - v.getXComponent(), yComponent - v.getYComponent());
	}
	
	public double dotProduct(Vector v){
		return (xComponent*v.getXComponent() + yComponent*v.getYComponent());
	}
	
	public double angleBetween(Vector v){
		return Math.acos(dotProduct(v)/(magnitude*v.getMagnitude()));
	}
	
	public Vector multiply(double s){
		return new Vector(xComponent*s, yComponent*s);
	}
	
	public void addX(double increment){
		xComponent += increment;
	}
	
	public void addY(double increment){
		yComponent += increment;
	}
	
	public void rotate(double theta){
		direction = (direction + theta) % (2*Math.PI);
		xComponent = magnitude*Math.cos(direction);
		yComponent = magnitude*Math.sin(direction);
		
	}
	
	public double getXComponent(){ return xComponent; }
	public double getYComponent(){ return yComponent; }
	public double getMagnitude(){ return magnitude; }
	public double getDirection(){ return direction; }

}
