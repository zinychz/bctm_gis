package gis.orm;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class ItemPk implements Serializable
{
	private Long invid;

	private Material material;

	@Column(name = "invid")
	public Long getInvid()
	{
		return invid;
	}

	public void setInvid(Long invid)
	{
		this.invid = invid;
	}

//	@ManyToOne(fetch = FetchType.LAZY)
	@ManyToOne
	@JoinColumn(name = "materialid")
	public Material getMaterial()
	{
		return material;
	}

	public void setMaterial(Material material)
	{
		this.material = material;
	}

	public ItemPk()
	{
	}
}
