<template>
    <div>
        <Row span ='2'>
            <Form  ref="filterItem" v-model="actFilter" :label-width="60" class="filter">
                <FormItem label='活动名称' prop="actName">
                    <Input v-model="actFilter.actName" type="text"></Input>
                </FormItem>
                <FormItem label='主办方' prop="actHost" >
                    <Input v-model="actFilter.actHost" type="text"></Input>
                </FormItem>
                <FormItem label="活动状态" prop="actState">
                     <Select v-model="actFilter.actState" readonly style="text-align:left">
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
            <Row v-if="actList.length<=0" class="nodata"><div>暂无数据</div></Row>
            <Row v-else class="actRow">
                <Card @click.native="toDetail(item)"
                v-for="(item,index) in actList"  :key="item.id" class="actCard" :title="item.actName"
                :class="item.actState == 'NOTSTART'?'notStart':(item.actState == 'ONGOING'?'onGoing':'ended')"
                >
                    <div class="content" >
                        <p :title="item.actHost">主办方：{{item.actHost}}</p>
                        <p :title="item.actSite">活动地点：{{item.actSite}}</p>
                        <p :title="item.actDesc" class="desc">活动描述：{{item.actDesc}}</p>
                        <span class="date">活动时间：{{item.startDate}}~{{item.endDate}}</span>
                    </div>
                </Card>
            </Row>
        </div>
        <Row span='2' class="page">
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
            actRanks:[],
            actFilter:{
                actName:'',
                actHost:'',
                actState:'',
                actRank:''
            },
            actList:[],
            current:1,
            limit:10,
            total:100,
        }
    },
    mounted(){
        this.getActRanks();
        this.getAllActList();
    },
    methods:{
        toDetail(act){
            this.$router.push({path:"/act/actDetail"});
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
        resetFilter(){
            this.$refs.filterItem.resetFields();
        },
        getAllActList(index){
            let param = {
                start:index == undefined?this.current:index,
                limit:this.limit,
                actName:this.actFilter.actName,
                actHost:this.actFilter.actName,
                actState:this.actFilter.actName,
                actRank:this.actFilter.actName,
            }
            this.$http.post("/getAllActList",param).then(
                res =>{
                    if(res.success){
                        this.actList = res.data.content;
                        this.total = res.data.totalElements;
                    }else{
                        this.$message.error(res.msg);
                    }
                }
            ).catch(err => {this.$message.error("请求异常");});
        },
    }

}
</script>
<style lang="less" scoped>
.activity{
    padding: 0px 10px 10px;
    margin: 5px 10px 10px;
    background: #fff;
    height: 420px;
    .actRow{
        padding: 10px;
        display: block;
        position: relative;
        text-align: left;
        .actCard{
            margin:5px;
            display: inline-block;
            height: 185px;
            width: 19%;
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
                .desc{
                    display: -webkit-box;
                    -webkit-box-orient: vertical;
                    -webkit-line-clamp: 1;
                    overflow: hidden;
                }
            }
        }
        .nodata{
            margin: 25%;
            text-align: center;
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