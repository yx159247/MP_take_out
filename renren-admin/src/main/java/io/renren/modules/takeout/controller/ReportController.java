package io.renren.modules.takeout.controller;

import io.renren.common.utils.Result;
import io.renren.modules.sys.service.SysUserService;
import io.renren.modules.takeout.service.OrdersService;
import io.renren.modules.takeout.service.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author Yangx
 */
@RestController
@RequestMapping("takeout/report")
@Api(tags="运营统计")
public class ReportController {

    @Autowired
    private UserService userService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private OrdersService ordersService;

    @GetMapping("/getToDayOrder")
    public Result<Long> getToDayOrder(){
        long count = ordersService.countToDayOrder();
        return new Result<Long>().ok(count);
    }

    @GetMapping("/getYesDayOrder")
    public Result<Long> getYesDayOrder(){
        long count = ordersService.countYesDayOrder();
        return new Result<Long>().ok(count);
    }

    @GetMapping("/getOneWeekLiuShui")
    public Result<Map> getOneWeekLiuShui(){
        Map map = ordersService.OneWeekLiuShui();
        return new Result<Map>().ok(map);
    }

    @GetMapping("/getOneWeekOrder")
    public Result<Map> getOneWeekOrder(){
        Map map = ordersService.OneWeekOrder();
        return new Result<Map>().ok(map);
    }

    @GetMapping("/getHotSeal")
    public Result<Map> getHotSeal(){
        Map map = ordersService.hotSeal();
        return new Result<Map>().ok(map);
    }
    @GetMapping("/getUserCount")
    public Result<Long> getUserCount() {

        long count = userService.userCount();
        return new Result<Long>().ok(count);
    }
    @GetMapping("/getEmpCount")
    public Result<Long> getEmpCount(){

        long count = sysUserService.empCount();
        return new Result<Long>().ok(count);
    }

}
