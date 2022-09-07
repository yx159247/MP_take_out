import {
	getBaseUrl,
	requestUtil,
	getUserProfile,
	getWxLogin
} from '../utils/requestUtils';
import regeneratorRuntime, {
	async
} from '../lib/runtime/runtime';

export const getDefaultAddressApi = () => {
	return new Promise((resolve, reject) => {
		requestUtil({
			url: 'mp/addressBook/default',
			method: 'get',
		}).then((res) => {
			console.log(res.data)
			resolve(res)
		}).catch((err) => {
			reject(err)
		})
	});
}
export const updateAddressApi = (data) => {
	return new Promise((resolve, reject) => {
		requestUtil({
			url: 'mp/addressBook',
			method: 'put',
			data: data
		}).then((res) => {
			console.log(res.data)
			resolve(res)
		}).catch((err) => {
			reject(err)
		})
	});
}
export const addAddressApi = (data) => {
	return new Promise((resolve, reject) => {
		requestUtil({
			url: 'mp/addressBook',
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
export const deleteAddressApi = (id) => {
	return new Promise((resolve, reject) => {
		requestUtil({
			url: 'mp/addressBook',
			method: 'delete',
			header: {
				"content-type": "application/x-www-form-urlencoded"
			},
			data: id
		}).then((res) => {
			console.log(res.data)
			resolve(res)
		}).catch((err) => {
			reject(err)
		})
	});
}
export const addressFindOneApi = (id) => {
	return new Promise((resolve, reject) => {
		requestUtil({
			url: 'mp/addressBook/' + id,
			method: 'get',

		}).then((res) => {
			console.log(res.data)
			resolve(res)
		}).catch((err) => {
			reject(err)
		})
	});
}

export const addressListApi = () => {
	return new Promise((resolve, reject) => {
		requestUtil({
			url: 'mp/addressBook/list',
			method: 'get',

		}).then((res) => {
			console.log(res.data)
			resolve(res)
		}).catch((err) => {
			reject(err)
		})
	});
}
export const setDefaultAddressApi = (data) => {
	return new Promise((resolve, reject) => {
		requestUtil({
			url: 'mp/addressBook/default',
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
