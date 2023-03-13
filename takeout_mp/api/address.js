
import regeneratorRuntime, {
	async
} from '../lib/runtime/runtime';

export const getDefaultAddressApi = () => {
	return uni.$ajax.get({
		url: 'mp/addressBook/default',
	})
}
export const updateAddressApi = (data) => {
	return uni.$ajax.put({
		url: 'mp/addressBook',
		data: data
	})
}
export const addAddressApi = (data) => {
	return uni.$ajax.post({
		url: 'mp/addressBook',
		data: data
	})
}
export const deleteAddressApi = (id) => {
	return uni.$ajax.delete({
		url: 'mp/addressBook',
		data: id
	})
}
export const addressFindOneApi = (id) => {
	return uni.$ajax.get({
		url: 'mp/addressBook/' + id,
	})
}

export const addressListApi = () => {
	return uni.$ajax.get({
		url: 'mp/addressBook/list',
	})
}
export const setDefaultAddressApi = (data) => {
	return uni.$ajax.put({
		url: 'mp/addressBook/default',
		data: data
	})
}
