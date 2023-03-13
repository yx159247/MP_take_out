
import regeneratorRuntime, { async } from '../lib/runtime/runtime';

export const addOrderApi =(data) =>{
	return uni.$ajax.post({
		url: 'mp/orders/submit',
		data: data
	})
}