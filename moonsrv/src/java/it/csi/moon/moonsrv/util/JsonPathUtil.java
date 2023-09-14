/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.InvalidPathException;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.PathNotFoundException;

public class JsonPathUtil<E> {
	
	public static Configuration DEFAULT = Configuration.builder()
			   .options(Option.DEFAULT_PATH_LEAF_TO_NULL).build();
	
	public static Configuration REQUIRE = Configuration.builder()
			   .options(Option.REQUIRE_PROPERTIES).build();		
	
	public static  Configuration RETURN_LIST = Configuration.builder()
			   .options(Option.ALWAYS_RETURN_LIST).build();
	
	public static Configuration SUPPRESS_EXCEPTIONS = Configuration.builder()
			   .options(Option.SUPPRESS_EXCEPTIONS).build();	
	
	private static final String CLASS_NAME = "JsonPathUtil";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	private String json = null;
	private String defaultIfNull = null;


	public JsonPathUtil() {
		// TODO Auto-generated constructor stub
	}
	
	public JsonPathUtil(String json) {
      this.json = json;
	}
	
	/**
	 * @param json the json to set
	 */
	public void setJson(String json) {
		this.json = json;
	}

	
	/**
	 * @return the defaultIfNull
	 */
	public String getDefaultIfNull() {
		return defaultIfNull;
	}

	/**
	 * @param defaultIfNull the defaultIfNull to set
	 */
	public void setDefaultIfNull(String defaultIfNull) {
		this.defaultIfNull = defaultIfNull;
	}
	
	public void resetDefaultIfNull() {
		this.defaultIfNull = null;
	}

		
	public String getStringValue(E value) {		
		if (value == null) {			
			return getDefaultIfNull();
		}			
		return value.toString();	
	}

	
	public <E> E getValue(String path) {
	    E resValue = null;
		List<E> ref;
		try {
			LOG.debug("[" + CLASS_NAME + "::getValue] PATH: "+path);
			ref = JsonPath.using(RETURN_LIST).parse(this.json).read(path);
			if (ref!=null) {
				E item = ref.get(0);
				if (item instanceof Map) {
					Map<String, E> map = (Map<String, E>) item;
					JSONObject jsonObj = new JSONObject(map);
					if (jsonObj != null) {
						String json = new JSONObject(map).toString();
						try {
							resValue = JsonPath.using(DEFAULT).parse(json).read(path);							
							if (resValue instanceof Map) {
								resValue = null;
							}
						} catch (PathNotFoundException e) {
							LOG.warn("[" + CLASS_NAME + "::getValue] PATH NOT FOUND "+e);
						}
					}
				} else {
					resValue = item;
				}
			}
			return resValue;
		} catch (PathNotFoundException pe) {
			LOG.warn("[" + CLASS_NAME + "::getValue] PATH NOT FOUND " + pe);
			return null;
		} catch (InvalidPathException ipe) {
			LOG.warn("[" + CLASS_NAME + "::getValue] INVALID PATH " + ipe);
			return null;			
		} catch (Exception e) {
			LOG.warn("[" + CLASS_NAME + "::getValue] Generic problem on parsing " + e);
			return null;
		}
	}
	
	public <E> E getValue(String jsonValue, String path) {
	    E resValue = null;
		List<E> ref;
		try {
			LOG.debug("[" + CLASS_NAME + "::getValue] PATH: "+path);
			ref = JsonPath.using(RETURN_LIST).parse(jsonValue).read(path);
			if (ref!=null) {
				E item = ref.get(0);
				if (item instanceof Map) {
					Map<String, E> map = (Map<String, E>) item;
					JSONObject jsonObj = new JSONObject(map);
					if (jsonObj != null) {
						String json = new JSONObject(map).toString();
						try {
							resValue = JsonPath.using(DEFAULT).parse(json).read(path);
							if (resValue instanceof Map) {
								resValue = null;
							}
						} catch (PathNotFoundException e) {
							LOG.warn("[" + CLASS_NAME + "::getValue] PATH NOT FOUND "+e);
						}
					}
				} else {
					resValue = item;
				}
			}
			return resValue;
		} catch (PathNotFoundException pe) {
			LOG.warn("[" + CLASS_NAME + "::getValue] PATH NOT FOUND " + pe);
			return null;
		} catch (InvalidPathException ipe) {
			LOG.warn("[" + CLASS_NAME + "::getValue] INVALID PATH " + ipe);
			return null;				
		} catch (Exception e) {
			LOG.warn("[" + CLASS_NAME + "::getValue]  Generic problem on parsing " + e);
			return null;
		}
	}
	
