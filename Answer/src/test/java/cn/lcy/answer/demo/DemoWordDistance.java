package cn.lcy.answer.demo;

import com.hankcs.hanlp.dictionary.CoreSynonymDictionary;

public class DemoWordDistance {
	public static void main(String[] args)
    {
        String[] wordArray = new String[]
                {
                        "出生日期",
                        "生日",
                        "年龄",
                };
        for (String a : wordArray)
        {
            for (String b : wordArray)
            {
                System.out.println(a + "\t" + b + "\t之间的距离是\t" + CoreSynonymDictionary.distance(a, b));
            }
        }
    }
}
