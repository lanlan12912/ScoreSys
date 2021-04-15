<template>
    <div>
        <Row span ='6'>
            <div>
            <Form :model="filterItem" :label-width="60" class="filter">
                <FormItem label='用户账号' prop="userNumber" class="filterItem">
                    <Input></Input>
                </FormItem>
                <FormItem label='用户名' prop="userName" class="filterItem">
                    <Input></Input>
                </FormItem>
                <FormItem label='用户角色' prop="userRole" class="filterItem">
                    <Input></Input>
                </FormItem>
                <FormItem label="用户状态" prop="userState" class="filterItem">
                    <Input></Input>
                </FormItem>
            </Form>
            </div>
        </Row>
        <Row span='16'>
            <Table  class="table" border size='small' :columns="columns" height='434' :data="data" ></Table>
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
                userName:'',
                userRole:"",
                userState:"",
            },
            columns:[
                {
                    type: 'selection',
                    width: 50,
                    align: 'center'
                },
                {
                    title:'用户账号',
                    key:'userNumber',
                    width:'150'
                },
                {
                    title:'用户别名',
                    key:'userName',
                    width:'150'
                },
                {
                    title:'用户状态',
                    key:'userState',
                    width:'150'
                },
                {
                    title:'用户角色',
                    key:'userRole',
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
    mounted:{

    },
    methods:{
        
        
    }
}
</script>
<style lang="less" scoped>
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