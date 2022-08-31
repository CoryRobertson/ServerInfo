package com.github.coryrobertson.serverinfo;

import java.io.Serializable;
import java.util.Date;

public record ServerInfoPacket(Date date) implements Serializable {
}
