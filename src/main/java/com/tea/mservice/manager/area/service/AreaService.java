package com.tea.mservice.manager.area.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.tea.mservice.manager.area.repository.AreaRepository;

import javax.persistence.criteria.Predicate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AreaService {
	private static final Logger LOG = LoggerFactory.getLogger(AreaService.class);
	
	private String apigwHost;

	@Autowired
	AreaRepository areaRepository;

	public List<Map> list(Long parentId) throws Exception {
		try {
			return areaRepository
					.findAll(
							(root, query, cb) -> {
								List<Predicate> list = new ArrayList<>();
								list.add(cb.equal(root.get("parentId"), parentId));
								return cb.and( list.toArray( new Predicate[list.size()] ) );
								}, new Sort(Sort.Direction.ASC, "sortby"))
					.stream()
					.map(area -> {
						Map<String, Object> vo = new HashMap<>(2);
						vo.put("id", area.getId());
						vo.put("name", area.getName());
						return vo; })
					.collect(Collectors.toList());
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw e;
		}
	}

	public String getAreaName(Long id) {
		return areaRepository.findOne(id).getName();
	}

	public Map<String, String> findAreaNames(Set<Long> ids) {
		Map<String, String> vo = new HashMap<>();
		areaRepository
				.findById(ids)
				.forEach(area -> {
					vo.put(String.valueOf(area.getId()), area.getName());
				});
		return vo;
	}
}
