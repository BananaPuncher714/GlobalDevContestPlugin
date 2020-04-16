package com.aaaaahhhhhhh.bananapuncher714.spaaace.core.api;

public class DamageType {
	public static final DamageType TRUE = new DamageType( "TRUE" );
	public static final DamageType PHYSICAL = new DamageType( "PHYSICAL" );
	public static final DamageType MISAKA = new DamageType( "MISAKA" );
	public static final DamageType CHEMICAL = new DamageType( "CHEMICAL" );
	public static final DamageType EXPLOSION = new DamageType( "EXPLOSION" );
	public static final DamageType FIRE = new DamageType( "FIRE" );
	public static final DamageType VANILLA = new DamageType( "VANILLA" );
	public static final DamageType CUSTOM = new DamageType( "CUSTOM" );
	
	private final String id;
	private final String type;
	
	private DamageType( String type ) {
		this( "default", type );
	}
	
	public DamageType( String id, String type ) {
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
		return "DamageType{" + id + ":" + type + "}";
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
		DamageType other = (DamageType) obj;
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
