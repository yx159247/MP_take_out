import {getBaseUrl,requestUtil,getUserProfile,getWxLogin} from '../utils/requestUtils';
import regeneratorRuntime, { async } from '../lib/runtime/runtime';


export const  addCartApi= (data) =>{
    return new Promise((resolve, reject) => {
        requestUtil({
            url: 'mp/shoppingCart/add',
            method: 'post',
            data: data
        }).then((res) => {
            console.log(res.data)
            resolve(res)
        }).catch((err) => {
            reject(err)
        })
    });
}

export const  updateCartApi= (data) =>{
    return new Promise((resolve, reject) => {
        requestUtil({
            url: 'mp/shoppingCart/sub',
            method: 'post',
            data: data
        }).then((res) => {
            console.log(res.data)
            resolve(res)
        }).catch((err) => {
            reject(err)
        })
    });
}
export const  clearCartApi= () =>{
    return new Promise((resolve, reject) => {
        requestUtil({
            url: 'mp/shoppingCart/clean',
            method: 'delete'
            
        }).then((res) => {
            console.log(res.data)
            resolve(res)
        }).catch((err) => {
            reject(err)
        })
    });
}

export const  setMealDishDetailsApi= (id) =>{
    return new Promise((resolve, reject) => {
        requestUtil({
            url: `mp/setmeal/${id}`,
            method: "get",
            
        }).then((res) => {
            console.log(res.data)
            resolve(res)
        }).catch((err) => {
            reject(err)
        })
    });
}

export const  dishListApi= (data) =>{
    return new Promise((resolve, reject) => {
        requestUtil({
            url: 'mp/dish/list',
            method: "get",
            data: { ...data }
        }).then((res) => {
            console.log(res.data)
            resolve(res)
        }).catch((err) => {
            reject(err)
        })
    });
}
export const  setmealListApi= (data) =>{
    return new Promise((resolve, reject) => {
        requestUtil({
            url: 'mp/setmeal/list',
            method: "get",
            data: { ...data }
        }).then((res) => {
            console.log(res.data)
            resolve(res)
        }).catch((err) => {
            reject(err)
        })
    });
}
export const categoryListApi = () => {
    return new Promise((resolve, reject) => {
        requestUtil({
            url: "mp/category/list",
            method: "get"
        }).then((res) => {
            console.log(res.data)
            resolve(res)
        }).catch((err) => {
            reject(err)
        })
    });
}
export const cartListApi = (data) => {
    return new Promise((resolve, reject) => {
        requestUtil({
            url: "mp/shoppingCart/list",
            method: "get",
            data: { ...data }
        }).then((res) => {
            console.log(res.data)
            resolve(res)
        }).catch((err) => {
            reject(err)
        })
    });
}

