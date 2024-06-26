//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-558 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.09.19 at 03:18:45 PM MESZ 
//


package playground.vsp.gexf;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for timeformat-type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="timeformat-type">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="integer"/>
 *     &lt;enumeration value="double"/>
 *     &lt;enumeration value="date"/>
 *     &lt;enumeration value="dateTime"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "timeformat-type")
@XmlEnum
public enum XMLTimeformatType {

    @XmlEnumValue("integer")
    INTEGER("integer"),
    @XmlEnumValue("double")
    DOUBLE("double"),
    @XmlEnumValue("date")
    DATE("date"),
    @XmlEnumValue("dateTime")
    DATE_TIME("dateTime");
    private final String value;

    XMLTimeformatType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static XMLTimeformatType fromValue(String v) {
        for (XMLTimeformatType c: XMLTimeformatType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
