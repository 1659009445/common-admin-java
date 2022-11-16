package com.huii.admin.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huii.admin.modules.system.entity.SysDept;
import com.huii.admin.modules.system.mapper.SysDeptMapper;
import com.huii.admin.modules.system.service.SysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements SysDeptService {

	@Autowired
	private SysDeptMapper sysDeptMapper;

	@Override
	public List<SysDept> getTreeList() {
		List<SysDept> list = this.list(null);
		return buildTreeList(list,0L);
	}

	@Override
	public Boolean deleteSysDeptR(Long id) {
		return null;
	}

	private List<SysDept> buildTreeList(List<SysDept> list, Long pid) {

		List<SysDept> __list = new ArrayList<>();
		for (SysDept dept : list) {
			if(Objects.equals(dept.getPid(),pid)){
				__list.add(findChildNode(dept,list));
			}
		}
		return __list;
	}

	private static SysDept findChildNode(SysDept dept, List<SysDept> list){
		for (SysDept d : list){
			if(Objects.equals(d.getPid(),dept.getId())){
				if(dept.getChildren() == null){
					dept.setChildren(new ArrayList<SysDept>());
				}
				dept.getChildren().add(findChildNode(d,list));
			}
		}
		return dept;
	}
}
