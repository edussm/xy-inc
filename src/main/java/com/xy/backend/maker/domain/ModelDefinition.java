package com.xy.backend.maker.domain;

import java.io.Serializable;
import java.util.Map;

import org.springframework.data.annotation.Id;

public class ModelDefinition implements Serializable {
	
	private static final long serialVersionUID = 6639755551480194459L;

	@Id
	private String modelName;

	/**
	 * descreve o nome da coluna e o tipo aceito
	 * tipos: string -> string
	 * 			text -> string
	 * 			int  -> int
	 * 			decimal -> double
	 * 			
	 */
	private Map<String, String> columns;

	public ModelDefinition() {
	}
	
	public ModelDefinition(String modelName, Map<String, String> columns) {
		this.modelName = modelName;
		this.columns = columns;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public Map<String, String> getColumns() {
		return columns;
	}

	public void setColumns(Map<String, String> columns) {
		this.columns = columns;
	}

	@Override
	public String toString() {
		return String.format("Model Definition [modelName=%s, columns=%s]", modelName, columns);
	}	

}
