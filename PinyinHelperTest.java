package net.sourceforge.pinyin4j.test;

import junit.framework.TestCase;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import net.sourceforge.pinyin4j.multipinyin.MultiPinyinConfig;

public class PinyinHelperTest extends TestCase {

	// 测试多音字
	public void testMorePY() throws BadHanyuPinyinOutputFormatCombination {
		String str = "所谓诚其意者，毋自欺也，如恶恶臭，如好好色，此之谓自谦，故君子必厌恶也。1÷2=0.5";

		//MultiPinyinConfig.multiPinyinPath = "D:\\workspace_tts\\pinyin4j\\src\\main\\resources\\pinyindb\\regix_pinyin.txt";
		MultiPinyinConfig.regexPinyinPath = "D:\\workspace_tts\\pinyin4j\\src\\main\\resources\\pinyindb\\regix_pinyin.txt";
		HanyuPinyinOutputFormat hanyuPinyinOutputFormat = new HanyuPinyinOutputFormat();
		hanyuPinyinOutputFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		hanyuPinyinOutputFormat.setToneType(HanyuPinyinToneType.WITH_TONE_NUMBER); // 用数字表示
		hanyuPinyinOutputFormat.setVCharType(HanyuPinyinVCharType.WITH_V);

		//System.out.println(getAlphabetic("test", str));
		System.out.println(PinyinHelper.toHanYuPinyinString(str, hanyuPinyinOutputFormat, " ", false, true));
	}

	private static String getAlphabetic(String name, String str) {
		// 声明字符数组
		char[] alphabeticArray = null;
		// 接收字符串中的单个字符
		alphabeticArray = str.trim().toCharArray();
		// 声明字符串数组用于接收单个汉字的拼音
		String[] singleChinese = new String[alphabeticArray.length];
		/*
		 * 声明拼音格式对象，用于设置拼音格式
		 */
		HanyuPinyinOutputFormat hanyuPinyinOutputFormat = new HanyuPinyinOutputFormat();
		hanyuPinyinOutputFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		hanyuPinyinOutputFormat.setToneType(HanyuPinyinToneType.WITH_TONE_NUMBER); // 用数字表示
		hanyuPinyinOutputFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
		String result = "", word = "";
		try {
			// 遍历每个字符
			for (int i = 0, size = alphabeticArray.length; i < size; i++) {
				word = java.lang.Character.toString(alphabeticArray[i]);
				// 判断当前字符是否为汉字
				if (word.matches("[\\u4E00-\\u9FA5]+")) {
					singleChinese = PinyinHelper.toHanyuPinyinStringArray(alphabeticArray[i], hanyuPinyinOutputFormat);
					try {
						result += singleChinese[0] + (i == size - 1 ? "" : " ");
					} catch (Exception e) {
						// e.printStackTrace();
						System.out.println(String.format("【%s】转拼音异常：%s", alphabeticArray[i], name));
					}
				} else {
					if (word != null && "".equals(word.trim()) == false) {
						result = result.trim() + word + " ";
					}
				}
			}
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}
		return result;
	}

