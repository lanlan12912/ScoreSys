<template>
  <div id="app">
    <div class="login" v-if="$route.path=='/login'">
      <router-view></router-view>
    </div>
    <div class="home" v-else>
      <Home></Home>
    </div>
  </div>
</template>

<script>
import Home from './components/home'
export default {
  name: 'App',
  components:{
    Home
  }
}
</script>

<style>
#app {
  height:100vh;
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}
</style>
