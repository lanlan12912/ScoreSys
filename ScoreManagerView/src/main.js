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
const viewConfig = require('../static/view.config.json')
import Viewer from 'v-viewer'
import 'viewerjs/dist/viewer.css'
Vue.use(Viewer)
Viewer.setDefaults({
  Options: { 
    'inline': true,
     'button': true, 
     'navbar': true, 
     'title': true, 
     'toolbar': true, 
     'tooltip': true, 
     'movable': true, 
     'zoomable': true, 
     'rotatable': true, 
     'scalable': true, 
     'transition': true, 
     'fullscreen': true,
      'keyboard': true, 
      'url': 'data-source'
     }
})

Vue.use(iView)
Vue.config.productionTip = false
Vue.prototype.$http = http
Vue.use(store);
/* eslint-disable no-new */
Vue.prototype.$moment = moment
router.options.base=viewConfig['context_path'];
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})
