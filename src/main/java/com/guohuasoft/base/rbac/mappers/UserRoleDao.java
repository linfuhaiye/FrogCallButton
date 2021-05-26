package com.guohuasoft.base.rbac.mappers;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.guohuasoft.base.rbac.entities.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * 用户角色操作对象
 *
 * @author chenfuqian
 */
@Mapper
@Component
public interface UserRoleDao extends BaseMapper<UserRole> {
}
