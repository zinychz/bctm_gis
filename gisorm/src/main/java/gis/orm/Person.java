package gis.orm;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "person")
public class Person implements Serializable
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

	public Person()
	{
	}
}
