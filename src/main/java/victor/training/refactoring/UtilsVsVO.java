package victor.training.refactoring;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UtilsVsVO {

    public static void main(String[] args) {
        CarSearchCriteria criteria = new CarSearchCriteria(2006, 2016, "Ford");
        new UtilsVsVO().filterCarModels(criteria, Arrays.asList(
            new CarModel("Ford", "Focus", 2008, 2018)
        ));
    }

    public List<CarModel> filterCarModels(CarSearchCriteria criteria, List<CarModel> models) {
        Interval criteriaInterval = new Interval(criteria.getStartYear(), criteria.getEndYear());


        List<CarModel> results = new ArrayList<>(models);
        results.removeIf(model -> !MathUtil.intervalsIntersect(new Interval(model.getStartYear(), model.getEndYear()),
            criteriaInterval));

        System.out.println("More filtering logic");
        return results;
    }
}

class Interval {
    private final int start;
    private final int end;

    public Interval(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }
}

class MathUtil {

    public static boolean intervalsIntersect(Interval interval1, Interval interval2) {
        return interval1.getStart() <= interval2.getEnd() &&
               interval2.getStart() <= interval1.getEnd();
    }
//    public static boolean intervalsIntersect(Interval interval1, Interval interval2) {
}






class CarSearchCriteria {
    private final int startYear;
    private final int endYear;
    private final String make;

    public CarSearchCriteria(int startYear, int endYear, String make) {
        this.make = make;
        if (startYear > endYear) throw new IllegalArgumentException("start larger than end");
        this.startYear = startYear;
        this.endYear = endYear;
    }

    public int getStartYear() {
        return startYear;
    }

    public int getEndYear() {
        return endYear;
    }

    public String getMake() {
        return make;
    }
}

class CarModel {
    private final String make;
    private final String model;
    private final int startYear;
    private final int endYear;

    public CarModel(String make, String model, int startYear, int endYear) {
        this.make = make;
        this.model = model;
        if (startYear > endYear) throw new IllegalArgumentException("start larger than end");
        this.startYear = startYear;
        this.endYear = endYear;
    }

    public int getEndYear() {
        return endYear;
    }

    public int getStartYear() {
        return startYear;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }
}