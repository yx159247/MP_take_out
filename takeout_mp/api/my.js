import {getBaseUrl,requestUtil,getUserProfile,getWxLogin} from '../utils/requestUtils';
import regeneratorRuntime, { async } from '../lib/runtime/runtime';

export const logoutApi = (data) =>{
	return new Promise((resolve, reject) => {
	    requestUtil({
	        url: 'mp/logout',
	        method: 'post',
	        data:data
	    }).then((res) => {
	        console.log(res.data)
	        resolve(res)
	    }).catch((err) => {
	        reject(err)
	    })
	});
}

export const updateUserInfoApi = (data) =>{
	return new Promise((resolve, reject) => {
	    requestUtil({
	        url: 'mp/user/updateUserInfo',
	        method: 'put',
	        data:data
	    }).then((res) => {
	        console.log(res.data)
	        resolve(res)
	    }).catch((err) => {
	        reject(err)
	    })
	});
}

export const phoneLoginApi = (data) =>{
	return new Promise((resolve, reject) => {
	    requestUtil({
	        url: 'mp/phoneLogin',
	        method: 'post',
	        data:data
	    }).then((res) => {
	        console.log(res.data)
	        resolve(res)
	    }).catch((err) => {
	        reject(err)
	    })
	});
}

export const sendValidateCodeApi = (data) =>{
	return new Promise((resolve, reject) => {
	    requestUtil({
	        url: `mp/sendValidateCode?`,
	        method: 'get',
			data : data
	    }).then((res) => {
	        console.log(res.data)
	        resolve(res)
	    }).catch((err) => {
	        reject(err)
	    })
	});
}