	public void testToTongyongPinyinStringArray() {
		// any input of non-Chinese characters will return null
		{
			assertNull(PinyinHelper.toTongyongPinyinStringArray('A'));
			assertNull(PinyinHelper.toTongyongPinyinStringArray('z'));
			assertNull(PinyinHelper.toTongyongPinyinStringArray(','));
			assertNull(PinyinHelper.toTongyongPinyinStringArray('。'));
		}

		// Chinese characters
		// single pronounciation
		{
			String[] expectedPinyinArray = new String[] { "li3" };
			String[] pinyinArray = PinyinHelper.toTongyongPinyinStringArray('李');

			assertEquals(expectedPinyinArray.length, pinyinArray.length);

			for (int i = 0; i < expectedPinyinArray.length; i++) {
				assertEquals(expectedPinyinArray[i], pinyinArray[i]);
			}
		}
		{
			String[] expectedPinyinArray = new String[] { "ciou2" };
			String[] pinyinArray = PinyinHelper.toTongyongPinyinStringArray('球');

			assertEquals(expectedPinyinArray.length, pinyinArray.length);

			for (int i = 0; i < expectedPinyinArray.length; i++) {
				assertEquals(expectedPinyinArray[i], pinyinArray[i]);
			}
		}
		{
			String[] expectedPinyinArray = new String[] { "jhuang1" };
			String[] pinyinArray = PinyinHelper.toTongyongPinyinStringArray('桩');

			assertEquals(expectedPinyinArray.length, pinyinArray.length);

			for (int i = 0; i < expectedPinyinArray.length; i++) {
				assertEquals(expectedPinyinArray[i], pinyinArray[i]);
			}
		}

		// multiple pronounciations
		{
			String[] expectedPinyinArray = new String[] { "chuan2", "jhuan4" };
			String[] pinyinArray = PinyinHelper.toTongyongPinyinStringArray('传');

			assertEquals(expectedPinyinArray.length, pinyinArray.length);

			for (int i = 0; i < expectedPinyinArray.length; i++) {
				assertEquals(expectedPinyinArray[i], pinyinArray[i]);
			}
		}

		{
			String[] expectedPinyinArray = new String[] { "lyu4", "lu4" };
			String[] pinyinArray = PinyinHelper.toTongyongPinyinStringArray('绿');

			assertEquals(expectedPinyinArray.length, pinyinArray.length);

			for (int i = 0; i < expectedPinyinArray.length; i++) {
				assertEquals(expectedPinyinArray[i], pinyinArray[i]);
			}
		}
	}

	public void testToWadeGilesPinyinStringArray() {
		// any input of non-Chinese characters will return null
		{
			assertNull(PinyinHelper.toWadeGilesPinyinStringArray('A'));
			assertNull(PinyinHelper.toWadeGilesPinyinStringArray('z'));
			assertNull(PinyinHelper.toWadeGilesPinyinStringArray(','));
			assertNull(PinyinHelper.toWadeGilesPinyinStringArray('。'));
		}

		// Chinese characters
		// single pronounciation
		{
			String[] expectedPinyinArray = new String[] { "li3" };
			String[] pinyinArray = PinyinHelper.toWadeGilesPinyinStringArray('李');

			assertEquals(expectedPinyinArray.length, pinyinArray.length);

			for (int i = 0; i < expectedPinyinArray.length; i++) {
				assertEquals(expectedPinyinArray[i], pinyinArray[i]);
			}
		}
		{
			String[] expectedPinyinArray = new String[] { "ch`iu2" };
			String[] pinyinArray = PinyinHelper.toWadeGilesPinyinStringArray('球');

			assertEquals(expectedPinyinArray.length, pinyinArray.length);

			for (int i = 0; i < expectedPinyinArray.length; i++) {
				assertEquals(expectedPinyinArray[i], pinyinArray[i]);
			}
		}
		{
			String[] expectedPinyinArray = new String[] { "chuang1" };
			String[] pinyinArray = PinyinHelper.toWadeGilesPinyinStringArray('桩');

			assertEquals(expectedPinyinArray.length, pinyinArray.length);

			for (int i = 0; i < expectedPinyinArray.length; i++) {
				assertEquals(expectedPinyinArray[i], pinyinArray[i]);
			}
		}

		// multiple pronounciations
		{
			String[] expectedPinyinArray = new String[] { "ch`uan2", "chuan4" };
			String[] pinyinArray = PinyinHelper.toWadeGilesPinyinStringArray('传');

			assertEquals(expectedPinyinArray.length, pinyinArray.length);

			for (int i = 0; i < expectedPinyinArray.length; i++) {
				assertEquals(expectedPinyinArray[i], pinyinArray[i]);
			}
		}

		{
			String[] expectedPinyinArray = new String[] { "lu:4", "lu4" };
			String[] pinyinArray = PinyinHelper.toWadeGilesPinyinStringArray('绿');

			assertEquals(expectedPinyinArray.length, pinyinArray.length);

			for (int i = 0; i < expectedPinyinArray.length; i++) {
				assertEquals(expectedPinyinArray[i], pinyinArray[i]);
			}
		}
	}

