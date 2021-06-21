package com.timecat.component.bmob.data.trace;

import androidx.annotation.NonNull;

import com.timecat.component.bmob.dao.UserDao;
import com.timecat.component.bmob.data._User;

import cn.bmob.v3.BmobObject;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-07-12
 * @description 足迹
 * @usage null
 */
public class Trace extends BmobObject {
    private Integer type = 0;
    private _User user;//谁的足迹

    //region 关注人
    public static final int FOLLOW_USER = 0;//关注人
    private _User target;
    //endregion

    public Trace() {
        super();
    }

    public Trace(_User user) {
        this();
        setUser(user);
    }

    public Trace(_User user, _User saying) {
        this(user);
        setTarget(saying);
    }

    //region getter and setter

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @NonNull
    public _User getUser() {
        if (user == null) {
            setUser(UserDao.getCurrentUser());
        }
        return user;
    }

    public void setUser(_User user) {
        this.user = user;
    }

    public _User getTarget() {
        return target;
    }

    public void setTarget(_User target) {
        this.target = target;
    }
    //endregion
}
