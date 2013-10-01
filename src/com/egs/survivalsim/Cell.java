package com.egs.survivalsim;

public class Cell {

	protected int id;
	protected int x;
	protected int y;
	protected int tile; //0: Grass, 1: Tree, 2: Rock
	protected int entityType; //-1: Nothing, 0: Zombie, 1: Person
	protected int entityId;
	protected boolean walkable;

	public Cell(){
		
	}
	
	public Cell(int x, int y){
		this.x = x;
		this.y = y;
	}

	public void update(){
		if(tile == 0 && entityType == -1){
			walkable = true;
		}else{
			walkable = false;
		}
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getTile() {
		return tile;
	}

	public void setTile(int tile) {
		this.tile = tile;
	}

	public int getEntityType() {
		return entityType;
	}

	public void setEntityType(int entityType) {
		this.entityType = entityType;
	}

	public int getEntityId() {
		return entityId;
	}

	public void setEntityId(int entityId) {
		this.entityId = entityId;
	}



}
