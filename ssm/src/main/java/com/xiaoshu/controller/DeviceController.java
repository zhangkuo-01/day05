package com.xiaoshu.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.xiaoshu.config.util.ConfigUtil;
import com.xiaoshu.entity.Device;
import com.xiaoshu.entity.Operation;
import com.xiaoshu.entity.Role;
import com.xiaoshu.entity.Type;
import com.xiaoshu.entity.User;
import com.xiaoshu.service.DeviceService;
import com.xiaoshu.service.OperationService;
import com.xiaoshu.util.StringUtil;
import com.xiaoshu.util.TimeUtil;
import com.xiaoshu.util.WriterUtil;

@Controller
@RequestMapping("device")
public class DeviceController {

	@Autowired
	DeviceService deviceService;
	
	@Autowired
	private OperationService operationService;
	
	@RequestMapping("outDevice")
	public void outDevice(HttpServletResponse response){
		JSONObject result=new JSONObject();
		try {
			// 导出的代码
			// 准备需要导出的数据
			List<Device> list = deviceService.findAll();
			
			// WorkBook 工作簿
			HSSFWorkbook wb = new HSSFWorkbook();
			// sheet表对象
			HSSFSheet sheet = wb.createSheet();
			// 设置一个标题 行 row
			HSSFRow row0 = sheet.createRow(0);
			String[]title = {"编号","设备名称","设备类型名称","内存","机身颜色","价格","设备状态","创建时间"};
			for (int i = 0; i < title.length; i++) {
				row0.createCell(i).setCellValue(title[i]);
			}
			
			for (int i = 0; i < list.size(); i++) {
				HSSFRow row = sheet.createRow(i+1);
				Device device = list.get(i);
				row.createCell(0).setCellValue(device.getDeviceid());
				row.createCell(1).setCellValue(device.getDevicename());
				row.createCell(2).setCellValue(device.getType().getTypename());
				row.createCell(3).setCellValue(device.getDeviceram());
				row.createCell(4).setCellValue(device.getColor());
				row.createCell(5).setCellValue(device.getPrice());
				row.createCell(6).setCellValue(device.getStatus());
				row.createCell(7).setCellValue(TimeUtil.formatTime(device.getCreatetime(), "yyyy-MM-dd HH:mm:ss"));
			}
			
			OutputStream out = new FileOutputStream(new File("E://ssm-h1909F.xls"));
			wb.write(out);
			out.close();
			wb.close();
			
			result.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("errorMsg", "对不起，删除失败");
		}
		WriterUtil.write(response, result.toString());
	}
	
	@RequestMapping("deviceIndex")
	public String index(HttpServletRequest request,Integer menuid) throws Exception{
		List<Type> roleList = deviceService.findTypeAll();
		List<Operation> operationList = operationService.findOperationIdsByMenuid(menuid);
		request.setAttribute("operationList", operationList);
		request.setAttribute("roleList", roleList);
		return "device";
	}
	@RequestMapping(value="userList",method=RequestMethod.POST)
	public void userList(Device device,HttpServletRequest request,HttpServletResponse response,String offset,String limit) throws Exception{
		try {
			
			Integer pageSize = StringUtil.isEmpty(limit)?ConfigUtil.getPageSize():Integer.parseInt(limit);
			Integer pageNum =  (Integer.parseInt(offset)/pageSize)+1;
			PageInfo<Device> userList= deviceService.findUserPage(device,pageNum,pageSize);
			
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("total",userList.getTotal() );
			jsonObj.put("rows", userList.getList());
	        WriterUtil.write(response,jsonObj.toString());
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	// 新增或修改
		@RequestMapping("reserveUser")
		public void reserveUser(HttpServletRequest request,Device device,HttpServletResponse response){
			Integer userId = device.getDeviceid();
			JSONObject result=new JSONObject();
			try {
				if (userId != null) {   // userId不为空 说明是修改
//					User userName = deviceService.existUserWithUserName(user.getUsername());
//					if(userName != null && userName.getUserid().compareTo(userId)==0){
//						user.setUserid(userId);
//						deviceService.updateUser(user);
//						result.put("success", true);
//					}else{
//						result.put("success", true);
//						result.put("errorMsg", "该用户名被使用");
//					}
					
				}else {   // 添加
					if(deviceService.existDevicename(device.getDevicename())==null){  // 没有重复可以添加
						device.setCreatetime(new Date());
						deviceService.addDevice(device);
						result.put("success", true);
					} else {
						result.put("success", true);
						result.put("errorMsg", "该用户名被使用");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				result.put("success", true);
				result.put("errorMsg", "对不起，操作失败");
			}
			WriterUtil.write(response, result.toString());
		}
		@RequestMapping("deleteUser")
		public void delUser(HttpServletRequest request,HttpServletResponse response){
			JSONObject result=new JSONObject();
			try {
				String[] ids=request.getParameter("ids").split(",");
				for (String id : ids) {
					deviceService.deleteDevice(Integer.parseInt(id));
				}
				result.put("success", true);
				result.put("delNums", ids.length);
			} catch (Exception e) {
				e.printStackTrace();
				result.put("errorMsg", "对不起，删除失败");
			}
			WriterUtil.write(response, result.toString());
		}
}
