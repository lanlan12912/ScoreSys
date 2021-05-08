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
                    <Button type="primary" size="small" icon="ios-search-outline" @click="getAllActList(1)">查询</Button>
                    <Button type="primary" size="small" icon="ios-refresh" @click="resetFilter">重置</Button>
                </FormItem>
            </Form>
        </Row>
        <div class="activity">
            <Row v-if="actList.length<=0"  class="nodata">
                <img src="../../images/nodata1.jpg"></img>
                <h2>暂无数据</h2>
            </Row>
            <Row v-else class="actRow">
                <Card @click.native="toDetail(item)"
                v-for="(item,index) in actList"  :key="item.id" class="actCard" :title="item.actName"
                :class="item.actState == 'NOTSTART'?'notStart':(item.actState == 'ONGOING'?'onGoing':'ended')"
                >
                    <div class="content" >
                        <p :title="item.actHost">主办方：{{item.actHost}}</p>
                        <p :title="item.actSite">活动地点：{{item.actSite}}</p>
                        <p :title="item.actDesc" >活动描述：{{item.actDesc}}</p>
                        <p class="date">开始时间：{{item.startDate}}</p>
                        <p class="date">结束时间：{{item.endDate}}</p>
                    </div>
                </Card>
            </Row>
        </div>
        <Row span='2' class="page">
            <Page :current="current" :total="total" :page-size="limit" show-total @on-change="getAllActList"></Page>
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
            total:0,
            current:1,
            limit:10,
        }
    },
    mounted(){
        this.getActRanks();
        this.getAllActList();
    },
    methods:{
        toDetail(act){
            // act = JSON.stringify(act);
            this.$router.push({path:"/act/actDetail",query:{actInfo:act}});
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
                actHost:this.actFilter.actHost,
                actState:this.actFilter.actState,
                actRank:this.actFilter.actRank,
                myAct:0,//0：全部活动；1：我参与的；2：我发布的；
            };
            this.$http.post("/getAllActList",param).then(
                res =>{
                    if(res.success){
                        res.data.content.forEach(element => {
                            element.startDate = this.$moment(element.startDate).format("YYYY-MM-DD");
                            element.endDate = this.$moment(element.endDate).format("YYYY-MM-DD");
                        });
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
    .nodata{
        text-align: center;
        img{
            margin: 25px;
            height: 300px;
            width: 300px;
        }
    }
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