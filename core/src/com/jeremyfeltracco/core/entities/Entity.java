package com.jeremyfeltracco.core.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public abstract class Entity {
	protected Sprite sprite;
	
	public Entity(TextureRegion texture) {
		sprite = new Sprite(texture);
		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
	}
	
	protected void setOriginPosition(float x, float y) {
		sprite.setPosition(x - sprite.getOriginX(), y - sprite.getOriginY());
	}
	
	/**
	 * Returns the position of the Entity, adjusting for the moved origin.
	 * @return the position, in Vector2 form
	 */
	protected Vector2 getOriginPosition() {
		return new Vector2(sprite.getX() + sprite.getOriginX(),
				sprite.getY() + sprite.getOriginY());
	}
	
	public abstract void update(float delta);
	
	public void draw(Batch batch) {
		sprite.draw(batch);
	}

}
