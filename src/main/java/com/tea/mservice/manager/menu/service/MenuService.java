package com.tea.mservice.manager.menu.service;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tea.mservice.manager.permission.service.PermissionService;
import com.tea.mservice.manager.permission.vo.PermissionVo;

@Service
public class MenuService {

	@Autowired
	private PermissionService permissionService;

	public List<Map<String, Object>> createMenus() {

		Subject subject = SecurityUtils.getSubject();
		List<PermissionVo> list = permissionService.findPermissionByUser(subject.getPrincipal().toString(), "menu");

		List<Map<String, Object>> menuList = new LinkedList<Map<String, Object>>();
		findMenus(menuList, list);

		return menuList;
	}

	private void findMenus(List<Map<String, Object>> menuList, List<PermissionVo> list) {

		for (PermissionVo vo : list) {

			if (vo.getParentId().longValue() != 0) {
				continue;
			}
			
			String url = StringUtils.isEmpty(vo.getUrl()) ? "" : vo.getUrl();
			
			Map<String, Object> firstMap = new LinkedHashMap<String, Object>();
			firstMap.put("id", vo.getId());
			firstMap.put("parentId", vo.getParentId());
			firstMap.put("name", vo.getName());
			firstMap.put("url", url);
			firstMap.put("code", vo.getCode());
			firstMap.put("icon", vo.getIcon());
			
			menuList.add(firstMap);

			findChildMenus(firstMap, vo, list);

		}
	}

	private void findChildMenus(Map<String, Object> firstMap, PermissionVo parentVo, List<PermissionVo> list) {

		if (!hasChildren(parentVo.getId(), list)) {
			return;
		}

		List<Map<String, Object>> childList = new LinkedList<Map<String, Object>>();
		for (PermissionVo vo : list) {
			if (parentVo.getId().longValue() != vo.getParentId().longValue()) {
				continue;
			}
			String url = StringUtils.isEmpty(vo.getUrl()) ? "" : vo.getUrl();
			
			
			Map<String, Object> childMap = new LinkedHashMap<String, Object>();
			childMap.put("id", vo.getId());
			childMap.put("parentId", vo.getParentId());
			childMap.put("name", vo.getName());
			childMap.put("url", url);
			childMap.put("code", vo.getCode());
			childMap.put("icon", vo.getIcon());
			
			childList.add(childMap);
			
			firstMap.put("child", childList);
			
			if (hasChildren(parentVo.getId(), list)) {
				findChildMenus(childMap, vo, list);
			}
		}
	}

	private boolean hasChildren(Long id, List<PermissionVo> list) {
		for (PermissionVo vo : list) {
			if (id.longValue() != vo.getParentId().longValue()) {
				continue;
			}
			return true;
		}
		return false;
	}
}
