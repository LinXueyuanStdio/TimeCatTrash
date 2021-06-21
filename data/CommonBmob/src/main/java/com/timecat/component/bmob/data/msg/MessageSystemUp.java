package com.timecat.component.bmob.data.msg;

import com.timecat.component.bmob.dao.UserDao;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;

/**
 * Created by yc on 2018/1/18.
 * 系统消息
 */
public class MessageSystemUp implements Message.IMessage {

    private String content;
    private String title;
    private String objId;
    private String objContent;
    private int objType;

    public static final int TYPE_SAYING = 0;

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

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    public String getObjContent() {
        return objContent;
    }

    public void setObjContent(String objContent) {
        this.objContent = objContent;
    }

    public int getObjType() {
        return objType;
    }

    public void setObjType(int objType) {
        this.objType = objType;
    }

    public static class Data {
        String title;
        String content;
        String objId;
        String objContent;
        Integer objType;

        public Data(String title, String content, String objId, String objContent, int objType) {
            this.title = title;
            this.content = content;
            this.objId = objId;
            this.objContent = objContent;
            this.objType = objType;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getObjId() {
            return objId;
        }

        public void setObjId(String objId) {
            this.objId = objId;
        }

        public String getObjContent() {
            return objContent;
        }

        public void setObjContent(String objContent) {
            this.objContent = objContent;
        }

        public int getObjType() {
            return objType;
        }

        public void setObjType(int objType) {
            this.objType = objType;
        }
    }

    public static Data parse(String s) {
        try {
            JSONObject msg = new JSONObject(s);
            return new Data(msg.optString("title"),
                    msg.optString("content"),
                    msg.optString("objId"),
                    msg.optString("objContent"),
                    msg.optInt("objType"));
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
            msg.put("objId", objId);
            msg.put("objContent", objContent);
            msg.put("objType", objType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return msg.toString();
    }

    @Override
    @NonNull
    public Message toMessage() {
        Message message = new Message();
        message.setType(Message.MESSAGE_SYSTEM_UP);
        message.setMessage(this);
        message.setAcceptor(UserDao.getCurrentUser());
        return message;
    }
}
