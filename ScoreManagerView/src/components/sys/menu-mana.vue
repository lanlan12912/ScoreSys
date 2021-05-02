<template>
    <div>
        <Row class="menu">
            <Col span='7' class="leftTree">
                <Tree ref="menuTree" :data="menuList" @on-select-change="quMenuInfo"></Tree>
            </Col>
            <Col span='14' class="rightInfo">
                <Form  ref="menuInfo" :model="menuInfo" :rules="menuRule">
                    <FormItem label="菜单名称" :label-width="80" prop="menuName">
                        <Input v-model="menuInfo.menuName" />
                    </FormItem>
                    <FormItem label="菜单图标" :label-width="80" prop="menuIcon">
                        <Input v-model="menuInfo.menuIcon" />
                    </FormItem>
                    <FormItem label="菜单类型" :label-width="80" prop="type">
                        <Select v-model="menuInfo.type" :disabled="!modifyFlag" readonly style="text-align:left">
                            <Option value="MODULE" key="MODULE">模块</Option>
                            <Option value="PAGE" key="PAGE">页面</Option>
                        </Select>
                    </FormItem>
                    <FormItem label="菜单路径" :label-width="80" prop="menuPath" v-if="menuInfo.type == 'PAGE'">
                        <Input v-model="menuInfo.menuPath" :disabled="!modifyFlag"/>
                    </FormItem>
                    <FormItem label="上级菜单" :label-width="80" prop="parentId">
                        <Dropdown ref="dropdown" trigger="click" placement="bottom-start" style="width:100%" >
                            <Input v-model="menuInfo.parentName" readonly  :disabled="!modifyFlag" />
                            <DropdownMenu slot="list">
                                <Tree :data="menuModules" style="line-height: 1.5;  float: left; padding:4px" @on-select-change="selectNode"></Tree>
                            </DropdownMenu>
                        </Dropdown>
                    </FormItem>
                    <FormItem label="排序" :label-width="80" prop="orders">
                        <Input v-model="menuInfo.orders" type="number" />
                    </FormItem>
                </Form>
                <Button type="success" icon ='md-add' @click="reset">新增</Button>
                <Button type="primary" @click="saveMenu">保存</Button>
                <Button type="error" icon='md-trash' @click="delMenu">删除</Button>
            </Col>
        </Row>
    </div>
</template>
<script>
import '../../less/menu-mana.less'
export default {
    name:"menulist",
    data(){
        return{
            modifyFlag:true,
            menuInfo:{
                menuName:'',
                menuIcon:'',
                menuPath:'',
                type:'',
                parentId:'',
                parentName:'',
                orders:0,
            },
            menuRule:{
                menuName:[
                    {required:true, message:'请填写菜单名称', trigger:'blur'}
                ],
                menuIcon:[
                    {required:true,message:'请填写菜单图标',trigger:'blur'}
                ],
                type:[
                    {required:true, message:'请选择菜单类型', trigger:'blur'}
                ],
                menuPath:[
                    {required:true, message:'请填写菜单路径', trigger:'blur'}
                ],
                parentId:[
                    {required:false, message:'请填写选择上级菜单', trigger:'blur'}
                ],
            },
            menuModules:[],
            menuList:[],
            contextData:null,
        }
    },
    mounted(){
        this.getMenuModule();
    },
    methods:{
        selectNode(node){
            this.menuInfo.parentId = node[0].id;
            this.menuInfo.parentName = node[0].title;      
        },
        delMenu(){
            if(this.menuInfo.menuId){
                let param = {
                        menuId:this.menuInfo.menuId
                    };
                this.$http.post('/delMenu',param).then(
                    res => {
                        if(res.success){
                            this.$Message.success(res.msg);
                            this.reset();
                            this.getMenuModule();
                        }else{
                            this.$Message.error(res.msg);
                        }
                    }
                ).catch(error => {this.$Message.error("请求异常")});
            }
        },
        reset(){
            this.$refs.menuInfo.resetFields();
            this.menuInfo.menuId = '';
            this.menuInfo.parentId='';
            this.menuInfo.parentName = '';
            this.menuInfo.menuPath ='';
            this.menuInfo.orders= '';
            this.modifyFlag = true;
        },
        quMenuInfo(node){
            this.$refs.menuInfo.resetFields();
            this.modifyFlag = false;
            let param = {
                menuId:node[0].id
            }
            this.$http.post("/quMenuInfo",param).then(
                res=>{
                    if(res.success){
                        this.menuInfo= res.data;
                    }else{
                        this.$Message.error(res.msg)
                    }
                }
            ).catch(err => {this.$Message.error(err)});
        },
        getMenuModule(){
            let param = {
                type:"MODULE"
            }
            //菜单树
            this.$http.post("/getMenuModule",param).then(
                res => {
                    if(res.success){
                        this.menuModules = res.data;
                    }else{
                        this.$Message.error(res.msg)
                    }
                }
            ).catch(err => {this.$Message.error(err)});
            param = {
                type:"PAGE"
            }
            //上级菜单树
            this.$http.post("/getMenuModule",param).then(
                res => {
                    if(res.success){
                        this.menuList = res.data;
                    }else{
                        this.$Message.error(res.msg)
                    }
                }
            ).catch(err => {this.$Message.error(err)});
            
        },
        saveMenu(){
            this.$refs.menuInfo.validate(valid =>{
                if(valid){
                    let param = this.menuInfo;
                    this.$http.post("/saveMemu",param).then(
                        res =>{
                            if(res.success){
                                this.$Message.success(res.msg);
                                this.getMenuModule();  
                            }else{
                                this.$Message.error(res.msg);
                            }
                        },
                    ).catch(err => {this.$Message.error("请求异常")})
                }else{
                    this.$Message.error("信息填写不规范")
                }
            });
        }
    }
}
</script>
<style lang="less" scoped>

</style>
