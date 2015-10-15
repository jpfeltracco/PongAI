package com.jeremyfeltracco.core.entities;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Disposable;
import com.jeremyfeltracco.core.Sim;

public abstract class Entity {
	public Sprite sprite;
	public static ArrayList<Entity> entities = new ArrayList<Entity>();
	private Vector2 edges[];
	public float maxSize;
	Body body;
	public Entity(TextureRegion texture, float x, float y) {
		sprite = new Sprite(texture);
		sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
		entities.add(this);
		maxSize = new Vector2(sprite.getWidth()/2,sprite.getHeight()/2).len();
		

		
		/*// Now create a BodyDefinition.  This defines the physics objects type and position in the simulation
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        // We are going to use 1 to 1 dimensions.  Meaning 1 in physics engine is 1 pixel
        // Set our body to the same position as our sprite
        bodyDef.position.set(x, y);

        // Create a body in the world using our definition
        body = Sim.world.createBody(bodyDef);

        // Now define the dimensions of the physics shape
        PolygonShape shape = new PolygonShape();
        // We are a box, so this makes sense, no?
        // Basically set the physics polygon to a box with the same dimensions as our sprite
        shape.setAsBox(sprite.getWidth()/2, sprite.getHeight()/2);

        // FixtureDef is a confusing expression for physical properties
        // Basically this is where you, in addition to defining the shape of the body
        // you also define it's properties like density, restitution and others we will see shortly
        // If you are wondering, density and area are used to calculate over all mass
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;

        Fixture fixture = body.createFixture(fixtureDef);

        // Shape is the only disposable of the lot, so get rid of it
        shape.dispose();*/
		
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
	
	public void updateSides(){
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
