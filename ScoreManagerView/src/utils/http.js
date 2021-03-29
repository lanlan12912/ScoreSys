import axios from 'axios';
import router from '../router/index'
import {Messagae} from 'iview'

var instance = axios.create({
  baseURL: '/scoresys.api',
  timeout: 60000,
  headers: {
    'Content-Type': 'application/json;charset=UTF-8'
  },
  withCredentials: true
});


//http request 拦截器
instance.interceptors.request.use(
  config => {
    // 在请求头中加token
    if (localStorage.getItem('token')) {
      config.headers.token = localStorage.getItem('token');
    }
    //格式化请求参数
    if (config.method === 'post') {
      config.data = JSON.stringify(config.data);
    }
    return config;
  },
  error => {
    return Promise.reject(err);
  }
);


//http response 拦截器
instance.interceptors.response.use(
  response => {
    return response;
  },
  error => {
    // 服务器状态码不是200的情况
    if (error.response.status) {
      switch (error.response.status) {
        case 401:
          Messagae.error("未登录")
          router.push({
            path:"/login"
          })
          break
        case 403:
          Messagae.error("登录过期，请重新登录")
          localStorage.removeItem('token')
          setTimeout(() => {
            router.push({
              path:"/login"
            })
          }, 1000)
          break
        case 404:
          Messagae.error('网络请求不存在')
          break
        default: 
          Message.error(error.response.data.msg)
      }
      return Promise.reject(error.response)
    }
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

