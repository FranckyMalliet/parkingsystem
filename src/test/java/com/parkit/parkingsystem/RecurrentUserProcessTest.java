package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import com.parkit.parkingsystem.service.RecurrentUserProcess;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;


@ExtendWith(MockitoExtension.class)
public class RecurrentUserProcessTest {

    @Mock
    private static FareCalculatorService fareCalculatorService;

    @Test
    public void givenAVehicleRegNumber_WhenAUserIsRecurrent_InvokeAPrintLnMessage(){
        //GIVEN
        String vehicleRegNumber = "ABCDEF";

        //WHEN
        RecurrentUserProcess recurrentUserProcess = new RecurrentUserProcess();
        boolean result = recurrentUserProcess.incomingRecurrentUser(vehicleRegNumber);

        //THEN
        Assertions.assertTrue(result);
    }

    @Test
    public void givenAVehicleRegNumber_WhenAUserIsRecurrent_GiveHim5PercentReductionFee(){
        //GIVEN
        Ticket ticket = new Ticket();
        fareCalculatorService = new FareCalculatorService();
        double price = 0.0;
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - ( 60 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");

        //WHEN
        fareCalculatorService.calculateFare(ticket);
        price = ticket.getPrice();

        RecurrentUserProcess recurrentUserProcess = new RecurrentUserProcess();
        price = recurrentUserProcess.outGoingRecurrentUser(ticket.getVehicleRegNumber(), price);

        //THEN
        Assertions.assertEquals(1.425, price);
    }
}
