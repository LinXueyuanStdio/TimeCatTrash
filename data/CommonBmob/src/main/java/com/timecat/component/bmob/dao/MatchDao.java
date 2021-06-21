package com.timecat.component.bmob.dao;

import com.timecat.component.bmob.data.social.Match;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-07-11
 * @description null
 * @usage null
 */
public class MatchDao {
    public static void getMatch(int page, int limit, final FindListener<Match> listener) {
        BmobQuery<Match> query = new BmobQuery<>();
        query.order("-updatedAt");
        query.include("user");
        query.setLimit(limit);
        query.setSkip(page*limit-limit);
        query.findObjects(new FindListener<Match>() {
            @Override
            public void done(List<Match> list, BmobException e) {
                if (e == null) {
                    if (list != null && list.size() > 0) {
                        listener.done(list, null);
                    } else {
                        listener.done(null, null);
                    }
                } else {
                    listener.done(list, e);
                }
            }
        });
    }

    /**
     * 增加一个正在匹配中
     */
    public static void newMatch(SaveListener<Match> saveListener) {
        Match match = new Match(UserDao.getCurrentUser());
        match.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                match.setObjectId(s);
                saveListener.done(match, e);
            }
        });
    }

    /**
     * 停止匹配
     */
    public static void stopMatch(Match match) {
        // 删除匹配信息
        match.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {

            }
        });
    }

}
