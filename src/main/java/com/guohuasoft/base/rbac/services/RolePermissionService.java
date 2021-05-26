package com.guohuasoft.base.rbac.services;

import com.guohuasoft.base.rbac.entities.RolePermission;
import com.guohuasoft.base.rbac.mappers.RolePermissionDao;
import com.guohuasoft.base.services.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 角色权限服务
 *
 * @author chenfuqian
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RolePermissionService extends BaseService<RolePermissionDao, RolePermission> {
    protected RolePermissionService(RolePermissionDao baseMapper) {
        super(baseMapper);
    }
}
