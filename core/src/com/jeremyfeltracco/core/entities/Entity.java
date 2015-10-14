package com.jeremyfeltracco.core.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class Entity {
	protected Sprite sprite;
	
	public Entity(TextureRegion texture) {
		sprite = new Sprite(texture);
		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
	}
	
	protected void setOriginPosition(float x, float y) {
		sprite.setPosition(x - sprite.getOriginX(), y - sprite.getOriginY());
	}
	
	public abstract void update(float delta);
	
	public void draw(Batch batch) {
		sprite.draw(batch);
	}
}
