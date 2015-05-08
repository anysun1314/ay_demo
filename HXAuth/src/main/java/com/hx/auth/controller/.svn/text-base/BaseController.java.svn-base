package com.hx.auth.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.catalina.Session;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.hx.auth.bean.AdminInfo;
import com.hx.auth.bean.Department;
import com.hx.auth.bean.Position;
import com.hx.auth.bean.RoleInfo;
import com.hx.auth.util.DigitalUtil;

/**
 * User: Ren yulin
 * Date: 14-3-6  下午4:18
 */
public class BaseController extends MultiActionController {
	
	private final static String PROPERTIES_NAME = "crs.image_storage_dir";
	private final static int BUFF_SIZE = 4096;
	
	
	protected final static String PROPERTIES_FILE_NAME = "config.properties";
	protected final static String PROPERTIES_TEMPLETE_NAME = "crs.templete_storage_dir";
	
    @Override
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        CustomDateEditor dateEditor = new CustomDateEditor(fmt, true);
        binder.registerCustomEditor(Date.class, dateEditor);
        super.initBinder(request, binder);

    }

    /**
     * 向页面写入字符串
     * @param response
     * @param result
     * @throws IOException
     */
    public void renderText(HttpServletResponse response, String result) throws IOException {
        PrintWriter out = response.getWriter();
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("text/html;charset=utf-8");
        out.print(result);
        out.flush();
        out.close();
    }

    /**
     * 获取Request对象
     * @return
     */
    public HttpServletRequest getRequest(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request;
    }
    
    /*protected void downloadAttachment(Attachment attachment,HttpServletRequest request,HttpServletResponse response){
		try {
			
			Properties pro = PropertiesLoaderUtils
					.loadAllProperties(PROPERTIES_FILE_NAME);
			String imageStorageDir = pro.getProperty(PROPERTIES_NAME, "");
			StringBuffer buff = new StringBuffer(imageStorageDir);
			buff.append(File.separator);
			buff.append(attachment.getPathInfo());
			buff.append(File.separator);
            buff.append(StringUtils.split(attachment.getAttachmentName(),'_')[2]);

			File file = new File(buff.toString());
			
			if(!file.exists()) 
				return;
			
			output(file,attachment.getFileType(),attachment.getAttachmentName(),request,response);
		} catch (Exception e) {
			e.printStackTrace();
		} 
    }*/
    
    protected void output(File file,String fileType,String fileName,HttpServletRequest request,HttpServletResponse response) throws Exception{
		// 下载文件
		FileInputStream inputStream = null;
		ServletOutputStream outputStream = null;
		try {
			response.reset();
			//设置响应类型	PDF文件为"application/pdf"，WORD文件为："application/msword"， EXCEL文件为："application/vnd.ms-excel"。  
			if("jpg".equalsIgnoreCase(fileType) || "jpeg".equalsIgnoreCase(fileType)){
				response.setContentType("image/jpeg;charset=utf-8");
			}else if("png".equalsIgnoreCase(fileType)){
				response.setContentType("image/png;charset=utf-8");
			}else if("pdf".equalsIgnoreCase(fileType)){
				response.setContentType("application/pdf;charset=utf-8");
			}else if("doc".equalsIgnoreCase(fileType) ||  "docx".equalsIgnoreCase(fileType)){
				response.setContentType("application/msword;charset=utf-8");
			}else if("xls".equalsIgnoreCase(fileType) ||  "xlsx".equalsIgnoreCase(fileType)){
				response.setContentType("application/vnd.ms-excel");
			}
//		response.setContentType("application/octet-stream;charset=utf-8");

			//设置响应的文件名称,并转换成中文编码
			//returnName = URLEncoder.encode(returnName,"UTF-8");
			String returnName = response.encodeURL(new String(fileName.getBytes(),"iso8859-1"));	//保存的文件名,必须和页面编码一致,否则乱码
			
			//attachment作为附件下载；inline客户端机器有安装匹配程序，则直接打开；注意改变配置，清除缓存，否则可能不能看到效果
			response.addHeader("Content-Disposition",   "attachment;filename="+returnName);  
			
			//将文件读入响应流
			inputStream = new FileInputStream(file);
			outputStream = response.getOutputStream();
			int readLength = 0;
			byte buf[] = new byte[BUFF_SIZE];
			readLength = inputStream.read(buf, 0, BUFF_SIZE);
			while (readLength != -1) {
				outputStream.write(buf, 0, readLength);
				readLength = inputStream.read(buf, 0, BUFF_SIZE);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("输出文件失败.");
		} finally {
			try {
				outputStream.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    }
    
    public <T> String encoder(T t){
    	return DigitalUtil.base64Encode(String.valueOf(t).getBytes());
    }
    
    public <T> byte[] decoder(T t){
    	return DigitalUtil.base64Decoder(String.valueOf(t));
    }
    
    public HttpSession getSession() {
    	return this.getRequest().getSession();
    }
   
    /**
     * 当前登录用户
     * @return AdminInfo
     */
    public AdminInfo getLoginUser() {
    	return (AdminInfo) getSession().getAttribute(AdminInfo.LOGINUSER);
    }
    /**
     * 当前所处部门
     * @return Department
     */
    public Department getCurrentDepartment() {
    	return (Department) getSession().getAttribute(AdminInfo.CURRENTDEPARTMENT);
    }
    /**
     * 当前所处部门的父级部门
     * @return Department
     */
    public Department getCurrentParentDepartment() {
    	return (Department) getSession().getAttribute(AdminInfo.CURRENTPARENTDEPARTMENT);
    }
    /**
     * 当前所处部门的子部门集
     * @return List<Department>
     */
    public List<Department> getCurrentChildDepartments() {
    	return (List<Department>) getSession().getAttribute(AdminInfo.CURRENTCHILDDEPARTMENTS);
    }
    /**
     * 当前使用的角色
     * @return RoleInfo
     */
    public RoleInfo getCurrentRole() {
    	return (RoleInfo) getSession().getAttribute(AdminInfo.CURRENTROLE);
    }
    /**
     * 当前所处职位
     * @return Position
     */
    public Position getCurrentPosition() {
    	return (Position) getSession().getAttribute(AdminInfo.CURRENTPOSITION);
    }
    /**
     * 当前使用角色对应的权限code集
     * @return List<String>
     */
    public List<String> getCurrentPermission() {
    	return (List<String>) getSession().getAttribute(AdminInfo.CURRENTPERMISSION);
    }
    /**
     * 当前登录用户信息集（带有部门、角色、权限的主键和各自名称,以及中间表对应id[唯一确定一个组织机构、角色、职位及用户对应关系]）
     * @return List<AdminInfo>
     */
    public List<AdminInfo> getUserinfos() {
    	return (List<AdminInfo>) getSession().getAttribute(AdminInfo.USERINFOS);
    }
    
}
