package com.github.xbynet.fxboot.search;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.springframework.stereotype.Service;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;

/**
 * 注意点:TextField是分词的，而StringField是整体使用不分词的
 * @author anzhou.tjw
 * @date 2018/6/15 上午10:09
 */
@Service
public class LuceneHelper {

    private static final String indexPath = System.getProperty("user.home") + File.separator + "fxbootdemo";
    private static Directory dir = null;
    private static Analyzer analyzer = new IKAnalyzer(true); // 使用IK分词
    private static IndexWriter indexWriter = null;
    private static IndexSearcher indexSearcher = null;
    private static IndexReader indexReader = null;

    public  IndexWriter getWriter(boolean create) throws IOException {
        if (indexWriter == null) {
            synchronized (LuceneHelper.class) {
                if (indexWriter == null) {
                    if (dir == null) {
                        dir = FSDirectory.open(Paths.get(indexPath));
                    }
                    //Analyzer analyzer = new StandardAnalyzer();
                    IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
                    if (create) {
                        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
                    } else {
                        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
                    }
                    indexWriter = new IndexWriter(dir, iwc);
                }
            }
        }
        return indexWriter;
    }

    private  IndexSearcher getSearcher() throws IOException {
        if (indexSearcher == null) {
            synchronized (LuceneHelper.class) {
                if (indexSearcher == null) {
                    if (dir == null) {
                        dir = FSDirectory.open(Paths.get(indexPath));
                    }
                    if (indexReader == null) {
                        indexReader = DirectoryReader.open(dir);
                    }
                    indexSearcher = new IndexSearcher(indexReader);
                }
            }
        }

        return indexSearcher;
    }


