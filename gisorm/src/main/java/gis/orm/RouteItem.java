package gis.orm;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "routeitem")
public class RouteItem implements Serializable
{
	private BigDecimal quantity;

	// ---------references--------------

	private ItemPk itemPk;

	// ---------getters--------------

	@EmbeddedId
	public ItemPk getItemPk()
	{
		return itemPk;
	}

	@Column(name = "quantity")
	public BigDecimal getQuantity()
	{
		return quantity;
	}

	// ---------setters--------------

	public void setItemPk(ItemPk itemPk)
	{
		this.itemPk = itemPk;
	}

	public void setQuantity(BigDecimal quantity)
	{
		this.quantity = quantity;
	}

	// ---------constructors--------------

	public RouteItem()
	{
	}
}
