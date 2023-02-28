import {getBaseUrl,requestUtil,getUserProfile,getWxLogin} from '../utils/requestUtils';
import regeneratorRuntime, { async } from '../lib/runtime/runtime';

export const getUserInfoApi = (data) =>{
	return new Promise((resolve, reject) => {
	    requestUtil({
	        url: 'mp/user/getUserInfo?',
	        method: 'get',
	        data:data
	    }).then((res) => {
	        console.log(res.data)
	        resolve(res)
	    }).catch((err) => {
	        reject(err)
	    })
	});
}