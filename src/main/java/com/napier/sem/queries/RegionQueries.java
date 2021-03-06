package com.napier.sem.queries;

import com.napier.sem.objects.Country;
import com.napier.sem.objects.Region;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RegionQueries {
    private Connection conn;

    public RegionQueries(Connection conn) {
        this.conn = conn;
    }

    /**
     * Gets top N populated countries in a region provided by the user.
     * @param region
     * @param number
     * @return list of countries
     */
    public List<Country> getTopNPopulatedCountriesInRegion(String region, int number) {
        List<Country> result = getRegionPopulationDescending(region);

        if(number >= result.size()) {
            throw new IllegalArgumentException("The provided number is invalid. The number of countries in that region is " + result.size());
        }

        return result.subList(0, number);
    }

    /**
     * Gets region population sorted in ascending order.
     *
     * @return list of countries
     */
    public List<Country> getRegionPopulationAscending(String region) {
        return getRegionPopulation(region);
    }


    /**
     * Gets region population sorted in descending order.
     *
     * @return list of countries
     */
    public List<Country> getRegionPopulationDescending(String region) {
        List<Country> result = getRegionPopulation(region);
        Collections.reverse(result);
        return result;
    }

    /**
     * Gets region population sorted in ascending order.
     *
     * @param region
     * @return
     */
    private List<Country> getRegionPopulation(String region) {
        if (region == null) {
            throw new IllegalArgumentException("You cannot pass null value as a region.");
        }

        List<Country> result = new ArrayList<>();
        try (Statement statement = conn.createStatement()) {
            statement.executeQuery("use world;");

            String query = "SELECT * " +
                    "FROM country " +
                    "WHERE region = ?" +
                    "ORDER BY population " +
                    "ASC;";

            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, region);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String countryName = resultSet.getString("name");
                String continent = resultSet.getString("continent");
                int population = resultSet.getInt("population");

                result.add(new Country(countryName, continent, region, population));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return result;
    }

    public Region getTotalPopulationOfRegion(String name)
    {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Region is null or empty");
        } else {
            try {
                Statement stmt = conn.createStatement();
                String query =
                        "SELECT region, SUM(population) as population "
                        + "FROM country "
                        + "WHERE region LIKE ? "
                        + "GROUP BY region";

                PreparedStatement preparedStatement = conn.prepareStatement(query);
                preparedStatement.setString(1, name);

                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    Region region = new Region();
                    region.setName(resultSet.getString("region"));
                    region.setPopulation(resultSet.getLong("population"));
                    return region;
                } else {
                    throw new Exception("Region not found");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Failed to get region population");
            }
            return null;
        }
    }
}
