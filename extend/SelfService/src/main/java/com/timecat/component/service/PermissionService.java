package com.timecat.component.service;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-01-20
 * @description 论坛服务
 * @usage 如果使用第三方云服务商的话，请实现这个服务
 */
public interface PermissionService {
    /**
     * 检查是否有权限
     *
     * @param permissionId 权限id
     */
    void validate(String permissionId, Callback callback);

    interface Callback {
        void onPass();
    }

    void initPermission();
}
