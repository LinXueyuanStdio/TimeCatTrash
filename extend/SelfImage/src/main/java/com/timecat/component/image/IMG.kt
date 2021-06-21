package com.timecat.component.image

import android.app.Activity
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import cc.shinichi.library.ImagePreview
import cc.shinichi.library.view.listener.OnBigImagePageChangeListener
import cc.shinichi.library.view.listener.OnOriginProgressListener
import com.luck.picture.lib.PictureSelectionModel
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/6/9
 * @description
 * 统一的图片加载器 图片选择器 图片预览器 图片编辑器
 * @usage null
 */
object IMG {
    /**
     * 图片预览器
     * 提供预配置的 ImagePreview，具体使用时只需要配置3个
     *
     * A. 从第几张图片开始，索引从0开始哦~
     *
     *  .setIndex(0)
     *
     * B. 有三种设置数据集合的方式，根据自己的需求进行三选一：
     *
     * 1：第一步生成的imageInfo List
     *
     *  .setImageInfoList(imageInfoList)
     *
     * 2：直接传url List
     *
     *  .setImageList(List<String> imageList)
     *
     * 3：只有一张图片的情况，可以直接传入这张图片的url
     *
     *  .setImage(String image)
     *
     * C. 开启预览
     *
     *  .start()
     *
     */
    @JvmStatic
    fun preview(context: Activity): ImagePreview {
        // 上下文，必须是activity，不需要担心内存泄漏，本框架已经处理好
        return ImagePreview.getInstance()
            .setContext(context)

            // 从第几张图片开始，索引从0开始哦~
            //=================================================================================================
            //.setIndex(0) TODO
            // 有三种设置数据集合的方式，根据自己的需求进行三选一：TODO
            // 1：第一步生成的imageInfo List
            //=================================================================================================
            // 2：直接传url List
            //.setImageInfoList(imageInfoList)
            // 3：只有一张图片的情况，可以直接传入这张图片的url
            //.setImageList(List<String> imageList)
            //.setImage(String image)
            //=================================================================================================
            // 加载策略，默认为手动模式

            // 保存的文件夹名称，会在Picture目录进行文件夹的新建。比如："BigImageView"，会在Picture目录新建BigImageView文件夹)
            .setLoadStrategy(ImagePreview.LoadStrategy.NetworkAuto)
            // 缩放动画时长，单位ms
            .setFolderName("timecat")
            // 是否显示加载失败的Toast
            .setZoomTransitionDuration(300)
            // 是否启用点击图片关闭。默认启用
            .setShowErrorToast(true)
            // 是否启用下拉关闭。默认不启用
            .setEnableClickClose(true)
            // 是否启用上拉关闭。默认不启用
            .setEnableDragClose(true)
            // 是否显示关闭页面按钮，在页面左下角。默认不显示
            .setEnableUpDragClose(true)
            // 设置关闭按钮图片资源，可不填，默认为库中自带：R.drawable.ic_action_close
            .setShowCloseButton(false)
            // 是否显示下载按钮，在页面右下角。默认显示
            .setCloseIconResId(R.drawable.ic_action_close)
            // 设置下载按钮图片资源，可不填，默认为库中自带：R.drawable.icon_download_new
            .setShowDownButton(true)
            // 设置是否显示顶部的指示器（1/9）默认显示
            .setDownIconResId(R.drawable.icon_download_new)
            // 设置顶部指示器背景shape，默认自带灰色圆角shape
            .setShowIndicator(true)
            // 设置失败时的占位图，默认为库中自带R.drawable.load_failed，设置为 0 时不显示
            .setIndicatorShapeResId(R.drawable.shape_indicator_bg)
            // 点击回调
            .setErrorPlaceHolder(R.drawable.load_failed)
            // ...
            .setBigImageClickListener { activity, view, position ->
                Log.d("dd", "onClick: ")
                // 长按回调
            }
            // ...
            .setBigImageLongClickListener { activity, view, position ->
                Log.d("dd", "onLongClick: ")
                false
                // 页面切换回调
            }
            .setBigImagePageChangeListener(object : OnBigImagePageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    Log.d("dd", "onPageScrolled: ")
                }

                override fun onPageSelected(position: Int) {
                    Log.d("dd", "onPageSelected: ")
                }

                override fun onPageScrollStateChanged(state: Int) {
                    Log.d("dd", "onPageScrollStateChanged: ")
                }
            })
            // 设置查看原图时的百分比样式：库中带有一个样式：ImagePreview.PROGRESS_THEME_CIRCLE_TEXT，使用如下：
            //=================================================================================================
            .setProgressLayoutId(
                ImagePreview.PROGRESS_THEME_CIRCLE_TEXT,
                object : OnOriginProgressListener {
                    override fun progress(parentView: View, progress: Int) {
                        Log.d("dd", "progress: $progress")
                        // 需要找到进度控件并设置百分比，回调中的parentView即传入的布局的根View，可通过parentView找到控件：

                        val progressBar =
                            parentView.findViewById<ProgressBar>(R.id.sh_progress_view)
                        val textView = parentView.findViewById<TextView>(R.id.sh_progress_text)
                        progressBar.progress = progress
                        val progressText = "$progress%"
                        textView.text = progressText
                    }

                    override fun finish(parentView: View) {
                        Log.d("dd", "finish: ")
                    }
                })
        // 使用自定义百分比样式，传入自己的布局，并设置回调，再根据parentView找到进度控件进行百分比的设置：
        //.setProgressLayoutId(R.layout.image_progress_layout_theme_1, new OnOriginProgressListener() {
        //    @Override public void progress(View parentView, int progress) {
        //        Log.d(TAG, "progress: " + progress);
        //
        //        ProgressBar progressBar = parentView.findViewById(R.id.progress_horizontal);
        //        progressBar.setProgress(progress);
        //    }
        //
        //    @Override public void finish(View parentView) {
        //        Log.d(TAG, "finish: ");
        //    }
        //})
        //=================================================================================================
        // 开启预览
        //.start() TODO
    }

    /**
     * 图片选择器
     * 提供预配置的 PictureSelector，具体使用时还要进一步设置
     * 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
     * val selector = PictureSelector.create(context).openGallery(PictureMimeType.ofAll())
     * val selector = PictureSelector.create(context).openGallery(PictureMimeType.ofImage())
     * val selector = PictureSelector.create(context).openGallery(PictureMimeType.ofVideo())
     * val selector = PictureSelector.create(context).openGallery(PictureMimeType.ofAudio())
     * IMG.select(selector).forResult()
     * 常用：
     * 最大选择数       .maxSelectNum(Int)
     * 是否圆形裁剪     .circleDimmedLayer(true)
     * 裁剪比例        .withAspectRatio(aspect_ratio_x, aspect_ratio_y)
     * 是否传入已选图片 .selectionData(mAdapter.getData())
     */
    @JvmStatic
    fun select(selector: PictureSelectionModel): PictureSelectionModel {
        // 进入相册 以下是例子：不需要的api可以不写
        return selector
            // 外部传入图片加载引擎，必传项
            .imageEngine(GlideEngine.createGlideEngine())
            // 主题样式设置 具体参考 values/styles
            // 用法：R.style.picture.white.style v2.3.3后 建议使用setPictureStyle()动态方式
            //.theme(themeId)
            // 是否开启微信图片选择风格
            .isWeChatStyle(false)
            // 是否使用自定义相机
            .isUseCustomCamera(false)
            // 设置语言，默认中文
            //.setLanguage(language)
            // 动态自定义相册主题
            //.setPictureStyle(mPictureParameterStyle)
            // 动态自定义裁剪主题
            //.setPictureCropStyle(mCropParameterStyle)
            // 自定义相册启动退出动画
            //.setPictureWindowAnimationStyle(mWindowAnimationStyle)
            // 图片和视频是否可以同选
            .isWithVideoImage(true)
            // 最大图片选择数量
            //.maxSelectNum(maxSelectNum)
            // 最小选择数量
            //.minSelectNum(1)
            // 视频最小选择数量，如果没有单独设置的需求则可以不设置，同用minSelectNum字段
            //.minVideoSelectNum(1)
            // 视频最大选择数量，如果没有单独设置的需求则可以不设置，同用maxSelectNum字段
            //.maxVideoSelectNum(1)
            // 每行显示个数
            .imageSpanCount(4)
            // 未选择数据时点击按钮是否可以返回
            .isReturnEmpty(false)
            // 是否需要处理Android Q 拷贝至应用沙盒的操作，只针对.isCompress(false); && .isEnableCrop(false);有效,默认处理
            //.isAndroidQTransform(false)
            // 获取图片资源缓存，主要是解决华为10部分机型在拷贝文件过多时会出现卡的问题，这里可以判断只在会出现一直转圈问题机型上使用
            .loadCacheResourcesCallback(GlideCacheEngine.createCacheEngine())
            // 设置相册Activity方向，不设置默认使用系统
            //.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
            // 是否显示原图控制按钮，如果设置为true则用户可以自由选择是否使用原图，压缩、裁剪功能将会失效
            .isOriginalImageControl(true)
            //.cameraFileName("test.png")    // 重命名拍照文件名、注意这个只在使用相机时可以使用，如果使用相机又开启了压缩或裁剪 需要配合压缩和裁剪文件名api
            //.renameCompressFile("test.png")// 重命名压缩文件名、 注意这个不要重复，只适用于单张图压缩使用
            //.renameCropFileName("test.png")// 重命名裁剪文件名、 注意这个不要重复，只适用于单张图裁剪使用
            // 多选 or 单选
            //.selectionMode(if (cb_choose_mode.isChecked()) PictureConfig.MULTIPLE else PictureConfig.SINGLE)
            // 单选模式下是否直接返回，PictureConfig.SINGLE模式下有效
            //.isSingleDirectReturn(true)
            // 是否可预览图片
            .isPreviewImage(true)
            // 是否可预览视频
            .isPreviewVideo(true)
            // 查询指定后缀格式资源
            //.querySpecifiedFormatSuffix(PictureMimeType.ofJPEG())
            // 是否可播放音频
            .isEnablePreviewAudio(true)
            // 是否显示拍照按钮
            .isCamera(true)
            // 多图裁剪时是否支持跳过，默认支持
            //.isMultipleSkipCrop(false)
            // 图片列表点击 缩放效果 默认true
            .isZoomAnim(true)
            // 拍照保存图片格式后缀,默认jpeg
            //.imageFormat(PictureMimeType.PNG)
            // 是否裁剪
            //.isEnableCrop(true)
            // 是否压缩
            .isCompress(true)
            // 图片压缩后输出质量 0~ 100
            .compressQuality(80)
            //同步false或异步true 压缩 默认同步
            .synOrAsy(true)
            //.queryMaxFileSize(10)// 只查多少M以内的图片、视频、音频  单位M
            //.compressSavePath(getPath())//压缩图片保存地址
            //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效 注：已废弃
            //.glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度 注：已废弃
            // 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
            //.withAspectRatio(aspect_ratio_x, aspect_ratio_y)
            // 是否显示uCrop工具栏，默认不显示
            //.hideBottomControls(true)
            // 是否显示gif图片
            .isGif(true)
            // 裁剪框是否可拖拽
            //.freeStyleCropEnabled(true)
            // 是否圆形裁剪
            //.circleDimmedLayer(true)
            // 设置圆形裁剪背景色值
            //.setCircleDimmedColor(ContextCompat.getColor(this, R.color.app_color_white))
            // 设置圆形裁剪边框色值
            //.setCircleDimmedBorderColor(ContextCompat.getColor(getApplicationContext(), R.color.app_color_white))
            // 设置圆形裁剪边框粗细
            //.setCircleStrokeWidth(3)
            // 是否显示裁剪矩形边框 圆形裁剪时建议设为false
            //.showCropFrame(true)
            // 是否显示裁剪矩形网格 圆形裁剪时建议设为false
            //.showCropGrid(true)
            // 是否开启点击声音
            .isOpenClickSound(true)
            // 是否传入已选图片
            //.selectionData(mAdapter.getData())
            // 是否可拖动裁剪框(固定)
            //.isDragFrame(false)
            //.videoMaxSecond(15)
            //.videoMinSecond(10)
            // 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
            .isPreviewEggs(false)
            //.cropCompressQuality(90)// 注：已废弃 改用cutOutQuality()
            // 裁剪输出质量 默认100
            .cutOutQuality(90)
            // 小于100kb的图片不压缩
            .minimumCompressSize(100)
            //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
            // 裁剪是否可旋转图片
            //.rotateEnabled(true)
            // 裁剪是否可放大缩小图片
            .scaleEnabled(true)
        // 视频录制质量 0 or 1
        //.videoQuality()
        // 录制视频秒数 默认60s
        //.recordVideoSecond()
        // 自定义拍照保存路径  注：已废弃
        //.setOutputCameraPath("/CustomPath")
        // 结果回调onActivityResult code
        //.forResult(PictureConfig.CHOOSE_REQUEST)
        //.forResult()
    }

}