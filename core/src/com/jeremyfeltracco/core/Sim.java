package com.jeremyfeltracco.core;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jeremyfeltracco.core.entities.Ball;
import com.jeremyfeltracco.core.entities.Paddle;

public class Sim extends ApplicationAdapter {
	
	public static int maxX;
	public static int maxY;
	
	private static int amtPad = 4;
	
	SpriteBatch batch;
	OrthographicCamera cam;
	Paddle[] pads;
	Paddle pad;
	Paddle pad2;
	Paddle pad3;
	Paddle pad4;
	Ball ball;
	
	@Override
	public void create () {
		maxX = Gdx.graphics.getWidth() / 2;
		maxY = Gdx.graphics.getHeight() / 2;
		
		pads = new Paddle[amtPad]; // Paddles and ball
		for (int i = 0; i < amtPad; i++) {
			pads[i] = new Paddle(Position.values()[i]);
		}
		ball = new Ball();
		
		batch = new SpriteBatch();
		cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.x = 0;
		cam.position.y = 0;
	}

	@Override
	public void render () {
		float delta = Gdx.graphics.getDeltaTime();
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
		ball.draw(batch);
		batch.end();
	}
}