	public void testToMPS2PinyinStringArray() {
		// any input of non-Chinese characters will return null
		{
			assertNull(PinyinHelper.toMPS2PinyinStringArray('A'));
			assertNull(PinyinHelper.toMPS2PinyinStringArray('z'));
			assertNull(PinyinHelper.toMPS2PinyinStringArray(','));
			assertNull(PinyinHelper.toMPS2PinyinStringArray('。'));
		}

		// Chinese characters
		// single pronounciation
		{
			String[] expectedPinyinArray = new String[] { "li3" };
			String[] pinyinArray = PinyinHelper.toMPS2PinyinStringArray('李');

			assertEquals(expectedPinyinArray.length, pinyinArray.length);

			for (int i = 0; i < expectedPinyinArray.length; i++) {
				assertEquals(expectedPinyinArray[i], pinyinArray[i]);
			}
		}
		{
			String[] expectedPinyinArray = new String[] { "chiou2" };
			String[] pinyinArray = PinyinHelper.toMPS2PinyinStringArray('球');

			assertEquals(expectedPinyinArray.length, pinyinArray.length);

			for (int i = 0; i < expectedPinyinArray.length; i++) {
				assertEquals(expectedPinyinArray[i], pinyinArray[i]);
			}
		}
		{
			String[] expectedPinyinArray = new String[] { "juang1" };
			String[] pinyinArray = PinyinHelper.toMPS2PinyinStringArray('桩');

			assertEquals(expectedPinyinArray.length, pinyinArray.length);

			for (int i = 0; i < expectedPinyinArray.length; i++) {
				assertEquals(expectedPinyinArray[i], pinyinArray[i]);
			}
		}

		// multiple pronounciations
		{
			String[] expectedPinyinArray = new String[] { "chuan2", "juan4" };
			String[] pinyinArray = PinyinHelper.toMPS2PinyinStringArray('传');

			assertEquals(expectedPinyinArray.length, pinyinArray.length);

			for (int i = 0; i < expectedPinyinArray.length; i++) {
				assertEquals(expectedPinyinArray[i], pinyinArray[i]);
			}
		}

		{
			String[] expectedPinyinArray = new String[] { "liu4", "lu4" };
			String[] pinyinArray = PinyinHelper.toMPS2PinyinStringArray('绿');

			assertEquals(expectedPinyinArray.length, pinyinArray.length);

			for (int i = 0; i < expectedPinyinArray.length; i++) {
				assertEquals(expectedPinyinArray[i], pinyinArray[i]);
			}
		}
	}

	public void testToYalePinyinStringArray() {
		// any input of non-Chinese characters will return null
		{
			assertNull(PinyinHelper.toYalePinyinStringArray('A'));
			assertNull(PinyinHelper.toYalePinyinStringArray('z'));
			assertNull(PinyinHelper.toYalePinyinStringArray(','));
			assertNull(PinyinHelper.toYalePinyinStringArray('。'));
		}

		// Chinese characters
		// single pronounciation
		{
			String[] expectedPinyinArray = new String[] { "li3" };
			String[] pinyinArray = PinyinHelper.toYalePinyinStringArray('李');

			assertEquals(expectedPinyinArray.length, pinyinArray.length);

			for (int i = 0; i < expectedPinyinArray.length; i++) {
				assertEquals(expectedPinyinArray[i], pinyinArray[i]);
			}
		}
		{
			String[] expectedPinyinArray = new String[] { "chyou2" };
			String[] pinyinArray = PinyinHelper.toYalePinyinStringArray('球');

			assertEquals(expectedPinyinArray.length, pinyinArray.length);

			for (int i = 0; i < expectedPinyinArray.length; i++) {
				assertEquals(expectedPinyinArray[i], pinyinArray[i]);
			}
		}
		{
			String[] expectedPinyinArray = new String[] { "jwang1" };
			String[] pinyinArray = PinyinHelper.toYalePinyinStringArray('桩');

			assertEquals(expectedPinyinArray.length, pinyinArray.length);

			for (int i = 0; i < expectedPinyinArray.length; i++) {
				assertEquals(expectedPinyinArray[i], pinyinArray[i]);
			}
		}

		// multiple pronounciations
		{
			String[] expectedPinyinArray = new String[] { "chwan2", "jwan4" };
			String[] pinyinArray = PinyinHelper.toYalePinyinStringArray('传');

			assertEquals(expectedPinyinArray.length, pinyinArray.length);

			for (int i = 0; i < expectedPinyinArray.length; i++) {
				assertEquals(expectedPinyinArray[i], pinyinArray[i]);
			}
		}

		{
			String[] expectedPinyinArray = new String[] { "lyu4", "lu4" };
			String[] pinyinArray = PinyinHelper.toYalePinyinStringArray('绿');

			assertEquals(expectedPinyinArray.length, pinyinArray.length);

			for (int i = 0; i < expectedPinyinArray.length; i++) {
				assertEquals(expectedPinyinArray[i], pinyinArray[i]);
			}
		}
	}

