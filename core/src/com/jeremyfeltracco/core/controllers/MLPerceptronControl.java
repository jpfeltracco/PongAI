package com.jeremyfeltracco.core.controllers;

import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.util.TransferFunctionType;

import com.jeremyfeltracco.core.entities.Ball;
import com.jeremyfeltracco.core.entities.Paddle;
import com.jeremyfeltracco.core.entities.Paddle.State;
import com.jeremyfeltracco.core.entities.Side;

public class MLPerceptronControl extends Controller {
	MultiLayerPerceptron mlPerceptron;
	
	public MLPerceptronControl(Side side, Paddle[] pads, Ball ball) {
		super(side, pads, ball);
		mlPerceptron = new MultiLayerPerceptron(TransferFunctionType.TANH, 1, 3, 3, 3, 1);
		
		DataSet trainingSet = new DataSet(1, 1);
		trainingSet.addRow(new DataSetRow(new double[]{.25}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{.75}, new double[]{1}));
		trainingSet.addRow(new DataSetRow(new double[]{0}, new double[]{0}));
		trainingSet.addRow(new DataSetRow(new double[]{1}, new double[]{0}));
		
		BackPropagation learningRule = new BackPropagation();
		learningRule.setMaxIterations(1000);
		learningRule.setMaxError(.1);
		learningRule.setLearningRate(0.2);
		// Here's a comment
		mlPerceptron.randomizeWeights(0, 1);

		
		mlPerceptron.setLearningRule(learningRule);
		
		mlPerceptron.learn(trainingSet);
	}

	@Override
	public State controlPaddle() {
		float ballX = this.getBallPos().x;
		float padX = this.getPaddlePos().x;
		float in = (ballX - padX) / (4 * 250) + 0.5f; // 0 = farthest left, 1 = farthest righ
		mlPerceptron.setInput(in);
		mlPerceptron.calculate();
		double output = mlPerceptron.getOutput()[0];
		if (output < .4) return State.LEFT;
		else if (output > .4) return State.RIGHT;
		
		return State.STOP;
	}
}

