package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
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
public class RecurrentUserProcessITTest {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;

    @Mock
    private static FareCalculatorService fareCalculatorService;

    @BeforeAll
    private static void setUp() throws Exception{
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }

    @Test
    public void givenAVehicleRegNumber_WhenAUserIsRecurrent_InvokeAPrintLnMessage(){
        //GIVEN
        Ticket ticket = new Ticket();
        fareCalculatorService = new FareCalculatorService();
        double price = 0.0;
        Date inTime = new Date();
        inTime.setTime(System.currentTimeMillis() - ( 60 * 60 * 1000));
        Date outTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(2, ParkingType.CAR, false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");
        ticket.setPrice(price);
        ticketDAO.saveTicket(ticket);

        //WHEN
        RecurrentUserProcess recurrentUserProcess = new RecurrentUserProcess();
        boolean result = recurrentUserProcess.incomingRecurrentUser(ticket.getVehicleRegNumber());

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
        ParkingSpot parkingSpot = new ParkingSpot(2, ParkingType.CAR, false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");
        ticket.setPrice(price);
        ticketDAO.saveTicket(ticket);

        //WHEN
        fareCalculatorService.calculateFare(ticket);
        price = ticket.getPrice();

        RecurrentUserProcess recurrentUserProcess = new RecurrentUserProcess();
        price = recurrentUserProcess.outGoingRecurrentUser(ticket.getVehicleRegNumber(), price);

        //THEN
        Assertions.assertEquals(1.425, price);
    }
}
