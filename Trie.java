package net.sourceforge.pinyin4j.multipinyin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 刘一波 on 16/3/4.
 * E-Mail:yibo.liu@tqmall.com
 */
public class Trie {

  private Hashtable<String, Trie> values = new Hashtable<String, Trie>();//本节点包含的值

  private String pinyin;//本节点的拼音

  private Trie nextTire;//下一个节点,也就是匹配下一个字符
  
  private static ThreadLocal<List<String>> regexlLocal = new ThreadLocal<List<String>>();

  public static List<String> getRegexlLocal() {
	return regexlLocal.get();
  }

	public static void setRegexlLocal(List<String> list) {
		regexlLocal.set(list);
	}

  public String getPinyin() {
    return pinyin;
  }

  public void setPinyin(String pinyin) {
    this.pinyin = pinyin;
  }

  public Trie getNextTire() {
    return nextTire;
  }

  public void setNextTire(Trie nextTire) {
    this.nextTire = nextTire;
  }

  /**
   * 加载拼音
   *
   * @param inStream 拼音文件输入流
   * @throws IOException
   */
  public synchronized void load(InputStream inStream) throws IOException {
    BufferedReader bufferedReader = null;
    InputStreamReader inputStreamReader = null;
    try {
      inputStreamReader = new InputStreamReader(inStream);
      bufferedReader = new BufferedReader(inputStreamReader);
      String s;
      while ((s = bufferedReader.readLine()) != null) {
        String[] keyAndValue = s.split(" ");
        if (keyAndValue.length != 2) continue;
        Trie trie = new Trie();
        trie.pinyin = keyAndValue[1];
        put(keyAndValue[0], trie);
      }
    } finally {
      if (inputStreamReader != null) inputStreamReader.close();
      if (bufferedReader != null) bufferedReader.close();
    }
  }

  /**
   * 加载多音字拼音词典
   *
   * @param inStream 拼音文件输入流
   */
  public synchronized void loadMultiPinyin(InputStream inStream) throws IOException {
    BufferedReader bufferedReader = null;
    InputStreamReader inputStreamReader = null;
    try {
      inputStreamReader = new InputStreamReader(inStream);
      bufferedReader = new BufferedReader(inputStreamReader);
      String s;
      while ((s = bufferedReader.readLine()) != null) {
        String[] keyAndValue = s.split(" ");
        if (keyAndValue.length != 2) continue;

        String key = keyAndValue[0];//多于一个字的字符串
        String value = keyAndValue[1];//字符串的拼音
        char[] keys = key.toCharArray();

        Trie currentTrie = this;
        for (int i = 0; i < keys.length; i++) {
          String hexString = Integer.toHexString(keys[i]).toUpperCase();

          Trie trieParent = currentTrie.get(hexString);
          if (trieParent == null) {//如果没有此值,直接put进去一个空对象
            currentTrie.put(hexString, new Trie());
            trieParent = currentTrie.get(hexString);
          }
          Trie trie = trieParent.getNextTire();//获取此对象的下一个

          if (keys.length - 1 == i) {//最后一个字了,需要把拼音写进去
            trieParent.pinyin = value;
            break;//此行其实并没有意义
          }

          if (trie == null) {
            if (keys.length - 1 != i) {
              //不是最后一个字,写入这个字的nextTrie,并匹配下一个
              Trie subTrie = new Trie();
              trieParent.setNextTire(subTrie);
              subTrie.put(Integer.toHexString(keys[i + 1]).toUpperCase(), new Trie());
              currentTrie = subTrie;
            }
          } else {
            currentTrie = trie;
          }

        }
      }
    } finally {
      if (inputStreamReader != null) inputStreamReader.close();
      if (bufferedReader != null) bufferedReader.close();
    }
  }

  /**
   * 加载用户自定义的扩展词库
   */
  public void loadMultiPinyinExtend() throws IOException {
    String path = MultiPinyinConfig.multiPinyinPath;
    if (path != null) {
      File userMultiPinyinFile = new File(path);
      if (userMultiPinyinFile.exists()) {
        loadMultiPinyin(new FileInputStream(userMultiPinyinFile));
      }
    }
  }
  
