# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-keep class com.timecat.component.commonsdk.core.GlobalConfiguration

-keep class com.time.cat.core.app.GlobalConfiguration
-keep class com.timecat.component.data.core.GlobalConfiguration
-keep class **.GlobalConfiguration{*;}
-keep class com.timecat.component.data.model.**{*;}
-keep class com.timecat.component.data.network.**{*;}
-keep class com.timecat.component.setting.**{*;}
-keep class com.timecat.component.data.room.**{*;}
-keep class com.timecat.component.data.database.dao.**{*;}
-keep class com.timecat.component.data.model.bean.**{*;}


############################## 以下为app里的 ###########################################
#保留
#
#-keep {Modifier} {class_specification} 保护指定的类文件和类的成员
#-keepclassmembers {modifier} {class_specification} 保护指定类的成员，如果此类受到保护他们会保护的更好
#-keepclasseswithmembers {class_specification} 保护指定的类和类的成员，但条件是所有指定的类和类成员是要存在。
#-keepnames {class_specification} 保护指定的类和类的成员的名称（如果他们不会压缩步骤中删除）
#-keepclassmembernames {class_specification} 保护指定的类的成员的名称（如果他们不会压缩步骤中删除）
#-keepclasseswithmembernames {class_specification} 保护指定的类和类的成员的名称，如果所有指定的类成员出席（在压缩步骤之后）
#-printseeds {filename} 列出类和类的成员-keep选项的清单，标准输出到给定的文件
#压缩
#
#-dontshrink 不压缩输入的类文件
#-printusage {filename}
#-whyareyoukeeping {class_specification}
#优化
#
#-dontoptimize 不优化输入的类文件
#-assumenosideeffects {class_specification} 优化时假设指定的方法，没有任何副作用
#-allowaccessmodification 优化时允许访问并修改有修饰符的类和类的成员
#混淆
#
#-dontobfuscate 不混淆输入的类文件
#-obfuscationdictionary {filename} 使用给定文件中的关键字作为要混淆方法的名称
#-overloadaggressively 混淆时应用侵入式重载
#-useuniqueclassmembernames 确定统一的混淆类的成员名称来增加混淆
#-flattenpackagehierarchy {package_name} 重新包装所有重命名的包并放在给定的单一包中
#-repackageclass {package_name} 重新包装所有重命名的类文件中放在给定的单一包中
#-dontusemixedcaseclassnames 混淆时不会产生形形色色的类名
#-keepattributes {attribute_name,…} 保护给定的可选属性，例如LineNumberTable, LocalVariableTable, SourceFile, Deprecated, Synthetic, Signature, and InnerClasses.
#-renamesourcefileattribute {string} 设置源文件中给定的字符串常量

#通配符匹配规则
#
#通配符	规则
#？	匹配单个字符
#*	匹配类名中的任何部分，但不包含额外的包名
#**	匹配类名中的任何部分，并且可以包含额外的包名
#%	匹配任何基础类型的类型名
#*	匹配任意类型名 ,包含基础类型/非基础类型
#...	匹配任意数量、任意类型的参数
#<init>	匹配任何构造器
#<ifield>	匹配任何字段名
#<imethod>	匹配任何方法
#*(当用在类内部时)	匹配任何字段和方法
#$	指内部类

#混淆后，会在/build/proguard/目录下输出下面的文件
#
#dump.txt 描述apk文件中所有类文件间的内部结构。
#mapping.txt 列出了原始的类，方法，和字段名与混淆后代码之间的映射。
#seeds.txt 列出了未被混淆的类和成员
#usage.txt 列出了从apk中删除的代码


# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in F:\Java\android\android-sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
#-keepattributes EnclosingMethod
#-dontobfuscate

#-keepattributes SourceFile,LineNumberTable,*Annotation*

# 对亍使用 4.0.3 以上 android-sdk 进行顷目编译时产生异常癿情况时,加入以下内容：
-dontwarn cn.waps.**
-ignorewarnings
-optimizationpasses 5          # 指定代码的压缩级别
-dontusemixedcaseclassnames   # 是否使用大小写混合
-dontpreverify           # 混淆时是否做预校验
-verbose                # 混淆时是否记录日志
-dontskipnonpubliclibraryclasses
-dontwarn org.apache.http.**
-dontwarn android.net.http.AndroidHttpClient
-dontwarn com.android.volley.toolbox.**
-dontwarn android.support.*

-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*  # 混淆时所采用的算法
-keepattributes *Annotation*
-keep @**annotation** class * {*;}

-keep class android.*
-keep class android.graphics.*
-keep class android.view.MiuiWindowManager
-keep class com.yunos.*
-keep class sun.misc.Unsafe
-keep class com.github.*
-keep class com.android.*
-keep class org.apache.*
-keep class org.jetbrains.*
-keep class android.os.*
-keep class kotlin.*
-keep class org.conscrypt.*
-keep class com.taobao.*

