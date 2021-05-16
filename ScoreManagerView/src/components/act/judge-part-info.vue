<template>
    <div>
        <Row span ='2'>
            <Form  ref="partFilter" v-model="partFilter" :label-width="60" class="filter">
                <FormItem label='参与人账号' :label-width='80' style="width:250px" prop="userNumber">
                    <Input v-model="partFilter.userNumber" type="text"></Input>
                </FormItem>
                <FormItem label='用户院系' prop="departIds" style="width:250px">
                    <Dropdown ref="depart" trigger="click" placement="bottom-start" style="width:100%" >
                        <Input v-model="partFilter.departNames" readonly />
                        <DropdownMenu slot="list">
                            <Tree style="line-height: 1.5;float: left; width:100%;height:50%; padding:2px;overflow:auto" 
                            :data="departList" show-checkbox @on-check-change="selectCollege"></Tree>
                        </DropdownMenu>
                    </Dropdown>
                </FormItem>
                 <FormItem label="审核状态" prop="certState" style="width:250px">
                    <Select  v-model="partFilter.certState" readonly style="text-align:left">
                        <Option v-for="item in certStates" :value="item.code" :key="item.code">{{item.name}}</Option>
                    </Select>
                </FormItem>
                <FormItem :label-width='30'>
                    <Button type="primary" size="small" icon="ios-search-outline" @click="getPartList(1)">查询</Button>
                    <Button type="primary" size="small" icon="ios-refresh" @click="resetFilter">重置</Button>
                </FormItem>
            </Form>
        </Row>
        <Row class="partList" span ='16'>
            <Table  ref="partTable" class="table" border size='small' :columns="columns" height='400' :data="partInfoList" ></Table>
        </Row>
        <Row class="page" span ='2'>
            <Page :current="current" :total="total" :page-size="limit" show-total @on-change="getPartList"></Page>
        </Row> 
    </div>
</template>
<script>
import '../../less/common.less'
export default {
    name:'JudgePartInfo',
    data(){
        return{
            departList:[],
            partFilter:{
                userNumber:'',
                departIds:'',
                certState:'',
                departNames:'',
            },
            certStates:[
                {
                    name:'审核中',
                    code:'INJUDGE'
                },
                {
                    name:'成功',
                    code:'PASS'
                },
                {
                    name:'失败',
                    code:'REFUSED'
                }
            ],
            actRanks:[],
            columns:[
                {
                    title:'用户账号',
                    key:'userNumber',
                    width:'150',
                    tooltip:true,
                },
                {
                    title:'用户名',
                    key:'userName',
                    width:'100',
                    tooltip:true,
                },
                {
                    title:'院/系名称',
                    key:'departName',
                    width:'100',
                    tooltip:true,
                },
                {
                    title:'活动名称',
                    key:'actName',
                    width:'150',
                    tooltip:true,
                },
                {
                    title:'活动等级',
                    key:'actRank',
                    width:'90'
                },
                {
                    title:'获奖级别',
                    key:'partInState',
                    width:'90'
                },
                {
                    title:'测评分数',
                    key:'score',
                    width:'90'
                },
                {
                    title:'证明材料',
                    key:'certImg',
                    width:'125',
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
                    title:'审核状态',
                    key:'certState',
                    width:'100'
                },
                {
                    title:'操作',
                    key:'action',
                    width:'110',
                    render: (h, params) => {
                        let buttons = [];
                        buttons.push(
                            h("Tooltip",{
                                    props:{
                                        content:"通过",
                                        placement: "top-end",
                                    }
                                },[
                                h('Icon', {
                                        props: {
                                            type:'md-checkmark',
                                            size:'24'
                                        },
                                        style: {
                                            color:"rgb(127, 219, 115)",
                                            marginRight: '8px'
                                        },
                                        on: {
                                            click: () => {
                                                this.passCert(params.row,true)
                                            }
                                        }
                                    }
                                )]
                            )
                        );
                        buttons.push(
                            h("Tooltip",{
                                    props:{
                                        content:"拒绝",
                                        placement: "top-end",
                                    }
                                },[
                                h('Icon', {
                                        props: {
                                            type: 'md-close',
                                            size:'24'
                                        },
                                        style: {
                                            color:"rgb(211, 110, 70)",
                                            marginRight: '8px'
                                        },
                                        on: {
                                            click: () => {
                                                this.passCert(params.row,false)
                                            }
                                        }
                                    })]
                            )
                        );
                        return h('div',buttons);
                    }
                }
            ],
            partInfoList:[],
            total:0,
            limit:10,
            current:1,
            actId:''
        }
    },
    watch:{
        $route:{
            deep: true,
            immediate: true, // 一旦监听到路由的变化立即执行
            handler(newval,oldval){
                if(newval.path == '/act/judgeCert')
                    if(this.$route.query !== null && this.$route.query !== undefined && JSON.stringify(this.$route.query)!=='{}'){
                        let actInfo = JSON.parse(this.$route.query.actInfo);
                        this.actId = actInfo.id;
                    }
            }   
        }
    },
    mounted(){
        this.getActRanks();
        this.quDepartTree();
        this.getPartList();
    },
    methods:{
        passCert(partInfo,flag){
            let param = {
                judgeFlag:flag?'PASS':'REFUSED',
                partId:partInfo.partId,
            };
            this.$http.post("/passCert",param).then(
                res =>{
                    if(res.success){
                        this.$nextTick(()=>{
                            this.getPartList(this.current);
                        })
                        this.$Message.success(res.msg);
                    }else{
                        this.$Message.error(res.msg)
                    }
                }
            ).catch(err=>{this.$Message.error("请求异常")});
        },
        resetFilter(){
            this.partFilter.userNumber='';
            this.partFilter.departIds='';
            this.partFilter.certState='';
            this.partFilter.departNames='';
            this.partFilter.departNames='';
        },
        selectCollege(nodes){
            this.partFilter.departIds = '';
            this.partFilter.departNames = '';
            nodes.forEach(element =>{
                this.partFilter.departIds = this.partFilter.departIds == ''?element.id:this.partFilter.departIds +","+element.id;
                this.partFilter.departNames = this.partFilter.departNames == ''?element.title:this.partFilter.departNames+","+element.title;
            });
        },
        quDepartTree(){
            let param = {
                departType:"CLASS"
            }
            this.$http.post("/quDepartTree",param).then(
                res => {
                    if(res.success){
                        this.departList = res.data;
                    }else{
                        this.$Message.error(res.msg)
                    }
                }
            ).catch(err => {this.$Message.error("请求异常")});
        },
        getActRanks(){
            this.$http.post("/getActRanks").then(
                res =>{
                    if(res.success){
                        this.actRanks = res.data;
                    }else{
                        this.$Message.error(res.msg)
                    }
                }
            ).catch(err=>{this.$Message.error("请求异常")});
        },
        getPartList(index){
            let param={
                start:index == undefined?this.current:index,
                limit:this.limit,
                actId:this.actId,
                userNumber:this.partFilter.userNumber,
                departIds:this.partFilter.departIds,
                certState:this.partFilter.certState
            };
            this.$http.post("/getPartList",param).then(
                res=>{
                    if(res.success){
                        this.partInfoList = res.data.content;
                        this.total = res.data.totalElements;
                    }else{
                        this.$Message.error(res.msg);
                    }
                }
            ).catch(err=>{this.$Message.error("请求异常")});
        }
    }
}
</script>
<style lang="less" scoped>
.partList{
    background: #fff;
    padding: 10px 0px;
    margin: 5px 10px 10px;
}
</style>