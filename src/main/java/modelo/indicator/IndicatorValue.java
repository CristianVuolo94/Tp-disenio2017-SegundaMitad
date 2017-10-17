package modelo.indicator;

import java.util.List;

import modelo.enterprise.EnterpriseRepository;



public class IndicatorValue {

	private String name;	
	private String value;
	
	public IndicatorValue(String name,int year,String enterpriseName) 
	{		
		this.name = name;		
		try
		{
			value=IndicatorRepository.getInstance().getIndicator(name).reduce(EnterpriseRepository.getInstance().getEnterprise(enterpriseName), year).toString();
		}
		catch(Exception e)
		{
			value="Indicador no disponible";
		}
		}
	public String getName() 
	{
		return name;
	}
	public String getValue() 
	{
		return value;
	}
	
	
}
