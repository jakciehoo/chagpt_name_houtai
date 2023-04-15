package com.ruoyi.ai.service.disableword;


public class TextMain {
    public static void main(String[] args) {
//需要屏蔽哪些字就在censorword.txt文档内添加即可

        SensitiveFilterService filter = SensitiveFilterService.getInstance();

        String txt = "xx需要进行检测的字符串xxx";
//如果需要过滤则用“”替换
//如果需要屏蔽，则用“*”替换
        String hou = filter.replaceSensitiveWord(txt, 1, "*");
        System.out.println("替换前的文字为：" + txt);
        System.out.println("替换后的文字为：" + hou);
//判断是否存在拦截词
        boolean check =  filter.checkContainCount(txt);
        System.out.println("是否存在关键词：" + check );
        //获得包含的关键词 逗号隔开
		String str =  filter.returnSensitiveWord(txt);
		System.out.println("所包含的关键词：" + str);
    }
}
