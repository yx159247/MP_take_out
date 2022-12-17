const baseUrl = "http://localhost:8081/api/";
// const baseUrl = "http://192.168.65.100:8081/api/";

export const getBaseUrl = () =>{
	return baseUrl;
}
export const getWxLogin = () => {
    return new Promise((resolve, reject) => {
        wx.login({
            timeout: 5000,
            success: (res) => {
                resolve(res)
            },
            fail: (err) => {
                reject(err)
            }
        })
    });
}


export const getUserProfile = () => {
    return new Promise((resolve, reject) => {
        wx.getUserProfile({
            desc: '获取用户信息',
            success: (res) => {
                resolve(res)
                wx.setStorageSync('userInfo', res.userInfo)
            },
            fail: (err) => {
                reject(err)
            }
        })
    });
}






export const requestUtil = (params) => {
    const token = wx.getStorageSync('token')
    let header = {...params.header};
    console.log(params.url)
    if(params.url.includes("/")){
        header["token"] = wx.getStorageSync('token')
    }

    return new Promise((resolve, reject) => {
        wx.request({
            ...params,
            header ,
            url: baseUrl + params.url,
            success: (res) => {
                resolve(res.data)
            },
            fail: (err) => {
                reject(err)
            },
            complete: () => {
                
            }
        })
    });
}