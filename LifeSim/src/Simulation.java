import java.awt.*;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.ListIterator;

import javax.swing.*;

public class Simulation extends JPanel {
	
	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 1600, HEIGHT = 900;
	public static final int FPS = 60;
	
	private static final int GRAPH_SIZE = 7, GRAPH_STEP = 1, GRAPH_SCALE = 4, MAX_GRAPH_HEIGHT = HEIGHT - 1500/GRAPH_SCALE;
	
	private LinkedList<Integer> plantGraph, herbivoreGraph, carnivoreGraph;
	private int graphUpdateCounter;
	
	private World world;
	public int speedMultiplier;
	public static final boolean SHOW_UPDATES = true;
	
	public Simulation(){
		world = new World();
		plantGraph = new LinkedList<Integer>();
		herbivoreGraph = new LinkedList<Integer>();
		carnivoreGraph = new LinkedList<Integer>();
		graphUpdateCounter = 0;
		speedMultiplier = 2;
	}
	
	public void update(){
		world.update();
		if(graphUpdateCounter <= 0){
			graphUpdateCounter = FPS/2;
			if((plantGraph.size()*GRAPH_STEP) > (WIDTH - World.WIDTH - 4*World.BUFFER)) plantGraph.remove();
			if((herbivoreGraph.size()*GRAPH_STEP) > (WIDTH - World.WIDTH - 4*World.BUFFER)) herbivoreGraph.remove();
			if((carnivoreGraph.size()*GRAPH_STEP) > (WIDTH - World.WIDTH - 4*World.BUFFER)) carnivoreGraph.remove();
			plantGraph.add(world.getNumPlants());
			herbivoreGraph.add(world.getNumHerbivores());
			carnivoreGraph.add(world.getNumCarnivores());
		}
		graphUpdateCounter--;
	}
	

