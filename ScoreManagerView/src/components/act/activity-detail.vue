<template>
    <div>
        <Col span="16" class="actDetail">
            <Row>
                <Col span="11">
                    <div class="actInfo">
                        <Row><h2 :title="actInfo.actName">{{actInfo.actName}}</h2></Row>
                        <Row>
                            <Col span="10">
                                <p><b>{{actInfo.actType=='ACT'?'获奖得分：':'职务得分：'}}</b>
                                    <p v-for="item in scoreList">
                                        <b>
                                            <img src="../../images/11.png" v-if="item.code == 'FPRISE'"/>
                                            <img src="../../images/22.png" v-if="item.code == 'SPRISE'"/>
                                            <img src="../../images/33.png" v-if="item.code == 'TPRISE'"/>
                                            <img src="../../images/33.png" v-if="item.code == 'OPRISE'"/>
                                            <img src="../../images/33.png" v-if="item.code == 'PARTINED'"/>
                                        {{item.name}}:</b>
                                        {{item.value}}
                                    </p>
                                </p>
                            </Col>
                            <Col span="14">
                                <p :title="actInfo.actJudge=='PASS'?'已通过':(actInfo.actJudge=='REFUSED'?'被拒绝':'审核中')"><b>申报状态：{{actInfo.actJudge=='PASS'?'已通过':(actInfo.actJudge=='REFUSED'?'被拒绝':'审核中')}}</b></p>
                                <p :title="actInfo.actRank"><b>{{actInfo.actType=='ACT'?'活动级别：':'团体等级：'}}{{actInfo.actRank}}</b></p>
                                <p :title="actInfo.actHost"><b>{{actInfo.actType=='ACT'?'主办方':'负责人'}}：{{actInfo.actHost}}</b></p>
                                <p :title="actInfo.crtUser"><b>发起人：{{actInfo.crtUser}}</b></p>
                                <p :title="actInfo.actSite"><b>{{actInfo.actType=='ACT'?'活动地点':'办公地点'}}：{{actInfo.actSite}}</b></p>
                                <p :title="actInfo.startDate"><b>开始时间：{{actInfo.startDate}}</b></p>
                                <p :title="actInfo.endDate"><b>结束时间：{{actInfo.endDate}}</b></p>
                            </Col>
                        </Row>
                        <Row>
                            <Button class="btns" type="info" v-if="getCurrentUser.userNumber == actInfo.crtUser" @click="compeleteImg()">完善材料</Button>
                            <Button class="btns" type="success"  @click="signAct" :disabled="!(actInfo.actState=='NOTSTART'&&(actInfo.actJudge=='PASS'&&(partInfo.partInState==''||partInfo.partInState==null)))">我要报名</Button>
                            <Button class="btns" type="success" v-if="actInfo.actType=='ACT'" @click="priseCert" :disabled="actInfo.actState!='ENDED'||partInfo.certState == 'PASS'">已参与/已获奖</Button>
                            <Button class="btns" type="success" v-if="actInfo.actType=='DUTY'" @click="priseCert" :disabled="actInfo.actState!='ENDED'||partInfo.certState == 'PASS'">上传职务证明</Button>
                            <Button class="btns" type="success" :disabled="actInfo.actJudge!='INJUDGE'" v-if="getCurrentUser.userRank == 'ADMIN'||getCurrentUser.userRank == 'TEACHER'" @click="judgeAct('PASS')">通过申报</Button>
                            <Button class="btns" type="warning" :disabled="actInfo.actJudge!='INJUDGE'" v-if="getCurrentUser.userRank == 'ADMIN'||getCurrentUser.userRank == 'TEACHER'" @click="judgeAct('REFUSED')">退回申报</Button>
                            <Button class="btns" type="info" v-if="getCurrentUser.userNumber == actInfo.crtUser" :disabled="actInfo.actJudge!='PASS'" @click="toJudgePartInfo">审核材料</Button>
                        </Row>
                    </div>
                </Col>
                <Col span="13">
                    <div class="imgLunBo">
                        <Carousel loop autoplay>
                            <CarouselItem v-if="actImgs.length<=0">
                                <img src="../../images/nodata1.jpg" />
                            </CarouselItem>
                            <CarouselItem v-if="actImgs.length>0" v-for="(item,index) in actImgs" :key="index">
                                <img :src="item"/>
                            </CarouselItem>
                        </Carousel>
                    </div>
                </Col>
            </Row>
            <Row>
                <Col span="24">
                    <div class="actDesc">
                        <h3>活动详情：</h3>
                        <p>{{actInfo.actDesc}}</p>
                    </div>
                </Col>
            </Row>
        </Col>
        <Col span="7" >
            <div class="userRank">
                <Row class="rankRow">
                    <Col span="3"><Icon type="ios-thumbs-up" size="22"/></Col>
                    <Col span="5"><Icon type="ios-person" size="22"/></Col>
                    <Col span="5"><b>班级</b></Col>
                    <Col span="5"><b>姓名</b></Col>
                    <Col span="5"><b>得分</b></Col>
                </Row>
                <Row v-for="(item,index) in partUsers" :key="index"  class="rankRow" 
                :class="index==0?'firstRank':(index==1?'secondRank':(index==2?'thirdRank':'othorRank'))" >
                    <Col span="3">
                        <p v-if="index==0"><img src="../../images/1.png"/></p>
                        <p v-else-if="index==1"><img  src="../../images/2.png"/></p>
                        <p v-else-if="index==2"><img src="../../images/3.png"/></p>
                        <p v-else>{{index+1}}</p>
                    </Col>
                    <Col span="5">
                        <img :src="item.headAvatar==null||item.headAvatar==''?head:item.headAvatar"/>
                    </Col>
                    <Col span="5">{{item.departName}}</Col>
                    <Col span="5">{{item.userName}}</Col>
                    <Col span="5">+{{item.score}}</Col>
                </Row>
            </div>
        </Col>
        <Modal
            v-model="flag"
            title="活动风采"
            @on-ok="ok"
            @on-cancel="cancel"
            :width="550"
            :height="500">
            <div class="demo-upload-list" v-if="actImgs.length>0" v-for="(item,index) in actImgs">
                <template>
                    <img :src="item">
                    <div class="demo-upload-list-cover">
                        <Icon type="ios-trash-outline" @click.native="handleRemove(item,index)"></Icon>
                    </div>
                </template>
            </div>
            <div class="demo-upload-list" v-if="uploadList.length>0" v-for="(item,index) in uploadList">
                <template>
                    <img :src="item">
                    <div class="demo-upload-list-cover">
                        <Icon type="ios-trash-outline" @click.native="remove(item,index)"></Icon>
                    </div>
                </template>
            </div>
            <Upload
                ref="upload"
                :paste="true"
                :multiple="false"
                :format="['jpg','jpeg','png']"
                :max-size="2048"
                :on-format-error="handleFormatError"
                :on-exceeded-size="handleMaxSize"
                :before-upload="handleBeforeUpload"
                type="drag"
                action=""
                style="display: inline-block;width:58px;">
                <div style="width: 58px;height:58px;line-height: 58px;">
                    <Icon type="ios-camera" size="20"></Icon>
                </div>
            </Upload>
        </Modal>
        <Modal
            v-model="modal"
            :title="actInfo.actType == 'ACT'?'职务证明':'参与/获奖上报'"
            @on-ok="ok1"
            @on-cancel="cancle1"
            :width="400">
             <Form ref="partInfo" :model="partInfo" :rules="certRules">
                <Row>
                    <FormItem prop="partInState" :label-width="80" :label="actInfo.actType=='ACT'?'获奖等级':'职务等级'">
                        <Select v-model="partInfo.partInState"  readonly style="text-align:left">
                            <Option v-for="item in scoreList" :value="item.code" :key="item.code">{{item.name}}(+{{item.value}})</Option>
                        </Select>
                    </FormItem>
                </Row>
                <Row>
                    <FormItem prop="actName" :label-width="80" label='证明图片' >
                        <div class="demo-upload-list" v-if="partInfo.certImg != '' &&partInfo.certImg != null">
                            <template >
                                <img :src="partInfo.certImg">
                            </template>
                        </div>
                        <Upload
                            ref="upload"
                            :paste="true"
                            :multiple="false"
                            :format="['jpg','jpeg','png']"
                            :max-size="2048"
                            :on-format-error="handleFormatError"
                            :on-exceeded-size="handleMaxSize"
                            :before-upload="handleBeforeUpload1"
                            type="drag"
                            action=""
                            style="display: inline-block;width:58px;">
                            <div style="width: 58px;height:58px;line-height: 58px;">
                                <Icon type="ios-camera" size="20"></Icon>
                            </div>
                        </Upload>
                    </FormItem>
                </Row>
            </Form>
        </Modal>
    </div>
