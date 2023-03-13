
import regeneratorRuntime, { async } from '../lib/runtime/runtime';

export const logoutApi = (data) =>{
	return uni.$ajax.post({
		url: 'mp/logout',
		data: data
	})
}

export const updateUserInfoApi = (data) =>{
	return uni.$ajax.put({
		url: 'mp/user/updateUserInfo',
		data: data
	})
}

export const phoneLoginApi = (data) =>{
	return uni.$ajax.post({
		url: 'mp/phoneLogin',
		data: data
	})
}

export const sendValidateCodeApi = (data) =>{
	return uni.$ajax.get({
		url: `mp/sendValidateCode?`,
		data : data
	})
}

