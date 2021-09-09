package com.parkit.parkingsystem.service;

import java.util.Date;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    private double price = 0.0;

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        Date inHour = ticket.getInTime();
        Date outHour = ticket.getOutTime();

        TimeDifferenceCalculation differenceCalculation = new TimeDifferenceCalculation();
        double duration = differenceCalculation.timeDifferenceCalculation(outHour, inHour);

        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
                price = ticket.getPrice();
                RecurrentUserProcess recurrentUser = new RecurrentUserProcess();
                recurrentUser.outGoingRecurrentUser(ticket.getVehicleRegNumber(), price);
                break;
            }
            case BIKE: {
                ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
                price = ticket.getPrice();
                RecurrentUserProcess recurrentUser = new RecurrentUserProcess();
                recurrentUser.outGoingRecurrentUser(ticket.getVehicleRegNumber(), price);
                break;
            }
            default: throw new IllegalArgumentException("Unknown Parking Type");
        }
    }
}