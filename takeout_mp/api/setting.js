
import regeneratorRuntime, { async } from '../lib/runtime/runtime';

export const getUserInfoApi = (data) =>{
	return uni.$ajax.get({
		url: 'mp/user/getUserInfo?',
		data : data
	})
}