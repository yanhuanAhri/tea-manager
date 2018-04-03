package com.tea.mservice.config.shiro;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.tea.mservice.config.shiro.UserRealm.ShiroUser;
import com.tea.mservice.manager.entity.Log;
import com.tea.mservice.manager.log.service.LogTasks;

@Component
public class MyShiroSessionListener implements SessionListener {

	private static final Logger LOG = LoggerFactory.getLogger(MyShiroSessionListener.class);

	private final AtomicInteger sessionCount = new AtomicInteger(0);

	@Override
	public void onStart(Session session) {
		sessionCount.incrementAndGet();
		LOG.info("--------->onStart, session id={}, session count={}", session.getId(), getSessionCount());
	}

	@Override
	public void onStop(Session session) {
		sessionCount.decrementAndGet();
		LOG.info("--------->onStop, session id={}, session count={}", session.getId(), getSessionCount());
		
		Object obj = session.getAttribute("SHIRO_USER_OBJ");
		addLog("登出", "用户注销登出", "/logout", obj);
	}

	@Override
	public void onExpiration(Session session) {
		sessionCount.decrementAndGet();
		LOG.info("--------->onExpiration, session count={}", getSessionCount());
		
		Object obj = session.getAttribute("SHIRO_USER_OBJ");
		addLog("登出", "用户会话过期登出", "/logout", obj);
	}

	public int getSessionCount() {
		return sessionCount.get();
	}
	
	private void addLog(String module, String operate, String content, Object obj) {
		try {
			ShiroUser user = (ShiroUser) obj;
			if(user == null) {
				return;
			}
			Log log = new Log();
			log.setUserId(user.id);
			log.setUserName(user.name);
			log.setCrTime(DateFormatUtils.format(new Date(), "yyyyMMddHHmmss"));
			log.setModule(module);
			log.setOperate(operate);
			log.setContent(content);
			LogTasks.add(log);

			LOG.info("id={}, login name={}, name={}, module={}, operate={}, content={}", user.id, user.loginName,
					user.name, module, operate, content);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}

}