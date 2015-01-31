package gis.orm;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "boilertype")
public class BoilerType implements Serializable
{
	private Long id;
	private String name;

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

	public BoilerType()
	{
	}
}
