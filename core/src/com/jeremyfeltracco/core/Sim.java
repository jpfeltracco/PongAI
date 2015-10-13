package com.jeremyfeltracco.core;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Sim extends ApplicationAdapter {
	SpriteBatch batch;
	Paddle pad;
	Paddle pad2;
	Paddle pad3;
	Paddle pad4;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		pad = new Paddle(Position.TOP);
		pad2 = new Paddle(Position.BOTTOM);
		pad3 = new Paddle(Position.LEFT);
		pad4 = new Paddle(Position.RIGHT);
	}

	@Override
	public void render () {
		// Update all game entities
		
		
		
		// Render all game entities
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		pad.draw(batch);
		pad2.draw(batch);
		pad3.draw(batch);
		pad4.draw(batch);
		batch.end();
	}
}
