package com.faster.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.faster.entity.web.Reptile;
import com.faster.enums.ReptileEnum;
import com.faster.web.mapper.ReptileMapper;
import com.faster.web.service.IReptileService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * 爬虫接口实现类
 *
 * @author da.yang@hand-china.com
 * @date 2020/8/18
 */
//@Service
@Slf4j
public class ReptileServiceImpl extends ServiceImpl<ReptileMapper, Reptile> implements IReptileService {
    // 模拟浏览器请求
    private static final String USER_AGENT_VALUE  = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36";

    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    /**
     * 资源下载之后，保存在本地的文件路径, 默认下载到桌面
     */
    private String downloadPath = FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath();

    @Override
    public void reptileNetworkResource(Reptile reptile) throws IOException {
        // 资源所在的网页地址  酷狗TOP: https://www.kugou.com/yy/rank/home/1-8888.html?from=rank
        String resourceURL = reptile.getResourceURL();
        downloadPath = reptile.getDownloadPath();
        // 需要爬取的文件类型 目前支持: img txt mp3
        String fileType = reptile.getFileType();
        // 属性匹配: 根据爬取的文件类型进一步匹配标签属性, 如：src、href、title
        String attributeMatch = reptile.getAttributeMatch();

        // 从一个网站获取和解析一个HTML文档
        Document document = Jsoup.connect(resourceURL).get();
        // 标签元素
        Elements elements = new Elements();
        if(ReptileEnum.IMG.getCode().equals(fileType)) {
            // 获取所有的img标签
            elements = document.getElementsByTag(fileType);
        }else if(ReptileEnum.MP3.getCode().equals(fileType)) {
            // https://webfs.yun.kugou.com/202008200844/915b018d6852627aa2868b2210e91ac8/G209/M09/0D/1F/sZQEAF6lRImAFAazADryyMZQqU8483.mp3
            elements = document.select("div.pc_temp_songlist a");
            //attributeMatch = "href";
        }else if(ReptileEnum.TXT.getCode().equals(fileType)) {

        }
        log.info("elements: {}", elements.toString());
        // 解压为zip文件 , 暂时不考虑压缩文件
        /// ZipOutputStream outFile = new ZipOutputStream(new FileOutputStream("E://picture//"+System.currentTimeMillis()+".zip"));
        // 下载量统计
        int count = 0;
        for(Element element : elements){
            // 获取每个img标签的src属性的内容，即图片地址，加"abs:"表示绝对路径
            String resourceSrc = element.attr("abs:"+attributeMatch);
            // 格式化url
            resourceSrc = formatUrl(resourceSrc);
            // 获取网络资源
            InputStream resource = getResource(resourceSrc);
            // 截取url中的后面部分作为文件名：例如“20150529/PP6A7429_副本1.jpg”
            String fileName = resourceSrc.substring(resourceSrc.lastIndexOf("/")+1);
            // 下载文件到电脑的本地硬盘上
            downFile(fileName, resource);
            log.info("{}下载完成：{}", fileType, resourceSrc);
            count++;
        }
        log.info("共下载了{}个文件", count);
    }

    /**
     *
     * 根据图片的外网地址下载图片到本地硬盘的filePath
     * @param fileName 文件名
     * @param resource 需要下载的网络资源
     */
    private void downFile(String fileName, InputStream resource) {
        try {
            // 创建文件目录
            File files = new File(downloadPath);
            if (!files.exists()) {
                files.mkdirs();
            }

            //创建文件，fileName为编码之前的文件名
            File file = new File(downloadPath + dtf.format(LocalDateTime.now()) +fileName);
            // 使用缓冲流读取文件
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
            byte[] bytes = new byte[1024];
            int len = -1;
            while((len = resource.read(bytes)) != -1){
                out.write(bytes, 0, len);
            }
            out.close();
            resource.close();
        } catch (Exception e) {
            log.error("error: ", e);
        }
    }

    /**
     * 格式化url
     *
     * @param resourceSrc  需要处理的URL
     * @return String
     * @throws UnsupportedEncodingException
     */
    private String formatUrl(String resourceSrc) throws UnsupportedEncodingException {
        // url中的前面部分：例如"http://images.csdn.net/"
        String beforeUrl = resourceSrc.substring(0, resourceSrc. lastIndexOf("/")+1);
        // url中的后面部分：例如“20150529/PP6A7429_副本1.jpg”
        String fileName = resourceSrc.substring(resourceSrc.lastIndexOf("/")+1);
        // 编码之后的fileName，空格会变成字符"+"
        String newFileName = URLEncoder.encode(fileName, "UTF-8");
        // 把编码之后的fileName中的字符"+"，替换为UTF-8中的空格表示："%20"
        newFileName = newFileName.replaceAll("\\+", "\\%20");
        // 编码之后的url
        resourceSrc = beforeUrl + newFileName;
        return resourceSrc;
    }

    /**
     * 根据url获取网络资源
     *
     * @param resourceSrc   网络资源url
     * @return InputStream
     * @throws IOException
     */
    private InputStream getResource(String resourceSrc) throws IOException {
        // 获取下载地址
        URL url = new URL(resourceSrc);
        // 链接网络地址
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        // 设置20秒的相应时间
        connection.setConnectTimeout(20 * 1000);
        // 模拟浏览器请求
        connection.setRequestProperty("User-Agent", USER_AGENT_VALUE);
        //获取链接的输出流
        return connection.getInputStream();
    }

    /**
     * 爬取音乐文件
     */
    private Elements reptileMusic(String url) throws IOException {
        // 从一个网站获取和解析一个HTML文档
        Document document = Jsoup.connect(url).get();
        return null;
    }


}
