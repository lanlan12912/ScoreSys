import Vue from 'vue';
import Vuex from 'vuex';
Vue.use(Vuex);
const store = new Vuex.Store({
    state: {
        // 存储token
        token: localStorage.getItem('token') ? localStorage.getItem('token') : '',
        user:{},
        tagList:[],
    },
    mutations: {
        // 修改token，并将token存入localStorage
        setToken (state,token) {
            state.token = token;
            localStorage.setItem("token",token);     //存储token
        },
        delToken (state) {
            state.token = '';
            localStorage.removeItem("token");    //删除token
        },
        setUser(state,user){
            state.user = user;
        },
        addTag(state,tag){
          //先判断数组当中是否已经存在该tag
          let flag = false;
          state.tagList.forEach(item => {
            if(item.path == tag.path){
              item = tag;
              flag = true;
            }
          });
          if(!flag){
            state.tagList.push(tag);
          }
        },
        delTag(state,tag){
          for (let i = 0; i <= state.tagList.length; i++) {
            if (state.tagList[i] == tag) {
                state.tagList.splice(i, 1);
                break;
            }
          }
        },
        removeAllTags(state){
          state.tagList.splice(0,state.tagList.length)
        }
    },
    getters:{
        getUser(state){
            return state.user;
        },
        getTagList(state){
          return state.tagList;
        }
    }
});
export default {
    install: function (Vue, options) {
      Vue.prototype.$store = store
    },
    store: store
  };