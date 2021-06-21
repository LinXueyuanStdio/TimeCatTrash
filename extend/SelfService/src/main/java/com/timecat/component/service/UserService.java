package com.timecat.component.service;

import androidx.annotation.NonNull;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-01-20
 * @description 用户服务，如果使用第三方云服务商的话，请实现这个服务
 * @usage null
 */
public interface UserService {
    @NonNull String getUsername();
    @NonNull String getEmail();
    @NonNull String getAvatar();
    @NonNull String getCoverPageUrl();
    @NonNull String getObjectId();
    boolean isLogin();
}
