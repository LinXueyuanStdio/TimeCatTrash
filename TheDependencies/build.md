```gradle
dependencies {
    api fileTree(dir: 'libs', include: ['*.jar'])

    api project(":component:CommonData")
    api project(":component:CommonSDK")
    api project(":component:CommonResource")

    api project(':widget:widget-colorpicker')
    api project(':widget:widget-messagelist')
    api project(':widget:widget-chatinput')
    api project(':widget:widget-calendar')

    api rootProject.ext.timecat["component-commondata"]
    api rootProject.ext.timecat["component-commonsdk"]
    api rootProject.ext.timecat["component-CommonResource"]

    api rootProject.ext.timecat["widget-calendar"]
    api rootProject.ext.timecat["widget-colorpicker"]
    api rootProject.ext.timecat["widget-messagelist"]
    api rootProject.ext.timecat["widget-chatinput"]

    api rootProject.ext.dependencies["paginate"]
    api rootProject.ext.dependencies["lottie"]
    api rootProject.ext.dependencies["circleimageview"]
    api rootProject.ext.dependencies["BaseRecyclerViewAdapterHelper"]
    api rootProject.ext.dependencies["banner"]

    api rootProject.ext.dependencies["dagger2-android-support"]
    api rootProject.ext.dependencies["banner"]
    api(rootProject.ext.dependencies["cardview-v7"]) {
        exclude module: 'support-annotations'
    }
    api rootProject.ext.dependencies["constraint-layout"]
    api 'com.android.support:support-dynamic-animation:27.1.1'

    //region 以后可能要用到的，被扁平化后的依赖，缓存在这里
    api 'com.github.chenBingX:SuperTextView:v3.1.4'
    api 'com.flyco.tablayout:FlycoTabLayout_Lib:2.1.2@aar'
    implementation 'com.github.xuexiangjys:XUI:1.0.1'

    //endregion

    //region UI
    api 'com.larswerkman:HoloColorPicker:1.5'

    api 'com.github.chenBingX:SuperTextView:v3.1.4'
    api 'com.github.gturedi:stateful-layout:1.2.1'
    api 'com.github.Bakumon:StatusLayoutManager:1.0.4'

    api 'com.flyco.tablayout:FlycoTabLayout_Lib:2.1.2@aar'
    //recyclerview
    api 'com.yanzhenjie:recyclerview-swipe:1.0.2'
    // ExpansionPanel
    api 'com.github.florent37:expansionpanel:1.0.7'
    // round image view(for material getDrawer)
    api 'com.makeramen:roundedimageview:2.3.0'
    api('com.mikepenz:materialdrawer:6.0.3@aar') { transitive = true }

    api 'eu.davidea:flipview:1.1.3'

    //progress bar
    api 'com.akexorcist:RoundCornerProgressBar:2.0.3'
    api 'com.github.iwgang:countdownview:2.1.6'
    //多选
    api 'com.github.jaouan:compoundlayout:1.0.1'
    api 'com.github.XunMengWinter:CircularAnim:0.4.1'
    //web
    api 'com.just.agentweb:agentweb:4.0.2' // (必选)
    api 'com.just.agentweb:download:4.0.2' // (可选)
    api 'com.just.agentweb:filechooser:4.0.2'// (可选)
    //endregion

    api 'com.haibin:calendarview:3.2.7'
    api 'com.qmuiteam:qmui:1.1.2'
    api 'com.scwang.smartrefresh:SmartRefreshLayout:1.0.5-alpha-2'
    api 'eu.davidea:flexible-adapter:5.0.4'
    api 'eu.davidea:flexible-adapter-ui:1.0.0-b4'
    api 'com.mikepenz:iconics-core:2.1.0@aar'
    api 'com.rengwuxian.materialedittext:library:2.0.3'
    //布局
    api 'org.apmem.tools:layouts:1.10@aar'
    // Time and date handling
    api 'joda-time:joda-time:2.9.9'
    // state
    api "com.evernote:android-state:${state_version}"
    //endregion

    //region UI
    // material dialog
    api 'com.afollestad.material-dialogs:commons:0.9.6.0'
    // toast
    api 'com.github.GrenderG:Toasty:1.1.3'
    // snackbar
    api 'com.nispok:snackbar:2.9.0'
    //endregion

    //region 代码
    api "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"
    // mvp 框架
    api "net.grandcentrix.thirtyinch:thirtyinch:${thirtyinchVersion}"
    api "net.grandcentrix.thirtyinch:thirtyinch-rx2:${thirtyinchVersion}"
    //RxLifecycle
    //Rx_lifecycle
    api 'com.trello.rxlifecycle2:rxlifecycle:2.2.2'
    api 'com.trello.rxlifecycle2:rxlifecycle-components:2.2.1'
    // If you want pre-written Activities and Fragments you can subclass as providers
    api 'com.trello.rxlifecycle2:rxlifecycle-navi:2.2.1'  // If you want to use Navi for providers
    api 'com.trello.rxlifecycle2:rxlifecycle-kotlin:2.2.1' // If you want to use Kotlin syntax
    // Event bus
    api 'org.greenrobot:eventbus:3.1.1'
    // Because RxAndroid releases are few and far between, it is recommended you also
    // explicitly depend on RxJava's latest version for bug fixes and new features.
    api 'io.reactivex:rxandroid:1.2.1'
    api 'io.reactivex.rxjava2:rxandroid:2.1.0'
    api 'io.reactivex.rxjava2:rxjava:2.2.4'
    api 'io.reactivex:rxjava:1.3.2'
    // fragment 框架
    api 'me.yokeyword:fragmentation-core:1.3.6'
    // To simplify the communication between Fragments.
    api 'me.yokeyword:eventbus-activity-scope:1.1.0'

    api 'com.github.zagum:Android-ExpandIcon:1.2.1'

    api "android.arch.lifecycle:extensions:$lifecycle_version"
    api "android.arch.lifecycle:common-java8:$lifecycle_version"
    //endregion

    //region MVPArms
    //support
    api(rootProject.ext.dependencies["support-v4"]) {
        exclude module: 'support-annotations'
    }
    api(rootProject.ext.dependencies["appcompat-v7"]) {
        exclude module: 'support-annotations'
    }
    compileOnly rootProject.ext.dependencies["design"]
    api(rootProject.ext.dependencies["recyclerview-v7"]) {
        exclude module: 'support-annotations'
    }
    api rootProject.ext.dependencies["annotations"]

    //view
    compileOnly rootProject.ext.dependencies["autolayout"]
    api(rootProject.ext.dependencies["butterknife"]) {
        exclude module: 'support-annotations'
        exclude module: 'support-compat'
    }

    //rx
    api rootProject.ext.dependencies["rxjava2"]
    api(rootProject.ext.dependencies["rxandroid2"]) {
        exclude module: 'rxjava'
    }
    api(rootProject.ext.dependencies["rxcache2"]) {
        exclude module: 'rxjava'
        exclude module: 'dagger'
        exclude module: 'api'
    }
    implementation(rootProject.ext.dependencies["rxcache-jolyglot-gson"]) {
        exclude module: 'gson'
    }
    api(rootProject.ext.dependencies["rxlifecycle2"]) {
        exclude module: 'rxjava'
        exclude module: 'jsr305'
    }
    api(rootProject.ext.dependencies["rxlifecycle2-android"]) {
        exclude module: 'support-annotations'
        exclude module: 'rxjava'
        exclude module: 'rxandroid'
        exclude module: 'rxlifecycle'
    }
    api(rootProject.ext.dependencies["rxpermissions2"]) {
        exclude module: 'rxjava'
        exclude module: 'support-annotations'
    }
    api rootProject.ext.dependencies['rxerrorhandler2']

    //network
    api(rootProject.ext.dependencies["retrofit"]) {
        exclude module: 'okhttp'
        exclude module: 'okio'
    }
    implementation(rootProject.ext.dependencies["retrofit-converter-gson"]) {
        exclude module: 'gson'
        exclude module: 'okhttp'
        exclude module: 'okio'
        exclude module: 'retrofit'
    }
    implementation(rootProject.ext.dependencies["retrofit-adapter-rxjava2"]) {
        exclude module: 'rxjava'
        exclude module: 'okhttp'
        exclude module: 'retrofit'
        exclude module: 'okio'
    }
    api rootProject.ext.dependencies["okhttp3"]
    compileOnly rootProject.ext.dependencies["glide"]
    annotationProcessor(rootProject.ext.dependencies["glide-compiler"]) {
        exclude module: 'jsr305'
    }

    //tools
    compileOnly rootProject.ext.dependencies["javax.annotation"]
    api rootProject.ext.dependencies["dagger2"]
    annotationProcessor rootProject.ext.dependencies["dagger2-compiler"]
    compileOnly rootProject.ext.dependencies["androideventbus"]
    compileOnly rootProject.ext.dependencies["eventbus"]
    api rootProject.ext.dependencies["gson"]

    //test
    api rootProject.ext.dependencies["timber"]
    //endregion
}
```