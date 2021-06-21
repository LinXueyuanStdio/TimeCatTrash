package com.timecat.component.bmob.dao;

import com.timecat.component.bmob.data._User;
import com.timecat.component.bmob.data.trace.Trace;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-07-13
 * @description 足迹，原子操作
 * @usage null
 */
public class TraceDao {
    public static BmobQuery<Trace> getAllTrace(_User user){
        BmobQuery<Trace> query = new BmobQuery<>();
        query.addWhereEqualTo("user", user);
        query.order("-updatedAt");
        query.include("user,target,collection,saying,note,collection.userId,saying.userId");
        return query;
    }

    public static void traceFollow(_User user, _User target) {
        Trace trace = new Trace(user, target);
        trace.setType(Trace.FOLLOW_USER);
        trace.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {

            }
        });
    }
}
