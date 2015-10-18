package com.jeremyfeltracco.core.entities;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.jeremyfeltracco.core.Sim;
import com.jeremyfeltracco.core.Textures;

public class Ball extends Entity {
	private Vector2 pos;
	private float radius;
	private float maxVelocity = 500;
	private int lastIDContact = -1;
	private String errorString;

	/**
	 * Creates a new ball. Can bounce off Entities that are properly configured.
	 * 
	 * @param x the initial x position
	 * @param y the initial y position
	 * @param initVelo the initial velocity
	 */
	public Ball(float x, float y, Vector2 initVelo) {
		super(Textures.ball, initVelo);
		setOriginPosition(x, y);
		pos = this.getOriginPosition();
		radius = this.sprite.getWidth() / 2;
	}
	
	/**
	 * Updates the ball's position and physics
	 */
	public void update(float delta) {
		if (velocity.len() > maxVelocity) {
			velocity.nor().scl(maxVelocity);
			//System.out.println("NEW VELOCITY VALUE: " + velocity + " :" + velocity.cpy().len() + "\tTime: "
			//		+ Sim.systemTime + "\n");
		}
		pos = pos.add(velocity.cpy().scl(delta));
		physics(delta);
		setOriginPosition(pos.x, pos.y);
	}

	/**
	 * Operates physics based on the delta t
	 * @param delta the time since the last update
	 */
	public void physics(float delta) {

		checkWallBounces();

		
		//Initial object sorting
		Entity c = this;
		int contactCount = 0;
		for (Entity cor : Entity.entities) {
			Vector2 tmp = pos.cpy().sub(cor.getOriginPosition());
			if (cor == this || tmp.x * tmp.x + tmp.y+tmp.y  > (cor.maxSize + radius * 3) * (cor.maxSize + radius * 3))
				continue;
			Vector2[] corners = cor.getCorners();
			for (int i = 0; i < 4; i++) {
				cor.segDists[i] = Intersector.distanceSegmentPoint(corners[i], corners[i + 1], pos);
			}

			cor.contactPoints = 0;
			cor.contact = false;
			for (int i = 0; i < 4; i++) {
				if (cor.segDists[i] <= radius) {
					// System.out.println(proj(pos.cpy().sub(cor.getOriginPosition()),velocity));
					cor.contactPoints++;
					cor.contact = true;
				}
			}
			if (cor.contact) {
				contactCount++;
				c = cor;
			}
		}
		
		//----------------------

		boolean virtualShape = false;
		if (contactCount > 2) {
			error("Collision with too many objects (>2)\tTIME: " + Sim.systemTime);
			return;
		} else if (contactCount == 2) {
			//System.out.println("----Contact with mutiple objects----");
			Entity mergeObj[] = new Entity[2];
			int i = 0;
			for (Entity cor : Entity.entities) {
				if (cor.contact == true) {
					mergeObj[i] = cor;
					i++;
				}
				if (i == 2)
					break;
			}

			Vector2[] e0 = mergeObj[0].getFourCorners();
			Vector2[] e1 = mergeObj[1].getFourCorners();

			//System.out.println("IDs: " + mergeObj[0] + ", " + mergeObj[1]);

			int numPointsConnected = 0;
			for (i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					if (e1[j] == null)
						continue;
					if (close(e1[j], e0[i])) {
						e1[j] = null;
						e0[i] = null;
						numPointsConnected++;
						break;
					}
				}
			}

			if (numPointsConnected == 0) {
				error("The ball hit two objects that were not touching. (Sandwich effect) (IDs: " + mergeObj[0]
						+ ", " + mergeObj[1] + "). TIME: " + Sim.systemTime);
				return;
			}

			//System.out.println("Points connected: " + numPointsConnected);
			if (numPointsConnected == 1) {
				//System.out.println("1: SIDE: " + mergeObj[0].side);
				//System.out.println("2: SIDE: " + mergeObj[1].side);
				if (mergeObj[0].side != null && mergeObj[1].side != null) {
					lastIDContact = -1;
					velocity = velocity.scl(-1);
					return;
				}
			}

			/*System.out.println("e0:");
			for (Vector2 v : e0) {
				System.out.print(v + " ");
			}
			System.out.println("\ne1:");
			for (Vector2 v : e1) {
				System.out.print(v + " ");
			}
			System.out.println();*/

			/*
			 * for(i = 0; i < 4; i++){ Vector2 point; for(Vector2 v : e0){ if(v
			 * != null){ point = v; break; } }
			 * 
			 * }
			 */

			Vector2[] newShape = new Vector2[5];
			int j = 0;
			for (i = 0; i < 4; i++) {
				if (e0[i] != null) {
					newShape[j++] = e0[i];
				}
			}
			for (i = 0; i < 4; i++) {
				if (e1[i] != null) {
					newShape[j++] = e1[i];
				}
			}

			newShape[4] = newShape[0].cpy();

			Vector2 sides[] = new Vector2[5];
			for (i = 0; i < 4; i++) {
				sides[i] = simplify(newShape[i + 1].cpy().sub(newShape[i]).cpy());
			}
			sides[4] = sides[0];
			for (i = 0; i < 4; i++) {
				if (Math.abs(sides[i].dot(sides[i + 1])) > 0.005) {
					Vector2 tmp = newShape[2];
					newShape[2] = newShape[3];
					newShape[3] = tmp;
					break;
				}
			}

			/*System.out.println("NEW:");
			for (Vector2 cor : newShape) {
				System.out.print(cor + "\t");
			}
			System.out.println("\n");*/

			c = new VirtualShape(newShape);
			Vector2[] corners = c.getCorners();
			for (i = 0; i < 4; i++) {
				c.segDists[i] = Intersector.distanceSegmentPoint(corners[i], corners[i + 1], pos);
			}
			virtualShape = true;
		}