  	/**
  	 * 加载用户自定义的扩展词库(正则匹配)
  	 * @throws IOException 
  	 */
	public static List<String> loadRegexPinyinExtend() throws IOException {
		List<String> resultList = getRegexlLocal();
		if (resultList != null) {
			return resultList;
		}
		
		BufferedReader bufferedReader = null;
		InputStreamReader inputStreamReader = null;
		InputStream inStream = null;
		try {
			String path = MultiPinyinConfig.regexPinyinPath;
			if (path != null) {
				File userMultiPinyinFile = new File(path);
				if (userMultiPinyinFile.exists()) {
					inStream = new FileInputStream(userMultiPinyinFile);
				}
			}
			if (inStream == null) {
				return null;
			}
			
			inputStreamReader = new InputStreamReader(inStream);
			bufferedReader = new BufferedReader(inputStreamReader);
			String s;
			resultList = new ArrayList<>();
			
			while ((s = bufferedReader.readLine()) != null) {
				String[] keyAndValue = s.split("\\/");
				if (keyAndValue.length < 3)
					continue;
				resultList.add(s);
			}

			setRegexlLocal(resultList);
			return resultList;
		} catch (FileNotFoundException e) {
			
		} finally {
			if (inputStreamReader != null)
				inputStreamReader.close();
			if (bufferedReader != null)
				bufferedReader.close();
		}

		return null;
	}

  	/**
  	 * 对字符串进行正则匹配处理
  	 * @throws IOException 
  	 */
	public static String regexText(String inStr, String separate) {
		try {
			List<String> regexStrList = loadRegexPinyinExtend();
			if (regexStrList == null) {
				return inStr;
			}
			
			for (String regexStr : regexStrList) {
				String [] temp = regexStr.split("\\/");
				String key = temp[0];		// 需要匹配的汉字
				String value = temp[1];		// 汉字对应的拼音
				String regexContent = temp[2];	// 正则字符串
				
				if (inStr.indexOf(key) != -1) {
					StringBuffer sBuffer = new StringBuffer(100);
					regexCheck(key, value, regexContent, inStr, sBuffer);
					if (sBuffer.toString().indexOf(value) != -1) {
						if (separate != null) {
							inStr = sBuffer.toString().replaceAll(value, sepaceWord(value, separate));
						} else {
							inStr = sBuffer.toString();
						}
					}
				}
			}
		} catch (IOException e) {
			System.out.println("正则匹配处理拼音异常：" + e.getMessage());
		}
		
		return separate != null ? inStr.replaceAll(separate + separate, separate) : inStr;
	}
	
//	public static void main(String[] args) {
//		System.out.println(sepaceWord("wu4e4", " "));
//	}
	
	/*
	 * 对拼音按指定值进行分隔
	 */
	private static String sepaceWord(String value, String separate) {		
		char [] c_arr = value.toCharArray();
		StringBuffer sBuffer = new StringBuffer();
		boolean num_flag = false;
		boolean az_flag = false;
		for (char c : c_arr) {
			if (Pattern.matches("[A-Za-z]", String.valueOf(c))) {
				az_flag = true;
			} else if (Pattern.matches("[0-9]", String.valueOf(c))) {
				num_flag = true;
				az_flag = false;
			}
			
			if (az_flag && num_flag) {
				sBuffer.append(separate);
				num_flag = false;
			}
			sBuffer.append(c);
		}
		return sBuffer.append(separate).toString();
	}

	/*public static void main(String[] args) {
		String str = "所谓诚其意者，毋自欺也，如恶恶臭，如好好色，此之谓自谦，故君子必厌恶也。";
//		Pattern pattern = Pattern.compile("如恶恶臭|恶杀|可恶|厌恶");
//		Matcher matcher = pattern.matcher(str);
//		if (matcher.find()) {
//			String matcherStr = matcher.group(0);
//			System.out.println(str.subSequence(0, str.indexOf(matcherStr)+matcherStr.length()));
//		}
		StringBuffer sBuffer = new StringBuffer(100);
		regexCheck("恶恶", "wu4e4", "如恶恶臭|恶杀|可恶|厌恶", str, sBuffer);
		System.out.println(sBuffer.toString());
	}*/
	
	/**
	 * 匹配判断
	 */
	public static void regexCheck(String key, String value, String regex, String str, StringBuffer sBuffer) {
		try {
			if (str != null && !"".equals(str.trim())) {
				Pattern pattern = Pattern.compile(regex);
				Matcher matcher = pattern.matcher(str);
				if (matcher.find()) {
					String matcherStr = matcher.group(0);
					String text = str.substring(0, str.indexOf(matcherStr)+matcherStr.length());
					sBuffer.append(text.replaceAll(matcherStr, matcherStr.replaceAll(key, value)));
					regexCheck(key, value, regex, str.replaceAll(text, ""), sBuffer);
					return;
				}
			}
		} catch (Throwable e) {
			//System.out.println(str);
		}
		
		sBuffer.append(str);
	}

  public Trie get(String hexString) {
    return values.get(hexString);
  }

  public void put(String s, Trie trie) {
    values.put(s, trie);
  }
  
}