    public  void search(String keyword, String fieldName) {
        try {
            // Parse a simple query that searches for "text":
            QueryParser parser = new QueryParser(fieldName, analyzer);
            Query query  = parser.parse(keyword);

            execQuery(query);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public  void termQuery(String keyword, String fieldName) {
        try {
            // Parse a simple query that searches for "text":
            TermQuery query = new TermQuery(new Term(fieldName,keyword));

            execQuery(query);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public  void termQuery(int value, String fieldName) {
        try {
            Query query=IntPoint.newExactQuery(fieldName,value);
            execQuery(query);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public  void multiFieldSearch(String keyword, String... fieldNames) {
        //指定搜索字段和分析器
        QueryParser queryParser = new MultiFieldQueryParser(fieldNames, analyzer);

        //用户输入内容
        try {
            Query query = queryParser.parse(keyword);
            execQuery(query);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    /**
     * TermRangeQuery 范围查询
     *TermRangeQuery是用于字符串范围查询的，既然涉及到范围必然需要字符串比较大小，
     * 字符串比较大小其实比较的是ASC码值，即ASC码范围查询。
     * 一般对于英文来说，进行ASC码范围查询还有那么一点意义，
     * 中文汉字进行ASC码值比较没什么太大意义，所以这个TermRangeQuery了解就行，
     * 用途不太大，一般数字范围查询NumericRangeQuery用的比较多一点，
     * 比如价格，年龄，金额，数量等等都涉及到数字，数字范围查询需求也很普遍。
     * @throws Exception
     */
    public void testTermRangeQuery()throws Exception{
        String searchField="contents";
        String q="1000001----1000002";
        String lowerTermString = "1000001";
        String upperTermString = "1000003";
        /**
         * field  字段
         * lowerterm -范围的下端的文字
         *upperterm -范围的上限内的文本
         *includelower -如果真的lowerterm被纳入范围。
         *includeupper -如果真的upperterm被纳入范围。
         *https://yq.aliyun.com/articles/45353
         */
        Query query=new TermRangeQuery(searchField,new BytesRef(lowerTermString),new BytesRef(upperTermString),true,true);
        TopDocs hits=getSearcher().search(query, 10);
        System.out.println("匹配 '"+q+"'，总共查询到"+hits.totalHits+"个文档");
        for(ScoreDoc scoreDoc:hits.scoreDocs){
            Document doc=getSearcher().doc(scoreDoc.doc);
            System.out.println(doc.get("fullPath"));
        }
    }
    /**
     * 多条件查询
     * <p>
     * BooleanQuery也是实际开发过程中经常使用的一种Query。
     * 它其实是一个组合的Query，在使用时可以把各种Query对象添加进去并标明它们之间的逻辑关系。
     * BooleanQuery本身来讲是一个布尔子句的容器，它提供了专门的API方法往其中添加子句，
     * 并标明它们之间的关系，以下代码为BooleanQuery提供的用于添加子句的API接口：
     *
     */
    //todo 改成可解析表达式
    public void BooleanQueryTest(){

        String searchField1 = "title";
        String searchField2 = "content";
        Query query1 = new TermQuery(new Term(searchField1, "Spark"));
        Query query2 = new TermQuery(new Term(searchField2, "Apache"));
        BooleanQuery.Builder builder = new BooleanQuery.Builder();

        // BooleanClause用于表示布尔查询子句关系的类，
        // 包 括：
        // BooleanClause.Occur.MUST，
        // BooleanClause.Occur.MUST_NOT，
        // BooleanClause.Occur.SHOULD。
        // 必须包含,不能包含,可以包含三种.有以下6种组合：
        //
        // 1．MUST和MUST：取得连个查询子句的交集。
        // 2．MUST和MUST_NOT：表示查询结果中不能包含MUST_NOT所对应得查询子句的检索结果。
        // 3．SHOULD与MUST_NOT：连用时，功能同MUST和MUST_NOT。
        // 4．SHOULD与MUST连用时，结果为MUST子句的检索结果,但是SHOULD可影响排序。
        // 5．SHOULD与SHOULD：表示“或”关系，最终检索结果为所有检索子句的并集。
        // 6．MUST_NOT和MUST_NOT：无意义，检索无结果。

        builder.add(query1, BooleanClause.Occur.SHOULD);
        builder.add(query2, BooleanClause.Occur.SHOULD);

        BooleanQuery query = builder.build();

        //执行查询，并打印查询到的记录数
        try {
            execQuery(query);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 匹配前缀
     * <p>
     * PrefixQuery用于匹配其索引开始以指定的字符串的文档。就是文档中存在xxx%
     * <p>
     *
     */
    public void prefixQueryTest(String prefix,String searchField)  {
        Term term = new Term(searchField, prefix);
        Query query = new PrefixQuery(term);

        try {
            execQuery(query);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 短语搜索
     * <p>
     * 所谓PhraseQuery，就是通过短语来检索，比如我想查“big car”这个短语，
     * 那么如果待匹配的document的指定项里包含了"big car"这个短语，
     * 这个document就算匹配成功。可如果待匹配的句子里包含的是“big black car”，
     * 那么就无法匹配成功了，如果也想让这个匹配，就需要设定slop，
     * 先给出slop的概念：slop是指两个项的位置之间允许的最大间隔距离
     *
     */
    public void phraseQueryTest(String keywordStart,String keywordEnd,String field) {

        PhraseQuery.Builder builder = new PhraseQuery.Builder();
        builder.add(new Term(field, keywordStart));
        builder.add(new Term(field, keywordEnd));
        builder.setSlop(0);
        PhraseQuery phraseQuery = builder.build();

        try {
            execQuery(phraseQuery);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通配符搜索
     * <p>
     * Lucene也提供了通配符的查询，这就是WildcardQuery。
     * 通配符“?”代表1个字符，而“*”则代表0至多个字符。
     *
     */
    public  void wildcardSearch(String keyword,String fieldName){
        //Term term = new Term(fieldName, "大*规模");
        Term term=new Term(fieldName,keyword);
        Query query = new WildcardQuery(term);

        try {
            execQuery(query);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 相近词语搜索
     * <p>
     * FuzzyQuery是一种模糊查询，它可以简单地识别两个相近的词语。
     *
     */
    public void fuzzyQueryTest(String keyword,String fieldName)  {

        Term t = new Term(fieldName, keyword);
        Query query = new FuzzyQuery(t);
        try {
            execQuery(query);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public  void execQuery(Query query) throws IOException {
        ScoreDoc[] hits = getSearcher().search(query, 1000).scoreDocs;

        // Iterate through the results:
        for (int i = 0; i < hits.length; i++) {
            Document hitDoc = indexSearcher.doc(hits[i].doc);
            System.out.println(hitDoc);
        }
    }

    public  void addDocument(Document... docs) {
        try {
            getWriter(false).addDocuments(toIterable(Arrays.asList(docs).iterator()));
            getWriter(false).commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private  Iterable<Document> toIterable(Iterator<Document> iterator) {
        return () -> iterator;
    }

    /**
     调用updateDocument的方法,传给它一个新的doc来更新数据,
     先去索引文件里查找id为1234567的Doc,如果有就更新它(如果有多条,最后更新后只有一条)。如果没有就新增.
     数据库更新的时候,我们可以只针对某个列来更新,而lucene只能针对一行数据更新。
     */
    public  void update(Term term, Document document){
        try {
            getWriter(false).updateDocument(term, document);
            getWriter(false).commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  void close() {
        try {
            dir.close();
            indexWriter.close();
            indexReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * IKAnalyzer  中文分词器
     * SmartChineseAnalyzer  smartcn分词器 需要lucene依赖 且和lucene版本同步
     *
     * @throws IOException
     */
    public void AnalyzerTest() throws IOException {
        //Analyzer analyzer = new StandardAnalyzer(); // 标准分词器，适用于英文
        //Analyzer analyzer = new SmartChineseAnalyzer();//中文分词
        //Analyzer analyzer = new ComplexAnalyzer();//中文分词
        //Analyzer analyzer = new IKAnalyzer();//中文分词

        Analyzer analyzer = null;
        String text = "Apache Spark 是专为大规模数据处理而设计的快速通用的计算引擎";

        analyzer = new IKAnalyzer();//IKAnalyzer 中文分词
        printAnalyzerDoc(analyzer, text);
        System.out.println();

        //analyzer = new ComplexAnalyzer();//MMSeg4j 中文分词
        //printAnalyzerDoc(analyzer, text);
        //System.out.println();

        analyzer = new SmartChineseAnalyzer();//Lucene 中文分词器
        printAnalyzerDoc(analyzer, text);
    }


    /**
     * 高亮处理
     *
     * @throws IOException
     */
    public void HighlighterTest() throws IOException, ParseException, InvalidTokenOffsetsException {
        //Analyzer analyzer = new StandardAnalyzer(); // 标准分词器，适用于英文
        //Analyzer analyzer = new SmartChineseAnalyzer();//中文分词
        //Analyzer analyzer = new ComplexAnalyzer();//中文分词
        //Analyzer analyzer = new IKAnalyzer();//中文分词

        Analyzer analyzer = new IKAnalyzer();//中文分词

        String searchField = "content";
        String text = "Apache Spark 大规模数据处理";

        //指定搜索字段和分析器
        QueryParser parser = new QueryParser(searchField, analyzer);

        //用户输入内容
        Query query = parser.parse(text);

        TopDocs topDocs = indexSearcher.search(query, 100);

        // 关键字高亮显示的html标签，需要导入lucene-highlighter-xxx.jar
        SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<span style='color:red'>", "</span>");
        Highlighter highlighter = new Highlighter(simpleHTMLFormatter, new QueryScorer(query));

        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {

            //取得对应的文档对象
            Document document = indexSearcher.doc(scoreDoc.doc);

            // 内容增加高亮显示
            TokenStream tokenStream = analyzer.tokenStream("content", new StringReader(document.get("content")));
            String content = highlighter.getBestFragment(tokenStream, document.get("content"));

            System.out.println(content);
        }

    }
    /**
     * 分词打印
     *
     * @param analyzer
     * @param text
     * @throws IOException
     */
    public void printAnalyzerDoc(Analyzer analyzer, String text) throws IOException {

        TokenStream tokenStream = analyzer.tokenStream("content", new StringReader(text));
        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
        try {
            tokenStream.reset();
            while (tokenStream.incrementToken()) {
                System.out.println(charTermAttribute.toString());
            }
            tokenStream.end();
        } finally {
            tokenStream.close();
            analyzer.close();
        }
    }

    public static void main(String[] args) {
        //System.out.println(System.getProperty("user.home"));
        //System.out.println(File.pathSeparator + ":" + File.separator + ":");
        LuceneHelper helper=new LuceneHelper();
        Document doc = new Document();
        int id = 1;
        //将字段加入到doc中
       // https://stackoverflow.com/questions/42659616/lucene-6-adding-intfields
        doc.add(new IntPoint("id", id));
        doc.add(new StoredField("id", id));

        doc.add(new StringField("title", "Spark", Field.Store.YES));
        doc.add(new TextField("content", "Apache Spark 是专为大规模数据处理而设计的快速通用的计算引擎", Field.Store.YES));

        //helper.addDocument(doc);
        //helper.addDocument(doc);
        //helper.termQuery(1, "id");

        helper.search("12312","content");
        //doc=new Document();
        //doc.add(new IntPoint("id", id));
        //doc.add(new StoredField("id", id));
        //
        //doc.add(new StringField("title", "Spark222", Field.Store.YES));
        //doc.add(new TextField("content", "Apache Spark 是专为大规模数据处理而设计的快速通用的计算引擎", Field.Store.YES));
        //
        //helper.update(new Term("title","Spark"),doc);
        //helper.termQuery(1, "id");
    }
}
