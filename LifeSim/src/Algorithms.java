
public class Algorithms {
	
	public static double mutate(double value, double rate){
		return value*(1 + rate*(Math.random() - 0.5));
	}
	
	public static double randValue(double min, double max){
		return min + (max - min)*Math.random();
	}
	
	public static double randDelta(double value, double maxdelta){
		return value + 2*maxdelta*Math.sin(Math.random()*2*Math.PI);
	}
	
	public static double randSpread(double value, double delta, double min, double max){
		double rNum;
		do {
			rNum = randDelta(value, delta);
		}while(rNum < min || rNum > max);
		return rNum;
	}
	
	public static double capValue(double value, double min, double max){
		if(value < min) return min;
		else if(value > max) return max;
		else return value;
	}

}
