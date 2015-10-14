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
	
	SpriteBatch batch;
	OrthographicCamera cam;
	Paddle pad;
	Paddle pad2;
	Paddle pad3;
	Paddle pad4;
	Ball ball;
	
	@Override
	public void create () {
		maxX = Gdx.graphics.getWidth() / 2;
		maxY = Gdx.graphics.getHeight() / 2;
		
		batch = new SpriteBatch();
		cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.x = 0;
		cam.position.y = 0;
		pad = new Paddle(Position.TOP);
		pad2 = new Paddle(Position.BOTTOM);
		pad3 = new Paddle(Position.LEFT);
		pad4 = new Paddle(Position.RIGHT);
		ball = new Ball();
	}

	@Override
	public void render () {
		// Update all game entities
		batch.setProjectionMatrix(cam.combined);
		
		
		// Render all game entities
		Gdx.gl.glClearColor(1, 0.8431372549f, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		pad.draw(batch);
		pad.update(Gdx.graphics.getDeltaTime());
		pad2.draw(batch);
		pad3.draw(batch);
		pad4.draw(batch);
		ball.draw(batch);
		batch.end();
	}
}
