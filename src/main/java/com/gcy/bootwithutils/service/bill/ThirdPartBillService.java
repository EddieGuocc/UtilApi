package com.gcy.bootwithutils.service.bill;

import com.gcy.bootwithutils.dao.ThirdPartBillDao;
import com.gcy.bootwithutils.model.dto.ThirdPartBillDto;
import com.gcy.bootwithutils.model.vo.AsynTaskVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
public class ThirdPartBillService {

    @Resource
    ThirdPartBillDao thirdPartBillDao;

    public List<ThirdPartBillDto> selectAll(AsynTaskVO vo) {
        System.out.println(vo);
        return thirdPartBillDao.select(1);
    }

    @Transactional
    public boolean insert() {
        Random random = new Random();
        long nowTime = System.currentTimeMillis() / 1000;
        String orderId = "100" + nowTime + random.nextInt(100);
        ThirdPartBillDto input = new ThirdPartBillDto(123, orderId, "第三方支付产品", nowTime, 666666 );
        return thirdPartBillDao.insert(input) == 1;
    }
}
