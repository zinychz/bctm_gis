package gis.orm;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "boilerproperty")
public class BoilerProperty implements Serializable
{
	private Long id;

	private BigDecimal heating;
	private BigDecimal capacity;
	private String remark;

	// ---------references--------------

	private BoilerType boilerType;

	// ---------getters--------------

	@ManyToOne
	@JoinColumn(name = "boilertypeid")
	public BoilerType getBoilerType()
	{
		return boilerType;
	}

	@Id
	@Column(name = "id")
	public Long getId()
	{
		return id;
	}

	@Column(name = "heating")
	public BigDecimal getHeating()
	{
		return heating;
	}

	@Column(name = "capacity")
	public BigDecimal getCapacity()
	{
		return capacity;
	}

	@Column(name = "remark")
	@Lob
	public String getRemark()
	{
		return remark;
	}

	// ---------setters--------------

	public void setId(Long id)
	{
		this.id = id;
	}

	public void setHeating(BigDecimal heating)
	{
		this.heating = heating;
	}

	public void setCapacity(BigDecimal capacity)
	{
		this.capacity = capacity;
	}

	public void setRemark(String remark)
	{
		this.remark = remark;
	}

	public void setBoilerType(BoilerType boilerType)
	{
		this.boilerType = boilerType;
	}

	// ---------constructors--------------

	public BoilerProperty()
	{
	}
}
