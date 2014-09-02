import java.awt.Color;

public class Plant extends Life {
	
	private static final int MIN_SIZE = 6, MAX_SIZE = 10, FARTHEST_GROWTH = 300;
	public static final int DRAW_SIZE = 5;
	private final double REPRODUCTION_DELAY = 2*Simulation.FPS, REPRODUCTION_DELTA = 0.9, CROWDING = 0.00045*Simulation.FPS;
	private double reproductionCounter;
	
	public Plant(Vector v, World w){
		super(Algorithms.randValue(MIN_SIZE, MAX_SIZE), v, w, Color.GREEN);
		reproductionCounter = Algorithms.mutate(REPRODUCTION_DELAY, REPRODUCTION_DELTA) + Math.pow(Math.E, CROWDING*world.getNumPlants());
	}

	@Override
	protected void die() {
		world.removePlant(this);
	}

	@Override
	protected void reproduce() {
		Vector spawnDelta, spawnLoc;
		do{
			spawnDelta = new Vector(Algorithms.randValue(0, 2*Math.PI)).multiply(Algorithms.randValue(0, FARTHEST_GROWTH));
			spawnLoc = location.add(spawnDelta);
		}while(spawnLoc.getXComponent() > (World.WIDTH - World.BUFFER) || spawnLoc.getXComponent() < World.BUFFER
				|| spawnLoc.getYComponent() > (World.HEIGHT - World.BUFFER) || spawnLoc.getYComponent() < World.BUFFER);
		world.addPlant(new Plant(spawnLoc, world));
	}

	@Override
	public void update() {
		if(reproductionCounter <= 0){
			reproductionCounter = Algorithms.mutate(REPRODUCTION_DELAY, REPRODUCTION_DELTA) + Math.pow(Math.E, CROWDING*world.getNumPlants());
			reproduce();
		}
		reproductionCounter--;
	}

}
