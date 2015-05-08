package com.hx.auth.cached;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hx.auth.bean.FunctionUrl;
import com.hx.auth.service.FunctionUrlService;

/**
 * @author anyang
 * @date 2014-5-29 上午9:52:55
 */
@Service("functionUrlCached")
public class FunctionUrlCachedImpl implements FunctionUrlCached {
	
	//private final static Logger logger = Logger.getLogger(FunctionUrlCachedImpl.class);

	/**
	 * 缓存�?��的url
	 */
	protected static List<String> allUrls = null;
	
	@Autowired
	private FunctionUrlService functionUrlService;
	
	//容器启动的时候，初始化所有url到缓�?
	private FunctionUrlCachedImpl() {
		//注意加载顺序，初始化bean之后调用functionUrlService
	}

	@Override
	public List<FunctionUrl> getAll() {
		return functionUrlService.findAll();
	}

	@Override
	public List<String> getAllUrl() {
		if(allUrls == null) {
			allUrls = functionUrlService.findAllUrls();
		}
		return allUrls;
	}

	@Override
	public void reset() {
		allUrls = null;
		allUrls = this.getAllUrl();
	}
	
	//初始化所有url到缓�?
	@PostConstruct
	protected void _init(){
		allUrls = functionUrlService.findAllUrls();
		/*if(logger.isDebugEnabled()){
			for(String url : allUrls){
				logger.debug("url:"+url);
			}
		}*/
	}
}