		if (c != this) {
			Vector2[] corners = c.getCorners();
			int index = -1;
			int altIndex = -1;

			// ----------------------CONTACT----------------------

			double dist = Double.MAX_VALUE;
			for (int i = 0; i < 4; i++) {
				if (c.segDists[i] < dist) {
					dist = c.segDists[i];
					index = i;
				}
			}

			boolean contactCorner = false;
			if (c.contact && c.contactPoints > 1) {
				for (int i = 0; i < 4; i++) {
					if (i != index) {
						if (Math.abs(c.segDists[i] - c.segDists[index]) < 0.5) {
							altIndex = i;
							contactCorner = true;
						}
					}
				}
			}

			// ---------------------------------------------------

			if (lastIDContact == c.getID() && lastIDContact != -1) {
				error("The ball hit the same object (ID: " + c.getID() + ") twice. TIME: " + Sim.systemTime);
			}
			lastIDContact = c.getID();
			Vector2 side;
			Vector2 n;
			if (contactCorner) {
				//System.out.println("----------Corner Collision--------  ID: " + c + "\tTime: " + Sim.systemTime);
				/*for (int i = 0; i < 4; i++) {
					if (i == index || i == altIndex) {
						System.out.println("\t[" + i + "]\t" + c.segDists[i] + "\tPixels Away\t" + corners[i] + ","
								+ corners[i + 1] + "\t:" + corners[i + 1].cpy().sub(corners[i].cpy()));
					} else {
						System.out.println("\t " + i + "\t" + c.segDists[i] + "\tPixels Away");
					}
				}*/
				
				Vector2 point;

				if (corners[index + 1] == corners[altIndex]) {
					point = corners[altIndex].cpy();
				} else {
					point = corners[index].cpy();
				}
				//System.out.println("DISTANCE: " + point.cpy().sub(pos.cpy()).len());

				Vector2 p = pos.cpy();
				Vector2 velTmp = velocity.cpy().nor();
				while (point.cpy().sub(p.cpy()).len() < radius) {
					p.sub(velTmp);
					//System.out.println("DIST: " + point.cpy().sub(p.cpy()).len() + "\t" + p);
				}

				//System.out.println("DISTANCE: " + point.cpy().sub(p.cpy()).len());
				n = p.cpy().sub(point).nor();
				//System.out.println("Calculated Corner Point: " + point);
				//System.out.println("New Ball Point: " + p);
				//System.out.println("Ball Pos: " + pos + "\tC Pos: " + c.getOriginPosition() + "\tNormal: " + n);

			} else {
				//System.out.println("-----------Side Collision---------  ID: " + c + "\tTime: " + Sim.systemTime);
				/*for (int i = 0; i < 4; i++) {
					if (i == index) {
						System.out.println("\t[" + i + "]\t" + c.segDists[i] + "\tPixels Away\t" + corners[i] + ","
								+ corners[i + 1] + "\t:" + corners[i + 1].cpy().sub(corners[i].cpy()));
					} else {
						System.out.println("\t " + i + "\t" + c.segDists[i] + "\tPixels Away");
					}
				}*/

				side = simplify(corners[index + 1].cpy().sub(corners[index]).cpy());
				//System.out.println(
				//		"Calculated Side: " + side + "\tCorners: " + corners[index] + "  " + corners[index + 1]);
				side = side.nor();
				n = new Vector2(-side.y, side.x).nor();
				//System.out.println("Ball Pos: " + pos + "\tC Pos: " + c.getOriginPosition() + "\tNormal: " + n);
			}

			Vector2 v = simplify(velocity.cpy());
			Vector2 addVel = new Vector2(0, 0);
			if (contactCorner && v.cpy().nor().dot(n.cpy()) > 0) {
				//System.out.println("----SKIM----");
				n = perp(v.cpy().nor());
			} else {
				addVel = simplify(proj(c.getVelocity(), n));
				velocity = v.cpy().sub(n.cpy().scl(2 * v.cpy().dot(n))); //***
				if (!(Math.abs(addVel.x) < 0.001) || !(Math.abs(addVel.y) < 0.001)) {
					//System.out.println("------------Acceleration----------");

					//System.out.print("Init Vel: " + velocity);
					//velocity = v.cpy().sub(n.cpy().scl(2 * v.cpy().dot(n)));
					//System.out.print("\tCal Vel: " + velocity);
					velocity.add(c.getVelocity());// .cpy().scl(1.1f));

					//System.out.println(
					//		"\tC Vel: " + c.getVelocity()/* .cpy().scl(1.1f) */ + "\tFinal Vel: " + velocity);

				} else {
					//System.out.println("---------Collision Physics--------");
					//System.out.println("Old Velocity: " + velocity + "\tNormal: " + n);
					//velocity = v.cpy().sub(n.cpy().scl(2 * v.cpy().dot(n))); //COMMENT ABOVE ***
					//velocity = v.cpy().sub(n.cpy().scl(2 * v.cpy().dot(n)));
					//System.out.println("New Velocity: " + velocity);

				}
				
			}
			Vector2 u = velocity.cpy().nor();
			// System.out.println(u + "\tSCALE: " + (c.segDists[index] - radius
			// + velocity.cpy().scl(delta).len()));
			pos.add(u.scl(radius - c.segDists[index]));
			//double distance = Intersector.distanceSegmentPoint(corners[index], corners[index + 1], pos);
			while (Intersector.distanceSegmentPoint(corners[index], corners[index + 1], pos) < radius) {
				pos.add(u);
				//System.out.println("DIST: " + distance);
				//distance = Intersector.distanceSegmentPoint(corners[index], corners[index + 1], pos);
			}

			// pos = pos.cpy().add(u.scl(radius - c.segDists[index]));// +
			// velocity.cpy().scl(delta).len() + addVel.scl(delta).len()));
			//System.out.println("New Pos: " + pos + "Dist: " + corners[index].cpy().sub(pos.cpy()).len());
			//System.out.println("----------------------------------\n");
			// Adjust position to keep the ball from getting stuck in the shape

			float[] vertices = new float[8];
			for (int i = 0; i < 4; i++) {
				vertices[i * 2] = corners[i].x;
				vertices[i * 2 + 1] = corners[i].y;
			}
			com.badlogic.gdx.math.Polygon poly = new com.badlogic.gdx.math.Polygon(vertices);
			if (poly.contains(pos.x, pos.y)) {
				error("The ball has been detected inside another object. (ID: " + c.getID() + ") TIME: "
						+ Sim.systemTime);
			}
		}
		
