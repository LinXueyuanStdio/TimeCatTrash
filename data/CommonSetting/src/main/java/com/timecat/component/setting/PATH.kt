package com.timecat.component.setting

import android.content.Context
import android.net.Uri
import com.timecat.extend.arms.BaseApplication
import java.io.File
import java.util.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/5/26
 * @description 规范应用的文件管理
 * 分区：私有：仅自己使用，卸载后删除
 *      共享：可分享给其他应用
 * 场景：
 * 1. 下载：共享：如doc文件
 *         私有：如 cache 、图片、用户生成内容UGC
 * 2. 使用：视频播放器、音乐播放器、图片浏览器等等
 *
 * 目标：
 * 1. 提供本地到云端等地方的一一映射。故只能保存相对路径，prefix可以动态化
 * 2. 运行时path，如果path中多个目录不存在时，在运行时再创建，不要在初始化时创建
 * 3. 标准目录下，按时间再细分目录
 *   Music
 *     2020-09-01
 *     2020-09-02
 *     2020-09-12
 *     2020-10-12
 * 4. 拦截器：动态改变path，在迁移数据时很有用
 * 5. 不动点是Uri，对其他的path，file，url，relativePath等，都要转化为Uri
 * 6. 权限控制
 * 7. 以静态方法的形式提供调用，调用者只需要做选择
 * 8. 提供自动文件名，调用者不需要思考文件名
 * @usage null
 */
object PATH {
    /**
     * 只返回相对路径 relative path
     *
     * dir/filename
     */
    @JvmOverloads
    fun of(dir: DIR, filename: String = random()): String {
        return "${dir.dirName}/$filename"
    }

    fun of(prefix: String, child: String): Uri {
        return Uri.fromFile(File(prefix, child))
    }

    fun random(): String {
        return UUID.randomUUID().toString()
    }

    /**
     * 外部存储，如果不存在则返回内部存储
     * /storage/emulated/0/Android/data/<PackageName>/files
     * /storage/emulated/0/Android/data/<PackageName>/files/<dir>
     *
     * 不需要权限，随意读写
     */
    fun over(context: Context = BaseApplication.getContext(), dir: DIR = DIR.Empty): String {
        val path = context.getExternalFilesDir(dir.dirName)?.absolutePath ?: under(context)
        assertExistDir(path)
        return path
    }

    /**
     * 内部存储
     * /data/user/0/<PackageName>/files
     * /data/user/0/<PackageName>/files/<dir>
     *
     * 不需要权限，随意读写
     */
    fun under(context: Context = BaseApplication.getContext(), dir: DIR = DIR.Empty): String {
        val dirname = if (dir.dirName.isEmpty()) "" else "/${dir.dirName}"
        val path = "${context.filesDir.absolutePath}${dirname}"
        assertExistDir(path)
        return path
    }

    fun assertExistDir(path: String) {
        val f = File(path)
        if (!f.exists()) f.mkdirs()
    }

    fun overOf(
        context: Context = BaseApplication.getContext(),
        dir: DIR = DIR.Cache,
        filename: String = random()
    ): Uri {
        return of(over(context), of(dir, filename))
    }

    fun underOf(
        context: Context = BaseApplication.getContext(),
        dir: DIR = DIR.Cache,
        filename: String = random()
    ): Uri {
        return of(under(context), of(dir, filename))
    }

    /**
     * 随机生成的临时文件
     *
     * /storage/emulated/0/Android/data/<PackageName>/files/Temp/uuid.emp
     */
    fun randomTemp(): Uri {
        return overOf(dir = DIR.Temp, filename = "${random()}.tmp")
    }

}