-keep public class cn.waps.** {*;}
-keep public interface cn.waps.** {*;}

-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}

-keepclassmembers class ** {
    public void onEvent*(**);
}
-keep public class com.time.cat.R$*{
public static final int *;
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep public class com.umeng.fb.ui.ThreadView { }

-keep class com.qhad.** {*;}

-keep class com.qq.e.** {
    public protected *;
}
-keep class android.support.v4.app.NotificationCompat**{
    public *;
}

-dontwarn javax.annotation.**
-dontwarn javax.inject.**
# OkHttp3
-dontwarn okhttp3.logging.**
-keep class okhttp3.internal.**{*;}
-dontwarn okio.**
# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
#-keepattributes Signature-keepattributes Exceptions
# RxJava RxAndroid
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

# Gson
-keep class com.google.gson.** {*;}
-keep class com.google.gson.stream.** { *; }
-keepattributes EnclosingMethod
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature
# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }
# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }
#这句非常重要，主要是滤掉 com.bgb.scan.model包下的所有.class文件不进行混淆编译
-keep class com.bgb.scan.model.** {*;}


# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on RoboVM on iOS. Will not be used at runtime.
-dontnote retrofit2.Platform$IOS$MainThreadExecutor
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions

-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}
-keep class timecat.xposed.* { *; }
-keep class timecat.xposed.** { *; }

-keep class timecat.* { *; }
-keep class timecat.** { *; }

-keep class org.apache.http.** { *; }
-dontwarn org.apache.http.**
-dontwarn android.net.**
-dontwarn org.jetbrains.annotations.*
-dontwarn com.google.code.**
-dontwarn  org.apache.**
-dontwarn  jp.wasabeef.recyclerview.**
-dontwarn  com.nostra13.universalimageloader.**
-dontwarn  org.acra.**

#wasabeef recyclerview
-keep class jp.wasabeef.recyclerview.** { *; }
-keepattributes Signature
#HTTP Legacy
-keep class org.apache.** { *; }
-keepattributes Signature
#Universal Image Loader
-keep class com.nostra13.universalimageloader.** { *; }
-keep class com.timecat.component.data.database.dao.** { *; }
-keepattributes Signature
#Acra
-keep class org.acra.**  { *; }
-keepattributes Signature
#Support libraries
-keep class com.android.** { *; }
-keepattributes Signature





# Keep the annotations
#-allowaccessmodification
#-renamesourcefileattribute SourceFile
#-keepattributes SourceFile,LineNumberTable
#-repackageclasses ''

-keep class TimeCatHeader$BoundWrapper{*;}
-keep class com.time.cat.data.**
-keep class com.time.cat.test.arc_float_view.**
-keep class com.timecat.component.commonsdk.core.GlobalConfiguration

-keep class com.time.cat.core.app.GlobalConfiguration
-keep class com.timecat.component.data.core.GlobalConfiguration
-keep class **.GlobalConfiguration{*;}
-keep class com.timecat.layout.ui.view.**{*;}
-keep class com.timecat.component.data.model.**{*;}

-dontwarn com.time.cat.ui.widgets.**
-dontwarn com.time.cat.ui.modules.**
-dontwarn com.time.cat.data.network.RxHelper
-dontwarn com.time.cat.ui.adapter.**
-dontwarn com.time.cat.helper.DeviceNameGetter
-dontwarn com.rey.material.**
-dontwarn com.qihoo.**
-dontwarn rx.Completable$CompletableTransformer
-dontwarn rx.**
-dontwarn java.lang.invoke.**



# Suppress duplicate warning for system classes;  Blaze is passing android.jar
# to proguard multiple times.
-dontnote android.**
-dontnote dalvik.**
-dontnote com.android.**
-dontnote google.**
-dontnote com.google.**
-dontnote java.**
-dontnote javax.**
-dontnote junit.**
-dontnote org.apache.**
-dontnote org.json.**
-dontnote org.w3c.dom.**
-dontnote org.xml.sax.**
-dontnote org.xmlpull.v1.**


# Stop warnings about missing unused classes
-dontwarn android.**
-dontwarn dalvik.**
-dontwarn com.android.**
-dontwarn google.**
-dontwarn com.google.**
-dontwarn java.**
-dontwarn javax.**
-dontwarn junit.**
-dontwarn org.apache.**
-dontwarn org.json.**
-dontwarn org.w3c.dom.**
-dontwarn org.xml.sax.**
-dontwarn org.xmlpull.v1.**

# Input/Output Options
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers

# Keep Options
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService

-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.content.Context {
   public void *(android.view.View);
   public void *(android.view.MenuItem);
}

-keepclassmembers class * implements android.os.Parcelable {
    static ** CREATOR;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}
