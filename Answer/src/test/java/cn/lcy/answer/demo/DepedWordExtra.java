package cn.lcy.answer.demo;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;
 
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.international.pennchinese.ChineseTreebankLanguagePack;
 
public class DepedWordExtra {
 
    static String[] options = { "-MAX_ITEMS", "200000000" };
    static LexicalizedParser lp = LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/chinesePCFG.ser.gz", options);
 
    public static void main(String[] args) {
 
       String sentence = "老师 穿 着 一件 很 美丽 的 衣服";
       String keyword = "衣服";
       String sentArry[] = sentence.split(" ");
       for (int i = 0; i < sentArry.length; i++) {
           if (keyword.equals(sentArry[i])) {
              break;
           }
       }
       // System.out.println(kwIndex);
 
       extraDepWord(sentence, keyword);
 
    }
 
    private static void extraDepWord(String sentence, String keyword) {
       // TODO Auto-generated method stub
       TreebankLanguagePack tlp = new ChineseTreebankLanguagePack();
       Tokenizer<? extends HasWord> toke = tlp.getTokenizerFactory()
              .getTokenizer(new StringReader(sentence));
       List<? extends HasWord> sentList = toke.tokenize();
       Tree parse = lp.apply(sentList);
       parse.pennPrint();
       List<Tree> leaves = parse.getLeaves();
 
       Iterator<Tree> it = leaves.iterator();
       while (it.hasNext()) {
           Tree leaf = it.next();
           System.out.println("value:"+leaf.value());
           System.out.println("label:"+leaf.parent(parse).label());
           if (leaf.nodeString().trim().equals(keyword)) {
              Tree start = leaf;
              start = start.parent(parse);
              String tag = start.value().toString().trim();
              boolean extraedflg = false;
              // 如果当前节点的父节点是NN，则遍历该父节点的父节点的兄弟节点
              if (tag.equals("NN") || tag.equals("VA")) {
                  for (int i = 0; i < parse.depth(); i++) {
                     start = start.parent(parse);
                     if (start.value().toString().trim().equals("ROOT")
                            || extraedflg == true) {
                         break;
                     } else {
 
                         List<Tree> bros = start.siblings(parse);
                         if (bros != null) {
 
                            Iterator<Tree> it1 = bros.iterator();
                            while (it1.hasNext()) {
 
                                Tree bro = it1.next();
                                extraedflg = IteratorTree(bro, tag);
                                if (extraedflg) {
                                   break;
                                }
 
                            }
                         }
                     }
                  }
              }
 
           }
       }
    }
 
    private static boolean IteratorTree(Tree bro, String tagKey) {
       List<Tree> ends = bro.getChildrenAsList();
       Iterator<Tree> it = ends.iterator();
      
       while (it.hasNext()) {
           Tree end = it.next();
           String tagDep = end.value().toString().trim();
           if ((tagKey.equals("NN") && tagDep.equals("VA")) || (tagKey.equals("VA") && tagDep.equals("AD"))) {
              Tree depTree = end.getChild(0);
              System.out.println("进来的了吗");
              System.out.println(depTree.value().toString());
              return true;
           } else if (IteratorTree(end, tagKey)) {
              return true;
           }
       }
       return false;
    }
}
