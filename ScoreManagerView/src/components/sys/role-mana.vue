<template>
    <div class="role">
        <Row span ='2'>
            <Button class="grpBtn" type="success" size="small" icon='md-add' @click="open(false)">新增</Button>
            <Button class="grpBtn" type="error" size="small" icon='ios-trash-outline' @click="delRoles(false)">删除</Button>
        </Row>
        <Row span = '16'>
            <Table ref="roleTable" class="table" border :columns="columns" size='small' height='434' :data="data"></Table>
        </Row>
        <Row class="page" span ='2'>
            <Page :current="current" :total="total" :page-size="limit" show-total @on-change="getAllRolePage"></Page>
        </Row>
        <Modal
            v-model="modal"
            title="填写角色信息"
            @on-ok="ok"
            @on-cancel="cancel">
            <Form ref="roleInfo" class="roleInfo" :model="roleInfo" :rules="roleRule">
               <FormItem prop="roleName" :label-width="80" label='角色名称'>
                    <Input type="text" v-model="roleInfo.roleName"></Input>
                </FormItem>
                 <FormItem prop="roleDes" :label-width="80" label="角色描述">
                    <Input type="textarea" v-model="roleInfo.roleDes"></Input>
                </FormItem>
            </Form>
        </Modal>
        <Modal
            v-model="modal1"
            title="角色授权"
            @on-ok="authorization"
            @on-cancel="close">
            <Tree ref="resTree" :data="menuList" show-checkbox expand-node></Tree>
        </Modal>
    </div>
</template>
<script>
import '../../less/common.less'
export default {
    name:'rolelist',
    data(){
        return{
            roleId:'',
            menuList:[],
            roleInfo:{
                roleId:'',
                roleName:'',
                roleDes:''
            },
            roleRule:{
                roleName:[
                    {required:true, message:'请填写角色名称', trigger:'blur'}
                ],
                roleDes:[
                    {required:true, message:'请填写角色相关描述', trigger:'blur'}
                ]
            },
            modal:false,
            modal1:false,
            columns:[
                 {
                    type: 'selection',
                    width: 60,
                    align: 'center'
                },
                {
                    title:'角色名称',
                    key:'roleName',
                    width:'150'
                },
                {
                    title:'创建人',
                    key:'crtUser',
                    width:'150'
                },
                {
                    title:'角色描述',
                    key:'roleDes'
                },
                {
                    title:'创建时间',
                    key:'crtDate',
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
            limit:10,
        }
    },
    mounted(){
        this.getAllRolePage();
    },
    methods:{
        close(){
            this.modal1 = false;
        },
        showRes(role){
            this.roleId = role.roleId;
            this.modal1 = true;
            let param = {
                roleId:role.roleId
            }
            this.$http.post("/getResTree",param).then(
                res => {
                    if(res.success){
                        this.menuList = res.data;
                    }else{
                        this.$Message.error(res.msg)
                    }
                }
            ).catch(error => {this.$Message.error("请求异常")})
        },
        authorization(){
            let nodes = this.$refs.resTree.getCheckedNodes();
            let resIds = [];
            nodes.forEach(element => {
                resIds.push(element.id)
            });
            let param = {
                roleId:this.roleId ,
                resIds:resIds
            }
            this.$http.post("/authMenuToRole",param).then(
                res => {
                    if(res.success){
                        this.$Message.success(res.msg)
                    }else{
                        this.$Message.error(res.msg)
                    }
                }
            ).catch(error => {this.$Message.error("请求异常")})
        },
        open(isModify,role){
            this.modal = true;
            if(isModify){
                this.roleInfo = role;
            }else{
                this.roleInfo.roleId = '';
                this.$refs.roleInfo.resetFields();
            }
        },
        ok(){
            let param = this.roleInfo;
            this.$http.post("/addRole",param).then(
                res => {
                    if(res.success){
                        this.$Message.success(res.msg)
                        this.getAllRolePage();
                    }else{
                        this.$Message.error(res.msg)
                    }
                    this.cancel();
                }
            ).catch(err => {this.$Message.error("请求异常")});
        },
        cancel(){
            this.modal = false;
            this.getAllRolePage();
        },
        delRoles(noSelect,role){
            let roles = [];
            if(noSelect){
                roles.push(role.roleId);
            }else{
                this.$refs.roleTable.getSelection().forEach(element => {
                        roles.push(element.roleId);
                    }
                )
            }
            let param = {
                roles:roles
            }
            this.$http.post("delRoles",param).then(
                res => {
                    if(res.success){
                        this.$Message.success(res.msg);
                        this.getAllRolePage();
                    }else{
                        this.$Message.error(res.msg)
                    }
                }
            ).catch(erro => {this.$Message.error("请求异常")})
        },
        getAllRolePage(index){
            let param = {
                start:index == undefined?this.current:index,
                limit:10,
            }
            this.$http.post('/getAllRoles',param).then(
                res =>{
                    if(res.success){
                        this.data = res.data.content;
                        this.total = res.data.totalElements;
                        this.data.forEach(element => {
                            element.crtDate = this.$moment(element.crtDate).format("YYYY-MM-DD HH:mm:ss");
                        });
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
    .role{
        background: #fff;
    }
    .roleInfo{
        margin: 20px;
    }
</style>