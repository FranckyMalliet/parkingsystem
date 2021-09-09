package com.parkit.parkingsystem;

import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Calendar;
import java.util.Date;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseTest {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;
    private static FareCalculatorService fareCalculatorService = new FareCalculatorService();

    private static String vehiculeRegNumber = "ABCDEF";

    @Mock
    private static InputReaderUtil inputReaderUtil;

    @BeforeAll
    private static void setUp() throws Exception{
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    private void setUpPerTest() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn(vehiculeRegNumber);
        dataBasePrepareService.clearDataBaseEntries();
    }

    @Test
    public void testParkingACar(){
        //GIVEN
        Ticket ticket;
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle();

        //WHEN
        ticketDAO.getTicket(vehiculeRegNumber);

        ticket = ticketDAO.getTicket(vehiculeRegNumber);
        ParkingSpot parkingSpot = ticket.getParkingSpot();
        parkingSpotDAO.updateParking(parkingSpot);

        //THEN
        Assertions.assertNotNull(ticket);
        Assertions.assertFalse(parkingSpot.isAvailable());

        //TODO: check that a ticket is actualy saved in DB and Parking table is updated with availability
    }

    @Test
    public void testParkingLotExitAfterOneHOur(){
        //GIVEN
        Ticket ticket;

        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle();

        parkingService.processExitingVehicle();

        //WHEN
        ticket = ticketDAO.getTicket(vehiculeRegNumber);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, +1);
        ticket.setOutTime(calendar.getTime());

        Date checkOutTime = ticket.getOutTime();

        fareCalculatorService.calculateFare(ticket);
        double farePrice = ticket.getPrice();

        //THEN
        Assertions.assertNotNull(checkOutTime);
        Assertions.assertTrue(farePrice > 0);
        //TODO: check that the fare generated and out time are populated correctly in the database
    }
}