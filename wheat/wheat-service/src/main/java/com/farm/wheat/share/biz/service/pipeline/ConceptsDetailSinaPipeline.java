package com.farm.wheat.share.biz.service.pipeline;

import com.alibaba.fastjson.JSONObject;
import com.farm.common.utils.NullCheckUtils;
import com.farm.wheat.share.biz.constant.ShareConst;
import com.farm.wheat.share.biz.dto.ShareConceptDetailDTO;
import com.farm.wheat.share.biz.mapper.simple.ShareConceptDetailMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description: 新浪
 * @author: xyc
 * @create: 2019-10-26 00:41
 */
@Service
public class ConceptsDetailSinaPipeline implements Pipeline {
    @Resource
    private ShareConceptDetailMapper shareConceptDetailMapper;

    @Transactional
    @Override
    public void process(ResultItems resultItems, Task task) {
        String conceptDetail = resultItems.get(ShareConst.CONCEPT_DETAIL_LIST).toString();
        if (NullCheckUtils.isNotBlank(conceptDetail)) {
            List<ShareConceptDetailDTO> list = JSONObject.parseArray(conceptDetail, ShareConceptDetailDTO.class);
            // 先清空
            shareConceptDetailMapper.deleteBySimpleName(list.get(0).getSimpleName());
            shareConceptDetailMapper.insertList(list);
        }
    }
}
