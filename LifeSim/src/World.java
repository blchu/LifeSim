import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class World {
	
	public static final Vector BASE = new Vector(Math.PI);
	public static final int BUFFER = 10, HEIGHT = Simulation.HEIGHT - 4*BUFFER, WIDTH = HEIGHT, GRID_CELL_SIZE = 10;
	private final int RESPAWN = 3*Simulation.FPS;
	
	private Set<Life>[][] plantGrid;
	private Set<Life>[][] herbivoreGrid;
	private Set<Life>[][] carnivoreGrid;
	
	private Queue<Carnivore> carnivoreToAdd;
	private Queue<Carnivore> carnivoreToRemove;
	private Queue<Herbivore> herbivoreToAdd;
	private Queue<Herbivore> herbivoreToRemove;
	private Queue<Plant> plantToAdd;
	private Queue<Plant> plantToRemove;
	
	private int numPlants, numHerbivores, numCarnivores;
	private double pRespawnCounter, hRespawnCounter;
	
	private double[] spawnGenes = {30, 90, 0.4, 0.1, 0.3, 0.2};
	private double[] aveGenes = {0, 0, 0, 0, 0, 0};
	
	@SuppressWarnings("unchecked")
	public World(){
		plantGrid = (HashSet<Life>[][]) new HashSet[WIDTH/GRID_CELL_SIZE][HEIGHT/GRID_CELL_SIZE];
		herbivoreGrid = (HashSet<Life>[][]) new HashSet[WIDTH/GRID_CELL_SIZE][HEIGHT/GRID_CELL_SIZE];
		carnivoreGrid = (HashSet<Life>[][]) new HashSet[WIDTH/GRID_CELL_SIZE][HEIGHT/GRID_CELL_SIZE];
		
		carnivoreToAdd = new LinkedList<Carnivore>();
		carnivoreToRemove = new LinkedList<Carnivore>();
		
		herbivoreToAdd = new LinkedList<Herbivore>();
		herbivoreToRemove = new LinkedList<Herbivore>();
		
		plantToAdd = new LinkedList<Plant>();
		plantToRemove = new LinkedList<Plant>();
		
		pRespawnCounter = RESPAWN;
		hRespawnCounter = RESPAWN;
		
		for(int i = 0; i < plantGrid.length; i++)
			for(int j = 0; j < plantGrid.length; j++)
				plantGrid[i][j] = new HashSet<Life>();
		for(int i = 0; i < herbivoreGrid.length; i++)
			for(int j = 0; j < herbivoreGrid.length; j++)
				herbivoreGrid[i][j] = new HashSet<Life>();
		for(int i = 0; i < carnivoreGrid.length; i++)
			for(int j = 0; j < carnivoreGrid.length; j++)
				carnivoreGrid[i][j] = new HashSet<Life>();
	}
	
	public void update(){
		
		long t2 = System.currentTimeMillis();
		for(Set<Life>[] arr : plantGrid){
			for(Set<Life> set : arr){
				if(set.size() > 0){
					Iterator<Life> iter = set.iterator();
					while(iter.hasNext())
						iter.next().update();
				}
			}
		}
		long t3 = System.currentTimeMillis();
		for(Set<Life>[] arr : herbivoreGrid){
			for(Set<Life> set : arr){
				if(set.size() > 0){
					Iterator<Life> iter = set.iterator();
					while(iter.hasNext())
						iter.next().update();
				}
			}
		}
		long t4 = System.currentTimeMillis();
		for(Set<Life>[] arr : carnivoreGrid){
			for(Set<Life> set : arr){
				if(set.size() > 0){
					Iterator<Life> iter = set.iterator();
					while(iter.hasNext())
						iter.next().update();
				}
			}
		}
		long t5 = System.currentTimeMillis();
		if(Simulation.SHOW_UPDATES){
			System.out.println("Plant: " + (t3 - t2));
			System.out.println("Herb: " + (t4 - t3));
			System.out.println("Carn: " + (t5 - t4));
		}
		
		
		@SuppressWarnings("unchecked")
		Set<Life>[][] tempSet = (HashSet<Life>[][]) new HashSet[WIDTH/GRID_CELL_SIZE][HEIGHT/GRID_CELL_SIZE];
		for(int i = 0; i < tempSet.length; i++)
			for(int j = 0; j < tempSet.length; j++)
				tempSet[i][j] = new HashSet<Life>();
		for(Set<Life>[] arr : herbivoreGrid){
			for(Set<Life> set : arr){
				if(set.size() > 0){
					Iterator<Life> iter = set.iterator();
					while(iter.hasNext()){
						Life l = iter.next();
						tempSet[(int)l.getLocation().getXComponent()/GRID_CELL_SIZE][(int)l.getLocation().getYComponent()/GRID_CELL_SIZE].add(l);
					}
				}
			}
		}
		
		herbivoreGrid = tempSet;
		
		@SuppressWarnings("unchecked")
		Set<Life>[][] tempSet2 = (HashSet<Life>[][]) new HashSet[WIDTH/GRID_CELL_SIZE][HEIGHT/GRID_CELL_SIZE];
		for(int i = 0; i < tempSet.length; i++)
			for(int j = 0; j < tempSet.length; j++)
				tempSet2[i][j] = new HashSet<Life>();
		for(Set<Life>[] arr : carnivoreGrid){
			for(Set<Life> set : arr){
				if(set.size() > 0){
					Iterator<Life> iter = set.iterator();
					while(iter.hasNext()){
						Life l = iter.next();
						tempSet2[(int)l.getLocation().getXComponent()/GRID_CELL_SIZE][(int)l.getLocation().getYComponent()/GRID_CELL_SIZE].add(l);
					}
				}
			}
		}
		
		carnivoreGrid = tempSet2;

		while(!plantToRemove.isEmpty()){
			Plant p = plantToRemove.remove();
			plantGrid[(int)p.getLocation().getXComponent()/GRID_CELL_SIZE][(int)p.getLocation().getYComponent()/GRID_CELL_SIZE].remove(p);
		}
		while(!plantToAdd.isEmpty()){
			Plant p = plantToAdd.remove();
			plantGrid[(int)p.getLocation().getXComponent()/GRID_CELL_SIZE][(int)p.getLocation().getYComponent()/GRID_CELL_SIZE].add(p);
		}
		while(!herbivoreToRemove.isEmpty()){
			Herbivore h = herbivoreToRemove.remove();
			herbivoreGrid[(int)h.getLocation().getXComponent()/GRID_CELL_SIZE][(int)h.getLocation().getYComponent()/GRID_CELL_SIZE].remove(h);
		}
		while(!herbivoreToAdd.isEmpty()){
			Herbivore h = herbivoreToAdd.remove();
			herbivoreGrid[(int)h.getLocation().getXComponent()/GRID_CELL_SIZE][(int)h.getLocation().getYComponent()/GRID_CELL_SIZE].add(h);
		}
		while(!carnivoreToRemove.isEmpty()){
			Carnivore c = carnivoreToRemove.remove();
			carnivoreGrid[(int)c.getLocation().getXComponent()/GRID_CELL_SIZE][(int)c.getLocation().getYComponent()/GRID_CELL_SIZE].remove(c);
		}
		while(!carnivoreToAdd.isEmpty()){
			Carnivore c = carnivoreToAdd.remove();
			carnivoreGrid[(int)c.getLocation().getXComponent()/GRID_CELL_SIZE][(int)c.getLocation().getYComponent()/GRID_CELL_SIZE].add(c);
		}
		
		long t6 = System.currentTimeMillis();		
		if(Simulation.SHOW_UPDATES) System.out.println("Adjust: " + (t6 - t5));
		if(numPlants == 0) {
			if(pRespawnCounter <= 0){
				pRespawnCounter = RESPAWN;
				addPlant(new Plant(new Vector(Algorithms.randValue(World.BUFFER, World.WIDTH - World.BUFFER),
						Algorithms.randValue(World.BUFFER, World.HEIGHT - World.BUFFER)), this));
			} else pRespawnCounter--;
		}
		if(numHerbivores == 0) {
			if(hRespawnCounter <= 0){
				hRespawnCounter = RESPAWN;
				addHerbivore(new Herbivore(spawnGenes, 10, 20*Simulation.FPS, new Vector(Algorithms.randValue(World.BUFFER, World.WIDTH - World.BUFFER),
								Algorithms.randValue(World.BUFFER, World.HEIGHT - World.BUFFER)), this, false, Color.YELLOW));
			} else hRespawnCounter--;
		}
		long t7 = System.currentTimeMillis();
		if(Simulation.SHOW_UPDATES) System.out.println("Respawn: " + (t7 - t6));
		numPlants = 0;
		numHerbivores = 0;
		numCarnivores = 0;
		for(Set<Life>[] arr : plantGrid)
			for(Set<Life> set : arr)
				numPlants += set.size();
		for(Set<Life>[] arr : herbivoreGrid)
			for(Set<Life> set : arr)
				numHerbivores += set.size();
		for(Set<Life>[] arr : carnivoreGrid)
			for(Set<Life> set : arr)
				numCarnivores += set.size();
	}
	
	public void draw(Graphics2D g){
		AffineTransform reset = g.getTransform();
		
		Set<Life>[][] plist = (Set<Life>[][])plantGrid.clone();
		for(Set<Life>[] arr : plist){
			for(Set<Life> set : arr){
				if(set.size() > 0){
					Iterator<Life> iter = set.iterator();
					while(iter.hasNext()){
						Life l = iter.next();
						int x = (int)l.getLocation().getXComponent() + BUFFER/2;
						int y = (int)l.getLocation().getYComponent() + BUFFER/2;
						g.setColor(l.getColor());
						g.fillOval((int)(x - Plant.DRAW_SIZE/2), (int)(y - Plant.DRAW_SIZE/2), (int)(Plant.DRAW_SIZE), (int)(Plant.DRAW_SIZE));	
					}
				}
			}
		}
		
		Set<Life>[][] hlist = (Set<Life>[][])herbivoreGrid.clone();
		for(Set<Life>[] arr : hlist){
			for(Set<Life> set : arr){
				if(set.size() > 0){
					Iterator<Life> iter = set.iterator();
					while(iter.hasNext()){
						Life l = iter.next();
						int hsize = (int)l.getSize();
						int x = (int)l.getLocation().getXComponent() + BUFFER/2;
						int y = (int)l.getLocation().getYComponent() + BUFFER/2;
						g.setColor(l.getColor());
						g.rotate(((Herbivore)l).getDirection().getDirection(), x, y);
						g.fillRoundRect((int)(x - hsize/2), (int)(y - hsize/2), hsize, hsize, hsize/3, hsize/3);
						g.setTransform(reset);
					}
				}
			}
		}
		
		Set<Life>[][] clist = (Set<Life>[][])carnivoreGrid.clone();
		for(Set<Life>[] arr : clist){
			for(Set<Life> set : arr){
				if(set.size() > 0){
					Iterator<Life> iter = set.iterator();
					while(iter.hasNext()){
						Life l = iter.next();
						int csize = (int)l.getSize();
						int x = (int)l.getLocation().getXComponent() + BUFFER/2;
						int y = (int)l.getLocation().getYComponent() + BUFFER/2;
						g.setColor(l.getColor());
						g.rotate(((Carnivore)l).getDirection().getDirection(), x, y);
						g.fillRoundRect((int)(x - csize), (int)(y - csize/2), 2*csize, csize, csize/4,csize/4);
						g.setTransform(reset);
					}
				}
			}
		}
	}
	
	public double[] getAverages(){
		if(numHerbivores > 0){
			double[] averages = new double[6];
			Set<Life>[][] hList = (Set<Life>[][])herbivoreGrid.clone();
			for(Set<Life>[] arr : hList){
				for(Set<Life> set : arr){
					if(set.size() > 0){
						Iterator<Life> iter = set.iterator();
							while(iter.hasNext()){
								Herbivore h = (Herbivore)iter.next();
								averages[0] += h.getGenes()[0];
								averages[1] += h.getGenes()[1];
								averages[2] += h.getGenes()[2];
								averages[3] += h.getGenes()[3];
								averages[4] += h.getGenes()[4];
								averages[5] += h.getGenes()[5];
							}
					}
				}
			}
			for(int i = 0; i < averages.length; i++)
				averages[i] = averages[i] / (double)numHerbivores;
			
			aveGenes = averages;
		}
		return aveGenes;
	}
	
	public void addCarnivore(Carnivore c){
		carnivoreToAdd.add(c);
	}
	
	public void removeCarnivore(Carnivore c){
		carnivoreToRemove.add(c);
	}
	
	public void addHerbivore(Herbivore h){
		herbivoreToAdd.add(h);
	}
	
	public void removeHerbivore(Herbivore h){
		herbivoreToRemove.add(h);
	}
	
	public void addPlant(Plant p){
		plantToAdd.add(p);
	}
	
	public void removePlant(Plant p){
		plantToRemove.add(p);
	}
	
	public Queue<Set<Life>> getLifeInBounds(Vector v, double r, char c){
		Queue<Set<Life>> q = new LinkedList<Set<Life>>();
		int minX = (int)(v.getXComponent() - r)/GRID_CELL_SIZE < 0 ? 0 : (int)(v.getXComponent() - r)/GRID_CELL_SIZE;
		int maxX = (int)(v.getXComponent() + r)/GRID_CELL_SIZE > WIDTH/GRID_CELL_SIZE ? WIDTH/GRID_CELL_SIZE : (int)(v.getXComponent() + r)/GRID_CELL_SIZE;
		for(int i = minX; i < maxX; i++){
			int minY = (int)(v.getYComponent() - r)/GRID_CELL_SIZE < 0 ? 0 : (int)(v.getYComponent() - r)/GRID_CELL_SIZE;
			int maxY = (int)(v.getYComponent() + r)/GRID_CELL_SIZE > HEIGHT/GRID_CELL_SIZE ? HEIGHT/GRID_CELL_SIZE : (int)(v.getYComponent() + r)/GRID_CELL_SIZE;
			for(int j = minY; j < maxY; j++){
				switch(c){
					case 'p':
						if(plantGrid[i][j].size() > 0) q.add(plantGrid[i][j]);
						break;
					case 'h':
						if(herbivoreGrid[i][j].size() > 0) q.add(herbivoreGrid[i][j]);
						break;
					case 'c':
						if(carnivoreGrid[i][j].size() > 0) q.add(carnivoreGrid[i][j]);
						break;
					default:
						break;
				}
			}
		}
		return q;
		}
	
	public void setSpawnGenes(double[] arr){ spawnGenes = arr; }
	
	public int getNumCarnivores(){ return numCarnivores; }
	public int getNumHerbivores(){ return numHerbivores; }
	public int getNumPlants(){ return numPlants; }

}
