package com.jeremyfeltracco.core.elvolver;

import java.util.ArrayList;
import java.util.Collections;

import org.neuroph.util.random.GaussianRandomizer;

import com.badlogic.gdx.math.MathUtils;
import com.jeremyfeltracco.core.controllers.Controller;
import com.jeremyfeltracco.core.controllers.mlps.MLPControl;
import com.jeremyfeltracco.core.entities.Ball;
import com.jeremyfeltracco.core.entities.Side;

public class GA {
	final int numPerGen = 40;
	final int numGamesPer = 10;
	final float mutationRate = .05f;
	final float mutationAmt = .20f;
	
	int curGen = 0;
	int gameGrp = 0;
	int gameNum = 0;
	int genCount = 0;
	
	MLPControl[] mlpcontrols;
	GaussianRandomizer r;
	ArrayList<Element> nets;
	
	public GA(MLPControl[] mlpcontrols, Ball ball) {
		this.mlpcontrols = mlpcontrols;
		
		r = new GaussianRandomizer(0, 1f);
		nets = new ArrayList<Element>();

		for (int i = 0; i < numPerGen; i++)
			nets.add(new Element());
		
		resetFit(); // Initializes and resets fitness
		
		// Abuse first network to generate 50 arrays of weights
		for (int i = 0; i < numPerGen; i++) {
			mlpcontrols[0].getNet().randomizeWeights(r);
			Double[] w = mlpcontrols[0].getNet().getWeights();
			nets.get(i).weights = new double[w.length];
			// Unbox the doubles
			for (int j = 0; j < w.length; j++)
				nets.get(i).weights[j] = w[j].doubleValue();
		}
		
		// Set weights for next group
		for (int i = 0; i < mlpcontrols.length; i++)
			mlpcontrols[i].getNet().setWeights(nets.get(gameGrp + i).weights);
		
		
	}
	
	public void update(Side loser) {
		if (loser != null)
			nets.get(gameGrp + loser.ordinal()).fitness--; // Group index + loser index
		gameNum++; // Increase to next game
		// Check if number of games has exceeded 10
		if (gameNum >= 10) {
			gameNum = 0;
			gameGrp += mlpcontrols.length;
			// Check if all groups in generation are done
			if (gameGrp > numPerGen - mlpcontrols.length) {
				gameGrp = 0; // Reset
				curGen++; // Increase generation count
				// Do something with fitness to make new weights[]
				Collections.sort(nets); // Lowest index is lowest fitness, higher index is higher fitness
				@SuppressWarnings("unchecked")
				ArrayList<Element> newNet = (ArrayList<Element>) nets.clone();
				for (int i = 0; i < numPerGen; i++) {
					float rand = MathUtils.random();
					newNet.get(i).weights = reproduce(nets.get(getRepInd(rand)).weights,
							nets.get(getRepInd(rand)).weights);
					
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
			}
			// Set weights for next group
			for (int i = 0; i < mlpcontrols.length; i++)
				mlpcontrols[i].getNet().setWeights(nets.get(gameGrp + i).weights);
		}
	}
	
	private double[] reproduce(double[] one, double[] two) {
		int cutPoint = (int) (MathUtils.random() * one.length);
		double[] child = new double[one.length];
		for (int i = 0; i < cutPoint; i++)
			child[i] = one[i];
		for (int i = cutPoint; i < child.length; i++)
			child[i] = two[i];
		return child;
	}
	
	private void resetFit() {
		for (Element net : nets)
			net.fitness = numGamesPer;
	}
	
	public Controller[] getControllers() {
		return mlpcontrols;
	}
	
	/**
	 * Returns the number of the current generation
	 * @return the gen number
	 */
	public int getCurrentGeneration(){
		return curGen;
	}
	
	public int getGameGroup(){
		return gameGrp;
	}
	
	private int getRepInd(float rand) {
		int ind;
		if (rand > .2f)
			if (rand > .4f)
				if (rand > .7f)
					ind = (int) rand * 5 + 35; // pick number in top 35:39
				else
					ind = (int) rand * 5 + 30; // pick number in 30:34
			else
				ind = (int) rand * 10 + 20; // pick number in 20:29
		else
			ind = (int) rand * 20; // pick number in 0:19
		return ind;
	}
}
