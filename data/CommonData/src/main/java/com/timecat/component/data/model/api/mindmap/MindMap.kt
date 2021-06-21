package com.timecat.component.data.model.api.mindmap

/**
 * @author dlink
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019/1/20
 * @description null
 * @usage null
 */
data class MindMap (
        var name: String,
        var children: List<MindMap>
){
    override fun toString(): String {
        return "{name:$name,children:$children}"
    }
}