package com.github.coryrobertson.serverinfo;

import java.io.Serializable;
import java.util.Date;

public record ServerInfoPacket(Date date, double CPU_LOAD, long RAM_USAGE, double TOTAL_SPACE, double FREE_SPACE) implements Serializable
{
    public ServerInfoPacket
    {
        if(CPU_LOAD < 0)
        {
            throw new IllegalArgumentException("CPU usage can not be negative.");
        }

        if(RAM_USAGE < 0)
        {
            throw new IllegalArgumentException("RAM usage can not be negative.");
        }

        if(TOTAL_SPACE < 0)
        {
            throw new IllegalArgumentException("TOTAL_SPACE can not be negative.");
        }

        if(FREE_SPACE < 0)
        {
            throw new IllegalArgumentException("FREE_SPACE can not be negative.");
        }
    }
}
