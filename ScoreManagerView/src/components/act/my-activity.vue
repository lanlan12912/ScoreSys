<template>
    <div>
        <Modal
            v-model="flag"
            title="填写活动信息"
            @on-ok="ok"
            @on-cancel="cancel"
            :width="550">
            <Form ref="actInfo" :model="actInfo" :rules="actRule">
                <Row>
                    <FormItem prop="actName" :label-width="80" label='活动名称'>
                        <Input type="text" v-model="actInfo.actName"></Input>
                    </FormItem>
                </Row>
                <Row>
                    <Col span = '12'>
                        <FormItem prop="startDate" :label-width="80" label='活动时间'>
                            <DatePicker 
                            :value="actDate" 
                            type="daterange"  
                            @on-change="tiemChange" :disabled="modifyFlag"></DatePicker>
                        </FormItem>
                    </Col>
                    <Col span = '12'>
                        <FormItem prop="actSite" :label-width="80" label='活动地点'>
                            <Input type="text" v-model="actInfo.actSite"></Input>
                        </FormItem>
                    </Col>
                </Row>
                <Row>
                    <Col span = '12'>
                        <FormItem prop="actRank" :label-width="80" label='活动等级'>
                            <Select v-model="actInfo.actRank" readonly style="titem.codeext-align:left" :disabled="modifyFlag">
                                <Option v-for="item in actRanks" :value="item.code" :key="item.code">{{item.name}}</Option>
                            </Select>
                        </FormItem>
                    </Col>
                    <Col span = '12'>
                        <FormItem prop="actHost" :label-width="80" label='主办方'>
                            <Input type="text" v-model="actInfo.actHost"></Input>
                        </FormItem>
                    </Col>
                </Row>
                <Row>
                    <FormItem prop="actDesc" :label-width="80" label="活动介绍">
                        <Input type="textarea" v-model="actInfo.actDesc"></Input>
                    </FormItem>
                </Row>
            </Form>
        </Modal>
        <Row span ='2'>
            <Form ref="filterItem" v-model="actFilter" :label-width="60" class="filter">
                <FormItem label='活动名称' prop="actName">
                    <Input v-model="actFilter.actName" type="text"></Input>
                </FormItem>
                <FormItem label='主办方' prop="actHost" >
                    <Input v-model="actFilter.actHost" type="text"></Input>
                </FormItem>
                <!-- <FormItem label="活动状态" prop="actState">
                    <Select v-model="actFilter.actState"  readonly style="text-align:left">
                        <Option v-for="item in actRanks" :value="item.code" :key="item.code">{{item.name}}</Option>
                    </Select>
                </FormItem> -->
                <FormItem label="活动等级" prop="actRank" >
                    <Select v-model="actFilter.actRank"  readonly style="text-align:left">
                        <Option v-for="item in actRanks" :value="item.code" :key="item.code">{{item.name}}</Option>
                    </Select>
                </FormItem>
                <FormItem :label-width='30'>
                    <Button type="primary" size="small" icon="ios-search-outline" @click="getActs(1)">查询</Button>
                    <Button type="primary" size="small" icon="ios-refresh" @click="resetFilter">重置</Button>
                </FormItem>
            </Form>
        </Row>
        <div class="activity">
            <Tabs type="card" class="actRow" @on-click="changeTab">
                <template slot="extra" class="actBtn">
                    <Button type="success" size="small" icon='md-add' @click="open()">我要发布</Button>
                </template>
                <TabPane label="我参与的" name="myAct">
                    <Row v-if="myActs.length<=0" class="nodata">
                        <img src="../../images/nodata1.jpg"></img>
                        <h2>暂无数据</h2>
                    </Row>
                    <Card v-for="(item,index) in myActs" :title="item.actName" :key="item.id" 
                    class="actCard" :class="item.actState == 'NOTSTART'?'notStart':(item.actState == 'ONGOING'?'onGoing':'ended')"
                     @click.native="toDetail(item)"
                    >
                        <div class="content">
                            <p :title="item.actHost">主办方：{{item.actHost}}</p>
                            <p :title="item.actSite">活动地点：{{item.actSite}}</p>
                            <p class="date">开始时间：{{item.startDate}}</p>
                            <p class="date">结束时间：{{item.endDate}}</p>
                        </div>
                    </Card>
                </TabPane>
                <TabPane label="我发布的" name="releaseAct">
                    <Row v-if="releaseActs.length<=0" class="nodata" >
                        <img src="../../images/nodata1.jpg"></img>
                        <h2>暂无数据</h2>
                    </Row>
                    <Card v-for="(item,index) in releaseActs"  :key="item.id" 
                    class="actCard" :class="item.actState == 'NOTSTART'?'notStart':(item.actState == 'ONGOING'?'onGoing':'ended')"
                    >
                    <p slot="title" @click="toDetail(item)">{{item.actName}}</p>
                        <div class="content">
                            <Icon size="22" type="ios-clipboard-outline" title="修改信息" @click="modifyActInfo(item)"/>
                            <Icon size="22" type="md-trash" title="删除" @click="delActInfo(item)"/>
                            <p :title="item.actSite">活动地点：{{item.actSite}}</p>
                            <p class="date">开始时间：{{item.startDate}}</p>
                            <p class="date">结束时间：{{item.endDate}}</p>
                        </div>
                    </Card>
                </TabPane>
            </Tabs> 
        </div>
        <Row class="page">
            <Page :current="current" :total="total" :page-size="limit" show-total @on-change="getActs"></Page>
        </Row> 
    </div>
