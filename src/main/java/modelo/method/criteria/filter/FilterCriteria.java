package modelo.method.criteria.filter;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.jooq.lambda.Seq;
import org.jooq.lambda.tuple.Tuple2;

import modelo.enterprise.Enterprise;
import modelo.indicator.Indicator;
import modelo.method.criteria.FilterCriterion;

public abstract class FilterCriteria
{
	private static Function<Function<Integer, Boolean>, Function<Enterprise, Boolean>> indicatorValueCompare(Indicator indicator, BigDecimal value, int years){
		return compare -> {
			return enterprise -> {
				return enterprise.getIndicatorValueFromLastNYears(indicator, years).stream()
						.allMatch(_value -> compare.apply(_value.compareTo(value)));
			};
		};
	}
	
	private static Function<Function<Integer, Boolean>, Function<Enterprise, Boolean>> indicatorAverageCompare(Indicator indicator, BigDecimal value, int years){
		return compare -> {
			return enterprise -> {
				List<BigDecimal> values = enterprise.getIndicatorValueFromLastNYears(indicator, years);
				
				BigDecimal average = values.stream()
										.reduce(BigDecimal.ZERO, BigDecimal::add)
										.divide(new BigDecimal(values.size()));
				
				return compare.apply(average.compareTo(value));
			};
		};
	}
	
	private static Function<Function<Integer, Boolean>, Function<Enterprise, Boolean>> indicatorMedianCompare(Indicator indicator, BigDecimal value, int years){
		return compare -> {
			return enterprise -> {
				List<BigDecimal> values = enterprise.getIndicatorValueFromLastNYears(indicator, years).stream()
						.sorted()
						.collect(Collectors.toList());

				int middle = values.size() / 2;
				BigDecimal median = values.get(middle);
	
				if(values.size() % 2 == 0)
				median = median.add(values.get(middle-1)).divide(new BigDecimal(2));
	
				return compare.apply(median.compareTo(value));
			};
		};
	}
	
	private static Function<Function<Integer, Boolean>, Function<Enterprise, Boolean>> variatingIndicatorValue(Indicator indicator, int years){
		return compare -> {
			return enterprise -> {
				List<BigDecimal> values = enterprise.getIndicatorValueFromLastNYears(indicator, years);
				
				if(values.isEmpty()) return true;
				
				Tuple2<Boolean, BigDecimal> acumulador = new Tuple2<>(true, values.get(0));
				
				return Seq.seq(values).drop(1)
						.foldLeft(acumulador, (tuple, _value) -> new Tuple2<Boolean, BigDecimal>(tuple.v1 && compare.apply(tuple.v2.compareTo(_value)), _value))
						.v1;
			};
		};
	}
		
	private static Function<Integer, Boolean> HigherThan = result -> result > 0;
	private static Function<Integer, Boolean> LowerThan = result -> result < 0;
	
	public static FilterCriterion indicatorValueHigherThan(Indicator indicator, BigDecimal value, int years){
		return new FilterCriterion(indicatorValueCompare(indicator, value, years).apply(HigherThan));
	}
	
	public static FilterCriterion indicatorValueLowerThan(Indicator indicator, BigDecimal value, int years){
		return new FilterCriterion(indicatorValueCompare(indicator, value, years).apply(LowerThan));
	}
	
	public static FilterCriterion indicatorAverageHigherThan(Indicator indicator, BigDecimal value, int years){
		return new FilterCriterion(indicatorAverageCompare(indicator, value, years).apply(HigherThan));
	}
	
	public static FilterCriterion indicatorAverageLowerThan(Indicator indicator, BigDecimal value, int years){
		return new FilterCriterion(indicatorAverageCompare(indicator, value, years).apply(LowerThan));
	}
	
	public static FilterCriterion indicatorMedianHigherThan(Indicator indicator, BigDecimal value, int years){
		return new FilterCriterion(indicatorMedianCompare(indicator, value, years).apply(HigherThan));
	}
	
	public static FilterCriterion indicatorMedianLowerThan(Indicator indicator, BigDecimal value, int years){
		return new FilterCriterion(indicatorMedianCompare(indicator, value, years).apply(LowerThan));
	}
	
	public static FilterCriterion increasingIndicatorValue(Indicator indicator, int years){
		return new FilterCriterion(variatingIndicatorValue(indicator, years).apply(HigherThan));
	}
	
	public static FilterCriterion decreasingIndicatorValue(Indicator indicator, int years){
		return new FilterCriterion(variatingIndicatorValue(indicator, years).apply(LowerThan));
	}
	
	public static FilterCriterion minimumLongevity(int minimumAge){
		return new FilterCriterion(enterprise -> {
			return enterprise.age() > minimumAge;
		});
	}
}