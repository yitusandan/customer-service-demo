package bs.cm.controller;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

public class JsonResult {

	private int retCode = 0;
	private String retMsg = "成功";

	private Map<String, Object> retMap;

	public JsonResult() {
		retMap = new HashMap<String, Object>();
	}

	public void add(String key, Object value) {
		retMap.put(key, value);
	}

	public String toJson() {
		retMap.put("retCode", retCode);
		retMap.put("retMsg", retMsg);
		return new Gson().toJson(retMap);
	}

	public void setErr(int code, String msg) {
		retCode = code;
		retMsg = msg;
	}

}
