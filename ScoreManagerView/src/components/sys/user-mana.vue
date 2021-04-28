<template>
    <div>
        <Row span ='5'>
            <Form :model="filterItem" :label-width="60" class="filter">
                <FormItem label='用户账号' prop="userNumber" class="filterItem">
                    <Input type="text" v-model ="filterItem.userNumber"></Input>
                </FormItem>
                <FormItem label='用户名' prop="userName" class="filterItem">
                    <Input type="text" v-model ="filterItem.userName"></Input>
                </FormItem>
                <FormItem label='用户角色' prop="userRole" class="filterItem">
                    <Input type="text" v-model ="filterItem.userRole"></Input>
                </FormItem>
                <FormItem label="学院名称" prop="collegeId" class="filterItem">
                    <Input type="text" v-model ="filterItem.collegeId"></Input>
                </FormItem>
                <FormItem label="院系名称" prop="departmentId" class="filterItem">
                    <Input type="text" v-model ="filterItem.departmentId"></Input>
                </FormItem>
                <FormItem label="用户状态" prop="userState" class="filterItem">
                    <Input type="text" v-model ="filterItem.userState"></Input>
                </FormItem>
                <FormItem  class="filterItem">
                    <Button type="primary" size="small" icon="ios-search-outline" class="grpBtn" @click="quUserList(1)">查询</Button>
                    <Button type="primary" size="small" icon="ios-refresh" class="grpBtn">重置</Button>
                </FormItem>
            </Form>
        </Row>
        <Row span='16' class="userList">
            <Row>
                <Button class="grpBtn" type="success" size="small" icon='md-add' @click="open(false)">新增</Button>
                <Button class="grpBtn" type="error" size="small" icon='ios-trash-outline' @click="delRoles(false)">删除</Button>
            </Row>
            <Row>
                <Table  class="table" border size='small' :columns="columns" height='434' :data="data" ></Table>
            </Row>
        </Row>
        <Row span='2' class="page">
            <Page :current="current" :total="total" :page-size="limit" show-total @on-change="getAllRolePage"></Page>
        </Row>
    </div>
</template>
<script>
import '../../less/common.less'
export default {
    name:"userlist",
    data(){
        return{
            filterItem:{
                userNumber:'',
                userName:'',
                userRole:"",
                collegeName:'',
                departmentName:'',
                userState:"",
            },
            columns:[
                {
                    type: 'selection',
                    width: 45,
                    align: 'center'
                },
                {
                    title:'用户账号',
                    key:'userNumber',
                    width:'160'
                },
                {
                    title:'用户别名',
                    key:'userName',
                    width:'100'
                },
                {
                    title:'学院名称',
                    key:'collegeName',
                    width:'100'
                },
                {
                    title:'院系名称',
                    key:'departmentName',
                    width:'100'
                },
                {
                    title:'用户状态',
                    key:'userState',
                    width:'90'
                },
                {
                    title:'用户角色',
                    key:'userRole',
                    width:'100'
                },
                {
                    title:'联系方式',
                    key:'userTeleno',
                    width:'150'
                },
                
                {
                    title:'操作',
                    key:'action',
                    width:'240',
                    render: (h, params) => {
                            return h('div', [
                                h('Button', {
                                    props: {
                                        icon:'ios-cog',
                                        type: 'primary',
                                        size: 'small'
                                    },
                                    style: {
                                        marginRight: '8px'
                                    },
                                    on: {
                                        click: () => {
                                            this.showRes(params.row)
                                        }
                                    }
                                }, '授权'),
                                h('Button', {
                                    props: {
                                        icon:'ios-brush',
                                        type: 'primary',
                                        size: 'small'
                                    },
                                    style: {
                                        marginRight: '8px'
                                    },
                                    on: {
                                        click: () => {
                                            this.open(true,params.row)
                                        }
                                    }
                                }, '修改'),
                                h('Button', {
                                    props: {
                                        icon:'ios-trash-outline',
                                        type: 'error',
                                        size: 'small'
                                    },
                                    style: {
                                        marginRight: '8px'
                                    },
                                    on: {
                                        click: () => {
                                            this.delRoles(true,params.row)
                                        }
                                    }
                                }, '删除')
                            ]);
                    }
                }
            ],
            data:[],
            total:0,
            current:1,
            limit:10
        }
    },
    mounted(){
        this.quUserList();
    },
    methods:{
        quUserList(index){
            let param = {
                start:index == undefined?this.current:index,
                limit:this.limit,
                userNumber:this.filterItem.userNumber,
                userName:this.filterItem.userName,
                userRole:this.filterItem.userRole,
                collegeId:this.filterItem.collegeId,
                departmentId:this.filterItem.departmentId,
                userState:this.filterItem.userState
            };
            this.$http.post("/quUserListByPage",param).then(
                res => {
                    if(res.success){
                        this.data = res.data.content;
                    }else{
                        this.$Message.error(res.msg)
                    }
                }
            ).catch(error => {this.$Message.success(error)});
        }
        
    }
}
</script>
<style lang="less" scoped>
.userList{
    padding: 0px 0px 10px 0px;
    margin: 5px 10px 10px;
    background: #fff;
}
.filter{
    padding: 10px 10px 10px 0px;
    margin: 5px 10px 10px;
    background: rgb(255, 255, 255);
    display: block;
    position: relative;
    zoom: 1;
    text-align: left;
}
.ivu-form-item {
    display: inline-block;
    position: relative;
    width: 230px;
    margin-left:20px;
    zoom: 1;
    vertical-align: middle;
}
</style>