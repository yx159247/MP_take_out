import http from "@/utils/request";
// 查看接口
export const queryOrderDetailById = (id) => {
    return http.get(
        `/takeout/orderdetail/detailInfo/${id}`
    )
}

// 取消，派送，完成接口
export const editOrderDetail = (params) => {
    return http.put(
        '/takeout/orders',
        {...params}
    )

}

