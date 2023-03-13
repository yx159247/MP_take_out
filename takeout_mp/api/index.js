import regeneratorRuntime, { async } from '../lib/runtime/runtime';

export const  addCartApi= (data) =>{
	return uni.$ajax.post({
		url: 'mp/shoppingCart/add',
		data: data
	})
}

export const  updateCartApi= (data) =>{
	return uni.$ajax.post({
		url: 'mp/shoppingCart/sub',
		data: data
	})
}
export const  clearCartApi= () =>{
	return uni.$ajax.delete({
		url: 'mp/shoppingCart/clean',
		
	})
}

export const  setMealDishDetailsApi= (id) =>{
	return uni.$ajax.get({
		url: `mp/setmeal/${id}`,
		
	})
}

export const  dishListApi= (data) =>{
	return uni.$ajax.get({
		url: 'mp/dish/list',
		data: { ...data }
	})
}
export const  setmealListApi= (data) =>{
	return uni.$ajax.get({
		url: 'mp/setmeal/list',
		data: { ...data }
	})
}
export const categoryListApi = () => {
	return uni.$ajax.get({
		url: "mp/category/list",
	})
}
export const cartListApi = (data) => {
	return uni.$ajax.get({
		url: "mp/shoppingCart/list",
		data: { ...data }
	})
}