	public void testToGwoyeuRomatzyhStringArray() {
		// any input of non-Chinese characters will return null
		{
			assertNull(PinyinHelper.toGwoyeuRomatzyhStringArray('A'));
			assertNull(PinyinHelper.toGwoyeuRomatzyhStringArray('z'));
			assertNull(PinyinHelper.toGwoyeuRomatzyhStringArray(','));
			assertNull(PinyinHelper.toGwoyeuRomatzyhStringArray('。'));
		}

		// Chinese characters
		// single pronounciation
		{
			String[] expectedPinyinArray = new String[] { "lii" };
			String[] pinyinArray = PinyinHelper.toGwoyeuRomatzyhStringArray('李');

			assertEquals(expectedPinyinArray.length, pinyinArray.length);

			for (int i = 0; i < expectedPinyinArray.length; i++) {
				assertEquals(expectedPinyinArray[i], pinyinArray[i]);
			}
		}
		{
			String[] expectedPinyinArray = new String[] { "chyou" };
			String[] pinyinArray = PinyinHelper.toGwoyeuRomatzyhStringArray('球');

			assertEquals(expectedPinyinArray.length, pinyinArray.length);

			for (int i = 0; i < expectedPinyinArray.length; i++) {
				assertEquals(expectedPinyinArray[i], pinyinArray[i]);
			}
		}
		{
			String[] expectedPinyinArray = new String[] { "juang" };
			String[] pinyinArray = PinyinHelper.toGwoyeuRomatzyhStringArray('桩');

			assertEquals(expectedPinyinArray.length, pinyinArray.length);

			for (int i = 0; i < expectedPinyinArray.length; i++) {
				assertEquals(expectedPinyinArray[i], pinyinArray[i]);
			}
		}

		{
			String[] expectedPinyinArray = new String[] { "fuh" };
			String[] pinyinArray = PinyinHelper.toGwoyeuRomatzyhStringArray('付');

			assertEquals(expectedPinyinArray.length, pinyinArray.length);

			for (int i = 0; i < expectedPinyinArray.length; i++) {
				assertEquals(expectedPinyinArray[i], pinyinArray[i]);
			}
		}

		// multiple pronounciations
		{
			String[] expectedPinyinArray = new String[] { "chwan", "juann" };
			String[] pinyinArray = PinyinHelper.toGwoyeuRomatzyhStringArray('传');

			assertEquals(expectedPinyinArray.length, pinyinArray.length);

			for (int i = 0; i < expectedPinyinArray.length; i++) {
				assertEquals(expectedPinyinArray[i], pinyinArray[i]);
			}
		}

		{
			String[] expectedPinyinArray = new String[] { ".me", ".mha", "iau" };
			String[] pinyinArray = PinyinHelper.toGwoyeuRomatzyhStringArray('么');

			assertEquals(expectedPinyinArray.length, pinyinArray.length);

			for (int i = 0; i < expectedPinyinArray.length; i++) {
				assertEquals(expectedPinyinArray[i], pinyinArray[i]);
			}
		}

		{
			String[] expectedPinyinArray = new String[] { "liuh", "luh" };
			String[] pinyinArray = PinyinHelper.toGwoyeuRomatzyhStringArray('绿');

			assertEquals(expectedPinyinArray.length, pinyinArray.length);

			for (int i = 0; i < expectedPinyinArray.length; i++) {
				assertEquals(expectedPinyinArray[i], pinyinArray[i]);
			}
		}
	}

