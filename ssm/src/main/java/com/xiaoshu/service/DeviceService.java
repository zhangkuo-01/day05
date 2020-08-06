package com.xiaoshu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.xiaoshu.dao.DeviceMapper;
import com.xiaoshu.dao.TypeMapper;
import com.xiaoshu.entity.Device;
import com.xiaoshu.entity.Type;
import com.xiaoshu.entity.User;
import com.xiaoshu.entity.UserExample;
import com.xiaoshu.entity.UserExample.Criteria;

@Service
public class DeviceService {

	@Autowired
	DeviceMapper deviceMapper;
	
	@Autowired
	TypeMapper typeMapper;

	public List<Type> findTypeAll() {
		// TODO Auto-generated method stub
		return typeMapper.selectAll();
	}

	public PageInfo<Device> findUserPage(Device device, Integer pageNum, Integer pageSize) {
		// TODO Auto-generated method stub
		PageHelper.startPage(pageNum, pageSize);
		List<Device> userList = deviceMapper.findDeviceAndtypeByExample(device);
		PageInfo<Device> pageInfo = new PageInfo<Device>(userList);
		return pageInfo;
	}

	public Device existDevicename(String devicename) {
		// TODO Auto-generated method stub
		List<Device> userList = deviceMapper.findByDevicename(devicename);
		return userList.isEmpty()?null:userList.get(0);
	}

	public void addDevice(Device device) {
		// TODO Auto-generated method stub
		deviceMapper.insert(device);
	}

	public void deleteDevice(int parseInt) {
		// TODO Auto-generated method stub
		deviceMapper.deleteByPrimaryKey(parseInt);
	}

	public List<Device> findAll() {
		// TODO Auto-generated method stub
		return deviceMapper.findDeviceAndtypeByExample(null);
	}
	
}
