package com.timecat.component.bmob.dao;

import com.timecat.component.bmob.data._User;
import com.timecat.component.bmob.data.msg.Message;
import com.timecat.component.bmob.data.msg.MessageFans;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-07-12
 * @description 消息操作，视为单个模型原子操作
 * @usage null
 */
public class MessageDao {
    public static void sendMessage(Message.IMessage message) {
        sendMessage(message, new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {

            }
        });
    }

    public static void sendMessage(Message.IMessage message, SaveListener<String> stringSaveListener) {
        sendMessage(message, UserDao.getCurrentUser(), stringSaveListener);
    }

    public static void sendMessage(Message.IMessage message, _User receptor, SaveListener<String> stringSaveListener) {
        Message msg = message.toMessage();
        msg.setAcceptor(receptor);
        msg.save(stringSaveListener);
    }

    public static void sendFollowUser(_User user, _User target) {
        sendMessage(new MessageFans(user, target));
    }
}
