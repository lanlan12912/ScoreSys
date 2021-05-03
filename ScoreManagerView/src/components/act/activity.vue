<template>
    <div>
        <Row span ='2'>
            <Form  ref="filterItem"  :label-width="60" class="filter">
                <FormItem label='活动名称' prop="userNumber">
                    <Input type="text"></Input>
                </FormItem>
                <FormItem label='主办方' prop="userName" >
                    <Input type="text"></Input>
                </FormItem>
                <FormItem label="活动状态" prop="departmentIds">
                    <Dropdown ref="depart" trigger="click" placement="bottom-start" style="width:100%" >
                    <Input  />
                    <DropdownMenu slot="list">
                        <Tree style="line-height: 1.5;float: left; width:100%;height:50%; padding:2px;overflow:auto" 
                        ></Tree>
                    </DropdownMenu>
                </Dropdown>
                </FormItem>
                <FormItem label="活动等级" prop="userState" >
                    <Select  readonly style="text-align:left">
                        <Option value="START" key="START">启用</Option>
                        <Option value="STOP" key="STOP">停用</Option>
                    </Select>
                </FormItem>
                <FormItem :label-width='30'>
                    <Button type="primary" size="small" icon="ios-search-outline" >查询</Button>
                    <Button type="primary" size="small" icon="ios-refresh">重置</Button>
                </FormItem>
            </Form>
        </Row>
        <div class="activity">
            <Row class="actRow">
                <Card v-for="(item,index) in actList"  :key="item.id" class="actCard" :class="item.actState == 'NOTSTART'?'notStart':(item.actState == 'ONGOING'?'onGoing':'ended')">
                    <p slot="title"><Icon type="ios-film-outline"></Icon>{{item.actName}}</p>
                </Card>
            </Row>
        </div>
        <Row span='2' class="page">
            <Page :current="current" :total="total" :page-size="limit" show-total @on-change="quUserList"></Page>
        </Row>
    </div>
</template>
<script>
import '../../less/common.less'
export default {
    name:"actlist",
    data(){
        return {
            actList:[
                {
                    id:'1',
                    actName:'hello',
                    actState:'NOTSTART'
                },
                {
                    id:'2',
                    actName:'hello',
                    actState:'ONGOING'
                },
                {
                    id:'3',
                    actName:'hello',
                    actState:'NOTSTART'
                },
                {
                    id:'4',
                    actName:'hello',
                    actState:'ONGOING'
                },
                {
                    id:'5',
                    actName:'hello',
                    actState:'NOTSTART'
                },
                {
                    id:'6',
                    actName:'hello',
                    actState:'ENDED'
                },
                {
                    id:'7',
                    actName:'hello',
                    actState:'ENDED'
                },
                {
                    id:'8',
                    actName:'hello',
                    actState:'ENDED'
                },
                {
                    id:'9',
                    actName:'hello',
                    actState:'ONGOING'
                },
            ],
        }
    },
    mounted(){

    },
    methods:{

    }

}
</script>
<style lang="less" scoped>
.activity{
    padding: 0px 10px 10px;
    margin: 5px 10px 10px;
    background: #fff;
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