</template>
<script>
import '../../less/common.less'
export default {
    name:"actlist",
    data(){
        return {
            actFilter:{
                actName:'',
                actHost:'',
                actState:'',
                actRank:''
            },
            flag:false,
            modifyFlag:false,
            actInfo:{
                actName:'',
                actRank:'',
                actHost:'',
                startDate:'',
                endDate:'',
                actSite:'',
                actDesc:'',
            },
            actDate:[],
            actRule:{
                actName:[
                    {required:true, message:'请填写活动名称', trigger:'blur'}
                ],
                actRank:[
                    {required:true, message:'请填写活动等级', trigger:'blur'}
                ],
                actHost:[
                    {required:true, message:'请填写活动主办方', trigger:'blur'}
                ],
                startDate:[
                    {required:true, message:'请填选择开始时间', trigger:'change'}
                ],
                endDate:[
                    {required:true, message:'请填选择结束时间', trigger:'blur'}
                ],
                actSite:[
                    {required:true, message:'请填写活动地点', trigger:'blur'}
                ]
            },
            myActs:[],
            releaseActs:[],
            total:0,
            current:1,
            limit:10,
            actRanks:[],
            actType:1,
            name:'myAct',
        }
    },
    mounted(){
        this.getActRanks();
        this.getActs();
    },
    methods:{
        open(){
            this.$refs.actInfo.resetFields();
            this.flag = true;
            this.actDate = [];
        },
        ok(){
            this.flag = false;
            this.saveActInfo();
        },
        cancel(){
            this.flag = false;
            this.modifyFlag = false;
        },
        getActRanks(){
            this.$http.post("/getActRanks").then(
                res =>{
                    if(res.success){
                        this.actRanks = res.data;
                    }else{
                        this.$message.error(res.msg)
                    }
                }
            ).catch(err=>{this.$message.error("请求异常")});
        },
        saveActInfo(){
            let param = this.actInfo;
            this.$http.post("/saveActInfo",param).then(
                res =>{
                    if(res.success){
                        this.$Message.success(res.msg);
                    }else{
                        this.$Message.error(res.msg);
                    }
                }
            ).catch(err => {this.$Message.error("请求异常")});
        },
        tiemChange(event){
            this.actInfo.startDate = event[0];
            this.actInfo.endDate = event[1];
            this.actDate = event;
        },
        toDetail(act){
            act = JSON.stringify(act);
            this.$router.push({path:"/act/actDetail",query:{actInfo:act}});
        },
        modifyActInfo(act){
            this.open();
            this.actInfo = act;
            this.actDate.push(act.startDate);
            this.actDate.push(act.endDate);
            this.modifyFlag = true;
        },
        delActInfo(act){
            let param = act;
            this.$http.post("/delActInfo",param).then(
                res =>{
                    if(res.success){
                        this.$Message.success(res.msg);
                        this.getActs();
                    }else{
                        this.$Message.error(res.msg);
                    }
                }
            ).catch(err =>{this.$Message.error("请求异常")});
        },
        resetFilter(){
            this.$refs.filterItem.resetFields();
        },
        changeTab(name){
            this.name = name;
            this.current = 1;
            this.getActs();
        },
        getActs(index){
            let param = {
                start:index == undefined?this.current:index,
                limit:this.limit,
                actName:this.actFilter.actName,
                actHost:this.actFilter.actHost,
                actState:this.actFilter.actState,
                actRank:this.actFilter.actRank,
                myAct:this.name == undefined?0:(this.name == "myAct"?1:2),//0：全部活动；1：我参与的；2：我发布的；
            };
            this.$http.post("/getAllActList",param).then(
                res =>{
                    if(res.success){
                        res.data.content.forEach(element => {
                            element.startDate = this.$moment(element.startDate).format("YYYY-MM-DD");
                            element.endDate = this.$moment(element.endDate).format("YYYY-MM-DD");
                        });
                        if(this.name == 'myAct'){
                            this.myActs=res.data.content;
                        }else{
                            this.releaseActs=res.data.content;
                        }
                        this.total = res.data.totalElements;
                    }else{
                        this.$message.error(res.msg);
                    }
                }
            ).catch(err => {this.$message.error("请求异常");});
        }
    }

}
</script>
<style lang="less" scoped>
.activity{
    padding:10px 10px 5px;
    margin: 2px 10px 0px;
    background: #fff;
    height: 420px;
    .actBtn{
        margin: 2px 5px;
    }
    .actRow{
        display: block;
        position: relative;
        text-align: left;
        .nodata{
            text-align: center;
            img{
                margin: 20px;
                height: 250px;
                width: 250px;
            }
        }
        .actCard{
            margin:5px;
            display: inline-block;
            width: 19%;
            overflow: hidden;
            .title{
                text-align: left;
            }
            .content{
                height: 100%;
                width: 100%;
                .date{
                    font-size: 6px;
                    vertical-align: bottom;
                }
                p{
                    white-space:nowrap;/*只对中文起作用，强制换行*/
                    overflow: hidden; 
                    text-overflow:ellipsis;
                }
            }
        }
        .notStart{
            background: rgb(178, 180, 180);
        }
        .onGoing{
            background: rgb(133, 211, 159);
        }
        .ended{
            background: rgb(230, 117, 97);
        }
    } 
}
</style>