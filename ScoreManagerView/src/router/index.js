import Vue from 'vue'
import Router from 'vue-router'
Vue.use(Router)

export const router =  new Router({
  routes: [
    
    { path: '/', redirect: '/login' },
    {
      path: '/login',
      name: 'login',
      meta: {
        title: '综合测评管理系统'
      },
      component:() => import('@/components/Login')
    },
    {
      path: '/home', 
      name:'home',
      meta: {
        title: '首页'
      },
      component:() => import('@/components/home'),
      children:[
        {
          path:'/sys/menulist',
          name:'menulist',
          meta:{
            title:'菜单管理'
          },
          component:() =>import('@/components/sys/menu-mana')
        },
        {
          path:"/sys/rolelist",
          name:'rolelist',
          meta:{
            title:'角色管理'
          },
          component:() =>import('@/components/sys/role-mana')
        },
        {
          path:"/sys/userList",
          name:'userlist',
          meta:{
            title:'用户管理'
          },
          component:() =>import('@/components/sys/user-mana')
        },
        {
          path:"/sys/departments",
          name:'departmentlist',
          meta:{
            title:'院系管理'
          },
          component:() =>import('@/components/sys/departments-mana')
        },
        {
          path:"/act/actlist",
          name:'actlist',
          meta:{
            title:'全部活动'
          },
          component:() =>import('@/components/act/activity')
        },
        {
          path:"/act/myact",
          name:'myact',
          meta:{
            title:'我的活动'
          },
          component:() =>import('@/components/act/my-activity')
        },
        {
          path:"/act/actDetail",
          name:'actDetail',
          meta:{
            title:'活动详情'
          },
          component:() =>import('@/components/act/activity-detail')
        },
        {
          path:"/act/judgeAct",
          name:'actJudge',
          meta:{
            title:'活动审核'
          },
          component:() =>import('@/components/act/activity-judge')
        },
      ]
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
const VueRouterPush = Router.prototype.push
Router.prototype.push = function push (to) {
  return VueRouterPush.call(this, to).catch(err => err)
}

router.beforeEach((to, from, next) => {

  if (to.path === '/login') {//跳转到登陆页面放行
    next();
  } else {
    let token = window.localStorage.getItem('token');
    if (token === null || token === '') {//跳转到其他页面需要token
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