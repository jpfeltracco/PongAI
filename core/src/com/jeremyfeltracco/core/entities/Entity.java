package com.jeremyfeltracco.core.entities;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public abstract class Entity {
	
	//-----Static Variables-----
	public static ArrayList<Entity> entities = new ArrayList<Entity>();
	public static int objCount = 0;
	//--------------------------
	

	public Sprite sprite;
	private int id;
	private Vector2 edges[];
	protected Vector2 velocity;
	
	//----Physics Variables-----
	public float maxSize;
	public float[] segDists = new float[4];
	public int contactPoints = 0;
	public boolean contact = false;
	public Side side = null;
	//--------------------------
	
	protected Vector2 initVelocity;
	protected float initRotation;
	protected Vector2 initPos;
	
	/**
	 * Constructs a new Entity
	 * @param texture the texture to display with the Entity
	 * @param vel the initial velocity of the Entity
	 */
	public Entity(TextureRegion texture, Vector2 vel, float rotation) {
		sprite = new Sprite(texture);
		Vector2 size = new Vector2(sprite.getWidth() / 2, sprite.getHeight() / 2);
		sprite.setOrigin(size.x, size.y);
		entities.add(this);
		sprite.rotate(rotation);
		maxSize = size.len();
		velocity = vel;
		id = objCount++;
		
		initVelocity = vel;
		initRotation = rotation;
		initPos = new Vector2(0,0);
	}
	
	/**
	 * Constructs a new Entity. Defaults to a velocity of zero.
	 * @param texture the texture to display with the Entity
	 */
	public Entity(TextureRegion texture){
		this(texture, new Vector2(0,0),0f);
	}
	
	protected void setInitPos(){
		initPos = getOriginPosition();
	}
	
	/**
	 * Constructs a new Entity, defined by the shape passed in. This Entity is not added to the entity arrayList and cannot be updated.
	 * Used for framing only.
	 * @param shape the vertices of the shape
	 */
	public Entity(Vector2[] shape){
		edges = shape;
		id = -1;
	}
	
	public void reset(){
		setOriginPosition(initPos);
		velocity = initVelocity;
		sprite.setRotation(initRotation);
		updateSides();
	}
	
	/**
	 * Gets the object ID. Unique to all objects except those defined by a shape.
	 * @return the object ID.
	 */
	public int getID(){
		return id;
	}
	
	/**
	 * Gets the velocity of the Entity
	 * @return the velocity of the Entity, in Vector2 form
	 */
	public Vector2 getVelocity() {
		return velocity;
	}
	
	
	/**
	 * Returns the position of the Entity.
	 * @return the position, in Vector2 form
	 */
	public Vector2 getOriginPosition() {
		if(sprite != null){
			return new Vector2(sprite.getX() + sprite.getOriginX(),
				sprite.getY() + sprite.getOriginY());
		}else{
			return new Vector2( (edges[0].x + edges[1].x)/2, (edges[0].y + edges[1].y)/2);
		}
	}
	
	/**
	 * Abstract function update: method that is called when each Entity is told to update.
	 * @param delta the change in time since the last update
	 */
	public abstract void update(float delta);
	
	
	/**
	 * Gets the corners of the Entity, including a 5th corner which is the same as the 1st one. Calculated based on the sprite or shape provided. 
	 * @return the corners in Vector2 form
	 */
	public Vector2[] getCorners() {		
		return edges.clone();
	}
	
	/**
	 * Gets the corners of the Entity. Calculated based on the sprite or shape provided. 
	 * @return the corners in Vector2 form
	 */
	public Vector2[] getFourCorners(){
		Vector2 out[] = new Vector2[4];
		for(int i = 0; i < 4; i++){
			out[i] = edges[i];
		}
		return out.clone();
	}
	
	/**
	 * Recalculates the sides of the Entity. Used if the entity is rotated or moved in any way.
	 */
	public void updateSides(){
		float[] vertices = sprite.getVertices().clone();
		Vector2 firstPoint = new Vector2(vertices[5],vertices[6]);
		edges = new Vector2[] {firstPoint,new Vector2(vertices[10],vertices[11]),new Vector2(vertices[15],vertices[16]),new Vector2(vertices[0],vertices[1]),firstPoint};
	}
	
	/**
	 * Draws the object on the screen.
	 * @param batch the batch to draw in
	 */
	public void draw(Batch batch) {
		sprite.draw(batch);
	}
	
	/**
	 * Returns a string version of this object.
	 */
	public String toString(){
		return "" + id;
	}
	
	/**
	 * Removes this object from the update list.
	 */
	public void remove(){
		entities.remove(this);
	}
	
	/**
	 * Sets the origin position (defined at the center of the image via width and height).
	 * @param x the x position
	 * @param y the y position
	 */
	protected void setOriginPosition(float x, float y) {
		sprite.setPosition(x - sprite.getOriginX(), y - sprite.getOriginY());
		updateSides();
	}
	
	/**
	 * Sets the origin position (defined at the center of the image via width and height).
	 * @param v the position in Vector2 form
	 */
	protected void setOriginPosition(Vector2 v) {
		setOriginPosition(v.x, v.y);
	}
	
	/**
	 * Prints the corners of the object. Used for debugging.
	 */
	protected void printCorners(){
		for(int i = 0; i < 4; i++){
			System.out.print(edges[i] + "\t");
		}
		System.out.println();
	}
	
	/**
	 * Gets the initial velocity of this Entity
	 * @return the initial velocity
	 */
	public Vector2 getInitVelocity(){
		return initVelocity;
	}
	
	/**
	 * Gets the initial rotation of this Entity
	 * @return the initial rotation
	 */
	public float getInitRotation(){
		return initRotation;
	}
	
	/**
	 * Gets the initial position of this Entity
	 * @return the initial position
	 */
	public Vector2 getInitPos(){
		return initPos;
	}

}
