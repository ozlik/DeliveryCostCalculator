package shilova;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static shilova.CargoDimensions.SMALL;


class PriceCalculatorTest {
    PriceCalculator calculator;

    @BeforeEach
    void init() {
        calculator = new PriceCalculator();
    }

    @ParameterizedTest
    @MethodSource("parametersAfter30")
    @DisplayName("должен отдавать правильный коэффициент на расстояние больше 30 км")
    void shouldGetRightDimensionsCoefficient300(Double km) {
        Integer expectedCoefficient = 300;

        assertEquals(expectedCoefficient, calculator.calculateDistanceCoefficient(km),
                "коэффициент должен быть равен 300 " + km);
    }

    @ParameterizedTest
    @MethodSource("parametersBefore30")
    @DisplayName("должен отдавать правильный коэффициент на расстояние меньше 30 км")
    void shouldGetRightDimensionsCoefficient200(Double km) {
        Integer expectedCoefficient = 200;

        assertEquals(expectedCoefficient, calculator.calculateDistanceCoefficient(km),
                "коэффициент должен быть равен 300 " + km);
    }

    @ParameterizedTest
    @MethodSource("parametersBefore10")
    @DisplayName("должен отдавать правильный коэффициент на расстояние меньше 10 км")
    void shouldGetRightDimensionsCoefficient100(Double km) {
        Integer expectedCoefficient = 100;

        assertEquals(expectedCoefficient, calculator.calculateDistanceCoefficient(km),
                "коэффициент должен быть равен 100 " + km);
    }

    @ParameterizedTest
    @MethodSource("parametersBefore2")
    @DisplayName("должен отдавать правильный коэффициент на расстояние меньше 2 км")
    void shouldGetRightDimensionsCoefficient50(Double km) {
        Integer expectedCoefficient = 50;

        assertEquals(expectedCoefficient, calculator.calculateDistanceCoefficient(km),
                "коэффициент должен быть равен 50 " + km);
    }

    @ParameterizedTest
    @MethodSource("parametersThrows")
    @DisplayName("должен отдавать ошибку при неверном значении расстояния")
    void shouldGetThrowWrongDistanse(Double km) {

        assertThrows(Exception.class, () -> calculator.calculateDistanceCoefficient(km),
                "должна выдаваться ошибка " + km);
    }

    @Test
    @DisplayName("должен верно рассчитывать коэффициент хрупкости")
    void shouldGetFragilityCoefficientTrue() {
        Boolean fragility = true;
        assertEquals(300, calculator.calculateFragilityCoefficient(fragility), "коэффициент должен быть равен 300");
    }

    @Test
    @DisplayName("должен верно рассчитывать коэффициент хрупкости")
    void shouldGetFragilityCoefficientFalse() {
        Boolean fragility = false;
        assertEquals(0, calculator.calculateFragilityCoefficient(fragility), "коэффициент должен быть равен 0");
    }

    @Test
    @DisplayName("должен выдавать ошибку при попытки отправки хрупкого груза на более чем 30 км")
    void shouldGetFragilityCoefficientThrow() {
        assertThrows(FragilityException.class, () -> calculator.calculatePrice(40.0, SMALL, true, ServiceWorkload.REGULAR_LOAD),
                "должна выдаваться ошибка ");
    }

    @Test
    @DisplayName("должен верно рассчитывать коэффициент на габариты груза")
    void shouldGetDimensionsCoefficientBig() {
        assertEquals(200, calculator.calculateDimensionsCoefficient(CargoDimensions.BIG), "коэффициент должен быть равен 200");
    }

    @Test
    @DisplayName("должен верно рассчитывать коэффициент на габариты груза")
    void shouldGetDimensionsCoefficientSmall() {
        assertEquals(100, calculator.calculateDimensionsCoefficient(SMALL), "коэффициент должен быть равен 100");
    }

    @Test
    @DisplayName("должен верно рассчитывать коэффициент на рабочую загрузку")
    void shouldGetWorkloadCoefficient() {
        Double regular = calculator.calculateWorkloadCoefficient(ServiceWorkload.REGULAR_LOAD);
        Double high = calculator.calculateWorkloadCoefficient(ServiceWorkload.HIGH_LOAD);
        Double higher = calculator.calculateWorkloadCoefficient(ServiceWorkload.HIGHER_LOAD);
        Double highest = calculator.calculateWorkloadCoefficient(ServiceWorkload.HIGHEST_LOAD);

        assertEquals(1, regular, "коэффициент должен быть равен 1");
        assertEquals(1.2, high, "коэффициент должен быть равен 1.2");
        assertEquals(1.4, higher, "коэффициент должен быть равен 1.4");
        assertEquals(1.6, highest, "коэффициент должен быть равен 1.6");
    }

    @Tag("smoke")
    @ParameterizedTest
    @CsvSource({"1.0, SMALL, false, REGULAR_LOAD"})
    @DisplayName("должен подставлять минимальную цену доставки, если по расчету меньше 400 рублей")
    void shouldCalculateDeliveryMinPrice(Double distanceKm, CargoDimensions dimensions, Boolean fragility, ServiceWorkload workload) {
        Double deliveryPrice = calculator.calculatePrice(distanceKm, dimensions, fragility, workload);

        assertEquals(400, deliveryPrice, "должна быть минимальная цена");
    }

    @Tag("smoke")
    @ParameterizedTest
    @CsvSource({"50.0, BIG, false, HIGHEST_LOAD"})
    @DisplayName("должен определять цену доставки")
    void shouldCalculateDeliveryPrice(Double distanceKm, CargoDimensions dimensions, Boolean fragility, ServiceWorkload workload) {
        Double deliveryPrice = calculator.calculatePrice(distanceKm, dimensions, fragility, workload);

        assertEquals(800, deliveryPrice, "должна быть минимальная цена");
    }

    static Stream<Double> parametersAfter30() {
        return Stream.of(30.1, 31.0, 500.0);
    }

    static Stream<Double> parametersBefore30() {
        return Stream.of(30.0, 29.0, 20.0, 11.0, 10.1);
    }

    static Stream<Double> parametersBefore10() {
        return Stream.of(10.0, 9.0, 3.0, 5.0, 2.1);
    }

    static Stream<Double> parametersBefore2() {
        return Stream.of(2.0, 1.0, 0.1);
    }

    static Stream<Double> parametersThrows() {
        return Stream.of(-1.0, 0.0, -500.0, -0.1);
    }


}