		if(virtualShape){
			c.remove();
		}
		
	}
	
	private void checkWallBounces(){
		if (pos.x + radius > Sim.maxX) {
			Sim.setSideHit(Side.RIGHT);
			velocity.x = -Math.abs(velocity.x);
			lastIDContact = -1;
			Sim.setSideHit(Side.RIGHT);
		}
		if (pos.x - radius < -Sim.maxX) {
			Sim.setSideHit(Side.LEFT);
			velocity.x = Math.abs(velocity.x);
			lastIDContact = -1;
			Sim.setSideHit(Side.LEFT);
		}
		if (pos.y + radius > Sim.maxY) {
			Sim.setSideHit(Side.UP);
			velocity.y = -Math.abs(velocity.y);
			lastIDContact = -1;
			Sim.setSideHit(Side.UP);
		}
		if (pos.y - radius < -Sim.maxY) {
			Sim.setSideHit(Side.DOWN);
			velocity.y = Math.abs(velocity.y);
			lastIDContact = -1;
			Sim.setSideHit(Side.DOWN);
		}
		
	}
	
	private void error(String message) {
		Sim.enable = false;
		System.out.println("\n-----ERROR-----\n" + message + "\n---------------");
	}

	private Vector2 perp(Vector2 v) {
		Vector2 tmp = v.cpy();
		return new Vector2(-tmp.y, tmp.x);
	}

	private Vector2 simplify(Vector2 v) {
		return new Vector2(((Math.abs(v.x) < 0.0001) ? 0 : v.x), ((Math.abs(v.y) < 0.0001) ? 0 : v.y));
	}

	private Vector2 proj(Vector2 ina, Vector2 inb) {
		Vector2 a = ina.cpy();
		Vector2 b = inb.cpy();
		return b.nor().scl(b.dot(a) / b.len());
	}

	private boolean close(Vector2 a, Vector2 b, double tol) {
		return (Math.abs(b.x - a.x) < tol && Math.abs(b.y - a.y) < tol);
	}

	private boolean close(Vector2 a, Vector2 b) {
		return close(a, b, 0.01);
	}

}