	public void testToHanyuPinyinStringArray() {

		// any input of non-Chinese characters will return null
		{
			HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
			try {
				assertNull(PinyinHelper.toHanyuPinyinStringArray('A', defaultFormat));
				assertNull(PinyinHelper.toHanyuPinyinStringArray('z', defaultFormat));
				assertNull(PinyinHelper.toHanyuPinyinStringArray(',', defaultFormat));
				assertNull(PinyinHelper.toHanyuPinyinStringArray('。', defaultFormat));
			} catch (BadHanyuPinyinOutputFormatCombination e) {
				e.printStackTrace();
			}
		}

		// Chinese characters
		// single pronounciation
		{
			try {
				HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();

				String[] expectedPinyinArray = new String[] { "li3" };
				String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray('李', defaultFormat);

				assertEquals(expectedPinyinArray.length, pinyinArray.length);

				for (int i = 0; i < expectedPinyinArray.length; i++) {
					assertEquals(expectedPinyinArray[i], pinyinArray[i]);
				}
			} catch (BadHanyuPinyinOutputFormatCombination e) {
				e.printStackTrace();
			}
		}
		{
			try {
				HanyuPinyinOutputFormat upperCaseFormat = new HanyuPinyinOutputFormat();
				upperCaseFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);

				String[] expectedPinyinArray = new String[] { "LI3" };
				String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray('李', upperCaseFormat);

				assertEquals(expectedPinyinArray.length, pinyinArray.length);

				for (int i = 0; i < expectedPinyinArray.length; i++) {
					assertEquals(expectedPinyinArray[i], pinyinArray[i]);
				}
			} catch (BadHanyuPinyinOutputFormatCombination e) {
				e.printStackTrace();
			}
		}
		{
			try {
				HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();

				String[] expectedPinyinArray = new String[] { "lu:3" };
				String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray('吕', defaultFormat);

				assertEquals(expectedPinyinArray.length, pinyinArray.length);

				for (int i = 0; i < expectedPinyinArray.length; i++) {
					assertEquals(expectedPinyinArray[i], pinyinArray[i]);
				}
			} catch (BadHanyuPinyinOutputFormatCombination e) {
				e.printStackTrace();
			}
		}
		{
			try {
				HanyuPinyinOutputFormat vCharFormat = new HanyuPinyinOutputFormat();
				vCharFormat.setVCharType(HanyuPinyinVCharType.WITH_V);

				String[] expectedPinyinArray = new String[] { "lv3" };
				String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray('吕', vCharFormat);

				assertEquals(expectedPinyinArray.length, pinyinArray.length);

				for (int i = 0; i < expectedPinyinArray.length; i++) {
					assertEquals(expectedPinyinArray[i], pinyinArray[i]);
				}
			} catch (BadHanyuPinyinOutputFormatCombination e) {
				e.printStackTrace();
			}
		}

		// multiple pronounciations
		{
			try {
				HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();

				String[] expectedPinyinArray = new String[] { "jian1", "jian4" };
				String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray('间', defaultFormat);

				assertEquals(expectedPinyinArray.length, pinyinArray.length);

				for (int i = 0; i < expectedPinyinArray.length; i++) {
					assertEquals(expectedPinyinArray[i], pinyinArray[i]);
				}
			} catch (BadHanyuPinyinOutputFormatCombination e) {
				e.printStackTrace();
			}
		}

