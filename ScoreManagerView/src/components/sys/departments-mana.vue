<template>
    <Row class="menu">
        <Col span='7' class="leftTree">
            <Tree ref="departTree" :data="departList" @on-select-change="quDepartInfo"></Tree>
        </Col>
        <Col span='14' class="rightInfo">
            <Form  ref="departInfo" :model="departInfo" :rules="departRule">
                <FormItem label="院/系 名称" :label-width="80" prop="departName">
                    <Input  v-model="departInfo.departName" />
                </FormItem>
                <FormItem label="院/系 类型" :label-width="80" prop="departType">
                    <Select v-model="departInfo.departType" :disabled="!modifyFlag" readonly style="text-align:left">
                        <Option value="COLLEGE" key="COLLEGE">院</Option>
                        <Option value="DEPARTMENT" key="DEPARTMENT">系</Option>
                        <Option value="CLASS" key="CLASS">班</Option>
                    </Select>
                </FormItem>
                <FormItem label="上级 院/系" :label-width="80" prop="parentId">
                    <Dropdown ref="dropdown" trigger="click" placement="bottom-start" style="width:100%" >
                        <Input v-model="departInfo.parentName" readonly :disabled="!modifyFlag" />
                        <DropdownMenu slot="list">
                            <Tree style="line-height: 1.5;  float: left; padding:4px" :data="collegeList" @on-select-change="selectCollege"></Tree>
                        </DropdownMenu>
                    </Dropdown>
                </FormItem>
                <FormItem label="院/系 介绍" :label-width="80" prop="departDesc">
                    <Input type="textarea" v-model="departInfo.departDesc" />
                </FormItem>
            </Form>
            <Button type="success" icon ='md-add' @click="reset">新增</Button>
            <Button type="primary" @click="saveDepart">保存</Button>
            <Button type="error" icon='md-trash' @click="delDepart">删除</Button>
        </Col>
    </Row>
</template>
<script>
import '../../less/menu-mana.less'
export default {
    name:"departmentlist",
    data(){
        return{
            modifyFlag:true,
            departInfo:{
                departName:"",
                departType:"",
                parentId:"",
                parentName:"",
                departDesc:""
            },
            departRule:{
                departName:[
                    {required:true, message:'请填写院/系 名称', trigger:'blur'}
                ],
                departType:[
                    {required:true,message:'请选择院/系 类型',trigger:'blur'}
                ]
            },
            departList:[],
            collegeList:[]
        }
    },
    mounted(){
        this.quDepartTree("DEPARTMENT");
        this.quDepartTree("CLASS");
    },
    methods:{
        selectCollege(node){
            this.departInfo.parentId = node[0].id;
            this.departInfo.parentName = node[0].title;
        },
        quDepartInfo(node){
            this.modifyFlag =false;
            let param = {
                id:node[0].id
            };
            this.$http.post("/quDepartInfo",param).then(
                res => {
                    if(res.success){
                        this.departInfo = res.data;
                    }else{
                        this.$Message.error(res.msg);
                    }
                }
            ).catch(err => {this.$Message.error("请求异常")});
        },
        delDepart(){
            if(this.departInfo.id){
                let param = {
                        id:this.departInfo.id
                    };
                this.$http.post('/delDepart',param).then(
                    res => {
                        if(res.success){
                            this.$Message.success(res.msg);
                            this.reset();
                            this.quDepartTree("DEPARTMENT");
                            this.quDepartTree("CLASS");
                        }else{
                            this.$Message.error(res.msg);
                        }
                    }
                ).catch(error => {this.$Message.error("请求异常")});
            }
        },
        reset(){
            this.$refs.departInfo.resetFields();
            this.departInfo.id = '';
            this.departInfo.departName = '';
            this.departInfo.departType='';
            this.departInfo.parentId = '';
            this.departInfo.parentName = '';
            this.departInfo.departDesc ='';
            this.modifyFlag = true;
        },
        saveDepart(){
            console.log(this.departInfo);
            this.$refs.departInfo.validate(valid =>{
                if(valid){
                    let param = this.departInfo;
                    this.$http.post("/saveDepartment",param).then(
                        res =>{
                            if(res.success){
                                this.$Message.success(res.msg);
                                this.reset();
                                this.quDepartTree("DEPARTMENT");
                                this.quDepartTree("CLASS");  
                            }else{
                                this.$Message.error(res.msg);
                            }
                        },
                    ).catch(err => {this.$Message.error("请求异常")})
                }else{
                    this.$Message.error("信息填写不规范")
                }
            });
        },
        quDepartTree(type){
            let param = {
                departType:type
            }
            this.$http.post("/quDepartTree",param).then(
                res => {
                    if(res.success){
                        if(type != "CLASS"){
                            this.collegeList = res.data;
                        }else{
                            this.departList = res.data;
                        }
                    }else{
                        this.$Message.error(res.msg)
                    }
                }
            ).catch(err => {this.$$Message.error("请求异常")});
        }

    }
    
}
</script>
<style lang="less" scoped>
.department{
    background: #fff;
}
</style>>