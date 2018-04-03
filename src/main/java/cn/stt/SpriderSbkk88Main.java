package cn.stt;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 随便看看吧
 *
 * @Author shitongtong
 * <p>
 * Created by shitongtong on 2018/4/2.
 */
public class SpriderSbkk88Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpriderSbkk88Main.class);
    private static final String BASEDIR = "/sprider-bixia/";
    private static Map<String, String> novelMap = new HashMap<>();

    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();
        Map<String, String> novelMap = new HashMap<>();
//        novelMap.put("扶摇皇后", "https://www.sbkk88.com/wangluo/fuyaohuanghou/");
//        novelMap.put("三生三世十里桃花", "https://www.sbkk88.com/wangluo/sanshengsanshishilitaohua/");
        novelMap.put("小时代", "https://www.sbkk88.com/mingzhu/zhongguoxiandaiwenxuemingzhu/xiaoshidai/");
//        novelMap.put("择天记", "http://www.kanshuzhong.com/book/35155/");


        Set<Map.Entry<String, String>> entrySet = novelMap.entrySet();
        for (Map.Entry<String, String> entry : entrySet) {
            String novelName = entry.getKey();
            String novelUrl = entry.getValue();
            spriderNovel(novelName, novelUrl);
        }

        LOGGER.info("结束，时间:{}s", (System.currentTimeMillis() - startTime) / 1000);

    }

    public static void spriderNovel(String novelName, String url) {
        new Thread(() -> {
            String novelDirPath = BASEDIR + novelName;
            File novelDir = new File(novelDirPath);
            if (!novelDir.exists()) {
                novelDir.mkdirs();
            }
            try {
//                writerToFile(url, novelDirPath);
                writerToFile2(url, novelDirPath);   //小时代特殊
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void writerToFile(String url, String novelDirPath) throws IOException {
        long startTime = System.currentTimeMillis();

        Connection connect = Jsoup.connect(url);
        Document document = connect.get();
        //章节名及其链接节点
        Elements chapterElements = document.select("div.mingzhuLeft > ul > li > a");
        Iterator<Element> chapterElement = chapterElements.iterator();

        String dirPath = novelDirPath + File.separator + "0章节目录.txt";
        BufferedWriter writer = new BufferedWriter(new FileWriter(dirPath));
        BufferedWriter bw = null;
//        List<Map<String, String>> chapterMapList = new ArrayList<>();
//        Map<String, String> chapterMap = new HashMap<>();
        int count = 1;
        while (chapterElement.hasNext()) {
            Element element = chapterElement.next();
            String href = element.attr("href"); //章节链接
            href = href.substring(href.lastIndexOf("/"));
            String title = element.text();  //章节名称
            String content = Jsoup.connect(url + href).get().select("#f_article").html();   //章节内容
            String path = novelDirPath + File.separator + count++ + ".txt";
            bw = new BufferedWriter(new FileWriter(path));
            bw.write(content);
            bw.close();
            writer.write(title);
            writer.write(System.lineSeparator());
            writer.flush();
            /*chapterMap = new HashMap<>();
            chapterMap.put("href", href);
            chapterMap.put("title", title);
            chapterMapList.add(chapterMap);
            LOGGER.info("href={}", href);
            LOGGER.info("text={}", text);*/
        }
        writer.close();
        LOGGER.info("{}结束，时间:{}s", novelDirPath, (System.currentTimeMillis() - startTime) / 1000);
    }

    /**
     * 小时代。。
     * @param url
     * @param novelDirPath
     * @throws IOException
     */
    public static void writerToFile2(String url, String novelDirPath) throws IOException {
        long startTime = System.currentTimeMillis();

        Connection connect = Jsoup.connect(url);
        Document document = connect.get();
        //章节名及其链接节点
        Elements chapterElements = document.select("div.book_list > div > a");
        Iterator<Element> chapterElement = chapterElements.iterator();

        String dirPath = novelDirPath + File.separator + "0章节目录.txt";
        BufferedWriter writer = new BufferedWriter(new FileWriter(dirPath));
        BufferedWriter bw = null;
//        List<Map<String, String>> chapterMapList = new ArrayList<>();
//        Map<String, String> chapterMap = new HashMap<>();
        int count = 1;
        url = "https://www.sbkk88.com";
        while (chapterElement.hasNext()) {
            Element element = chapterElement.next();
            String href = element.attr("href"); //章节链接
//            href = href.substring(href.lastIndexOf("/"));
            String title = element.text();  //章节名称
            String content = Jsoup.connect(url + href).get().select("#f_article").html();   //章节内容
            String path = novelDirPath + File.separator + count++ + ".txt";
            bw = new BufferedWriter(new FileWriter(path));
            bw.write(content);
            bw.close();
            writer.write(title);
            writer.write(System.lineSeparator());
            writer.flush();
            /*chapterMap = new HashMap<>();
            chapterMap.put("href", href);
            chapterMap.put("title", title);
            chapterMapList.add(chapterMap);
            LOGGER.info("href={}", href);
            LOGGER.info("text={}", text);*/
        }
        writer.close();
        LOGGER.info("{}结束，时间:{}s", novelDirPath, (System.currentTimeMillis() - startTime) / 1000);
    }
}
