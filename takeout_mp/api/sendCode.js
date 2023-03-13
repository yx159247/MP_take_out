
import regeneratorRuntime, { async } from '../lib/runtime/runtime';

export const updateUserInfo = (data) =>{
	return uni.$ajax.put({
		url: 'mp/user/updateUserInfo',
		data: data
	})
}