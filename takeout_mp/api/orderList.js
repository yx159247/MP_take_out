import regeneratorRuntime, { async } from '../lib/runtime/runtime';

export const addOrderApi = (data) =>{
	return uni.$ajax.post({
		url: 'mp/orders/submit',
		data: data
	})
}

export const orderListApi = () =>{
	return uni.$ajax.get({
		url: 'mp/orders/list',
	})
}
export const orderPagingApi = (data) =>{

	 return uni.$ajax.get({
		url: 'mp/orders/page',
		data:{...data},
	})

}
export const orderAgainApi = (data) =>{
	return uni.$ajax.post({
		url: 'mp/orders/again',
		data: data
	})
}
export const deleteOrderApi = (id) =>{
	return uni.$ajax.delete({
		url: 'mp/orders',
		data: id
	})
}