	public <E> E getInnerValue(String outerPath, String innerPath) {
	    E resValue = null;
		List<E> ref;
		try {
			LOG.debug("[" + CLASS_NAME + "::getValue] OUTER PATH: "+outerPath);
			ref = JsonPath.using(RETURN_LIST).parse(this.json).read(outerPath);
			if (ref!=null) {
				E item = ref.get(0);
				if (item instanceof Map) {
					Map<String, E> map = (Map<String, E>) item;
					JSONObject jsonObj = new JSONObject(map);
					if (jsonObj != null) {
						String json = new JSONObject(map).toString();
						try {
							LOG.debug("[" + CLASS_NAME + "::getValue] INNER PATH: "+innerPath);
							resValue = JsonPath.using(DEFAULT).parse(json).read(innerPath);	
							if (resValue instanceof Map) {
								resValue = null;
							}
						} catch (PathNotFoundException e) {
							LOG.warn("[" + CLASS_NAME + "::getValue] PATH NOT FOUND "+e);
						}
					}
				} else {
					resValue = item;
				}
			}
			return resValue;
		} catch (PathNotFoundException pe) {
			LOG.warn("[" + CLASS_NAME + "::getInnerValue] PATH NOT FOUND " + pe);
			return null;
		} catch (InvalidPathException ipe) {
			LOG.warn("[" + CLASS_NAME + "::getInnerValue] INVALID PATH " + ipe);
			return null;				
		} catch (Exception e) {
			LOG.warn("[" + CLASS_NAME + "::getInnerValue]  Generic problem on parsing " + e);
			return null;
		}
	}

	
	public static Boolean checkPath(String json, String path) {
		try {
			LOG.debug("[" + CLASS_NAME + "::checkPath] PATH: "+path);
			JsonPath.using(RETURN_LIST).parse(json).read(path);				
		} catch (PathNotFoundException e) {
			LOG.warn("[" + CLASS_NAME + "::checkPath] PATH NOT FOUND "+e);
			return Boolean.FALSE;			
		}
		catch (InvalidPathException ipe) {
			LOG.warn("[" + CLASS_NAME + "::checkPath] INVALID PATH " + ipe);
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}
	
	public List<String> getListValues(String outerPath, List<String> innerPaths, String separator) {
		List<E> ref;
		List<String> ret = new ArrayList<>();
		LOG.debug("[" + CLASS_NAME + "::getListValues] OUTER PATH: " + outerPath);
		try {
			ref = JsonPath.using(RETURN_LIST).parse(this.json).read(outerPath);
			if (ref!=null) {
				E item = ref.get(0);
				if (item instanceof Map) {
					Map<String, E> map = (Map<String, E>) item;
					JSONObject jsonObj = new JSONObject(map);
					if (jsonObj != null) {
						String json = new JSONObject(map).toString();
						ret.add(this.getTextForListElements(json, innerPaths, separator));
					}
				} else {
					List<Object> listItems = (List<Object>) item;
					for (Object innerItem : listItems) {
						if (innerItem instanceof Map) {
							Map<String, Object> map = (Map<String, Object>) innerItem;
							String json = new JSONObject(map).toString();
							ret.add(this.getTextForListElements(json, innerPaths, separator));
						} else {
							ret = (List<String>) item;
							break;
						}
					}
				}
			}
			return ret;
		} catch (PathNotFoundException pe) {
			LOG.warn("[" + CLASS_NAME + "::getListValues] PATH NOT FOUND " + pe);
			return null;
		} catch (InvalidPathException ipe) {
			LOG.warn("[" + CLASS_NAME + "::getListValues] INVALID PATH " + ipe);
			return null;			
		} catch (Exception e) {
			LOG.warn("[" + CLASS_NAME + "::getListValues]  Generic problem on parsing " + e);
			return null;
		}
	}
	
	
	public List<String> getListLabelledValues(String outerPath, List<String> labels, List<String> innerPaths, String separator) {
		List<E> ref;
		List<String> ret = new ArrayList<>();
		LOG.debug("[" + CLASS_NAME + "::getListValues] OUTER PATH: " + outerPath);
		try {
			ref = JsonPath.using(RETURN_LIST).parse(this.json).read(outerPath);
			if (ref!=null) {
				E item = ref.get(0);
				if (item instanceof Map) {
					Map<String, E> map = (Map<String, E>) item;
					JSONObject jsonObj = new JSONObject(map);
					if (jsonObj != null) {
						String json = new JSONObject(map).toString();
						ret.add(this.getTextForListLabelledElements(json, labels,innerPaths, separator));
					}
				} else {
					List<Object> listItems = (List<Object>) item;
					for (Object innerItem : listItems) {
						if (innerItem instanceof Map) {
							Map<String, Object> map = (Map<String, Object>) innerItem;
							String json = new JSONObject(map).toString();
							ret.add(this.getTextForListLabelledElements(json, labels,innerPaths, separator));
						} else {
							ret = (List<String>) item;
							break;
						}
					}
				}
			}
			return ret;
		} catch (PathNotFoundException pe) {
			LOG.warn("[" + CLASS_NAME + "::getListValues] PATH NOT FOUND " + pe);
			return null;
		} catch (InvalidPathException ipe) {
			LOG.warn("[" + CLASS_NAME + "::getListValues] INVALID PATH " + ipe);
			return null;			
		} catch (Exception e) {
			LOG.warn("[" + CLASS_NAME + "::getListValues]  Generic problem on parsing " + e);
			return null;
		}
	}
	
	public String getStringValueByPaths(String outerPath, List<String> innerPaths, String separator) {
		List<E> ref;
		String ret = null;
		LOG.debug("[" + CLASS_NAME + "::getListValues] OUTER PATH: " + outerPath);
		try {
			ref = JsonPath.using(RETURN_LIST).parse(this.json).read(outerPath);
			if (ref!=null) {
				E item = ref.get(0);
				if (item instanceof Map) {
					Map<String, E> map = (Map<String, E>) item;
					JSONObject jsonObj = new JSONObject(map);
					if (jsonObj != null) {
						String json = new JSONObject(map).toString();
						ret = this.getTextForListElements(json, innerPaths, separator);					
					}
				} else {
					List<Object> listItems = (List<Object>) item;
					for (Object innerItem : listItems) {
						if (innerItem instanceof Map) {
							Map<String, Object> map = (Map<String, Object>) innerItem;
							String json = new JSONObject(map).toString();
							ret = this.getTextForListElements(json, innerPaths, separator);	
						} else {					
							ret = String.join(separator, (List<String>) item);
							break;
						}
					}
				}
			}
			return ret;
		} catch (PathNotFoundException pe) {
			LOG.warn("[" + CLASS_NAME + "::getStringValueByPaths] PATH NOT FOUND " + pe);
			return null;
		} catch (InvalidPathException ipe) {
			LOG.warn("[" + CLASS_NAME + "::getStringValueByPaths] INVALID PATH " + ipe);
			return null;			
		} catch (Exception e) {
			LOG.warn("[" + CLASS_NAME + "::getStringValueByPaths]  Generic problem on parsing " + e);
			return null;
		}
	}
	
	private boolean checkEmptyList(List<String> listOfValues, String nullValue) {
		boolean ret = true; 		
		if (listOfValues.isEmpty()) return true;		
		for (String value : listOfValues) {
        	if (value != null  && value.length() > 0 && !value.equals(nullValue)) {
        		return false;
        	}
        } 		
		return ret;
	}
	
    public String getStringValueByPaths(List<String> paths, String separator){        
        List<String> listOfValues = new ArrayList<>();
        String value="";
    	for (String path : paths) {
        	listOfValues.add(this.getStringValue(this.getValue(this.json, path)));     
        }        
//        return !listOfValues.isEmpty()? String.join(separator, listOfValues): null;
    	return !checkEmptyList(listOfValues, "") ? String.join(separator, listOfValues): null;
    }
	
    private String getTextForListElements(String json, List<String> paths, String separator){        
        List<String> listOfValues = new ArrayList<>();
        String value="";
    	for (String path : paths) {
        	listOfValues.add(this.getStringValue(this.getValue(json, path)));     
        }        
        return String.join(separator, listOfValues);    
    }
    
    private String getTextForListLabelledElements(String json, List<String> labels, List<String> paths, String separator){        
        List<String> listOfValues = new ArrayList<>();
        String value="";
        int[] idx = { 0 };        
        paths.forEach(e -> listOfValues.add(labels.get(idx[0]++) + " " +this.getStringValue(this.getValue(json, e))));  
        return String.join(separator, listOfValues);    
    }


}
