package com.jeremyfeltracco.core.entities;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.jeremyfeltracco.core.Sim;

public abstract class Entity {
	protected Sprite sprite;
	public static ArrayList<Entity> entities = new ArrayList<Entity>();
	private Vector2 edges[];
	public float maxSize;
	public Entity(TextureRegion texture) {
		sprite = new Sprite(texture);
		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
		entities.add(this);
		maxSize = new Vector2(sprite.getWidth()/2,sprite.getHeight()/2).len();
	}
	
	protected void setOriginPosition(float x, float y) {
		sprite.setPosition(x - sprite.getOriginX(), y - sprite.getOriginY());
	}
	
	/**
	 * Returns the position of the Entity, adjusting for the moved origin.
	 * @return the position, in Vector2 form
	 */
	public Vector2 getOriginPosition() {
		return new Vector2(sprite.getX() + sprite.getOriginX(),
				sprite.getY() + sprite.getOriginY());
	}
	
	public abstract void update(float delta);
	
	public Vector2[] getCorners() {		
		/* 0/4  1
		 * 	_____
		 * 	|	|
		 * 	|___|
		 *  3   2
		 */
		return edges.clone();
	}
	
	protected void updateSides(){
		float[] vertices = sprite.getVertices().clone();
		Vector2 firstPoint = new Vector2(vertices[5],vertices[6]);
		edges = new Vector2[] {firstPoint,new Vector2(vertices[10],vertices[11]),new Vector2(vertices[15],vertices[16]),new Vector2(vertices[0],vertices[1]),firstPoint};
	}
	
	public void draw(Batch batch) {
		sprite.draw(batch);
	}
	
	protected void printCorners(){
		for(int i = 0; i < 4; i++){
			System.out.print(edges[i] + "\t");
		}
		System.out.println();
	}

}
