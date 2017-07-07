package modelo.method.criteria.filter;

import java.math.BigDecimal;

import modelo.indicator.Indicator;

public class IndicatorValueLessThanCriterion extends IndicatorValueCompare
{

	public IndicatorValueLessThanCriterion(String name, Indicator indicator, BigDecimal value, int years) 
	{
		super(name, indicator, value, years);
	}

	protected boolean compare(int result){
		return result < 0;
	}

}