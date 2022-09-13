package com.github.coryrobertson.serverinfo;

import com.github.coryrobertson.Logger.LogLevels;
import com.github.coryrobertson.Logger.Logger;

import java.io.Serializable;
import java.util.Date;

public record ServerInfoPacket(Date date, double CPU_LOAD, long RAM_USAGE, long TOTAL_RAM, double TOTAL_SPACE, double FREE_SPACE) implements Serializable
{
    public ServerInfoPacket
    {

        // massive list of validity checking when creating ServerInfoPacket Record
        if(CPU_LOAD < 0)
        {
            final String error = "CPU usage can not be negative.";
            Logger.log(error, LogLevels.ERROR);
            throw new IllegalArgumentException(error);
        }

        if(RAM_USAGE < 0)
        {
            final String error = "RAM usage can not be negative.";
            Logger.log(error, LogLevels.ERROR);
            throw new IllegalArgumentException(error);
        }

        if(TOTAL_SPACE < 0)
        {
            final String error = "TOTAL_SPACE can not be negative.";
            Logger.log(error, LogLevels.ERROR);
            throw new IllegalArgumentException(error);
        }

        if(FREE_SPACE < 0)
        {
            final String error = "FREE_SPACE can not be negative.";
            Logger.log(error, LogLevels.ERROR);
            throw new IllegalArgumentException(error);
        }

        if(TOTAL_RAM < 0)
        {
            final String error = "TOTAL_RAM can not be negative.";
            Logger.log(error, LogLevels.ERROR);
            throw new IllegalArgumentException(error);
        }
    }
}
