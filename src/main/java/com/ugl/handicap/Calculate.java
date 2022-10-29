package com.ugl.handicap;

import java.util.Arrays;

public class Calculate {

	public Calculate() {

	}

	/**
	 * Find the mean of a range of data.
	 * 
	 * @param data
	 * @return
	 */
	public static double mean(double[] data) {
		double sum = 0.0;
		double len = (double) data.length;
		for (double d : data) {
			sum += d;
		}
		sum = (sum) / len;
		return sum;
	}

	/**
	 * Find median of a ran of values.
	 * 
	 * @param data
	 * @return
	 */
	public static double median(double[] data) {
		double med = 0.0;
		Arrays.sort(data);
		int index = (data.length / 2) - 1;
		if (data.length > 1) {
			med = data[index] + data[index + 1];
			med /= 2;
		} else {
			med = data[0];
		}
		return med;
	}

	/**
	 * Calculate the standard deviation from your average.
	 * 
	 * @param data
	 * @return
	 */
	public static double standardDeviation(double[] data) {
		double std = 0.0;
		double mn = mean(data);
		int i = 0;
		double[] arrSamp = new double[data.length];
		for (double d : data) {
			double samp = Math.pow(d - mn, 2);
			arrSamp[i++] = samp;
		}
		double sumCol = mean(arrSamp);
		double div = mn - 1.0;
		std = Math.sqrt(sumCol / div);
		return std;
	}

	/**
	 * Calculate the handicap index.
	 * 
	 * @param rating - course rating
	 * @param slope  - course slope
	 * @param score  - 72 hole score
	 */
	public static double calculateHandicapIndex(double rating, double slope, double score) {
		double index = ((score - rating) * 113) / slope;
		index *= .96;
		return index;
	}

	/**
	 * Calculate the handicap.
	 * 
	 * @param rating        - course rating
	 * @param slope         - course slope
	 * @param handicapIndex - player handicap index
	 */
	public static double calculateHandicap(double rating, double slope, double handicapIndex) {
		double handicap = (handicapIndex * slope) / 113;
		return handicap;
	}

}
