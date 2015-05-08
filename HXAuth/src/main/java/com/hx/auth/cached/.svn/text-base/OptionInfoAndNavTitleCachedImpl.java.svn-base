package com.hx.auth.cached;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

/**
 * @author anyang
 * 该处：1.配置url的相关含义，参数含义，并缓存，供日志记录使用
 * 	   2.配置导航栏标题，以及对应url，并缓存，供登录后进入默认首页使用
 * （注：系统如有新增url，需在此处进行添加，以记录相关url的操作日志[日志相关备注，须在日志拦截器中配置]，以及跳转默认首页）
 * @date 2014-6-4 下午3:45:00
 */
@Service("optionInfoCached")
public class OptionInfoAndNavTitleCachedImpl implements OptionInfoAndNavTitleCached {

	/**
	 * 封装操作信息，即url含义，参数含义
	 * 格式：optionInfoMap.put("url链接", "操作名称,参数含义1or相关含义,参数名1,参数含义2or相关含义,参数名2...");
	 */
	protected static Map<String, String> optionInfoMap = new HashMap<String, String>();
	protected static Map<String, String> navTitleMap = new HashMap<String, String>();
	
	static {
		optionInfoMap.put("/login.do", "登录系统");
		optionInfoMap.put("/logout.do", "登出系统");
		
		//借款合同
		optionInfoMap.put("/jsp/loan_application/controller/index", "查看借款合同列表");
		optionInfoMap.put("/jsp/loan_application/controller/import", "导入借款合同");
		optionInfoMap.put("/jsp/loan_application/controller/update_loan_application_fee_item", "修改借款合同费率,借款合同号,loanApplicationId");
		optionInfoMap.put("/jsp/loan_application/controller/delete", "删除借款合同,借款合同号,loanApplicationId");
		optionInfoMap.put("/jsp/loan_application/controller/payment", "借款合同放款,借款合同号,loanApplicationId");
		optionInfoMap.put("/jsp/loan_application/controller/repayment", "借款合同还款");
		optionInfoMap.put("/jsp/loan_application/controller/to_customer_detail_page", "查看客户信息");
		optionInfoMap.put("/jsp/loan_application/controller/detail", "借款信息查看,借款合同号,loanApplicationId");
		optionInfoMap.put("/jsp/loan_application/controller/customer_card_detail", "查看银行卡信息,借款合同号,loanApplicationId");
		optionInfoMap.put("/jsp/loan_application/page/controller/query", "查看还款计划");
		optionInfoMap.put("/jsp/loan_application/controller/search_creditor_rights", "查看出借人列表");
		optionInfoMap.put("/jsp/loan_application/controller/download_templete", "下载借款合同模板,借款合同号,loanApplicationId");
		
		//出借合同
		optionInfoMap.put("/jsp/lend_application/list", "查看出借合同列表");
		optionInfoMap.put("/jsp/lend_application/import", "导入理财合同");
		optionInfoMap.put("/jsp/lend_application/toDetailPage", "查看理财合同详情");
		optionInfoMap.put("/jsp/lend_application/toCustomerDetailPage", "查看客户信息,客户信息,customerId");
		optionInfoMap.put("/jsp/lend_application/customer_card_detail", "查看客户卡信息,出借合同号,lendOrderId");
		optionInfoMap.put("/jsp/lend_application/toOrderDetail", "查看理财合同信息");
		optionInfoMap.put("/jsp/lend_application/printInfo", "打印理财合同,出借合同号,t");
		optionInfoMap.put("/jsp/lend_application/delete", "删除理财合同,出借合同号,lendOrderId");
		optionInfoMap.put("/jsp/lend_application/detail", "查看理财合同详情,出借合同号,lendOrderId");
		optionInfoMap.put("/jsp/lend_application/getLendOrderReceiveDetailslend", "查看返息明细,出借合同号,lendOrderId");
		optionInfoMap.put("/jsp/lend_application/getFeesItems", "查看出借产品费用项目,出借合同号,lendOrderId");
		optionInfoMap.put("/jsp/lend_application/getFeesItemsDetail", "查看出借产品费用明细,出借合同号,lendOrderId");
		optionInfoMap.put("/jsp/lend_application/fees", "查看出借产品的梯度优惠");
		
		//客户管理
		optionInfoMap.put("/jsp/customer/customerInfoList.do", "查看用户信息列表");
		optionInfoMap.put("/jsp/customer/importList.do", "导入用户信息");
		optionInfoMap.put("/jsp/customer/editCustomerInfo.do", "修改客户信息,客户信息,customerId");
		optionInfoMap.put("/jsp/customer/toDetailPage", "查看客户信息详情");
		optionInfoMap.put("/jsp/customer/toCustomerDetailPage", "查看客户基础信息,客户信息,customerId");
		optionInfoMap.put("/jsp/customer/toCustomerCardPage", "查看客户卡信息");
		optionInfoMap.put("/jsp/customer/toLendOrderDetailPage", "查看出借合同,客户信息,customerId");
		optionInfoMap.put("/jsp/customer/toLoanApplicationDetailPage", "查看借款合同,客户信息,customerId");
		optionInfoMap.put("/jsp/customer/uploadImgPage.do", "附件上传");
		
		//组织机构
		optionInfoMap.put("/jsp/department/departmentsList.do", "查看组织机构列表");
		optionInfoMap.put("/jsp/department/showDepartment.do", "查看组织机构节点详情,组织机构编码,editingId");
		/*
		 * 以下一条，属于同一url不同参数执行不同操作，根据参数addFlag来判断操作
		 * 格式：optionInfoMap.put("url链接", "操作名称1,操作名称2,判断参数,参数含义1or相关含义,参数名1,参数含义2or相关含义,参数名2...");
		 */
		optionInfoMap.put("/jsp/department/saveDepartment.do", "新增组织机构,修改组织机构,addFlag,组织机构编码,departmentCode");
		optionInfoMap.put("/jsp/department/deleteDepartment.do", "删除组织机构,组织机构编码,editingId");
		optionInfoMap.put("/jsp/department/showAdmins.do", "查看组织机构维护人员,组织机构编码,editingId");
		optionInfoMap.put("/jsp/department/saveAdminRole.do", "新增组织机构维护人员,员工,adminId");
		optionInfoMap.put("/jsp/department/deleteAdmin.do", "删除组织机构维护人员 ,员工角色关联ID,userRoleIds");
		
		//员工管理
		optionInfoMap.put("/jsp/admin/adminsList.do", "查看员工列表");
		optionInfoMap.put("/jsp/admin/showAdmin.do", "查看员工详情,员工,adminId");
		optionInfoMap.put("/jsp/admin/savePwd.do", "修改员工密码,员工,adminId");
		optionInfoMap.put("/jsp/admin/saveAdmin.do", "新增员工,修改员工,addFlag,员工,adminId");
		optionInfoMap.put("/jsp/admin/showAdminInfo.do", "查看员工详情,员工,adminId");
		optionInfoMap.put("/jsp/admin/saveAdminPwd.do", "修改员工密码,员工,adminId");
		optionInfoMap.put("/changeDepart.do", "切换角色,员工角色关联ID,userRoleId");
		
		//权限管理
		optionInfoMap.put("/jsp/function/functionsList.do", "查看权限列表");
		optionInfoMap.put("/jsp/function/showFunction.do", "查看权限详情,权限名称,functionId");
		optionInfoMap.put("/jsp/function/saveFunction.do", "新增权限,修改权限,addFlag,权限名称,functionId");
		
		//角色管理
		optionInfoMap.put("/jsp/role/rolesList.do", "查看角色列表");
		optionInfoMap.put("/jsp/role/saveRole.do", "新增角色,修改角色,addFlag,角色,roleId");
		optionInfoMap.put("/jsp/role/saveRoleFunction.do", "角色授权,角色,roleId");
		
		//职位管理
		optionInfoMap.put("/jsp/position/positionsList.do", "查看职位列表");
		optionInfoMap.put("/jsp/position/savePosition.do", "新增职位,修改职位,addFlag,职位,positionId");
		
		//费用管理
		optionInfoMap.put("/jsp/feesitem/feesItemList.do", "查看费用项目列表");
		optionInfoMap.put("/jsp/feesitem/disableFees.do", "禁用费用,费用名称,feesItemId");
		optionInfoMap.put("/jsp/feesitem/enableFees.do", "启用费用,费用名称,feesItemId");
		//optionInfoMap.put("/jsp/feesitem/addFeesItem.do", "添加费用项");
		//optionInfoMap.put("/jsp/feesitem/editFeesItem", "修改费用项,费用名称,feesItemId");
		optionInfoMap.put("/jsp/feesitem/findFeesMess", "查看费用详情,费用名称,feesItemId");
		optionInfoMap.put("/jsp/feesitem/saveFeesItem.do", "添加费用项,修改费用项,addFlag,费用名称,feesItemId");
		
		//借款产品
		optionInfoMap.put("/jsp/product/loanProductList.do", "借款产品列表");
		//optionInfoMap.put("/jsp/product/addLoanProduct", "添加借款产品");
		//optionInfoMap.put("/jsp/product/editLoanProduct.do", "修改借款产品,借款产品名称,loanProductId");
		optionInfoMap.put("/jsp/product/enableOrdisableLoanProduct.do", "启用/禁用借款产品,借款产品名称,loanProductId");
		optionInfoMap.put("/jsp/product/findLoanProduct.do", "查看借款产品详情,借款产品名称,loanProductId");
		optionInfoMap.put("/jsp/product/saveLoanProduct.do", "添加借款产品,修改借款产品,addFlag,借款产品名称,loanProductId");
		
		//出借产品
		optionInfoMap.put("/jsp/product/lendProductList.do", "出借产品列表");
		optionInfoMap.put("/jsp/product/disableLendProduct", "禁用出借产品,出借产品名称,lendProductId");
		optionInfoMap.put("/jsp/product/enableLendProduct", "启用出借产品,出借产品名称,lendProductId");
		//optionInfoMap.put("/jsp/product/editLendProduct.do", "修改出借产品,出借产品名称,lendProductId");
		//optionInfoMap.put("/jsp/product/addLendProduct.do", "新增出借产品");
		optionInfoMap.put("/jsp/product/toViewLendProduct.do", "查看出借产品详细信息,出借产品名称,lendProductId");
		optionInfoMap.put("/jsp/product/publishList.do", "发布出借产品,出借产品名称,lendProductId");
		optionInfoMap.put("/jsp/product/toAddLadder.do", "增加阶梯优惠,费用项,feesItems,起售金额,startsAt,递增金额,upAt");
		optionInfoMap.put("/jsp/product/findLendProductLadderDiscountFees", "处理阶梯优惠,出借产品名称,lendProductId");
		optionInfoMap.put("/jsp/product/lendLoanBindings", "处理适用债权,出借产品名称,lendProductId");
		optionInfoMap.put("/jsp/product/saveLendProduct.do", "新增出借产品,修改出借产品,addFlag,出借产品名称,lendProductId");
		optionInfoMap.put("/jsp/product/saveProductPublish.do", "发布出借产品");
		
		//常量定义
		optionInfoMap.put("jsp/constant/constantsList.do", "常量定义列表");
		optionInfoMap.put("/jsp/constant/saveConstant.do", "新增常量,修改常量,addFlag,常量名,constantDefineId");
		
		
		
		/*
		 * 以下封装导航栏标题
		 */
		//合同管理
		navTitleMap.put("/jsp/loan_application/controller/index", "合同管理>>借款合同列表");
		navTitleMap.put("/jsp/lend_application/list", "合同管理>>出借合同列表");
		//客户管理
		navTitleMap.put("/jsp/customer/customerInfoList.do", "客户管理>>客户信息列表");
		//产品管理
		navTitleMap.put("/jsp/product/loanProductList.do", "产品管理>>借款产品管理");
		navTitleMap.put("/jsp/product/lendProductList.do", "产品管理>>出借产品管理");
		navTitleMap.put("/jsp/feesitem/feesItemList.do", "产品管理>>费用管理");
		//报表管理
		navTitleMap.put("/jsp/report/toRepayment.do", "报表管理>>到期还款报表");
		navTitleMap.put("/jsp/receive/page/controller/index", "报表管理>>返息报表");
		//系统设置
		navTitleMap.put("/jsp/function/functionsList.do", "系统设置>>权限管理");
		navTitleMap.put("/jsp/admin/adminsList.do", "系统设置>>员工管理");
		navTitleMap.put("/jsp/department/departmentsList.do", "系统设置>>组织机构管理");
		navTitleMap.put("/jsp/customer/systemAccountFlowList.do", "系统设置>>平台账户流水");
		navTitleMap.put("/jsp/position/positionsList.do", "系统设置>>职位管理");
		navTitleMap.put("/jsp/role/rolesList.do", "系统设置>>角色管理");
		navTitleMap.put("/jsp/constant/constantsList.do", "系统设置>>常量定义");
		navTitleMap.put("/jsp/optionLog/optionLogsList.do", "系统设置>>操作日志");
		navTitleMap.put("/jsp/task", "系统设置>>定时任务");
		navTitleMap.put("/jsp/event/trigger/eventList.do", "系统设置>>事件执行列表");
		//系统管理
		navTitleMap.put("/jsp/systemoptions/toHedge.do", "账户管理>>账户操作");
		navTitleMap.put("/jsp/systemoptions/toDelLoanApp.do", "账户管理>>删除借款合同");
		navTitleMap.put("/jsp/systemoptions/toSystemAccountManager.do", "账户管理>>平台账户");
	}
	
	/**
	 * 返回操作信息，即url含义，参数含义
	 */
	@Override
	public Map<String, String> getOptionInfo() {
		return optionInfoMap;
	}

	/**
	 * 返回导航栏链接对应标题
	 */
	@Override
	public Map<String, String> getNavTitle() {
		return navTitleMap;
	}
}
