<template>
    <div>
        <Row span ='2'>
            <Button class="grpBtn" type="success" icon='md-add'>新增</Button>
        </Row>
        <Row span = '16'>
            <Table class="table" border :columns="columns" size='small' height='434' :data="data"></Table>
        </Row>
        <Row class="page" span ='2'><Page :total="100"></Page></Row>
    </div>
</template>
<script>
export default {
    name:'rolelist',
    data(){
        return{
            columns:[
                {
                    title:'角色名称',
                    key:'roleName'
                },
                {
                    title:'创建人',
                    key:'crtUser'
                },
                {
                    title:'创建时间',
                    key:'crtDate'
                },
                {
                    title:'角色描述',
                    key:'roleDes'
                },
                {
                    title:'操作',
                    key:'action',
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
                                            this.show(params.index)
                                        }
                                    }
                                }, '授权'),
                                h('Button', {
                                    props: {
                                        icon:'ios-trash-outline',
                                        type: 'error',
                                        size: 'small'
                                    },
                                    style: {
                                        marginLeft: '8px'
                                    },
                                    on: {
                                        click: () => {
                                            this.remove(params.index)
                                        }
                                    }
                                }, '删除')
                            ]);
                        }
                }
            ],
            data:[]
        }
    },
    mounted(){
        this.getAllRolePage();
    },
    methods:{
        getAllRolePage(){
            let param = {
                start:1,
                limit:10,
            }
            this.$http.post('/getAllRoles',param).then(
                res =>{
                    if(res.success){
                        console.log(res.data)
                        this.data = res.data.content;
                    }else{
                        this.$Message.error(res.msg);
                    }
                }
            ).catch(err => {this.$Message.error(err)});
        }

    },
}
</script>
<style lang="less" scoped>
    .grpBtn{
        height: 10%;
        margin: 10px;
        float: left;
    }
    .table{
        height: 80%;
        margin: 0px 10px 10px;
    }
    .page{
        float: right;
        height: 10%;
        margin: 0px 10px 10px;
    }
</style>