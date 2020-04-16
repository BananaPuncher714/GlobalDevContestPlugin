package com.aaaaahhhhhhh.bananapuncher714.space.core.api.resourcepack;

import com.google.gson.JsonObject;

public abstract class FontProvider {
	protected String type;
	
	public FontProvider( String type ) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
	
	public JsonObject toJsonObject() {
		JsonObject object = new JsonObject();
		object.addProperty( "type", type );
		
		return object;
	}
}
