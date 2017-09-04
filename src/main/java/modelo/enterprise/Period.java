package modelo.enterprise;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import exceptions.MissingCalculationInPeriodException;

import modelo.ModelEntity;

@Entity
public class Period extends ModelEntity
{	
	@Column(nullable = false)
	private int year;
	
	@OneToMany
	@JoinColumn(referencedColumnName = "CalculationId")			//Un periodo tiene muchas cuentas, pero cada cuenta pertenece solo a un periodo		
	private List<Calculation> calculations;						//(porque cada cuenta tiene un valor distinto, aunque varias tengan el mismo nombre)
	
	public Period(int year, List<Calculation> calculations)
	{
		this.year = year;
		this.calculations = calculations;
	}
	
	public List<Calculation> getCalculations()
	{
		return calculations;
	}
	
	public int getYear() {
		return year;
	}
	
	public Calculation getCalculation(String name){
		return calculations.stream()
				.filter(calculation -> calculation.getName().equals(name))
				.findFirst()
				.orElseThrow(() -> new MissingCalculationInPeriodException(name, this));
	}
	
	@Override
	public String toString(){
		return String.valueOf(year);
	}
}
