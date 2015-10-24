package edu.boun.swe74.pinkelephant.db.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class PersistentObject implements Serializable {

	private static final long serialVersionUID = -8913200492941600346L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id = null;

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof PersistentObject)) {
			return false;
		}
		final PersistentObject other = (PersistentObject) obj;
		if (this.id != null && other.id != null) {
			if (this.id != other.id) {
				return false;
			}
		}
		return true;
	}

	protected static boolean getBooleanValue(final Boolean value) {
		return Boolean.valueOf(String.valueOf(value));
	}

	public Long getId() {
		return this.id;
	}

	@SuppressWarnings("unused")
	private void setId(final Long id) {
		this.id = id;
	}
}
