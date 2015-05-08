package com.hx.auth.cached;
import java.util.List;

import com.hx.auth.bean.FunctionUrl;

/**
 * @author anyang
 * @date 2014-5-29 上午9:41:05
 */

public interface FunctionUrlCached {
	
	public List<FunctionUrl> getAll();
	
	public List<String> getAllUrl();
	
	public void reset();
	
}
