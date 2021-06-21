package com.timecat.component.setting

import android.os.Environment

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/7/1
 * @description 时光猫标准文件夹
 * @usage null
 */
enum class DIR(val dirName: String, val descName: String) {
    //标准文件夹
    Music(Environment.DIRECTORY_MUSIC, "音乐"),
    Podcasts(Environment.DIRECTORY_PODCASTS, "播客"),
    Ringtones(Environment.DIRECTORY_RINGTONES, "铃声"),
    Alarms(Environment.DIRECTORY_ALARMS, "闹钟"),
    Notification(Environment.DIRECTORY_NOTIFICATIONS, "通知"),
    Pictures(Environment.DIRECTORY_PICTURES, "图片"),
    Movies(Environment.DIRECTORY_MOVIES, "电影"),
    Downloads(Environment.DIRECTORY_DOWNLOADS, "下载"),
    DCIM(Environment.DIRECTORY_DCIM, "照片"),
    Document(Environment.DIRECTORY_DOCUMENTS, "文档"),

    //时光猫拓展文件夹
    Empty("", "空文件夹"),
    Temp("Temp", "临时文件夹，里面的文件用后即删"),
    Cache("Cache", "缓存"),
    Chat("Chat", "聊天历史"),
    Thumb("Thumb", "缩略图"),
    Voice("Voice", "录音"),
    Markdown("Markdown", "MD文件"),
    Org("Org", "Org文件"),
    Block("Block", "块文件"),
    MindMap("MindMap", "思维导图文件"),
    Plugin("Plugin", "插件文件"),
    Graft("Graft", "嫁接文件"),
    Novel("Novel", "小说文件"),
    Book("Book", "书文件"),
    Comic("Comic", "漫画文件"),
    Subscribe("Subscribe", "订阅文件"),
    NovelSource("NovelSource", "小说书源管理文件"),
}

