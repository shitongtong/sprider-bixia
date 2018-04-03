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
import java.util.Iterator;

/**
 * @Author shitongtong
 * <p>
 * Created by shitongtong on 2018/4/2.
 */
public class SpriderMain {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpriderMain.class);

    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();
        String baseDir = "/sprider-bixia/";

        new Thread(() -> {
            String novelName = "武道宗师";
            String novelDirPath = baseDir + novelName;
            File novelDir = new File(novelDirPath);
            if (!novelDir.exists()) {
                novelDir.mkdirs();
            }
            String url = "https://www.bixia.org/29_29872/";
            try {
                writerToFile(url, novelDirPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            String novelName = "星辰之主";
            String novelDirPath = baseDir + novelName;
            File novelDir = new File(novelDirPath);
            if (!novelDir.exists()) {
                novelDir.mkdirs();
            }
            String url = "https://www.bixia.org/29_29511/";
            try {
                writerToFile(url, novelDirPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();


        LOGGER.info("结束，时间:{}s", (System.currentTimeMillis() - startTime) / 1000);

    }

    public static void writerToFile(String url, String novelDirPath) throws IOException {
        long startTime = System.currentTimeMillis();

        Connection connect = Jsoup.connect(url);
        Document document = connect.get();
        Elements chapterElements = document.select("#list > dl > dd > a");
        Iterator<Element> chapterElement = chapterElements.iterator();

        String dirPath = novelDirPath + File.separator + "0章节目录.txt";
        BufferedWriter writer = new BufferedWriter(new FileWriter(dirPath));
        BufferedWriter bw = null;
//        List<Map<String, String>> chapterMapList = new ArrayList<>();
//        Map<String, String> chapterMap = new HashMap<>();
        int count = 1;
        while (chapterElement.hasNext()) {
            Element element = chapterElement.next();
            String href = element.attr("href");
            String title = element.text();
            String content = Jsoup.connect(url + href).get().select("#content").html();
            String path = novelDirPath + File.separator + count++ + ".txt";
            bw = new BufferedWriter(new FileWriter(path));
            bw.write(content);
            bw.close();
            writer.write(title);
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

    /*public List<Map<String,String>> getChapterMapList(){

    }*/
}
