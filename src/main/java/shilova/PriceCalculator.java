package shilova;

public class PriceCalculator {
    private static final int MIN_COST_RUBLE = 400;


    public Double calculatePrice(Double distanceKm, CargoDimensions dimensions, Boolean fragility, ServiceWorkload workload) {
       if (distanceKm >30 && fragility) {
           throw new FragilityException ("Хрупкие грузы переводят только на расстояние до 30 км. Вы хотите перевезти хрупкий груз на " + distanceKm);
       }
        Double deliveryPrice = (calculateDistanceCoefficient(distanceKm) + calculateDimensionsCoefficient(dimensions) + calculateFragilityCoefficient(fragility)) * calculateWorkloadCoefficient(workload);
        if (deliveryPrice < MIN_COST_RUBLE) {
            return (double) MIN_COST_RUBLE;
        }
        return deliveryPrice;
    }

    public Integer calculateDistanceCoefficient(Double distanceKm) {
        if (distanceKm > 30) {
            return 300;
        } else if (distanceKm <= 30 && distanceKm > 10) {
            return 200;
        } else if (distanceKm <= 2 && distanceKm > 0) {
            return 50;
        } else if (distanceKm <= 10 && distanceKm > 2) {
            return 100;
        }
        else {
            throw new IllegalArgumentException("Unknown value: " + distanceKm);
        }
    }

    public Double calculateWorkloadCoefficient(ServiceWorkload workload) {
        return switch (workload) {
            case HIGHEST_LOAD -> 1.6;
            case HIGHER_LOAD -> 1.4;
            case HIGH_LOAD -> 1.2;
            case REGULAR_LOAD -> 1.0;
            default -> throw new IllegalArgumentException("Unknown workload value: " + workload);
        };
    }

    public Integer calculateDimensionsCoefficient(CargoDimensions dimensions) {
        return switch (dimensions) {
            case BIG -> 200;
            case SMALL -> 100;
            default -> throw new IllegalArgumentException("Unknown dimensions value: " + dimensions);
        };
    }

    public Integer calculateFragilityCoefficient(Boolean fragility) {
        if (fragility) {
            return 300;
        } else {
            return 0;
        }
    }
}