-keepclassmembernames class * {
    java.lang.Class class$(java.lang.String);
    java.lang.Class class$(java.lang.String, boolean);
}
-keepclasseswithmembernames,includedescriptorclasses class * {
    native <methods>;
}

-keepclassmembers,allowoptimization enum * {
    <init>(...);
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}





















#-------------------------------------------定制化区域----------------------------------------------
#---------------------------------1.实体类---------------------------------

-keep class com.time.cat.data.model.entity.**{*;}
-keep class com.time.cat.data.model.**{*;}

#-------------------------------------------------------------------------

#---------------------------------2.第三方包-------------------------------

# aboutlibraries
-dontwarn com.mikepenz.aboutlibraries.**


# eventbus
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

## jsoup
-keeppackagenames org.jsoup.nodes

## Joda Time 2.3

-dontwarn org.joda.convert.**
-dontwarn org.joda.time.**
-keep class org.joda.time.** { *; }
-keep interface org.joda.time.** { *; }

##  materialize
-dontwarn com.mikepenz.materialize.holder.**

## ORMLite

-keepattributes *DatabaseField*
-keepattributes *DatabaseTable*
-keepattributes *SerializedName*
-keep class com.j256.**
-keepclassmembers class com.j256.** { *; }
-keep enum com.j256.**
-keepclassmembers enum com.j256.** { *; }
-keep interface com.j256.**
-keepclassmembers interface com.j256.** { *; }
-dontwarn com.j256.ormlite.android.**
-dontwarn com.j256.ormlite.logger.**
-dontwarn com.j256.ormlite.misc.**
# Keep all model classes that are used by OrmLite
# Also keep their field names and the constructor
-keep @com.j256.ormlite.table.DatabaseTable class * {
    @com.j256.ormlite.field.DatabaseField <fields>;
    @com.j256.ormlite.field.ForeignCollectionField <fields>;
}

## square-okhttp

# OkHttp
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-dontwarn com.squareup.okhttp.**

# okhttp3
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn org.conscrypt.**
# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# retrofit2
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain service method parameters.
-keepclassmembernames,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
# Ignore annotation used for build tooling.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement


# glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}


## aliyun

-keepattributes Signature
-keep class sun.misc.Unsafe { *; }
-keep class com.taobao.** {*;}
-keep class com.alibaba.** {*;}
-keep class com.alipay.** {*;}
-dontwarn com.taobao.**
-dontwarn com.alibaba.**
-dontwarn com.alipay.**
-keep class com.ut.** {*;}
-dontwarn com.ut.**
-keep class com.ta.** {*;}
-dontwarn com.ta.**
-keep class com.alibaba.sdk.android.feedback.** {*;}

-keepattributes InnerClasses
-dontoptimize

# ARouter
-keep public class com.alibaba.android.arouter.routes.**{*;}
-keep public class com.alibaba.android.arouter.facade.**{*;}
-keep class * implements com.alibaba.android.arouter.facade.template.ISyringe{*;}

# 如果使用了 byType 的方式获取 Service，需添加下面规则，保护接口
-keep interface * implements com.alibaba.android.arouter.facade.template.IProvider

# 如果使用了 单类注入，即不定义接口实现 IProvider，需添加下面规则，保护实现
-keep class * implements com.alibaba.android.arouter.facade.template.IProvider

# baidu OCR
-keep class com.baidu.ocr.sdk.**{*;}
-dontwarn com.baidu.ocr.**

# Bomb

-ignorewarnings

-keepattributes Signature,*Annotation*

# 不混淆greenDao类
-dontwarn de.greenrobot.dao.**
-keep class de.greenrobot.dao.** { *;}
-keepclassmembers class * extends de.greenrobot.dao.AbstractDao {
    public static java.lang.String TABLENAME;
}
-keep class **$Properties

-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
public static java.lang.String TABLENAME;
}
-keep class **$Properties { *; }

# If you DO use SQLCipher:
-keep class org.greenrobot.greendao.database.SqlCipherEncryptedHelper { *; }

# If you do NOT use SQLCipher:
-dontwarn net.sqlcipher.database.**
# If you do NOT use RxJava:
-dontwarn rx.**

# keep BmobPush
-dontwarn  cn.bmob.push.**
-keep class cn.bmob.push.** {*;}

# keep okhttp3、okio
-dontwarn okhttp3.**
-keep class okhttp3.** { *;}
-keep interface okhttp3.** { *; }
-dontwarn okio.**

# keep rx
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
 long producerIndex;
 long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
 rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
 rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

# 如果你需要兼容6.0系统，请不要混淆org.apache.http.legacy.jar
-dontwarn android.net.compatibility.**
-dontwarn android.net.http.**
-dontwarn com.android.internal.http.multipart.**
-dontwarn org.apache.commons.**
-dontwarn org.apache.http.**
-keep class android.net.compatibility.**{*;}
-keep class android.net.http.**{*;}
-keep class com.android.internal.http.multipart.**{*;}
-keep class org.apache.commons.**{*;}
-keep class org.apache.http.**{*;}

