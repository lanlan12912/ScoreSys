import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

export default new Router({
  routes: [
    
    { path: '/', redirect: '/login' },
    {
      path: '/login',
      name: 'login',
      component:() => import('@/components/Login')
    },
    {
      path: '*',
      redirect: {
        path: '/404'
      }
    },
    {
      path: '/404',
      meta: {
        title: '404'
      },
      component: resolve => require(['@/components/404.vue'], resolve)
    }
  ]
})
