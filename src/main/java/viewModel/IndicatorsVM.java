package viewModel;

import java.util.List;

import org.uqbar.commons.utils.Observable;

import modelo.Indicator;
import modelo.IndicatorRepository;

@Observable
public class IndicatorsVM {
	
	private List<Indicator> indicators;
	private Indicator selectedIndicator;
	
	public IndicatorsVM(){
		indicators = IndicatorRepository.getIndicatorList();
	}
	
	public List<Indicator> getIndicators() {
		return indicators;
	}
	public void setIndicators(List<Indicator> indicators) {
		this.indicators = indicators;
	}
	public Indicator getSelectedIndicator() {
		return selectedIndicator;
	}
	public void setSelectedIndicator(Indicator selectedIndicator) {
		this.selectedIndicator = selectedIndicator;
	}
	
	public Indicator newIndicator(){
		selectedIndicator = new Indicator(null, null, null);
		return selectedIndicator;
	}
	
	public Indicator editIndicator(){
		return selectedIndicator;
	}

	public void deleteIndicator(){
		indicators.remove(selectedIndicator);
	}
	
	public void addNewIndicator(){
		IndicatorRepository.addIndicator(selectedIndicator);
	}
}