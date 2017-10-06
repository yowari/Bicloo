package com.beapp.yowari.bicloo.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.*;

public class StationComparatorTest {

    public static class AlphabeticalComparatorTest {

        @Test
        public void shouldLesser() {
            Comparator<Station> comparator = new Station.AlphabeticalComparator();

            Station stationA = new Station(0, "testA", "test address", 0, 0, true, 0, 0, 0);
            Station stationB = new Station(0, "testB", "test address", 0, 0, true, 0, 0, 0);

            assertEquals(-1, comparator.compare(stationA, stationB));
        }

        @Test
        public void shouldBeGreater() {
            Comparator<Station> comparator = new Station.AlphabeticalComparator();

            Station stationA = new Station(0, "testB", "test address", 0, 0, true, 0, 0, 0);
            Station stationB = new Station(0, "testA", "test address", 0, 0, true, 0, 0, 0);

            assertEquals(1, comparator.compare(stationA, stationB));
        }

        @Test
        public void shouldBeEqual() {
            Comparator<Station> comparator = new Station.AlphabeticalComparator();

            Station stationA = new Station(0, "test", "test address", 0, 0, true, 0, 0, 0);
            Station stationB = new Station(0, "test", "test address", 0, 0, true, 0, 0, 0);

            assertEquals(0, comparator.compare(stationA, stationB));
        }

    }

    public static class LocationComparatorTest {

        @Test
        public void shouldLesser() {
            Comparator<Station> comparator = new Station.LocationComparator(0, 0);

            Station stationA = new Station(0, "test", "test address", 0, 0, true, 0, 0, 0);
            Station stationB = new Station(0, "test", "test address", 10, 10, true, 0, 0, 0);

            assertEquals(-1, comparator.compare(stationA, stationB));
        }

        @Test
        public void shouldBeGreater() {
            Comparator<Station> comparator = new Station.LocationComparator(0, 0);

            Station stationA = new Station(0, "test", "test address", 10, 10, true, 0, 0, 0);
            Station stationB = new Station(0, "test", "test address", 0, 0, true, 0, 0, 0);

            assertEquals(1, comparator.compare(stationA, stationB));
        }

        @Test
        public void shouldBeEqual() {
            Comparator<Station> comparator = new Station.LocationComparator(0, 0);

            Station stationA = new Station(0, "test", "test address", 10, 10, true, 0, 0, 0);
            Station stationB = new Station(0, "test", "test address", 10, 10, true, 0, 0, 0);

            assertEquals(0, comparator.compare(stationA, stationB));
        }

    }

}
