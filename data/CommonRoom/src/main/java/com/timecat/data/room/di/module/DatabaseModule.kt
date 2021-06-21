package com.timecat.data.room.di.module

import android.content.Context
import com.timecat.data.room.TimeCatRoomDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/8/2
 * @description 数据库注入模块
 * @usage null
 */
@Module
internal class DatabaseModule(val testing: Boolean = false) {
    @Provides
    @Singleton
    internal fun provideRoomDatabase(context: Context): TimeCatRoomDatabase {
        return if (testing) {
            TimeCatRoomDatabase.forFile(context, TimeCatRoomDatabase.NAME_FOR_TESTS)
            // return OrgzlyDatabase.forMemory(context)
        } else {
            TimeCatRoomDatabase.forFile(context, TimeCatRoomDatabase.NAME)
        }
    }
}