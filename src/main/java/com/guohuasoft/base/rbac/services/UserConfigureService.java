package com.guohuasoft.base.rbac.services;


import com.guohuasoft.base.misc.SecurityUtils;
import com.guohuasoft.base.rbac.entities.UserConfigure;
import com.guohuasoft.base.rbac.mappers.UserConfigureDao;
import com.guohuasoft.base.services.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 个人配置服务
 *
 * @author linyehai
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserConfigureService extends BaseService<UserConfigureDao, UserConfigure> {
    private final UserConfigureDao userConfigureDao;

    protected UserConfigureService(UserConfigureDao userConfigureDao) {
        super(userConfigureDao);
        this.userConfigureDao = userConfigureDao;
    }

    /**
     * 通过用户ID获取个人配置信息
     *
     * @param userId 用户ID
     * @return 用户个人配置信息
     */
    public com.guohuasoft.base.rbac.models.UserConfigure getUserConfigureByUserId(Long userId) {
        return userConfigureDao.getUserConfigureByUserId(userId);
    }

    /**
     * 更新用户个人配置。存在则修改，不存在则新增
     *
     * @param userConfigure 用户个人配置信息
     */
    public void updateUserConfigure(UserConfigure userConfigure) {
        com.guohuasoft.base.rbac.models.UserConfigure userConfigureModel = userConfigureDao.getUserConfigureByUserId(SecurityUtils.getCurrentUser().getId());
        userConfigure.setUserId(SecurityUtils.getCurrentUser().getId());
        if (userConfigureModel != null) {
            updateById(userConfigure);
        } else {
            add(userConfigure);
        }
    }
}