</template>
<script>
import '../../less/activity-detail.less'
export default {
    name:'actDetail',
    data(){
        return{
            head:require('../../images/head.png'),
            scoreList:[],
            modal:false,
            flag:false,
            partUsers:[],
            actInfo:{
                id:'',
                actName:'',
                actHost:'',
                actDesc:'',
                crtUser:'',
                actRank:'',
                actType:''
            },
            partInfo:{
                partInState:'',
                certImg:'',
                certState:'',
            },
            uploadList:[],
            imgFile:[],
            actImgs:[],
            delImg:'',
            priseRank:[
                {
                    code:"",
                    name:''
                },
                {
                    code:"",
                    name:''
                },
                {
                    code:"",
                    name:''
                },
                {
                    code:"",
                    name:''
                },
                {
                    code:"",
                    name:''
                },
                {
                    code:"",
                    name:''
                }
            ],
            certRules:{
                partInState:[
                    {required:true, message:'请选择获奖级别', trigger:'change'}
                ],
                certImg:[
                    {required:true, message:'请上传证明材料', trigger:'blur'}
                ],
            },
            
        }
    },
    watch:{
        $route:{
            deep: true,
            immediate: true, // 一旦监听到路由的变化立即执行
            handler(newval,oldval){
                if(newval.path == '/act/actDetail')
                this.imgFile = [];
                this.uploadList = [];
                this.actImgs = [];
                    if(this.$route.query !== null && this.$route.query !== undefined && JSON.stringify(this.$route.query)!=='{}'){
                        let actInfo = JSON.parse(this.$route.query.actInfo);
                        this.actInfo.id = actInfo.id;
                        this.getActInfo(this.actInfo.id);
                        this.getPartInfo();
                    }
            }   
        }
    },
    mounted(){
        this.imgFile = [];
        this.uploadList = [];
        this.getActInfo(this.actInfo.id);
        this.getPartInfo();
        this.getActScoreSort();
    },
    computed:{
        getCurrentUser(){
            return this.$store.getters.getUser;
        }
    },
    methods:{
        getActInfo(id){
            let param = {
                id:id
            };
            this.$http.post("/getActInfo",param).then(
                res=>{
                    if(res.success){
                        this.actInfo = res.data.activity;
                        this.actInfo.startDate = this.$moment(this.actInfo.startDate).format("YYYY-MM-DD")
                        this.actInfo.endDate = this.$moment(this.actInfo.endDate).format("YYYY-MM-DD")
                        this.scoreList = res.data.scoreList;
                        this.$nextTick(()=>{
                            this.actImgs = this.actInfo.actImgs!=null&&this.actInfo.actImgs!=""?this.actInfo.actImgs.split(";"):[];
                        })
                    }else{
                        this.$Message.error(res.msg)
                    }
                }
            ).catch(err=>{this.$Message.error("请求异常")})
        },
        ok(){
            if(this.imgFile.length>0){
                let param ={
                    id:this.actInfo.id,
                    imgFile:this.imgFile
                }
                this.$http.post("/uploadImgs",param).then(
                res =>{
                    if(res.success){
                        this.actImgs = res.data;
                        return true;
                    }else{
                        this.$Message.error(res.msg);
                        return false;
                    }
                }).catch(err=>{
                    this.$Message.error("请求异常")
                    return false;
                });
            }
        },
        cancel(){
            this.imgFile = [];
            this.uploadList = [];
            this.flag = false;
        },
        compeleteImg(){
            this.imgFile = [];
            this.uploadList = [];
            this.flag = true;
        },
        handleRemove (item,index) {
            let param = {
                id:this.actInfo.id,
                actImg:item
            }
            this.$http.post("/delActImg",param).then(
                res=>{
                    if(res.success){
                        this.$nextTick(()=>{
                            this.actImgs.splice(index,1)
                        })
                        this.$Message.success(res.msg);
                    }else{
                        this.$Message.error(res.msg);
                    }
                }
            ).catch(err=>{this.$Message.error(err)});
        },
        handleFormatError (file) {
            this.$Notice.warning({
                title: '图片格式不正确',
                desc: '图片  '  + file.name + ' 格式不正确, 请上传的图片格式为 jpg / png.'
            });
        },
        handleMaxSize (file) {
            this.$Notice.warning({
                title: '图片太大了',
                desc: '图片  ' + file.name + ' 太大了，已经超过了2M.'
            });
        },
        handleBeforeUpload (file) {
            const check = this.actImgs.length < 5;
            if (!check) {
                this.$Notice.warning({title: '最多可上传 5 张图片'});
            }
            this.getBase64(file).then(
                res =>{
                    this.$nextTick(()=>{
                        this.imgFile.push(res);
                        this.uploadList.push(res);
                    })
                }
            ).catch(err => {this.$Message.error(err)});
            return check;
        },
        getBase64(file) {
            return new Promise(function(resolve, reject) {
                let reader = new FileReader();
                let imgResult = "";
                reader.readAsDataURL(file);
                reader.onload = function() {
                    imgResult = reader.result;
                };
                reader.onerror = function(error) {
                    reject(error);
                };
                reader.onloadend = function() {
                    resolve(imgResult);
                };
            });
        },
        remove(item,index){
            this.$nextTick(()=>{
                this.imgFile.splice(index,1);
                this.uploadList.splice(index,1);
            })
            
        },
        signAct(){
            let param = {
                id:this.actInfo.id
            };
            this.$http.post("/signAct",param).then(
                res=>{
                    if(res.success){
                        this.$Message.success(res.msg);
                        this.$nextTick(()=>{
                            this.getPartInfo();
                            this.getActScoreSort();
                        })
                    }else{
                        this.$Message.error(res.msg);
                    }
                }
            ).catch(err=>{this.$Message.error("请求异常")})
        },
        getPartInfo(){
            let param = {
                id:this.actInfo.id
            };
            this.$http.post("/getPartInfo",param).then(
                res=>{
                    if(res.success){
                        if(res.data!= null){
                            this.partInfo = res.data;
                        }
                    }else{
                        this.$Message.error(res.msg);
                    }
                }
            ).catch(err=>{this.$Message.error("请求异常")})
        },
        priseCert(){
            this.certImg = '';
            this.modal = true;
        },
        handleBeforeUpload1(file){
            this.getBase64(file).then(
                res =>{
                    this.$nextTick(()=>{
                        this.partInfo.certImg = res;
                    })
                    return true;
                }
            ).catch(err => {
                this.$Message.error(err)
                return false
            });
        },
        ok1(){
            if(this.partInfo.certImg==''){
                this.$Message.error("请上传图片")
                return;
            }else{
                let param = {
                    actId:this.actInfo.id,
                    certImg:this.partInfo.certImg,
                    partInState:this.partInfo.partInState
                };
                this.$http.post("/uploadCertImg",param).then(
                    res=>{
                        if(res.success){
                            this.partInfo = res.data;
                            this.$Message.success(res.msg)
                        }else{
                            this.$Message.error(res.msg);
                        }
                    }
                ).catch(err=>{this.$Message.error("请求异常")})
            }
        },
        cancle1(){
            this.$refs.partInfo.resetFields();
            this.modal = false;
        },
        judgeAct(str){
            let param={
                actId:this.actInfo.id,
                actJudge:str
            };
            this.$http.post("/judgeAct",param).then(
                res =>{
                    if(res.success){
                        this.$nextTick(()=>{
                            this.getActInfo(this.actInfo.id);
                        })
                        if(str == 'PASS'){
                            this.$Message.success('已通过审核')
                        }
                        if(str == 'REFUSED'){
                            this.$Message.success('已退回')
                        }
                    }else{
                        this.$Message.error(res.msg)
                    }
                }
            ).catch(err=>{this.$Message.error("请求异常")})
        },
        toJudgePartInfo(){
            let act = JSON.stringify(this.actInfo);
            this.$router.push({path:"/act/judgeCert",query:{actInfo:act}})
        },
        getActScoreSort(){
            let param ={
                actId:this.actInfo.id
            };
            this.$http.post("/getActScoreSort",param).then(
                res=>{
                    if(res.success){
                        this.partUsers=res.data;
                    }else{
                        this.$Message.error(res.msg)
                    }
                }
            ).catch(err=>{this.$Message.error("请求异常")})
        }
    }
}
</script>
<style lang="less" scoped>
</style>