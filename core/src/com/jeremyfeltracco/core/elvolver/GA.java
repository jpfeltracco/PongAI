package com.jeremyfeltracco.core.elvolver;

import java.util.ArrayList;
import java.util.Collections;

import org.neuroph.util.random.GaussianRandomizer;

import com.badlogic.gdx.math.MathUtils;
import com.jeremyfeltracco.core.Sim;
import com.jeremyfeltracco.core.controllers.Controller;
import com.jeremyfeltracco.core.controllers.MLPEvolver;
import com.jeremyfeltracco.core.controllers.MLPerceptronControl;
import com.jeremyfeltracco.core.entities.Ball;
import com.jeremyfeltracco.core.entities.Paddle;
import com.jeremyfeltracco.core.entities.Side;

public class GA {
	Sim sim;
	//MLPEvolver[] mlps;
	MLPerceptronControl[] mlps;
	GaussianRandomizer r;
	
	final int numPerGen = 40;
	final int numGamesPer = 10;
	final float mutationRate = .05f;
	final float mutationAmt = .10f;
	
	int curGen = 0;
	int gameGrp = 0;
	int gameNum = 0;
	
	ArrayList<Element> nets;
	
	int genCount = 0;
	
	public GA(Sim sim, Paddle[] pads, Ball ball) {
		this.sim = sim;
		//mlps = new MLPEvolver[pads.length];
		mlps = new MLPerceptronControl[pads.length];
		r = new GaussianRandomizer(0, 1f);
		nets = new ArrayList<Element>();

		for (int i = 0; i < numPerGen; i++)
			nets.add(new Element());
		
		resetFit(); // Initializes and resets fitness
		
		// We only need 4 networks, can have weights changed
		for (int i = 0; i < pads.length; i++){
			//mlps[i] = new MLPEvolver(Side.values()[i], pads, ball);
			mlps[i] = new MLPerceptronControl(Side.values()[i], pads, ball);
		}
		
		// Abuse first network to generate 50 arrays of weights
		for (int i = 0; i < numPerGen; i++) {
			mlps[0].getNet().randomizeWeights(r);
			Double[] w = mlps[0].getNet().getWeights();
			nets.get(i).weights = new double[w.length];
			// Unbox the doubles
			for (int j = 0; j < w.length; j++)
				nets.get(i).weights[j] = w[j].doubleValue();
		}
	}
	
	public void update(Side loser) {
		nets.get(gameGrp + loser.ordinal()).fitness--; // Group index + loser index
		gameNum++; // Increase to next game
		// Check if number of games has exceeded 10
		if (gameNum >= 10) {
			gameNum = 0;
			gameGrp += mlps.length;
			// Check if all groups in generation are done
			if (gameGrp > numPerGen - mlps.length) {
				gameGrp = 0; // Reset
				curGen++; // Increase generation count
				// Do something with fitness to make new weights[]
				Collections.sort(nets); // Lowest index is lowest fitness, higher index is higher fitness
				@SuppressWarnings("unchecked")
				ArrayList<Element> newNet = (ArrayList<Element>) nets.clone();
				int ind1 = 39;
				for (int i = 0; i < numPerGen; i++) {
					float rand = MathUtils.random();
					if (rand > .2f) {
						if (rand > .4f) {
							if (rand > .7f) {
								// pick number in top 35:39
								ind1 = (int) rand * 5 + 35;
							} else {
								// pick number in 30:34
								ind1 = (int) rand * 5 + 30;
							}
						} else {
							// pick number in 20:29
							ind1 = (int) rand * 10 + 20;
						}
						// pick number in 0:19
					} else {
						ind1 = (int) rand * 20;
					}
					
					int ind2 = 39;
					rand = MathUtils.random();
					if (rand > .2f) {
						if (rand > .4f) {
							if (rand > .7f) {
								// pick number in top 35:39
								ind2 = (int) rand * 5 + 35;
							} else {
								// pick number in 30:34
								ind2 = (int) rand * 5 + 30;
							}
						} else {
							// pick number in 20:29
							ind2 = (int) rand * 10 + 20;
						}
						// pick number in 0:19
					} else {
						ind2 = (int) rand * 20;
					}
					
					newNet.get(i).weights = reproduce(nets.get(ind1).weights, nets.get(ind2).weights);
					
					int weightAmt = newNet.get(i).weights.length;
					
					rand = MathUtils.random();
					if (rand < mutationRate) {
						int numInd = (int) (weightAmt * mutationAmt);
						numInd = MathUtils.random(0, numInd);
						for (int j = 0; j < numInd; j++) {
							int ind = (int) (MathUtils.random() * weightAmt);
							newNet.get(i).weights[ind] = r.getRandomGenerator().nextGaussian();
						}
					}
				}
				
				// fill in remaining from reproduction algorithm
				// higher fitness, higher chance to reproduce!
				
				// randomize weight order so higher fitness not grouped
			}
			// Set weights for next group
			for (int i = 0; i < mlps.length; i++)
				mlps[i].getNet().setWeights(nets.get(gameGrp + i).weights);
		}
		// Continue playing!
	}
	
	private double[] reproduce(double[] one, double[] two) {
		double[] child = new double[one.length];
		for (int i = 0; i < child.length / 2; i++)
			child[i] = one[i];
		for (int i = child.length / 2; i < child.length; i++)
			child[i] = two[i];
		return child;
	}
	
	private void resetFit() {
		for (Element net : nets)
			net.fitness = numGamesPer;
	}
	
	public Controller[] getControllers() {
		return mlps;
	}
	
}
