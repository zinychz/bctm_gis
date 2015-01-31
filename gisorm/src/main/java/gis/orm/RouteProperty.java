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
@Table(name = "routeproperty")
public class RouteProperty implements Serializable
{
	private Long id;

	private BigDecimal depth;
	private BigDecimal pipecount;
	private String width;
	private String remark;

	// ---------references--------------

	private RouteType routeType;
	private Insulation insulation;
	private Tray tray;

	// ---------getters--------------

	@ManyToOne
	@JoinColumn(name = "trayid")
	public Tray getTray()
	{
		return tray;
	}

	@ManyToOne
	@JoinColumn(name = "insulationid")
	public Insulation getInsulation()
	{
		return insulation;
	}

	@ManyToOne
	@JoinColumn(name = "routetypeid")
	public RouteType getRouteType()
	{
		return routeType;
	}

	@Id
	// @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	public Long getId()
	{
		return id;
	}

	@Column(name = "depth")
	public BigDecimal getDepth()
	{
		return depth;
	}

	@Column(name = "pipecount")
	public BigDecimal getPipecount()
	{
		return pipecount;
	}

	@Column(name = "width")
	public String getWidth()
	{
		return width;
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

	public void setDepth(BigDecimal depth)
	{
		this.depth = depth;
	}

	public void setPipecount(BigDecimal pipecount)
	{
		this.pipecount = pipecount;
	}

	public void setWidth(String width)
	{
		this.width = width;
	}

	public void setRemark(String remark)
	{
		this.remark = remark;
	}

	public void setRouteType(RouteType routeType)
	{
		this.routeType = routeType;
	}

	public void setInsulation(Insulation insulation)
	{
		this.insulation = insulation;
	}

	public void setTray(Tray tray)
	{
		this.tray = tray;
	}

	// ---------constructors--------------

	public RouteProperty()
	{
	}
}
