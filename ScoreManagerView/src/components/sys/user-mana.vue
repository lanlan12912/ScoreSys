<template>
    <div>
        <Row span ='2'>
            <Form  ref="filterItem" :model="filterItem" :label-width="60" class="filter">
                <FormItem label='用户账号' prop="userNumber">
                    <Input type="text" v-model ="filterItem.userNumber"></Input>
                </FormItem>
                <FormItem label='用户名' prop="userName" >
                    <Input type="text" v-model ="filterItem.userName"></Input>
                </FormItem>
                <FormItem label="院/系" prop="departmentIds">
                    <Dropdown ref="depart" trigger="click" placement="bottom-start" style="width:100%" >
                        <Input v-model="filterItem.departmentNames" readonly />
                        <DropdownMenu slot="list">
                            <Tree style="line-height: 1.5;float: left; width:100%;height:50%; padding:2px;overflow:auto" 
                            :data="departList" show-checkbox @on-check-change="selectCollege"></Tree>
                        </DropdownMenu>
                    </Dropdown>
                </FormItem>
                <FormItem label="用户状态" prop="userState" >
                    <Select  v-model="filterItem.userState" readonly style="text-align:left">
                        <Option value="START" key="START">启用</Option>
                        <Option value="STOP" key="STOP">停用</Option>
                    </Select>
                </FormItem>
                <FormItem :label-width='30'>
                    <Button type="primary" size="small" icon="ios-search-outline" @click="quUserList(1)">查询</Button>
                    <Button type="primary" size="small" icon="ios-refresh" @click="resetFilter">重置</Button>
                </FormItem>
            </Form>
        </Row>
        <Row span='16' class="userList">
            <Row>
                <Button class="grpBtn" type="success" size="small" icon='md-add' @click="open(false)">新增</Button>
                <Button class="grpBtn" type="error" size="small" icon='ios-trash-outline' @click="delUsers(false)">删除</Button>
            </Row>
            <Row>
                <Table  ref="userTable" class="table" border size='small' :columns="columns" height='365' :data="data" ></Table>
            </Row>
        </Row>
        <Row span='2' class="page">
            <Page :current="current" :total="total" :page-size="limit" show-total @on-change="quUserList"></Page>
        </Row>
        <Modal
            v-model="modal"
            title="填写用户信息"
            width="570"
            @on-ok="ok"
            @on-cancel="cancel">
            <Form ref="userInfo" :model="userInfo" :rules="userRule">
                <Row>
                    <Col span = '12'>
                        <FormItem prop="userName" :label-width="80" label='用户名称'>
                            <Input type="text" v-model="userInfo.userName" :disabled="!modifyFlag"></Input>
                        </FormItem>
                    </Col>
                    <Col span = '12'>
                         <FormItem label="用户状态" :label-width="80" prop="userState">
                            <RadioGroup v-model="userInfo.userState">
                                <Radio label="START">启用</Radio>
                                <Radio label="STOP">停用</Radio>
                            </RadioGroup>
                        </FormItem>
                    </Col>
                </Row>
                <Row>
                    <Col span = '12'>
                        <FormItem label="院/系/班" :label-width="80" prop="departmentName">
                            <Dropdown ref="dropdown" placement="bottom-start" style="width:100%" >
                                <Input v-model="userInfo.departmentName" readonly :disabled="!modifyFlag"/>
                                <DropdownMenu slot="list">
                                    <Tree style="line-height: 1.5; float:left; padding:4px" :data="departList" @on-select-change="selectNode"></Tree>
                                </DropdownMenu>
                            </Dropdown>
                        </FormItem>
                    </Col>
                    <Col span = '12'>
                        <FormItem label="用户身份" :label-width="80" prop="userRank">
                            <Select :disabled="!modifyFlag" v-model="userInfo.userRank" readonly style="text-align:left">
                                <Option v-for="item in rankList" :value="item.code" :key="item,code">{{item.name}}</Option>
                            </Select>
                        </FormItem>
                    </Col>
                </Row>
                <Row>
                    <FormItem prop="userTeleno" :label-width="90" label='联系电话'>
                        <Input type="text" v-model="userInfo.userTeleno"></Input>
                    </FormItem>
                </Row>
                <Row>
                    <FormItem prop="userDesc" :label-width="80" label='描述'>
                        <Input type="textarea" v-model="userInfo.userDesc"></Input>
                    </FormItem>
                </Row>
            </Form>
        </Modal>
        <Modal
            v-model="modal1"
            title="分配角色"
            width="500"
            @on-ok="distributeRole"
            @on-cancle="back"
            >
            <template>
            <Transfer
                :data="roleList"
                :titles="titles"
                :target-keys="targetRoles"
                @on-change="handleChange"></Transfer>
            </template>
        </Modal>
    </div>
