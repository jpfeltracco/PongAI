package com.jeremyfeltracco.core.elvolver;

import org.neuroph.util.random.GaussianRandomizer;

import com.jeremyfeltracco.core.Sim;
import com.jeremyfeltracco.core.controllers.MLPerceptronControl;
import com.jeremyfeltracco.core.entities.Ball;
import com.jeremyfeltracco.core.entities.Paddle;
import com.jeremyfeltracco.core.entities.Side;

public class GA {
	Sim sim;
	MLPerceptronControl[] mlps;
	GaussianRandomizer r;
	
	final int numPerGen = 40;
	final int numGamesPer = 10;
	
	int curGen = 0;
	int gameGrp = 0;
	int gameNum = 0;
	
	double[][] weights;
	double[] fitness;
	
	int genCount = 0;
	
	public GA(Sim sim, Paddle[] pads, Ball ball) {
		this.sim = sim;
		mlps = new MLPerceptronControl[pads.length];
		r = new GaussianRandomizer(0, 1f);
		weights = new double[numPerGen][];
		resetFit(); // Initializes and resets fitness
		
		// We only need 4 networks, can have weights changed
		for (int i = 0; i < pads.length; i++) {
			mlps[i] = new MLPerceptronControl(Side.values()[i], pads, ball);
		}
		
		// Abuse first network to generate 50 arrays of weights
		for (int i = 0; i < numPerGen; i++) {
			mlps[0].getNet().randomizeWeights(r);
			Double[] w = mlps[i].getNet().getWeights();
			weights[i] = new double[w.length];
			// Stupid class Double issues
			for (int j = 0; j < w.length; j++) {
				weights[i][j] = w[j].doubleValue();
			}
		}
	}
	
	public void update(Side loser) {
		fitness[gameGrp + loser.ordinal()]--; // Group index + loser index
		gameNum++; // Increase to next game
		// Check if number of games has exceeded 10
		if (gameNum >= 10) {
			gameNum = 0;
			gameGrp += 4;
			// Check if all groups in generation are done
			if (gameGrp > numPerGen - mlps.length) {
				gameGrp = 0; // Reset
				curGen++; // Increase generation count
				// Do something with fitness to make new weights[]
				double newWeights[] = new double[weights.length];
				// set weight[0] to highest from previous generation (elitism)
				// fill in remaining from reproduction algorithm
				// higher fitness, higher chance to reproduce!
				
				// randomize weight order so higher fitness not grouped
			}
			// Set weights for next group
			for (int i = 0; i < mlps.length; i++)
				mlps[i].getNet().setWeights(weights[gameGrp + i]);
		}
		// Continue playing!
	}
	
	private void resetFit() {
		if (fitness == null) fitness = new double[numPerGen];
		for (int i = 0; i < fitness.length; i++)
			fitness[i] = numGamesPer;
	}
	
}
