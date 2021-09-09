package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.config.DataBaseConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class RecurrentUserProcess {

    private static final Logger logger = LogManager.getLogger("RecurrentUserProcess");
    private int recurrentUser = 0;
    private boolean result = false;
    private String queryRecurrentUser = "SELECT VEHICLE_REG_NUMBER FROM test.ticket WHERE VEHICLE_REG_NUMBER=?";

    private DataBaseConfig dataBaseConfig = new DataBaseConfig();

    public boolean incomingRecurrentUser(String vehicleRegNumber){
        Connection connection = null;

        try{
            connection = dataBaseConfig.getConnection();

            PreparedStatement ps = connection.prepareStatement(queryRecurrentUser);
            ps.setString(1, vehicleRegNumber);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                recurrentUser++;
            }

            if(recurrentUser >= 2){
                result = true;
                System.out.println("Welcome back ! As a recurring user of our parking lot you'll benefit from a 5% discount.");
            }

        } catch(Exception errorRecurrentUser){
            logger.error("Error looking for recurrent user");
        } finally{
            dataBaseConfig.closeConnection(connection);
        }

        return result;
    }

    public double outGoingRecurrentUser(String vehicleRegNumber, Double price){
        Connection connection = null;

        try{
            connection = dataBaseConfig.getConnection();

            PreparedStatement psRecurrentUser = connection.prepareStatement(queryRecurrentUser);
            psRecurrentUser.setString(1, vehicleRegNumber);

            ResultSet rsRecurrentUser = psRecurrentUser.executeQuery();

            while(rsRecurrentUser.next()){
                recurrentUser++;
            }

            if(recurrentUser >= 2){
                price = price - (price*5)/100;
            }

        } catch(Exception errorRecurrentUser){
            logger.error("Error looking for recurrent user");
        } finally{
            dataBaseConfig.closeConnection(connection);
        }

        return price;
    }
}

