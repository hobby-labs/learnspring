package com.github.hobbylabs.rest.restxml.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.List;

/**
 * This will create elements like below.
 *   <Country id=1>
 *       <citires></>
 *   </Country>
 */
@Data
@JacksonXmlRootElement
public class Country implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JacksonXmlProperty(isAttribute = true)
    private int id;

    /**
     * This will create elements like below.
     *   <cities><city>...</city><city>...</city>......</cities>
     */
    @JacksonXmlElementWrapper(localName = "Cities")
    @JacksonXmlProperty(localName = "City")
    private List<City> cities;
}
