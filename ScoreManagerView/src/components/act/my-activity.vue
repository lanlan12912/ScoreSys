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
                            @on-change="tiemChange"></DatePicker>
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
                            <Select v-model="actInfo.actRank" readonly style="titem.codeext-align:left">
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
                    <FormItem prop="actDes" :label-width="80" label="活动介绍">
                        <Input type="textarea" v-model="actInfo.actDes"></Input>
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
                <FormItem label="活动状态" prop="actState">
                    <Select v-model="actFilter.actState"  readonly style="text-align:left">
                        <Option v-for="item in actRanks" :value="item.code" :key="item.code">{{item.name}}</Option>
                    </Select>
                </Dropdown>
                </FormItem>
                <FormItem label="活动等级" prop="actRank" >
                    <Select v-model="actFilter.actRank"  readonly style="text-align:left">
                        <Option v-for="item in actRanks" :value="item.code" :key="item.code">{{item.name}}</Option>
                    </Select>
                </FormItem>
                <FormItem :label-width='30'>
                    <Button type="primary" size="small" icon="ios-search-outline" @click="getActList">查询</Button>
                    <Button type="primary" size="small" icon="ios-refresh" @click="resetFilter">重置</Button>
                </FormItem>
            </Form>
        </Row>
        <div class="activity">
            <Tabs type="card" class="actRow">
                <template slot="extra" class="actBtn">
                    <Button type="success" size="small" icon='md-add' @click="open()">我要发布</Button>
                </template>
                <TabPane label="我参与的">
                    <Card v-for="(item,index) in actList" :title="item.actName" :key="item.id" 
                    class="actCard" :class="item.actState == 'NOTSTART'?'notStart':(item.actState == 'ONGOING'?'onGoing':'ended')"
                     @click.native="toDetail(item)"
                    >
                        <div class="content">
                            <p :title="item.actHost">主办方：{{item.actHost}}</p>
                            <p :title="item.actSite">活动地点：{{item.actSite}}</p>
                            <span class="date">活动时间：{{item.startDate}}~{{item.endDate}}</span>
                        </div>
                    </Card>
                </TabPane>
                <TabPane label="我发布的">
                    <Card v-for="(item,index) in actList"  :key="item.id" 
                    class="actCard" :class="item.actState == 'NOTSTART'?'notStart':(item.actState == 'ONGOING'?'onGoing':'ended')"
                    >
                    <p slot="title" @click="toDetail(item)">{{item.actName}}</p>
                        <div class="content">
                            <Icon size="22" type="ios-clipboard-outline" title="修改信息" @click="modifyActInfo(item)"/>
                            <Icon size="22" type="md-trash" title="删除" @click="delActInfo(item)"/>
                            <p :title="item.actSite">活动地点：{{item.actSite}}</p>
                            <span class="date">活动时间：{{item.startDate}}~{{item.endDate}}</span>
                        </div>
                    </Card>
                </TabPane>
            </Tabs> 
        </div>
        <Row class="page">
            <Page :current="current" :total="total" :page-size="limit" show-total ></Page>
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
            actInfo:{
                actName:'',
                actRank:'',
                actHost:'',
                startDate:'',
                endDate:'',
                actSite:'',
                actDes:'',
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
            actList:[
                {
                    id:'1',
                    actName:'尽快VCD具备就很方便v较为方便我充电宝v京东方v背后腐败v年度分红v的v',
                    actState:'NOTSTART',
                    startDate:'2017-3-5',
                    endDate:'2017-4-10',
                    actDesc:'差点把我尽快VCD具备就很方便v较为方便我充电宝v京东方v背后腐败v年度分红v的v年底哦v和i如何v比u发表v合法的v',
                    actSite:'便v较为方便我充电宝v京东方v背后腐败v',
                },
                {
                    id:'2',
                    actName:'尽快VCD具备就很方便v较为方便我充电宝v京东方v背后腐败v年度分红v的v',
                    actState:'ONGOING',
                    startDate:'2017-3-5',
                    endDate:'2017-4-10',
                    actSite:'便v较为方便我充电宝v京东方v背后腐败v',
                    actDesc:'差点把我尽快VCD具备就很方便v较为方便我充电宝v京东方v背后腐败v年度分红v的v年底哦v和i如何v比u发表v合法的v'
                },
                {
                    id:'3',
                    actName:'较为方便我充电宝v京东方v背',
                    actState:'NOTSTART',
                    startDate:'2017-3-5',
                    endDate:'2017-4-10',
                    actSite:'便v较为方便我充电宝v京东方v背后腐败v',
                    actDesc:'差点把我尽快VCD具备就很方便v较为方便我充电宝v京东方v背后腐败v年度分红v的v年底哦v和i如何v比u发表v合法的v'
                },
                {
                    id:'4',
                    actName:'较为方便我充电宝v京东方v背',
                    actState:'ONGOING',
                    startDate:'2017-3-5',
                    endDate:'2017-4-10',
                    actDesc:'差点把我尽快VCD具备就很方便v较为方便我充电宝v京东方v背后腐败v年度分红v的v年底哦v和i如何v比u发表v合法的v'
                },
                {
                    id:'5',
                    actName:'较为方便我充电宝v京东方v背',
                    actState:'NOTSTART',
                    startDate:'2017-3-5',
                    endDate:'2017-4-10',
                    actDesc:'差点把我尽快VCD具备就很方便v较为方便我充电宝v京东方v背后腐败v年度分红v的v年底哦v和i如何v比u发表v合法的v'
                },
                {
                    id:'6',
                    actName:'hello',
                    actState:'ENDED',
                    startDate:'2017-3-5',
                    endDate:'2017-4-10',
                    actDesc:'差点把我尽快VCD具备就很方便v较为方便我充电宝v京东方v背后腐败v年度分红v的v年底哦v和i如何v比u发表v合法的v'
                },
                {
                    id:'7',
                    actName:'hello',
                    actState:'ENDED',
                    startDate:'2017-3-5',
                    endDate:'2017-4-10',
                    actDesc:'差点把我尽快VCD具备就很方便v较为方便我充电宝v京东方v背后腐败v年度分红v的v年底哦v和i如何v比u发表v合法的v'
                },
                {
                    id:'8',
                    actName:'hello',
                    actState:'ENDED',
                    startDate:'2017-3-5',
                    endDate:'2017-4-10',
                    actDesc:'差点把我尽快VCD具备就很方便v较为方便我充电宝v京东方v背后腐败v年度分红v的v年底哦v和i如何v比u发表v合法的v'
                },
                {
                    id:'9',
                    actName:'hello',
                    actState:'ONGOING',
                    startDate:'2017-3-5',
                    endDate:'2017-4-10',
                    actDesc:'差点把我尽快VCD具备就很方便v较为方便我充电宝v京东方v背后腐败v年度分红v的v年底哦v和i如何v比u发表v合法的v'
                },
            ],
            current:1,
            limit:10,
            total:10,
            actRanks:[]
        }
    },
    mounted(){
        this.getActRanks();
    },
    methods:{
        open(){
            this.$refs.actInfo.resetFields();
            this.flag = true;
        },
        ok(){
            this.flag = false;
            this.saveActInfo();
        },
        cancel(){
            this.flag = false;
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
            this.actInfo.endDate = event[0];
            this.actDate = event;
        },
        toDetail(act){
            console.log(act);
            this.$router.push({path:"/act/actDetail"});
        },
        modifyActInfo(act){
            console.log(11,act);
        },
        delActInfo(act){
            console.log(22,act);
        },
        resetFilter(){
            this.$refs.filterItem.resetFields();
        },
        getActList(index){
            console.log(this.actFilter)
        }
    }

}
</script>
<style lang="less" scoped>
.activity{
    padding:10px 10px 5px;
    margin: 2px 10px 5px;
    background: #fff;
    .actBtn{
        margin: 2px 5px;
    }
    .actRow{
        display: block;
        position: relative;
        text-align: left;
        .actCard{
            margin:8px 5px;
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
                    // word-wrap:normal;/*--只对英文起作用，以单词作为换行依据*/
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