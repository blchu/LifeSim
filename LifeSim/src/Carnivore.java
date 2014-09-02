import java.awt.Color;

public class Carnivore extends MovingLife {
	
	public Carnivore(double[] g, double s, double egy, Vector v, World w){
		super(g, s, egy, v, w, 2*Simulation.FPS, Color.RED);
		SPEED_MULTIPLIER = 1.05;
		ENERGY_THRESHOLD = 300*Simulation.FPS;
		SIZE_THRESHOLD = 30;
		REPRODUCTION_COST = 100*Simulation.FPS;
	}
	
	@Override
	protected void reproduce(){
		energy -= REPRODUCTION_COST;
		for(int i = 0; i < 2; i++) world.addCarnivore(new Carnivore(GENE_SET, size/Math.sqrt(2), energy/2, location, world));
		die();
	}
	
	@Override
	protected void die(){
		world.removeCarnivore(this);
	}
	
	@Override
	public void update(){
		super.update('h');
	}

}
