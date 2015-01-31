package gis.orm;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "inventories")
public class Inventories implements Serializable
{
	private Long id;
	private String name;

	private BigDecimal value;
	private Date commissioning;
	private BigDecimal lifetime;
	private Date dateupdate;

	// ---------references---------
	private Person person;
	private Division division;
	// private Set<RouteItem> routeItems = new HashSet<RouteItem>();

	private Gisgraphs gisgraph;

	// ---------flags---------
	private BigDecimal magistral;
	private BigDecimal boiler;

	// ---------getters-----------

	@ManyToOne
	@JoinColumn(name = "division")
	public Division getDivision()
	{
		return division;
	}

	@ManyToOne
	@JoinColumn(name = "person")
	public Person getPerson()
	{
		return person;
	}

	@OneToOne(mappedBy = "inventory")
	public Gisgraphs getGisgraph()
	{
		return gisgraph;
	}

	@Id
	@Column(name = "id")
	public Long getId()
	{
		return id;
	}

	@Column(name = "name")
	public String getName()
	{
		return name;
	}

	@Column(name = "value")
	public BigDecimal getValue()
	{
		return value;
	}

	@Column(name = "commissioning")
	public Date getCommissioning()
	{
		return commissioning;
	}

	@Column(name = "lifetime")
	public BigDecimal getLifetime()
	{
		return lifetime;
	}

	@Column(name = "dateupdate")
	public Date getDateupdate()
	{
		return dateupdate;
	}

	@Column(name = "magistral")
	public BigDecimal getMagistral()
	{
		return magistral;
	}

	@Column(name = "boiler")
	public BigDecimal getBoiler()
	{
		return boiler;
	}

	// ---------setters--------------

	public void setGisgraph(Gisgraphs gisgraph)
	{
		this.gisgraph = gisgraph;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setValue(BigDecimal value)
	{
		this.value = value;
	}

	public void setCommissioning(Date commissioning)
	{
		this.commissioning = commissioning;
	}

	public void setLifetime(BigDecimal lifetime)
	{
		this.lifetime = lifetime;
	}

	public void setDateupdate(Date dateupdate)
	{
		this.dateupdate = dateupdate;
	}

	public void setPerson(Person person)
	{
		this.person = person;
	}

	public void setDivision(Division division)
	{
		this.division = division;
	}

	public void setMagistral(BigDecimal magistral)
	{
		this.magistral = magistral;
	}

	public void setBoiler(BigDecimal boiler)
	{
		this.boiler = boiler;
	}

	// ---------constructors--------------

	public Inventories()
	{
	}
}
