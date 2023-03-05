// const baseUrl = "http://localhost:8081/api/";
const baseUrl = "http://192.168.76.200:8081/api/";

export const getBaseUrl = () =>{
	return baseUrl;
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