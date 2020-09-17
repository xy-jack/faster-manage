package com.faster.entity.web;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 爬虫相关参数
 *
 * @author da.yang@hand-china.com
 * @date 2020/8/19
 */
@Data
@Accessors(chain = true)
public class Reptile {

    /**
     * 下载地址
     */
    private String downloadPath;

    /**
     * 资源地址
     */
    private String resourceURL;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 根据爬取的文件类型进一步匹配标签属性
     */
    private String attributeMatch;

    /**
     * 自定义选择器进去匹配
     */
    private String selector;

    /**
     * 压缩文件名
     */
    private String zipFileName;



}