# 讯飞
-dontwarn com.iflytek.speek.**
-keep class com.iflytek.speek.** {*;}
-keep class com.iflytek.**{*;}
-keepattributes Signature

# chat
#FOR CHATKIT

-keep class * extends com.stfalcon.chatkit.messages.MessageHolders$OutcomingTextMessageViewHolder {
     public <init>(android.view.View, java.lang.Object);
     public <init>(android.view.View);
 }
-keep class * extends com.stfalcon.chatkit.messages.MessageHolders$IncomingTextMessageViewHolder {
     public <init>(android.view.View, java.lang.Object);
     public <init>(android.view.View);
 }
-keep class * extends com.stfalcon.chatkit.messages.MessageHolders$IncomingImageMessageViewHolder {
     public <init>(android.view.View, java.lang.Object);
     public <init>(android.view.View);
 }
-keep class * extends com.stfalcon.chatkit.messages.MessageHolders$OutcomingImageMessageViewHolder {
     public <init>(android.view.View, java.lang.Object);
     public <init>(android.view.View);
 }
-keep class * extends com.stfalcon.chatkit.messages.MessageHolders$OutcomingTextMessageViewHolder {
     public <init>(android.view.View, java.lang.Object);
     public <init>(android.view.View);
 }
-keep class * extends cn.jiguang.imui.** {*;}
-keep class * extends io.github.rockerhieu.** {*;}
#-------------------------------------------------------------------------

#---------------------------------3.与js互相调用的类------------------------

#-keepclasseswithmembers class com.demo.login.bean.ui.MarkdownEditorActivity$JSInterface {
#      ;
#}
-keep class com.just.agentweb.** {
    *;
}
-dontwarn com.just.agentweb.**
-keepclassmembers class com.just.agentweb.sample.common.AndroidInterface{ *; }
-keepclassmembers class ** extends WebView {*;}
-keepclassmembers class com.time.cat.views.editor.EditAreaView.JavascriptApi {*;}
-keepclassmembers class com.jecelyin.editor.v2.widget.text.EditAreaView.JavascriptApi {*;}

#-------------------------------------------------------------------------

#---------------------------------4.反射相关的类和方法-----------------------

#TODO 我的工程里没有。。。

#----------------------------------------------------------------------------
#---------------------------------------------------------------------------------------------------

#-------------------------------------------基本不用动区域--------------------------------------------
#---------------------------------基本指令区----------------------------------
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontpreverify
-verbose
-printmapping proguardMapping.txt
-optimizations !code/simplification/cast,!field/*,!class/merging/*
-keepattributes *Annotation*,InnerClasses
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable
#----------------------------------------------------------------------------

#---------------------------------默认保留区---------------------------------
-keep public class * extends android.app.Activity      # 保持哪些类不被混淆
-keep public class * extends android.app.Application   # 保持哪些类不被混淆
-keep public class * extends android.app.Service       # 保持哪些类不被混淆
-keep public class * extends android.content.BroadcastReceiver  # 保持哪些类不被混淆
-keep public class * extends android.content.ContentProvider    # 保持哪些类不被混淆
-keep public class * extends android.app.backup.BackupAgentHelper # 保持哪些类不被混淆
-keep public class * extends android.preference.Preference        # 保持哪些类不被混淆
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService    # 保持哪些类不被混淆
-keep class android.support.** {*;}

-keepclasseswithmembernames class * {  # 保持 native 方法不被混淆
    native <methods>;
}
-keepclasseswithmembers class * {   # 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {   # 保持自定义控件类不被混淆
    public <init>(android.content.Context);
}
-keepclasseswithmembers class * {# 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * extends android.app.Activity { # 保持自定义控件类不被混淆
    public void *(android.view.View);
}
# keep setters in Views so that animations can still work.
-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}
-keepclassmembers enum * {     # 保持枚举 enum 类不被混淆
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class * implements android.os.Parcelable { # 保持 Parcelable 不被混淆
    public static final android.os.Parcelable$Creator *;
}
-keepclassmembers class * implements android.os.Parcelable {
 public <fields>;
 private <fields>;
}

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
#----------------------------------------------------------------------------

#---------------------------------webview------------------------------------
#保持JS不混淆。
#1.保留annotation， 例如 @JavascriptInterface 等 annotation
-keepattributes Annotation

#2.保留跟 javascript相关的属性
-keepattributes JavascriptInterface

#3.保留JavascriptInterface中的方法
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.webView, jav.lang.String);
}
#----------------------------------------------------------------------------
#---------------------------------------------------------------------------------------------------