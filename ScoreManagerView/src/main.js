// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import iView from 'iview'
import 'iview/dist/styles/iview.css' // 使用 CSS
import http from './utils/http'
import store from './store/store';
import moment from 'moment'

Vue.use(iView)
Vue.config.productionTip = false
Vue.prototype.$http = http
Vue.use(store);
/* eslint-disable no-new */
Vue.prototype.$moment = moment

new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})
