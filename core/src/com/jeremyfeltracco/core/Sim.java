package com.jeremyfeltracco.core;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
	OrthographicCamera cam;
	Controller[] controls;
	
	@Override
	public void create () {
		maxX = Gdx.graphics.getWidth() / 2;
		maxY = Gdx.graphics.getHeight() / 2;
		
		pads = new Paddle[amtPad]; // Paddles and ball
		corners = new Corner[8];
		for (int i = 0; i < amtPad; i++) {
			pads[i] = new Paddle(Side.values()[i]);
		}
		ball = new Ball();
		
		cornerSize = Textures.corner.getTexture().getHeight()/2;
		corners[0] = new Corner(-maxX+cornerSize,-maxY+cornerSize);
		corners[1] = new Corner(maxX-cornerSize,-maxY+cornerSize);
		corners[2] = new Corner(-maxX+cornerSize,maxY-cornerSize);
		corners[3] = new Corner(maxX-cornerSize,maxY-cornerSize);
		corners[4] = new Corner(0,40);
		corners[5] = new Corner(0,-25);
		corners[6] = new Corner(40,0);
		corners[7] = new Corner(-25,0);
		
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
		float delta = Gdx.graphics.getDeltaTime();
		
		for (Controller c : controls)
			c.updatePaddle();
		
		// Update all game entities
		batch.setProjectionMatrix(cam.combined);
		for (int i = 0; i < pads.length; i++) {
			pads[i].update(delta);
		}
		ball.update(delta);
		
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
		ball.draw(batch);
		
		
		batch.end();
	}
}