</template>
<script>
import '../../less/common.less'
export default {
    name:"userlist",
    data(){
        return{
            modal1:false,
            modifyFlag:true,
            modal:false,
            filterItem:{
                userNumber:'',
                userName:'',
                departmentIds:'',
                departmentNames:'',
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
                    width:'160',
                    tooltip:true,
                },
                {
                    title:'用户别名',
                    key:'userName',
                    width:'100',
                    tooltip:true,
                },
                {
                    title:'院/系名称',
                    key:'departmentName',
                    width:'100',
                    tooltip:true,
                },
                {
                    title:'用户身份',
                    key:'rankName',
                    width:'100'
                },
                {
                    title:'联系方式',
                    key:'userTeleno',
                    width:'150',
                    tooltip:true,
                },
                {
                    title:'描述',
                    key:'userDesc',
                    width:'210',
                    tooltip:true,
                },
                {
                    title:'用户状态',
                    key:'stateName',
                    width:'100'
                },
                {
                    title:'操作',
                    key:'action',
                    width:'140',
                    render: (h, params) => {
                        let buttons = [];
                        buttons.push(
                            h("Tooltip",{
                                    props:{
                                        content:"授权",
                                        placement: "top-end",
                                    }
                                },[
                                h('Icon', {
                                        props: {
                                            type:'ios-cog',
                                            size:'24'
                                        },
                                        style: {
                                            marginRight: '8px'
                                        },
                                        on: {
                                            click: () => {
                                                this.openRoleList(params.row.userNumber)
                                            }
                                        }
                                    }
                                )]
                            )
                        );
                        buttons.push(
                            h("Tooltip",{
                                    props:{
                                        content:"修改",
                                        placement: "top-end",
                                    }
                                },[
                                h('Icon', {
                                        props: {
                                            type: 'ios-brush',
                                            size:'24'
                                        },
                                        style: {
                                            marginRight: '8px'
                                        },
                                        on: {
                                            click: () => {
                                                this.open(true,params.row)
                                            }
                                        }
                                    })]
                            )
                        );
                        buttons.push(
                            h("Tooltip",{
                                    props:{
                                        content:"删除",
                                        placement: "top-end",
                                    }
                                },[
                                h('Icon', {
                                    props: {
                                        type: 'md-trash',
                                        size:'24',
                                        color:'red'
                                    },
                                    style: {
                                        marginRight: '8px'
                                    },
                                    on: {
                                        click: () => {
                                            this.delUsers(true,params.row)
                                        }
                                    }
                                })]
                            )
                        );
                        return h('div',buttons);
                    }
                }
            ],
            userInfo:{
                userNumber:'',
                userName:'',
                userRank:'',
                userState:'START',
                userTeleno:'',
                departmentName:'',
                departmentId:'',
                userTeleno:'',
                userDesc:''
            },
            userRule:{
                userName:[
                    {required:true, message:'请填写用户名称', trigger:'blur'}
                ],
                userRank:[
                    {required:true, message:'请填选择用户身份', trigger:'blur'}
                ],
                departmentName:[
                    {required:true, message:'请选择用户所在院系', trigger:'change'}
                ]
            },
            departList:[],
            data:[],
            total:0,
            current:1,
            limit:10,
            roleList:[],
            targetRoles:[],
            titles:["剩余角色","已分配角色"],
            userNumber:'',
            rankList:[],
        }
    },
    mounted(){
        this.getUserRanks();
        this.quUserList();
        this.quDepartTree();
    },
    methods:{
        getUserRanks(){
            this.$http.post("/getUserRanks").then(
                res =>{
                    if(res.success){
                        this.rankList = res.data;
                    }else{
                        this.$Message.error(res.msg);
                    }
                }
            ).catch(err=>{this.$Message.error("请求异常")})
        },
        selectCollege(nodes){
            this.filterItem.departmentIds = '';
            this.filterItem.departmentNames = '';
            nodes.forEach(element =>{
                this.filterItem.departmentIds = this.filterItem.departmentIds == ''?element.id:this.filterItem.departmentIds +","+element.id;
                this.filterItem.departmentNames = this.filterItem.departmentNames == ''?element.title:this.filterItem.departmentNames+","+element.title;
            });
        },
        distributeRole(){
            let param = {
                userNumber:this.userNumber,
                targetRoles:Array.from(new Set(this.targetRoles))
            };
            this.$http.post("/distrUserRoles",param).then(
                res => {
                    if(res.success){
                        this.$Message.success(res.msg);
                    }else{
                        this.$Message.error(res.msg);
                    }
                }
            ).catch(err =>{this.$Message.error("请求异常")});
        },
        handleChange(newtargetKeys){
           this.targetRoles = newtargetKeys;
        },
        back(){
            this.modal1 = false;
            this.userNUmber ='';
        },
        openRoleList(userNumber){
            this.roleList = [];
            this.targetRoles = [];
            this.modal1 = true;
            this.userNumber = userNumber;
            this.getRoleList();
        },
        render(item){
            return item.label;
        },
        selectNode(node){
            this.userInfo.departmentId = node[0].id;
            this.userInfo.departmentName = node[0].title;
        },
        resetFilter(){
            this.$refs.filterItem.resetFields();
            this.quUserList();
        },
        resetUserInfo(){
            this.$refs.userInfo.resetFields();
            this.userInfo.departmentName = '';
            this.userInfo.departmentId='';
            this.userInfo.userNumber ='';
        },
        ok(){
            this.$refs.userInfo.validate(valid =>{
                if(valid){
                    let param = this.userInfo;
                    this.$http.post("/saveUser",param).then(
                        res =>{
                            if(res.success){
                                this.$Message.success(res.msg)
                                this.quUserList();
                            }else{
                                this.$Message.error(res.msg)
                            }
                        }
                    ).catch(err => {this.$Message.error("请求异常")});
                }else{
                    this.$Message.error("信息填写不规范")
                }
            })
            
        },
        cancel(){
            this.modal = false;
            this.quUserList();
        },
        delUsers(flag,user){
            let users = [];
            if(flag){
                users.push(user.userNumber)
            }else{
                this.$refs.userTable.getSelection().forEach(element => {
                    users.push(element.userNumber);
                });
            }
            let param = {
                users:users
            };
            this.$http.post("/delUserInfos",param).then(
                res =>{
                    if(res.success){
                        this.$Message.success(res.msg);
                        this.quUserList();
                    }else{
                        this.$Message.error(res.msg);
                    }
                }
            ).catch(err => {this.$Message.error("请求异常")});
        },
        open(flag,user){
            this.modal = true;
            if(flag){
                this.userInfo = user;
                this.userInfo.userState = this.userInfo.userState == "启用"? "START":"STOP";
            }else{
                this.resetUserInfo();
            }
        },
        quUserList(index){
            let param = {
                start:index == undefined?this.current:index,
                limit:this.limit,
                userNumber:this.filterItem.userNumber,
                userName:this.filterItem.userName,
                userRole:this.filterItem.userRole,
                collegeId:this.filterItem.collegeId,
                departmentIds:this.filterItem.departmentIds,
                userState:this.filterItem.userState
            };
            this.$http.post("/quUserListByPage",param).then(
                res => {
                    if(res.success){
                        this.data = res.data.content;
                        this.total = res.data.totalElements;
                    }else{
                        this.$Message.error(res.msg)
                    }
                }
            ).catch(error => {this.$Message.success(error)});
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
            ).catch(err => {this.$$Message.error("请求异常")});
        },
        getRoleList(){
            this.$http.post("/getRolesNoPage").then(
                res => {
                    if(res.success){
                        res.data.forEach(element => {
                            let obj = {
                                key:element.roleId,
                                label:element.roleName,
                                description:element.roleDes,
                                disabled:false
                            };
                            this.roleList.push(obj);
                            this.getUDistriRoles();
                        });
                    }else{
                        this.$Message.error(res.msg);
                    }
                }
            ).catch(err =>{this.$Message.error("请求异常")});
        },
        getUDistriRoles(){
            let param = {
                userNumber:this.userNumber
            };
            this.$http.post("/getAuthResRoles",param).then(
                res =>{
                    if(res.success){
                        res.data.forEach(element => {
                             this.targetRoles.push(element.roleId);
                        });
                    }else{
                        this.$Message.success(res.msg);
                    }
                }
            ).catch(err =>{this.$Message.error("请求异常")});
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
</style>