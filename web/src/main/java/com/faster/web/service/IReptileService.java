package com.faster.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.faster.entity.web.Reptile;

import java.io.IOException;

/**
 * 爬虫接口
 *
 * @author da.yang@hand-china.com
 * @date 2020/8/18
 */
public interface IReptileService extends IService<Reptile>{


    /**
     * 爬取网络资源
     *
     * @param reptile 爬虫对象
     */
    void reptileNetworkResource(Reptile reptile) throws IOException;

}