		{
			try {
				HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();

				String[] expectedPinyinArray = new String[] { "hao3", "hao4" };
				String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray('好', defaultFormat);

				assertEquals(expectedPinyinArray.length, pinyinArray.length);

				for (int i = 0; i < expectedPinyinArray.length; i++) {
					assertEquals(expectedPinyinArray[i], pinyinArray[i]);
				}
			} catch (BadHanyuPinyinOutputFormatCombination e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * test for combination of output formats
	 */
	public void testOutputCombination() {
		try {
			HanyuPinyinOutputFormat outputFormat = new HanyuPinyinOutputFormat();

			// fix case type to lowercase firstly, change VChar and Tone
			// combination
			outputFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);

			// WITH_U_AND_COLON and WITH_TONE_NUMBER
			outputFormat.setVCharType(HanyuPinyinVCharType.WITH_U_AND_COLON);
			outputFormat.setToneType(HanyuPinyinToneType.WITH_TONE_NUMBER);

			assertEquals("lu:3", PinyinHelper.toHanyuPinyinStringArray('吕', outputFormat)[0]);

			// WITH_V and WITH_TONE_NUMBER
			outputFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
			outputFormat.setToneType(HanyuPinyinToneType.WITH_TONE_NUMBER);

			assertEquals("lv3", PinyinHelper.toHanyuPinyinStringArray('吕', outputFormat)[0]);

			// WITH_U_UNICODE and WITH_TONE_NUMBER
			outputFormat.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);
			outputFormat.setToneType(HanyuPinyinToneType.WITH_TONE_NUMBER);

			assertEquals("lü3", PinyinHelper.toHanyuPinyinStringArray('吕', outputFormat)[0]);

			// // WITH_U_AND_COLON and WITHOUT_TONE
			outputFormat.setVCharType(HanyuPinyinVCharType.WITH_U_AND_COLON);
			outputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

			assertEquals("lu:", PinyinHelper.toHanyuPinyinStringArray('吕', outputFormat)[0]);

			// WITH_V and WITHOUT_TONE
			outputFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
			outputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

			assertEquals("lv", PinyinHelper.toHanyuPinyinStringArray('吕', outputFormat)[0]);

			// WITH_U_UNICODE and WITHOUT_TONE
			outputFormat.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);
			outputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

			assertEquals("lü", PinyinHelper.toHanyuPinyinStringArray('吕', outputFormat)[0]);

			// WITH_U_AND_COLON and WITH_TONE_MARK is forbidden

			// WITH_V and WITH_TONE_MARK is forbidden

			// WITH_U_UNICODE and WITH_TONE_MARK
			outputFormat.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);
			outputFormat.setToneType(HanyuPinyinToneType.WITH_TONE_MARK);

			assertEquals("lǚ", PinyinHelper.toHanyuPinyinStringArray('吕', outputFormat)[0]);

			// fix case type to UPPERCASE, change VChar and Tone
			// combination
			outputFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);

			// WITH_U_AND_COLON and WITH_TONE_NUMBER
			outputFormat.setVCharType(HanyuPinyinVCharType.WITH_U_AND_COLON);
			outputFormat.setToneType(HanyuPinyinToneType.WITH_TONE_NUMBER);

			assertEquals("LU:3", PinyinHelper.toHanyuPinyinStringArray('吕', outputFormat)[0]);

			// WITH_V and WITH_TONE_NUMBER
			outputFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
			outputFormat.setToneType(HanyuPinyinToneType.WITH_TONE_NUMBER);

			assertEquals("LV3", PinyinHelper.toHanyuPinyinStringArray('吕', outputFormat)[0]);

			// WITH_U_UNICODE and WITH_TONE_NUMBER
			outputFormat.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);
			outputFormat.setToneType(HanyuPinyinToneType.WITH_TONE_NUMBER);

			assertEquals("LÜ3", PinyinHelper.toHanyuPinyinStringArray('吕', outputFormat)[0]);

			// // WITH_U_AND_COLON and WITHOUT_TONE
			outputFormat.setVCharType(HanyuPinyinVCharType.WITH_U_AND_COLON);
			outputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

			assertEquals("LU:", PinyinHelper.toHanyuPinyinStringArray('吕', outputFormat)[0]);

			// WITH_V and WITHOUT_TONE
			outputFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
			outputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

			assertEquals("LV", PinyinHelper.toHanyuPinyinStringArray('吕', outputFormat)[0]);

			// WITH_U_UNICODE and WITHOUT_TONE
			outputFormat.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);
			outputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

			assertEquals("LÜ", PinyinHelper.toHanyuPinyinStringArray('吕', outputFormat)[0]);

			// WITH_U_AND_COLON and WITH_TONE_MARK is forbidden

			// WITH_V and WITH_TONE_MARK is forbidden

			// WITH_U_UNICODE and WITH_TONE_MARK
			outputFormat.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);
			outputFormat.setToneType(HanyuPinyinToneType.WITH_TONE_MARK);

			assertEquals("LǙ", PinyinHelper.toHanyuPinyinStringArray('吕', outputFormat)[0]);
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}
	}
}
