package gis.orm;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "Gisgraphs")
public class Gisgraphs implements Serializable {
	private Long id;

	private String type;
	private Long priority;
	private String name;
	private String coord;

	// ---------references--------------

	private Set<Gisgraphs> groups = new HashSet<Gisgraphs>();
	private Set<Gisgraphs> children = new HashSet<Gisgraphs>();

	private Inventories inventory;

	// ---------getters--------------

	@OneToOne(fetch = FetchType.LAZY)
	// @OneToOne
	@JoinColumn(name = "invid")
	// @org.hibernate.annotations.LazyToOne(org.hibernate.annotations.LazyToOneOption.NO_PROXY)
	public Inventories getInventory() {
		return inventory;
	}

	// @ManyToMany(fetch = FetchType.EAGER)
	@ManyToMany
	@JoinTable(name = "GROUPS", joinColumns = { @JoinColumn(name = "SHAPEID") }, inverseJoinColumns = {
			@JoinColumn(name = "GROUPID") })
	public Set<Gisgraphs> getGroups() {
		return groups;
	}

	// @ManyToMany(mappedBy = "groups", fetch = FetchType.EAGER)
	@ManyToMany(mappedBy = "groups")
	public Set<Gisgraphs> getChildren() {
		return children;
	}

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "id")
	public Long getId() {
		return id;
	}

	@Column(name = "type")
	public String getType() {
		return type;
	}

	@Column(name = "priority")
	public Long getPriority() {
		return priority;
	}

	@Column(name = "name")
	public String getName() {
		return name;
	}

	@Lob
	@Column(name = "coord")
	public String getCoord() {
		return coord;
	}

	// ---------setters--------------

	public void setInventory(Inventories inventory) {
		this.inventory = inventory;
	}

	public void setGroups(Set<Gisgraphs> groups) {
		this.groups = groups;
	}

	public void setChildren(Set<Gisgraphs> children) {
		this.children = children;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setPriority(Long priority) {
		this.priority = priority;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCoord(String coord) {
		this.coord = coord;
	}

	// ---------constructors--------------

	public Gisgraphs() {
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Gisgraphs other = (Gisgraphs) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
