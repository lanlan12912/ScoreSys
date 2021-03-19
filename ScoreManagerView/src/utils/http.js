import axios from 'axios';

var instance = axios.create({
  baseURL: '/scoresys.api',
  timeout: 60000,
  headers: {
    'Content-Type': 'application/json;charset=UTF-8'
  }
});


//http request 拦截器
instance.interceptors.request.use(
  config => {
    // const token = getCookie('名称');注意使用的时候需要引入cookie方法，推荐js-cookie
    config.data = JSON.stringify(config.data);
    // if(token){
    //   config.params = {'token':token}
    // }
    return config;
  },
  error => {
    return Promise.reject(err);
  }
);


//http response 拦截器
instance.interceptors.response.use(
  response => {
    if(response.data.errCode ==2){
      router.push({
        path:"/login",
        ery:{redirect:router.currentRoute.fullPath}//从哪个页面跳转qu
      })
    }
    return response;
  },
  error => {
    return Promise.reject(error)
  }
)


/**
 * 封装get方法
 * @param url
 * @param data
 * @returns {Promise}
 */

const get = (url,params) =>{
  return new Promise((resolve,reject) => {
    instance.get(url,{
      params:data
    })
    .then(response => {
      resolve(response.data);
    })
    .catch(err => {
      reject(err)
    })
  })
}


/**
 * 封装post请求
 * @param url
 * @param data
 * @returns {Promise}
 */
//  直接使用post方法，传递的参数为 data 的方式
 const post = (url,data) =>{
   return new Promise((resolve,reject) => {
    instance.post(url,data)
          .then(response => {
            resolve(response.data);
          },err => {
            reject(err)
          })
   })
 }

 /**
 * 封装put请求
 * @param url
 * @param data
 * @returns {Promise}
 */

const put = (url,data) =>{
  return new Promise((resolve,reject) => {
    instance.put(url,data)
         .then(response => {
           resolve(response.data);
         },err => {
           reject(err)
         })
  })
}

export default  {
  post:post,
  put:put,
  get :get
}

