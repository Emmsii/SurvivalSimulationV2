package com.egs.survivalsim.entities;

import java.util.Random;

import com.egs.survivalsim.MainComponent;

public class Person extends Entity{
	
	MainComponent main;
	Random random;

	public boolean canWander = true;
	public boolean accepted = false;
	
	public Person(int id, int x, int y, MainComponent main){
		this.id = id;
		this.x = x;
		this.y = y;
		this.main = main;
		random = new Random();
	}

	public void update(){
		System.out.println("------------------------------");
		System.out.println("UPDATING: " + id);
		System.out.println("X: " + x);
		System.out.println("Y: " + y);
		System.out.println("XA: " + xa);
		System.out.println("YA: " + ya);
		System.out.println("Can Wandering: " + canWander);
		if(canWander){
			wander();
		}
		if(accepted){
			move();
		}
	}

	public void wander(){
		System.out.println("Starting to wander.");
		System.out.println("XA + YA Accepted: " + accepted);
		canWander = false;
		while(!accepted){
			System.out.println("Starting loop");
			int guessX = x + random.nextInt(4) - 2;
			int guessY = y + random.nextInt(4) - 2;
			
			System.out.println("GuessX: " + guessX);
			System.out.println("GuessY: " + guessY);
			
			if(guessX > 0 && guessX < main.worldWidth && guessY > 0 && guessX < main.worldHeight){
				if(main.cell[guessX][guessY].getTile() == 0 && main.cell[guessX][guessY].getEntityType() == -1){
					System.out.println("Guess accepted.");
					accepted = true;
					xa = guessX;
					ya = guessY;
					return;
				}else{
					System.out.println("Tile already occupied");
				}
			}else{
				System.out.println("Guess out of bounds.");
			}
			System.out.println("End of loop");
		}
		System.out.println("XA + YA Accepted: " + accepted);

	}
	
	public void move(){
		System.out.println("Accepted, now moving.");
//		if(x == xa && y == ya){
//			System.out.println("Target reached, reseting");
//			canWander = true;
//			accepted = false;
//			setXa(0);
//			setYa(0);
//			return;
//		}
		if(xa > x){
			System.out.println("XA greater than x");
			main.cell[x][y].setEntityType(-1);
			main.cell[x][y].setEntityId(-1);
			main.cell[x++][y].setEntityId(getId());
			main.cell[x++][y].setEntityType(1);
			setX(x++);
			if(x == xa && y == ya){
				System.out.println("Target reached, reseting");
				canWander = true;
				accepted = false;
				setXa(0);
				setYa(0);
				return;
			}
		}else if(xa < x){
			System.out.println("XA less than x");
			main.cell[x][y].setEntityType(-1);
			main.cell[x][y].setEntityId(-1);
			main.cell[x--][y].setEntityId(getId());
			main.cell[x--][y].setEntityType(1);
			setX(x--);
			if(x == xa && y == ya){
				System.out.println("Target reached, reseting");
				canWander = true;
				accepted = false;
				setXa(0);
				setYa(0);
				return;
			}
		}
		
		if(ya > y){
			System.out.println("YA greater than Y");
			main.cell[x][y].setEntityType(-1);
			main.cell[x][y].setEntityId(-1);
			main.cell[x][y++].setEntityId(getId());
			main.cell[x][y++].setEntityType(1);
			setY(y++);
			if(x == xa && y == ya){
				System.out.println("Target reached, reseting");
				canWander = true;
				accepted = false;
				setXa(0);
				setYa(0);
				return;
			}
		}else if(ya < y){
			System.out.println("YA less than y");
			main.cell[x][y].setEntityType(-1);
			main.cell[x][y].setEntityId(-1);
			main.cell[x][y--].setEntityId(getId());
			main.cell[x][y--].setEntityType(1);
			setY(y--);
			if(x == xa && y == ya){
				System.out.println("Target reached, reseting");
				canWander = true;
				accepted = false;
				setXa(0);
				setYa(0);
				return;
			}
		}
	}
		
}
