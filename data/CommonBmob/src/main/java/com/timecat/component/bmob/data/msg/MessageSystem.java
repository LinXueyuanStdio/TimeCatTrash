package com.timecat.component.bmob.data.msg;

import com.timecat.component.bmob.dao.UserDao;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;

/**
 * Created by yc on 2018/1/18.
 * 系统消息
 */
public class MessageSystem implements Message.IMessage {

    private String content;
    private String title;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static class Data {
        @NonNull
        String title;
        @NonNull
        String content;

        public Data(@NonNull String title, @NonNull String content) {
            this.title = title;
            this.content = content;
        }

        @NonNull
        public String getTitle() {
            return title;
        }

        public void setTitle(@NonNull String title) {
            this.title = title;
        }

        @NonNull
        public String getContent() {
            return content;
        }

        public void setContent(@NonNull String content) {
            this.content = content;
        }
    }

    public static Data parse(String s) {
        try {
            JSONObject msg = new JSONObject(s);
            return new Data(msg.optString("title"),
                    msg.optString("content"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    @NonNull
    public String toMessageJson() {
        JSONObject msg = new JSONObject();
        try {
            msg.put("title", title);
            msg.put("content", content);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return msg.toString();
    }

    @Override
    @NonNull
    public Message toMessage() {
        Message message = new Message();
        message.setType(Message.MESSAGE_SYSTEM);
        message.setMessage(this);
        message.setAcceptor(UserDao.getCurrentUser());
        return message;
    }
}
