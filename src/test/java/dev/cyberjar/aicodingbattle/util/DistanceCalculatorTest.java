package dev.cyberjar.aicodingbattle.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DistanceCalculatorTest {

    @Test
    void returnsZeroForSameCoordinates() {
        double distance = DistanceCalculator.calculateDistanceInKilometers(48.137154D, 11.576124D, 48.137154D, 11.576124D);

        assertThat(distance).isZero();
    }

    @Test
    void calculatesApproximateKilometersUsingEuclideanDistance() {
        double distance = DistanceCalculator.calculateDistanceInKilometers(48.137154D, 11.576124D, 48.147154D, 11.586124D);

        assertThat(distance).isCloseTo(1.57D, org.assertj.core.data.Offset.offset(0.05D));
    }
}
