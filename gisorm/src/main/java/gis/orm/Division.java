package gis.orm;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "division")
public class Division implements Serializable
{
	private Long id;
	private String name;

	// ---------LinksToOthers--------------
	// it is not necessary - never use it
	// @OneToMany(mappedBy = "person")
	// private Set<Inventories> inventories = new HashSet<Inventories>();

	// ---------getters--------------

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

	// ---------setters--------------

	public void setId(Long id)
	{
		this.id = id;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	// ---------constructors--------------

	public Division()
	{
	}
}
