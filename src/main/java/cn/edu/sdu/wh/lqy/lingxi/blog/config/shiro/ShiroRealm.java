//package cn.edu.sdu.wh.lqy.lingxi.blog.config.shiro;
//
//import cn.edu.sdu.wh.lqy.lingxi.blog.model.Vo.AuthorizingUser;
//import cn.edu.sdu.wh.lqy.lingxi.blog.model.Vo.User;
//import cn.edu.sdu.wh.lqy.lingxi.blog.service.IUserService;
//import org.apache.shiro.SecurityUtils;
//import org.apache.shiro.authc.*;
//import org.apache.shiro.authz.AuthorizationInfo;
//import org.apache.shiro.authz.SimpleAuthorizationInfo;
//import org.apache.shiro.realm.AuthorizingRealm;
//import org.apache.shiro.subject.PrincipalCollection;
//import org.apache.shiro.util.ByteSource;
//import org.springframework.beans.BeanUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//
//public class ShiroRealm extends AuthorizingRealm {
//
//    @Autowired
//    private IUserService userService;
////	@Autowired
////	private RoleService roleService;
////	@Autowired
////	private MenuService menuService;
//
//    @Override
//    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
//
//        User user = (User) SecurityUtils.getSubject().getPrincipal();
//        String userName = user.getUsername();
//
//        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
//
////		List<Role> roleList = this.roleService.findUserRole(userName);
////		Set<String> roleSet = new HashSet<String>();
////		for (Role r : roleList) {
////			roleSet.add(r.getRoleName());
////		}
////		simpleAuthorizationInfo.setRoles(roleSet);
//
////		List<Menu> permissionList = this.menuService.findUserPermissions(userName);
////		Set<String> permissionSet = new HashSet<String>();
////		for (Menu m : permissionList) {
////			permissionSet.add(m.getPerms());
////		}
////		simpleAuthorizationInfo.setStringPermissions(permissionSet);
//
//        return simpleAuthorizationInfo;
//    }
//
//    @Override
//    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
//
//        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
//        User user = userService.getUserByName(token.getUsername());
//
//        if (user == null) {
//            throw new UnknownAccountException();// 没找到帐号
//        }
//
//        AuthorizingUser authorizingUser = new AuthorizingUser();
//        BeanUtils.copyProperties(user, authorizingUser);
//
//        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(authorizingUser, user.getPassword(),
//                ByteSource.Util.bytes(authorizingUser.getCredentialsSalt()), getName());
//
//        return authenticationInfo;
//    }
//
//}
