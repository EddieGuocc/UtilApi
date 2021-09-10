package com.gcy.bootwithutils.service.bill;

import com.gcy.bootwithutils.dao.CIBNBillDao;
import com.gcy.bootwithutils.dao.ThirdPartBillDao;
import com.gcy.bootwithutils.model.dto.CIBNBillDto;
import com.gcy.bootwithutils.model.dto.ThirdPartBillDto;
import com.gcy.bootwithutils.model.vo.AsynTaskVO;
import com.gcy.bootwithutils.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@Service
public class AllBillService {

    private static String UNION_PAY_BILL_EDGE_TIME = " 23:00:00";
    private static String WECHAT_PAY_BILL_EDGE_TIME = " 00:00:00";
    private static String ALI_PAY_BILL_EDGE_TIME = " 00:00:00";


    @Resource
    private ThirdPartBillDao thirdPartBillDao;

    @Autowired
    private CIBNBillDao cibnBillDao;

    @Transactional
    public int processBill(AsynTaskVO vo) {
        int res = 0;
        System.out.println("对账日期起：" + vo.getStartDate());
        System.out.println("对账日期止：" + vo.getEndDate());
        // todo 根据日期计算出 所有需要对账的具体日期
        System.out.println(DateUtil.getDayList(vo.getStartDate(), vo.getEndDate()));
        // 三方对账

        Random random = new Random();
        long nowTime = System.currentTimeMillis() / 1000;
        String thirdNewOrderId = "100" + nowTime + random.nextInt(100);
        ThirdPartBillDto input = new ThirdPartBillDto(123, thirdNewOrderId, "第三方支付产品", nowTime, 666666 );
        if (thirdPartBillDao.insert(input) == 0) {
            throw new RuntimeException("三方对账失败！");
        }

        List<ThirdPartBillDto> thirdPartBillList =  thirdPartBillDao.select(1);
        System.out.println("三方对账检索完毕，获取到数据" + thirdPartBillList.size() + "条");
        res += thirdPartBillList.size();


        // 业务对账

        // 国广对账
        nowTime = System.currentTimeMillis() / 1000;
        String cibnOrderId = "100" + nowTime + random.nextInt(100);
        CIBNBillDto dto = new CIBNBillDto(new BigDecimal("25.00"), cibnOrderId, nowTime,
                666666, 1234, "测试商品");
        if (cibnBillDao.insert(dto) == 0) {
            throw new RuntimeException("国广对账失败！");
        }
        List<CIBNBillDto> cibnBillList = cibnBillDao.select(1);
        System.out.println("国广对账检索完毕，获取到数据" + cibnBillList.size() + "条");
        throw new RuntimeException("模拟未知异常");
        //res += cibnBillList.size();
        //return res;
    }
}
