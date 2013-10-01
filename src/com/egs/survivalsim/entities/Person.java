package com.egs.survivalsim.entities;

import java.util.Random;

import com.egs.survivalsim.MainComponent;

public class Person extends Entity{
	
	MainComponent main;
	Random random;
	
	public Person(int id, int x, int y, MainComponent main){
		this.id = id;
		this.x = x;
		this.y = y;
		this.main = main;
		random = new Random();
	}

	public void update(){

	}
		
}
