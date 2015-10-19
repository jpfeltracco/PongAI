package com.jeremyfeltracco.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.jeremyfeltracco.core.controllers.Controller;
import com.jeremyfeltracco.core.controllers.MLPerceptronControl;
import com.jeremyfeltracco.core.controllers.Naive;
import com.jeremyfeltracco.core.elvolver.GA;
import com.jeremyfeltracco.core.entities.Ball;
import com.jeremyfeltracco.core.entities.Corner;
import com.jeremyfeltracco.core.entities.Entity;
import com.jeremyfeltracco.core.entities.Paddle;
import com.jeremyfeltracco.core.entities.Side;

public class Sim extends ApplicationAdapter {
	
	public static int maxX;
	public static int maxY;
	
	public static int amtPad = 4;
	public static Paddle[] pads;
	public static Ball ball;
	public static float cornerSize;
	public static double systemTime = 0;
	public static boolean enable = true;
	public static String errorMessage;
	public static FileHandle fh;
	private static Side loser;
	private static int simulationRuns = 0;
	private static double totalSystemTime = 0; 
	SpriteBatch batch;
	public static OrthographicCamera cam;
	Controller[] controls;
	
	GA algorithm;
	
	boolean value = false;
	@Override
	public void create () {
		fh = Gdx.files.internal("output.txt");
		log("----------Simulation Start----------\n");
		maxX = Gdx.graphics.getWidth() / 2;
		maxY = Gdx.graphics.getHeight() / 2;
		
		pads = new Paddle[amtPad];
		for (int i = 0; i < amtPad; i++) {
			pads[i] = new Paddle(Side.values()[i]);
		}
		ball = new Ball(0,0, new Vector2(-300f,680f));
		
		cornerSize = Textures.corner.getTexture().getHeight()/2;
		new Corner(-maxX+cornerSize,-maxY+cornerSize);
		new Corner(maxX-cornerSize,-maxY+cornerSize);
		new Corner(-maxX+cornerSize,maxY-cornerSize);
		new Corner(maxX-cornerSize,maxY-cornerSize);
		new Corner(130,130);
		new Corner(50,50);
		new Corner(-49f,-50);
		new Corner(-55,0);
		
		log("Added " + Entity.objCount + " objects to the simulation.\n");
		
		algorithm = new GA(this, pads, ball);
		
//		controls = new Controller[amtPad];
//		for (int i = 0; i < amtPad; i++) {
//			controls[i] = new Naive(Side.values()[i], pads, ball);
//		}
		
		controls = algorithm.getControllers();

		batch = new SpriteBatch();
		cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.x = 0;
		cam.position.y = 0;

		
		
		
		
		value = true;
		while(value && simulationRuns < 10000){
			update();
		}
		
	}
	
	public void update(){
		if(!enable || systemTime > 120){
			reset();
		}
		float delta = 1/60f;//0.01666f;
		systemTime += delta;
		totalSystemTime += delta;
		for (Controller c : controls)
			c.update();
		for(Entity e : Entity.entities)
			e.update(delta);
		
		if(loser != null){
			System.out.print("Loser: " + loser + "\t");
			algorithm.update(loser);
			reset();
			//Handle loser
			//log("Loser: " + loser + "\tSim Runs: " + simulationRuns + "\tTotalSystemTime: " + totalSystemTime + "\n");
			loser = null;
		}
	}
	
	private void reset(){
		for(Entity e : Entity.entities){
			e.reset();
		}
		enable = true;
		systemTime = 0;
		System.out.println("Sim Runs: " + simulationRuns + "\tTotalSystemTime: " + totalSystemTime);
		
		simulationRuns++;
	}

	@Override
	public void render () {
		update();
		batch.setProjectionMatrix(cam.combined);
		Gdx.gl.glClearColor(1, 0.8431372549f, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		for(Entity e : Entity.entities)
			e.draw(batch);
		batch.end();
		
	}
	
	public static void setSideHit(Side s){
		loser = s;
	}
	
	public static void writeError(String in){
		System.out.println(in);
		log(in + "\nElements:\n");
		for(Entity e : Entity.entities){
			log("\t" + e.getClass() + "\t POS: " + e.getInitPos() + "\t ROT: " + e.getInitRotation() + "\t VEL: " + e.getInitVelocity() + "\n");
		}
		log("\nConditions: " + "\tSimulation Number: " + simulationRuns + "\tAI Version: " + "<AI VERSION INFO>" + "\tTotal Sim Time: " + totalSystemTime + "\n");
	}
	
	public static void log(String s){
		try {
		    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fh.file().getAbsolutePath(), true)));
		    out.print(s);
		    out.close();
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}


