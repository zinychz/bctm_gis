package gis.orm;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "boileritem")
public class BoilerItem implements Serializable
{
	private Long id;

	private Long invid;
	private BigDecimal quantity;
	private BigDecimal installPower;
	private String registrationNumber;
	private Date lastCheckDate;

	// ---------references--------------

	private Material material;
	private CheckPeriod checkPeriod;

	// ---------getters--------------

	@ManyToOne
	@JoinColumn(name = "checkperiodid")
	public CheckPeriod getCheckPeriod()
	{
		return checkPeriod;
	}

	@ManyToOne
	@JoinColumn(name = "materialid")
	public Material getMaterial()
	{
		return material;
	}

	@Id
	@Column(name = "id")
	public Long getId()
	{
		return id;
	}

	@Column(name = "lastcheckdate")
	public Date getLastCheckDate()
	{
		return lastCheckDate;
	}

	@Column(name = "registrationnumber")
	public String getRegistrationNumber()
	{
		return registrationNumber;
	}

	@Column(name = "installpower")
	@Type(type = "")
	public BigDecimal getInstallPower()
	{
		return installPower;
	}

	@Column(name = "invid")
	public Long getInvid()
	{
		return invid;
	}

	@Column(name = "quantity")
	public BigDecimal getQuantity()
	{
		return quantity;
	}

	// ---------setters--------------

	public void setId(Long id)
	{
		this.id = id;
	}

	public void setInvid(Long invid)
	{
		this.invid = invid;
	}

	public void setQuantity(BigDecimal quantity)
	{
		this.quantity = quantity;
	}

	public void setInstallPower(BigDecimal installPower)
	{
		this.installPower = installPower;
	}

	public void setRegistrationNumber(String registrationNumber)
	{
		this.registrationNumber = registrationNumber;
	}

	public void setLastCheckDate(Date lastCheckDate)
	{
		this.lastCheckDate = lastCheckDate;
	}

	public void setMaterial(Material material)
	{
		this.material = material;
	}

	public void setCheckPeriod(CheckPeriod checkPeriod)
	{
		this.checkPeriod = checkPeriod;
	}

	// ---------constructors--------------

	public BoilerItem()
	{
	}
}
