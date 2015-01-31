package gis.orm;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "material")
public class Material implements Serializable
{
	private Long id;
	private String name;

	// ---------references--------------

	private Measure measure;

	// ---------getters--------------

	@ManyToOne
	@JoinColumn(name = "measureid")
	public Measure getMeasure()
	{
		return measure;
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

	// ---------setters--------------

	public void setMeasure(Measure measure)
	{
		this.measure = measure;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	// ---------constructors--------------

	public Material()
	{
	}
}
