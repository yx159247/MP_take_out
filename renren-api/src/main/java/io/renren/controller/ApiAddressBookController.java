package io.renren.controller;

import io.renren.annotation.Login;
import io.renren.common.entity.AddressBookEntity;
import io.renren.common.utils.Result;
import io.renren.dto.AddressBookDTO;
import io.renren.service.AddressBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 微信小程序地址模块
 *
 * @author David 321740709@qq.com
 * @since 1.0.0 2022-08-17
 */
@RestController
@RequestMapping("mp/addressBook")
@Api(tags = "微信小程序地址模块")
public class ApiAddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    @Login
    @GetMapping("/default")
    @ApiOperation("默认地址")
    public Result defaultAddress(Long userId) {
        return new Result<>().ok(addressBookService.findDefaultAddress(userId));
    }

    @Login
    @PutMapping
    @ApiOperation("修改默认地址")
    public Result updateAddress(@RequestBody AddressBookDTO addressBookDTO) {

        //通过用户ID修改地址
        addressBookService.UpdateAddressByUserId(addressBookDTO);
        return new Result<>().ok(null);
    }

    @Login
    @PostMapping
    @ApiOperation("添加地址")
    public Result<AddressBookEntity> save(@RequestBody AddressBookDTO addressBookDTO) {

        AddressBookEntity addressBookEntity = addressBookService.saveAddress(addressBookDTO);
        return new Result<AddressBookEntity>().ok(addressBookEntity);
    }


    /**
     * 删除地址
     *
     * @param addressBookDTO dto
     * @return {@link Result}
     */
    @Login
    @DeleteMapping
    @ApiOperation("删除地址")
    public Result delete(@RequestBody AddressBookDTO addressBookDTO) {
        addressBookService.deleteById(addressBookDTO.getId());
        return new Result<>().ok(null);
    }

    /**
     * 地址详细信息
     */
    @Login
    @GetMapping("/{id}")
    @ApiOperation("地址详细信息")
    public Result<AddressBookEntity> info(@PathVariable("id") Long id) {
        AddressBookEntity addressBookEntity = addressBookService.selectById(id);
        //判断是否为空
        if (addressBookEntity == null) {
            return new Result<AddressBookEntity>().error("地址不存在");
        }
        return new Result<AddressBookEntity>().ok(addressBookEntity);
    }

    /**
     * 根据用户ID查询所有地址
     */
    @Login
    @GetMapping("/list")
    @ApiOperation("根据用户ID查询所有地址")
    public Result<List<AddressBookEntity>> list(AddressBookEntity addressBookEntity) {
        return new Result<List<AddressBookEntity>>().ok(addressBookService.findAddressByUserId(addressBookEntity));
    }

    /**
     * 设置默认地址
     */
    @Login
    @PutMapping("/default")
    @ApiOperation("设置默认地址")
    public Result setDefaultAddress(@RequestBody AddressBookDTO addressBookDTO) {
        addressBookService.setDefaultAddress(addressBookDTO);
        return new Result<>().ok(null);
    }

}
