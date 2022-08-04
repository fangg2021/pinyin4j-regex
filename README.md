1、原项目在https://github.com/belerweb/pinyin4j

2、覆盖PinyinHelper.java、Trie.java、MultiPinyinConfig.java文件，引入regix_pinyin.txt格式的文本文件，调用如下：
    

		String str = "所谓诚其意者，毋自欺也，如恶恶臭，如好好色，此之谓自谦，故君子必厌恶也。";
		
		MultiPinyinConfig.regexPinyinPath = "D:\\xxx\\pinyin4j\\src\\main\\resources\\pinyindb\\regix_pinyin.txt";
		HanyuPinyinOutputFormat hanyuPinyinOutputFormat = new HanyuPinyinOutputFormat();
		hanyuPinyinOutputFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		hanyuPinyinOutputFormat.setToneType(HanyuPinyinToneType.WITH_TONE_NUMBER); // 用数字表示
		hanyuPinyinOutputFormat.setVCharType(HanyuPinyinVCharType.WITH_V);

		System.out.println(PinyinHelper.toHanYuPinyinString(str, hanyuPinyinOutputFormat, " ", false, true));

3、regix_pinyin.txt的格式需要特别注意，因为会对第三个参数进行正则匹配，所以一些正则匹配用到的符号需要小心处理，如“.”通常表示全部匹配，等等

效果如下：

所谓诚其意者，毋自欺也，如恶恶臭，如好好色，此之谓自谦，故君子必厌恶也。

suo3 wei4 cheng2 qi2 yi4 zhe3 wu2 zi4 qi1 ye3 ru2 wu4 e4 chou4 ru2 hao4 hao3 se4 ci3 zhi1 wei4 zi4 qian1 gu4 jun1 zi5 bi4 yan4 wu4 ye3 

1÷2=0.5

1chu2 yi3 2deng3 yu2 0dian3 wu3 

