import http from "@/utils/request";

// 获取菜品分类列表
export const getCategoryList = (params) => {
    return http.get(
        '/takeout/category/list',
        {
            params
        }
    )
}

// 查菜品列表的接口
export const queryDishList = (params) => {
    return http.get(
        'takeout/dish/list',
        {params}
    )}


