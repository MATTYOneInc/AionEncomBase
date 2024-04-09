package com.aionemu.gameserver.model.templates.minion;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MinionAttribute", propOrder = {"physicalAttr"})
public class MinionAttribute {
    @XmlElement(name = "physical_attr")
    protected List<MinionStatAttribute> physicalAttr;

    @XmlElement(name = "magical_attr")
    protected List<MinionStatAttribute> magicalAttr;


    public List<MinionStatAttribute> getPyhsicalAttr() {
        if (physicalAttr == null) {
            physicalAttr = new ArrayList<>();
        }
        return physicalAttr;
    }

    public List<MinionStatAttribute> getMagicalAttr() {
        if (magicalAttr == null) {
            magicalAttr = new ArrayList<>();
        }
        return magicalAttr;
    }
}
