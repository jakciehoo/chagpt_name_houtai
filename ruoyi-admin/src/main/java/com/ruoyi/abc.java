package com.ruoyi;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.ruoyi.ai.doamin.SettingVO;

import java.util.List;

public class abc {
    public static void main(String[] args) {
        //[{"name":"平台LOGO","key":"plat_logo","value":"https://yuan-ai.oss-cn-beijing.aliyuncs.com/logo.png","desc":"","dataType":"1"},{"name":"平台LOGO","key":"plat_logo","value":"https://yuan-ai.oss-cn-beijing.aliyuncs.com/logo.png","desc":"","dataType":"1"}]
        String aa = "[{\"name\":\"平台LOGO\",\"key\":\"plat_logo\",\"value\":\"https://yuan-ai.oss-cn-beijing.aliyuncs.com/logo.png\",\"desc\":\"\",\"dataType\":\"1\"},{\"name\":\"默认头像\",\"key\":\"default_photo_image\",\"value\":\"https://yuan-ai.oss-cn-beijing.aliyuncs.com/photo.png\",\"desc\":\"\",\"dataType\":\"1\"},{\"name\":\"小程序名称\",\"key\":\"weichat_name\",\"value\":\"人工智能问答\",\"desc\":\"\",\"dataType\":\"1\"},{\"name\":\"小程序描述\",\"key\":\"weichat_desc\",\"value\":\"你想问的，应有尽有\",\"desc\":\"\",\"dataType\":\"1\"},{\"name\":\"小程序公告\",\"key\":\"weichat_notice\",\"value\":\"2023年03月17日小程序更新,一切正常中\",\"desc\":\"\",\"dataType\":\"1\"},{\"name\":\"微信小程序appid\",\"key\":\"appid\",\"value\":\"wx74378f1d2e142c4d\",\"desc\":\"\",\"dataType\":\"1\"},{\"name\":\"微信小程序appkey\",\"key\":\"secret\",\"value\":\"61103312c89d7267386731dff744895d\",\"desc\":\"\",\"dataType\":\"1\"},{\"name\":\"是否开启问答次数或者时间限制\",\"key\":\"is_open_num\",\"value\":\"1\",\"desc\":\"0关闭,1开启（关闭状态是不消耗次数以及验证会员的）\",\"dataType\":\"1\"},{\"name\":\"用户注册赠送问答数\",\"key\":\"register_give_number\",\"value\":\"1\",\"desc\":\"用户注册赠送问答数\",\"dataType\":\"1\"},{\"name\":\"是否开启邀请赠送数\",\"key\":\"is_open_intive_register_give_num\",\"value\":\"1\",\"desc\":\"是否开启注册赠送数,0关闭，1开启（如果此选项开始，邀请用户赠送数量-intive_give_num填写0，也是可以的,但是会影响系统性能）\",\"dataType\":\"1\"},{\"name\":\"邀请赠送数量\",\"key\":\"intive_give_num\",\"value\":\"10\",\"desc\":\"\",\"dataType\":\"1\"},{\"name\":\"代理路径\",\"key\":\"proxy_url\",\"value\":\"https://chatapi.broue.cn/v1/chat/completions\",\"desc\":\"\",\"dataType\":\"1\"},{\"name\":\"sql在线更新链接\",\"key\":\"online_update_url\",\"value\":\"http://localhost/dev-api/chatgpt/online/list?pageNum=1&pageSize=10\",\"desc\":\"可以在线更新后台sql表,链接一般不变,如果变了请联系群主\",\"dataType\":\"1\"},{\"name\":\"版本号\",\"key\":\"pro_version\",\"value\":\"2.0.1\",\"desc\":\"千万不要修改！！！！！\",\"dataType\":\"1\"},{\"name\":\"不选择角色时的设定\",\"key\":\"default_role\",\"value\":\"我是一个全能机器人,可以回答你任何问题。\",\"desc\":\"\",\"dataType\":\"1\"},{\"name\":\"获取上下执行文的组数\",\"key\":\"default_context_num\",\"value\":\"10\",\"desc\":\"对话时携带的上下文数量\",\"dataType\":\"1\"}]";
        String replace = StrUtil.replace("127.0.0.1", ".", ",");
        System.out.println(replace);
    }
}
