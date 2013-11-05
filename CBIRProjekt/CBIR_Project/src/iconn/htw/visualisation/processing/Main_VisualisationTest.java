package iconn.htw.visualisation.processing;

import iconn.htw.main.RBMTest;
import processing.core.PApplet;

@SuppressWarnings("serial")
public class Main_VisualisationTest extends PApplet {
	// An array of stripes

	double[][] exampleMMatrix;
	double[][] matrix;
	RBM_Visualisation bm_vis_test;
	int rectSize = 20;
	private int mH;
	private int mW;

	RBMTest rbm;

	public void setup() {
		colorMode(RGB);
		size(800, 600);

		exampleMMatrix = new double[][] { { 0.000, 0.000, 0.000 },
				{ -0.3, -0.7, -1.0 }, { 0.3, 0.7, 1.0 },
				{ 0.000, -0.177, -0.007 }, { 0.000, -0.059, -0.152 },
				{ 0.000, -0.004, -0.053 }, { 0.000, -0.115, -0.085 } };


		rbm = new RBMTest(6, 2, 0.1f);

		double data[][] = {
				// Alice: (Harry Potter = 1, Avatar = 1, LOTR 3 = 1, Gladiator =
				// 0, Titanic = 0, Glitter = 0). Big SF/fantasy fan.
				{ 1, 1, 1, 0, 0, 0 },
				// Bob: (Harry Potter = 1, Avatar = 0, LOTR 3 = 1, Gladiator =
				// 0, Titanic = 0, Glitter = 0). SF/fantasy fan, but doesn't
				// like Avatar.
				{ 1, 0, 1, 0, 0, 0 },
				// Carol: (Harry Potter = 1, Avatar = 1, LOTR 3 = 1, Gladiator =
				// 0, Titanic = 0, Glitter = 0). Big SF/fantasy fan.
				{ 1, 1, 1, 0, 0, 0 },
				// David: (Harry Potter = 0, Avatar = 0, LOTR 3 = 1, Gladiator =
				// 1, Titanic = 1, Glitter = 0). Big Oscar winners fan.
				{ 0, 0, 1, 1, 1, 0 },
				// Eric: (Harry Potter = 0, Avatar = 0, LOTR 3 = 1, Gladiator =
				// 1, Titanic = 0, Glitter = 0). Oscar winners fan, except for
				// Titanic.
				{ 0, 0, 1, 1, 0, 0 },
				// Fred: (Harry Potter = 0, Avatar = 0, LOTR 3 = 1, Gladiator =
				// 1, Titanic = 1, Glitter = 0). Big Oscar winners fan.
				{ 0, 0, 1, 1, 1, 0 }, };

		mH = rbm.getWeights().length;
		mW = rbm.getWeights()[0].length;
		bm_vis_test = new RBM_Visualisation(this, mH, mW);
		rbm.train(data, 1000);
		rbm.printMatrix("Weights", rbm.getWeights());

		bm_vis_test.draw(rbm.getWeights());
		double user[][] = {
				// Gregory: (Harry Potter = 1, Avatar = 1, LOTR 3 = 1, Gladiator
				// = 0, Titanic = 0, Glitter = 0). Big SF/fantasy fan.
				{ 0, 0, 1, 1, 1, 0 }, { 1, 1, 1, 0, 0, 0 } };

		for (int i = 0; i < 1; i++) {
			rbm.printMatrix("User", user);
			double[][] result1 = rbm.run_visual(user);
			rbm.printMatrix("Result", result1);
			double[][] result2 = rbm.run_hidden(result1);
			rbm.printMatrix("Check", result2);
			System.out.println("");
		}

	}

	// public void draw() {
	// background(100);
	// bm_vis_test.draw(randomMatrix);
	//
	// }
}