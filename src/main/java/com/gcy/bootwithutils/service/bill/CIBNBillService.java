package com.gcy.bootwithutils.service.bill;

import com.gcy.bootwithutils.dao.CIBNBillDao;
import com.gcy.bootwithutils.model.dto.CIBNBillDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
public class CIBNBillService {

    @Autowired
    private CIBNBillDao cibnBillDao;

    @Transactional
    public boolean insert() {
        Random random = new Random();
        long nowTime = System.currentTimeMillis() / 1000;
        String orderId = "100" + nowTime + random.nextInt(100);
        CIBNBillDto dto = new CIBNBillDto(new BigDecimal("25.00"), orderId, System.currentTimeMillis() / 1000,
                666666, 1234, "测试商品");
        return cibnBillDao.insert(dto) == 1;
    }


}
