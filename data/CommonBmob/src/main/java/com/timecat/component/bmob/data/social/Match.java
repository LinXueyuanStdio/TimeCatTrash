package com.timecat.component.bmob.data.social;

import com.timecat.component.bmob.dao.UserDao;
import com.timecat.component.bmob.data._User;

import cn.bmob.v3.BmobObject;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-07-11
 * @description null
 * @usage null
 */
public class Match extends BmobObject {
    private _User user;

    public _User getUser() {
        return user;
    }

    public void setUser(_User user) {
        this.user = user;
    }

    public Match(_User accountId) {
        this.user = accountId;
    }

    public Match() {
        this.user = UserDao.getCurrentUser();
    }
}
