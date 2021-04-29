package com.napier.sem;

import com.napier.sem.objects.City;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

public class CityQueriesTest {

    private static App app;

    @BeforeClass
    public static void setup() {
        app = new App();
        app.connect("localhost:33060");
    }

    @Test(expected = IllegalArgumentException.class)
    public void getCitiesInDistrictByLargestToSmallestPopulationInvalidInput() {
        List<City> query = app.getCitiesInDistrictDescending(null);
    }
}
