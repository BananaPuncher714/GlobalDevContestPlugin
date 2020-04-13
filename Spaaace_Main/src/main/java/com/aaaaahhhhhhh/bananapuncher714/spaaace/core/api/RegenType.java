package com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api;

public class RegenType {
	public static final RegenType VANILLA = new RegenType( "VANILLA" );
	public static final RegenType NATURAL = new RegenType( "NATURAL" );
	public static final RegenType CUSTOM = new RegenType( "CUSTOM" );
	
	private final String id;
	private final String type;
	
	private RegenType( String type ) {
		this( "default", type );
	}
	
	public RegenType( String id, String type ) {
		this.id = id;
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public String getType() {
		return type;
	}
	
	@Override
	public String toString() {
		return "RegenType{" + id + ":" + type + "}";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		RegenType other = (RegenType) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
}
