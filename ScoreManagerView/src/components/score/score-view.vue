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
                                @on-change="tiemChange" ></DatePicker>
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
                        <p  title="20147852369">已参与：<span>{{scoreInfo.partEdAct}}</span>&nbsp;&nbsp;&nbsp;&nbsp;项</p>
                        <p  title="20147852369">获奖：<span>{{scoreInfo.pirseAct}}</span>&nbsp;&nbsp;&nbsp;&nbsp;项</p>
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
                    <div><h2 class="title">
                        <!-- <span class="departName">全部</span> -->
                        
                        综合测评成绩排行榜</h2>
                    </div>
                    <Col span="8" class="second"><p><font size="4" color="rgb(241, 81, 81)">2</font></p><img  src="../../images/nodata1.jpg"/><p>叶兰兰</p></Col>
                    <Col span="8" class="first"><p><font size="5" color="rgb(241, 81, 81)">1</font></p><img src="../../images/nodata1.jpg"/><p>叶兰兰</p></Col>
                    <Col span="8" class="second"><p><font size="4" color="rgb(241, 81, 81)">3</font></p><img src="../../images/nodata1.jpg"/><p>叶兰兰</p></Col>
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
                            <img src="../../images/nodata1.jpg"/>
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

export default {
    name:'scoreView',
    components:{
        
    },
    data(){
        return{
            head:require('../../images/head.png'),
            userList:[
                {
                    departName:'计173-1',
                    userName:'叶兰兰',
                    score:'22'
                },
                {
                    departName:'计173-1',
                    userName:'叶兰兰',
                    score:'22'
                },
                {
                    departName:'计173-1',
                    userName:'叶兰兰',
                    score:'22'
                },
                {
                    departName:'计173-1',
                    userName:'叶兰兰',
                    score:'22'
                },
                {
                    departName:'计173-1',
                    userName:'叶兰兰',
                    score:'22'
                },
                {
                    departName:'计173-1',
                    userName:'叶兰兰',
                    score:'22'
                },
                {
                    departName:'计173-1',
                    userName:'叶兰兰',
                    score:'22'
                },
                {
                    departName:'计173-1',
                    userName:'叶兰兰',
                    score:'22'
                },
                {
                    departName:'计173-1',
                    userName:'叶兰兰',
                    score:'22'
                },
                {
                    departName:'计173-1',
                    userName:'叶兰兰',
                    score:'22'
                },
                {
                    departName:'计173-1',
                    userName:'叶兰兰',
                    score:'22'
                },
                {
                    departName:'计173-1',
                    userName:'叶兰兰',
                    score:'22'
                },
                {
                    departName:'计173-1',
                    userName:'叶兰兰',
                    score:'22'
                },
                {
                    departName:'计173-1',
                    userName:'叶兰兰',
                    score:'22'
                },
                {
                    departName:'计173-1',
                    userName:'叶兰兰',
                    score:'22'
                },
            ],
            actDate:[
                '2021-5-1',
                '2021-12-30'
            ],
            columns:[
                {
                    title:'活动名称',
                    key:'actName',
                    ellipsis:'true',
                    width:'100',
                    tooltip:true,
                },
                {
                    title:'活动等级',
                    key:'actRank',
                    width:'100',
                    tooltip:true,
                },
                {
                    title:'活动时间',
                    key:'actDate',
                    width:'100',
                    tooltip:true,
                },
                {
                    title:'获奖级别',
                    key:'partInState',
                    width:'100',
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
                    width:'100',
                    tooltip:true,
                },
                {
                    title:'审核状态',
                    key:'certState',
                    width:'100',
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
        tiemChange(event){
            this.startDate = event[0];
            this.endDate = event[1];
            this.$nextTick(()=>{
                this.getScoreInfos();
            })
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
.scoreView{
    margin: 5px 10px ;
    .myScore{
        margin: 5px 10px 5px 0px;
        width: 98%;
        height: 200px;
        background: #fff;
        padding: 10px;
        .headAvatar{
            padding: 10px;
            border-radius: 50%;
            width: 180px;
            height: 180px;
        }
        .userInfo{
            line-height:30px;
            font-size: 14px;
            text-align: left;
            padding: 20px 10px;
            p{
                white-space:nowrap;
                text-overflow:ellipsis;
                overflow: hidden;
            }
        }
    }
    .scoreDetail{
        margin: 10px 10px 5px 0px;
        width: 98%;
        height: 355px;
        background: #fff;
        .table{
            width: 100%;
        }
    }
    .rightBody{
        margin: 5px 15px 5px 0px;
        height: 566px;
        width: 100%;
        background: #fff;
        .champion{
            padding: 10px;
            background-image: linear-gradient(120deg, #c16ef8 0%, #8eb1f1 100%);
            height: 25%;
            .title{
                text-align: center;
                .departName{
                    cursor: pointer;
                    bordeR-bottom:2px solid rgb(34, 35, 36);
                }
            }
            .first{
                img{
                    border-radius: 50%;
                    height: 45px;
                    width: 45px;
                }
                .num{
                    font-size: 16px;
                }
            }
            .second{
                margin-top: 20px;
                .num{
                    font-size: 10px;
                }
                img{
                    border-radius: 50%;
                    height: 35px;
                    width: 35px; 
                }
            }
        }
        .sortList{
            width: 100%;
            overflow: auto;
            height: 75%;
            .rankRow{
                padding: 10px 5px 5px ;
                text-align: center; 
                background-image: linear-gradient(120deg, #c69feb 0%, #89afd4 100%);
            }
            .firstRank{
                background-image: linear-gradient(120deg, #eb7b48 0%, #faba66 100%);
            }
            .secondRank{
                background-image: linear-gradient(120deg, #faba66 0%, #eed47f 100%);
            }
            .thirdRank{
                background-image: linear-gradient(120deg, #eed47f 0%, #f4f5f7 100%);
            }
            .othorRank{
                background: #fff;
            }
            img{
                border-radius: 50%;
                height: 25px;
                width: 25px;
            }
        }
      
    }
}
</style>