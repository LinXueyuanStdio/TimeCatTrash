public long id;//ID唯一主键
//外键---->
public DBUser user;
public DBSubPlan subplan;
//----------------------------------------------------------------------------------
public String url;// task的url 访问该url可返回该task
public String title;//日程标题
public String content;//日程内容

public String attachment;//附件
public Integer label = LABEL_IMPORTANT_URGENT;//重要紧急标签,重要紧急=0，重要不紧急=1，紧急不重要=2，不重要不紧急=3

public Integer showType = FOLD;//显示类型：0:收缩，1:展开
public int itemIndex = 0;//在子计划里的索引
public String objectIdInServer = "";

public String created_datetime = DateTimeCalculator.formatGMTDate(new Date());//创建时间
public String finished_datetime;//完成时间
public String begin_datetime;//开始时间
public String end_datetime;//结束时间

public Boolean readonly = true;//是否可完成
public Boolean is_finished = false;//是否完成，1 - 是，0 - 不是
public Boolean archive = false;//是否归档，1 - 是，0 - 不是,不能修改,可以删除
public Boolean delete = false;//是否删除
public Boolean pin = false;//是否置顶
public Boolean lock = false;//是否锁定，不能删除，不能修改
public Boolean privateTask = false;//是否私有
public boolean is_all_day = false;//是否全天，1 - 是，0 - 不是
public Boolean rawtext = true;//是否纯文本，1 - 是，0 - 不是，为富文本
//region 以下都可空，提醒
public int reminder1Minutes = -1;
public int reminder2Minutes = -1;
public int reminder3Minutes = -1;
public int repeatInterval = 0;//每隔repeatInterval秒重复
public int repeatLimit;
public int repeatRule;
public long lastUpdated;
public String location;
public String repeat;//重复
public String weeks;//周期
public String ringName;//铃声名
public String ringUrl;//铃声地址
public int ringPager;//铃声选择标记界面
public int volume;//音量
public int napInterval;//督促间隔
public int napTimes;//督促次数
public Boolean vibrate = false;//振动
public Boolean nap = false;//督促
public Boolean weaPrompt = false;//天气提示
public Boolean onOff = false;//开关


// DBNote ====================
private long id = -1;
private DBUser user;
private DBNoteBook noteBook;

private String url;// note的url 访问该url可返回该note
private String title;//笔记标题
private String content;//笔记内容
private String created_datetime = DateTimeCalculator.formatGMTDate(new Date());
private String update_datetime = DateTimeCalculator.formatGMTDate(new Date());
private String objectIdInServer = "";

private int color;
private int render_type = TYPE_MARKDOWN;//渲染类型，0为不渲染，1为Markdown渲染

private boolean archive = false;
private boolean rawtext = true;
private Boolean delete = false;//是否删除
private Boolean pin = false;//是否置顶
private Boolean lock = false;//是否锁定，不能删除，可以修改
