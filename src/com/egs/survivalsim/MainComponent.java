package com.egs.survivalsim;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import com.egs.survivalsim.entities.Person;
import com.egs.survivalsim.entities.Zombie;

public class MainComponent extends Canvas implements Runnable{

	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 720;
	private static final int HEIGHT = 405;

	private Thread thread;
	private static Random random;
	private JFrame frame;
	private boolean running = false;
	private static String ups = "60";
	private int updates;
	private int frames;
	
	public double uStartTime;
	public double uEndTime;
	public double uTimeTaken = 0;
	
	//WORLD
	public int scale = 12;
	public int worldWidth = 32;
	public int worldHeight = 32;
	public Cell[][] cell = new Cell[worldWidth][worldHeight];

	public boolean firstTurn = true;
	
	public List<Person> personArray = new ArrayList<Person>();
	public List<Zombie> zombieArray = new ArrayList<Zombie>();
		
	public MainComponent(){
		Dimension size = new Dimension(WIDTH, HEIGHT);
		setPreferredSize(size);
		frame = new JFrame();
		random = new Random();
		//ups = JOptionPane.showInputDialog("How many updates per second? Default is 60.", "60");
		frame.setResizable(false);
		frame.add(this);
		frame.pack();
		frame.setTitle("World Gen Test");
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		start();
	}

	public static void main(String[] args){
		new MainComponent();
	}

	public void run() {
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		final double ns = 1000000000.0 / Integer.parseInt(ups);
		double delta = 0;
		updates = 0;
		frames = 0;
		while(running){
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >= 1){
				//UPDATE
				update();
				updates++;
				delta--;
			}
			//RENDER
			if(!firstTurn){
				render();
			}
			frames++;
			if(System.currentTimeMillis() - timer > 1000){
				timer += 1000;
				//System.out.println(updates +" ups " + frames + "fps");
				System.out.println("UPDATED IN: " + uTimeTaken + " ms");
				//SHOW STATS
				updates = 0;
				frames = 0;
			}
		}
		stop();
	}
	
	public synchronized void start(){
		running = true;
		thread = new Thread(this, "maincomp");
		thread.start();
	}
	
	public synchronized void stop(){
		try{
			thread.join();
		}catch(InterruptedException e){
			e.printStackTrace();
		}
	}
	
	public void render(){
		BufferStrategy bs = getBufferStrategy();
		if(bs == null){
			createBufferStrategy(3);
			return;
		}

		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		g.setFont(new Font("Arial", Font.BOLD, scale));
		g.setColor(Color.WHITE);
		g.drawString("Survivors: " + personArray.size(), 400, 30);
		g.drawString("Zombies: " + zombieArray.size(), 400, 41);
		
		
		g.setFont(new Font("Arial", Font.PLAIN, scale));
		for(int y = 0; y < worldHeight; y++){
			for(int x = 0; x < worldWidth; x++){
				g.setColor(Color.WHITE);
				if(cell[x][y].getEntityType() == -1){
					if(cell[x][y].getTile() == 0) g.drawString("~", (x * scale) + 3, (y * scale) + 12);
					if(cell[x][y].getTile() == 1) g.drawString("T", (x * scale) + 3, (y * scale) + 12);
					if(cell[x][y].getTile() == 2) g.drawString("R", (x * scale) + 3, (y * scale) + 12);
				}
				
				
				
				if(cell[x][y].getEntityType() == 0){
					g.setColor(Color.RED);
					g.drawString("Z", (x * scale) + 3, (y * scale) + 12);
				}
				if(cell[x][y].getEntityType() == 1 && cell[x][y].getEntityType() != -1){
					g.setColor(Color.GREEN);
					g.drawString("S", (x * scale) + 3, (y * scale) + 12);
				}
				
			}
		}
		
		g.setFont(new Font("Arial", Font.BOLD, scale));
		g.setColor(Color.WHITE);
		g.drawString("Last Update: " + uTimeTaken + " ms", 8, 398);
		
		
		g.dispose();
		bs.show();
	}
	
	public void update(){
		uStartTime = System.nanoTime();
		if(firstTurn){
			generateWorld();
			populateWorld();
			firstTurn = false;
		}
		
		for(int i = 0; i < cell.length; i++){
			for(int j = 0; j < cell.length; j++){
				cell[i][j].update();
			}
		}
		
//		for(int i = 0; i < personArray.size(); i++){
//			personArray.get(i).update();
//		}
		
//		personArray.get(6).update();
		
		uEndTime = System.nanoTime();
		uTimeTaken = (uEndTime - uStartTime) / 1000000;

	}	
	
	public void generateWorld(){
		System.out.println("GENERATING WORLD");
		double startTime = System.nanoTime();
		
		//Place grass
		for(int y = 0; y < worldHeight; y++){
			for(int x = 0; x < worldWidth; x++){
				cell[x][y] = new Cell();
				cell[x][y].setId(x + y);
				cell[x][y].setX(x);
				cell[x][y].setY(y);
				cell[x][y].setTile(0);
				cell[x][y].setEntityType(-1);
				cell[x][y].setEntityId(-1);
				
			}
		}
		
		//Place trees
		for(int y = 0; y < worldHeight; y++){
			for(int x = 0; x < worldWidth; x++){
				int roll = random.nextInt(10);
				if(roll == 1){
					cell[x][y].setTile(1);
				}
			}
		}
		
		//Place rocks
		int rockSize = 2;
		for(int y = 0; y < worldHeight; y++){
			for(int x = 0; x < worldWidth; x++){
				int roll = random.nextInt(20);
				if(roll == 1){
					for(int i = 0; i < rockSize; i++){
						for(int j = 0; j < rockSize; j++){
							int rockRoll = random.nextInt(5);
							if(rockRoll <= 2){
								if((x + i) < worldWidth && (y + j) < worldHeight){
									cell[x + i][y + j].setTile(2);
								}else{

								}
							}
						}
					}
				}
			}
		}

		double endTime = System.nanoTime();
		double timeTaken = (endTime - startTime) / 1000000;
		System.out.println("WORLD GENERATED IN: " + timeTaken + " ms");
	}
		
	public void populateWorld(){
		for(int y = 0; y < worldHeight; y++){
			for(int x = 0; x < worldWidth; x++){
				if(cell[x][y].getTile() == 0){
					int roll = random.nextInt(20);
					if(roll == 1){
						int typeRoll = random.nextInt(2);
						if(typeRoll == 1){
							createPerson(x, y);
						}else{
							createZombie(x, y);
						}
					}
				}
			}
		}
	}
	
	public void createPerson(int x, int y){
		personArray.add(new Person(personArray.size() + 1, x, y, this));
		cell[x][y].setEntityId(personArray.size());
		cell[x][y].setEntityType(1);
	}
	
	public void createZombie(int x, int y){
		zombieArray.add(new Zombie(zombieArray.size() + 1, x, y));
		cell[x][y].setEntityId(zombieArray.size());
		cell[x][y].setEntityType(0);
	}
}
