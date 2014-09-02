import java.awt.Color;
import java.util.Iterator;
import java.util.Set;

public abstract class MovingLife extends Life {
	
	/* contains values for all genes
	 * 0: speed
	 * 1: vision
	 * 2: metabolism
	 * 3: mutation
	 * 4: efficiency
	 * 5: digestion
	 */
	protected final double[] GENE_SET;
	
	protected int SIZE_THRESHOLD, ENERGY_THRESHOLD, REPRODUCTION_COST;
	protected double SPEED_MULTIPLIER;
	
	protected double energy, omega, speed;
	protected int turnCounter, spawnDelay, eatDelay, locateDelay;
	protected boolean locateFood;
	
	private Vector direction;
	
	public MovingLife(double[] g, double s, double egy, Vector v, World w, int sdelay, Color c){
		super(s, v, w, c);
		GENE_SET = new double[6];
		GENE_SET[0] = Algorithms.capValue(Algorithms.mutate(g[0], g[3]), 0, 100);
		GENE_SET[1] = Algorithms.capValue(Algorithms.mutate(g[1], g[3]), 0, 200);
		GENE_SET[2] = Algorithms.capValue(Algorithms.mutate(g[2], g[3]), 0.1, 1);
		GENE_SET[3] = Algorithms.capValue(Algorithms.mutate(g[3], g[3]), 0, 0.2);
		GENE_SET[4] = Algorithms.capValue(Algorithms.mutate(g[4], g[3]), 0, 1);
		GENE_SET[5] = Algorithms.capValue(Algorithms.mutate(g[5], g[3]), 0, 1);
		energy = egy;
		direction = new Vector(Math.random()*2*Math.PI);
		omega = 0;
		speed = 0;
		locateFood = false;
		spawnDelay = sdelay;
		eatDelay = 0;
	}
	
	protected void locateClosestToEat(char c){
		double closest = GENE_SET[1];
		Life life = null;
		Vector toClosest = new Vector(0);
		Iterator<Set<Life>> iter = world.getLifeInBounds(location, GENE_SET[1], c).iterator();
		while(iter.hasNext()){
			Iterator<Life> i = iter.next().iterator();
			while(i.hasNext()){
				Life l = i.next();
				double temp = l.getLocation().subtract(location).getMagnitude();
				if(temp < closest){
					closest = temp;
					life = l;
					toClosest = l.getLocation().subtract(location);
				}
			}
		}
		if(closest < GENE_SET[1]){
			locateFood = true;
			speed = SPEED_MULTIPLIER*GENE_SET[0];
			direction = toClosest.normalize();
			if(closest < (size/2 + 1) && c != 'c') eat(life);
			if(c == 'c') direction.rotate(Math.PI);
		}
	}
	
	private void eat(Life l){
		if(l != null){
			double inc = l.getSize()*GENE_SET[4];
			l.die();
			size += Math.sqrt(inc);
			energy += 12*inc*Simulation.FPS;
			eatDelay += (int)(2*GENE_SET[5]*Simulation.FPS);
		}
	}
		
	private void wander(){
		if(turnCounter <= 0){
			turnCounter = (int)Algorithms.randValue(1, 3)*Simulation.FPS;
			direction = new Vector(Math.random()*2*Math.PI);
			omega = 2*(Math.random() - 0.5) / Simulation.FPS;
		}
		direction = new Vector(direction.getDirection() + omega);
		speed = 0.5*GENE_SET[0];
		turnCounter--;
	}
	
	public void update(char c){
		locateFood = false;
		if(spawnDelay > 0) spawnDelay--;
		if(eatDelay > 0) eatDelay--;
		if(locateDelay > 0) locateDelay--;
		if(size >= SIZE_THRESHOLD && energy >= ENERGY_THRESHOLD) reproduce();
		else {
			if(spawnDelay <= 0 && eatDelay <= 0) locateClosestToEat(c);
			if(this instanceof Herbivore && ((Herbivore)this).getCarnivoreVisibility()){
				locateClosestToEat('c');
			}
			if(!locateFood) wander();
			location = location.add(direction.multiply(2*speed/Simulation.FPS));
			
			if((location.getXComponent() + size/2) > World.WIDTH){
				location.addX(World.WIDTH - size/2 - location.getXComponent());
				turnCounter = 0;
			}
			else if((location.getXComponent() - size/2) < 0){
				location.addX(size/2 - location.getXComponent());
				turnCounter = 0;
			}
			if((location.getYComponent() + size/2) > World.HEIGHT){
				location.addY(World.HEIGHT - size/2 - location.getYComponent());
				turnCounter = 0;
			}
			else if((location.getYComponent() - size/2) < 0){
				location.addY(size/2 - location.getYComponent());
			turnCounter = 0;
			}
			energy -= (Math.pow(size, 2) + speed)*GENE_SET[2]/Simulation.FPS;
			if(size > SIZE_THRESHOLD) size -= 0.002*Math.pow(size, 2)/Simulation.FPS;
			if(energy <= 0) die();
		}
	}
	
	public double[] getGenes(){ return GENE_SET; }
	public Vector getDirection(){ return direction; }

}