	@Override
	public void paint(Graphics g){
		
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g2.setColor(Color.GRAY);
		g2.fillRect(World.BUFFER/2, World.BUFFER/2, World.WIDTH, World.HEIGHT);
		
		world.draw(g2);
		drawGraphs(g2);
		drawData(g2);
		
		g2.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke(World.BUFFER));
		g2.drawLine(World.BUFFER/2, World.BUFFER/2, World.WIDTH + World.BUFFER/2, World.BUFFER/2);
		g2.drawLine(World.BUFFER/2, World.BUFFER/2, World.BUFFER/2, World.HEIGHT + World.BUFFER/2);
		g2.drawLine(World.WIDTH + World.BUFFER/2, World.BUFFER/2, World.WIDTH + World.BUFFER/2, World.HEIGHT + World.BUFFER/2);
		g2.drawLine(World.BUFFER/2, World.HEIGHT + World.BUFFER/2, World.WIDTH + World.BUFFER/2, World.HEIGHT + World.BUFFER/2);
		
	}

	@SuppressWarnings("unchecked")
	public void drawGraphs(Graphics2D g2){
		g2.setColor(Color.LIGHT_GRAY);
		g2.fillRect(World.WIDTH + World.BUFFER, MAX_GRAPH_HEIGHT - World.BUFFER, WIDTH - World.BUFFER, HEIGHT - World.BUFFER);
		g2.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke(1));
		g2.drawLine(World.WIDTH + 2*World.BUFFER, HEIGHT - 5*World.BUFFER + GRAPH_SIZE, 
				World.WIDTH + 2*World.BUFFER + (WIDTH - World.WIDTH - 4*World.BUFFER), HEIGHT - 5*World.BUFFER + GRAPH_SIZE);
		g2.drawLine(World.WIDTH + 2*World.BUFFER, HEIGHT - 5*World.BUFFER + GRAPH_SIZE, World.WIDTH + 2*World.BUFFER, MAX_GRAPH_HEIGHT);
		
		g2.setColor(Color.GREEN);
		LinkedList<Integer> pglist = (LinkedList<Integer>)plantGraph.clone();
		ListIterator<Integer> piter = pglist.listIterator();
		while(piter.hasNext()){
			g2.fillOval(World.WIDTH + 2*World.BUFFER + piter.nextIndex()*GRAPH_STEP, HEIGHT - 5*World.BUFFER - piter.next().intValue()/GRAPH_SCALE, 
					GRAPH_SIZE, GRAPH_SIZE);
		}
		pglist.clear();
		
		g2.setColor(Color.YELLOW);
		LinkedList<Integer> hglist = (LinkedList<Integer>)herbivoreGraph.clone();
		ListIterator<Integer> hiter = hglist.listIterator();
		while(hiter.hasNext())
			g2.fillOval(World.WIDTH + 2*World.BUFFER + hiter.nextIndex()*GRAPH_STEP, HEIGHT - 5*World.BUFFER - hiter.next().intValue()/GRAPH_SCALE, 
					GRAPH_SIZE, GRAPH_SIZE);
		hglist.clear();
		
		g2.setColor(Color.RED);
		LinkedList<Integer> cglist = (LinkedList<Integer>)carnivoreGraph.clone();
		ListIterator<Integer> citer = cglist.listIterator();
		while(citer.hasNext())
			g2.fillOval(World.WIDTH + 2*World.BUFFER + citer.nextIndex()*GRAPH_STEP, HEIGHT - 5*World.BUFFER - citer.next().intValue()/GRAPH_SCALE, 
					GRAPH_SIZE, GRAPH_SIZE);
		cglist.clear();
	}
	
	public void drawData(Graphics2D g){
		g.setColor(Color.WHITE);
		g.fillRect(World.WIDTH + World.BUFFER, 0, WIDTH, MAX_GRAPH_HEIGHT - World.BUFFER);
		g.setColor(Color.BLACK);
		g.setFont(new Font("Verdana", Font.PLAIN, 2*World.BUFFER));
		g.drawString("Population of Carnivores: " + world.getNumCarnivores(), World.WIDTH + 2*World.BUFFER, 2*World.BUFFER);
		g.drawString("Population of Herbivores: " + world.getNumHerbivores(), World.WIDTH + 2*World.BUFFER, 5*World.BUFFER);
		g.drawString("Population of Plants: " + world.getNumPlants(), World.WIDTH + 2*World.BUFFER, 8*World.BUFFER);
		double[] averages = world.getAverages();
		DecimalFormat df = new DecimalFormat("#.###");
		g.drawString("Average Speed: " + df.format(averages[0]), World.WIDTH + 2*World.BUFFER, 11*World.BUFFER);
		g.drawString("Average Vision: " + df.format(averages[1]), World.WIDTH + 2*World.BUFFER, 14*World.BUFFER);
		g.drawString("Average Metabolism: " + df.format(averages[2]), World.WIDTH + 2*World.BUFFER, 17*World.BUFFER);
		g.drawString("Average Mutation Rate: " + df.format(averages[3]), World.WIDTH + 2*World.BUFFER, 20*World.BUFFER);
		g.drawString("Average Efficiency: " + df.format(averages[4]), World.WIDTH + 2*World.BUFFER, 23*World.BUFFER);
		g.drawString("Average Digestion: " + df.format(averages[5]), World.WIDTH + 2*World.BUFFER, 26*World.BUFFER);
		if(world.getNumHerbivores() > 0) world.setSpawnGenes(averages);
	}
	
	public static void main(String[] args){
		JFrame frame = new JFrame("Life Simulation");
		Simulation sim = new Simulation();
		frame.add(sim);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(WIDTH, HEIGHT);
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setFocusable(true);
		
		while(true){
			long initTime = System.currentTimeMillis();
			for(int i = 0; i < sim.speedMultiplier; i++) sim.update();
			long t1 = System.currentTimeMillis();
			if(SHOW_UPDATES) System.out.println("Update: " + (t1 - initTime));
			sim.repaint();
			if(SHOW_UPDATES) System.out.println("Draw: " + (System.currentTimeMillis() - t1));
			try{
				long delta = System.currentTimeMillis() - initTime;
				if(delta < 1000/FPS)
					Thread.sleep(1000/FPS - delta);
			} catch(InterruptedException e){
				System.out.println("Error: Thread interrupted");
			}
		}
	}
}
