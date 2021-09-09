package com.parkit.parkingsystem.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TimeDifferenceCalculation {

	private final int hoursCalculation = 60 * 60 * 1000;
	private final int freeParkingDuration = 30 * 60 * 1000;

	public double timeDifferenceCalculation(Date exiting, Date incoming) {

		TimeUnit time = TimeUnit.HOURS;

		long difference = exiting.getTime() - incoming.getTime();
		double differenceBetweenTwoDates = time.convert(difference, TimeUnit.HOURS);

		if (differenceBetweenTwoDates <= freeParkingDuration){
			return 0;
		} else {
			return differenceBetweenTwoDates/hoursCalculation;
		}

	}
}
