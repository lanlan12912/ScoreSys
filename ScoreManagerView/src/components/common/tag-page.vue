<template>
<div>
    <Tag :key="home.path"  :class="isActive(home) ? active : ''"  class="tagStyle">
        <router-link :to="{path:home.path}" :key="home.path" class="content" >
            <span><Icon type="md-home" />首页</span>
        </router-link>
    </Tag>
    <Tag closable
        class="tagStyle"
        v-for="(tagroute,index) in tagList" 
        :key="index" 
        :class="isActive(tagroute) ? active : ''" 
        @on-close="handleClose(tagroute)">
        <router-link :to="{path:tagroute.path}" :key="tagroute.path" class="content">
            <span>{{tagroute.meta.title}}</span>
        </router-link>
    </Tag>
</div>
</template>
<script>
export default {
    name:'TagPage',
    data(){
        return{
            active: "active",
            home:{
                path:'/home'
            }
        }
    },
    computed: {
        tagList() {
            let tagLists = this.$store.getters.getTagList;
            return tagLists;
        }
    },
    methods:{
        isActive(tagroute){
            return tagroute.path == this.$route.path;
        },
        handleClose(tagroute){
            this.$store.commit("delTag",tagroute);
            let lastTag = this.tagList[this.tagList.length-1]
            //当标签列表还有元素的时候，跳转到最后一个tag
            if(lastTag!= null && ''!== lastTag){
                console.log(11)
                this.$router.replace({path:lastTag.path})
            }else{//当已经没有元素时，跳转到首页
                console.log(22)
                this.$router.replace({path:this.home.path})
            }
        }

    }
}
</script>>
<style lang="less" scoped>
.tagStyle{
    padding: 3px;
    font-size: 13px;
    text-align: center;
    height: 30px;
    width: 90px;
}
.content{
    color: #000;
    margin: 10px;
}
.active {
    background-color: rgb(105, 176, 197);
    color: white;
    border-radius: 5px;
}
</style>