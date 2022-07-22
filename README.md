1、原项目在https://github.com/belerweb/pinyin4j

2、覆盖PinyinHelper.java、Trie.java、MultiPinyinConfig.java文件，引入regix_pinyin.txt格式的文本文件，调用如PinyinHelperTest.java里面的testMorePY()方法

3、regix_pinyin.txt的格式需要特别注意，因为会对第三个参数进行正则匹配，所以一些正则匹配用到的符号需要小心处理，如“.”通常表示全部匹配，等等
