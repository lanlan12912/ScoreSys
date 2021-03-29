import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

export const router =  new Router({
  routes: [
    
    { path: '/', redirect: '/login' },
    {
      path: '/login',
      name: 'login',
      component:() => import('@/components/Login')
    },
    {
      path: '/home', 
      name:'home',
      component:() => import('@/components/home')
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

router.beforeEach((to, from, next) => {
  if (to.path === '/login') {//跳转到登陆页面放行
    next();
  } else {
    let token = window.localStorage.getItem('token');
    if (token === 'null' || token === '') {//跳转到其他页面需要token
      next('/login');
    } else {
      next();
    }
  }
});

router.afterEach((to) => {

});

Vue.prototype.$routers = router;
export default router;