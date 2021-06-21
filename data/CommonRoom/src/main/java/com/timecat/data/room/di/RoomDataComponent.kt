package com.timecat.data.room.di

import com.timecat.data.room.di.module.DatabaseModule
import dagger.Component
import javax.inject.Singleton

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/8/2
 * @description Room数据模块
 * @usage null
 */
@Singleton
@Component(modules = [
    DatabaseModule::class
])
interface RoomDataComponent {
}