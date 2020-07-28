package com.timecat.module.search.fragment

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.BmobUser
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.timecat.component.alert.ToastUtil
import com.timecat.component.bmob.dao.UserDao
import com.timecat.component.bmob.data._User
import com.timecat.component.commonbase.friend.list.BaseStatefulListFragment
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.component.data.core.LoginNavigationCallbackImpl
import com.timecat.component.readonly.RouterHub
import com.timecat.component.resource.base.Attr
import com.timecat.component.ui.utils.IconLoader
import com.timecat.module.search.vm.SearchViewModel
import com.xiaoxing.search.R

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/6/8
 * @description 搜索 用户名 或 uid
 * @usage null
 */
@Route(path = RouterHub.SEARCH_SearchUserFragment)
class SearchUserFragment : BaseStatefulListFragment() {
    private lateinit var searchResultAdapter: SearchResultAdapter

    /**
     * 当前为未关注状态
     */
    private val UNFOLLOW = 0

    /**
     * 当前为已关注状态
     */
    private val FOLLOW = 1
    private var followeList_id: String? = null //当前用户对应的粉丝user_followers的object
    private lateinit var searchViewModel: SearchViewModel

    override fun getAdapter(): RecyclerView.Adapter<out RecyclerView.ViewHolder> {
        searchResultAdapter = SearchResultAdapter(requireContext(), ArrayList())
        return searchResultAdapter
    }

    override fun loadData() {
        searchViewModel = ViewModelProviders.of(activity!!).get(SearchViewModel::class.java)
        searchViewModel.searchText.observe(this, Observer {
            it?.let { onSearch(it) }
        })
        mStatefulLayout?.showEmpty()
    }

    private fun onSearch(q: String) {
        searchUser(q, true)
    }

    private fun searchUser(text: String?, debug: Boolean) {
        LogUtil.se(text)
        mStatefulLayout.showLoading()
        //本来是可以直接使用bmob的模糊查询的，但是要付费，所以只能另辟蹊径
        val query1 = BmobQuery<_User>().or(mutableListOf(
            BmobQuery<_User>().apply { addWhereContains("username", text) },
            BmobQuery<_User>().apply { addWhereContains("objectId", text) }
        ))
        query1.findObjects(object : FindListener<_User>() {
            override fun done(list: List<_User>?, e: BmobException?) {
                if (e == null) {
                    if (list == null || list.isEmpty()) {
                        if (debug) {
                            ToastUtil.w("抱歉，查无此用户")
                        }
                        mStatefulLayout.showEmpty()
                    } else {
                        mStatefulLayout.showContent()
                        searchResultAdapter.replaceData(list)
                    }
                } else {
                    ToastUtil.e("查询失败")
                }
            }
        })
    }

    inner class SearchResultAdapter(
        private val mContext: Context,
        data: MutableList<_User>
    ) : BaseQuickAdapter<_User, BaseViewHolder>(R.layout.search_item_user, data) {
        override fun convert(helper: BaseViewHolder, item: _User?) {
            item?.let {
                LogUtil.e(item.toString())
                IconLoader.loadIcon(mContext, helper.getView(R.id.img), item.avatar)
                helper.setText(R.id.tv_name, "${item.username}\nid : ${it.objectId}")
                helper.getView<View>(R.id.tv_name)
                    .setOnClickListener {
                        LogUtil.e(item.toString())
                        ARouter.getInstance().build(RouterHub.USER_UserDetailActivity)
                            .withString("userId", item.objectId)
                            .navigation(requireContext(), LoginNavigationCallbackImpl())
                    }
                helper.getView<View>(R.id.follow).tag = UNFOLLOW
                helper.getView<View>(R.id.follow)
                    .setOnClickListener { v -> followOrUnfollow(item.objectId, v as Button) }
            }
        }

    }

    //region follow
    private fun followOrUnfollow(
        hisId: String,
        followOrNot: Button
    ) {
        val you = BmobUser.getCurrentUser(_User::class.java)
        //you are the User, he is who you are looking at.
        val UserRelation = you.relation
        if (UserRelation != null) {
            followeList_id = UserRelation.objectId
        }
        val he = _User()
        he.objectId = hisId
        if (followOrNot.tag == UNFOLLOW) {
            follow(you, he, followOrNot)
        } else {
            unfollow(you, he, followOrNot)
        }
    }

    /**
     * you follow he, will update whichIsNotFocusedNow button
     *
     * @param you                  current login user
     * @param he                   who are watched
     * @param whichIsNotFocusedNow tv
     */
    private fun follow(you: _User, he: _User, whichIsNotFocusedNow: TextView) {
        UserDao.follow(you, he, object : UserDao.FollowUserListener {
            override fun onFollowSuccess() {
                whichIsNotFocusedNow.setBackgroundResource(R.drawable.shape_4)
                whichIsNotFocusedNow.text = "已关注"
                whichIsNotFocusedNow.setTextColor(Attr.getPrimaryTextColor(requireContext()))
                whichIsNotFocusedNow.tag = FOLLOW
                ToastUtil.ok("关注成功")
            }

            override fun onFollowFail(e: BmobException) {
                ToastUtil.ok("关注失败")
                LogUtil.e(e.toString())
            }
        })
    }

    /**
     * you unfollow he, will update whichIsNotFocusedNow button
     *
     * @param you                  current login user
     * @param he                   who are watched
     * @param whichIsNotFocusedNow tv
     */
    private fun unfollow(you: _User, he: _User, whichIsNotFocusedNow: TextView) {
        // 移除
        UserDao.unFollow(you, he, object : UserDao.UnFollowUserListener {
            override fun onUnFollowSuccess() {
                whichIsNotFocusedNow.setBackgroundResource(R.drawable.shape_3)
                whichIsNotFocusedNow.text = "关注"
                whichIsNotFocusedNow.setTextColor(Attr.getPrimaryTextColorReverse(requireContext()))
                whichIsNotFocusedNow.tag = UNFOLLOW
                ToastUtil.ok("解除粉丝成功")
            }

            override fun onUnFollowFail(e: BmobException) {
                ToastUtil.e("解除粉丝失败")
                LogUtil.e(e.toString())
            }
        })
    }
    //endregion
}