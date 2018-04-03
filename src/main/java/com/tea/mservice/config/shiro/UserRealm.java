package com.tea.mservice.config.shiro;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Objects;
import com.tea.mservice.config.shiro.exception.DisableUserException;
import com.tea.mservice.config.shiro.exception.NotFoundUserException;
import com.tea.mservice.core.util.Encodes;
import com.tea.mservice.manager.entity.User;
import com.tea.mservice.manager.permission.service.PermissionService;
import com.tea.mservice.manager.roles.service.RoleService;
import com.tea.mservice.manager.users.service.UserService;

/**
 * 验证用户登录
 * 
 * @author Administrator
 */
@Component("userRealm")
public class UserRealm extends AuthorizingRealm {
	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private PermissionService permissionService;

	/*public UserRealm() {
		setName("UserRealm");
		// 采用MD5加密
		setCredentialsMatcher(new HashedCredentialsMatcher("md5"));
	}*/

	// 权限资源角色
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		ShiroUser user = (ShiroUser) principals.getPrimaryPrincipal();
		
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		// add Permission Resources
		info.setStringPermissions(permissionService.findPermissions(user.loginName));
		// add Roles String[Set<String> roles]
		Set<String> roleSet = roleService.findByUserId(user.id);
		// info.setRoles(roles);
		info.setRoles(roleSet);
		return info;
	}

	// 登录验证
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		UsernamePasswordToken upt = (UsernamePasswordToken) token;
		String userName = upt.getUsername();
		User user = userService.findByAccount(userName);

		if (user == null) {
			user = userService.findByMobile(userName);
		}

		if (user == null) {
			throw new NotFoundUserException("用户名或者密码错误");
		}

		Short status = user.getStatus();
		if (status == 0) {
			throw new DisableUserException("用户已经被禁用，请联系管理员");
		}

		byte[] salt = Encodes.decodeHex(user.getSalt());
		return new SimpleAuthenticationInfo(
				new ShiroUser(user.getId(), user.getDeptId(), user.getLoginName(), StringUtils.isEmpty(user.getName()) ? "用户" : user.getName(), user.getStatus(), user.getRoleList(), user.getOrgIds()),
				user.getPassword(), ByteSource.Util.bytes(salt), getName());
	}
	
	/**
	 * 设定Password校验的Hash算法与迭代次数.
	 */
	@PostConstruct
	public void initCredentialsMatcher() {
		HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(UserService.HASH_ALGORITHM);
		matcher.setHashIterations(UserService.HASH_INTERATIONS);

		setCredentialsMatcher(matcher);
	}

	/**
	 * 自定义Authentication对象，使得Subject除了携带用户的登录名外还可以携带更多信息.
	 */
	public static class ShiroUser implements Serializable {
		private static final long serialVersionUID = -1373760761780840081L;
		public Long id;
		public Long deptId;
		public String loginName;
		public String name;
		public Short status;
		public List<String> roleList;
		public List<Long> orgIdList;

		public ShiroUser(Long id, Long deptId, String loginName, String name, Short status, List<String> roleList, List<Long> orgIdList) {
			this.id = id;
			this.deptId = deptId;
			this.loginName = loginName;
			this.name = name;
			this.status = status;
			this.roleList = roleList;
			this.orgIdList = orgIdList;
		}

		public String getName() {
			return name;
		}
		
		/**
		 * 本函数输出将作为默认的<shiro:principal/>输出.
		 */
		@Override
		public String toString() {
			return loginName;
		}

		/**
		 * 重载hashCode,只计算loginName;
		 */
		@Override
		public int hashCode() {
			return Objects.hashCode(loginName);
		}

		/**
		 * 重载equals,只计算loginName;
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			ShiroUser other = (ShiroUser) obj;
			if (loginName == null) {
				if (other.loginName != null) {
					return false;
				}
			} else if (!loginName.equals(other.loginName)) {
				return false;
			}
			return true;
		}
	}
}