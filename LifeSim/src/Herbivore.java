import java.awt.Color;

class Herbivore extends MovingLife {
	
	private final int SPAWN_CARNIVORE_THRESHOLD = 100;
	
	private boolean canFindCarnivore;
	
	public Herbivore(double[] g, double s, double egy, Vector v, World w, boolean b, Color c){
		super(g, s, egy, v, w, Simulation.FPS, c);
		SIZE_THRESHOLD = 15;
		ENERGY_THRESHOLD = 100*Simulation.FPS;
		SPEED_MULTIPLIER = 1;
		REPRODUCTION_COST = 40*Simulation.FPS;
		canFindCarnivore = b;
	}

	@Override
	protected void reproduce(){
		energy -= REPRODUCTION_COST;
		if(world.getNumHerbivores() > SPAWN_CARNIVORE_THRESHOLD && Math.random() < 0.005)
			world.addCarnivore(new Carnivore(GENE_SET, size, energy, location, world));
		else for(int i = 0; i < 2; i++){
			boolean b;
			if(canFindCarnivore) b = (Math.random() / 2 < GENE_SET[3]) ? !canFindCarnivore : canFindCarnivore;
			else b = (Math.random() < GENE_SET[3]) ? !canFindCarnivore : canFindCarnivore;
			Color c = b ? Color.CYAN : Color.YELLOW;
			world.addHerbivore(new Herbivore(GENE_SET, size/Math.sqrt(2), energy/2, location, world, b, c));
		}
		die();
	}

	@Override
	protected void die(){
		world.removeHerbivore(this);
	}
	
	@Override
	public void update(){
		super.update('p');
	}
	
	public boolean getCarnivoreVisibility(){ return canFindCarnivore; }

}
