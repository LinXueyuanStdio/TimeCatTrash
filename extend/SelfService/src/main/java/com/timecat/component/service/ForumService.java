package com.timecat.component.service;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-01-20
 * @description 论坛服务
 * @usage 如果使用第三方云服务商的话，请实现这个服务
 */
public interface ForumService {
    /**
     * 去论坛
     * 如果论坛存在，则跳转
     * 如果论坛不存在，需要创建
     * - 当前用户没有登录：跳到登录界面
     * - 当前用户登录了：该用户成为新论坛的创建着
     *
     * @param name 论坛名
     */
    void gotoForum(String name, String content, String icon);
}
