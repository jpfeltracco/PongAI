package com.jeremyfeltracco.core;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.jeremyfeltracco.core.controllers.Controller;
import com.jeremyfeltracco.core.controllers.Naive;
import com.jeremyfeltracco.core.entities.Ball;
import com.jeremyfeltracco.core.entities.Corner;
import com.jeremyfeltracco.core.entities.Paddle;
import com.jeremyfeltracco.core.entities.Side;

public class Sim extends ApplicationAdapter {
	
	public static int maxX;
	public static int maxY;
	
	public static int amtPad = 4;
	public static Paddle[] pads;
	public static Corner[] corners;
	public static Ball ball;
	public static float cornerSize;
	SpriteBatch batch;
	public static OrthographicCamera cam;
	Controller[] controls;
	public static World world;
	
	@Override
	public void create () {
		//world = new World(new Vector2(0, 0), true);
		maxX = Gdx.graphics.getWidth() / 2;
		maxY = Gdx.graphics.getHeight() / 2;
		
		pads = new Paddle[amtPad]; // Paddles and ball
		corners = new Corner[8];
		for (int i = 0; i < amtPad; i++) {
			pads[i] = new Paddle(Side.values()[i]);
		}
		//pads[0] = new Paddle(Side.LEFT);
		ball = new Ball(107,102);
		
		cornerSize = Textures.corner.getTexture().getHeight()/2;
		corners[0] = new Corner(-maxX+cornerSize,-maxY+cornerSize);
		corners[1] = new Corner(maxX-cornerSize,-maxY+cornerSize);
		corners[2] = new Corner(-maxX+cornerSize,maxY-cornerSize);
		corners[3] = new Corner(maxX-cornerSize,maxY-cornerSize);
		corners[4] = new Corner(130,130);
		corners[5] = new Corner(0,-35);
		corners[6] = new Corner(50,0);
		corners[6].sprite.rotate(30);
		corners[6].updateSides();
		corners[7] = new Corner(-55,0);
		
		controls = new Controller[amtPad];
		for (int i = 0; i < amtPad; i++) {
			controls[i] = new Naive(Side.values()[i], pads, ball);
		}
		
		
		
		batch = new SpriteBatch();
		cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.x = 0;
		cam.position.y = 0;
	}

	@Override
	public void render () {
		float delta = 0.01f;//Gdx.graphics.getDeltaTime();
		//world.step(0.1f, 10, 10);
		
		
		//world.st
		
		for (Controller c : controls)
			c.update();
		
		// Update all game entities
		batch.setProjectionMatrix(cam.combined);
		for (int i = 0; i < pads.length; i++) {
			pads[i].update(delta);
		}
		
		
		// Render all game entities
		Gdx.gl.glClearColor(1, 0.8431372549f, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		for (int i = 0; i < pads.length; i++) {
			pads[i].draw(batch);
		}
		for (int i = 0; i < corners.length; i++) {
			corners[i].draw(batch);
		}
		ball.update(delta);
		ball.draw(batch);
		
		
		batch.end();
		
		/*try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
}
