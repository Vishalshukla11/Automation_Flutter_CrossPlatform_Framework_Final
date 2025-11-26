package com.briskpe.smeportal.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.Map;

public class ElementLocatorReader {

    private static Map<String, String> locatorMap;

    static {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream inputStream = ElementLocatorReader.class
                    .getClassLoader()
                    .getResourceAsStream("element-locators.json");

            if (inputStream == null) {
                throw new RuntimeException("Could not find 'element-locators.json'");
            }

            locatorMap = mapper.readValue(inputStream, Map.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load element locators JSON");
        }
    }

    public static String getLocatorKey(String elementName) {
        String value = locatorMap.get(elementName);
        if (value == null) {
            throw new IllegalArgumentException("No locator key found for element: " + elementName);
        }
        return value;
    }
}
