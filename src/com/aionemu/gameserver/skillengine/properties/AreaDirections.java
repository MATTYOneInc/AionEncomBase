package com.aionemu.gameserver.skillengine.properties;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "directions")
@XmlEnum
public enum AreaDirections {

    NONE,
    FRONT,
    BACK
}
