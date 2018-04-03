package com.tea.mservice.manager.log.service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.tea.mservice.config.shiro.UserRealm.ShiroUser;
import com.tea.mservice.core.util.ShiroUserUtil;
import com.tea.mservice.manager.entity.Log;
import com.tea.mservice.manager.log.repository.LogRepository;
import com.tea.mservice.manager.permission.vo.PermissionVo;

@Component
@Configurable
@EnableScheduling
public class LogTasks {

	private static final Logger LOG = LoggerFactory.getLogger(LogTasks.class);

	@Autowired
	private LogRepository logRepository;
	@Value("${is.record.log}")
	private Boolean isRecordLog;

	public static ConcurrentLinkedQueue<Log> queue = new ConcurrentLinkedQueue<Log>();

	// 每5秒执行一次一次
	@Scheduled(cron = "0/5 * *  * * * ")
	public void saveLog() {
		while (true) {
			Log log = queue.poll();
			if (log == null) {
				break;
			}

			try {
				if (isRecordLog) {
					logRepository.save(log);
				}
			} catch (Exception e) {
				LOG.error(log.toString());
				LOG.error(e.getMessage(), e);
			}
		}
	}

	public static void add(Log log) {
		queue.add(log);
	}

	public void recordFilter(List<PermissionVo> list, PermissionVo vo, String path, String method) {

		ShiroUser user = ShiroUserUtil.getUser();
		Log log = new Log();
		log.setUserId(user.id);
		log.setUserName(user.name);
		log.setCrTime(DateFormatUtils.format(new Date(), "yyyyMMddHHmmss"));
		log.setModule(findModule(list, vo));
		log.setOperate(vo.getName());
		log.setContent(path);
		queue.add(log);

		LOG.info(log.toString());
	}

	private String findModule(List<PermissionVo> list, PermissionVo vo) {
		if (vo.getType() == 1) {
			return vo.getName();
		}

		if (vo.getType() == 2) {
			return findParent(list, vo.getParentId());
		}

		return null;
	}

	private String findParent(List<PermissionVo> list, Long parentId) {

		PermissionVo findVo = null;
		for (PermissionVo vo : list) {
			if (parentId.longValue() == vo.getId().longValue()) {
				findVo = vo;
				break;
			}
		}

		if (findVo == null) {
			return null;
		}

		if (findVo.getParentId().longValue() == 0) {
			return findVo.getName();
		}

		if (findVo.getType() == 1) {
			return findVo.getName();
		}

		if (findVo.getType() == 2) {
			findParent(list, findVo.getParentId());
		}

		return null;
	}
}