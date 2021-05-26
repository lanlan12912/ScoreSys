<template>
    <div class="scoreView">
        <Col span="16">
            <div class="myScore">
                <Col span="6">
                    <img :src="currentUser.headAvatar == null||currentUser.headAvatar==''?head:currentUser.headAvatar" class="headAvatar"/>
                </Col>
                <Col span="10">
                    <div  class="userInfo">
                        <div>
                            时间：<DatePicker 
                                :value="actDate" 
                                type="daterange" 
                                size="small" 
                                @on-change="tiemChange" ></DatePicker>
                                <Button shape="circle" icon="ios-search" @click="queryScore"></Button>
                        </div>
                        <p title="20147852369">账号：<span>{{scoreInfo.userNumber}}</span></p>
                        <p  title="20147852369">姓名：<span>{{scoreInfo.userName}}</span></p>
                        <p  title="20147852369">学院/系：<span>{{scoreInfo.departName}}</span></p>
                    </div>
                </Col>
                <Col span="8">
                    <div  class="userInfo">
                        <p  title="20147852369">总分：<span>{{scoreInfo.totalScore}}</span></p>
                        <p  title="20147852369">总排名：<span>{{scoreInfo.ranking}}</span>&nbsp;&nbsp;&nbsp;&nbsp;名</p>
                        <p  title="20147852369">已报名：<span>{{scoreInfo.signEdAct}}</span>&nbsp;&nbsp;&nbsp;&nbsp;项</p>
                        <p  title="20147852369">参与得分活动：<span>{{scoreInfo.partEdAct}}</span>&nbsp;&nbsp;&nbsp;&nbsp;项</p>
                        <p  title="20147852369">获奖得分活动：<span>{{scoreInfo.pirseAct}}</span>&nbsp;&nbsp;&nbsp;&nbsp;项</p>
                    </div>
                </Col>
            </div>
            <div class="scoreDetail">
                <Table  ref="userTable" class="table" border size='small'  height='355' 
                :columns="columns" :data="myDetail" ></Table>
            </div>
        </Col>
        <Col span="8">
            <div class="rightBody">
                <div class="champion">
                    <div><h2 class="title">综合测评成绩排行榜</h2>
                    </div>
                    <Col span="8" class="second">
                        <p><font size="4" color="rgb(241, 81, 81)">2</font></p>
                        <img :src="partUsers.length>=2&&partUsers[1].headAvatar!=null&&partUsers[1].headAvatar!=''?partUsers[1].headAvatar:head"/>
                        <p v-if="partUsers.length>=2">{{partUsers[1].userName}}</p>
                    </Col>
                    <Col span="8" class="first">
                        <p><font size="5" color="rgb(241, 81, 81)">1</font></p>
                        <img :src="partUsers.length>=1&&partUsers[0].headAvatar!=null&&partUsers[0].headAvatar!=''?partUsers[0].headAvatar:head"/>
                        <p v-if="partUsers.length>=1">{{partUsers[0].userName}}</p>
                    </Col>
                    <Col span="8" class="second">
                        <p><font size="4" color="rgb(241, 81, 81)">3</font></p>
                        <img :src="partUsers.length>=3&&partUsers[2].headAvatar!=null&&partUsers[2].headAvatar!=''?partUsers[2].headAvatar:head"/>
                        <p v-if="partUsers.length>=3">{{partUsers[2].userName}}</p>
                    </Col>
                </div>
                <div class="sortList">
                    <Row class="rankRow">
                        <Col span="3"><Icon type="ios-thumbs-up" size="22"/></Col>
                        <Col span="5"><Icon type="ios-person" size="22"/></Col>
                        <Col span="5"><b>班级</b></Col>
                        <Col span="5"><b>姓名</b></Col>
                        <Col span="5"><b>得分</b></Col>  
                    </Row>
                    <Row v-for="(item,index) in partUsers" :key="index"  class="rankRow" 
                    :class="index==0?'firstRank':(index==1?'secondRank':(index==2?'thirdRank':'othorRank'))" >
                        <Col span="3">
                            <p v-if="index==0"><img src="../../images/1.png"/></p>
                            <p v-else-if="index==1"><img  src="../../images/2.png"/></p>
                            <p v-else-if="index==2"><img src="../../images/3.png"/></p>
                            <p v-else>{{index+1}}</p>
                        </Col>
                        <Col span="5">
                            <img :src="item.headAvatar==null||item.headAvatar==''?head:item.headAvatar"/>
                        </Col>
                        <Col span="5">{{item.departName}}</Col>
                        <Col span="5">{{item.userName}}</Col>
                        <Col span="5">+{{item.score}}</Col>
                    </Row>
                </div>
            </div>
        </Col>
       
    </div>
</template>
<script>
import '../../less/score-view.less'
export default {
    name:'scoreView',
    components:{
        
    },
    data(){
        return{
            head:require('../../images/head.png'),
            userList:[],
            actDate:[
                '2021-5-1',
                '2021-12-30'
            ],
            columns:[
                {
                    title:'活动名称',
                    key:'actName',
                    ellipsis:'true',
                    width:'120',
                    tooltip:true,
                },
                {
                    title:'活动等级',
                    key:'actRank',
                    width:'90',
                    tooltip:true,
                },
                {
                    title:'活动时间',
                    key:'actDate',
                    width:'130',
                    tooltip:true,
                },
                {
                    title:'获奖级别',
                    key:'partInState',
                    width:'110',
                    tooltip:true,
                },
                {
                    title:'参与/获奖证明',
                    key:'certImg',
                    width:'120',
                    render:(h,params)=>{
                        return h("viewer",[
                            h("img",{
                                attrs:{
                                    src:params.row.certImg
                                },
                                style:{
                                    width:'25px',
                                    height:'25px',
                                }
                            })
                        ])
                         
                    }
                },
                {
                    title:'得分',
                    key:'score',
                    width:'70',
                    tooltip:true,
                },
                {
                    title:'审核状态',
                    key:'certState',
                    width:'90',
                    tooltip:true,
                },
            ],
            myDetail:[],
            startDate:'',
            endDate:'',
            scoreInfo:{
                userNumber:'',
                userName:'',
                departName:'',
                totalScore:'',
                ranking:'',
                signEdAct:'',
                partEdAct:'',
                pirseAct:'',
            },
            partUsers:[],           
        }
    },
    computed:{
        currentUser(){
            return this.$store.getters.getUser;
        }
    },
    mounted(){
        this.getScoreInfos();
        this.getActScoreSort();
    },
    methods:{
        queryScore(){
            this.$nextTick(()=>{
                this.getScoreInfos();
            })
        },
        tiemChange(event){
            this.startDate = event[0];
            this.endDate = event[1];
            
        },
        getScoreInfos(){
            let param = {
                startDate : this.startDate,
                endDate : this.endDate
            }
            this.$http.post("/getMyScoreInfo",param).then(
                res=>{
                    if(res.success){
                        this.myDetail =  res.data.scoreDetails;
                        this.scoreInfo = res.data.scoreInfo;
                    }else{
                        this.$Message.error(res.msg)
                    }
                }
            ).catch(err => {this.$Message.error("请求异常");});
        },
        getActScoreSort(){
            let param ={
                departId:this.departId,
            }
            this.$http.post("/getAllRank",param).then(
                res=>{
                    if(res.success){
                        this.partUsers=res.data;
                    }else{
                        this.$Message.error(res.msg)
                    }
                }
            ).catch(err=>{this.$Message.error("请求异常")})
        },
    }
}
</script>
<style lang="less" scoped>
</style>