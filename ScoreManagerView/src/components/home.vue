<template>
  <div class="layout">
    <Layout>
      <Header :style="{padding: 0}" class="header">
        <Row>
          <Col span="3" class="title"><p>综合测评管理系统</p></Col>
          <Col span="1">
            <Icon @click.native="collapsedSider" :class="rotateIcon" class="colseIcon"  type="md-menu" size="24"></Icon>
          </Col>
          <Col span="4" class="rightHead">
            <Avatar icon="ios-person" size="large" :src="headAvatar" class="headAvatar" />
            <span class="name">{{currentUser.userName}}</span>
            <span  @click="loginOut" class="extSpan"><Icon type="md-power" />退出</span>
          </Col>
        </Row>
      </Header>
      <Layout style="top: 64px;bottom: 0px;left: 0px;right: 0px;">
        <Sider ref="side1" hide-trigger collapsible :collapsed-width="100" v-model="isCollapsed" class="sider">
          <Menu theme="light" mode="vertical" width="auto" :class="menuitemClasses" @on-select="changeMenu" :active-name="$route.path">
             <template v-for="(item,index) in menuList">
              <Submenu v-if="item.childPages&&item.childPages.length>0" :name="item.menuPath||item.menuName">
                  <template slot="title">
                      <Icon :type="item.menuIcon" /><span v-if="!isCollapsed">{{item.menuName}}</span>
                  </template>
                  <template v-for="(child,index) in item.childPages">
                      <MenuItem :name="child.menuPath||child.menuName"><Icon :type="child.menuIcon" /><span v-if="!isCollapsed">{{child.menuName}}</span></MenuItem>
                  </template>
              </Submenu>
              <MenuItem  v-else :name="item.menuPath" ><Icon :type="item.menuIcon" /><span v-if="!isCollapsed">{{item.menuName}}</span></MenuItem>
          </template>
          </Menu>
        </Sider>
        <Content class="Content">
          <Row class="bread">
              <TagPage></TagPage>
          </Row>
          <div id="contentView" class="content-view">
            <router-view></router-view>
          </div>
        </Content>
      </Layout>
    </Layout>
  </div>
</template>
<script>
import '../less/home.less'
import TagPage from './common/tag-page.vue'
export default {
  name:"Home",
  components:{
    TagPage
  },
  data(){
    return{
      isCollapsed:false,
      headAvatar: require("../images/loginbackgroud.jpg"),
      currentUser:'',
      menuList:[],
    }
  },
  watch:{
    $route:{
      immediate: true, // 一旦监听到路由的变化立即执行
      handler(newval,oldval){
        if(newval.path == '/home') return;
        this.$store.commit("addTag",this.$router.currentRoute);
      }   
    }
  },
  mounted(){
    this.getCurrentUser();
    this.quUserMenuList()
  },
  computed: {
    pageTagsList() {
      return this.$store.state.pageOpenedList; // 打开的页面的页面对象
    },
    rotateIcon () {
      return [
        'menu-icon',
        this.isCollapsed ? 'rotate-icon' : ''
      ];
    },
    menuitemClasses () {
      return [
        // 'menu-item',
        this.isCollapsed ? 'collapsed-menu' : ''
      ]
    }
  },
  methods:{
    changeMenu(path){
      this.$router.push({path:path})
    },
    loginOut(){
      this.$store.commit("delToken")
      this.$router.push({path:"/login"})
      this.$store.commit("removeAllTags")
    },
    collapsedSider () {
      this.$refs.side1.toggleCollapse();
    },
    saveUserInfo(){
      let user={
        userName:"张祥雨",
        userRole:"STUDENT",
        userTeleno:"1254487978",
        userDesc:"本科生"
      };
      this.$http.post("/saveUser",user).then(
        res =>{
          if(res.success){
            this.$Message.success(res.msg);
          }else{
            this.$Message.error(res.msg);
          }
        }
      )
    },
    getCurrentUser(){
      this.$http.post("/getCurrentUser").then(
        res =>{
          this.currentUser = res;
          this.$store.commit("setUser",res);
        },
        err =>{
          this.$Message.error("请求失败")
        }
      )
    },
    quUserMenuList(){
      this.$http.post("/quMemuList").then(
        res => {
          if(res.success){
            this.menuList = res.data;
          }
        }
      ).catch(err => {this.$Message.error("请求异常")})
    }
  }
}
</script>
<style lang="less" scoped>

</style>