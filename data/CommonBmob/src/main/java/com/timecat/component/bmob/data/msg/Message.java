package com.timecat.component.bmob.data.msg;

import com.timecat.component.bmob.data._User;

import androidx.annotation.NonNull;
import cn.bmob.v3.BmobObject;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-07-11
 * @description null
 * @usage null
 */
public class Message extends BmobObject {
    private Integer type = 0;//消息类型
    private String message;
    private _User acceptor;

    //region intro
    public static final int MESSAGE_NONE = -1;//不支持的通知格式

    public static final int MESSAGE_SYSTEM = 0;//系统通知
    public static final int MESSAGE_SYSTEM_UP = 8;//系统推荐通知

    public static final int MESSAGE_FANS = 1;//有人关注

    public static final int MESSAGE_BOOKS_LIKE = 2;//笔记本通知
    public static final int MESSAGE_BOOKS_COMMENT = 3;//笔记本通知
    public static final int MESSAGE_BOOKS_REPLY = 4;//笔记本通知

    public static final int MESSAGE_SAYING_LIKE = 5;//语录通知
    public static final int MESSAGE_SAYING_COMMENT = 6;//语录通知
    public static final int MESSAGE_SAYING_REPLY = 7;//语录通知
    //endregion

    //region setter getter
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public void setMessage(IMessage message) {
        this.message = message.toMessageJson();
    }

    public _User getAcceptor() {
        return acceptor;
    }

    public void setAcceptor(_User acceptor) {
        this.acceptor = acceptor;
    }

    //endregion

    public interface IMessage {
        @NonNull
        String toMessageJson();
        @NonNull
        Message toMessage();
    }
}
