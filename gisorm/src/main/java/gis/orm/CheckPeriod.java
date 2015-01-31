package gis.orm;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "checkperiod")
public class CheckPeriod implements Serializable
{
	private Long id;
	private BigDecimal period;

	// ---------getters--------------

	@Id
	@Column(name = "id")
	public Long getId()
	{
		return id;
	}

	@Column(name = "period")
	public BigDecimal getPeriod()
	{
		return period;
	}

	// ---------setters--------------

	public void setId(Long id)
	{
		this.id = id;
	}

	public void setPeriod(BigDecimal period)
	{
		this.period = period;
	}

	// ---------constructors--------------

	public CheckPeriod()
	{
	}
}
