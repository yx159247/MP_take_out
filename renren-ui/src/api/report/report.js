import http from "@/utils/request";

// 查询今日订单量
export const getToDayOrderApi = () => {
    return http.get(
        'takeout/report/getToDayOrder',

    )
}

// 查询昨日订单量
export const getYesDayOrderApi = () => {
    return http.get(
        'takeout/report/getYesDayOrder',

    )
}

//查询近一周的订单流水
export const getOneWeekLiuShuiApi = () => {
    return http.get(
        'takeout/report/getOneWeekLiuShui',

    )
}


//查询近一周的订单数量
export const getOneWeekOrderApi = () => {
    return http.get(
        'takeout/report/getOneWeekOrder',

    )
}


//查询热门套餐
export const getHotSealApi = () => {
    return http.get(
        'takeout/report/getHotSeal',

    )
}

//查询会员总数
export const getUserCountApi = () => {
    return http.get(
        'takeout/report/getUserCount',

    )
}
//查询员工总数
export const getEmpCountApi = () => {
    return http.get(
        'takeout/report/getEmpCount',

    )
}


