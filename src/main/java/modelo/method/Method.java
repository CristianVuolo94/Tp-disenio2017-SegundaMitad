package modelo.method;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import org.jooq.lambda.Seq;

import exceptions.EmptyFieldException;
import modelo.db.ModelEntity;
import modelo.enterprise.Enterprise;
import modelo.method.criteria.FilterCriterion;
import modelo.method.criteria.MixedCriterion;
import modelo.method.criteria.OrderCriterion;
import modelo.method.criteria.order.Unordered;
import modelo.method.result.Pass;
import modelo.method.result.Result;

@Entity
@Table(name = "Methods")
public class Method extends ModelEntity{

	@Column(nullable = false, unique = true)
	private String name;
	
	@OneToMany(	cascade = CascadeType.ALL,
				orphanRemoval = true,
				fetch = FetchType.LAZY)
	@JoinColumn(name = "method_id", referencedColumnName = "id")
	private List<FilterCriterion> filterCriteria;
	
	@OneToMany(	cascade = CascadeType.ALL,
				orphanRemoval = true,
				fetch = FetchType.LAZY)
	@JoinColumn(name = "method_id", referencedColumnName = "id")
	@OrderColumn
	private List<OrderCriterion> orderCriteria;
	
	@OneToMany(	cascade = CascadeType.ALL,
				orphanRemoval = true,
				fetch = FetchType.LAZY)
	@JoinColumn(name = "method_id", referencedColumnName = "id")
	private List<MixedCriterion> mixedCriteria;

	public Method(String name, List<FilterCriterion> filterCriteria, List<OrderCriterion> orderCriteria, List<MixedCriterion> mixedCriteria)
	{
		if(name == null) throw new EmptyFieldException("nombre");
		this.name = name;
		this.filterCriteria = filterCriteria;
		this.orderCriteria = orderCriteria;
		this.mixedCriteria = mixedCriteria;
	}
	
	public Method(String name, List<FilterCriterion> filterCriteria, List<OrderCriterion> orderCriteria, List<MixedCriterion> mixedCriteria, Long id)
	{
		this(name, filterCriteria, orderCriteria, mixedCriteria);
		this.id = id;
	}
	
	protected Method(){}

	public String getName()
	{
		return name;
	}
	
	public List<FilterCriterion> getFilterCriteria() 
	{
		return filterCriteria;
	}
	
	public List<OrderCriterion> getOrderCriteria() 
	{
		return orderCriteria;
	}
	
	public List<MixedCriterion> getMixedCriteria() {
		return mixedCriteria;
	}
	
	private Comparator<Enterprise> buildComparator(){
		return Seq.seq(orderCriteria.stream())
				.foldLeft((Comparator<Enterprise>) new Unordered(), (seed, comp) -> seed.thenComparing(comp));
	}
	
	public List<Result> apply(List<Enterprise> enterprises){
		return enterprises.stream()
				.sorted(buildComparator())
				.map(enterprise -> 
					Seq.seq(filterCriteria.stream())
						.foldLeft((Result) new Pass(enterprise), (seed, filterCriterion) -> 
							seed.eval(filterCriterion)))
				.collect(Collectors.toList());
	}
	
	public String getUrl(){
		return "methods/" + id;
	}
}
